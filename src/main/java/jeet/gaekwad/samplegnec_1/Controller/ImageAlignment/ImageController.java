package jeet.gaekwad.samplegnec_1.Controller.ImageAlignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar4i;
import org.bytedeco.opencv.opencv_imgproc.Vec4iVector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgproc.HoughLinesP;


@Tag(name = "Image Processing APIs", description = "Endpoint for analyzing page alignment in uploaded images using computer vision techniques.")
@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Operation(
            summary = "Analyze Page Alignment from Image",
            description = "This API detects whether a document image is properly aligned or skewed. It analyzes the uploaded image using edge detection and Hough Transform to find the average skew angle. Returns alignment status (centered, left-tilted, or right-tilted) with angle details."
    )
    @PostMapping("/alignment")
    public ResponseEntity<Map<String, Object>> analyzePageAlignment(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. Load the image
        Mat image = opencv_imgcodecs.imdecode(new Mat(file.getBytes()), opencv_imgcodecs.IMREAD_COLOR);
        if (image.empty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to load image"));
        }

        // 2. Convert to grayscale & detect edges (Canny)
        Mat gray = new Mat();
        Mat edges = new Mat();
        cvtColor(image, gray, COLOR_BGR2GRAY);
        Canny(gray, edges, 100, 200);

        // 3. Detect lines (HoughLinesP)
        Vec4iVector lines = new Vec4iVector();
        HoughLinesP(edges, lines, 1, Math.PI / 180, 80, 150, 20);

        if (lines.empty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No lines detected. Ensure the document is clear."));
        }

        // 4. Calculate average skew angle
        double totalAngle = 0;
        int userfulLineCount = 0;
        long lineCount = lines.size();
        int[] line = new int[4]; // To store [x1, y1, x2, y2]

        for (int i = 0; i < lineCount; i++) {
            Scalar4i points = lines.get(i);
            int x1 = points.get(0);
            int y1 = points.get(1);
            int x2 = points.get(2);
            int y2 = points.get(3);


            // Calculate angle relative to horizontal (0°)
            double angle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

            // Normalize angle to [-90°, 90°] to ignore vertical lines
            if (angle > 90) angle -= 180;
            if (angle < -90) angle += 180;

            if (Math.abs(angle) > 45) {
                continue; // ignore lines that are too vertical
            }

            totalAngle += angle;
            userfulLineCount++;

        }
        double avgAngle = userfulLineCount == 0 ? 0 : totalAngle / userfulLineCount;

        // 5. Determine alignment
        String alignment;
        if (avgAngle < -5) {
            alignment = "Right_SIDED (Page tilts to the left) move slightly towards the  left side";
        } else if (avgAngle > 5) {
            alignment = "Left_SIDED (Page tilts to the right) move slightly towards the  right side";
        } else {
            alignment = "CENTERED (Well-aligned)";
        }

        // 6. Build response
        Map<String, Object> response = new HashMap<>();
        response.put("alignment", alignment);
        response.put("skew_angle", avgAngle);
        response.put("line_count", lineCount);
        response.put("message",
                alignment.contains("CENTERED") ?
                        "Perfectly aligned for processing!" :
                        "Tilt detected. Adjust the document for better results.");

        return ResponseEntity.ok(response);
    }
}

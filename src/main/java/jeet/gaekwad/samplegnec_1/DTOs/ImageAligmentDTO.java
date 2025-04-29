package jeet.gaekwad.samplegnec_1.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAligmentDTO {
    @NotBlank(message = "Alignment description cannot be blank")
    private String alignment;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    @NotNull(message = "Detected angle must be provided")
    private double detectedAngle;

    @NotBlank(message = "Corrected image must be provided in Base64 format")
    private String correctedImageBase64;
}

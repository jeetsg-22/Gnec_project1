package jeet.gaekwad.samplegnec_1.Controller.ProfilePhotoCurd;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jeet.gaekwad.samplegnec_1.DTOs.ProfilePhotoResponseDTO;
import jeet.gaekwad.samplegnec_1.Service.ProfilePhotoService.ProfilePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Profile Photo Management APIs", description = "Endpoints for uploading, retrieving, updating, and deleting profile photos for user accounts.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile photo retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Profile photo not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
})
@RestController
@RequestMapping("/v1/profile-photo")
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    @Autowired
    public ProfilePhotoController(ProfilePhotoService profilePhotoService) {
        this.profilePhotoService = profilePhotoService;
    }
    @Operation(
            summary = "Upload Profile Photo",
            description = "Upload a new profile photo for the currently authenticated user. Accepts image file (JPEG/PNG)."
    )
    @PostMapping("/upload")
    public ResponseEntity<ProfilePhotoResponseDTO> uploadProfilePhoto(@RequestParam("file") MultipartFile file) throws Exception {
        ProfilePhotoResponseDTO response = profilePhotoService.uploadProfilePhoto(file);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Get Profile Photo by Account ID",
            description = "Retrieve the profile photo of a specific user account by providing the account ID. Returns image data."
    )
    @GetMapping("/get/{accountId}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Long accountId) {
        byte[] photoData = profilePhotoService.getProfilePhoto(accountId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or dynamically set depending on saved contentType
                .body(photoData);
    }
    @Operation(
            summary = "Update Profile Photo",
            description = "Update the profile photo of the currently authenticated user by uploading a new image file."
    )
    @PutMapping("/update")
    public ResponseEntity<ProfilePhotoResponseDTO> updateProfilePhoto(@RequestParam("file") MultipartFile file) throws Exception {
        ProfilePhotoResponseDTO response = profilePhotoService.updateProfilePhoto(file);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Delete Profile Photo",
            description = "Delete the profile photo associated with the currently authenticated user account."
    )
    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteProfilePhoto() {
        String result = profilePhotoService.deleteProfilePhoto();
        return ResponseEntity.ok(result);
    }

}


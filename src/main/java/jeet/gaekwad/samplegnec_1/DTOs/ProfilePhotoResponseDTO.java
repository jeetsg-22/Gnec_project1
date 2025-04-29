package jeet.gaekwad.samplegnec_1.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhotoResponseDTO {
    @NotNull(message = "Profile photo ID cannot be null")
    private Long id;

    @NotBlank(message = "Content type cannot be blank")
    private String contentType;

    @NotBlank(message = "Uploaded timestamp must be provided")
    private String uploadedAt;
}

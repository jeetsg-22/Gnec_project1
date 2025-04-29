package jeet.gaekwad.samplegnec_1.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AudioFileDTO {

    @NotNull(message = "Audio file ID cannot be null")
    private Long id;

    @NotBlank(message = "File name cannot be blank")
    private String fileName;

    @NotNull(message = "Uploaded timestamp must be provided")
    private LocalDateTime uploadedAT;
}

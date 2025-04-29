package jeet.gaekwad.samplegnec_1.DTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jeet.gaekwad.samplegnec_1.Model.AudioFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteBookDetailDTO {
    @NotNull(message = "Notebook ID cannot be null")
    private Long id;

    @NotBlank(message = "Notebook name cannot be blank")
    private String name;

    @NotNull(message = "Audio files list cannot be null")
    private List<AudioFile> audioFiles;

    @NotNull(message = "Created timestamp must be provided")
    private LocalDateTime createdAt;

}

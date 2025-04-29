package jeet.gaekwad.samplegnec_1.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteBookDTO {
    @NotNull(message = "Notebook ID cannot be null")
    private Long id;

    @NotBlank(message = "Notebook name cannot be blank")
    private String name;

    @NotNull(message = "Audio file count must be provided")
    private Integer audioFileCount;

    @NotNull(message = "Created timestamp must be provided")
    private LocalDateTime createdAt;
}

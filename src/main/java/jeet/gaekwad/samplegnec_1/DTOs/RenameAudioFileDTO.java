package jeet.gaekwad.samplegnec_1.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenameAudioFileDTO {

    @NotBlank(message = "Old file name cannot be blank")
    private String oldName;

    @NotBlank(message = "New file name cannot be blank")
    private String newName;
}

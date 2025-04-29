package jeet.gaekwad.samplegnec_1.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity representing a user's notebook for organizing audio files.")
public class NoteBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Notebookid;

    private String name;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Accounts createdBy; // Who owns this notebook

    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AudioFile> audioFiles = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}


package jeet.gaekwad.samplegnec_1.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    private String contentType; // example: image/jpeg

    private LocalDateTime uploadedAt;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts account;

}

package jeet.gaekwad.samplegnec_1.Repository;

import jeet.gaekwad.samplegnec_1.Model.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
    AudioFile findAudioFileByFilename(String audioFileName);

    AudioFile findAudioFileById(Long id);
}

package jeet.gaekwad.samplegnec_1.Service.AudioFileService;

import jeet.gaekwad.samplegnec_1.DTOs.NoteBookDetailDTO;
import jeet.gaekwad.samplegnec_1.DTOs.TTSRequestDTO;
import jeet.gaekwad.samplegnec_1.Model.AudioFile;

import java.io.IOException;
import java.util.List;

public interface AudioFileService {
    public String generateAndSaveAudioFile(TTSRequestDTO text) throws IOException;
    public AudioFile saveAudioFile(AudioFile audioFile);
    public List<AudioFile> getAudioFilesForUser();
    public AudioFile updateAudioFile(AudioFile audioFile); // internal use only
    public AudioFile deleteAudioFile(Long audioFileId);
    public AudioFile updateAudioFileName(String audioFileName,String OldFileName);
    public String ShareAudioFileLink(Long audioFileName);
    public byte[] playAudioFile(Long audioFileId);
    public NoteBookDetailDTO saveToNoteBook(Long notebookid, Long audioFileid);
}

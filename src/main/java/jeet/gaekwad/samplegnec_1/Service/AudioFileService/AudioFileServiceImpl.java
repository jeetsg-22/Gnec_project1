package jeet.gaekwad.samplegnec_1.Service.AudioFileService;

import jakarta.transaction.Transactional;
import jeet.gaekwad.samplegnec_1.Controller.TTSIntegreater.TTSController;
import jeet.gaekwad.samplegnec_1.DTOs.NoteBookDetailDTO;
import jeet.gaekwad.samplegnec_1.DTOs.TTSRequestDTO;
import jeet.gaekwad.samplegnec_1.Exception.AudioFileException;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Model.AudioFile;
import jeet.gaekwad.samplegnec_1.Model.NoteBook;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import jeet.gaekwad.samplegnec_1.Repository.AudioFileRepository;
import jeet.gaekwad.samplegnec_1.Repository.NoteBookRepository;
import jeet.gaekwad.samplegnec_1.Service.NoteBookService.NoteBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class AudioFileServiceImpl implements AudioFileService {


    private AudioFileRepository audioFileRepository;
    private TTSController ttsController;
    private AccountRepository accountRepository;
    private NoteBookRepository noteBookRepository;
    private NoteBookService noteBookService;

    @Autowired
    public AudioFileServiceImpl(AudioFileRepository audioFileRepository, TTSController ttsController, AccountRepository accountRepository, NoteBookService noteBookService , NoteBookRepository noteBookRepository) {
        this.audioFileRepository = audioFileRepository;
        this.ttsController = ttsController;
        this.accountRepository = accountRepository;
        this.noteBookService = noteBookService;
        this.noteBookRepository = noteBookRepository;
    }


    @Override
    public String generateAndSaveAudioFile(TTSRequestDTO text) throws IOException {
        ResponseEntity<byte[]> Mp3File =   ttsController.generateTTS(text);
        byte[] Mp3FileBytes = Mp3File.getBody();
        AudioFile audioFile = new AudioFile();
        audioFile.setData(Mp3FileBytes);
        audioFile.setFormat("Mp3");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Accounts accounts = accountRepository.findByUsername(username);
            audioFile.setAccount(accounts);
            audioFile.setUpLoadedAt(LocalDateTime.now());
            audioFile.setFilename(UUID.randomUUID().toString());
            accounts.getAudioFiles().add(audioFile);
            Optional<AudioFile> savedFile = Optional.of(audioFileRepository.save(audioFile));
            accountRepository.save(accounts);
            return "File Saved Successfully :  " + savedFile.get().getFilename();
        }catch (Exception e){
            e.printStackTrace();

        }
        return "File generation Failed";
    }

    //extra method for future use
    @Override
    public AudioFile saveAudioFile(AudioFile audioFile) {
        return null;
    }

    @Override
    public List<AudioFile> getAudioFilesForUser() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String username = authentication.getName();
       Accounts accounts = accountRepository.findByUsername(username);
       return accounts.getAudioFiles();
    }

    @Override
    public AudioFile updateAudioFile(AudioFile audioFile) {
        return null;
    }

    @Override
    public AudioFile deleteAudioFile(Long audioFileId) {
        AudioFile audioFile = audioFileRepository.findById(audioFileId)
                .orElseThrow(() -> new AudioFileException("Audio file not found"));

        audioFileRepository.delete(audioFile);
        return audioFile;
    }

    @Override
    public AudioFile updateAudioFileName(String audioFileName , String OldFilename) {
        AudioFile existingAudioFile = audioFileRepository.findAudioFileByFilename(audioFileName);
        if (existingAudioFile != null) {
            throw new AudioFileException("Audio file already exists");
        }
        AudioFile audiofile = audioFileRepository.findAudioFileByFilename(OldFilename);
        audiofile.setFilename(audioFileName);
        audioFileRepository.save(audiofile);
        return audiofile;
    }

    @Override
    public String ShareAudioFileLink(Long audioFileName) {
        AudioFile audioFile = audioFileRepository.findById(audioFileName).orElseThrow(
                () -> new AudioFileException("Audio file not found")
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Base URL can be injected via application.properties or hardcoded for now
        String baseUrl = "http://localhost:8080";

        // Build link
        return baseUrl + "/audio/play/" + audioFile.getFilename() + "?username=" + username;
    }

    @Override
    public byte[] playAudioFile(Long audioFileId) {
        AudioFile audioFile = audioFileRepository.findById(audioFileId)
                .orElseThrow(() -> new AudioFileException("Audio file not found"));

        byte[] raw = audioFile.getData();

        return raw;
    }

    @Override
    @Transactional
    public NoteBookDetailDTO saveToNoteBook(Long notebookid, Long audioFileid) {
         NoteBook noteBook = noteBookRepository.findById(notebookid).orElseThrow(
                 () -> new AudioFileException("Notebook not found")
         );
         AudioFile audioFile = audioFileRepository.findAudioFileById(audioFileid);
         if(audioFile == null)
             throw new AudioFileException("Audio file not found");

         audioFile.setNotebook(noteBook);
         audioFileRepository.save(audioFile);

         noteBook.getAudioFiles().add(audioFile);
         noteBookRepository.save(noteBook);
         return noteBookService.getNoteBook(notebookid);

    }
}

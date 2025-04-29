package jeet.gaekwad.samplegnec_1.Service.AudioFileService;

import jeet.gaekwad.samplegnec_1.Controller.TTSIntegreater.TTSController;
import jeet.gaekwad.samplegnec_1.DTOs.TTSRequestDTO;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Model.AudioFile;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import jeet.gaekwad.samplegnec_1.Repository.AudioFileRepository;
import jeet.gaekwad.samplegnec_1.Repository.NoteBookRepository;
import jeet.gaekwad.samplegnec_1.Service.NoteBookService.NoteBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AudioFileServiceTest {
    @InjectMocks
    private AudioFileServiceImpl audioFileService;

    @Mock
    private AudioFileRepository audioFileRepository;

    @Mock
    private TTSController ttsController;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NoteBookService noteBookService;

    @Mock
    private NoteBookRepository noteBookRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void generateAndSaveAudioFile_validText_shouldSaveFile() throws Exception {
        // Arrange
        TTSRequestDTO text = new TTSRequestDTO();
        byte[] audioData = new byte[]{1, 2, 3};
        when(ttsController.generateTTS(text)).thenReturn(ResponseEntity.ok(audioData));

        Accounts account = new Accounts();
        account.setUsername("jeet");
        account.setAudioFiles(new java.util.ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("jeet");
        when(accountRepository.findByUsername("jeet")).thenReturn(account);
        when(audioFileRepository.save(any(AudioFile.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        String result = audioFileService.generateAndSaveAudioFile(text);

        // Assert
        assertTrue(result.contains("File Saved Successfully"));
    }


    @Test
    void getAudioFilesForUser_shouldReturnListOfAudioFiles() {
        // Arrange
        Accounts account = new Accounts();
        account.setUsername("jeet");
        account.setAudioFiles(Collections.singletonList(new AudioFile()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("jeet");
        when(accountRepository.findByUsername("jeet")).thenReturn(account);

        // Act
        List<AudioFile> audioFiles = audioFileService.getAudioFilesForUser();

        // Assert
        assertEquals(1, audioFiles.size());
    }



    @Test
    void deleteAudioFile_validId_shouldDelete() {
        // Arrange
        AudioFile audioFile = new AudioFile();
        when(audioFileRepository.findById(1L)).thenReturn(Optional.of(audioFile));

        // Act
        AudioFile deletedFile = audioFileService.deleteAudioFile(1L);

        // Assert
        verify(audioFileRepository).delete(audioFile);
        assertNotNull(deletedFile);
    }

    @Test
    void deleteAudioFile_invalidId_shouldThrowException() {
        when(audioFileRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                audioFileService.deleteAudioFile(1L));
        assertEquals("Audio file not found", exception.getMessage());
    }



    @Test
    void playAudioFile_validId_shouldReturnBytes() {
        AudioFile audioFile = new AudioFile();
        audioFile.setData(new byte[]{1, 2, 3});

        when(audioFileRepository.findById(1L)).thenReturn(Optional.of(audioFile));

        byte[] result = audioFileService.playAudioFile(1L);

        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    void playAudioFile_invalidId_shouldThrowException() {
        when(audioFileRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                audioFileService.playAudioFile(1L));
        assertEquals("Audio file not found", exception.getMessage());
    }
}

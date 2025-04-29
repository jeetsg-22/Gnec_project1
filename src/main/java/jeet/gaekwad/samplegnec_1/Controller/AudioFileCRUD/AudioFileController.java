package jeet.gaekwad.samplegnec_1.Controller.AudioFileCRUD;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jeet.gaekwad.samplegnec_1.DTOs.NoteBookDetailDTO;
import jeet.gaekwad.samplegnec_1.DTOs.RenameAudioFileDTO;
import jeet.gaekwad.samplegnec_1.DTOs.TTSRequestDTO;
import jeet.gaekwad.samplegnec_1.Model.AudioFile;
import jeet.gaekwad.samplegnec_1.Service.AudioFileService.AudioFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Audio File Management APIs", description = "Endpoints for managing, generating, updating, deleting, renaming, and playing audio files.")
@RestController
@RequestMapping("/v1/audioFile")
public class AudioFileController {

    private AudioFileService audioFileService;

    @Autowired
    public AudioFileController(AudioFileService audioFileService) {
        this.audioFileService = audioFileService;
    }
    @Operation(
            summary = "Get all Audio Files",
            description = "Retrieve all audio files uploaded by the authenticated user."
    )
    @GetMapping("/allAudioFiles")
    public ResponseEntity<List<AudioFile>> getAllAudioFiles() {
        List<AudioFile> userAudioFiles = audioFileService.getAudioFilesForUser();
        return new ResponseEntity<>(userAudioFiles, HttpStatus.OK);
    }
    @Operation(
            summary = "Generate and Save Audio File",
            description = "Generate a new audio file from provided text using Text-to-Speech (TTS) and save it to the database."
    )
    @PostMapping("/generate-and-save")
    public ResponseEntity<String> generateAndSaveAudioFile(@RequestBody TTSRequestDTO text) throws IOException {
        String savedAudio = audioFileService.generateAndSaveAudioFile(text);
        return new ResponseEntity<>(savedAudio , HttpStatus.OK);
    }
    @Operation(
            summary = "Save an Audio File manually",
            description = "Save an already created AudioFile object manually into the database."
    )
    @PostMapping("/saveFile")
    public ResponseEntity<AudioFile> saveFile(@RequestBody AudioFile audioFile) {
        AudioFile newAudioFile = audioFileService.saveAudioFile(audioFile);
        return new ResponseEntity<>(newAudioFile, HttpStatus.OK);
    }
    @Operation(
            summary = "Admin: Update an Audio File",
            description = "Update an existing audio file. This operation is restricted to Admin users only."
    )
    @PutMapping("/Admin/UpdateFile")
    /*Internal use only <b>ROLE</b> <i>Admin</i> **/
    public ResponseEntity<AudioFile> updateFile(@RequestBody AudioFile audioFile) {
        AudioFile updatedAudioFile = audioFileService.updateAudioFile(audioFile);
        return new ResponseEntity<>(updatedAudioFile, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an Audio File by ID",
            description = "Delete a specific audio file by providing its ID as a query parameter."
    )
    @DeleteMapping("/deleteFile/{id}")
    public ResponseEntity<AudioFile>  deleteFile(@PathVariable("id") Long id) {
        AudioFile deletedAudioFile = audioFileService.deleteAudioFile(id);
        return new ResponseEntity<>(deletedAudioFile, HttpStatus.OK);
    }
    @Operation(
            summary = "Rename an Audio File",
            description = "Change the name of an existing audio file by providing the old and new names."
    )
    @PutMapping("/rename")
    public ResponseEntity<AudioFile> changeName(@RequestBody RenameAudioFileDTO renameAudioFileDTO) {
        String oldFileName = renameAudioFileDTO.getOldName();
        String newFileName = renameAudioFileDTO.getNewName();
        AudioFile updatedFile = audioFileService.updateAudioFileName(newFileName , oldFileName);
        return new ResponseEntity<>(updatedFile, HttpStatus.OK);
    }
    @Operation(
            summary = "Get Shareable Link for Audio File",
            description = "Retrieve a shareable link for a specific audio file by providing its ID."
    )
    @GetMapping("/link")
    public ResponseEntity<String> getLink(Long audioFileId) {
        String audioFileLink = audioFileService.ShareAudioFileLink(audioFileId);
        return new ResponseEntity<>(audioFileLink, HttpStatus.OK);
    }
    @Operation(
            summary = "Play an Audio File",
            description = "Stream and play an audio file by its ID. The file is returned with audio/mpeg content type."
    )
    @GetMapping("/play/{id}")
    public ResponseEntity<byte []> play(@PathVariable("id") Long audioFileId) {
        byte[] audioPlay = audioFileService.playAudioFile(audioFileId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentLength(audioPlay.length);
        headers.set("Content-Disposition", "inline; filename=audio.mp3");

        return new ResponseEntity<>(audioPlay,headers, HttpStatus.OK);
    }
    @Operation(
            summary = "Save Audio File into a Notebook",
            description = "Associate an existing audio file with a notebook by providing notebook ID and audio file ID."
    )
    @PostMapping("/save/{notebookid}/{audioFileid}")
    public ResponseEntity<NoteBookDetailDTO> saveToNoteBook(@PathVariable("notebookid") Long notebookid,@PathVariable("audioFileid") Long audioFileid) {
        NoteBookDetailDTO myNoteBook = audioFileService.saveToNoteBook(notebookid, audioFileid);
        return new ResponseEntity<>(myNoteBook, HttpStatus.OK);
    }

}

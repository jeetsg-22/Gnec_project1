package jeet.gaekwad.samplegnec_1.Service.NoteBookService;import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jeet.gaekwad.samplegnec_1.DTOs.NoteBookDTO;
import jeet.gaekwad.samplegnec_1.DTOs.NoteBookDetailDTO;
import jeet.gaekwad.samplegnec_1.Exception.NoteBookInvalidException;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Model.NoteBook;
import jeet.gaekwad.samplegnec_1.Repository.NoteBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

class NoteBookServiceImplTest {

    @InjectMocks
    private NoteBookServiceImpl noteBookService;

    @Mock
    private NoteBookRepository notebookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void getNoteBook_validId_shouldReturnDetailDTO() {
        NoteBook notebook = new NoteBook();
        notebook.setNotebookid(1L);
        notebook.setName("Test Notebook");

        when(notebookRepository.findById(1L)).thenReturn(Optional.of(notebook));

        NoteBookDetailDTO dto = noteBookService.getNoteBook(1L);

        assertNotNull(dto);
        assertEquals("Test Notebook", dto.getName());
    }

    @Test
    void getNoteBook_invalidId_shouldThrowException() {
        when(notebookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoteBookInvalidException.class, () -> noteBookService.getNoteBook(1L));
    }



    @Test
    void listNoteBooks_shouldReturnListOfDTOs() {
        // Arrange
        NoteBook notebook = new NoteBook();
        notebook.setNotebookid(1L);
        notebook.setName("Test Notebook");
        notebook.setAudioFiles(Collections.emptyList()); // important to set this!

        when(notebookRepository.findByCreatedBy_AccountId(1L))
                .thenReturn(Collections.singletonList(notebook));

        List<NoteBookDTO> notebooks = noteBookService.listNoteBooks(1L);

        assertEquals(1, notebooks.size());
        assertEquals("Test Notebook", notebooks.get(0).getName());
    }


    @Test
    void createNoteBook_shouldSaveAndReturnDTO() {
        Accounts account = new Accounts();
        account.setAccountId(1L);

        NoteBook notebook = new NoteBook();
        notebook.setNotebookid(1L);
        notebook.setName("New Notebook");
        notebook.setCreatedBy(account);

        when(notebookRepository.save(any(NoteBook.class))).thenReturn(notebook);

        NoteBookDTO result = noteBookService.createNoteBook("New Notebook", account);

        assertNotNull(result);
        assertEquals("New Notebook", result.getName());
    }


    @Test
    void deleteNoteBook_validId_shouldDelete() {
        NoteBook notebook = new NoteBook();
        notebook.setNotebookid(1L);
        notebook.setName("Delete Notebook");
        notebook.setAudioFiles(Collections.emptyList());

        when(notebookRepository.findById(1L))
                .thenReturn(Optional.of(notebook));


        NoteBookDTO result = noteBookService.deleteNoteBook(1L);


        verify(notebookRepository).delete(notebook);
        assertEquals("Delete Notebook", result.getName());
    }

    @Test
    void deleteNoteBook_invalidId_shouldThrowException() {
        when(notebookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoteBookInvalidException.class, () -> noteBookService.deleteNoteBook(1L));
    }


    @Test
    void updateNoteBook_validId_shouldUpdateName() {
        // Arrange
        NoteBook notebook = new NoteBook();
        notebook.setNotebookid(1L);
        notebook.setName("Old Name");

        when(notebookRepository.findById(1L)).thenReturn(Optional.of(notebook));
        when(notebookRepository.save(any(NoteBook.class))).thenReturn(notebook);


        NoteBookDTO result = noteBookService.updateNoteBook(1L, "Updated Name");


        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateNoteBook_invalidId_shouldThrowException() {
        when(notebookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoteBookInvalidException.class, () -> noteBookService.updateNoteBook(1L, "Updated Name"));
    }
}

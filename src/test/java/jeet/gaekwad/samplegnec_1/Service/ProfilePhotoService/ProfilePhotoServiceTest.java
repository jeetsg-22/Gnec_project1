package jeet.gaekwad.samplegnec_1.Service.ProfilePhotoService;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jeet.gaekwad.samplegnec_1.DTOs.ProfilePhotoResponseDTO;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Model.ProfilePhoto;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import jeet.gaekwad.samplegnec_1.Repository.ProfilePhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

class ProfilePhotoServiceImplTest {

    @InjectMocks
    private ProfilePhotoServiceImpl profilePhotoService;

    @Mock
    private ProfilePhotoRepository profilePhotoRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadProfilePhoto_shouldUploadSuccessfully() throws IOException {
        Accounts account = new Accounts();
        account.setAccountId(1L);
        byte[] fileBytes = "fake image".getBytes();

        when(authentication.getName()).thenReturn("jeet");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(accountRepository.findByUsername("jeet")).thenReturn(account);
        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.empty());
        when(multipartFile.getBytes()).thenReturn(fileBytes);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");

        ProfilePhoto savedPhoto = new ProfilePhoto();
        savedPhoto.setId(1L);
        savedPhoto.setContentType("image/jpeg");
        savedPhoto.setUploadedAt(LocalDateTime.now());

        when(profilePhotoRepository.save(any(ProfilePhoto.class))).thenReturn(savedPhoto);


        ProfilePhotoResponseDTO result = profilePhotoService.uploadProfilePhoto(multipartFile);


        assertNotNull(result);
        assertEquals("image/jpeg", result.getContentType());
    }



    @Test
    void getProfilePhoto_validId_shouldReturnPhotoBytes() {
        ProfilePhoto photo = new ProfilePhoto();
        photo.setData("sample photo".getBytes());

        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.of(photo));

        byte[] result = profilePhotoService.getProfilePhoto(1L);

        assertArrayEquals("sample photo".getBytes(), result);
    }

    @Test
    void getProfilePhoto_invalidId_shouldThrowException() {
        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profilePhotoService.getProfilePhoto(1L));
    }


    @Test
    void updateProfilePhoto_shouldUpdateSuccessfully() throws IOException {
        // Arrange
        Accounts account = new Accounts();
        account.setAccountId(1L);

        ProfilePhoto existingPhoto = new ProfilePhoto();
        existingPhoto.setId(1L);

        when(authentication.getName()).thenReturn("jeet");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(accountRepository.findByUsername("jeet")).thenReturn(account);
        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.of(existingPhoto));
        when(multipartFile.getBytes()).thenReturn("new photo".getBytes());
        when(multipartFile.getContentType()).thenReturn("image/png");

        ProfilePhoto updatedPhoto = new ProfilePhoto();
        updatedPhoto.setId(1L);
        updatedPhoto.setContentType("image/png");
        updatedPhoto.setUploadedAt(LocalDateTime.now());

        when(profilePhotoRepository.save(any(ProfilePhoto.class))).thenReturn(updatedPhoto);

        ProfilePhotoResponseDTO result = profilePhotoService.updateProfilePhoto(multipartFile);

        assertNotNull(result);
        assertEquals("image/png", result.getContentType());
    }

    @Test
    void updateProfilePhoto_photoNotFound_shouldThrowException() throws IOException {
        Accounts account = new Accounts();
        account.setAccountId(1L);

        when(authentication.getName()).thenReturn("jeet");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(accountRepository.findByUsername("jeet")).thenReturn(account);
        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profilePhotoService.updateProfilePhoto(multipartFile));
    }



    @Test
    void deleteProfilePhoto_shouldDeleteSuccessfully() {
        Accounts account = new Accounts();
        account.setAccountId(1L);

        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setId(1L);

        when(authentication.getName()).thenReturn("jeet");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(accountRepository.findByUsername("jeet")).thenReturn(account);
        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.of(profilePhoto));

        String message = profilePhotoService.deleteProfilePhoto();

        verify(profilePhotoRepository).delete(profilePhoto);
        assertEquals("Profile photo removed successfully.", message);
    }

    @Test
    void deleteProfilePhoto_photoNotFound_shouldThrowException() {
        Accounts account = new Accounts();
        account.setAccountId(1L);

        when(authentication.getName()).thenReturn("jeet");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(accountRepository.findByUsername("jeet")).thenReturn(account);
        when(profilePhotoRepository.findByAccount_AccountId(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profilePhotoService.deleteProfilePhoto());
    }
}


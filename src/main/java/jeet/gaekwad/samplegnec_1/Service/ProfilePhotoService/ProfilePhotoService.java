package jeet.gaekwad.samplegnec_1.Service.ProfilePhotoService;

import jeet.gaekwad.samplegnec_1.DTOs.ProfilePhotoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfilePhotoService {
    ProfilePhotoResponseDTO uploadProfilePhoto(MultipartFile file) throws IOException;
    byte[] getProfilePhoto(Long accountId);

    ProfilePhotoResponseDTO updateProfilePhoto(MultipartFile file) throws IOException;

    String deleteProfilePhoto();
}

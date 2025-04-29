package jeet.gaekwad.samplegnec_1.Service.ProfilePhotoService;

import jeet.gaekwad.samplegnec_1.DTOs.ProfilePhotoResponseDTO;
import jeet.gaekwad.samplegnec_1.Exception.ProfilePhotoException;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Model.ProfilePhoto;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import jeet.gaekwad.samplegnec_1.Repository.ProfilePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService {
    private final ProfilePhotoRepository profilePhotoRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ProfilePhotoServiceImpl(ProfilePhotoRepository profilePhotoRepository, AccountRepository accountRepository) {
        this.profilePhotoRepository = profilePhotoRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public ProfilePhotoResponseDTO uploadProfilePhoto(MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Accounts account = accountRepository.findByUsername(username);

        ProfilePhoto profilePhoto = profilePhotoRepository.findByAccount_AccountId(account.getAccountId())
                .orElse(new ProfilePhoto()); // if exists, replace; otherwise new

        profilePhoto.setData(file.getBytes());
        profilePhoto.setContentType(file.getContentType());
        profilePhoto.setUploadedAt(LocalDateTime.now());
        profilePhoto.setAccount(account);

        profilePhoto = profilePhotoRepository.save(profilePhoto);

        ProfilePhotoResponseDTO dto = new ProfilePhotoResponseDTO();
        dto.setId(profilePhoto.getId());
        dto.setContentType(profilePhoto.getContentType());
        dto.setUploadedAt(profilePhoto.getUploadedAt().toString());
        return dto;
    }
    @Override
    public byte[] getProfilePhoto(Long accountId) {
        ProfilePhoto profilePhoto = profilePhotoRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new ProfilePhotoException("Profile photo not found"));
        return profilePhoto.getData();
    }
    @Override
    public ProfilePhotoResponseDTO updateProfilePhoto(MultipartFile file) throws IOException {
        // Same logic as upload but strictly update
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Accounts account = accountRepository.findByUsername(username);

        ProfilePhoto profilePhoto = profilePhotoRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new ProfilePhotoException("Profile photo not found"));

        profilePhoto.setData(file.getBytes());
        profilePhoto.setContentType(file.getContentType());
        profilePhoto.setUploadedAt(LocalDateTime.now());

        profilePhoto = profilePhotoRepository.save(profilePhoto);

        ProfilePhotoResponseDTO dto = new ProfilePhotoResponseDTO();
        dto.setId(profilePhoto.getId());
        dto.setContentType(profilePhoto.getContentType());
        dto.setUploadedAt(profilePhoto.getUploadedAt().toString());
        return dto;
    }

    @Override
    public String deleteProfilePhoto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Accounts account = accountRepository.findByUsername(username);

        ProfilePhoto profilePhoto = profilePhotoRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new ProfilePhotoException("Profile photo not found"));

        profilePhotoRepository.delete(profilePhoto);
        return "Profile photo removed successfully.";
    }
}

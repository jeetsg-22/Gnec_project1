package jeet.gaekwad.samplegnec_1.Repository;

import jeet.gaekwad.samplegnec_1.Model.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Integer> {
    Optional<ProfilePhoto> findByAccount_AccountId(Long accountId);
}

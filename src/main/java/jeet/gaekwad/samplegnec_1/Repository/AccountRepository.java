package jeet.gaekwad.samplegnec_1.Repository;

import jeet.gaekwad.samplegnec_1.Model.Accounts;
import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {
    Accounts findByUsername(String username);
    Accounts findByAccountIdAndUsername(Long accountId, String username);
    Optional<Accounts> findByEmail(String email);
}

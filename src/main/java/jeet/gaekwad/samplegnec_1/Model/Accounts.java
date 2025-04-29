package jeet.gaekwad.samplegnec_1.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accounts implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long accountId;
    @Column(unique=true, nullable=false)
    public String username;
    public String password;
    public String role;
    public String email;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "account",cascade = CascadeType.ALL,orphanRemoval = true)
    List<AudioFile> audioFiles = new ArrayList<>();

    @OneToOne(mappedBy = "account" ,cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private ProfilePhoto profilePhoto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

}

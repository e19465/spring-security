package com.sasindu.springsecurity.entities;

import com.sasindu.springsecurity.abstractions.dto.response.user.UserResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "users")
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @NaturalId
    @Column (nullable = false, unique = true, length = 200)
    private String email;

    @Column (nullable = false, length = 500)
    private String password;

    @Column (nullable = false, length = 50)
    private Boolean isEmailVerified = false;

    @Column (nullable = false, length = 50)
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
         return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
         return UserDetails.super.isAccountNonLocked();

    }

    @Override
    public boolean isCredentialsNonExpired() {
         return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isEmailVerified;
    }

    /**
     * This method is used to convert the AppUser entity to a UserResponseDto object.
     * @return UserResponseDto
     */
    public UserResponseDto toUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(id);
        userResponseDto.setFirstName(firstName);
        userResponseDto.setLastName(lastName);
        userResponseDto.setEmail(email);
        userResponseDto.setIsEmailVerified(isEmailVerified);
        userResponseDto.setRole(role);
        return userResponseDto;
    }
}

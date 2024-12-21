package com.markian.rentitup.User;

import com.markian.rentitup.BaseEntity.BaseEntity;
import com.markian.rentitup.Booking.Booking;
import com.markian.rentitup.Machine.Machine;
import com.markian.rentitup.Review.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity(name = "user_table")
@Table(indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true)
})
@EqualsAndHashCode(callSuper = true, of = {})
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(length = 1024)
    private String profileImageUrl;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(unique = true)
    private String registrationId;

    private LocalDateTime verifiedAt;

    private Long verifiedByAdminId;

    @Column(length = 1024)
    private String verificationImageUrl;

    @Column(length = 50)
    private String nationalId;


    @OneToMany(mappedBy = "owner")
    private List<Machine> ownedMachines;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviews;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

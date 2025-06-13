package com.example.projectservice.entity;

import com.example.projectservice.util.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "user_account", indexes = {
        @Index(name = "username", columnList = "username"),
        @Index(name = "status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccount implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    @Column(nullable = false)
    String password;
    String fullName;
    String phone;

    @Column(unique = true, nullable = false)
    String username;

    @Column(columnDefinition = "TEXT")
    String avatar;


    @Builder.Default
    String status = Constant.STATUS_ACTIVE;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    String role;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    @JsonBackReference
    Set<CourseRegistration> courseRegistrations = new HashSet<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority(getRole())));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}

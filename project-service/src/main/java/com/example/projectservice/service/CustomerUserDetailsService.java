package com.example.projectservice.service;

import com.example.projectservice.repo.UserAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserAccountRepo userAccountRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepo
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy username!"));
    }
}

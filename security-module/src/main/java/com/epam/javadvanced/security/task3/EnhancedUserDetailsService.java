package com.epam.javadvanced.security.task3;

import com.epam.javadvanced.security.task2.User;
import com.epam.javadvanced.security.task2.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("task3")
public class EnhancedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final BruteForceService bruteForceService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if user is blocked
        if (bruteForceService.isBlocked(email)) {
            throw new UsernameNotFoundException("User is temporarily blocked: " + email);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // Add role authority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        
        // Add permission authorities
        List<UserPermission> userPermissions = userPermissionRepository.findByUser(user);
        for (UserPermission userPermission : userPermissions) {
            authorities.add(new SimpleGrantedAuthority(userPermission.getPermission().getName()));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.isEnabled())
                .build();
    }
}
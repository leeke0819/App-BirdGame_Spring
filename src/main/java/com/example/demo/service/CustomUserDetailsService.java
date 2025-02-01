package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String UID) throws UsernameNotFoundException {
        System.out.println(UID);
        Optional<UserEntity> userOptional = Optional.ofNullable(userRepository.findByEmail(UID));
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return createUserDetails(user);
        } else {
            throw new UsernameNotFoundException(UID + "를 Model 에서 찾을 수 없습니다.");
        }
    }

    private UserDetails createUserDetails(UserEntity userEntity) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userEntity.getAuthority().toString());
        return new User(
                String.valueOf(userEntity.getEmail()),
                userEntity.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

}

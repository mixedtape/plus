package com.example.plus.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CUserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found" + nickname));
        return new UserDetailsImpl(user);
    }

}
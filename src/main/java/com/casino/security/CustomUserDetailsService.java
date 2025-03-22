package com.casino.security;

import com.casino.entity.Player;
import com.casino.repository.PlayerRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PlayerRepo playerRepo;

    public CustomUserDetailsService(PlayerRepo playerRepository) {
        this.playerRepo = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Player not found"));

        return AuthUser.builder()
                .player(player)
                .build();
    }
}
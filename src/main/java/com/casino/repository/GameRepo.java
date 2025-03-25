package com.casino.repository;

import com.casino.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepo extends JpaRepository<Game, Long> {
    List<Game> findByNameContainingIgnoreCase(String name);
}

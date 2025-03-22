package com.casino.repository;

import com.casino.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BetRepo extends JpaRepository<Bet, Long>, JpaSpecificationExecutor<Bet> {
    List<Bet> findByPlayerId(Long playerId);
}

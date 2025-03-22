package com.casino.specification;

import com.casino.entity.Bet;
import org.springframework.data.jpa.domain.Specification;

public class BetSpecifications {

    /**
     * Creates a specification to filter bets by player ID.
     *
     * @param playerId The ID of the player.
     * @return A Specification for filtering bets by player ID.
     */
    public static Specification<Bet> hasPlayerId(Long playerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("playerId"), playerId);
    }

    /**
     * Creates a specification to filter bets by game ID.
     *
     * @param gameId The ID of the game.
     * @return A Specification for filtering bets by game ID.
     */
    public static Specification<Bet> hasGameId(Long gameId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("gameId"), gameId);
    }

    /**
     * Creates a specification to filter bets by whether they were won or lost.
     *
     * @param won Whether the bet was won (true) or lost (false).
     * @return A Specification for filtering bets by outcome.
     */
    public static Specification<Bet> hasWon(boolean won) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("won"), won);
    }

    /**
     * Creates a specification to filter bets by a minimum bet amount.
     *
     * @param minBet The minimum bet amount.
     * @return A Specification for filtering bets by minimum bet amount.
     */
    public static Specification<Bet> hasMinBet(double minBet) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("betAmount"), minBet);
    }

    /**
     * Creates a specification to filter bets by a maximum bet amount.
     *
     * @param maxBet The maximum bet amount.
     * @return A Specification for filtering bets by maximum bet amount.
     */
    public static Specification<Bet> hasMaxBet(double maxBet) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("betAmount"), maxBet);
    }
}
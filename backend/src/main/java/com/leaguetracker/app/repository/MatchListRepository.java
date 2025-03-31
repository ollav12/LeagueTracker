package com.leaguetracker.app.repository;

import com.leaguetracker.app.model.MatchList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchListRepository extends JpaRepository<MatchList, Long> {
    MatchList findByMatchId(String matchId);

    List<MatchList> findByPuuid(String puuid);

    MatchList findById(int id);

    @Query("SELECT m.matchId FROM MatchList m WHERE m.puuid = :puuid AND m.matchId < :lastMatchId ORDER BY m.matchId DESC")
    List<String> getNextMatchIds(@Param("puuid") String puuid, @Param("lastMatchId") String lastMatchId,
            Pageable pageable);

    @Query(value = """
            SELECT match_id FROM summoner_matchlist
            WHERE puuid = :puuid
            ORDER BY match_id DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<String> findAllByPuuidOrderByMatchIdDesc(
            @Param("puuid") String puuid,
            @Param("limit") int limit);

    @Query(value = """
            SELECT COUNT(*) FROM summoner_matchlist
            WHERE puuid = :puuid
            """, nativeQuery = true)
    long countByPuuid(@Param("puuid") String puuid);
}

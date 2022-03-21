package server.database;

import commons.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    LeaderboardEntry findByUsername(String username);
    @Modifying
    @Query("update LeaderboardEntry l set l.points = :newScore where l.username = :username")
    void updateScoreForUser(@Param("username")String username, @Param("newScore") int newScore);
}

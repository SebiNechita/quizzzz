package server.database;

import commons.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
    LeaderboardEntry findByUsername(String username);}

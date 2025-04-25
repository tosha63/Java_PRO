package t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import t1.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("""
            UPDATE User u
            SET u.username = :newUsername,
                u.age = :age
            WHERE u.id = :id
            """)
    void updateUser(Long id, String newUsername, int age);
}

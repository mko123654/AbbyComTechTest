package abby.exam.repository;

import abby.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserUsername(String username);
    User findByUserEmail(String email);
}

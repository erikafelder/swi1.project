package cz.osu.swidemo.repositories;

import cz.osu.swidemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository  extends JpaRepository <User, String> {
    List<User> findByBooksId(Long bookId);
}

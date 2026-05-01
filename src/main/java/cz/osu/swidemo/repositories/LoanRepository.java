package cz.osu.swidemo.repositories;

import cz.osu.swidemo.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(String userId);
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
}
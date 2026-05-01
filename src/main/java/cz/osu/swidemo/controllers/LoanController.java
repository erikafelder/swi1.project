package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Loan;
import cz.osu.swidemo.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/user/{userId}")
    public List<Loan> getLoansByUser(@PathVariable String userId) {
        return loanRepository.findByUserId(userId);
    }

    @PostMapping("/{loanId}/return")
    public ResponseEntity<String> returnLoan(@PathVariable Long loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);

        if (optionalLoan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Loan loan = optionalLoan.get();

        if (loan.getReturnDate() != null) {
            return ResponseEntity.ok("Kniha už byla vrácena.");
        }

        LocalDate today = LocalDate.now();
        loan.setReturnDate(today);

        int fine = 0;

        if (loan.getDueDate() != null && today.isAfter(loan.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), today);
            fine = (int) daysLate * 5;
        }

        loan.setFineAmount(fine);
        loanRepository.save(loan);

        if (fine > 0) {
            return ResponseEntity.ok("Kniha vrácena! Pokuta za pozdní vrácení: " + fine + " Kč");
        }

        return ResponseEntity.ok("Kniha byla vrácena bez pokuty.");
    }
}
package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Loan;
import cz.osu.swidemo.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}
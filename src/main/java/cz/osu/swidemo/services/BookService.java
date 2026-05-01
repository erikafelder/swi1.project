package cz.osu.swidemo.services;

import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.Loan;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.LoanRepository;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookService {

    private static final int LOAN_DAYS = 14;
    private static final int FINE_PER_DAY = 5;

    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private LoanRepository loanRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(String title, String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    Author a = new Author();
                    a.setName(authorName);
                    return authorRepository.save(a);
                });

        Book book = new Book();
        book.setTitle(title);
        book.setIsbn("ISBN-" + System.currentTimeMillis());
        book.addAuthor(author);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Loan loanBook(Long bookId, String userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (book.isLoaned()) {
            throw new RuntimeException("Kniha je už vypůjčená");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(LOAN_DAYS));
        loan.setFineAmount(0);
        return loanRepository.save(loan);
    }

    public Loan returnBook(Long bookId) {
        Loan loan = loanRepository.findByBookIdAndReturnDateIsNull(bookId)
                .orElseThrow(() -> new RuntimeException("Kniha není aktuálně vypůjčená"));

        LocalDate today = LocalDate.now();
        loan.setReturnDate(today);
        loan.setFineAmount(calculateFine(loan.getDueDate(), today));
        return loanRepository.save(loan);
    }

    public int calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) return 0;
        long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return (int) lateDays * FINE_PER_DAY;
    }
}
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LoanRepository loanRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        createAdminIfMissing();
        createBooksIfMissing();
        createEmilWithOverdueHobbitLoan();

        System.out.println("--------------------------------------");
        System.out.println("DATABÁZE INICIALIZOVÁNA");
        System.out.println("Admin login: admin / Heslo: admin123");
        System.out.println("Emil login: emil / Heslo: emil123");
        System.out.println("Emil má půjčeného Hobita po termínu.");
        System.out.println("--------------------------------------");
    }

    private void createAdminIfMissing() {
        Optional<User> existingAdmin = userRepository.findFirstByUsername("admin");

        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Erika");
            admin.setLastName("Admin");
            admin.setEmail("admin@knihovna.cz");
            admin.setAge(20);
            admin.setRole("ADMIN");
            userRepository.save(admin);
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Erika");
        admin.setLastName("Admin");
        admin.setEmail("admin@knihovna.cz");
        admin.setAge(20);
        admin.setRole("ADMIN");
        userRepository.save(admin);
    }

    private void createBooksIfMissing() {
        if (bookRepository.count() > 0) {
            return;
        }

        Author christie = createAuthor("Agatha Christie");
        Author doyle = createAuthor("Arthur Conan Doyle");
        Author orwell = createAuthor("George Orwell");
        Author capek = createAuthor("Karel Čapek");
        Author gaiman = createAuthor("Neil Gaiman");
        Author pratchett = createAuthor("Terry Pratchett");
        Author tolkien = createAuthor("J.R.R. Tolkien");
        Author rowling = createAuthor("J.K. Rowling");

        createBook("Vražda v Orient expresu", "978-80-001", christie);
        createBook("Deset malých černoušků", "978-80-002", christie);
        createBook("Pes baskervillský", "978-80-003", doyle);
        createBook("1984", "978-80-004", orwell);
        createBook("Farma zvířat", "978-80-005", orwell);
        createBook("Válka s Mloky", "978-80-006", capek);
        createBook("Bílá nemoc", "978-80-007", capek);
        createBook("Hobit", "978-80-008", tolkien);
        createBook("Pán prstenů", "978-80-009", tolkien);
        createBook("Harry Potter a kámen mudrců", "978-80-010", rowling);
        createBook("Harry Potter a tajemná komnata", "978-80-011", rowling);
        createBookWithTwoAuthors("Dobrá znamení", "978-80-012", gaiman, pratchett);
    }

    private void createEmilWithOverdueHobbitLoan() {
        Optional<User> existingEmil = userRepository.findFirstByUsername("emil");

        User emil;

        if (existingEmil.isPresent()) {
            emil = existingEmil.get();
        } else {
            emil = new User();
            emil.setUsername("emil");
        }

        emil.setPassword(passwordEncoder.encode("emil123"));
        emil.setFirstName("Emil");
        emil.setLastName("Novák");
        emil.setEmail("emil@knihovna.cz");
        emil.setAge(22);
        emil.setRole("USER");
        emil = userRepository.save(emil);

        User finalEmil = emil;

        Book hobit = bookRepository.findAll()
                .stream()
                .filter(book -> book.getTitle().equalsIgnoreCase("Hobit"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Kniha Hobit nebyla nalezena."));

        Optional<Loan> existingActiveLoan = loanRepository.findAll()
                .stream()
                .filter(loan -> loan.getUser() != null)
                .filter(loan -> loan.getBook() != null)
                .filter(loan -> loan.getReturnDate() == null)
                .filter(loan -> loan.getUser().getUsername().equalsIgnoreCase("emil"))
                .filter(loan -> loan.getBook().getTitle().equalsIgnoreCase("Hobit"))
                .findFirst();

        Loan loan = existingActiveLoan.orElseGet(() -> {
            Loan newLoan = new Loan();
            newLoan.setUser(finalEmil);
            newLoan.setBook(hobit);
            newLoan.setLoanDate(LocalDate.now());
            newLoan.setReturnDate(null);
            newLoan.setFineAmount(0);
            return newLoan;
        });

        loan.setUser(finalEmil);
        loan.setBook(hobit);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().minusDays(1));
        loan.setReturnDate(null);
        loan.setFineAmount(0);

        loanRepository.save(loan);
    }

    private Author createAuthor(String name) {
        Author author = new Author();
        author.setName(name);
        return authorRepository.save(author);
    }

    private void createBook(String title, String isbn, Author author) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.addAuthor(author);
        bookRepository.save(book);
    }

    private void createBookWithTwoAuthors(String title, String isbn, Author author1, Author author2) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.addAuthor(author1);
        book.addAuthor(author2);
        bookRepository.save(book);
    }
}
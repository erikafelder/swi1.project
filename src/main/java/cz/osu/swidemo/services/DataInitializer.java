package cz.osu.swidemo.services;

import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {

        if (!userRepository.existsByUsername("admin")) {
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

        if (bookRepository.count() > 0) {
            System.out.println("--------------------------------------");
            System.out.println("ADMIN ÚČET EXISTUJE");
            System.out.println("Login: admin / Heslo: admin123");
            System.out.println("--------------------------------------");
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

        System.out.println("--------------------------------------");
        System.out.println("DATABÁZE INICIALIZOVÁNA");
        System.out.println("Login: admin / Heslo: admin123");
        System.out.println("--------------------------------------");
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

    private void createBookWithTwoAuthors(String title, String isbn, Author firstAuthor, Author secondAuthor) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.addAuthor(firstAuthor);
        book.addAuthor(secondAuthor);
        bookRepository.save(book);
    }
}
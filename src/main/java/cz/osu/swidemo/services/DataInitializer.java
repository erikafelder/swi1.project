package cz.osu.swidemo.services;

import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.LoanRepository;
import cz.osu.swidemo.repositories.UserRepository;
import cz.osu.swidemo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private LoanRepository loanRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();

        // ADMIN
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Erika");
        admin.setLastName("Admin");
        admin.setEmail("admin@knihovna.cz");
        admin.setAge(25);
        admin.setRole("ADMIN");
        userRepository.save(admin);

        // AUTOŘI
        Author shakespeare = createAuthor("William Shakespeare");
        Author orwell = createAuthor("George Orwell");
        Author tolkien = createAuthor("J.R.R. Tolkien");
        Author rowling = createAuthor("J.K. Rowling");
        Author dostojevski = createAuthor("Fjodor Dostojevskij");
        Author kafka = createAuthor("Franz Kafka");
        Author twain = createAuthor("Mark Twain");
        Author hugo = createAuthor("Victor Hugo");

        // KNIHY
        createBook("Romeo a Julie", "978-80-001", shakespeare);
        createBook("Hamlet", "978-80-002", shakespeare);
        createBook("Othello", "978-80-003", shakespeare);
        createBook("1984", "978-80-004", orwell);
        createBook("Farma zvířat", "978-80-005", orwell);
        createBook("Pán prstenů: Společenstvo prstenu", "978-80-006", tolkien);
        createBook("Pán prstenů: Dvě věže", "978-80-007", tolkien);
        createBook("Pán prstenů: Návrat krále", "978-80-008", tolkien);
        createBook("Hobit", "978-80-009", tolkien);
        createBook("Harry Potter a kámen mudrců", "978-80-010", rowling);
        createBook("Harry Potter a tajemná komnata", "978-80-011", rowling);
        createBook("Harry Potter a vězeň z Azkabanu", "978-80-012", rowling);
        createBook("Harry Potter a ohnivý pohár", "978-80-013", rowling);
        createBook("Zločin a trest", "978-80-014", dostojevski);
        createBook("Idiot", "978-80-015", dostojevski);
        createBook("Bratři Karamazovi", "978-80-016", dostojevski);
        createBook("Proměna", "978-80-017", kafka);
        createBook("Proces", "978-80-018", kafka);
        createBook("Zámek", "978-80-019", kafka);
        createBook("Dobrodružství Toma Sawyera", "978-80-020", twain);
        createBook("Dobrodružství Huckleberryho Finna", "978-80-021", twain);
        createBook("Bídníci", "978-80-022", hugo);
        createBook("Chrám Matky Boží v Paříži", "978-80-023", hugo);
        createBook("Devadesát tři", "978-80-024", hugo);
        createBook("Muž, který se směje", "978-80-025", hugo);

        System.out.println("--------------------------------------");
        System.out.println("DATABÁZE INICIALIZOVÁNA");
        System.out.println("Login: admin / Heslo: admin123");
        System.out.println("Nahráno 25 knih a 8 autorů");
        System.out.println("--------------------------------------");
    }

    private Author createAuthor(String name) {
        Author a = new Author();
        a.setName(name);
        return authorRepository.save(a);
    }

    private void createBook(String title, String isbn, Author author) {
        Book b = new Book();
        b.setTitle(title);
        b.setIsbn(isbn);
        b.setAuthor(author);
        bookRepository.save(b);
    }
}
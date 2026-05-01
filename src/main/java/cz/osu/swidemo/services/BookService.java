package cz.osu.swidemo.services;

import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Přijme název a jméno autora, autor se najde nebo vytvoří
    public Book createBook(String title, String authorName) {
        // Najdi autora podle jména, nebo vytvoř nového
        Author author = authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(authorName);
                    return authorRepository.save(newAuthor);
                });

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn("ISBN-" + System.currentTimeMillis());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void loanBook(Long bookId, String userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (!user.getBooks().contains(book)) {
            user.addBook(book);
            userRepository.save(user);
        }
    }

    public void returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        for (User user : book.getLoanedByUsers()) {
            user.getBooks().remove(book);
            userRepository.save(user);
        }
    }
}
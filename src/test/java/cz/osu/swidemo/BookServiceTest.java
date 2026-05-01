package cz.osu.swidemo;

import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.Loan;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.LoanRepository;
import cz.osu.swidemo.repositories.UserRepository;
import cz.osu.swidemo.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private AuthorRepository authorRepository;
    @Mock private LoanRepository loanRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testCreateBook_newAuthor_isCreated() {
        when(authorRepository.findByName("Nový Autor")).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenAnswer(i -> i.getArguments()[0]);
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        Book result = bookService.createBook("Nová kniha", "Nový Autor");

        assertEquals("Nová kniha", result.getTitle());
        assertNotNull(result.getIsbn());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testCreateBook_existingAuthor_isReused() {
        Author existing = new Author();
        existing.setName("Existující Autor");

        when(authorRepository.findByName("Existující Autor")).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        Book result = bookService.createBook("Kniha", "Existující Autor");

        assertFalse(result.getAuthors().isEmpty());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testLoanBook_createsLoan() {
        Book book = new Book();
        book.setId(1L);

        User user = new User();
        user.setId("user-1");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);

        Loan result = bookService.loanBook(1L, "user-1");

        assertNotNull(result);
        assertEquals(book, result.getBook());
        assertEquals(user, result.getUser());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testDeleteBook_callsRepository() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
package cz.osu.swidemo;

import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.UserRepository;
import cz.osu.swidemo.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorRepository authorRepository;

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

        assertEquals("Existující Autor", result.getAuthor().getName());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testLoanBook_addsBookToUser() {
        Book book = new Book();
        book.setId(1L);

        User user = new User();
        user.setId("user-1");
        user.setBooks(new ArrayList<>());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        bookService.loanBook(1L, "user-1");

        assertTrue(user.getBooks().contains(book));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteBook_callsRepository() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
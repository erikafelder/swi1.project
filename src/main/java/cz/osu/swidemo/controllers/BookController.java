package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public Book createBook(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String authorName = body.get("authorName");
        return bookService.createBook(title, authorName);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PostMapping("/{bookId}/loan/{userId}")
    public ResponseEntity<?> loanBook(@PathVariable Long bookId, @PathVariable String userId) {
        bookService.loanBook(bookId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookId}/loan/return")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        bookService.returnBook(bookId);
        return ResponseEntity.ok().build();
    }
}
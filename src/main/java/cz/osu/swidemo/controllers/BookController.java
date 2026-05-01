package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.Loan;
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
        return bookService.createBook(body.get("title"), body.get("authorName"));
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PostMapping("/{bookId}/loan/{userId}")
    public ResponseEntity<Loan> loanBook(@PathVariable Long bookId, @PathVariable String userId) {
        return ResponseEntity.ok(bookService.loanBook(bookId, userId));
    }

    @PostMapping("/{bookId}/loan/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.returnBook(bookId));
    }
}
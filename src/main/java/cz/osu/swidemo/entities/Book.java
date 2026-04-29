package cz.osu.swidemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    public boolean isLoaned() {
        return !loanedByUsers.isEmpty();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(mappedBy = "books")
    @JsonIgnore  // zabrání infinite loop při serializaci
    private List<User> loanedByUsers = new ArrayList<>();

    // --- GETTERY A SETTERY ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public List<User> getLoanedByUsers() { return loanedByUsers; }
    public void setLoanedByUsers(List<User> users) { this.loanedByUsers = users; }
}

package com.booleanuk.api.controller;

import com.booleanuk.api.model.Author;
import com.booleanuk.api.model.Book;
import com.booleanuk.api.model.Publisher;
import com.booleanuk.api.repository.AuthorRepository;
import com.booleanuk.api.repository.BookRepository;
import com.booleanuk.api.repository.PublisherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {
    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookController(BookRepository repository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;

    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Book> getById(@PathVariable("id") Integer id) {
        Book book = this.repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find book with that id."));
        return ResponseEntity.ok(book);
    }

    private record PostBook(String title, String genre, int author_id, int publisher_id) {}

    //@ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Book> create(@RequestBody PostBook request) {
        Author author = authorRepository.findById(request.author_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find author with that id."));
        Publisher publisher = publisherRepository.findById(request.publisher_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find publisher with that id."));
        Book book = new Book(request.title(), request.genre(), author, publisher);
        return new ResponseEntity<>(this.repository.save(book), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody PostBook book) {
        Book bookToUpdate = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find book with that id."));
        Author author = authorRepository.findById(book.author_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find author with that id."));
        Publisher publisher = publisherRepository.findById(book.publisher_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find publisher with that id."));
        bookToUpdate.setTitle(book.title());
        bookToUpdate.setGenre(book.genre());
        bookToUpdate.setAuthor(author);
        bookToUpdate.setPublisher(publisher);
        return new ResponseEntity<>(this.repository.save(bookToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable int id) {
        Book bookToDelete = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find book with that id."));
        this.repository.delete(bookToDelete);
        return ResponseEntity.ok(bookToDelete);
    }
}

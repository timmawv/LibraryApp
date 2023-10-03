package avlyakulov.timur.LibraryApp.service;

import avlyakulov.timur.LibraryApp.models.Book;
import avlyakulov.timur.LibraryApp.models.Person;
import avlyakulov.timur.LibraryApp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getListBooks() {
        return bookRepository.findAll(Sort.by("id"));
    }

    @Transactional
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    public Book getBook(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void editBook(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void appointOwnerForBook(Person newOwner, int id) {
        Book book = bookRepository.findById(id).get();
        book.setOwner(newOwner);
    }

    @Transactional
    public void releaseBookFromPerson(int id) {
        Book book = bookRepository.findById(id).get();
        book.setOwner(null);
    }

    public boolean checkOwner(int id) {
        Book book = bookRepository.findById(id).get();
        return book.getOwner() != null;
    }

    public Book getBookByName(String name) {
        return bookRepository.findBookByName(name);
    }

    public List<Book> getPageableBooks(int page, int itemsPerPage) {
        return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("id"))).getContent();
    }

    public List<Book> getBooksSortByParameter(String sortBy, String order) {
        switch (sortBy) {
            case "name" -> {
                if (order.equals("asc"))
                    return bookRepository.findAll(Sort.by("name"));
                else
                    return bookRepository.findAll(Sort.by("name").descending());
            }
            case "year" -> {
                if (order.equals("asc"))
                    return bookRepository.findAll(Sort.by("year"));
                else
                    return bookRepository.findAll(Sort.by("year").descending());
            }
            default -> {
                return bookRepository.findAll(Sort.by("name").descending());
            }
        }
    }

    public List<Book> findBookByNameStartingWith(String name) {
        if (name.equals(""))
            return Collections.emptyList();
        else {
            return bookRepository.findAllByNameStartingWith(name);
        }
    }
}
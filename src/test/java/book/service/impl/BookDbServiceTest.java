package book.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.db.book.service.BookDbService;
import org.db.book.service.impl.BookDbServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for book db service.
 *
 * @author Akash Kumar
 * @version 1.0
 * @since 1.0
 */

public class BookDbServiceTest {

  private BookDbService bookDbService;

  @BeforeEach
  public void setUp() {
    bookDbService = new BookDbServiceImpl();

  }


  @Test
  public void testAddBook() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(1, allBooks.size());
    String bookTitle = allBooks.iterator().next();
    Assertions.assertEquals("Java Concurrency in practice", bookTitle);
    Assertions.assertEquals(authors, bookDbService.getAuthorsByTitle(bookTitle));

    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);
    allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(2, allBooks.size());
  }

  @Test
  public void testAddBookWithDuplicateTitle() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.addBook("Java Concurrency in practice", authors));
  }

  @Test
  public void testAddBookWithDuplicateAuthors() {
    List<String> authors = new ArrayList<>();
    authors.add("Doug Lea");
    authors.add("Brian Goetz");
    authors.add("Doug Lea");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    List<String> expectedAuthors = new ArrayList<>();
    expectedAuthors.add("Doug Lea");
    expectedAuthors.add("Brian Goetz");
    expectedAuthors.add("Joshua Bloch");
    Assertions.assertEquals(expectedAuthors, bookDbService.getAuthorsByTitle("Java Concurrency in practice"));

  }

  @Test
  public void testGetAuthorsByTitle() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertEquals(authors,
        bookDbService.getAuthorsByTitle("Java Concurrency in practice"));

    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);
    Assertions.assertEquals(effectiveJAuthors, bookDbService.getAuthorsByTitle("Effective Java"));
  }

  @Test
  public void testGetAuthorByNonExistentTitle() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.getAuthorsByTitle("Random Title"));
  }

  @Test
  public void testGetAuthorByNonExistentTitleEmptyDatabase() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.getAuthorsByTitle("Random Title"));
  }

  @Test
  public void testGetTitleByAuthor() {
    List<String> concurrencyAuthors = new ArrayList<>();
    concurrencyAuthors.add("Brian Goetz");
    concurrencyAuthors.add("Joshua Bloch");
    concurrencyAuthors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", concurrencyAuthors);
    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);
    List<String> booksByAuthor = bookDbService.getTitlesByAuthor("Joshua Bloch");
    Assertions.assertEquals(2, booksByAuthor.size());
    Assertions.assertTrue(booksByAuthor.contains("Java Concurrency in practice"));
    Assertions.assertTrue(booksByAuthor.contains("Effective Java"));
  }

  @Test
  public void testGetTitleByNonExistentAuthor() {
    List<String> concurrencyAuthors = new ArrayList<>();
    concurrencyAuthors.add("Brian Goetz");
    concurrencyAuthors.add("Joshua Bloch");
    concurrencyAuthors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", concurrencyAuthors);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.getTitlesByAuthor("Random Author"));
  }

  @Test
  public void testGetTitleByNonExistentAuthorEmptyDatabase() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.getTitlesByAuthor("Random Author"));
  }

  @Test
  public void testRemoveTitle() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    bookDbService.removeTitle("Java Concurrency in practice");
    List<String> allBooksTitle = bookDbService.getAllTitles();
    Assertions.assertEquals(0, allBooksTitle.size());
  }

  @Test
  public void testRemoveNonexistentTitle() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.removeTitle("Random Book"));
    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(1, allBooks.size());
    String bookTitle = allBooks.iterator().next();
    Assertions.assertEquals("Java Concurrency in practice", bookTitle);
  }

  @Test
  public void testRemoveNonexistentTitleEmptyDatabase() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.removeTitle("Random Book"));
  }

  @Test
  public void testRemoveAllTitlesByAuthor() {
    List<String> scjpAuthors = new ArrayList<>();
    scjpAuthors.add("Kathy Sierra");
    bookDbService.addBook("scjp", scjpAuthors);
    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);

    bookDbService.removeAllTitlesByAuthor("Kathy Sierra");

    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(1, allBooks.size());
    String book = allBooks.iterator().next();
    Assertions.assertEquals("Effective Java", book);
  }

  @Test
  public void testRemoveAllTitlesByAuthorNonexistentAuthor() {
    List<String> scjpAuthors = new ArrayList<>();
    scjpAuthors.add("Kathy Sierra");
    bookDbService.addBook("scjp", scjpAuthors);

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.removeAllTitlesByAuthor("Random Author"));

    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(1, allBooks.size());
    String bookTitle = allBooks.iterator().next();
    Assertions.assertEquals("scjp", bookTitle);
  }

  @Test
  public void testRemoveAllTitlesByAuthorMultipleAuthors() {
    List<String> scjpAuthors = new ArrayList<>();
    scjpAuthors.add("Kathy Sierra");
    bookDbService.addBook("scjp", scjpAuthors);

    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);

    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);

    bookDbService.removeAllTitlesByAuthor("Joshua Bloch");

    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(1, allBooks.size());
    String bookTitle = allBooks.iterator().next();
    Assertions.assertEquals("scjp", bookTitle);
  }

  @Test
  public void testRemoveAllTitlesByAuthorEmptyDatabase() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.removeAllTitlesByAuthor("Joshua Bloch"));
  }

  @Test
  public void testGetAllTitles() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);

    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);

    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(2, allBooks.size());
  }

  @Test
  public void testGetAllTitlesEmptyDatabase() {
    List<String> allBooks = bookDbService.getAllTitles();
    Assertions.assertEquals(0, allBooks.size());
  }

  @Test
  public void testGetAllAuthors() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);

    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);

    List<String> scjpAuthors = new ArrayList<>();
    scjpAuthors.add("Kathy Sierra");
    bookDbService.addBook("scjp", scjpAuthors);

    List<String> allAuthors = bookDbService.getAllAuthors();
    Assertions.assertEquals(4, allAuthors.size());
  }

  @Test
  public void testGetAllAuthorsEmptyDatabase() {
    List<String> allAuthors = bookDbService.getAllAuthors();
    Assertions.assertEquals(0, allAuthors.size());
  }

  @Test
  public void testUpdateBookAuthorsByTitle() {
    List<String> effectiveJAuthors = new ArrayList<>();
    effectiveJAuthors.add("Joshua Bloch");
    bookDbService.addBook("Effective Java", effectiveJAuthors);
    Assertions.assertEquals(effectiveJAuthors, bookDbService.getAuthorsByTitle("Effective Java"));

    List<String> newEffectiveJAuthors = new ArrayList<>();
    newEffectiveJAuthors.add("Doug Lea");
    bookDbService.updateBookAuthorsByTitle("Effective Java", newEffectiveJAuthors);
    Assertions.assertEquals(newEffectiveJAuthors,
        bookDbService.getAuthorsByTitle("Effective Java"));
  }

  @Test
  public void testUpdateBookAuthorsByTitleNonexistentTitle() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.updateBookAuthorsByTitle("Random Title", authors));
  }

  @Test
  public void testUpdateBookAuthorsByTitleMultipleAuthors() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertEquals(authors,
        bookDbService.getAuthorsByTitle("Java Concurrency in practice"));

    List<String> newAuthors = new ArrayList<>();
    newAuthors.add("Kathy Sierra");
    bookDbService.updateBookAuthorsByTitle("Java Concurrency in practice", newAuthors);
    List<String> allAuthors = bookDbService.getAuthorsByTitle("Java Concurrency in practice");
    Assertions.assertEquals(newAuthors, allAuthors);
    Assertions.assertEquals(1, allAuthors.size());
  }

  @Test
  public void testUpdateBookAuthorsByTitleEmptyAuthors() {
    List<String> authors = new ArrayList<>();
    authors.add("Brian Goetz");
    authors.add("Joshua Bloch");
    authors.add("Doug Lea");
    bookDbService.addBook("Java Concurrency in practice", authors);
    Assertions.assertEquals(authors,
        bookDbService.getAuthorsByTitle("Java Concurrency in practice"));

    List<String> newAuthors = new ArrayList<>();
    bookDbService.updateBookAuthorsByTitle("Java Concurrency in practice", authors);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.updateBookAuthorsByTitle("Java Concurrency in practice", newAuthors));
  }

  @Test
  public void testUpdateBookAuthorsByTitleEmptyDatabase() {
    List<String> authors = new ArrayList<>();
    authors.add("Kathy Sierra");
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> bookDbService.updateBookAuthorsByTitle("Java Concurrency in practice", authors));
  }

}

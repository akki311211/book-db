package org.db.book.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.db.book.service.BookDbService;

/**
 * Represents a book DB and supports CRUD operations.
 *
 * @author Akash Kumar
 * @version 1.0
 * @since 1.0
 */
public class BookDbServiceImpl implements BookDbService {

  private final Map<String, Set<String>> authorsByBook;
  private final Map<String, Set<String>> booksByAuthor;



  public BookDbServiceImpl() {
    authorsByBook = new HashMap<>();
    booksByAuthor = new HashMap<>();
  }


  /**
   * Method to add book in the database.
   *
   * @param title - represents title of the book
   * @param authors - represents authors of the book
   */
  @Override
  public void addBook(final String title, final List<String> authors) {
    if (authorsByBook.containsKey(title)) {
      throw new IllegalArgumentException("A book with the title '" + title + "' already exists.");
    }

    authorsByBook.put(title, new LinkedHashSet<>(authors));

    for (String author : authors) {
      if (!booksByAuthor.containsKey(author)) {
        booksByAuthor.put(author, new LinkedHashSet<>());
      }
      booksByAuthor.get(author).add(title);
    }
  }

  /**
   * Method to fetch all authors by title.
   *
   * @param title - represents title of the book
   * @return Set of authors
   */
  @Override
  public List<String> getAuthorsByTitle(final String title) {
    if (!authorsByBook.containsKey(title)) {
      throw new IllegalArgumentException(
          "No book with the title '" + title + "' exists in the database.");
    }
    return new ArrayList<>(authorsByBook.get(title));
  }

  /**
   * Method to remove book by title.
   * 
   * @param title - represents title of the book
   */
  @Override
  public void removeTitle(final String title) {
    if (!authorsByBook.containsKey(title)) {
      throw new IllegalArgumentException(
          "No book with the title '" + title + "' exists in the database.");
    }

    for (String author : authorsByBook.get(title)) {
      Set<String> allTitlesByAuthor = booksByAuthor.get(author);
      allTitlesByAuthor.remove(title);

      if (allTitlesByAuthor.isEmpty()) {
        booksByAuthor.remove(author);
      }
    }
    authorsByBook.remove(title);
  }


  /**
   * Method to remove all books by author
   *
   * @param author - represents author of the book
   */
  @Override
  public void removeAllTitlesByAuthor(final String author) {
    if (!booksByAuthor.containsKey(author)) {
      throw new IllegalArgumentException(
          "No books found by author '" + author + "' in the database.");
    }
    Set<String> allTitlesByAuthor = booksByAuthor.get(author);
    for (String title : allTitlesByAuthor) {
      authorsByBook.remove(title);
    }
    booksByAuthor.remove(author);
  }


  /**
   * Method to fetch all books.
   *
   * @return All books in the database.
   */
  @Override
  public List<String> getAllTitles() {
    return new ArrayList<>(authorsByBook.keySet());
  }

  /**
   * Method to fetch all books.
   *
   * @return All authors for all books in the database.
   */
  @Override
  public List<String> getAllAuthors() {
    return new ArrayList<>(booksByAuthor.keySet());
  }


  /**
   * Method to get All titles by author
   *
   * @param author - represents author of the book
   * @return All titles by author
   */
  @Override
  public List<String> getTitlesByAuthor(final String author) {
    if (booksByAuthor.containsKey(author)) {
      return new ArrayList<>(booksByAuthor.get(author));
    } else {
      throw new IllegalArgumentException("No books found by author '" + author + "'.");
    }
  }

  /**
   * Method to update authors by title.
   *
   * @param title - represents title of the book
   * @param authors - represents author of the book
   */
  @Override
  public void updateBookAuthorsByTitle(final String title, final List<String> authors) {
    if (!authorsByBook.containsKey(title)) {
      throw new IllegalArgumentException(
          "No book with the title '" + title + "' exists in the database.");
    }
    if (authors.isEmpty()) {
      throw new IllegalArgumentException("Authors cannot be empty for update.");
    }
    Set<String> allAuthorsByTitle = authorsByBook.get(title);
    authorsByBook.put(title, new LinkedHashSet<>(authors));

    for (String author : allAuthorsByTitle) {
      if (!booksByAuthor.containsKey(author)) {
        booksByAuthor.put(author, new LinkedHashSet<>());
      }
      booksByAuthor.get(author).add(title);
    }
  }
}

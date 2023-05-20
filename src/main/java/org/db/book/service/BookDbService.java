package org.db.book.service;

import java.util.List;

/**
 * Interface for all book db service.
 *
 * @author Akash Kumar
 * @version 1.0
 * @since 1.0
 */
public interface BookDbService {

  void addBook(String title, List<String> authors);

  List<String> getAuthorsByTitle(String title);

  List<String> getTitlesByAuthor(String author);

  void removeTitle(String title);

  void removeAllTitlesByAuthor(String author);

  List<String> getAllTitles();

  List<String> getAllAuthors();

  void updateBookAuthorsByTitle(String title, List<String> authors);

}

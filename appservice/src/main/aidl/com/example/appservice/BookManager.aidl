// BookManager.aidl
package com.example.appservice;

import com.example.appservice.Book;
// Declare any non-default types here with import statements

interface BookManager {
   List<Book> getBooks();
   void addBook(in Book book);
}

package ru.javarush.crud.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.javarush.crud.entity.Book;
import ru.javarush.crud.repository.BookRepository;

import java.io.IOException;
import java.util.List;

@Service("bookService")
@Repository
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;


    @Override
    @Transactional(readOnly=true)
    public List<Book> findAll() {
        return Lists.newArrayList(bookRepository.findAll());
    }

    @Override
    @Transactional(readOnly=true)
    public Book findById(Long id) {
        return bookRepository.findOne(id);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Page<Book> findAllByPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book update(Book book, Long id) {
        Book entity = bookRepository.findOne(id);
        if (book.getAutor() != null) entity.setAutor(book.getAutor());
        if (book.getTitle() != null) entity.setTitle(book.getTitle());
        if (book.getDescription() != null) entity.setDescription(book.getDescription());
        if (book.getIsbn() != null) entity.setIsbn(book.getIsbn());
        if (book.getPrintYear() != 0) entity.setPrintYear(book.getPrintYear());
        if (book.getImageStr() != null) entity.setImageStr(book.getImageStr());
        if (book.getImageData() != null) entity.setImageData(book.getImageData());
        entity.setReadAlready(book.isReadAlready());

        return bookRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> search(String term, int printYear, Pageable pageable) {
        return bookRepository.findBySearchParams(term, printYear, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> search(String term, int printYear, boolean readAlReady, Pageable pageable) {
        return bookRepository.findBySearchParamsAndReadAlready(term, printYear, readAlReady, pageable);
    }

    @Override
    public Book uploadFileData(Book book, MultipartFile file) throws IOException {

        if (!file.isEmpty()){
            String fileName = file.getOriginalFilename();

            book.setImageData(file.getBytes());
            book.setImageStr(fileName);
        }

        return book;
    }
}
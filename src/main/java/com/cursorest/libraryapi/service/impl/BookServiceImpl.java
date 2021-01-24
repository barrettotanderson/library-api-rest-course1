

package com.cursorest.libraryapi.service.impl;

import com.cursorest.libraryapi.exception.BusinessException;
import com.cursorest.libraryapi.model.entity.Book;
import com.cursorest.libraryapi.model.repository.BookRepository;
import org.springframework.stereotype.Service;

//anotação @service na classe: utilizada para que seja gerenciada pelo spring framework
@Service
public class BookServiceImpl implements com.cursorest.libraryapi.service.BookService{

    private BookRepository repository;

    //O que é um JPA Repository
    // é a uma interface de um spring data JPA que vai servir para fazer operações com uma entidade na base de dados

    //Cria uma instância de book repository
    public BookServiceImpl(BookRepository repository){
        this.repository = repository;
    }

    @Override
    public Book save(Book book){

        if( repository.existsByIsbn(book.getIsbn()) ){
            throw new BusinessException("Isbn já cadastrado.");
        }

        return repository.save(book);
    }
}

package com.cursorest.libraryapi.model.repository;

import com.cursorest.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

//a interface recebe dois parametros: entidade que vai ser trabalhada e o tipo da propriedade da entidade que vai ser trabalhada (no caso, ID)
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
}

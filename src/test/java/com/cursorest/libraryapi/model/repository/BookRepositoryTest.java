package com.cursorest.libraryapi.model.repository;

import com.cursorest.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

//
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //vai criar uma instância do bd em memória apenas para executar os testes da classe e ao final do teste, vai apagar tudo
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager; //objeto utilizado para criar um cenário.. vai simular um entity manager

    //também será necessário injetar o repository que vai ter os testes executados
    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir o livro na base com o isbn informado.")
    public void returnTrueWhenIsbnExists(){
        //cenario
        String isbn = "123";

        //criar através do método persist utilizado para persistir uma entidade, no caso, persistir um book
        Book book = Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
        //o entityManager é utilizado dentro das implementações dos repositories
        //..para executar as operações da base de dados
        entityManager.persist(book);

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        //importado estaticamente do static jcore.. pq??
        assertThat(exists).isTrue(); //espero que retorne verdadeiro.. ou seja, que o livro cadastrado exista
    }


    @Test
    @DisplayName("Deve retornar falso quando não existir o livro na base com o isbn informado.")
    public void returnFalseWhenIsbnDoesntExist(){
        //cenario
        String isbn = "123";

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        //importado estaticamente do static jcore.. pq??
        assertThat(exists).isFalse(); //agora eu espero que retorne falso.. ou seja, que o livro não exista
    }


}

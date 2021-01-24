package com.cursorest.libraryapi.service;

import com.cursorest.libraryapi.exception.BusinessException;
import com.cursorest.libraryapi.model.entity.Book;
import com.cursorest.libraryapi.model.repository.BookRepository;
import com.cursorest.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service; //variavel que representa a interface

    @MockBean
    BookRepository repository;

    @BeforeEach //essa anotation faz com que o metodo abaixo seja executado antes de qualquer método nessa classe
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    //criação do primeiro método de teste
    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenario
        //criando uma instancia de Book utilizando lombok
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        //o metodo when é equivalente ao given, utilizado anteriormente
        Mockito.when(repository.save(book))
                .thenReturn(
                        Book.builder().id(11)
                                .isbn("123")
                                .author("Fulano")
                                .title("As aventuras").build()
                );

        //execucao
        Book savedBook = service.save(book);

        //verificacao
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        //cenario onde preciso de um livro para salvar
        Book book = createValidBook() ;
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificações
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        //vai verificar que o Mockito nunca irá executar o método com esse mesmo parâmetro
        Mockito.verify(repository, Mockito.never()).save(book);

    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

}

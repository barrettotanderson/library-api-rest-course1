package com.cursorest.libraryapi.api.resource;

//com o TDD nós vamos desenhar nossa API, e mais a frente vamos adicionar outras camadas e testando

import com.cursorest.libraryapi.api.dto.BookDTO;
import com.cursorest.libraryapi.exception.BusinessException;
import com.cursorest.libraryapi.model.entity.Book;
import com.cursorest.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest //testes unitarios, nao sera teste de integração
@AutoConfigureMockMvc //vai configurar um objeto para fazer requisicoes
public class BookControllerTest {

    @Autowired
    MockMvc mvc; //objeto que vai estar mockando as requisições

    //definição da rota
    static String BOOK_API = "/api/books";

    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.") //essa é a anotation do Junit5 para mostrar um nome para o teste/metodo
    //criação do primeiro teste
    public void createBookTest() throws Exception{

        BookDTO dto = createNewBook();
        Book savedBook = Book.builder().id(101).author("Anderson").title("As Aventuras").isbn("001").build();
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto);

        //para criar uma requisição, é necessário utilizar a classe MockMvcRequestBuilders
        //serve para definir um tipo de requisição
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders
            .post(BOOK_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        //agora é realizado a requisição de fato
        mvc
                .perform(request)
                .andExpect(status().isCreated()) //espero que retorne created
                //espero também que retorne um json com esses ids e com esses conteúdos
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()))
        ;

    }


    //essa validação é do tipo de integridade de objeto
    @Test
    //essa é a anotation do Junit5 para mostrar um nome para o teste/metodo
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do livro.")
    //criação do segundo teste
    public void createInvalidBookTest() throws Exception {
        //sera criado um novo json (BookDTO),porém vazio
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        //para criar uma requisição, é necessário utilizar a classe MockMvcRequestBuilders
        //serve para definir um tipo de requisição
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json); //o json enviado aqui estará nulo, por conta de estar enviando "new BookDTO"

        mvc.perform(request)
                .andExpect( status().isBadRequest() )  //para mampear o erro 400
                .andExpect( jsonPath("errors", hasSize(3))); //um array para 3 mensagens de erro.. uma para Title, author e isbn

    }


    //essa validação é do tipo de regras de negocio: não se pode cadastrar um livro que já esteja com o isbn cadastrado
    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro.")
    public void createBookWithDuplicatedIsbn() throws Exception{

        BookDTO dto = createNewBook();

        //sera criado um novo json (BookDTO),porém vazio
        String json = new ObjectMapper().writeValueAsString(dto);

        String mensagemErro = "Isbn já cadastrado.";
        //vou ter que simular que o serviço mandou a mensagem de erro
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(mensagemErro));

        //para criar uma requisição, é necessário utilizar a classe MockMvcRequestBuilders
        //serve para definir um tipo de requisição
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json); //o json enviado aqui estará nulo, por conta de estar enviando "new BookDTO"

        //execucao do request
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1))) //espero que os errors só tenha uma mensagem de erro
                .andExpect(jsonPath("errors[0]").value(mensagemErro));

    }

    private BookDTO createNewBook() {
        return BookDTO.builder().author("Anderson").title("As Aventuras").isbn("001").build();
    }

}

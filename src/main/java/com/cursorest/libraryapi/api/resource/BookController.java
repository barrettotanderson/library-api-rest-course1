package com.cursorest.libraryapi.api.resource;

import com.cursorest.libraryapi.api.dto.BookDTO;
import com.cursorest.libraryapi.api.exception.ApiErrors;
import com.cursorest.libraryapi.exception.BusinessException;
import com.cursorest.libraryapi.model.entity.Book;
import com.cursorest.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    //injetar o id aqui
    private BookService service;

    //cria uma instancia de ModelMapper
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper){
        this.service = service;
        this.modelMapper = mapper;
    }

    //esse método vaai tratar uma requição do tipo post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){ //A anotation @Valid vai fazer que o spring boot e o mvc valide o objeto, conforme as anotations em BookDTO
        //Book entity = Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build();
        //alterado/refatorado a linha acima para utilizar o ModelMapper
        Book entity = modelMapper.map( dto, Book.class );

        entity = service.save(entity);
        //return BookDTO.builder().id(entity.getId()).author(entity.getAuthor()).title(entity.getTitle()).isbn(entity.getIsbn()).build();
        //alterado/refatorado a linha acima para utilizar o ModelMapper
        return modelMapper.map(entity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex){
        //o binding result terá todas as mensagens de erros
        BindingResult bindingResult = ex.getBindingResult();

        return new ApiErrors(bindingResult);

    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){
        return new ApiErrors(ex);
    }

}

/*
* Como é bom acordar do seu lado
* Olhar você ainda dormindo
* Tocar sua pele, acareciar seus cabelos
* Admirar sua beleza pura
*
* Acordar com você é uma dádiva
* Mal posso esperar para que abra os olhos
* E ao me ver, diga bom dia
* Sim, com certeza será um bom dia
*
* Um abraço apertado
* Um apalpada bem forte no bumbum
* Um beijo no pescoço
* Um sussurro no ouvido: Eu te amo!
*
* Para começar, a hora do café
* Um cafézinho com pão na chapa
*
*
* To_do dia é bom quando o amor está presente
* Significa começar o dia
*
* Você é um anjo que me acompanha todas as noites
* */
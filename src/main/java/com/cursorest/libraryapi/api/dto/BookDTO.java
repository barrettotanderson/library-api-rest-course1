package com.cursorest.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@Builder  //faz com que o anotation do lombok gere um builder para a classe
@NoArgsConstructor
@AllArgsConstructor
//DTO: É uma classe simples com atributo simples
//.. utilizado para representar um json (os dados que irão vir da requisição)
public class BookDTO {
    private Integer id;

    //vamos colocar aqui as anotations de validação do objeto (@NotEmpty)
    //assim, espera-se que esses campos não venham mais nulos
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

}

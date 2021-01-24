package com.cursorest.libraryapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//vou anotar com as anotations do JPA
@Entity //a primeira é a entity do pacote javax.persistence.. essa anotation vai dizer que
//.. a classe Book é uma entidade
@Table
public class Book {
    //agora é necessário mapear os meus campos da tabela
    @Id //usado para mapear a chave primaria da entidade (identificador)
    //@Column(name = "codigo") //anotation utilizada se caso não quiser usar o nome padrão do campo abaixo
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Essa anotation diz que o campoid é auto incremento
    private Integer id;

    @Column //não é obrigatório colocar, mas foi colocado para seguir instrutor
    private String title;

    @Column //não é obrigatório colocar, mas foi colocado para seguir instrutor
    private String author;

    @Column //não é obrigatório colocar, mas foi colocado para seguir instrutor
    private String isbn;



}

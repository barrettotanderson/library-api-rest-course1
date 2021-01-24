package com.cursorest.libraryapi.api.exception;

import com.cursorest.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>(); //aqui é instanciado a variavel String errors

        //obtem a mensagem de erro para cada erro obtido da mensagem da validação (através da opção lambda)
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors(){
        return errors;
    }



}

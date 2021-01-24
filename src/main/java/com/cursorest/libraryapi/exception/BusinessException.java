
//não foi colocado do pacote api.exception.. pq não é específica da API, mas da regra de negócio do sistema inteiro
package com.cursorest.libraryapi.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }
}

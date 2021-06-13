package br.com.carlos.todolist.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.carlos.todolist.exception.comum.TodoListException;


/**
 * @author carlos.oliveira
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends TodoListException {

    public BadRequestException(String mensagem) {
        super(mensagem);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

package br.com.carlos.todolist.exception.comum;


import org.springframework.http.HttpStatus;


/**
 * Exception padrão para controle e log das exceções internas
 *
 * @author carlos.oliveira
 */
public abstract class TodoListException extends RuntimeException {

    public TodoListException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();
}

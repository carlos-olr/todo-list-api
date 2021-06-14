package br.com.carlos.todolist.exception.comum;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Handler geral pra exceções internas controladas pela classe {@link TodoListException} e aplicar log padrão para monitoramento
 *
 * @author carlos.oliveira
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = { TodoListException.class })
    protected ResponseEntity<Object> handleException(TodoListException ex, WebRequest request) {
        LOGGER.error("ERRO - " + ((ServletWebRequest)request).getRequest().getRequestURI());

        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getStatus(), request);
    }

}

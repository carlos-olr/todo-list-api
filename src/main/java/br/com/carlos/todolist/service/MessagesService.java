package br.com.carlos.todolist.service;


import static java.util.Locale.getDefault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;


/**
 * @author carlos.oliveira
 */
public abstract class MessagesService {

    @Autowired
    private MessageSource messages;

    protected String getMessage(String chave, @Nullable Object... valores) {
        return this.messages.getMessage(chave, valores, getDefault());
    }

}

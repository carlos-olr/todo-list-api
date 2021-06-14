package br.com.carlos.todolist.service;


import static java.util.Locale.getDefault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;


/**
 * Classe abstrata para resolver problema comum nos Services, geração de mensagens com base na api Messages.properties
 *
 * @author carlos.oliveira
 */
public abstract class MessagesService {

    @Autowired
    private MessageSource messages;

    /**
     * Gera uma String com base na chave passado em relação ao message.properties
     *
     * @param chave chave do messages.properties
     * @param valores valores que podem ser sobrepostos nas mensagens
     * @return mensagem gerada
     */
    protected String getMessage(String chave, @Nullable Object... valores) {
        return this.messages.getMessage(chave, valores, getDefault());
    }

}

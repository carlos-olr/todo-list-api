package br.com.carlos.todolist.model.comum;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Classe comum entre os jsons para compartilhar recursos repetitivos
 *
 * @author carlos.oliveira
 */
public abstract class Json {

    /**
     * @return objeto atual no formato Json com base na API Jackson
     */
    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }


}

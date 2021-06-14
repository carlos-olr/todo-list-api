package br.com.carlos.todolist.security;


import br.com.carlos.todolist.model.comum.Json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Bean simples para servir para geração de token
 *
 * @author carlos.oliveira
 */
@Getter
@Setter
@NoArgsConstructor
public class User extends Json {

    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}

package br.com.carlos.todolist.security;


import org.springframework.stereotype.Component;

import br.com.carlos.todolist.model.Usuario;


/**
 * @author carlos.oliveira
 */
@Component
public class ContextoUsuario {

    private static final ThreadLocal<Usuario> USUARIO_THREAD_LOCAL = new ThreadLocal<>();

    public void remove() {
        USUARIO_THREAD_LOCAL.set(null);
    }

    public Usuario getContext() {
        return USUARIO_THREAD_LOCAL.get();
    }

    public void set(Usuario userContext) {
        USUARIO_THREAD_LOCAL.set(userContext);
    }

}

package br.com.carlos.todolist.security;


import org.springframework.stereotype.Component;

import br.com.carlos.todolist.model.Usuario;


/**
 * Componente que permite gerenciar em nível de Thread uma variável que carrega o Usuário logado a partir do token
 * Permitindo assim fácil acesso ao usuário e facilitando testes
 * 
 * @author carlos.oliveira
 */
@Component
public class ContextoUsuario {

    /**
     * ThreadLocal é um recuso que permite criar variáveis com múltiplos valores variados por Thread
     */
    private static final ThreadLocal<Usuario> USUARIO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * Faz a limpeza da variável na thread atual
     */
    public void clear() {
        USUARIO_THREAD_LOCAL.set(null);
    }

    /**
     * @return {@link Usuario} salvo na Thread
     */
    public Usuario getUsuarioLogado() {
        return USUARIO_THREAD_LOCAL.get();
    }

    /**
     * Salva um {@link Usuario} na thread atual, sobrescrevendo outra que já exista
     *
     * @param usuario {@link Usuario} a ser salvo na Thread
     */
    public void setUsuarioLodado(Usuario usuario) {
        USUARIO_THREAD_LOCAL.set(usuario);
    }

}

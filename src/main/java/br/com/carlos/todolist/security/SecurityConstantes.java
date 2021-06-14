package br.com.carlos.todolist.security;


/**
 * Constantes gerais para autenticação
 *
 * @author carlos.oliveira
 */
public class SecurityConstantes {

    /**
     * Segredo para os tokens gerado aleatoriamente
     */
    public static final String SECRET = "jPrdsRzhrVH1Fn5aJUMj";
    public static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutos
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final String LOGIN_URL = "/auth";

}

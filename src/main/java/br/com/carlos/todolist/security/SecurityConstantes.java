package br.com.carlos.todolist.security;


/**
 * @author carlos.oliveira
 */
public class SecurityConstantes {

    public static final String SECRET = "jPrdsRzhrVH1Fn5aJUMj";
    public static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/user";
    public static final String LOGIN_URL = "/auth";

}

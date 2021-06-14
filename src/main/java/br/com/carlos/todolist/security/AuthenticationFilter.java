package br.com.carlos.todolist.security;


import static br.com.carlos.todolist.security.SecurityConstantes.EXPIRATION_TIME;
import static br.com.carlos.todolist.security.SecurityConstantes.LOGIN_URL;
import static br.com.carlos.todolist.security.SecurityConstantes.SECRET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.carlos.todolist.model.Usuario;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;


/**
 * Filtro para Autenticação de usuários com base em Login e Senha
 *
 * @author carlos.oliveira
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    /**
     * Inicialização do filtro indicando a URL padrão para geração de tokens
     */
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl(LOGIN_URL);
    }

    /**
     * Método que executa a autenticação com base em login e senha, esse método se relaciona diretamente a classe
     * {@link br.com.carlos.todolist.service.UsuarioService} pela interface {@link UserDetailsService} onde será executado
     * o método br.com.carlos.todolist.service.UsuarioService#loadUserByUsername(java.lang.String) e após o usuário
     * ser encontrado será feito o match da senha pelo {@link BCryptPasswordEncoder}
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        User creds = new ObjectMapper().readValue(req.getInputStream(), User.class);
        LOGGER.info("GERAR-TOKEN-login - " + creds.getLogin());

        try {
            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getLogin(), creds.getPassword(), new ArrayList<>()));
        } catch (Exception e) {
            LOGGER.warn("FALHA-GERAR-TOKEN-login - " + creds.getLogin());
            throw e;
        }

    }

    /**
     * Após o sucesso na autenticação será gerado token para utilização nas requests
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException {
        Usuario usuario = ((Usuario) auth.getPrincipal());

        String usuarioJson = new ObjectMapper().writeValueAsString(usuario);

        String token = JWT.create().withSubject(usuarioJson)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        res.getWriter().write(token);
        res.getWriter().flush();
    }

}

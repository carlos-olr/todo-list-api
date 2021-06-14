package br.com.carlos.todolist.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


/**
 * Handler de exceções específico para casos de "Forbidden Access" oriunda de requests sem Token de Autorização
 *
 * @author carlos.oliveira
 */
@Component
public class AuthenticationForbiddenHandler implements AuthenticationEntryPoint {

    Logger LOGGER = LoggerFactory.getLogger(AuthenticationForbiddenHandler.class);

    /**
     * Tratamento da exceção para melhoria da mensagem e também log
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
            throws IOException, ServletException {
        LOGGER.error("FALHA-ACESSO-NAO-PERMITIDO - " + req.getRequestURI());
        res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso não permitido");
    }
}

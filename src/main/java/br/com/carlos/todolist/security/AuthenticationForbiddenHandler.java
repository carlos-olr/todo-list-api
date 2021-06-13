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
 * @author carlos.oliveira
 */
@Component
public class AuthenticationForbiddenHandler implements AuthenticationEntryPoint {

    Logger LOGGER = LoggerFactory.getLogger(AuthenticationForbiddenHandler.class);

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
            throws IOException, ServletException {
        LOGGER.error("FALHA-ACESSO-NAO-PERMITIDO - " + req.getContextPath());
        res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso não permitido");
    }
}

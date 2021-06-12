package br.com.carlos.todolist.interceptor;


import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.security.ContextoUsuario;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;


/**
 * @author carlos.oliveira
 */
@Component
public class ContextoUsuarioInteceptor implements HandlerInterceptor {

    @Autowired
    private ContextoUsuario contextoUsuario;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorization = request.getHeader("Authorization");

        String token = authorization.split("\\s")[1];
        Usuario usuario = Usuario.fromJson(JWT.decode(token).getSubject());

        this.contextoUsuario.set(usuario);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        this.contextoUsuario.remove();
    }

}

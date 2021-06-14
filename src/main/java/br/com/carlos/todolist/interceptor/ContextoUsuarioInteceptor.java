package br.com.carlos.todolist.interceptor;


import static br.com.carlos.todolist.security.SecurityConstantes.HEADER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.ContextoUsuario;

import com.auth0.jwt.JWT;


/**
 * Interceptor que executa a operação de "pendurar" na Thread atual quem é o usuário que está logado a partir do Token
 * de autenticação
 *
 * @author carlos.oliveira
 */
@Component
public class ContextoUsuarioInteceptor implements HandlerInterceptor {

    @Autowired
    private ContextoUsuario contextoUsuario;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * INICIO DO TRATAMENTO DAS REQUESTS
     *
     * Com base no Header "Authorization" será recuperado o {@link Usuario} logado e com isso o mesmo será
     * recuperado do Banco de Dados com o objetivo de trazer as informações atuais do mesmo, e também permitindo que o
     * token não precise carregar dados desnecessários do usuário na autenticação
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorization = request.getHeader(HEADER);
        if (authorization != null) {
            String token = authorization.split("\\s")[1];
            Usuario usuario = Usuario.fromJson(JWT.decode(token).getSubject());

            Usuario usuaarioBD = this.usuarioRepository.findByLogin(usuario.getLogin());

            this.contextoUsuario.setUsuarioLodado(usuaarioBD);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    /**
     * FIM DO TRATAMENTO DAS REQUESTS
     *
     * Sempre ao final de cada request o {@link Usuario} "pendurado" na Thread é limpo, independente de se ter
     * algo salvo ou não
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        this.contextoUsuario.clear();
    }

}

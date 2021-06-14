package br.com.carlos.todolist.interceptor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.carlos.todolist.interceptor.ContextoUsuarioInteceptor;


/**
 * Configuração geral para interceptors da aplicação.
 * Esses interceptors serão aplicados após a validação de autenticação, caso o request seja autenticada
 *
 * @author carlos.oliveira
 */
@Component
public class InterceptorsConfig implements WebMvcConfigurer {

    @Autowired
    private ContextoUsuarioInteceptor contextoUsuarioInteceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.contextoUsuarioInteceptor);
    }
}

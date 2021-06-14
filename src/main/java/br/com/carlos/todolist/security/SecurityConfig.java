package br.com.carlos.todolist.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.carlos.todolist.service.UsuarioService;


/**
 * Bean para configurações de segurança da aplicação
 *
 * @author carlos.oliveira
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AuthenticationForbiddenHandler authenticationForbidden;

    /**
     * Configs gerais de segurança
     * 1 - todas as requests "/todo" precisam ser autenticadas
     * 2 - outras requests estão liberadas facilitando a configuração de criação de usuários e geração de token
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers("/todo/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .exceptionHandling().authenticationEntryPoint(this.authenticationForbidden).and()
                .csrf().disable(); // desabilitei para execução via postman/curl


    }

    /**
     * método que faz a ligação {@link UsuarioService}, {@link BCryptPasswordEncoder} com o sistema de segurança
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.usuarioService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    /**
     * Configuração geral simplificada para CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

}

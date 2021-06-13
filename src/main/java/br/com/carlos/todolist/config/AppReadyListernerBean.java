package br.com.carlos.todolist.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.service.UsuarioService;


/**
 * @author carlos.oliveira
 */
@Component
public class AppReadyListernerBean implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String loginSuper = "adminSuper";
        String passSuper = "passwordSuper";

        Usuario usuariopSuperDB = this.usuarioRepository.findByLogin(loginSuper);
        if (usuariopSuperDB == null) {
            Usuario usuarioSuper = new Usuario(loginSuper, this.bCryptPasswordEncoder.encode(passSuper), true);

            this.usuarioRepository.save(usuarioSuper);
        }
    }
}

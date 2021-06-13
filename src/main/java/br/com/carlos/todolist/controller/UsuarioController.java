package br.com.carlos.todolist.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.service.UsuarioService;


/**
 * Controller para as operações relacionadas ao usuário
 *
 * @author carlos.oliveira
 */
@RestController
public class UsuarioController {

    Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Usuario putUsuario(@RequestBody Usuario usuario) {
        LOGGER.info("SAVE-USUARIO" + usuario.toJson());
        Usuario usuarioSalvo = this.usuarioService.salvar(usuario);
        usuarioSalvo.setPassword(null);
        return usuarioSalvo;
    }

}

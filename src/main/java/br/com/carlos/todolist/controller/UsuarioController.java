package br.com.carlos.todolist.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;


/**
 * Controller para as operações relacionadas ao usuário
 *
 * @author carlos.oliveira
 */
@Api(tags = {"Usuários"})
@SwaggerDefinition(tags = {
        @Tag(name = "Usuários", description = "Endpoints para manipulação de Usuários")
})
@RestController
public class UsuarioController {

    Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para criação de {@link Usuario} a partir de API JSON
     *
     * @param usuario {@link Usuario} recebido como JSON
     * @return {@link Usuario} simplificado apenas com login
     */
    @ApiOperation("Criação de Usuários")
    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Usuario putUsuario(@RequestBody Usuario usuario) {
        LOGGER.info("SAVE-USUARIO" + usuario.toJson());
        Usuario usuarioSalvo = this.usuarioService.salvar(usuario);
        usuarioSalvo.setPassword(null);
        return usuarioSalvo;
    }

}

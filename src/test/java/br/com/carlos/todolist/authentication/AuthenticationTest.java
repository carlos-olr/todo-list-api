package br.com.carlos.todolist.authentication;


import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.User;
import br.com.carlos.todolist.service.UsuarioService;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class AuthenticationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void limparRegistros() {
        this.usuarioRepository.deleteAll();
    }

    @Test
    public void tentativaLoginUsuarioNaoExistente() throws Exception {
        this.mvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new User("user", "teste").toJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void tentativaLoginUsuarioExistenteMasComSenhaErrada() throws Exception {
        this.usuarioService.salvar(new Usuario("user", "teste"));

        this.mvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new User("user", "teste1").toJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginComSucesso() throws Exception {
        this.usuarioService.salvar(new Usuario("user", "teste"));

        this.mvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new User("user", "teste").toJson()))
                .andExpect(status().isOk());
    }

}

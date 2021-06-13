package br.com.carlos.todolist.authentication;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.todolist.comum.TodoListTest;
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.User;
import br.com.carlos.todolist.service.UsuarioService;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest extends TodoListTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

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

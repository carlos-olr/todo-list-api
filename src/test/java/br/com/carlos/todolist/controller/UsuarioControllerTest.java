package br.com.carlos.todolist.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.carlos.todolist.comum.TodoListTest;
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.User;
import br.com.carlos.todolist.service.UsuarioService;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UsuarioControllerTest extends TodoListTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void criarUsuario() throws Exception {
        MvcResult mvcResult = this.mvc.perform(put("/user")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(new User("user", "teste").toJson()))   //
                .andExpect(status().isOk())                     //
                .andReturn();

        Usuario usuarioRetornado = Usuario.fromJson(mvcResult.getResponse().getContentAsString());
        assertNotNull(usuarioRetornado.getId());
        assertNotNull(usuarioRetornado.getLogin());
        assertNull(usuarioRetornado.getPassword());

        Usuario usuarioEncontrado = this.usuarioRepository.findByLogin("user");
        assertNotNull(usuarioEncontrado);
        assertNotEquals("teste", usuarioEncontrado.getPassword());
    }

    @Test
    public void criarUsuario_semLogin() throws Exception {
        this.mvc.perform(put("/user") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(new User(null, "teste").toJson())) //
                .andExpect(status().isBadRequest()) //
                .andExpect(result -> assertEquals("'login' não informado", result.getResponse().getContentAsString()));
    }

    @Test
    public void criarUsuario_semPassword() throws Exception {
        this.mvc.perform(put("/user") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(new User("user", null).toJson())) //
                .andExpect(status().isBadRequest()) //
                .andExpect(result -> assertEquals("'password' não informado", result.getResponse().getContentAsString()));
    }

}

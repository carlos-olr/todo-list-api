package br.com.carlos.todolist.controller;


import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.User;
import br.com.carlos.todolist.service.UsuarioService;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class UsuarioControllerTest {

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

}

package br.com.carlos.todolist.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.carlos.todolist.comum.TodoListTest;
import br.com.carlos.todolist.exception.BadRequestException;
import br.com.carlos.todolist.model.Usuario;


@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioServiceTest extends TodoListTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void criarUsuario() {
        Usuario usuario = new Usuario("user", "teste");

        this.usuarioService.salvar(usuario);

        List<Usuario> salvosBD = this.usuarioRepository.findAll();

        assertEquals(salvosBD.size(), 1);
        assertEquals("user", salvosBD.get(0).getLogin());
        assertTrue(this.bCryptPasswordEncoder.matches("teste", salvosBD.get(0).getPassword()));
    }

    @Test
    public void criarUsuario_naoPodeAlterar() {
        Usuario usuario = new Usuario("user", "teste");

        this.usuarioService.salvar(usuario);

        List<Usuario> salvosBD = this.usuarioRepository.findAll();

        assertEquals(salvosBD.size(), 1);
        assertEquals("user", salvosBD.get(0).getLogin());
        assertTrue(this.bCryptPasswordEncoder.matches("teste", salvosBD.get(0).getPassword()));

        usuario = new Usuario("user", "teste1010");

        try {
            this.usuarioService.salvar(usuario);
            fail();
        } catch (BadRequestException ex) {
            assertEquals("Já existe um usuário com o login \"user\"", ex.getMessage());
        }
    }

}

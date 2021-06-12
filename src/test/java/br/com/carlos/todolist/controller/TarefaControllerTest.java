package br.com.carlos.todolist.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import br.com.carlos.todolist.model.StatusTarefa;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.TarefaRepository;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.User;
import br.com.carlos.todolist.service.UsuarioService;

import lombok.SneakyThrows;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TarefaRepository tarefaRepository;

    @BeforeEach
    public void limparCenario() {
        this.tarefaRepository.deleteAll();
        this.usuarioRepository.deleteAll();
    }

    @Test
    public void criarTarefa() throws Exception {
        String token = this.getToken("user", "teste");

        Tarefa tarefa = new Tarefa();
        tarefa.setReusmo("resumo");
        tarefa.setDescricao("descricao");

        MvcResult mvcResult = this.mvc.perform(put("/todo")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefa.toJson())                       //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        Tarefa tarefaRetornada = Tarefa.fromJson(mvcResult.getResponse().getContentAsString());
        assertNotNull(tarefaRetornada.getId());
        assertNotNull(tarefaRetornada.getStatusTarefa());
        assertEquals(StatusTarefa.PENDING, tarefaRetornada.getStatusTarefa());
        assertNotNull(tarefaRetornada.getDataInclusao());
        assertNotNull(tarefaRetornada.getDataAlteracao());

        Tarefa tarefaBD = this.tarefaRepository.findById(tarefaRetornada.getId()).get();
        assertNotNull(tarefaBD);
    }

    @SneakyThrows
    private String getToken(String login, String password) {
        Usuario usuario = new Usuario(login, password);

        this.usuarioService.salvar(usuario);

        MvcResult mvcResult = this.mvc.perform(put("/auth")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(new User(login, password).toJson()))   //
                .andExpect(status().isOk())                     //
                .andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

}

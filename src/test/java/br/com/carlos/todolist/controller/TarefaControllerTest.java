package br.com.carlos.todolist.controller;


import static br.com.carlos.todolist.model.StatusTarefa.COMPLETED;
import static br.com.carlos.todolist.model.StatusTarefa.PENDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import br.com.carlos.todolist.comum.TodoListTest;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.model.Usuario;


@SpringBootTest
@AutoConfigureMockMvc
public class TarefaControllerTest extends TodoListTest {

    @Test
    public void criarTarefa() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");
        String token = this.getToken(usuario);

        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");

        MvcResult mvcResult = this.mvc.perform(put("/todo")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefa.toJson())                       //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        Tarefa tarefaRetornada = Tarefa.fromJson(mvcResult.getResponse().getContentAsString());
        assertNotNull(tarefaRetornada.getId());
        assertNotNull(tarefaRetornada.getStatus());
        assertEquals(PENDING, tarefaRetornada.getStatus());
        assertNotNull(tarefaRetornada.getDataInclusao());
        assertNotNull(tarefaRetornada.getDataAlteracao());

        Tarefa tarefaBD = this.tarefaRepository.findById(tarefaRetornada.getId()).get();
        assertNotNull(tarefaBD);
        assertEquals("resumo", tarefaBD.getResumo());
        assertEquals("descricao", tarefaBD.getDescricao());
    }

    @Test
    public void alterarTarefa() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");
        String token = this.getToken(usuario);

        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");

        MvcResult mvcResult = this.mvc.perform(put("/todo")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefa.toJson())                       //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        Tarefa tarefaRetornada = Tarefa.fromJson(mvcResult.getResponse().getContentAsString());

        tarefaRetornada.setStatus(COMPLETED);

        this.mvc.perform(post("/todo/" + tarefaRetornada.getId())     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefaRetornada.toJson())                       //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        Tarefa tarefaBD = this.tarefaRepository.findById(tarefaRetornada.getId()).get();
        assertNotNull(tarefaBD);
        assertEquals(COMPLETED, tarefaBD.getStatus());
    }

    @Test
    public void deletarTarefa() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");
        String token = this.getToken(usuario);

        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");

        MvcResult mvcResult = this.mvc.perform(put("/todo")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefa.toJson())                       //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        Tarefa tarefaRetornada = Tarefa.fromJson(mvcResult.getResponse().getContentAsString());

        Tarefa tarefaBD = this.tarefaRepository.findById(tarefaRetornada.getId()).get();
        assertNotNull(tarefaBD);

        this.mvc.perform(delete("/todo") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(tarefaBD.toJson()) //
                .header("Authorization", "Bearer " + token)) //
                .andExpect(status().isOk()) //
                .andReturn();

        assertTrue(this.tarefaRepository.findById(tarefaRetornada.getId()).isEmpty());
    }

    @Test
    public void deletarTarefa_comParametro() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");
        String token = this.getToken(usuario);

        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");

        MvcResult mvcResult = this.mvc.perform(put("/todo")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefa.toJson())                       //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        Tarefa tarefaRetornada = Tarefa.fromJson(mvcResult.getResponse().getContentAsString());

        Tarefa tarefaBD = this.tarefaRepository.findById(tarefaRetornada.getId()).get();
        assertNotNull(tarefaBD);

        this.mvc.perform(delete("/todo/" + tarefaBD.getId()) //
                .contentType(MediaType.APPLICATION_JSON) //
                .header("Authorization", "Bearer " + token)) //
                .andExpect(status().isOk()) //
                .andReturn();

        assertTrue(this.tarefaRepository.findById(tarefaRetornada.getId()).isEmpty());
    }

    @Test
    public void criarTarefa_SemToken() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");

        this.mvc.perform(put("/todo")                           //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(tarefa.toJson()))                      //
                .andExpect(status().isForbidden())              //
                .andExpect(result -> assertEquals("Acesso não permitido", result.getResponse().getErrorMessage()));
    }

    @Test
    public void listarTrefas_pendentesNoTopo() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING);
        this.criarTarefa("t3", "tarefa3", usuario, PENDING);

        assertEquals(3, this.tarefaRepository.findAll().size());

        String token = this.getToken(usuario);

        MvcResult mvcResult = this.mvc.perform(get("/todo")     //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        List<Tarefa> tarefas = this.getResponseAsObject(mvcResult, Tarefa.class);

        assertEquals(3, tarefas.size());
        assertEquals(PENDING, tarefas.get(0).getStatus());
        assertEquals("t2", tarefas.get(0).getResumo());
        assertEquals(PENDING, tarefas.get(1).getStatus());
        assertEquals("t3", tarefas.get(1).getResumo());
        assertEquals(COMPLETED, tarefas.get(2).getStatus());
        assertEquals("t1", tarefas.get(2).getResumo());
    }

    @Test
    public void listarTrefas_tarefasDeOutroUsuarioNaoAparecem() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING);
        this.criarTarefa("t3", "tarefa3", usuario, PENDING);

        assertEquals(3, this.tarefaRepository.findAll().size());

        Usuario outroUsuario = this.criarUsuario("user1", "teste1");
        String token = this.getToken(outroUsuario);

        MvcResult mvcResult = this.mvc.perform(get("/todo")     //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        List<Tarefa> tarefas = this.getResponseAsObject(mvcResult, Tarefa.class);

        assertTrue(tarefas.isEmpty());
    }

    @Test
    public void listarTrefas_tarefasDeOutrosUsuariosAparecemParaSuperUsuario() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING);
        this.criarTarefa("t3", "tarefa3", usuario, PENDING);

        assertEquals(3, this.tarefaRepository.findAll().size());

        Usuario superUsuario = this.criarSuperUsuario("user1", "teste1");
        String token = this.getToken(superUsuario);

        MvcResult mvcResult = this.mvc.perform(get("/todo")     //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        List<Tarefa> tarefas = this.getResponseAsObject(mvcResult, Tarefa.class);

        assertEquals(3, tarefas.size());
        assertEquals(PENDING, tarefas.get(0).getStatus());
        assertEquals("t2", tarefas.get(0).getResumo());
        assertEquals(PENDING, tarefas.get(1).getStatus());
        assertEquals("t3", tarefas.get(1).getResumo());
        assertEquals(COMPLETED, tarefas.get(2).getStatus());
        assertEquals("t1", tarefas.get(2).getResumo());
    }

    @Test
    public void listarTraefas_semToken() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING);
        this.criarTarefa("t3", "tarefa3", usuario, PENDING);

        assertEquals(3, this.tarefaRepository.findAll().size());

        this.mvc.perform(get("/todo")) //
                .andExpect(status().isForbidden())
                .andExpect(result -> assertEquals("Acesso não permitido", result.getResponse().getErrorMessage()));

    }

    @Test
    public void listarTrefas_pendentesNoTopoMaisOrdenacaoPorData() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING, "2021-01-05 00:00:00", "2021-01-05 00:00:00");
        this.criarTarefa("t3", "tarefa3", usuario, PENDING, "2021-01-01 00:00:00", "2021-01-01 00:00:00");

        assertEquals(3, this.tarefaRepository.findAll().size());

        String token = this.getToken(usuario);

        MvcResult mvcResult = this.mvc.perform(get("/todo")     //
                .header("Authorization", "Bearer " + token))    //
                .andExpect(status().isOk())                     //
                .andReturn();

        List<Tarefa> tarefas = this.getResponseAsObject(mvcResult, Tarefa.class);

        assertEquals(3, tarefas.size());
        assertEquals(PENDING, tarefas.get(0).getStatus());
        assertEquals("t3", tarefas.get(0).getResumo());
        assertEquals(PENDING, tarefas.get(1).getStatus());
        assertEquals("t2", tarefas.get(1).getResumo());
        assertEquals(COMPLETED, tarefas.get(2).getStatus());
        assertEquals("t1", tarefas.get(2).getResumo());
    }

    @Test
    public void listarTarefas_pendentes() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING);

        assertEquals(2, this.tarefaRepository.findAll().size());

        String token = this.getToken(usuario);

        MvcResult mvcResult = this.mvc.perform(get("/todo") //
                .param("status", "PENDING") //
                .header("Authorization", "Bearer " + token)) //
                .andExpect(status().isOk()) //
                .andReturn();

        List<Tarefa> tarefas = this.getResponseAsObject(mvcResult, Tarefa.class);

        assertEquals(1, tarefas.size());
        assertEquals(PENDING, tarefas.get(0).getStatus());
        assertEquals("t2", tarefas.get(0).getResumo());
    }

    @Test
    public void listarTrefas_completas() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.criarTarefa("t1", "tarefa1", usuario, COMPLETED);
        this.criarTarefa("t2", "tarefa2", usuario, PENDING);

        assertEquals(2, this.tarefaRepository.findAll().size());

        String token = this.getToken(usuario);

        MvcResult mvcResult = this.mvc.perform(get("/todo") //
                .param("status", "COMPLETED") //
                .header("Authorization", "Bearer " + token)) //
                .andExpect(status().isOk()) //
                .andReturn();

        List<Tarefa> tarefas = this.getResponseAsObject(mvcResult, Tarefa.class);

        assertEquals(1, tarefas.size());
        assertEquals(COMPLETED, tarefas.get(0).getStatus());
        assertEquals("t1", tarefas.get(0).getResumo());
    }

}

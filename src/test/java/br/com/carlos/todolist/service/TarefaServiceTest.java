package br.com.carlos.todolist.service;


import static br.com.carlos.todolist.model.StatusTarefa.COMPLETED;
import static br.com.carlos.todolist.model.StatusTarefa.PENDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.carlos.todolist.comum.TodoListTest;
import br.com.carlos.todolist.exception.BadRequestException;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.model.Usuario;

import lombok.SneakyThrows;


@SpringBootTest
@AutoConfigureMockMvc
public class TarefaServiceTest extends TodoListTest {

    @Test
    public void criarTarefa() {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.contextoUsuario.set(usuario);

        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");

        Long id = this.tarefaService.salvarTarefa(tarefa).getId();

        Tarefa tarefaBD = this.tarefaRepository.findById(id).get();
        assertNotNull(tarefaBD);
        assertEquals("resumo", tarefaBD.getResumo());
        assertEquals("descricao", tarefaBD.getDescricao());
        assertEquals(0, (int) tarefaBD.getCodigoStatus());
        assertEquals(PENDING, tarefaBD.getStatus());
    }

    @Test
    @SneakyThrows
    public void editarTarefa() {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.contextoUsuario.set(usuario);

        Date dataBase = dateFormat.parse("2021-01-01 00:00:00");

        Tarefa tarefa = this.criarTarefa("t1", "d1", usuario, PENDING, dataBase, dataBase).clone();

        tarefa.setResumo("t1.modificado");
        tarefa.setDescricao("d1.modificado");

        this.tarefaService.salvarTarefa(tarefa);

        Tarefa tarefaBD = this.tarefaRepository.findById(tarefa.getId()).get();
        assertNotNull(tarefaBD);
        assertEquals("t1.modificado", tarefaBD.getResumo());
        assertEquals("d1.modificado", tarefaBD.getDescricao());
        assertEquals(0, (int) tarefaBD.getCodigoStatus());
        assertEquals(PENDING, tarefaBD.getStatus());
        assertEquals(dataBase, tarefaBD.getDataInclusao());
        assertNotEquals(dataBase, tarefaBD.getDataAlteracao());
    }

    @Test
    @SneakyThrows
    public void editarTarefa_idNaoExistente() {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.contextoUsuario.set(usuario);

        Date dataBase = dateFormat.parse("2021-01-01 00:00:00");

        Tarefa tarefa = this.criarTarefa("t1", "d1", usuario, PENDING, dataBase, dataBase).clone();
        tarefa.setId(250L);

        try {
            this.tarefaService.salvarTarefa(tarefa);
            fail();
        } catch (BadRequestException ex) {
            assertEquals("Não foi encontrada tarefa com o id \"250\"", ex.getMessage());
        }
    }

    @Test
    @SneakyThrows
    public void editarTarefa_usuarioNaoPodeAlterarTarefa() {
        Usuario usuario = this.criarUsuario("user", "teste");
        this.contextoUsuario.set(usuario);

        Date dataBase = dateFormat.parse("2021-01-01 00:00:00");

        Tarefa tarefa = this.criarTarefa("t1", "d1", usuario, PENDING, dataBase, dataBase).clone();

        Usuario outroUsuario = this.criarUsuario("user1", "teste1");
        this.contextoUsuario.set(outroUsuario);

        try {
            this.tarefaService.salvarTarefa(tarefa);
            fail();
        } catch (BadRequestException ex) {
            assertEquals("Usuário não pode manipular a tareva pois a mesma pertence a outro usuário", ex.getMessage());
        }
    }

    @SneakyThrows
    public void deletarTarefa() {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.contextoUsuario.set(usuario);

        Date dataBase = dateFormat.parse("2021-01-01 00:00:00");

        Tarefa tarefa = this.criarTarefa("t1", "d1", usuario, PENDING, dataBase, dataBase).clone();

        this.tarefaService.deletar(tarefa);

        assertTrue(this.tarefaRepository.findById(tarefa.getId()).isEmpty());
    }

    @Test
    @SneakyThrows
    public void deletarTarefa_idNaoExistente() {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.contextoUsuario.set(usuario);

        Date dataBase = dateFormat.parse("2021-01-01 00:00:00");

        Tarefa tarefa = this.criarTarefa("t1", "d1", usuario, PENDING, dataBase, dataBase).clone();
        tarefa.setId(250L);

        try {
            this.tarefaService.deletar(tarefa);
            fail();
        } catch (BadRequestException ex) {
            assertEquals("Não foi encontrada tarefa com o id \"250\"", ex.getMessage());
        }
    }

    @Test
    @SneakyThrows
    public void deletarTarefa_usuarioNaoPodeAlterarTarefa() {
        Usuario usuario = this.criarUsuario("user", "teste");
        this.contextoUsuario.set(usuario);

        Date dataBase = dateFormat.parse("2021-01-01 00:00:00");

        Tarefa tarefa = this.criarTarefa("t1", "d1", usuario, PENDING, dataBase, dataBase).clone();

        Usuario outroUsuario = this.criarUsuario("user1", "teste1");
        this.contextoUsuario.set(outroUsuario);

        try {
            this.tarefaService.deletar(tarefa);
            fail();
        } catch (BadRequestException ex) {
            assertEquals("Usuário não pode manipular a tareva pois a mesma pertence a outro usuário", ex.getMessage());
        }
    }

    @Test
    public void criarTarefa_completed() throws Exception {
        Usuario usuario = this.criarUsuario("user", "teste");

        this.contextoUsuario.set(usuario);

        Tarefa tarefa = new Tarefa();
        tarefa.setResumo("resumo");
        tarefa.setDescricao("descricao");
        tarefa.setUsuario(usuario);
        tarefa.setStatus(COMPLETED);

        Long id = this.tarefaService.salvarTarefa(tarefa).getId();

        Tarefa tarefaBD = this.tarefaRepository.findById(id).get();
        assertNotNull(tarefaBD);
        assertEquals("resumo", tarefaBD.getResumo());
        assertEquals("descricao", tarefaBD.getDescricao());
        assertEquals(1, (int) tarefaBD.getCodigoStatus());
        assertEquals(COMPLETED, tarefaBD.getStatus());
    }

}

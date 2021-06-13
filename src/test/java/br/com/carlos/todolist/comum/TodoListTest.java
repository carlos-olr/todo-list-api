package br.com.carlos.todolist.comum;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.carlos.todolist.model.StatusTarefa;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.TarefaRepository;
import br.com.carlos.todolist.repository.UsuarioRepository;
import br.com.carlos.todolist.security.ContextoUsuario;
import br.com.carlos.todolist.security.User;
import br.com.carlos.todolist.service.TarefaService;
import br.com.carlos.todolist.service.UsuarioService;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import lombok.SneakyThrows;


/**
 * @author carlos.oliveira
 */
public abstract class TodoListTest {

    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected TarefaRepository tarefaRepository;
    @Autowired
    protected UsuarioRepository usuarioRepository;
    @Autowired
    protected UsuarioService usuarioService;
    @Autowired
    protected ContextoUsuario contextoUsuario;
    @Autowired
    protected TarefaService tarefaService;

    @BeforeEach
    public void limparCenario() {
        this.tarefaRepository.deleteAll();
        this.usuarioRepository.deleteAll();
    }

    protected Tarefa criarTarefa(String resumo, String descricao, Usuario usuario, StatusTarefa status) {
        return this.criarTarefa(resumo, descricao, usuario, status, new Date(), new Date());
    }

    @SneakyThrows
    protected Tarefa criarTarefa(String resumo, String descricao, Usuario usuario, StatusTarefa status,
            String dataInclusao, String dataAlteracao) {
        return this.criarTarefa(resumo, descricao, usuario, status, dateFormat.parse(dataInclusao),
                dateFormat.parse(dataAlteracao));
    }

    protected Tarefa criarTarefa(String resumo, String descricao, Usuario usuario, StatusTarefa status,
            Date dataInclusao, Date dataAlteracao) {
        Tarefa tarefa = new Tarefa();

        tarefa.setResumo(resumo);
        tarefa.setDescricao(descricao);
        tarefa.setUsuario(usuario);
        tarefa.setCodigoStatus(status.getCodigo());
        tarefa.setDataInclusao(dataInclusao);
        tarefa.setDataAlteracao(dataAlteracao);

        return this.tarefaRepository.save(tarefa);
    }

    protected Usuario criarUsuario(String login, String password) {
        Usuario usuario = new Usuario(login, password, false);
        this.usuarioService.salvar(usuario);
        return usuario;
    }

    protected Usuario criarSuperUsuario(String login, String password) {
        Usuario usuario = new Usuario(login, password, true);
        this.usuarioService.salvar(usuario);
        return usuario;
    }

    @SneakyThrows
    protected String getToken(Usuario usuario) {
        User user = new User(usuario.getLogin(), usuario.getPassword());

        MvcResult mvcResult = this.mvc.perform(put("/auth")     //
                .contentType(MediaType.APPLICATION_JSON)        //
                .content(user.toJson()))                        //
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    @SneakyThrows
    protected <T> List<T> getResponseAsObject(MvcResult result, Class<T> klass) {
        JsonMapper mapper = new JsonMapper();
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, klass);
        return mapper.readValue(result.getResponse().getContentAsString(), collectionType);
    }

}

package br.com.carlos.todolist.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.todolist.model.StatusTarefa;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.service.TarefaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;


/**
 * Controller para as operações relacionadas as Tarefas
 *
 * @author carlos.oliveira
 */
@Api(tags = {"Tarefas"})
@SwaggerDefinition(tags = {
        @Tag(name = "Tarefas", description = "Endpoints para manipulação de Tarefas")
})
@RestController
@RequestMapping("/todo")
public class TarefaController {

    Logger LOGGER = LoggerFactory.getLogger(TarefaController.class);

    @Autowired
    private TarefaService tarefaService;

    /**
     * Endpoint para listagem de tarefas por Usuário e por Status. O Usuário Super pode listar tarefas de qualquer usuário
     *
     * @param statusTarefas lista de {@link StatusTarefa} a ser considerada na busca, ou vazia para anular o filtro
     * @return lista de {@link Tarefa} pertencentes ao {@link br.com.carlos.todolist.model.Usuario} logado
     */
    @ApiOperation(value = "Listagem de tarefas por Usuário")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Tarefa> listarTarefas(@RequestParam(value = "status", required = false) List<StatusTarefa> statusTarefas) {
        LOGGER.info("LIST-TAREFA-status - " + statusTarefas);
        return this.tarefaService.buscarTarefas(statusTarefas);
    }

    /**
     * Endpoint para criação de novas tarefas para o usuário logado (token)
     * @param tarefa {@link Tarefa} recebida como json
     * @return {@link Tarefa} salva com id preenchido e data de alteração atual
     */
    @ApiOperation("Criação de Tarefas")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Tarefa salvarTarefa(@RequestBody Tarefa tarefa) {
        LOGGER.info("SAVE-TAREFA - " + tarefa.toJson());
        return this.tarefaService.salvarTarefa(tarefa);
    }

    /**
     * Endpoint para alteração de tarefas existentes
     *
     * @param id Id da tarefa que está sendo salva
     * @param tarefa {@link Tarefa} recebida como json
     * @return {@link Tarefa} salva com id preenchido e data de alteração atual
     */
    @ApiOperation("Alteração de Tarefas")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Tarefa salvarTarefa(@PathVariable("id") long id, @RequestBody Tarefa tarefa) {
        LOGGER.info("SAVE-TAREFA - " + tarefa.toJson());
        return this.tarefaService.salvarTarefa(id, tarefa);
    }

    /**
     * Endpoint para remover tarefas com base em uma {@link Tarefa} recebida via JSON
     *
     * @param tarefa {@link Tarefa} recebida como json
     */
    @ApiOperation("Remoção de Tarefas pelo body")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deletarTarefa(@RequestBody Tarefa tarefa) {
        LOGGER.info("DELETE-TAREFA - " + tarefa.getId());
        this.tarefaService.deletar(tarefa);
    }

    /**
     * Endpoint para remover tarefas com base em um Id recebido path da URL
     *
     * @param id id da {@link Tarefa} alvo da remoção
     */
    @ApiOperation("Remoção de Tarefas pelo ID na URL")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarTarefa(@PathVariable("id") long id) {
        LOGGER.info("DELETE-TAREFA - " + id);
        this.tarefaService.deletarPorId(id);
    }

}

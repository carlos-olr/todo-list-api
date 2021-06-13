package br.com.carlos.todolist.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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


/**
 * Controller para operações relacionadas as Tarefas
 *
 * @author carlos.oliveira
 */
@RestController
@RequestMapping("/todo")
public class TarefaController {

    Logger LOGGER = LoggerFactory.getLogger(TarefaController.class);

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Tarefa> listarTarefas(@RequestParam(value = "status", required = false) List<StatusTarefa> statusTarefas) {
        LOGGER.info("LIST-TAREFA-status - " + statusTarefas);
        return this.tarefaService.buscarTarefas(statusTarefas);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Tarefa salvarTarefa(@RequestBody Tarefa tarefa) {
        LOGGER.info("SAVE-TAREFA - " + tarefa.toJson());
        return this.tarefaService.salvarTarefa(tarefa);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deletarTarefa(@RequestBody Tarefa tarefa) {
        LOGGER.info("DELETE-TAREFA - " + tarefa.getId());
        this.tarefaService.deletar(tarefa);
    }

}

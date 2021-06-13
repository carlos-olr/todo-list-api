package br.com.carlos.todolist.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Tarefa> listarTarefas() {
        return this.tarefaService.buscarTarefas();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Tarefa salvarTarefa(@RequestBody Tarefa tarefa) {
        return this.tarefaService.salvarTarefa(tarefa);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deletarTarefa(@RequestBody Tarefa tarefa) {
        this.tarefaService.deletar(tarefa);
    }


}

package br.com.carlos.todolist.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.todolist.model.Tarefa;


/**
 * Controller para operações relacionadas as Tarefas
 *
 * @author carlos.oliveira
 */
@RestController
@RequestMapping("/todo")
public class TarefaController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Tarefa> listarTarefas() {
        return null;
    }

}

package br.com.carlos.todolist.model;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;


/**
 * Enum com possíveis Status para as tarefas
 *
 * @author carlos.oliveira
 */
public enum StatusTarefa implements Serializable {

    /**
     * Status padrão para tarefas recém criadas, indicando que está pendente
     */
    PENDING(0),
    /**
     * Status utilizado para marcar que uma tarefa foi concluída
     */
    COMPLETED(1);

    private final int codigo;

    StatusTarefa(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public static StatusTarefa valueOf(int codigo) {
        switch (codigo) {
            case 0:
                return PENDING;
            case 1:
                return COMPLETED;
            default:
                return null;
        }
    }

    public static List<Integer> getTodosOsCodigos() {
        return getCodigos(Lists.newArrayList(values()));
    }

    public static List<Integer> getCodigos(List<StatusTarefa> statusTarefas) {
        return statusTarefas.stream().map(StatusTarefa::getCodigo).collect(Collectors.toList());
    }
}

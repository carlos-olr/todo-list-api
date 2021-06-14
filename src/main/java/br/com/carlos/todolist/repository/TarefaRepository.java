package br.com.carlos.todolist.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.carlos.todolist.model.Tarefa;


/**
 * Classe do tipo Repositório para gerenciamento de {@link Tarefa} e definição de comportamentos específicos
 *
 * @author carlos.oliveira
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    /**
     * Busca de {@link Tarefa} considerando a chave para {@link br.com.carlos.todolist.model.Usuario} e uma lista de
     * códigos de {@link br.com.carlos.todolist.model.StatusTarefa}
     *
     * @param usuarioId ID do Usuário dono da Tarefa
     * @param codigosStatus lista de códigos de status de tarefas
     * @return lista de {@link Tarefa} com base no usuário e códigos passados
     */
    @Query("from Tarefa tarefa where tarefa.usuario.id = :usuarioId and tarefa.codigoStatus in (:codigosStatus) "
            + "order by tarefa.codigoStatus, tarefa.dataInclusao asc")
    List<Tarefa> findByUsuarioIdOrderByStatusAndDataInclusao(@Param("usuarioId") Long usuarioId,
            @Param("codigosStatus") List<Integer> codigosStatus);

    /**
     * Busca de {@link Tarefa} uma lista de códigos de {@link br.com.carlos.todolist.model.StatusTarefa}
     *
     * @param codigosStatus lista de códigos de status de tarefas
     * @return lista de {@link Tarefa} numa lista de códigos passados
     */
    @Query("from Tarefa tarefa where tarefa.codigoStatus in (:codigosStatus) "
            + "order by tarefa.codigoStatus, tarefa.dataInclusao asc")
    List<Tarefa> findAllOrderByStatusAndDataInclusao(@Param("codigosStatus") List<Integer> codigosStatus);

}

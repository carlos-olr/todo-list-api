
package br.com.carlos.todolist.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.carlos.todolist.model.Tarefa;


/**
 * @author carlos.oliveira
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("from Tarefa tarefa where tarefa.usuario.id = :usuarioId order by tarefa.codigoStatus, tarefa.dataInclusao asc")
    List<Tarefa> findByUsuarioIdOrderByStatusAndDataInclusao(@Param("usuarioId") Long usuarioId);

    @Query("from Tarefa tarefa order by tarefa.codigoStatus, tarefa.dataInclusao asc")
    List<Tarefa> findAllOrderByStatusAndDataInclusao();

}

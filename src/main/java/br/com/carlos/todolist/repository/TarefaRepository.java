
package br.com.carlos.todolist.repository;



import static br.com.carlos.todolist.model.Tarefa.CACHE_FIND_TAREFAS_BY_ID_USUARIO;

import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.todolist.model.Tarefa;


/**
 * @author carlos.oliveira
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Cacheable(CACHE_FIND_TAREFAS_BY_ID_USUARIO)
    Set<Tarefa> findByIdUsuario(int customerId);

    @Override
    @CacheEvict(value = CACHE_FIND_TAREFAS_BY_ID_USUARIO, key = "tarefa")
    <S extends Tarefa> S save(S tarefa);

}

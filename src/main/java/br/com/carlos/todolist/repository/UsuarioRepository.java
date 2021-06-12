
package br.com.carlos.todolist.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.todolist.model.Usuario;


/**
 * @author carlos.oliveira
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLoginAndPassword(String login, String password);

    Usuario findByLogin(String login);

}

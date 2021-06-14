
package br.com.carlos.todolist.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.todolist.model.Usuario;


/**
 * Classe do tipo Repositório para gerenciamento de {@link Usuario} e definição de comportamentos específicos
 *
 * @author carlos.oliveira
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca de {@link Usuario} com base no login passado
     *
     * @param login login alvo que está sendo buscado
     * @return {@link Usuario} encontrado ou null caso não exista usuário com o login passado
     */
    Usuario findByLogin(String login);

}

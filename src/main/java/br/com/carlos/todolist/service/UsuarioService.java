
package br.com.carlos.todolist.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.carlos.todolist.exception.BadRequestException;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;


/**
 * Serviço para aplicação de regras de negócio relacionadas as {@link Usuario}s
 *
 * @author carlos.oliveira
 */
@Service
public class UsuarioService extends MessagesService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MessageSource messages;

    /**
     * Salva o {@link Usuario} passado aplicando validações
     * 1 - login é um campo obrigatório
     * 2 - password é um campo obrigatório
     * 3 - não é permitido alterar um usuário já existente
     *
     * O password passado é sempre encriptado antes do save
     */
    @Transactional
    public Usuario salvar(Usuario usuarioparam) {
        if (StringUtils.isEmpty(usuarioparam.getLogin())) {
            throw new BadRequestException(this.getMessage("UsuarioService.loginObrigatorio"));
        }

        String originalPassword = usuarioparam.getPassword();
        if (StringUtils.isEmpty(originalPassword)) {
            throw new BadRequestException(this.getMessage("UsuarioService.passwordObrigatorio"));
        }

        Usuario usuarioBD = this.usuarioRepository.findByLogin(usuarioparam.getLogin());
        if (usuarioBD != null) {
            throw new BadRequestException(this.getMessage("UsuarioService.usuarioJaExiste", usuarioparam.getLogin()));
        }

        // O processo de clone serve para que o service na exponha o password encriptado
        Usuario clone = usuarioparam.clone();
        clone.setPassword(this.bCryptPasswordEncoder.encode(originalPassword));

        Usuario usuarioSalvo = this.usuarioRepository.save(clone);

        usuarioparam.setId(usuarioSalvo.getId());

        return usuarioparam;
    }

    /**
     * Implementação da API {@link UserDetailsService} para fornecer método que resolva os {@link UserDetails} com
     * base no username passado, no caso desse projeto, resolverá como o campo login na entidade {@link Usuario}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = this.usuarioRepository.findByLogin(username);
        if (usuarioEncontrado != null) {
            return usuarioEncontrado;
        }
        throw new BadRequestException(this.getMessage("UsuarioService.usuarioNaoEncontrado", username));
    }
}

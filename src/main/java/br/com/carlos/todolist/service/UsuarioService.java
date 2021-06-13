
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
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.UsuarioRepository;


/**
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

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (StringUtils.isEmpty(usuario.getLogin())) {
            throw new BadRequestException(this.getMessage("UsuarioService.loginObrigatorio"));
        }

        if (StringUtils.isEmpty(usuario.getPassword())) {
            throw new BadRequestException(this.getMessage("UsuarioService.passwordObrigatorio"));
        }

        Usuario clone = usuario.clone();

        clone.setPassword(this.bCryptPasswordEncoder.encode(clone.getPassword()));

        clone = this.usuarioRepository.save(clone);

        usuario.setId(clone.getId());

        return usuario;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = this.usuarioRepository.findByLogin(username);
        if (usuarioEncontrado != null) {
            return usuarioEncontrado;
        }
        throw new BadRequestException(this.getMessage("UsuarioService.usuarioNaoEncontrado", username));
    }
}

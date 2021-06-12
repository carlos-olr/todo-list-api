
package br.com.carlos.todolist.service;


import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.carlos.todolist.exception.BadRequestException;
import br.com.carlos.todolist.model.StatusTarefa;
import br.com.carlos.todolist.model.Tarefa;
import br.com.carlos.todolist.model.Usuario;
import br.com.carlos.todolist.repository.TarefaRepository;
import br.com.carlos.todolist.security.ContextoUsuario;


/**
 * @author carlos.oliveira
 */
@Service
public class TarefaService extends MessagesService {

    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private ContextoUsuario contextoUsuario;

    @Transactional
    public Tarefa salvarTarefa(Tarefa tarefa) {
        Usuario usuarioLogado = this.contextoUsuario.getContext();

        Date agora = new Date();
        if (tarefa.getId() == null) {
            tarefa.setStatus(StatusTarefa.PENDING.getCodigo());
            tarefa.setDataInclusao(agora);
            tarefa.setDataAlteracao(agora);
            tarefa.setUsuario(usuarioLogado);
        } else {
            this.validarSeExisteTarefaComIdPassado(tarefa.getId());
            this.validarSeUsuarioLogadoPodeAlterarTarefa(usuarioLogado, tarefa);

            tarefa.setDataAlteracao(agora);
        }

        return this.tarefaRepository.save(tarefa);
    }

    @Transactional(readOnly = true)
    public Set<Tarefa> buscarTarefas() {
        Usuario usuarioLogado = this.contextoUsuario.getContext();

        if (BooleanUtils.isTrue(usuarioLogado.getSuperUser())) {
            return this.tarefaRepository.findAllOrderByStatusAndDataAlteracao();
        } else {
            return this.tarefaRepository.findByUsuarioIdOrderByStatusAndDataAlteracao(usuarioLogado.getId());
        }
    }

    public void deletar(Tarefa tarefa) {
        this.validarSeExisteTarefaComIdPassado(tarefa.getId());

        Usuario usuarioLogado = this.contextoUsuario.getContext();
        this.validarSeUsuarioLogadoPodeAlterarTarefa(usuarioLogado, tarefa);

        this.tarefaRepository.deleteById(tarefa.getId());
    }

    private void validarSeUsuarioLogadoPodeAlterarTarefa(Usuario usuario, Tarefa tarefa) {
        Optional<Tarefa> resultadoBusca = this.tarefaRepository.findById(tarefa.getId());
        if (resultadoBusca.isPresent()) {
            Usuario donoTarefaEncontradaBD = resultadoBusca.get().getUsuario();
            if (!donoTarefaEncontradaBD.getId().equals(usuario.getId())) {
                throw new BadRequestException(getMessage("TarefaService.usuarioNaoPodeAlteraTarefa"));
            }
        }
    }

    private void validarSeExisteTarefaComIdPassado(Long tarefaId) {
        Optional<Tarefa> resultadoBusca = this.tarefaRepository.findById(tarefaId);
        if (resultadoBusca.isEmpty()) {
            throw new BadRequestException(getMessage("TarefaService.tarefaNaoEncontrada", tarefaId));
        }
    }

}

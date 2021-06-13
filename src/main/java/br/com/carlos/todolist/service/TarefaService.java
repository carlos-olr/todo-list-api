
package br.com.carlos.todolist.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public Tarefa salvarTarefa(Long id, Tarefa tarefa) {
        this.validarSeIdsEstaoCorretos(id, tarefa);
        return this.salvarTarefa(tarefa);
    }

    @Transactional
    public Tarefa salvarTarefa(Tarefa tarefa) {
        Date agora = new Date();
        Usuario usuarioLogado = this.contextoUsuario.getContext();

        if (tarefa.getId() == null) {
            tarefa.setDataInclusao(agora);
        } else {
            this.validarSeExisteTarefaComIdPassado(tarefa.getId());
            this.validarSeUsuarioLogadoPodeAlterarTarefa(usuarioLogado, tarefa.getId());

            tarefa.setCodigoStatus(tarefa.getStatus().getCodigo());
        }

        tarefa.setDataAlteracao(agora);
        tarefa.setUsuario(usuarioLogado);

        if (tarefa.getStatus() == null) {
            tarefa.setCodigoStatus(StatusTarefa.PENDING.getCodigo());
        }

        tarefa.setCodigoStatus(tarefa.getStatus().getCodigo());

        return this.tarefaRepository.save(tarefa);
    }

    @Transactional(readOnly = true)
    public List<Tarefa> buscarTarefas() {
        return this.buscarTarefas(null);
    }

    @Transactional(readOnly = true)
    public List<Tarefa> buscarTarefas(List<StatusTarefa> statusConsiderados) {
        Usuario usuario = this.contextoUsuario.getContext();

        List<Integer> codigoStatus;
        if (statusConsiderados == null || statusConsiderados.isEmpty()) {
            codigoStatus = StatusTarefa.getTodosOsCodigos();
        } else {
            codigoStatus = StatusTarefa.getCodigos(statusConsiderados);
        }

        if (BooleanUtils.isTrue(usuario.getSuperUser())) {
            return this.tarefaRepository.findAllOrderByStatusAndDataInclusao(codigoStatus);
        } else {
            return this.tarefaRepository.findByUsuarioIdOrderByStatusAndDataInclusao(usuario.getId(), codigoStatus);
        }
    }

    public void deletar(Tarefa tarefa) {
        this.deletarPorId(tarefa.getId());
    }

    public void deletarPorId(Long id) {
        this.validarSeFoiPassadoUmId(id);
        this.validarSeExisteTarefaComIdPassado(id);

        Usuario usuarioLogado = this.contextoUsuario.getContext();
        this.validarSeUsuarioLogadoPodeAlterarTarefa(usuarioLogado, id);

        this.tarefaRepository.deleteById(id);
    }

    private void validarSeUsuarioLogadoPodeAlterarTarefa(Usuario usuario, Long tarefaId) {
        Optional<Tarefa> resultadoBusca = this.tarefaRepository.findById(tarefaId);
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

    private void validarSeFoiPassadoUmId(Long tarefaId) {
        if (tarefaId == null) {
            throw new BadRequestException(getMessage("TarefaService.idDaTarefaObrigatorio", tarefaId));
        }
    }

    private void validarSeIdsEstaoCorretos(Long tarefaId, Tarefa tarefaRecebida) {
        if (tarefaId == null) {
            throw new BadRequestException(getMessage("TarefaService.idDaTarefaObrigatorio", tarefaId));
        }
        if (tarefaRecebida.getId() == null || !tarefaId.equals(tarefaRecebida.getId()) ) {
            throw new BadRequestException(getMessage("TarefaService.idDoPathDiferenteDoIdDoBody", tarefaId));
        }
    }

}

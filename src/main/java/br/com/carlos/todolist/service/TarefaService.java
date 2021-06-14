
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
 * Serviço para aplicação de regras de negócio relacionadas as {@link Tarefa}s
 *
 * @author carlos.oliveira
 */
@Service
public class TarefaService extends MessagesService {

    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private ContextoUsuario contextoUsuario;

    /**
     * Serviço para salvar uma tarefa fazendo validação por ID
     */
    @Transactional
    public Tarefa salvarTarefa(Long id, Tarefa tarefa) {
        this.validarSeIdsEstaoCorretos(id, tarefa);
        return this.salvarTarefa(tarefa);
    }

    /**
     * Serviço para salvar uma {@link Tarefa} considerando
     * 1 - Usuário Logado
     * 2 - Se é uma edição ou não
     * 3 - Preenchendo o código status com base no status já preenchido
     * 4 - Na ausência de status, e utilizado o {@link StatusTarefa}.PENDING como padrão
     * 5 - Para criações é sempre utilizado o agora (new Date) para data de inclusão
     * 6 - A data de alteração é sempre salva
     * 7 - valida se o usuário logado é dono na tarefa, somente nesse cenário é possível alterar
     */
    @Transactional
    public Tarefa salvarTarefa(Tarefa tarefa) {
        Date agora = new Date();
        Usuario usuarioLogado = this.contextoUsuario.getUsuarioLogado();

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

    /**
     * Retorna todas as Tarefas do Usuário logado ou todas de todos os usuário se o usuário logado for Super
     *
     * @return lista de {@link Tarefa} encontradas
     */
    @Transactional(readOnly = true)
    public List<Tarefa> buscarTarefas() {
        return this.buscarTarefas(null);
    }

    /**
     * Retorna todas as Tarefas do Usuário logado ou todas de todos os usuário se o usuário logado for Super,
     * podendo ainda filtrar por {@link StatusTarefa}
     *
     * Caso não seja passado nenhum filtro de status, serão considerados todos os status
     *
     * @return lista de {@link Tarefa} encontradas
     */
    @Transactional(readOnly = true)
    public List<Tarefa> buscarTarefas(List<StatusTarefa> statusConsiderados) {
        Usuario usuario = this.contextoUsuario.getUsuarioLogado();

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

    /**
     * Deletar tarefa pelo ID existente na {@link Tarefa} passada aplicando validações:
     * 1 - se foi passado um ID
     * 2 - se existe tarefa com o ID passado
     * 2 - se usuário é dono da tarefa é o que está logado
     */
    public void deletar(Tarefa tarefa) {
        this.deletarPorId(tarefa.getId());
    }

    /**
     * Deletar tarefa pelo ID existente na {@link Tarefa} passada aplicando validações:
     * 1 - se foi passado um ID
     * 2 - se existe tarefa com o ID passado
     * 2 - se usuário é dono da tarefa é o que está logado
     */
    public void deletarPorId(Long id) {
        this.validarSeFoiPassadoUmId(id);
        this.validarSeExisteTarefaComIdPassado(id);

        Usuario usuarioLogado = this.contextoUsuario.getUsuarioLogado();
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

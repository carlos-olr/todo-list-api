/*
 * Copyright (c) 1999-2020 Touch Tecnologia e Informatica Ltda.
 * Gomes de Carvalho, 1666, 3o. Andar, Vila Olimpia, Sao Paulo, SP, Brasil.
 * Todos os direitos reservados.
 *
 * Este software e confidencial e de propriedade da Touch Tecnologia e
 * Informatica Ltda. (Informacao Confidencial). As informacoes contidas neste
 * arquivo nao podem ser publicadas, e seu uso esta limitado de acordo com os
 * termos do contrato de licenca.
 */

package br.com.carlos.todolist.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;


/**
 * Entidade para representar o conceito de tarefa que será manipulado pela APIs
 *
 * @author carlos.oliveira
 */
@Getter
@Setter
@Entity
@Table(name = "TDL_TAREFAS")
public class Tarefa {

    public static final String CACHE_FIND_TAREFAS_BY_ID_USUARIO = "cache_find_tarefas_by_id_usuario";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TAREFAS")
    @SequenceGenerator(name = "SEQ_TAREFAS", sequenceName = "SEQ_TDL_TAREFAS")
    private Long id;

    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "DATA_INCLUSAO")
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date dataInclusao;

    @Column(name = "STATUS")
    private String reusmo;

    @Column(name = "STATUS")
    private String descricao;

    @Enumerated
    @Column(name = "STATUS")
    private StatusTarefa status;

    @Column(name = "DATA_ATERACAO")
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date dataAlteracao;

    public Tarefa() {
        this.dataInclusao = new Date();
        this.status = StatusTarefa.PENDING;
    }

}

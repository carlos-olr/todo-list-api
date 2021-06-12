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


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.carlos.todolist.model.comum.Json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;


/**
 * Entidade para representar o conceito de tarefa que será manipulado pela APIs
 *
 * @author carlos.oliveira
 */
@Getter
@Setter
@Entity
@Table(name = "TDL_TAREFA")
public class Tarefa extends Json implements Serializable {

    public static final String CACHE_FIND_TAREFAS_BY_ID_USUARIO = "cache_find_tarefas_by_id_usuario";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TAREFAS")
    @SequenceGenerator(name = "SEQ_TAREFAS", sequenceName = "SEQ_TDL_TAREFA", allocationSize = 10, initialValue = 10)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @Column(name = "DATA_INCLUSAO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dataInclusao;

    @Column(name = "RESUMO")
    private String reusmo;

    @Column(name = "DESCRICAO")
    private String descricao;

    @JsonIgnore
    @Column(name = "STATUS")
    private int status;

    @Column(name = "DATA_ALTERACAO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dataAlteracao;

    @Transient
    @JsonIgnore
    public StatusTarefa getStatusTarefa() {
        return StatusTarefa.valueOf(this.status);
    }

    @SneakyThrows
    public static Tarefa fromJson(String tarefaStr) {
        return new ObjectMapper().readValue(tarefaStr, Tarefa.class);
    }

}

CREATE TABLE tdl_tarefas (
    id bigint NOT NULL,
    id_usuario bigint NOT NULL,
    resumo varchar(255) NULL,
    descricao varchar(2000) NOT NULL,
    status varchar(100) NOT NULL,
    data_inclusao timestamp NOT NULL,
    data_alteracao timestamp NOT NULL,

    CONSTRAINT tdl_tarefas_pk PRIMARY KEY (ID)
);

CREATE INDEX idx_tdl_tarefas_id_usuario ON tdl_tarefas (id_usuario);

CREATE SEQUENCE SEQ_TDL_TAREFAS
    INCREMENT 10
    MINVALUE 10
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1;
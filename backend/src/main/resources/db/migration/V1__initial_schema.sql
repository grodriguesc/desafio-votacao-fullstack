-- Migration V1: Create initial schema
-- This is an example of a Flyway migration file

CREATE TABLE IF NOT EXISTS pautas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS associados (
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS sessoes (
    id BIGSERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP NOT NULL,
    aberta BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (pauta_id) REFERENCES pautas(id)
);

CREATE INDEX idx_sessoes_pauta ON sessoes(pauta_id);
CREATE INDEX idx_sessoes_aberta ON sessoes(aberta);

CREATE TABLE IF NOT EXISTS votos (
    id BIGSERIAL PRIMARY KEY,
    sessao_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    opcao VARCHAR(3) NOT NULL,
    data_voto TIMESTAMP NOT NULL,
    FOREIGN KEY (sessao_id) REFERENCES sessoes(id),
    FOREIGN KEY (associado_id) REFERENCES associados(id),
    UNIQUE (sessao_id, associado_id)
);

CREATE INDEX idx_votos_sessao ON votos(sessao_id);
CREATE INDEX idx_votos_opcao ON votos(opcao);

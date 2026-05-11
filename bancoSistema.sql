CREATE SCHEMA sistema;

CREATE TABLE sistema.usuario (
	id SERIAL,
	nome varchar NOT NULL,
	email varchar NOT NULL,
	senha_hash varchar NOT NULL,
	criado_em timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_usuario PRIMARY KEY (id),
	CONSTRAINT unq_email_usuario UNIQUE (email)
);

CREATE TABLE sistema.dataset (
    id SERIAL,
    nome VARCHAR(200) NOT NULL,
    descricao VARCHAR(400),
    fontes VARCHAR(200),
    criador_id INT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_dataset PRIMARY KEY (id),
    CONSTRAINT fk_dataset FOREIGN KEY (criador_id) REFERENCES sistema.usuario(id)
);

CREATE TABLE sistema.versao_dataset (
    id SERIAL,
	dataset_id INT NOT NULL,
	versao_base_id INT, 
	criador_id INT NOT NULL,
	numero_versao INT NOT NULL,
	desc_modificacoes VARCHAR (500),
	caminho_arquivo VARCHAR(100),
	criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
	CONSTRAINT pk_versao PRIMARY KEY (id),
	CONSTRAINT fk_versao_dataset FOREIGN KEY (dataset_id) REFERENCES sistema.dataset(id),
	CONSTRAINT fk_versao_criador FOREIGN KEY (criador_id) REFERENCES sistema.usuario(id)
);

ALTER TABLE sistema.versao_dataset 
	ADD CONSTRAINT fk_versao_base FOREIGN KEY (versao_base_id) REFERENCES sistema.versao_dataset(id);


CREATE TABLE sistema.acesso_versao (
	id SERIAL,
	versao_id int NOT NULL,
	usuario_id int NOT NULL,
	tipo_acesso varchar NOT NULL,
	acessado_em timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_acesso_versao PRIMARY KEY (id),
	CONSTRAINT fk_acesso_versao_versao FOREIGN KEY (versao_id) REFERENCES sistema.versao_dataset(id),
	CONSTRAINT fk_acesso_versao_usuario FOREIGN KEY (usuario_id) REFERENCES sistema.usuario(id),
	CONSTRAINT ck_tipo_acesso CHECK (tipo_acesso = 'visualizacao' OR tipo_acesso = 'download')
);

CREATE TABLE sistema.feature (
	id SERIAL,
	nome varchar NOT NULL,
	tipo varchar NOT NULL,
	descricao varchar NOT NULL,
	versao_id int NOT NULL,
	CONSTRAINT pk_feature PRIMARY KEY (id),
    CONSTRAINT fk_feature_versao FOREIGN KEY (versao_id) REFERENCES sistema.versao_dataset(id)
);
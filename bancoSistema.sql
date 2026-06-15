CREATE SCHEMA sistema;
CREATE TABLE sistema.usuario (
	cpf varchar(11),
	nome varchar NOT NULL,
	email varchar NOT NULL,
	senha_hash varchar NOT NULL,
	criado_em timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_usuario PRIMARY KEY (cpf),
	CONSTRAINT unq_email_usuario UNIQUE (email),
	CONSTRAINT ck_cpf_numeros CHECK (cpf ~ '^[0-9]{11}$')
);
CREATE TABLE sistema.dataset (
	id SERIAL,
	nome VARCHAR NOT NULL,
	descricao VARCHAR,
	criador_cpf varchar(11) NOT NULL,
	criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
	CONSTRAINT pk_dataset PRIMARY KEY (id),
	CONSTRAINT fk_dataset FOREIGN KEY (criador_cpf) REFERENCES sistema.usuario(cpf)
);

CREATE TABLE sistema.fonte_dataset (
    dataset_id INT NOT NULL,
    fonte VARCHAR NOT NULL,
    CONSTRAINT pk_fonte_dataset PRIMARY KEY (dataset_id, fonte),
    CONSTRAINT fk_fonte_dataset FOREIGN KEY (dataset_id) REFERENCES sistema.dataset(id)
);

CREATE TABLE sistema.versao_dataset (
	dataset_id INT NOT NULL,
	versao_base_numero INT,
	numero_versao INT NOT NULL,
	criador_cpf varchar(11) NOT NULL,
	desc_modificacoes VARCHAR (500),
	arquivo BYTEA,
	criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
	CONSTRAINT pk_versao PRIMARY KEY (dataset_id, numero_versao),
	CONSTRAINT fk_versao_dataset FOREIGN KEY (dataset_id) REFERENCES sistema.dataset(id),
	CONSTRAINT fk_versao_criador FOREIGN KEY (criador_cpf) REFERENCES sistema.usuario(cpf)
);

ALTER TABLE sistema.versao_dataset
	ADD CONSTRAINT fk_versao_base FOREIGN KEY (dataset_id, versao_base_numero) REFERENCES sistema.versao_dataset(dataset_id, numero_versao);


CREATE TABLE sistema.acesso_versao (
	dataset_id INT NOT NULL,
	numero_versao INT NOT NULL,
	usuario_cpf varchar(11) NOT NULL,
	
	tipo_acesso varchar NOT NULL,
	acessado_em timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_acesso_versao PRIMARY KEY (acessado_em, dataset_id, numero_versao, usuario_cpf),
	CONSTRAINT fk_acesso_versao_versao FOREIGN KEY (dataset_id, numero_versao) REFERENCES sistema.versao_dataset(dataset_id, numero_versao) ON DELETE CASCADE,
	CONSTRAINT fk_acesso_versao_usuario FOREIGN KEY (usuario_cpf) REFERENCES sistema.usuario(cpf) ON DELETE CASCADE,
	CONSTRAINT ck_tipo_acesso CHECK (
		tipo_acesso = 'visualizacao' OR tipo_acesso = 'download'
	)
);
CREATE TABLE sistema.feature (
	id SERIAL,
	nome varchar NOT NULL,
	tipo varchar NOT NULL,
	descricao varchar NOT NULL,
	dataset_id INT NOT NULL,
	numero_versao INT NOT NULL,		
	CONSTRAINT pk_feature PRIMARY KEY (id),
	CONSTRAINT fk_feature_versao FOREIGN KEY (dataset_id, numero_versao) REFERENCES sistema.versao_dataset(dataset_id, numero_versao)
);
-------------------- INSERT USUARIO --------------------
INSERT INTO sistema.usuario (cpf, nome, email, senha_hash)
VALUES ('11111111111', 'usuario 1', 'email1@email.com', '123'),
	('22222222222', 'usuario 2', 'email2@email.com', '123'),
	('33333333333','usuario 3', 'email3@email.com', '123');
SELECT *
FROM sistema.usuario;
--------------------------------------------------------
-------------------- INSERT DATASET --------------------
INSERT INTO sistema.dataset (nome, descricao, criador_cpf)
VALUES ('dataset 1', 'dataset 1', '11111111111'),
	('dataset 2', 'dataset 2', '11111111111'),
	('dataset 3', 'dataset 3', '33333333333');
SELECT *
FROM sistema.dataset;
--------------------------------------------------------
-------------------- INSERT FONTE ----------------------
INSERT INTO sistema.fonte_dataset (dataset_id, fonte)
VALUES (1, 'Kaggle'),
       (1, 'UCI Repository'),
       (3, 'IBGE');
--------------------------------------------------------
-------------------- INSERT VERSAO DATASET --------------
-- CRIANDO VERSAO A PARTIR DO DATASET ORIGINAL
INSERT INTO sistema.versao_dataset (
		dataset_id,
		numero_versao,
		versao_base_numero,
		criador_cpf,
		desc_modificacoes,
		arquivo
	)
VALUES(1, 1, null, '11111111111', 'adicionando fontes', NULL);
-- CRIANDO VERSAO A PARTIR DE OUTRA VERSAO
INSERT INTO sistema.versao_dataset (
		dataset_id,
		numero_versao,
		versao_base_numero,
		criador_cpf,
		desc_modificacoes,
		arquivo
	)
VALUES(1, 2, 1, '22222222222', 'formatando dados', NULL);
SELECT *
FROM sistema.versao_dataset;
--------------------------------------------------------
-------------------- INSERT ACESSSO VERSAO ---------
INSERT INTO sistema.acesso_versao (dataset_id, numero_versao, usuario_cpf, tipo_acesso)
VALUES (1, 1, '22222222222', 'visualizacao'),
	   (1, 1, '11111111111', 'download'),
	   (1, 2, '33333333333', 'download');
SELECT *
FROM sistema.acesso_versao;
-----------------------------------------------------
-------------------- INSERT FEATURE --------------------
INSERT INTO sistema.feature (nome, tipo, descricao, dataset_id, numero_versao)
VALUES ('feature 1', 'novos dados', 'novos dados', 1, 2),
	('feature 2', 'normalização', 'normalização', 1, 2),
	('feature 3', 'novos dados', 'novos dados', 1, 1);
SELECT *
FROM sistema.feature;
-----------------------------------------------------
-------------------- TESTE DELETE --------------------
DELETE from sistema.dataset
WHERE id = 2;
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
	nome VARCHAR NOT NULL,
	descricao VARCHAR,
	fontes VARCHAR,
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
	versao_id int NOT NULL,
	usuario_id int NOT NULL,
	tipo_acesso varchar NOT NULL,
	acessado_em timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_acesso_versao PRIMARY KEY (tipo_acesso, versao_id, usuario_id),
	CONSTRAINT fk_acesso_versao_versao FOREIGN KEY (versao_id) REFERENCES sistema.versao_dataset(id) ON DELETE CASCADE;
CONSTRAINT fk_acesso_versao_usuario FOREIGN KEY (usuario_id) REFERENCES sistema.usuario(id) ON DELETE CASCADE;
CONSTRAINT ck_tipo_acesso CHECK (
	tipo_acesso = 'visualizacao'
	OR tipo_acesso = 'download'
)
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
-------------------- INSERT USUARIO --------------------
INSERT INTO sistema.usuario (nome, email, senha_hash)
VALUES ('usuario 1', 'email1@email.com', '123'),
	('usuario 2', 'email2@email.com', '123'),
	('usuario 3', 'email3@email.com', '123');
SELECT *
FROM sistema.usuario;
--------------------------------------------------------
-------------------- INSERT DATASET --------------------
INSERT INTO sistema.dataset (nome, descricao, fontes, criador_id)
VALUES ('dataset 1', 'dataset 1', 'fontes', 1),
	('dataset 2', 'dataset 2', 'fontes', 1),
	('dataset 3', 'dataset 3', 'fontes', 3);
SELECT *
FROM sistema.dataset;
--------------------------------------------------------
-------------------- INSERT VERSAO DATASET --------------
-- CRIANDO VERSAO A PARTIR DO DATASET ORIGINAL
INSERT INTO sistema.versao_dataset (
		dataset_id,
		criador_id,
		numero_versao,
		desc_modificacoes,
		caminho_arquivo
	)
VALUES(1, 1, 1, 'adicionando fontes', 'C:/dataset');
-- CRIANDO VERSAO A PARTIR DE OUTRA VERSAO
INSERT INTO sistema.versao_dataset (
		dataset_id,
		versao_base_id,
		criador_id,
		numero_versao,
		desc_modificacoes,
		caminho_arquivo
	)
VALUES(1, 1, 2, 2, 'formatando dados', 'C:/dataset');
SELECT *
FROM sistema.versao_dataset;
--------------------------------------------------------
-------------------- INSERT ACESSSO VERSAO ---------
INSERT INTO sistema.acesso_versao (versao_id, usuario_id, tipo_acesso)
VALUES (1, 2, 'visualizacao'),
	(1, 1, 'download'),
	(2, 3, 'download');
SELECT *
FROM sistema.acesso_versao;
-----------------------------------------------------
-------------------- INSERT FEATURE --------------------
INSERT INTO sistema.feature (nome, tipo, descricao, versao_id)
VALUES ('feature 1', 'novos dados', 'novos dados', 3),
	('feature 2', 'normalização', 'normalização', 3),
	('feature 3', 'novos dados', 'novos dados', 1);
SELECT *
FROM sistema.feature;
-----------------------------------------------------
-------------------- TESTE DELETE --------------------
DELETE from sistema.dataset
WHERE id = 2;
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
CREATE TABLE sistema.dataset ();
CREATE TABLE sistema.versao_dataset ();
CREATE TABLE sistema.acesso_versao (
	id SERIAL,
	versao_id int NOT NULL,
	usuario_id int NOT NULL,
	tipo_acesso varchar NOT NULL,
	acessado_em timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_acesso_versao PRIMARY KEY (id),
	CONSTRAINT fk_acesso_versao_versao FOREIGN KEY (versao_id) REFERENCES sistema.versao_dataset(id),
	CONSTRAINT fk_acesso_versao_usuario FOREIGN KEY (usuario_id) REFERENCES sistema.usuario(id)
);
CREATE TABLE sistema.feature (
	id SERIAL,
	nome varchar NOT NULL,
	tipo varchar NOT NULL,
	descricao varchar NOT NULL,
	versao_id int NOT NULL,
	CONSTRAINT pk_feature PRIMARY KEY (id) CONSTRAINT fk_feature_versao FOREIGN KEY (versao_id) REFERENCES sistema.versao_dataset(id)
);
CREATE SCHEMA my_portfolio;

CREATE TABLE my_portfolio.empresa (
	id_empresa bigserial,
	cnpj varchar(255) NOT NULL,
	data_atualizacao timestamp NOT NULL,
	data_criacao timestamp NULL,
	razao_social varchar(255) NOT NULL,
	CONSTRAINT empresa_pk PRIMARY KEY (id_empresa)
);

CREATE TABLE my_portfolio.funcionario (
	id_funcionario bigserial,
	cpf varchar(255) NOT NULL,
	data_atualizacao timestamp NOT NULL,
	data_criacao timestamp NULL,
	email varchar(255) NOT NULL,
	nome varchar(255) NOT NULL,
	perfil varchar(255) not null,
	qtd_horas_almoco real default null,
	qtd_horas_trabalho_dia real default null,
	senha varchar(255) not null,
	valor_hora decimal(19, 2) default null,
	id_empresa int8 default null,
	CONSTRAINT fk_funcionario_empresa FOREIGN KEY(id_empresa) 
	REFERENCES my_portfolio.empresa (id_empresa),	
	CONSTRAINT funcionario_pk PRIMARY KEY (id_funcionario)
);

CREATE TABLE my_portfolio.lancamento (
	id_lancamento bigserial,
	data timestamp NOT NULL,
	data_atualizacao timestamp NOT NULL,
	data_criacao timestamp NULL,
	descricao varchar(255) default NULL,
	localizacao varchar(255) default NULL,
	tipo varchar(255) NOT NULL,
	id_funcionario int8 default null,
	CONSTRAINT fk_lancamento_funcionario FOREIGN KEY(id_funcionario) 
	REFERENCES my_portfolio.funcionario (id_funcionario),	
	CONSTRAINT lancamento_pk PRIMARY KEY (id_lancamento)
);

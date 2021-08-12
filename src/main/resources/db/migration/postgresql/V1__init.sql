CREATE SCHEMA my_portfolio;

CREATE TABLE my_portfolio.usuario (
	id_usuario bigserial NOT NULL,
	nome varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	senha varchar(255) NULL,
	cpf varchar(255) NULL,
	perfil varchar(255) NOT NULL,
	data_atualizacao timestamp NOT NULL,
	data_criacao timestamp NOT NULL,
	data_desativacao timestamp NULL,
	CONSTRAINT usuario_pk PRIMARY KEY (id_usuario)
);

CREATE TABLE my_portfolio.portfolio (
	id_portfolio bigserial NOT NULL,
	id_usuario int8 NOT NULL,
	nome varchar(100) NOT NULL,
	descricao varchar(255) NULL,
	data_atualizacao timestamp NOT NULL,
	data_criacao timestamp NOT NULL,
	data_desativacao timestamp NULL,
	CONSTRAINT fk_portfolio_usuario FOREIGN KEY(id_usuario) 
	REFERENCES my_portfolio.usuario (id_usuario),		
	CONSTRAINT portfolio_pk PRIMARY KEY (id_portfolio)
);


CREATE TABLE my_portfolio.carteira (
	id_carteira bigserial NOT NULL,
	id_portfolio int8 NOT null,
	nome varchar(100) NOT NULL,
	descricao varchar(255) NULL,
	data_atualizacao timestamp NOT NULL,
	percentual double precision NULL,
	data_criacao timestamp NOT NULL,
	data_desativacao timestamp NULL,
	CONSTRAINT fk_carteira_portfolio FOREIGN KEY(id_portfolio) 
	REFERENCES my_portfolio.portfolio (id_portfolio),		
	CONSTRAINT carteira_pk PRIMARY KEY (id_carteira)
);



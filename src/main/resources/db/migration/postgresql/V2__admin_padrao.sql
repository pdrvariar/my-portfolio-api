INSERT INTO empresa (cnpj, data_atualizacao, data_criacao, razao_social) VALUES('82198127000121', now(), now(), 'KAZALE IT');

INSERT INTO funcionario (cpf, data_atualizacao, data_criacao, email, nome, perfil, qtd_horas_almoco, qtd_horas_trabalho_dia, senha, valor_hora, id_empresa)
VALUES('16248890935', now(), now(), 'admin@kazale.com', 'ADMIN', 'ROLE_ADMIN', null, null, '$2a$06$xIvBeNRfS65L1N17I7JzgefzxEuLAL0Xk0wFAgIkoNqu9WD6rmp4m', null, (select id_empresa from empresa where cnpj = '82198127000121'));


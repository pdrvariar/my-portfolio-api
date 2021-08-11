package com.pdrvariar.myportfolio.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.repositories.UsuarioRepository;
import com.pdrvariar.myportfolio.api.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario persistir(Usuario usuario) {
		log.info("Persistindo usuario: {}", usuario);
		return this.usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> buscarPorCpf(String cpf) {
		log.info("Buscando usuário pelo CPF {}", cpf);
		return Optional.ofNullable(this.usuarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Usuario> buscarPorEmail(String email) {
		log.info("Buscando usuário pelo email {}", email);
		return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
	}

	@Override
	public Optional<Usuario> buscarPorId(Long id) {
		log.info("Buscando usuário pelo ID {}", id);
		return this.usuarioRepository.findById(id);
	}

}

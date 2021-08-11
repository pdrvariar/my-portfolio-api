package com.pdrvariar.myportfolio.api.services;

import java.util.Optional;

import com.pdrvariar.myportfolio.api.entities.Usuario;

public interface UsuarioService {

	/**
	 * @param usuario
	 * @return Usuario
	 */
	Usuario persistir(Usuario usuario);

	/**
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Usuario> buscarPorCpf(String cpf);

	/**
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Usuario> buscarPorEmail(String email);

	/**
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Usuario> buscarPorId(Long id);
}

package com.pdrvariar.myportfolio.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdrvariar.myportfolio.api.dtos.UsuarioDto;
import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.response.Response;
import com.pdrvariar.myportfolio.api.services.UsuarioService;
import com.pdrvariar.myportfolio.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;

	public UsuarioController() {
	}

	/**
	 * Atualiza os dados de um usuário.
	 * 
	 * @param id
	 * @param usuarioDto
	 * @param result
	 * @return ResponseEntity<Response<UsuarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<UsuarioDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody UsuarioDto usuarioDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando usuário: {}", usuarioDto.toString());
		Response<UsuarioDto> response = new Response<UsuarioDto>();

		Optional<Usuario> usuario = this.usuarioService.buscarPorId(id);
		if (!usuario.isPresent()) {
			result.addError(new ObjectError("usuario", "Usuário não encontrado."));
		}

		this.atualizarDadosUsuario(usuario.get(), usuarioDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando usuário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.usuarioService.persistir(usuario.get());
		response.setData(this.converterUsuarioDto(usuario.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados do usuário com base nos dados encontrados no DTO.
	 * 
	 * @param usuario
	 * @param usuarioDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosUsuario(Usuario usuario, UsuarioDto usuarioDto, BindingResult result)
			throws NoSuchAlgorithmException {
		usuario.setNome(usuarioDto.getNome());

		if (!usuario.getEmail().equals(usuarioDto.getEmail())) {
			this.usuarioService.buscarPorEmail(usuarioDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			usuario.setEmail(usuarioDto.getEmail());
		}

		if (usuarioDto.getSenha().isPresent()) {
			usuario.setSenha(PasswordUtils.gerarBCrypt(usuarioDto.getSenha().get()));
		}
	}

	/**
	 * Retorna um DTO com os dados de um usuário.
	 * 
	 * @param usuario
	 * @return UsuarioDto
	 */
	private UsuarioDto converterUsuarioDto(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setId(usuario.getId());
		usuarioDto.setEmail(usuario.getEmail());
		usuarioDto.setNome(usuario.getNome());

		return usuarioDto;
	}

}

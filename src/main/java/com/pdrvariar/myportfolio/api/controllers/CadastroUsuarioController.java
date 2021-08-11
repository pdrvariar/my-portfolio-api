package com.pdrvariar.myportfolio.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdrvariar.myportfolio.api.dtos.CadastroPFDto;
import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.enums.PerfilEnum;
import com.pdrvariar.myportfolio.api.response.Response;
import com.pdrvariar.myportfolio.api.services.UsuarioService;
import com.pdrvariar.myportfolio.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-usuario")
@CrossOrigin(origins = "*")
public class CadastroUsuarioController {

	private static final Logger log = LoggerFactory.getLogger(CadastroUsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;

	public CadastroUsuarioController() {
	}

	/**
	 * Cadastra um usuário pessoa física no sistema.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPFDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando usuário: {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();

		validarDadosExistentes(cadastroPFDto, result);
		Usuario usuario = this.converterDtoParaFuncionario(cadastroPFDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro do usuário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.usuarioService.persistir(usuario);

		response.setData(this.converterCadastroPFDto(usuario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o usuário não existe na base
	 * de dados.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {

		this.usuarioService.buscarPorCpf(cadastroPFDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("usuario", "CPF já existente.")));

		this.usuarioService.buscarPorEmail(cadastroPFDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("usuario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para usuário.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Usuario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome(cadastroPFDto.getNome());
		usuario.setEmail(cadastroPFDto.getEmail());
		usuario.setCpf(cadastroPFDto.getCpf());
		usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
		usuario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));

		return usuario;
	}

	/**
	 * Popula o DTO de cadastro com os dados do usuário e empresa.
	 * 
	 * @param usuario
	 * @return CadastroPFDto
	 */
	private CadastroPFDto converterCadastroPFDto(Usuario usuario) {
		CadastroPFDto cadastroPFDto = new CadastroPFDto();
		cadastroPFDto.setId(usuario.getId());
		cadastroPFDto.setNome(usuario.getNome());
		cadastroPFDto.setEmail(usuario.getEmail());
		cadastroPFDto.setCpf(usuario.getCpf());

		return cadastroPFDto;
	}

}

package com.pdrvariar.myportfolio.api.controllers;

import java.text.ParseException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdrvariar.myportfolio.api.dtos.PortfolioDto;
import com.pdrvariar.myportfolio.api.entities.Portfolio;
import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.response.Response;
import com.pdrvariar.myportfolio.api.services.PortfolioService;
import com.pdrvariar.myportfolio.api.services.UsuarioService;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "*")
public class PortfolioController {

	private static final Logger log = LoggerFactory.getLogger(PortfolioController.class);

	@Autowired
	private PortfolioService portfolioService;

	@Autowired
	private UsuarioService usuarioService;

	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	public PortfolioController() {
	}

	/**
	 * Retorna a listagem de portfolios de um usuário.
	 * 
	 * @param usuarioId
	 * @return ResponseEntity<Response<PortfolioDto>>
	 */
	@GetMapping(value = "/usuario/{usuarioId}")
	public ResponseEntity<Response<Page<PortfolioDto>>> listarPorUsuarioId(@PathVariable("usuarioId") Long usuarioId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando portfolios por ID do usuário: {}, página: {}", usuarioId, pag);
		Response<Page<PortfolioDto>> response = new Response<Page<PortfolioDto>>();

		PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Portfolio> portfolios = this.portfolioService.buscarPorUsuarioId(usuarioId, pageRequest);
		Page<PortfolioDto> portfoliosDto = portfolios.map(portfolio -> this.converterPortfolioDto(portfolio));

		response.setData(portfoliosDto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna um portfolio por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<PortfolioDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<PortfolioDto>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando portfolio por ID: {}", id);
		Response<PortfolioDto> response = new Response<PortfolioDto>();
		Optional<Portfolio> portfolio = this.portfolioService.buscarPorId(id);

		if (!portfolio.isPresent()) {
			log.info("Portfolio não encontrado para o ID: {}", id);
			response.getErrors().add("Portfolio não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterPortfolioDto(portfolio.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Adiciona um novo portfolio.
	 * 
	 * @param portfolio
	 * @param result
	 * @return ResponseEntity<Response<PortfolioDto>>
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<PortfolioDto>> adicionar(@Valid @RequestBody PortfolioDto portfolioDto,
			BindingResult result) throws ParseException {
		log.info("Adicionando portfolio: {}", portfolioDto.toString());
		Response<PortfolioDto> response = new Response<PortfolioDto>();
		validarUsuario(portfolioDto, result);
		Portfolio portfolio = this.converterDtoParaPortfolio(portfolioDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando portfolio: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		portfolio = this.portfolioService.persistir(portfolio);
		response.setData(this.converterPortfolioDto(portfolio));
		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados de um portfolio.
	 * 
	 * @param id
	 * @param portfolioDto
	 * @return ResponseEntity<Response<Portfolio>>
	 * @throws ParseException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<PortfolioDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody PortfolioDto portfolioDto, BindingResult result) throws ParseException {
		log.info("Atualizando portfolio: {}", portfolioDto.toString());
		Response<PortfolioDto> response = new Response<PortfolioDto>();
		validarUsuario(portfolioDto, result);
		portfolioDto.setId(Optional.of(id));
		Portfolio portfolio = this.converterDtoParaPortfolio(portfolioDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando portfolio: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		portfolio = this.portfolioService.persistir(portfolio);
		response.setData(this.converterPortfolioDto(portfolio));
		return ResponseEntity.ok(response);
	}

	/**
	 * Remove um portfolio por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Portfolio>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo portfolio: {}", id);
		Response<String> response = new Response<String>();
		Optional<Portfolio> portfolio = this.portfolioService.buscarPorId(id);

		if (!portfolio.isPresent()) {
			log.info("Erro ao remover devido ao portfolio ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover portfolio. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.portfolioService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Valida um usuário, verificando se ele é existente e válido no sistema.
	 * 
	 * @param portfolioDto
	 * @param result
	 */
	private void validarUsuario(PortfolioDto portfolioDto, BindingResult result) {
		if (portfolioDto.getUsuarioId() == null) {
			result.addError(new ObjectError("usuario", "Usuário não informado."));
			return;
		}

		log.info("Validando usuário id {}: ", portfolioDto.getUsuarioId());
		Optional<Usuario> usuario = this.usuarioService.buscarPorId(portfolioDto.getUsuarioId());
		if (!usuario.isPresent()) {
			result.addError(new ObjectError("usuario", "Usuário não encontrado. ID inexistente."));
		}
	}

	/**
	 * Converte uma entidade portfolio para seu respectivo DTO.
	 * 
	 * @param portfolio
	 * @return PortfolioDto
	 */
	private PortfolioDto converterPortfolioDto(Portfolio portfolio) {
		PortfolioDto portfolioDto = new PortfolioDto();
		portfolioDto.setId(Optional.of(portfolio.getId()));
		portfolioDto.setNome(portfolio.getNome());
		portfolioDto.setDescricao(portfolio.getDescricao());
		portfolioDto.setUsuarioId(portfolio.getUsuario().getId());

		return portfolioDto;
	}

	/**
	 * Converte um PortfolioDto para uma entidade Portfolio.
	 * 
	 * @param portfolioDto
	 * @param result
	 * @return Portfolio
	 * @throws ParseException
	 */
	private Portfolio converterDtoParaPortfolio(PortfolioDto portfolioDto, BindingResult result) throws ParseException {
		Portfolio portfolio = new Portfolio();

		if (portfolioDto.getId().isPresent()) {
			Optional<Portfolio> lanc = this.portfolioService.buscarPorId(portfolioDto.getId().get());
			if (lanc.isPresent()) {
				portfolio = lanc.get();
			} else {
				result.addError(new ObjectError("portfolio", "Portfolio não encontrado."));
			}
		} else {
			portfolio.setUsuario(new Usuario());
			portfolio.getUsuario().setId(portfolioDto.getUsuarioId());
		}

		portfolio.setNome(portfolioDto.getNome());
		portfolio.setDescricao(portfolioDto.getDescricao());

		return portfolio;
	}

}

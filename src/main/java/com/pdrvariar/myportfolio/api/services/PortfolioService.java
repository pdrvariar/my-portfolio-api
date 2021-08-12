package com.pdrvariar.myportfolio.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.pdrvariar.myportfolio.api.entities.Portfolio;

public interface PortfolioService {

	/**
	 * @param usuarioId
	 * @param pageRequest
	 * @return Page<Portfolio>
	 */
	Page<Portfolio> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest);

	/**
	 * @param id
	 * @return Optional<Portfolio>
	 */
	Optional<Portfolio> buscarPorId(Long id);

	/**
	 * @param portfolio
	 * @return Portfolio
	 */
	Portfolio persistir(Portfolio portfolio);

	/**
	 * @param id
	 */
	void remover(Long id);
}

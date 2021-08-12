package com.pdrvariar.myportfolio.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pdrvariar.myportfolio.api.entities.Portfolio;
import com.pdrvariar.myportfolio.api.repositories.PortfolioRepository;
import com.pdrvariar.myportfolio.api.services.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	private static final Logger log = LoggerFactory.getLogger(PortfolioServiceImpl.class);

	@Autowired
	private PortfolioRepository portfolioRepository;

	@Override
	public Page<Portfolio> buscarPorUsuarioId(Long usuarioId, PageRequest pageRequest) {
		log.info("Buscando lançamentos para o usuário ID {}", usuarioId);
		return this.portfolioRepository.findByUsuarioId(usuarioId, pageRequest);
	}

	@Cacheable("portfolioPorId")
	@Override
	public Optional<Portfolio> buscarPorId(Long id) {
		log.info("Buscando um lançamento pelo ID {}", id);
		return this.portfolioRepository.findById(id);
	}

	@CachePut("portfolioPorId")
	@Override
	public Portfolio persistir(Portfolio portfolio) {
		log.info("Persistindo portfolio: {}", portfolio);
		return this.portfolioRepository.save(portfolio);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo o lançamento ID {}", id);
		this.portfolioRepository.deleteById(id);
	}
}

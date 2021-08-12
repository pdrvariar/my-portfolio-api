package com.pdrvariar.myportfolio.api.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.pdrvariar.myportfolio.api.entities.Portfolio;
import com.pdrvariar.myportfolio.api.repositories.PortfolioRepository;

@SpringBootTest
@ActiveProfiles("test")
public class PortfolioServiceTest {

	@MockBean
	private PortfolioRepository portfolioRepository;

	@Autowired
	private PortfolioService portfolioService;

	@BeforeEach
	public void setUp() throws Exception {
		BDDMockito.given(this.portfolioRepository.findByUsuarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.willReturn(new PageImpl<Portfolio>(new ArrayList<Portfolio>()));
		BDDMockito.given(this.portfolioRepository.findById(Mockito.anyLong()))
				.willReturn(Optional.ofNullable(new Portfolio()));
		BDDMockito.given(this.portfolioRepository.save(Mockito.any(Portfolio.class))).willReturn(new Portfolio());
	}

	@Test
	public void testBuscarPortfolioPorUsuarioId() {
		Page<Portfolio> portfolio = this.portfolioService.buscarPorUsuarioId(1L, PageRequest.of(0, 10));

		assertNotNull(portfolio);
	}

	@Test
	public void testBuscarPortfolioPorId() {
		Optional<Portfolio> portfolio = this.portfolioService.buscarPorId(1L);

		assertTrue(portfolio.isPresent());
	}

	@Test
	public void testPersistirPortfolio() {
		Portfolio portfolio = this.portfolioService.persistir(new Portfolio());

		assertNotNull(portfolio);
	}
}

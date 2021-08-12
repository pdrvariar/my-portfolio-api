package com.pdrvariar.myportfolio.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdrvariar.myportfolio.api.dtos.PortfolioDto;
import com.pdrvariar.myportfolio.api.entities.Portfolio;
import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.services.PortfolioService;
import com.pdrvariar.myportfolio.api.services.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PortfolioControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PortfolioService portfolioService;

	@MockBean
	private UsuarioService usuarioService;

	private static final String URL_BASE = "/api/portfolios/";
	private static final Long ID_USUARIO = 1L;
	private static final Long ID_PORTFOLIO = 1L;
	private static final String NOME = "Principal";
	private static final String DESCRICAO = "Meu Portfólio";

	@Test
	@WithMockUser
	public void testCadastrarPortfolio() throws Exception {
		Portfolio portfolio = obterDadosPortfolio();
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Usuario()));
		BDDMockito.given(this.portfolioService.persistir(Mockito.any(Portfolio.class))).willReturn(portfolio);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.obterJsonRequisicaoPost())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.usuarioId").value(ID_USUARIO)).andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@WithMockUser
	public void testCadastrarPortfolioUsuarioIdInvalido() throws Exception {
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.obterJsonRequisicaoPost())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Usuário não encontrado. ID inexistente."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@WithMockUser(username = "admin@admin.com", roles = { "ADMIN" })
	public void testRemoverPortfolio() throws Exception {
		BDDMockito.given(this.portfolioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Portfolio()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_PORTFOLIO).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testRemoverPortfolioAcessoNegado() throws Exception {
		BDDMockito.given(this.portfolioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Portfolio()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_PORTFOLIO).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private String obterJsonRequisicaoPost() throws JsonProcessingException {
		PortfolioDto portfolioDto = new PortfolioDto();
		portfolioDto.setId(null);
		portfolioDto.setNome(NOME);
		portfolioDto.setDescricao(DESCRICAO);
		portfolioDto.setUsuarioId(ID_USUARIO);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(portfolioDto);
	}

	private Portfolio obterDadosPortfolio() {
		Portfolio portfolio = new Portfolio();
		portfolio.setId(ID_PORTFOLIO);
		portfolio.setUsuario(new Usuario());
		portfolio.getUsuario().setId(ID_USUARIO);
		return portfolio;
	}

}

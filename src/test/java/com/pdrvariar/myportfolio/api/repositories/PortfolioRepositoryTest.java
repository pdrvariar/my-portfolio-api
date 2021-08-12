package com.pdrvariar.myportfolio.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.pdrvariar.myportfolio.api.entities.Portfolio;
import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.enums.PerfilEnum;
import com.pdrvariar.myportfolio.api.utils.PasswordUtils;

@SpringBootTest
@ActiveProfiles("test")
public class PortfolioRepositoryTest {

	@Autowired
	private PortfolioRepository portfolioRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private Long usuarioId;
	
	private static final String NOME = "Principal";
	private static final String DESCRICAO = "Meu Portfólio";

	@BeforeEach
	public void setUp() throws Exception {

		Usuario usuario = this.usuarioRepository.save(obterDadosUsuario());
		this.usuarioId = usuario.getId();

		this.portfolioRepository.save(obterDadosPortfolios(usuario));
		this.portfolioRepository.save(obterDadosPortfolios(usuario));
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.portfolioRepository.deleteAll();
	}

	@Test
	public void testBuscarPortfoliosPorUsuarioId() {
		List<Portfolio> portfolios = this.portfolioRepository.findByUsuarioId(usuarioId);

		assertEquals(2, portfolios.size());
	}

	@Test
	public void testBuscarPortfoliosPorUsuarioIdPaginado() {
		PageRequest page = PageRequest.of(0, 10);
		Page<Portfolio> portfolios = this.portfolioRepository.findByUsuarioId(usuarioId, page);

		assertEquals(2, portfolios.getTotalElements());
	}

	private Portfolio obterDadosPortfolios(Usuario usuario) {
		Portfolio portfolio = new Portfolio();
		portfolio.setNome(NOME);
		portfolio.setDescricao(DESCRICAO);
		portfolio.setUsuario(usuario);
		return portfolio;
	}

	private Usuario obterDadosUsuario() throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano de Tal");
		usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
		usuario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		usuario.setCpf("24291173474");
		usuario.setEmail("email@email.com");
		return usuario;
	}

}

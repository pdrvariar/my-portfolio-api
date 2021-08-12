package com.pdrvariar.myportfolio.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.enums.PerfilEnum;
import com.pdrvariar.myportfolio.api.utils.PasswordUtils;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String EMAIL = "email@email.com";
	private static final String CPF = "24291173474";

	@BeforeEach
	public void setUp() throws Exception {
		this.usuarioRepository.save(obterDadosUsuario());
	}

	@AfterEach
	public final void tearDown() {
		this.usuarioRepository.deleteAll();
	}

	@Test
	public void testBuscarUsuarioPorEmail() {
		Usuario usuario = this.usuarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, usuario.getEmail());
	}

	@Test
	public void testBuscarUsuarioPorCpf() {
		Usuario usuario = this.usuarioRepository.findByCpf(CPF);
		assertEquals(CPF, usuario.getCpf());
	}

	@Test
	public void testBuscarUsuarioPorEmailECpf() {
		Usuario usuario = this.usuarioRepository.findByCpfOrEmail(CPF, EMAIL);
		assertNotNull(usuario);
	}

	@Test
	public void testBuscarUsuarioPorEmailOuCpfParaEmailInvalido() {
		Usuario usuario = this.usuarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");
		assertNotNull(usuario);
	}

	@Test
	public void testBuscarUsuarioPorEmailOuCpfParaCpfInvalido() {
		Usuario usuario = this.usuarioRepository.findByCpfOrEmail("12345678901", EMAIL);
		assertNotNull(usuario);
	}

	private Usuario obterDadosUsuario() throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano de Tal");
		usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
		usuario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		usuario.setCpf(CPF);
		usuario.setEmail(EMAIL);
		return usuario;
	}

}

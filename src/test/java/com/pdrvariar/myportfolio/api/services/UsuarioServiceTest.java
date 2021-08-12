package com.pdrvariar.myportfolio.api.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.pdrvariar.myportfolio.api.entities.Usuario;
import com.pdrvariar.myportfolio.api.repositories.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@MockBean
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@BeforeEach
	public void setUp() throws Exception {
		BDDMockito.given(this.usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		BDDMockito.given(this.usuarioRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(new Usuario()));
		BDDMockito.given(this.usuarioRepository.findByEmail(Mockito.anyString())).willReturn(new Usuario());
		BDDMockito.given(this.usuarioRepository.findByCpf(Mockito.anyString())).willReturn(new Usuario());
	}

	@Test
	public void testPersistirUsuario() {
		Usuario usuario = this.usuarioService.persistir(new Usuario());

		assertNotNull(usuario);
	}

	@Test
	public void testBuscarUsuarioPorId() {
		Optional<Usuario> usuario = this.usuarioService.buscarPorId(1L);

		assertTrue(usuario.isPresent());
	}

	@Test
	public void testBuscarUsuarioPorEmail() {
		Optional<Usuario> usuario = this.usuarioService.buscarPorEmail("email@email.com");

		assertTrue(usuario.isPresent());
	}

	@Test
	public void testBuscarUsuarioPorCpf() {
		Optional<Usuario> usuario = this.usuarioService.buscarPorCpf("24291173474");

		assertTrue(usuario.isPresent());
	}
}

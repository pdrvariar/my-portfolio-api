package com.pdrvariar.myportfolio.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pdrvariar.myportfolio.api.entities.Portfolio;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "PortfolioRepository.findByUsuarioId", query = "SELECT port from Portfolio port WHERE port.usuario.id = :usuarioId") })
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

	List<Portfolio> findByUsuarioId(@Param("usuarioId") Long usuarioId);

	Page<Portfolio> findByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);

}

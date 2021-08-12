package com.pdrvariar.myportfolio.api.dtos;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;

public class PortfolioDto {

	private Optional<Long> id = Optional.empty();
	private String nome;
	private String descricao;
	private Long usuarioId;

	public PortfolioDto() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> id) {
		this.id = id;
	}

	@NotEmpty(message = "Nome não pode ser vazio.")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	@Override
	public String toString() {
		return "PortfolioDto [id=" + id + ", descricao=" + descricao + ", usuarioId=" + usuarioId + "]";
	}
}

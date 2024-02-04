package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CozinhaModel {

	private Long id;
	private String nome;
}

/*
@JsonIgnore
@OneToMany(mappedBy = "cozinha")
private List<Restaurante> restaurantes = new ArrayList<>();
*/
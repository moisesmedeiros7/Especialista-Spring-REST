package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Grupo {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false)
	private String nome;

	@ManyToMany
	@JoinTable(name = "grupo_permissao",             // <- nome da tabela criada
	joinColumns = @JoinColumn(name = "grupo_id"),   // <- nome do ID referência de grupo
	inverseJoinColumns = @JoinColumn(name = "permissao_id"))  // <- nome do ID referência de permissao
	List<Permissao> permissoes = new ArrayList<>();
	
}

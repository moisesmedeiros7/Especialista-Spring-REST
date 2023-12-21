package com.algaworks.algafood.domain.model;

import java.time.LocalDateTime;
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

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String senha;
	 
	@CreationTimestamp  //cria automaticamente o datetime na CRIAÇÃO da entidade
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCadastro;
	
	@ManyToMany
	@JoinTable(name = "usuario_grupo",             // <- nome da tabela criada
	joinColumns = @JoinColumn(name = "usuario_id"),   // <- nome do ID referência de USUÁRIO
	inverseJoinColumns = @JoinColumn(name = "grupo_id"))  // <- nome do ID referência de GRUPO
	List<Grupo> grupos = new ArrayList<>();
	
	
	
	
}

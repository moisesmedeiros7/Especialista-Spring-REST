package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Column(name = "taxa_frete")
	private BigDecimal taxaFrete;

//	@JsonIgnore
	@ManyToOne // (fetch = FetchType.LAZY) // <- Pra evitar o carregamento de entidade sem uso.
	@JoinColumn (name="cozinha_id", nullable = false)
	private Cozinha cozinha;
	
	@JsonIgnore 
	@CreationTimestamp  //cria automaticamente o datetime na CRIAÇÃO da entidade
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	@UpdateTimestamp  //cria automaticamente o datetime na ATUALIZAÇÃO da entidade
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataAtualizacao;
	
	@JsonIgnore
	@Embedded  // vai incorporar no BD as informações da classe endereço 
	private Endereco endereco;  // 6.4. Mapeando classes incorporáveis com @Embedded e @Embeddable
	
	@JsonIgnore
	@ManyToMany  (fetch = FetchType.LAZY) // Pra evitar o carregamento de entidade sem uso.
	@JoinTable(name = "restaurante_forma_pagamento",             // <- nome da tabela criada
			joinColumns = @JoinColumn(name = "restaurante_id"),   // <- nome do ID referência de restaurante
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))  // <- nome do ID referência de forma_pagamento
	private List<FormaPagamento> formasPagamento = new ArrayList<>();

	@JsonIgnore 
	@OneToMany (mappedBy = "restaurante")
	List<Produto> produtos = new ArrayList<>();
	
}

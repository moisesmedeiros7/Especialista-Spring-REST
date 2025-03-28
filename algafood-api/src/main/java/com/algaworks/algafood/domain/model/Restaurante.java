package com.algaworks.algafood.domain.model;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.core.validation.Groups;
import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", 
descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@NotNull  // <- BEAN VALIDATION
//	@NotEmpty
	@NotBlank
	private String nome;

//	@DecimalMin("0")
//	@Multiplo(numero = 5)	
	@NotNull
	@PositiveOrZero
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;

	
	@Valid // <-validar (BeanValidation) as propriedades de cozinha 
	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@NotNull
	@ManyToOne // (fetch = FetchType.LAZY) // <- Pra evitar o carregamento (Consultas BD) de entidade sem uso.
	@JoinColumn (name="cozinha_id", nullable = false)
	private Cozinha cozinha;
	
 
	@CreationTimestamp  //cria automaticamente o datetime na CRIAÇÃO da entidade
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;
	
	@UpdateTimestamp  //cria automaticamente o datetime na ATUALIZAÇÃO da entidade
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	
	@Embedded  // vai incorporar no BD as informações da classe endereço 
	private Endereco endereco;  // 6.4. Mapeando classes incorporáveis com @Embedded e @Embeddable
	
	private Boolean ativo = Boolean.TRUE;
	
	private Boolean aberto = Boolean.FALSE;
	
	@ManyToMany  (fetch = FetchType.LAZY) // Pra evitar o carregamento de entidade sem uso.
	@JoinTable(name = "restaurante_forma_pagamento",             // <- nome da tabela criada
			joinColumns = @JoinColumn(name = "restaurante_id"),   // <- nome do ID referência de restaurante
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))  // <- nome do ID referência de forma_pagamento
	private Set<FormaPagamento> formasPagamento = new HashSet<>();
 
	@OneToMany (mappedBy = "restaurante")
	List<Produto> produtos = new ArrayList<>();
	
	@ManyToMany (fetch = FetchType.LAZY)
	@JoinTable (name= "restaurante_usuario_responsavel",
		joinColumns = @JoinColumn(name = "restaurante_id"),   
		inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> responsaveis = new HashSet<>();
	
	public void adicionarResponsavel (Usuario usuario) {
		responsaveis.add(usuario);
	}
	
	public void removerResponsavel (Usuario usuario) {
		responsaveis.remove(usuario);
	}
	
	public void ativar() {
		setAtivo(true);
	}
	
	public void inativar() {
		setAtivo(false);
	}
	
	public void abrir () {
		setAberto(true);
	}
	
	public void fechar () {
		setAberto(false);
	}
	
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().remove(formaPagamento);
	}
	
	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().add(formaPagamento);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return getFormasPagamento().contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return !aceitaFormaPagamento(formaPagamento);
	}

}

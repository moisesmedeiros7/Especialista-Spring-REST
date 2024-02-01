package com.algaworks.algafood.api.model.mixin;

import java.time.OffsetDateTime;
import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * {@link RestauranteMixin} é utilizada para setar as configurações
 * json da classe {@link Restaurante}, deixando o código mais limpo,e
 * também para setar essas configurações quando não se tem acesso as
 *  classes do pacote modelo.
 */

public abstract class RestauranteMixin {

    @JsonIgnoreProperties(value = "nome", allowGetters = true)
    private Cozinha cozinha;
    
    @JsonIgnore
    private Endereco endereco;
    
    @JsonIgnore
    private OffsetDateTime dataCadastro;
    
    @JsonIgnore
    private OffsetDateTime dataAtualizacao;
    
    @JsonIgnore
    private List<FormaPagamento> formasPagamento;
    
    @JsonIgnore
    private List<Produto> produtos;
    
}        



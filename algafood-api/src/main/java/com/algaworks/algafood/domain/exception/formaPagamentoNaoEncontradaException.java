package com.algaworks.algafood.domain.exception;

public class formaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;
	
	public formaPagamentoNaoEncontradaException(String mensagem) {
		super (mensagem);
	}
	
	public formaPagamentoNaoEncontradaException(Long estadoId) {
		this(String.format("Não existe uma cadastro de forma de pagamento com código %d", estadoId));
	}
	
}

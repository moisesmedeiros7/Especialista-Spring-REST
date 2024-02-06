package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.formaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service	
public class CadastroFormaPagamentoService {

	private static final String MSG_FORMA_PAGAMENTO_EM_USO 
	= "Forma de pagamento de código %d não pode ser removida, pois está em uso";

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository ;
	 
    public List<FormaPagamento> listar(){
    	return formaPagamentoRepository.findAll();
    }
    
    public FormaPagamento buscar (Long id) {
    	return formaPagamentoRepository.findById(id).get();
    }
    
    public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
		return formaPagamentoRepository.findById(formaPagamentoId)
			.orElseThrow(() -> new formaPagamentoNaoEncontradaException(formaPagamentoId));
	}
    
    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        return formaPagamentoRepository.save(formaPagamento);
    }
    
	@Transactional
	public void excluir(Long formaPagamentoId) {
		try {
			formaPagamentoRepository.deleteById(formaPagamentoId);
			formaPagamentoRepository.flush(); //forçar operações do BD p/ não pular exceção
		} catch (EmptyResultDataAccessException e) {
			throw new formaPagamentoNaoEncontradaException(formaPagamentoId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format(MSG_FORMA_PAGAMENTO_EM_USO, formaPagamentoId));
		}
	}
    
    
}

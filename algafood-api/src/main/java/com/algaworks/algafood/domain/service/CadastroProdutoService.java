package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;


@Service
public class CadastroProdutoService {
	
	private static final String MSG_PRODUTO_EM_USO 
	= "Produto de código %d não pode ser removido, pois está em uso";

    @Autowired
    private ProdutoRepository produtoRepository;
    
    public List<Produto> listar () {
    	return produtoRepository.findAll();
    }
    
    public Produto buscar (Long produtoId) {
    	return produtoRepository.findById(produtoId).get();
    }
    
    public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findById(restauranteId, produtoId)
            .orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }   
    
	@Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
    
	@Transactional
    public void excluir(Long restauranteId, Long produtoId ) {
        try {
        	produtoRepository.deleteById(produtoId);
        	produtoRepository.flush(); //forçar operações do BD p/ não pular exceção
        } catch (EmptyResultDataAccessException e) {
            throw new ProdutoNaoEncontradoException(restauranteId, produtoId);
        
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_PRODUTO_EM_USO, produtoId));
        }
    }
    

    
}

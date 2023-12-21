package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean  //Para o spring não instanciar
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID>{

	Optional<T> buscarPrimeiro();
}

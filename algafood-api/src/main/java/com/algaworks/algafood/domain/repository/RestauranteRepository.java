package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, 
RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante>   {

	
	// Errata: se um restaurante não tiver nenhuma forma de pagamento associada a ele,
	// esse restaurante não será retornado usando JOIN FETCH r.formasPagamento.
	// Para resolver isso, temos que usar LEFT JOIN FETCH r.formasPagamento
//	@Query("from Restaurante r join fetch r.cozinha join fetch r.formasPagamento")
//	@Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
	
	@Query("from Restaurante r join fetch r.cozinha") // <- sem pegar forma de pagamento
	List<Restaurante> findAll(); // implementar esse método vai fazer cm que se tenha menos conultas no BD devido as instancias FK
	
	
	
	/* Between é keyword que traz um INTERVALO. Também funciona com datas */
	List<Restaurante> findByTaxaFreteBetween (BigDecimal taxaInicial, BigDecimal taxaFinal);
	/* Faz uma pesquisa de parte do nome e id da cozinha */
	List<Restaurante> findByNomeContainingAndCozinhaId (String nome, Long Cozinha);
	/* First faz com que venha apenas o primeiro resultado */
	Optional <Restaurante> findFirstByNomeContaining (String nome);
	/* Top2 faz com que venham apenas dois resultados */
	List<Restaurante> findTop2ByNomeContaining (String nome);
	/* count contabiliza quantos restaurantes existem com cozinhaId */
	int countByCozinhaId (Long cozinha);
	
	// 5.9. Usando queries JPQL customizadas com @Query
	//@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")  //comentado para usar arquivo xml
	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);
	
	// 5.18 
	
	
}

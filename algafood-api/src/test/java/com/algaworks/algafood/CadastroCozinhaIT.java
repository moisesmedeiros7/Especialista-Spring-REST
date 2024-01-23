package com.algaworks.algafood;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT {
	

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	private static final int COZINHA_ID_INEXISTENTE = 100;
	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;
	
	@BeforeEach  // método vai rodar antes de cada método de teste
	private void setUp () {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Se o teste falhar, apresenta dados no output
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
		
		databaseCleaner.clearTables();
		prepararDados();
		
		
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConterQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
				
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeCozinhasCadastradas))
			.body("nome", Matchers.hasItems("Americana", "Tailandesa") );
	}
	

	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		RestAssured.given()
		.body(jsonCorretoCozinhaChinesa)
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
	.when()
		.post()
	.then()
		.statusCode(HttpStatus.CREATED.value());
	}
	
	// GET /cozinhas/{cozinhaId}
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente () {
		RestAssured.given()
			.pathParam("cozinhaId", cozinhaAmericana.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(cozinhaAmericana.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente () {
		RestAssured.given()
			.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	
	
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
	    cozinhaTailandesa.setNome("Tailandesa");
	    cozinhaRepository.save(cozinhaTailandesa);

	    cozinhaAmericana = new Cozinha();
	    cozinhaAmericana.setNome("Americana");
	    cozinhaRepository.save(cozinhaAmericana);
	    
	    quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}
	
	
}


/*

//  testes de integração
 
 @SpringBootTest
public class CadastroCozinhaIT {

@Autowired
private CadastroCozinhaService cadastroCozinha;

@Test
public void testarCadastroCozinhaComSucesso() {
	// cenário
	Cozinha novaCozinha = new Cozinha();
	novaCozinha.setNome("Chinesa");
	
	// ação
	novaCozinha = cadastroCozinha.salvar(novaCozinha);
	
	// validação
	assertThat(novaCozinha).isNotNull();
	assertThat(novaCozinha.getId()).isNotNull();
}

@Test
public void testarCadastroCozinhaSemNome() {
   Cozinha novaCozinha = new Cozinha();
   novaCozinha.setNome(null);
   
   ConstraintViolationException erroEsperado =
      Assertions.assertThrows(ConstraintViolationException.class, () -> {
         cadastroCozinha.salvar(novaCozinha);
      });
   
   assertThat(erroEsperado).isNotNull();
}

@Test
public void deveFalhar_QuandoExcluirCozinhaEmUso() {
	  
	EntidadeEmUsoException erroEsperado =
	      Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
	         cadastroCozinha.excluir(1L);
	      });
	 assertThat(erroEsperado).isNotNull();
//	 System.out.println(erroEsperado);
}

@Test
public void deveFalhar_QuandoExcluirCozinhaInexistente() {
	CozinhaNaoEncontradaException erroEsperado =
		      Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
		         cadastroCozinha.excluir(1000L);
		      });
		 assertThat(erroEsperado).isNotNull();
//		 System.out.println(erroEsperado);
}

}

*/

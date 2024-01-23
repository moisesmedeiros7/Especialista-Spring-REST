package com.algaworks.algafood;

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
	
	@BeforeEach  // método vai rodar antes de cada método de teste
	public void setUp () {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Se o teste falhar, apresenta dados no output
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
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
	public void deveConter2Cozinhas_QuandoConsultarCozinhas() {
				
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(2))
			.body("nome", Matchers.hasItems("Americana", "Tailandesa") );
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha () {
		RestAssured.given()
			.body(" { \"nome\" : \"Chinesa\"  } ")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);

		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Americana");
		cozinhaRepository.save(cozinha2);
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

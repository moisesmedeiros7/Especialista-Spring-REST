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
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {
	

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;
	private Restaurante restauranteAmericano;
	private int quantidadeRestaurantesCadastrados;
	private String jsonCorretoRestauranteCamaroes;
	
	@BeforeEach  // método vai rodar antes de cada método de teste
	private void setUp () {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Se o teste falhar, apresenta dados no output
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		
		jsonCorretoRestauranteCamaroes = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
		
		databaseCleaner.clearTables();
		prepararDados();
		
		
	}
	
	
	
	
	
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
	    cozinhaTailandesa.setNome("Tailandesa");
	    cozinhaRepository.save(cozinhaTailandesa);

	    cozinhaAmericana = new 
	    cozinhaAmericana.setNome("Americana");
	    cozinhaRepository.save(cozinhaAmericana);
	    
	    quantidadeRestaurantesCadastrados = (int) restauranteRepository.count();
	}
	
	
}


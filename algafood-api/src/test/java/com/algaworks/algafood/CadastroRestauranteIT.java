package com.algaworks.algafood;

import java.math.BigDecimal;

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

// https://app.algaworks.com/aulas/1985/desafio-escrevendo-testes-de-api

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {
	

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;
	private Restaurante restauranteAmericano;
	private int quantidadeRestaurantesCadastrados;
	private int quantidadeCozinhasCadastradas;
	
	private String jsonCorretoRestauranteCamaroes;
	private String jsonIncorretoRestauranteCamaroes;
	private String xmlRestauranteCamaroes;
	
	@BeforeEach  // método vai rodar antes de cada método de teste
	private void setUp () {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Se o teste falhar, apresenta dados no output
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		
		jsonCorretoRestauranteCamaroes = ResourceUtils.getContentFromResource(
				"/json/correto/restaurante-camaroes.json");
		
		jsonIncorretoRestauranteCamaroes = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-camaroes-semtaxafrete.json");
		
		xmlRestauranteCamaroes = ResourceUtils.getContentFromResource(
				"/json/incorreto/restaurante-camaroes.xml");
		
		databaseCleaner.clearTables();
		prepararDados();
		
		
	}
	
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
	    cozinhaTailandesa.setNome("Tailandesa");
	    cozinhaRepository.save(cozinhaTailandesa);

	    Cozinha cozinhaAmericana = new Cozinha();
	    cozinhaAmericana.setNome("Americana");
	    cozinhaRepository.save(cozinhaAmericana);
	    
	    quantidadeCozinhasCadastradas = (int) restauranteRepository.count();
	    
	    
		Restaurante restauranteThaiGourmet = new Restaurante();
		restauranteThaiGourmet.setNome("Thai Gourmet");
		restauranteThaiGourmet.setTaxaFrete( new BigDecimal(10) );
		restauranteThaiGourmet.setCozinha(cozinhaTailandesa);
		restauranteRepository.save(restauranteThaiGourmet);
		
		Restaurante restauranteMcDonalds = new Restaurante();
		restauranteMcDonalds.setNome("Mc Donalds");
		restauranteMcDonalds.setTaxaFrete( new BigDecimal(15) );
		restauranteMcDonalds.setCozinha(cozinhaAmericana);
		restauranteRepository.save(restauranteMcDonalds);
		
		quantidadeRestaurantesCadastrados = (int) restauranteRepository.count();
	}
	
	@Test
	public void deveRetornarStatus200eQtdeCorreta_QuandoConsultarRestaurantes() {
		
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value())
			.body ("", Matchers.hasSize(quantidadeRestaurantesCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarRestauranteCorretamente() {
			
			RestAssured.given()
			 	.body(jsonCorretoRestauranteCamaroes)
			 	.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCampoObg() {
			
			RestAssured.given()
			 	.body(jsonIncorretoRestauranteCamaroes)
			 	.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void deveRetornarStatus415_QuandoCadastrarRestauranteComXML() {
			
			RestAssured.given()
			 	.body(xmlRestauranteCamaroes)
			 	.contentType(ContentType.XML)
				.accept(ContentType.XML)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente () {
		RestAssured.given()
			.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{restauranteId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus204_QuandoDeletarRestauranteExistente () {
		RestAssured.given()
			.pathParam("restauranteId", 1L)
			.accept(ContentType.JSON)
		.when()
			.delete("/{restauranteId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	public void deveRetornarStatus404_QuandoDeletarRestauranteInexistente () {
		RestAssured.given()
			.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{restauranteId}")
		.then()	
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	
}


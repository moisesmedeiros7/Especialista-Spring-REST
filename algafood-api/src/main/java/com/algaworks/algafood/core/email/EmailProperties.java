package com.algaworks.algafood.core.email;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

	@NotNull
	private String remetente;
		
	private Fake local = new Fake();
	private Smtp smtp = new Smtp();
	private Impl impl = Impl.FAKE;
		
		public enum Impl {
			
			SMTP, FAKE
			
		}
	
		@Getter
		@Setter
		public class Fake {
			
			private String impl; 
		}
		
		@Getter
		@Setter
		public class Smtp {
			
			private String impl; 
			
		}
	
}
package com.algaworks.algafood.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class validationConfig {
	
	public LocalValidatorFactoryBean validator (MessageSource messageSource ) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource); // indica para o spring usar o messages.properties
		return bean;
	}
	
}

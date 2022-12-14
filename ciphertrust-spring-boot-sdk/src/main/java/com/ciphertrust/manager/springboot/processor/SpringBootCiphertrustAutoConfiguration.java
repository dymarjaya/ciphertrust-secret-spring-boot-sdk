/*
 *
 * CipherTrust Manager SDK Spring Boot
 * Code by Mudito Adi Pranowo
 * December 2022
 *
 */

package com.ciphertrust.manager.springboot.processor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

@ConditionalOnClass(CiphertrustRetrieveSecretService.class)

public class SpringBootCiphertrustAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	CiphertrustValueClassProcessor ciphertrustSecretValueClassProcessor() {
		return new CiphertrustValueClassProcessor();
	}

	@ConditionalOnMissingBean
	@Bean
	CiphertrustRetrieveSecretService ciphertrustRetrieveSecretService() {
		return new CiphertrustRetrieveSecretService();
	}

}

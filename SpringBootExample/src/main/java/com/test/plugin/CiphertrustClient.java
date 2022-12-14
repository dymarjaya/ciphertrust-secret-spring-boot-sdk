/*
 *
 * CipherTrust Manager SDK Spring Boot - Sample Client Retrieve Secret
 * Code by Mudito Adi Pranowo
 * December 2022
 * Notes: trationDelegate$BeanPostProcessorChecker : Bean 'com.ciphertrust.manager.springboot.processor.SpringBootCiphertrustAutoConfiguration' of type [com.ciphertrust.manager.springboot.processor.SpringBootCiphertrustAutoConfiguration$$EnhancerBySpringCGLIB$$4756ddf9] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
 * https://github.com/spring-cloud/spring-cloud-config/issues/1400
 *
 */

package com.test.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ciphertrust.manager.springboot.annotations.CiphertrustValue;

@SpringBootApplication
public class CiphertrustClient implements CommandLineRunner{

	private static Logger logger = LoggerFactory.getLogger(CiphertrustClient.class);

	@CiphertrustValue(key="mypass")
	private String secret1;

	@CiphertrustValue(key="sbpassword")
	private String secret2;

    public static void main(String[] args) {
        SpringApplication.run(CiphertrustClient.class, args);
    }

	@Override
	public void run(String... args) {

		logger.info("Fetch secret1 from CipherTrust Manager --> " + secret1);
		logger.info("Fetch secret2 from CipherTrust Manager --> " + secret2);
		
	}
}


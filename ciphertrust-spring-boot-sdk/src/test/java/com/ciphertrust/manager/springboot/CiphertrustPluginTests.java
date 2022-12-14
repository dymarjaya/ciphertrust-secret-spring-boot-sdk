/*
 *
 * CipherTrust Manager SDK Spring Boot
 * Code by Mudito Adi Pranowo
 * December 2022
 *
 */

package com.ciphertrust.manager.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.ciphertrust.manager.springboot.annotations.CiphertrustValue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CiphertrustPluginTests.class)
		public class CiphertrustPluginTests {

			@CiphertrustValue(key="mypass")
			private String mySecretFromCustomAnnotation;

			@Test
			void testForAllEnvVariables() {
			assertEquals(System.getenv("CM_REFRESH_TOKEN"), "qJ6HJfr6fqKQynnFU2x4AN1A3yyo94U42nH9HfJftAuE56FFrKlFLRI1DmGfr8Ff");
			assertEquals(System.getenv("CM_APPLIANCE_URL"), "ciphertrust.dymarjaya.com");
			}
		}
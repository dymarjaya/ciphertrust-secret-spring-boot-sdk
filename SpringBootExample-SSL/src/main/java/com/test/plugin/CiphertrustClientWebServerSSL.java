/*
 *
 * CipherTrust Manager SDK Spring Boot - Sample Protect Web Server SSL Secret
 * Code by Mudito Adi Pranowo
 * December 2022
 *
 */

package com.test.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ciphertrust.manager.springboot.annotations.CiphertrustValue;

@SpringBootApplication
@Controller
public class CiphertrustClientWebServerSSL {
	@CiphertrustValue(key="mypass")
	private String privateKeyPassword;
	@GetMapping("/")
	public String index(final Model model) {
		model.addAttribute("title", "Spring Boot SSL (HTTPS)");
		model.addAttribute("msg", "Welcome to the Spring Boot SSL!");
		return "index";
	}
	public static void main(String[] args) {
		SpringApplication.run(CiphertrustClientWebServerSSL.class, args);

	}
	@Component
	public class KeystoreInit {
		private final Environment environment;

		@Autowired
		public KeystoreInit(Environment environment) {
			this.environment = environment;
		}

		@Bean
		@Primary
		public ServerProperties serverProperties() {
			final ServerProperties serverProperties = new ServerProperties();
			final Ssl ssl = new Ssl();
			final String keystorePassword = getKeystorePassword();
			ssl.setKeyPassword(keystorePassword);
			System.setProperty("server.ssl.key-store-password", keystorePassword);
			serverProperties.setSsl(ssl);
			return serverProperties;
		}

		private String getKeystorePassword() {
			return privateKeyPassword;
		}
	}
}

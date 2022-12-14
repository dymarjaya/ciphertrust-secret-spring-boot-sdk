/*
 *
 * CipherTrust Manager SDK Spring Boot
 * Code by Mudito Adi Pranowo
 * December 2022
 *
 */

package com.ciphertrust.manager.springboot.processor;

import com.ciphertrust.manager.springboot.cm.CipherTrustManagerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CiphertrustRetrieveSecretService {

	private static Logger logger = LoggerFactory.getLogger(CiphertrustRetrieveSecretService.class);

	public String retriveSingleSecretForCustomAnnotation(String key) {
		String result = null;
		CipherTrustManagerHelper cipherTrustManagerHelper = new CipherTrustManagerHelper();
		String refreshtoken = System.getenv().getOrDefault("CM_REFRESH_TOKEN", null);
		try {
//			result = cipherTrustManagerHelper.cmRESTSecret(key, refreshtoken) != null
//					? cipherTrustManagerHelper.cmRESTSecret(key, refreshtoken).getBytes()
//					: null;
			result = cipherTrustManagerHelper.cmRESTSecret(key, refreshtoken);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}

/*
 *
 * CipherTrust Manager SDK Spring Boot
 * Code by Mudito Adi Pranowo
 * December 2022
 *
 */

package com.ciphertrust.manager.springboot.processor;

import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ReflectionUtils;

import com.ciphertrust.manager.springboot.annotations.CiphertrustValue;

@Configuration
public class CiphertrustValueClassProcessor implements BeanPostProcessor {

	@Lazy
	@Autowired
	CiphertrustRetrieveSecretService ciphertrustRetrieveSecretService;

	private static Logger logger = LoggerFactory.getLogger(CiphertrustValueClassProcessor.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		Class<?> managedBeanClass = bean.getClass();

		List<Field> fieldList = FieldUtils.getFieldsListWithAnnotation(managedBeanClass, CiphertrustValue.class);

		for (Field field : fieldList) {
			if (field.isAnnotationPresent(CiphertrustValue.class)) {
				ReflectionUtils.makeAccessible(field);
				String variableId = field.getDeclaredAnnotation(CiphertrustValue.class).key();
				String result = null;
				try {
					result = ciphertrustRetrieveSecretService.retriveSingleSecretForCustomAnnotation(variableId);

					field.set(bean, result);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
		return bean;
	}

	@Nullable
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return null;
	}

}

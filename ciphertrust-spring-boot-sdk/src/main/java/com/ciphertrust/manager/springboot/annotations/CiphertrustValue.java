/*
 *
 * CipherTrust Manager SDK Spring Boot
 * Code by Mudito Adi Pranowo
 * December 2022
 *
 */

package com.ciphertrust.manager.springboot.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CiphertrustValue {

	String key();

	String kind() default "variable";
}

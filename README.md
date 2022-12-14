# Thales CipherTrust Spring Boot Plugin

The CipherTrust Manager Spring Boot Plugin provides client-side support for externalized configuration of secrets in a distributed system. You can integrate the plugin with Spring Boot applications to retrieve secrets from CipherTrust Manager. Using the Spring Boot Plugin, you can retrieve application credentials and secrets stored in CipherTrust Manager with minimal code changes to the existing Spring Boot application code.


## Benefits of storing application secrets in [CipherTrust Manager's Vault]

* Provides one central location to store and retrieve secrets for applications across all environments.
* Supports the management of secrets such as username and password for remote applications and resources.
* Provides credentials for external services like Tomcat Webserver, MySQL, PostgreSQL, Apache Cassandra, Couchbase, MongoDB, Consul, AWS, and more.

## Features

The following features are available with the Spring Boot Plugin:

* Retrieve a single secret from the CipherTrust Vault by specifying the alias to the secret in the CipherTrust Manager Vault.
* Retrieve secrets from the CipherTrust Vault and initialize the Spring environment with remote property sources.

## Limitations

The Spring Boot Plugin does not support creating, deleting, or updating secrets.

## Technical Requirements

| Technology          | Version |
|---------------------|---------|
| Java                | 11      |
| Spring Boot         | 2.7.x   |
| CipherTrust Manager | 2.10    |


# Prerequisites

The following are prerequisites to using the Spring Boot Plugin.

## Installation and Setup

### CipherTrust Manager setup

Install and setup CipherTrust Manager (call Dymar if you need any consultation; for customer only).

### Store your secret

The easiest way to store any password or secret in CipherTrust Manager vault is using CLI Rest API.

Before store the secret, you need to authenticate to your account in CipherTrust Manager.

```
authentication command:
curl -k -X POST '[CipherTrust_Manager_Address]/api/v1/auth/tokens/' -H 'Content-Type: application/json' --data '{"username": "[username_account]", "password": "[username_password]", "grant_type": "password"}'

Sample command:
curl -k -X POST 'https://ciphertrust.dymar.com/api/v1/auth/tokens/' -H 'Content-Type: application/json' --data '{"username": "admin", "password": "mypassword", "grant_type": "password"}'
```

You will get response from CipherTrust Manager such as below:
```
{"jwt":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiI2MTJjZDM2ZS0wNjZhLTRhZmMtYTNhNy0wZDFjZjMzYmU5OGIiLCJzdWIiOiJsb2NhbHw4MDI0YTk2Ni0zZmRhLTQxYjEtODNiMC1lZTBjNzU4OWM1MjEiLCJpc3MiOiJreWxvIiwiYWNjIjoia3lsbyIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiY3VzdCI6eyJkb21haW5faWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJkb21haW5fbmFtZSI6InJvb3QiLCJncm91cHMiOlsiQWxsIENsaWVudHMiLCJhZG1pbiJdLCJzaWQiOiI3M2I1MjBkZi1iYTQxLTRiYjgtYTMzZS1jNjJjYzU2YTRlZDMiLCJ6b25lX2lkIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIn0sImp3dGlkIjoiNTRlZjIxNjEtNDcwOS00NWExLWE3YTAtZmI5NDZhZjg4MmIwIiwiaWF0IjoxNjcwOTI0NDE5LCJleHAiOjE2NzA5MjQ3MTl9.BsDc65R9VZyPYAUIsfRWh1LMXGNDfYyHhPY4fTFIKUk","duration":300,"token_type":"Bearer","client_id":"73b520df-ba41-4bb8-a33e-c62cc56a4ed3","refresh_token_id":"5654a61f-df69-49b4-9efb-d91a7a87e0b5","refresh_token":"LzdpmPM7kpSHM0DohOsgPR8MorDvrjnFoRT5EFMDGma1EhuZMaMEQtyY2XvmTpqy"}
```
You can use the `jwt` token to execute store secret Rest API. Don't forget to keep your `refresh_token` to fetch the secret from Spring Boot App later.
```
store secret command:
curl -k -X POST '[CipherTrust_Manager_Address]/api/v1/vault/secrets' -H 'Authorization: Bearer <<<JWT_TOKEN>>>' -H 'Content-Type: application/json' -H 'accept: application/json' --data '{"name": "[secret_alias]", "dataType": "password", "material": "[secret_value]"}'

sample command:
curl -k -X POST 'https://ciphertrust.dymar.com/api/v1/vault/secrets' -H 'Authorization: Bearer eyJhbG...GIzA' -H 'Content-Type: application/json' -H 'accept: application/json' --data '{"name": "mypass", "dataType": "password", "material": "123456"}'
```

### Spring Boot Setup

You can import the CipherTrust Spring Boot Plugin manually by building the source code locally or using a dependency configuration to import from Maven Central. For information about your specific use case, see the following instructions.

#### Using the source code

You can grab the library's dependencies using Maven:

1. Create a new Maven project using an IDE of your choice.
2. If you are using Maven to manage your project's dependencies, include the following
   Spring Boot Plugin dependency snippet in your `pom.xml` under `<project>`/`<dependencies>`:

```xml
       <dependency>
         <groupId>com.ciphertrust</groupId>
         <artifactId>ciphertrust-sdk-springboot</artifactId>
         <version>LATEST</version>
      </dependency>
```

NOTE: Depending on the Java compiler version you have, you may need to update
the version. At this time, we are targeting compatibility with Java 11:

```xml
  <properties>
    <maven.compiler.source>{version}</maven.compiler.source>
    <maven.compiler.target>{version}</maven.compiler.target>
  </properties>
```

Run `mvn install -DskipTests` in `ciphertrust-spring-boot-sdk` directory to install the Spring Boot Plugin into your local Maven repository.

#### Using the Jarfile

If generating a JAR is preferred, build the library locally and add the dependency to the project manually:

1. Clone the Spring Boot Plugin repository locally: `git clone {repo}`
2. Go into the cloned repository with `cd cipherTrust-spring-boot-sdk`
3. Run `mvn package -DskipTests` to generate a JAR file. The output `.jar` files are located
   in the `target` directory of the repository.

4a. For Intellij, follow the steps outlined [here](https://www.jetbrains.com/help/idea/library.html)
to add the SDK JAR files to the new app's project.

4b. For Eclipse, `right click project > Build Path > Configure Build Path > Library > Add External JARs`.

#### Setup trust between app and CipherTrust

By default, CipherTrust Manager generates and uses self-signed SSL certificates. Without trusting them, the Java app cannot connect to the CipherTrust Manager server using the CipherTrust APIs. You need to configure your app to trust them.
1. Copy the .pem certificate created while setting up the CipherTrust Manager or get with export the CipherTrust Manager certificate from browser.
2. Store the certificate to trusted store Java (CA certs).
```
Command:
keytool -importcert -alias [alias_cert] -file [cipehrtrust_certificate_name] -keystore [cacrets_location]

Sample command:
keytool -importcert -alias cm-dji -file ciphertrustmanager.crt -keystore /usr/lib/jvm/java-11-openjdk-amd64/lib/security/cacerts

password: changeme or changeit
```

## Environment setup

Once the setup steps are successfully run, define the variables needed to make the connection between the plugin and CipherTrust Manager. You can do this by setting
[environment variables](#environment-variables).


#### Environment variables

Environment variables are mapped to configuration variables
by prepending `CM_` to the all-caps name of the configuration variable.
For example:`appliance_url` is `CM_APPLIANCE_URL`, `refresh_token_from_ciphertrust manager` is `CM_REFRESH_TOKEN`.

If no other configuration is done (e.g. over system properties or CLI parameters), include the following environment variables in the app's runtime environment to use the Spring Boot Plugin.

| Name                     | Environment ID    |   Description                            |
| ------------------------ | ------------------|------------------------------------------|
| CipherTrust Account      | CM_APPLIANCE_URL  |  CipherTrust Manager instance to connect |
| API key                  | CM_REFRESH_TOKEN  |  Token to autenticate                    |


##### Set environment variables in the Eclipse IDE

1. Select the Client Class in Eclipse, then right click Properties -> Run/Debug Setting-> New.
2. In the Select Configuration popup, click the Java App.
3. In the Edit Launch Configuration properties window, select the Environment Tab and click Add.
4. In the New Environment Variable window, enter the properties with the corresponding name and vale one at a time by clicking the
   Add button followed by Apply & Close.

##### Set environment variables in the IntelliJ IDE
Follow the steps outlined in the [IntelliJ documentation](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html).

##### Set environment variables in CLI
```
$ export CM_APPLIANCE_URL=ciphertrust.dymar.com
$ export CM_REFRESH_TOKEN=qJ6HJfr6fqKQynnFU2x4AN1A3yyo94U42nH9HfJftAuE56FFrKlFLRI1DmGfr8Ff
```
###### Add environment variables

* CM_APPLIANCE_URL is the URL of the CipherTrust instance to which you are connecting. When connecting to CipherTrust Manager Enterprise, configure for high availability by including the URL of the master load balancer. Example: ciphertrust.dymar.com
* CM_REFRESH_TOKEN is the refresh token to supply authentication to CipherTrust Manager instead of username password.

## Using the CipherTrust Spring Boot Plugin

How to use the plugin.
* @CiphertrustValue is the CipherTrust Manager native annotations (custom annotations) that enable individual secret retrieval.


#### CipherTrust Manager native annotations (custom annotation)
The `@CiphertrustValue` annotation are intended for new Spring Boot applications. Injecting `@CiphertrustValue`
into your Spring Boot code allows you to retrieve a single secret from the CipherTrust Manager Vault. Type the alias of
the secret want to retrieve in annotation with `key=` prefix.

```
@SpringBootApplication
public class CiphertrustClient implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(CiphertrustClient.class);

	@CiphertrustValue(key="mypass")
	private String secret1;
	
	public static void main(String[] args) {
        SpringApplication.run(CiphertrustClient.class, args);
        }

	@Override
	public void run(String... args) {
	
	logger.info("Fetch secret1 from CipherTrust Manager --> " + secret1);
		
	}
}
```

## About the sample code
1. ciphertrust-spring-boot-sdk
2. SpringBootExample
3. SpringBootExample-SSL

> **Note**
> This sample code is NOT an official version of Thales CPL. This plugin was created by PT Dymar Jaya Indonesia as a platinum partner of Thales CPL.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
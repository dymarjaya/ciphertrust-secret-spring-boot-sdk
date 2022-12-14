/*
 *
 * CipherTrust Manager SDK Spring Boot
 * Code modified by Mudito Adi Pranowo
 * December 2022
 * Source https://github.com/thalescpl-io/CipherTrust_Application_Protection/blob/master/rest/src/main/java/com/thales/cm/rest/cmhelper/CipherTrustManagerHelper.java
 *
 */

package com.ciphertrust.manager.springboot.cm;

import java.io.IOException;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.jayway.jsonpath.JsonPath;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherTrustManagerHelper {
    String cmdebug = "0";
    public String token = null;
    public String key = null;
    public String cmipaddress = System.getenv().getOrDefault("CM_APPLIANCE_URL", null );
    public String username;
    public String password;
    public String dataformat;
    public static final String quote = "\"";
    public static final String comma = ",";
    public static final MediaType JSONOCTET = MediaType.get("application/octet-stream");
    OkHttpClient client = getUnsafeOkHttpClient();

    private static Logger logger = LoggerFactory.getLogger(CipherTrustManagerHelper.class);

    public CipherTrustManagerHelper() {
        super();
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            if (envName.equalsIgnoreCase("cmuserid")) {
                username = env.get(envName);
                if (cmdebug.equalsIgnoreCase("1"))
                    System.out.println("cmuserid=" + username);
            } else if (envName.equalsIgnoreCase("cmpassword")) {
                password = env.get(envName);
                if (cmdebug.equalsIgnoreCase("1"))
                    System.out.println("cmpassword=" + password);
            } else if (envName.equalsIgnoreCase("cmserver")) {
                cmipaddress = env.get(envName);
                if (cmdebug.equalsIgnoreCase("1"))
                    System.out.println("cmserver=" + cmipaddress);
            } else if (envName.equalsIgnoreCase("cmkey")) {
                key = env.get(envName);
                if (cmdebug.equalsIgnoreCase("1"))
                    System.out.println("cmkey=" + key);
            } else if (envName.equalsIgnoreCase("cmdataformat")) {
                dataformat = env.get(envName);
                if (cmdebug.equalsIgnoreCase("1"))
                    System.out.println("cmdataformat=" + dataformat);
            } else if (envName.equalsIgnoreCase("cmdebug")) {
                cmdebug = env.get(envName);
                if (cmdebug.equalsIgnoreCase("1"))
                    System.out.println("cmdebug=" + cmdebug);
            }
            if (cmdebug.equalsIgnoreCase("1"))
                System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }

    private String poststream(String url) throws IOException {
        RequestBody body = RequestBody.create("", JSONOCTET);
        Request request = new Request.Builder().url(url).post(body).addHeader("Authorization", "Bearer " + this.token)
                .addHeader("Accept", "application/json").addHeader("Content-Type", "application/octet-stream").build();

//        logger.info("REQUEST SECRET " + request.toString() + body);
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String returnvalue = response.body().string();
//            logger.info("RESPONSE SECRET " + response.toString() + returnvalue );
            return returnvalue;
        }
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTokenFromRefreshToken(String refreshtoken) throws IOException {
        OkHttpClient client = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String grant_typetag = "{\"grant_type\":";
        String grant_type = "refresh_token";
        String refreshtokentag = "\"refresh_token\":";

        String authcall = grant_typetag + quote + grant_type + quote + comma + refreshtokentag + quote
                + refreshtoken + quote + " }";

        RequestBody body = RequestBody.create(authcall, mediaType);
        Request request = new Request.Builder().url("https://" + this.cmipaddress + "/api/v1/auth/tokens")
                .method("POST", body).addHeader("Content-Type", "application/json").build();

//        logger.info("REQUEST TOKEN " + request.toString() + authcall);

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String returnvalue = response.body().string();

//        logger.info("RESPONSE TOKEN " + response.toString() + returnvalue );
        return JsonPath.read(returnvalue, "$.jwt").toString();
    }

    public String cmRESTSecret(String name, String refreshtoken) throws Exception {
        return secret(name, refreshtoken);
    }

    private String secret(String name, String refreshtoken) throws Exception {
        String returnvalue = null;
        String results;
        this.token = getTokenFromRefreshToken(refreshtoken);

        results = this.poststream(buildSecretURL(name));

        try {
            results = JsonPath.read(results, "$.material").toString();
//            logger.info("SECRET " + results );
            returnvalue = results;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String code = JsonPath.read(results, "$.code").toString();
            String msg = JsonPath.read(results, "$.codeDesc").toString();
            System.out.println("code " + code);
            System.out.println("code desc  " + msg);
            StackTraceElement[] ste = e.getStackTrace();
            for (StackTraceElement stackTraceElement : ste) {
                System.out.println(stackTraceElement);
            }
            System.exit(-1);
        }
        return returnvalue;
    }

    private String buildSecretURL(String id) {
        String url;
        url = "https://" + this.cmipaddress + "/api/v1/vault/secrets/" + id + "/export?type=name";
        return url;
    }
}
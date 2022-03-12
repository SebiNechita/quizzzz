package client.utils;

import client.Main;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Objects;

public class HTTPSUtil {

    private static String currentURL = "";
    private static SSLContext sslContext;

    /**
     * Used to create a new SSL context, when the URL changes or the SSL context doesn't yet exist
     *
     * @return The new SSL context
     */
    private static SSLContext initializeSSLContext() {
        // This line allows localhost to connect as localhost technically doesn't have a domain which can be certified.
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));

        try {
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(HTTPSUtil.class.getResourceAsStream("/servercert.crt"));

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server", certificate);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            currentURL = Main.URL;
            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Can be used to retrieve the SSL context which in turn can be used for HTTPS requests
     *
     * @return The SSL Context
     */
    public static SSLContext getSSLContext() {
        if (!Objects.equals(currentURL, Main.URL)) {
            return initializeSSLContext();
        }

        return sslContext;
    }
}

package client.utils;

import client.Main;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Objects;

public class HTTPSUtil {

    private static String currentURL = "";
    private static SSLContext sslContext;

    private static SSLContext initializeSSLContext() {
        // This line allows localhost to connect as localhost technically doesn't have a domain which can be certified.
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));

        try {
            File crtFile = new File("src/main/resources/servercert.crt");
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(crtFile));

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

    public static SSLContext getSSLContext() {
        if (!Objects.equals(currentURL, Main.URL)) {
            return initializeSSLContext();
        }

        return sslContext;
    }
}

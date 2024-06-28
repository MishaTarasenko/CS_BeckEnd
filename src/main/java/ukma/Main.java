package ukma;

import com.sun.net.httpserver.*;
import ukma.controller.LoginController;
import ukma.controller.ProductCategoryController;
import ukma.controller.ProductController;
import ukma.filters.CORSFilter;
import ukma.util.Auth;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        char[] password = "12345678".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("keystore.jks");
        ks.load(fis, password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        HttpsServer server = HttpsServer.create(new InetSocketAddress(8080), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                params.setSSLParameters(sslContext.getDefaultSSLParameters());
            }
        });

        HttpContext authContext = server.createContext("/api/login", new LoginController());
        authContext.getFilters().add(new CORSFilter());

        HttpContext categoryContext = server.createContext("/api/category", new ProductCategoryController());
        categoryContext.setAuthenticator(new Auth());
        categoryContext.getFilters().add(new CORSFilter());

        HttpContext productContext = server.createContext("/api/product", new ProductController());
        productContext.setAuthenticator(new Auth());
        productContext.getFilters().add(new CORSFilter());

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }
}

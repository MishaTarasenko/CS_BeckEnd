package ukma;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import ukma.controller.LoginController;
import ukma.controller.ProductCategoryController;
import ukma.controller.ProductController;
import ukma.util.Auth;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        HttpContext authContext = server.createContext("/api/login", new LoginController());
        HttpContext categoryContext = server.createContext("/api/category", new ProductCategoryController());
        categoryContext.setAuthenticator(new Auth());
        HttpContext productContext = server.createContext("/api/product", new ProductController());
        productContext.setAuthenticator(new Auth());

        server.setExecutor(null);
        server.start();
    }
}
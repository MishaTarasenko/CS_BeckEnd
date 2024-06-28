package ukma.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class CORSFilter extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        // Set CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

        // If the request method is OPTIONS, respond with 204 and return
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Continue with the next handler in the chain
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "Adds CORS headers to the response";
    }
}


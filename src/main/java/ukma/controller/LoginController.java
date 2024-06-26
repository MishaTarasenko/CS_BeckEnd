package ukma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ukma.model.entity.Credentials;
import ukma.model.entity.UserEntity;
import ukma.repository.UserRepository;
import ukma.services.TokenService;
import ukma.util.Handler;

import javax.persistence.Persistence;
import java.io.InputStream;

public class LoginController extends Handler implements HttpHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TokenService service = new TokenService();
    private static final UserRepository repository = new UserRepository(Persistence.createEntityManagerFactory("CS_BeckEnd"));


    @Override
    public void handle(HttpExchange exchange) {
        try {
            InputStream in = exchange.getRequestBody();
            Credentials credentials = mapper.readValue(in.readAllBytes(), Credentials.class);
            UserEntity user;
            try {
                user = repository.findByEmail(credentials.getEmail());
            } catch (Exception e) {
                sendError(exchange, 401, "User not found, not valid credentials!");
                return;
            }
            if (!credentials.getPassword().equals(user.getPassword()))
                sendError(exchange, 401, "Password not correct!");
            sendResponse(exchange, 200, mapper.writeValueAsString(service.generateAccessToken(user)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                sendError(exchange, 500, "Internal Server Error");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

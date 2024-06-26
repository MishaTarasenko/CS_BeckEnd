package ukma.util;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import ukma.model.entity.UserEntity;
import ukma.repository.UserRepository;
import ukma.services.TokenService;

import javax.persistence.Persistence;


public class Auth extends Authenticator {
    private final static TokenService service = new TokenService();
    private final static UserRepository repository = new UserRepository(Persistence.createEntityManagerFactory("CS_BeckEnd"));

    @Override
    public Result authenticate(HttpExchange exch) {
        String authorization = exch.getRequestHeaders().getFirst("Authorization");

        if (authorization == null) {
            return new Failure(403);
        }
        String email = service.validateToken(authorization);
        UserEntity user;
        try {
            user = repository.findByEmail(email);
        } catch (Exception e) {
            return new Failure(403);
        }

        return new Success(new HttpPrincipal(user.getEmail(), user.getPassword()));
    }
}
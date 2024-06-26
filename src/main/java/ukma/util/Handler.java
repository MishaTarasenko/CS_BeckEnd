package ukma.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;

public class Handler {

    protected ObjectMapper mapper = new ObjectMapper();

    protected void sendResponse(HttpExchange httpExchange, int status, String resp) throws Exception {
        byte[] response = resp.getBytes();
        httpExchange.sendResponseHeaders(status, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }

    protected void sendError(HttpExchange httpExchange, int status, String msg) throws Exception {
        String error = mapper.writeValueAsString(msg);
        byte[] res = error.getBytes();
        httpExchange.sendResponseHeaders(status, res.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(res);
        os.close();
    }
}

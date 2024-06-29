package ukma.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ukma.model.entity.ProductEntity;
import ukma.model.view.ProductView;
import ukma.services.product.ProductService;
import ukma.util.Handler;

import java.util.HashMap;
import java.util.List;

public class ProductController extends Handler implements HttpHandler {


    private ProductService service = new ProductService();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);

            switch (method) {
                case "GET":
                    if (path.contains("all")) getAll(exchange);
                    else get(exchange, id);
                    break;
                case "POST":
                    if (path.contains("all/criteria")) getAllByCriteria(exchange);
                    else post(exchange, id);
                    break;
                case "PUT":
                    put(exchange);
                    break;
                case "DELETE":
                    delete(exchange, id);
                    break;
                default:
                    sendError(exchange, 404, "Method not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                sendError(exchange, 500, "Internal Server Error");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void get(HttpExchange exchange, String id) throws Exception {
        try {
            ProductEntity product = get(Integer.parseInt(id));
            if (product == null) {
                sendError(exchange, 404, "Product with id " + id + " not found");
                return;
            }
            String response = mapper.writeValueAsString(product);
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void getAll(HttpExchange exchange) throws Exception {
        try {
            List<ProductEntity> products = null;
            products = service.getAll();
            String response = mapper.writeValueAsString(products);
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void getAllByCriteria(HttpExchange exchange) throws Exception {
        try {
            byte[] reqBody = exchange.getRequestBody().readAllBytes();
            if(reqBody == null) sendError(exchange, 400, "Bad Request");
            HashMap<String, Object> criteria;
            try {
                criteria = mapper.readValue(reqBody, HashMap.class);
            } catch (Exception e) {
                sendError(exchange, 409, e.getMessage());
                return;
            }
            List<ProductEntity> products = null;
            products = service.getAllByCriteria(criteria);
            String response = mapper.writeValueAsString(products);
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void put(HttpExchange exchange) throws Exception {
        try {
            byte[] reqBody = exchange.getRequestBody().readAllBytes();
            if(reqBody == null) sendError(exchange, 400, "Bad Request");
            ProductView product = map(reqBody);
            if (product == null) {
                sendError(exchange, 409, "Bad Request");
                return;
            }
            Integer id = null;
            try {
                id = service.create(product);
            } catch (Exception e) {
                sendError(exchange, 401, e.getMessage());
            }
            if (id == null) sendError(exchange, 409, "Error creating product");
            String response = mapper.writeValueAsString(id);
            sendResponse(exchange, 201, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void post(HttpExchange exchange, String id) throws Exception {
        try {
            byte[] reqBody = exchange.getRequestBody().readAllBytes();
            if(reqBody == null) sendError(exchange, 400, "Bad Request");
            Integer productId = Integer.parseInt(id);
            if (!isProductExists(productId)) {
                sendError(exchange, 404, "Product with id " + id + " not found");
                return;
            }
            ProductView updatedProduct = map(reqBody);
            if (updatedProduct == null) {
                sendError(exchange, 409, "Bad Request");
                return;
            }
            updatedProduct.setId(productId);
            boolean resp = false;
            try {
                resp = service.update(updatedProduct);
            } catch (Exception e) {
                sendError(exchange, 401, e.getMessage());
                return;
            }
            if (resp)
                sendResponse(exchange, 204, "");
            else
                sendError(exchange, 409, "Error updating product");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void delete(HttpExchange exchange, String id) throws Exception {
        try {
            Integer productId = Integer.parseInt(id);
            if (!isProductExists(productId)) {
                sendError(exchange, 404, "Product with id " + id + " not found");
                return;
            }
            boolean resp = service.delete(productId);
            if (resp)
                sendResponse(exchange, 204, "");
            else
                sendError(exchange, 500, "Internal Server Error");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private ProductEntity get(Integer id) throws Exception {
        try {
            return service.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isProductExists(Integer id) {
        try {
            service.getById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ProductView map(byte[] reqBody) throws Exception {
        try {
            return mapper.readValue(reqBody, ProductView.class);
        } catch (Exception e) {
            return null;
        }
    }
}

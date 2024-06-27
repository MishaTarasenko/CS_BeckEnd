package ukma.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ukma.model.entity.ProductCategoryEntity;
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
                    if (path.contains("all/criteria")) getAllByCriteria(exchange);
                    else if (path.contains("all")) getAll(exchange);
                    else get(exchange, id);
                    break;
                case "POST":
                    post(exchange, id);
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
            ProductEntity product = null;
            try {
                product = service.getById(Integer.parseInt(id));
            } catch (Exception e) {
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
            ProductView product = null;
            try {
                product = mapper.readValue(reqBody, ProductView.class);
                System.out.println(product.toString());
            } catch (Exception e) {
                sendError(exchange, 409, e.getMessage());
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
            try {
                service.getById(Integer.parseInt(id));
            } catch (Exception e) {
                sendError(exchange, 404, "Product with id " + id + " not found");
                return;
            }
            ProductView updatedProduct = null;
            try {
                updatedProduct = mapper.readValue(reqBody, ProductView.class);
            } catch (Exception e) {
                sendError(exchange, 409, e.getMessage());
            }
            updatedProduct.setId(productId);
            boolean resp = false;
            try {
                resp = service.update(updatedProduct);
            } catch (Exception e) {
                sendError(exchange, 401, e.getMessage());
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
            try {
                service.getById(Integer.parseInt(id));
            } catch (Exception e) {
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
}

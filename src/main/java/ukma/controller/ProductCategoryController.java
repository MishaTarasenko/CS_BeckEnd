package ukma.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ukma.model.entity.ProductCategoryEntity;
import ukma.services.category.ProductCategoryService;
import ukma.util.Handler;

import java.io.DataInput;
import java.util.HashMap;
import java.util.List;

public class ProductCategoryController extends Handler implements HttpHandler {

    private ProductCategoryService service = new ProductCategoryService();

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
            ProductCategoryEntity category = null;
            try {
                category = service.getById(Integer.parseInt(id));
            } catch (Exception e) {
                sendError(exchange, 404, "Category with id " + id + " not found");
                return;
            }
            String response = mapper.writeValueAsString(category);
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void getAll(HttpExchange exchange) throws Exception {
        try {
            List<ProductCategoryEntity> category = null;
            category = service.getAll();
            String response = mapper.writeValueAsString(category);
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
            ProductCategoryEntity category = null;
            try {
                category = mapper.readValue(reqBody, ProductCategoryEntity.class);
                System.out.println(category.toString());
            } catch (Exception e) {
                sendError(exchange, 409, e.getMessage());
                return;
            }
            Integer id = null;
            try {
                id = service.create(category);
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
            Integer categoryId = Integer.parseInt(id);
            try {
                service.getById(Integer.parseInt(id));
            } catch (Exception e) {
                sendError(exchange, 404, "Category with id " + id + " not found");
                return;
            }
            ProductCategoryEntity updatedCategory = null;
            try {
                updatedCategory = mapper.readValue(reqBody, ProductCategoryEntity.class);
            } catch (Exception e) {
                sendError(exchange, 409, e.getMessage());
            }
            updatedCategory.setId(categoryId);
            boolean resp = false;
            try {
                resp = service.update(updatedCategory);
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
            Integer categoryId = Integer.parseInt(id);
            try {
                service.getById(Integer.parseInt(id));
            } catch (Exception e) {
                sendError(exchange, 404, "Category with id " + id + " not found");
                return;
            }
            boolean resp = service.delete(categoryId);
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

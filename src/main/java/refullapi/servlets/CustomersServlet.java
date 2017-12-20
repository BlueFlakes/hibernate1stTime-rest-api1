package refullapi.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import refullapi.hibernate.DaoPool;
import refullapi.models.Customer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomersServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String jsonToString;

        try {

            if (path.equals("/customers")) {
                jsonToString = getJsonStringFromList(mapper);
            }
            else {
                jsonToString = getJsonStringFromObject(mapper, path);
            }

            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().write(jsonToString);

        } catch (Exception e) {
            resp.setStatus(404);
        }
    }

    private String getJsonStringFromList(ObjectMapper mapper) throws JsonProcessingException {
        List<Customer> customers = DaoPool.customerDao.getAll(Customer.class);
        return mapper.writeValueAsString(customers);
    }

    private String getJsonStringFromObject(ObjectMapper mapper, String path) throws JsonProcessingException {
        Integer id = Integer.parseInt(path.split("/")[2]);
        Customer customer = DaoPool.customerDao.get(Customer.class, id);
        return mapper.writeValueAsString(customer);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            DaoPool.customerDao.saveToDatabase(getCustomerFromRequest(req));
            resp.setStatus(201);
            resp.getWriter().write("create");

        } catch (InvalidFormatException e) {
            resp.setStatus(406);

        } catch (IOException e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            DaoPool.customerDao.remove(getCustomerFromRequest(req));
            resp.setStatus(201);
            resp.getWriter().write("remove");

        } catch (InvalidFormatException e) {
            resp.setStatus(406);

        } catch (IOException e) {
            resp.setStatus(400);
        }
    }
}

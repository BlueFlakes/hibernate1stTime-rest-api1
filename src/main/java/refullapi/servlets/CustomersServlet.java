package refullapi.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.hibernate.StaleStateException;
import refullapi.hibernate.DaoPool;
import refullapi.models.Customer;

import javax.persistence.OptimisticLockException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomersServlet extends HttpServlet {

    private static final int RESOURCE = 1;
    private static final int RESOURCE_WITH_ID = 2;
    private static final int RESOURCE_INDEX = 0;
    private static final int ID_INDEX = 1;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] dataFromURI = parseURItoList(req);
        String jsonToString;

        try {

            if (dataFromURI.length == RESOURCE && dataFromURI[RESOURCE_INDEX].equalsIgnoreCase("customers")) {
                jsonToString = getJsonStringFromList();
            }

            else if (dataFromURI.length == RESOURCE_WITH_ID) {
                jsonToString = getJsonStringFromObject(getIdFromURI(dataFromURI));

            } else {
                throw new IndexOutOfBoundsException();
            }

            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().write(jsonToString);

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            resp.setStatus(406);

        } catch (NullPointerException e) {
            resp.setStatus(404);

        } catch (IOException e) {
            resp.setStatus(400);
        }
    }

    private String getJsonStringFromList() throws JsonProcessingException {
        List<Customer> customers = DaoPool.customerDao.getAll(Customer.class);
        return mapper.writeValueAsString(customers);
    }

    private String getJsonStringFromObject(Integer id) throws JsonProcessingException, NullPointerException {
        Customer customer = DaoPool.customerDao.get(Customer.class, id);

        if (customer == null) {
            throw new NullPointerException();
        }

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
        String[] dataFromURI = parseURItoList(req);

        try {
            if (dataFromURI.length == RESOURCE_WITH_ID) {
                Integer id = getIdFromURI(dataFromURI);
                DaoPool.customerDao.remove(DaoPool.customerDao.get(Customer.class, id));
                resp.setStatus(200);
                resp.getWriter().write("remove");

            } else {
                throw new IndexOutOfBoundsException();
            }


        } catch (OptimisticLockException | StaleStateException e) {
            resp.setStatus(406);

        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            resp.setStatus(406);

        } catch (IOException e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] dataFromURI = parseURItoList(req);

        try {
            if (dataFromURI.length == RESOURCE_WITH_ID) {
                Integer id = getIdFromURI(dataFromURI);
                Customer customer = getCustomerFromRequest(req);
                customer.setId(id);
                DaoPool.customerDao.update(customer);
                resp.setStatus(201);
                resp.getWriter().write("update");

            } else {
                throw new IndexOutOfBoundsException();
            }

        } catch (OptimisticLockException | StaleStateException e) {
                resp.setStatus(406);

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            resp.setStatus(406);

        } catch (IOException e) {
            resp.setStatus(400);

        } catch (NullPointerException e) {
            resp.setStatus(404);
        }
    }

    private Customer getCustomerFromRequest(HttpServletRequest req) throws IOException {
        String requestStr = req.getReader().lines().collect(Collectors.joining());
        return mapper.readValue(requestStr, Customer.class);
    }

    private String[] parseURItoList(HttpServletRequest req) {
        String path = req.getRequestURI();
        Integer pathLength = path.length();
        String relativePath = path.substring(1, pathLength);

        return  relativePath.split("/");
    }

    private Integer getIdFromURI(String[] dataFromURI) throws NumberFormatException {
        return Integer.parseInt(dataFromURI[ID_INDEX]);
    }


}

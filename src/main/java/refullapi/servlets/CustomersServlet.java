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

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] dataFromURI = ServletsCommon.parseURItoList(req);
        String jsonToString;

        try {

            if (dataFromURI.length == ServletsCommon.RESOURCE && dataFromURI[ServletsCommon.RESOURCE_INDEX].equalsIgnoreCase("customers")) {
                jsonToString = getJsonStringFromList();
            }

            else if (dataFromURI.length == ServletsCommon.RESOURCE_WITH_ID) {
                jsonToString = getJsonStringFromObject(ServletsCommon.getIdFromURI(dataFromURI));

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
        String[] dataFromURI = ServletsCommon.parseURItoList(req);

        try {
            if (dataFromURI.length == ServletsCommon.RESOURCE && dataFromURI[ServletsCommon.RESOURCE_INDEX].equalsIgnoreCase("customers")) {
                DaoPool.customerDao.saveToDatabase(getCustomerFromRequest(req));
                resp.setStatus(201);
                resp.getWriter().write("create");
            } else {
                throw new IllegalArgumentException();
            }

        } catch (InvalidFormatException | IllegalArgumentException e ) {
            resp.setStatus(406);

        } catch (IOException e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String[] dataFromURI = ServletsCommon.parseURItoList(req);

        try {
            if (dataFromURI.length == ServletsCommon.RESOURCE_WITH_ID) {
                Integer id = ServletsCommon.getIdFromURI(dataFromURI);
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
        String[] dataFromURI = ServletsCommon.parseURItoList(req);

        try {
            if (dataFromURI.length == ServletsCommon.RESOURCE_WITH_ID) {
                Integer id = ServletsCommon.getIdFromURI(dataFromURI);
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

}

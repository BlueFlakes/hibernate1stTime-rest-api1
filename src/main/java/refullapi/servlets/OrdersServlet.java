package refullapi.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.hibernate.StaleStateException;
import refullapi.hibernate.DaoPool;
import refullapi.models.Order;

import javax.persistence.OptimisticLockException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String[] dataFromURI = ServletsCommon.parseURItoList(req);
        String jsonToString;

        try {
            if (dataFromURI.length == ServletsCommon.RESOURCE
                    && dataFromURI[ServletsCommon.RESOURCE_INDEX].equalsIgnoreCase("orders")) {
                jsonToString = getJsonStringFromArrayList(mapper);
            }
            else if ((dataFromURI.length == ServletsCommon.RESOURCE_WITH_ID)) {
                jsonToString = getJsonStringFromObject(mapper, path);
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

        } catch (Exception e) {
            resp.setStatus(404);
        }
    }

    private String getJsonStringFromArrayList(ObjectMapper mapper) throws JsonProcessingException {
        List<Order> orders = DaoPool.orderDao.getAll(Order.class);
        return mapper.writeValueAsString(orders);
    }

    private String getJsonStringFromObject(ObjectMapper mapper, String path) throws JsonProcessingException {
        Integer id = Integer.parseInt(path.split("/")[2]);
        Order order = DaoPool.orderDao.get(Order.class, id);
        return mapper.writeValueAsString(order);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            DaoPool.orderDao.saveToDatabase(getOrderFromRequest(req));
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
        String[] dataFromURI = ServletsCommon.parseURItoList(req);

        try {
            if (dataFromURI.length == ServletsCommon.RESOURCE_WITH_ID) {
                Integer id = ServletsCommon.getIdFromURI(dataFromURI);
                DaoPool.orderDao.remove(DaoPool.orderDao.get(Order.class, id));
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
                Order order = getOrderFromRequest(req);
                order.setId(id);
                DaoPool.orderDao.update(order);
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

    private Order getOrderFromRequest(HttpServletRequest req) throws IOException {
        String requestStr = req.getReader().lines().collect(Collectors.joining());
        return mapper.readValue(requestStr, Order.class);
    }
}

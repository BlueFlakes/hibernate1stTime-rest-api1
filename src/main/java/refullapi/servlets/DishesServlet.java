package refullapi.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.hibernate.StaleStateException;
import refullapi.hibernate.DaoPool;
import refullapi.models.Dish;

import javax.persistence.OptimisticLockException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DishesServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] dataFromURI = ServletsCommon.parseURItoList(req);
        String jsonToString;

        try {

            if (dataFromURI.length == ServletsCommon.RESOURCE && dataFromURI[ServletsCommon.RESOURCE_INDEX].equalsIgnoreCase("/dishes")) {
                jsonToString = getJsonStringFromArrayList();
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

    private String getJsonStringFromArrayList() throws JsonProcessingException {
        List<Dish> dishes = DaoPool.dishDao.getAll(Dish.class);
        return mapper.writeValueAsString(dishes);
    }

    private String getJsonStringFromObject(Integer id) throws JsonProcessingException {
        Dish dish = DaoPool.dishDao.get(Dish.class, id);

        if (dish == null) {
            throw new NullPointerException();
        }

        return mapper.writeValueAsString(dish);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            DaoPool.dishDao.saveToDatabase(getDishFromRequest(req));
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
                DaoPool.dishDao.remove(DaoPool.dishDao.get(Dish.class, id));
                resp.setStatus(201);
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
                Dish dish = getDishFromRequest(req);
                dish.setId(id);
                DaoPool.dishDao.update(dish);
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

    private Dish getDishFromRequest(HttpServletRequest req) throws IOException {
        String requestStr = req.getReader().lines().collect(Collectors.joining());
        return mapper.readValue(requestStr, Dish.class);
    }

}

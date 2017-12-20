package refullapi.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.core.util.IOUtils;
import refullapi.hibernate.DaoPool;
import refullapi.models.Dish;

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
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String jsonToString;

        try {

            if (path.equals("/dishes")) {
                jsonToString = getJsonStringFromArrayList(mapper);
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

    private String getJsonStringFromArrayList(ObjectMapper mapper) throws JsonProcessingException {
        List<Dish> dishes = DaoPool.dishDao.getAll(Dish.class);
        return mapper.writeValueAsString(dishes);
    }

    private String getJsonStringFromObject(ObjectMapper mapper, String path) throws JsonProcessingException {
        Integer id = Integer.parseInt(path.split("/")[2]);
        Dish dish = DaoPool.dishDao.get(Dish.class, id);
        return mapper.writeValueAsString(dish);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String requestStr = req.getReader().lines().collect(Collectors.joining());
            Dish dish = mapper.readValue(requestStr, Dish.class);
            DaoPool.dishDao.saveToDatabase(dish);
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
            String requestStr = req.getReader().lines().collect(Collectors.joining());
            Dish dish = mapper.readValue(requestStr, Dish.class);
            DaoPool.dishDao.remove(dish);
            resp.setStatus(201);
            resp.getWriter().write("remove");

        } catch (InvalidFormatException e) {
            resp.setStatus(406);

        } catch (IOException e) {
            resp.setStatus(400);
        }
    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//    }
}

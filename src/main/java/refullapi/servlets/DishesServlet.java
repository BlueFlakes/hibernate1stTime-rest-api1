package refullapi.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import refullapi.hibernate.DaoPool;
import refullapi.models.Dish;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DishesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        ObjectMapper mapper = new ObjectMapper();
        String jsonToString;

        try {

            if (path.equals("/dishes")) {
                jsonToString = getJsonStringFromArrayList(mapper);
            }
            else {
                jsonToString = getJsonStringFromObject(mapper, path);
            }

            resp.setContentType("application/json");
            resp.getWriter().write(jsonToString);

        } catch (Exception e) {
            resp.setStatus(404);
            resp.getWriter().println("Fail: " + resp.getStatus());
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
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        Double price = Double.parseDouble(req.getParameter("price"));
//        Dish dish = Dish.builder().name(name)
//                                  .price(price)
//                                  .build();
//        dishDao.insert(dish);
//        resp.setStatus(201);
//        resp.getWriter().write("create");
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//    }
}

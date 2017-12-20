package refullapi.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomersServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String jsonToString;

        try {

            if (path.equals("/customers")) {
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
}

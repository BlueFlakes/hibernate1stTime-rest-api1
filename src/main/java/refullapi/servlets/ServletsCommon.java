package refullapi.servlets;

import javax.servlet.http.HttpServletRequest;

public class ServletsCommon {
    public static final int RESOURCE = 1;
    public static final int RESOURCE_WITH_ID = 2;
    public static final int RESOURCE_INDEX = 0;
    public static final int ID_INDEX = 1;

    public static Integer getIdFromURI(String[] dataFromURI) throws NumberFormatException {
        return Integer.parseInt(dataFromURI[ID_INDEX]);
    }

    public static String[] parseURItoList(HttpServletRequest req) {
        String path = req.getRequestURI();
        Integer pathLength = path.length();
        String relativePath = path.substring(1, pathLength);

        return  relativePath.split("/");
    }
}

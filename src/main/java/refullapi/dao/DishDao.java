package refullapi.dao;

import refullapi.exceptions.DAOException;
import refullapi.models.Dish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DishDao {

    public ArrayList<Dish> getAll() {
        ArrayList<Dish> dishes = new ArrayList<>();

        try {
            Connection connection = DBConnection.getConnection();
            String sql = "SELECT * FROM dishes";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                Dish dish = new Dish(id, name, price);
                dishes.add(dish);
            }

        } catch (DAOException | SQLException e) {
// send 404?
            e.printStackTrace();
        }

        return dishes;
    }

    public Dish getDish(Integer id) {
        Dish dish = null;

        try {
            Connection connection = DBConnection.getConnection();
            String sql = "SELECT * FROM dishes WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                dish = new Dish(id, name, price);
            }

            } catch (DAOException | SQLException e) {
// send 404?
                e.printStackTrace();
        }
        return dish;
    }

    public void insert(Dish dish) {

        try {
            Connection connection = DBConnection.getConnection();
            String sql = "INSERT INTO dishes (name, price) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());

            ps.executeUpdate();

        } catch (DAOException | SQLException e) {
// send 404?
            e.printStackTrace();
        }
    }

    public void update(Dish dish) {
        try {
            Connection connection = DBConnection.getConnection();
            String sql = "UPDATE dishes SET name = ?, price = ? WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());
            ps.executeUpdate();

        } catch (DAOException | SQLException e) {
// send 404?
            e.printStackTrace();
        }
    }

    public void remove(Dish dish) {
        try {
            Connection connection = DBConnection.getConnection();
            String sql = "DELETE FROM dishes WHERE id = (?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, dish.getId());
            ps.executeUpdate();

        } catch (DAOException | SQLException e) {
// send 404?
            e.printStackTrace();
        }
    }
}

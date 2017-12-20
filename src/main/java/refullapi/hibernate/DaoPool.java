package refullapi.hibernate;

import lombok.Getter;
import refullapi.models.Customer;
import refullapi.models.Dish;
import refullapi.models.Order;

@Getter
public class DaoPool {
    public final static HibernateDao<Order> orderDao = new HibernateDao<>();
    public final static HibernateDao<Customer> customerDao = new HibernateDao<>();
    public final static HibernateDao<Dish> dishDao = new HibernateDao<>();
}

package refullapi.hibernate;


import org.hibernate.*;
import refullapi.models.Customer;
import refullapi.models.Dish;
import refullapi.models.Order;

public class HibernateTest {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        session.getTransaction().begin();

        Customer customer = Customer.builder().name("pp111111111pppppme")
                                              .surname("xooooooooooooood")
                                              .email("email@")
                                              .build();
        Dish dish = Dish.builder().name("12331313").price(1.332).build();
        Order order = Order.builder().dishId(1).clientId(1).amount(1223432311).build();
        session.save(order);
        session.getTransaction().commit();
        session.getTransaction().begin();
        Customer custome3 = session.get(Customer.class, 1);
        session.getTransaction().commit();
        System.out.println(custome3);

        session.close();

    }

}

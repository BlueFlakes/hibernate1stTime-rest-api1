package refullapi.models;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "orders", schema = "public", catalog = "canteen__database")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer amount;

    @OneToOne
    private Customer clientId;

    @OneToOne
    private Dish dishId;

    public Order() {}

    private Order(OrderBuilder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.clientId = builder.clientId;
        this.dishId = builder.dishId;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Integer id;
        private Integer amount;
        private Customer clientId;
        private Dish dishId;

        private OrderBuilder() {}

        public OrderBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public OrderBuilder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public OrderBuilder clientId(Customer clientId) {
            this.clientId = clientId;
            return this;
        }

        public OrderBuilder dishId(Dish dishId) {
            this.dishId = dishId;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
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

    @Column(name="client_id")
    private Integer clientId;

    @Column(name="dish_id")
    private Integer dishId;

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
        private Integer clientId;
        private Integer dishId;

        private OrderBuilder() {}

        public OrderBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public OrderBuilder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public OrderBuilder clientId(Integer clientId) {
            this.clientId = clientId;
            return this;
        }

        public OrderBuilder dishId(Integer dishId) {
            this.dishId = dishId;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}

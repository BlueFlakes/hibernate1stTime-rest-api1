package refullapi.models;

import lombok.Getter;

@Getter
public class Order {
    private Integer id;
    private Integer amount;

    public Order() {}

    public Order(Integer id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }
}

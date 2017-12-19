package refullapi.models;

import lombok.Getter;

@Getter
public class Dish {
    private Integer id;
    private String name;
    private Double price;

    public Dish() {}

    public Dish(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

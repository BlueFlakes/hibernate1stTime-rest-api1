package refullapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "dishes", schema = "public", catalog = "canteen__database")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    private String name;
    private Double price;

    public Dish() {}

    private Dish(DishBuilder dishBuilder) {
        this.id = dishBuilder.id;
        this.name = dishBuilder.name;
        this.price = dishBuilder.price;
    }

    public static DishBuilder builder() {
        return new DishBuilder();
    }

    public static class DishBuilder {
        private Integer id;
        private String name;
        private Double price;

        private DishBuilder() {}

        public DishBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public DishBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DishBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public Dish build() {
            return new Dish(this);
        }
    }
}

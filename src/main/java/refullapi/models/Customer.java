package refullapi.models;

import lombok.Getter;

@Getter
public class Customer {
    private Integer id;
    private String name;
    private String surname;
    private String email;

    public Customer() {}

    public Customer(Integer id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}

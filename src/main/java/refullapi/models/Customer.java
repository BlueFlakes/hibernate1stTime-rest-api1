package refullapi.models;

import lombok.Getter;

@Getter
public class XD {
    private Integer id;
    private String name;
    private String surname;
    private String email;

    private XD(CustomerBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.email = builder.email;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {
        private Integer id;
        private String name;
        private String surname;
        private String email;

        private CustomerBuilder() {}

        public CustomerBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public XD build() {
            return new XD(this);
        }
    }
}

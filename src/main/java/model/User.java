package model;

import lombok.Data;
import java.util.Date;

@Data
public class User {

    private int id;
    private String email;
    private  String login;
    private String name;
    private Date birthday;


    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public static class UserBuilder {
        private final User user;

        public UserBuilder() {
            user = new User();
        }

        public UserBuilder id(int id) {
            user.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            user.email = email;
            return this;
        }

        public UserBuilder login(String login) {
            user.login = login;
            return this;
        }

        public UserBuilder name(String name) {
            user.name = name;
            return this;
        }

        public UserBuilder birthday(Date birthday) {
            user.birthday = birthday;
            return this;
        }

        public User build() {
            return user;
        }
    }
}

package project.model;

import java.io.Serializable;

public class LoginForm implements Serializable {

    private String name;

    private String password;

    public LoginForm(String name,String password){
        this.name=name;
        this.password = password;
    }
    public LoginForm(){

    }

    LoginForm(LoginForm loginForm){
        this.name = loginForm.getName();
        this.password = loginForm.getPassword();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
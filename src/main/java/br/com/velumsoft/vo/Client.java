package br.com.velumsoft.vo;

public class Client {
    
    private String name;
    private String surname;
    
    public Client() {
        super();
    }
    
    public Client(String name, String surname) {
        super();
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public boolean validate(){
        return name != null && ! name.isEmpty() && surname != null && ! surname.isEmpty();
    }
    
    public String toString(){
        return String.format("client: %s %s", name, surname);
    }
}

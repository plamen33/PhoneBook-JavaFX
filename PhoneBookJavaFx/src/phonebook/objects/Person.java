package phonebook.objects;

public class Person {

    private String names;
    private String phone;

    public Person(String names, String phone) {
        this.names = names;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }



}

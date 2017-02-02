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

    // I do this in order to set Person data to String and then when I click record and click on add button - it will show in console the selected record
    @Override
    public String toString() {
        return "Person{" +
                "names='" + names + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}

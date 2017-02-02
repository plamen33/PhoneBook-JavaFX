package phonebook.objects;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    // initialized the 2 columns in the table
    private SimpleStringProperty names = new SimpleStringProperty("");
    private SimpleStringProperty phone = new SimpleStringProperty("");

    public Person(){

    }

    public Person(String names, String phone) {
        this.names = new SimpleStringProperty(names);
        this.phone = new SimpleStringProperty(phone);
    }

    public String getNames() {
        return names.get();
    }

    public void setNames(String names) {
        this.names.set(names);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }


    public SimpleStringProperty namesProperty(){
        return names;
    }
    public SimpleStringProperty phoneProperty(){
        return phone;
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


package phonebook.interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import phonebook.interfaces.PhoneBook;
import phonebook.objects.Person;


public class CollectionPhoneBook implements PhoneBook {

    private ObservableList<Person> personList = FXCollections.observableArrayList();
    @Override
    public void add(Person person) {
        personList.add(person);
    }
    @Override
    public void update(Person person){
        // here we won't do anything, as the data is stored in the collection
        // we would use this method if we store the data in a file or in DB
    }

    @Override
    public void delete(Person person){
        personList.remove(person);
    }
    public ObservableList<Person> getPersonList() {
        return personList;
    }

    public void print(){
        int number = 0;
        System.out.println();
        for(Person person: personList){
            number++;
            System.out.println(number + "names =" + person.getNames() +"; phone = " + person.getPhone());
        }
    }

    public void fillTestData(){
        personList.add(new Person("Michael", "333784723"));
        personList.add(new Person("Peter", "23948723948"));
        personList.add(new Person("Filip", "23948723948"));
        personList.add(new Person("Ioan", "23948723948"));
        personList.add(new Person("Donald", "17739843043"));
        personList.add(new Person("Kiril", "37739823043"));
        personList.add(new Person("Vladimir", "77739843043"));
    }

}

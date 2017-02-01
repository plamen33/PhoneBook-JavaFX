package phonebook.interfaces;

import phonebook.objects.Person;

public interface PhoneBook {

    void add(Person person);

    void update(Person person);

    void delete(Person person);
}

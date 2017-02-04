package phonebook.interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import phonebook.objects.Person;
import phonebook.interfaces.PhoneBook;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


public class ContactData implements PhoneBook {

    private static final String CONTACTS_FILE = "contacts.xml";

    private static final String CONTACT = "contact";
    private static final String NAMES = "names";
    private static final String PHONE = "phone";


    private ObservableList<Person> contacts;

    // *** initialize the contacts list here ***
    public ContactData() {
        contacts = FXCollections.observableArrayList();
    }


    public ObservableList<Person> getContacts() {
        return contacts;
    }
    // *** Add methods to add/delete/access contacts here ***
    public void add(Person person) {
        contacts.add(person);
    }

    public void update(Person person){
        // here we won't do anything, as the data is stored in the collection
        // we would use this method if we store the data in a file or in DB
    }
    public void delete(Person person) {
        contacts.remove(person);
    }

    public void loadContacts() {
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(CONTACTS_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Person contact = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a contact item, we create a new contact
                    if (startElement.getName().getLocalPart().equals(CONTACT)) {
                        contact = new Person();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(NAMES)) {
                            event = eventReader.nextEvent();
                            contact.setNames(event.asCharacters().getData());
                            continue;
                        }
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PHONE)) {
                        event = eventReader.nextEvent();
                        contact.setPhone(event.asCharacters().getData());
                        continue;
                    }

                }

                // If we reach the end of a contact element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(CONTACT)) {
                        contacts.add(contact);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void saveContacts() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(CONTACTS_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement contactsStartElement = eventFactory.createStartElement("",
                    "", "contacts");
            eventWriter.add(contactsStartElement);
            eventWriter.add(end);

            for (Person contact: contacts) {
                saveContact(eventWriter, eventFactory, contact);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "contacts"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Problem with Contacts file: " + e.getMessage());
            e.printStackTrace();
        }
        catch (XMLStreamException e) {
            System.out.println("Problem writing contact: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveContact(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Person contact)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create contact open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", CONTACT);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, NAMES, contact.getNames());
        createNode(eventWriter, PHONE, contact.getPhone());


        eventWriter.add(eventFactory.createEndElement("", "", CONTACT));
        eventWriter.add(end);
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

//    public void fillTestData(){
//        contacts.add(new Person("Michael", "333784723"));
//        contacts.add(new Person("Peter", "23948723948"));
//        contacts.add(new Person("Filip", "23948723948"));
//        contacts.add(new Person("Ioan", "23948723948"));
//        contacts.add(new Person("Donald", "17739843043"));
//        contacts.add(new Person("Kiril", "37739823043"));
//        contacts.add(new Person("Vladimir", "77739843043"));
//    }

}

package table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import register.Person;

import java.util.ArrayList;
import java.util.Arrays;

public class BuildTable {
    private static ObservableList<Person> obsList_Person = FXCollections.observableArrayList();
    private static ObservableList<String> filterCategories = FXCollections.observableArrayList();
    private static String filterCategory;
    private static boolean listLoaded = false;

    // ADDS A PERSON OBJECT TO THE OBSERVABLE LIST
    public static void addDataToObedList(Person newPerson){
        obsList_Person.add(newPerson);
    }

    // TAKES ALL VALUES OF THE OBSERVABLE LIST AND SETS IT AS THE TABLEVIEW ITEMS
    public static void insertDataToTable(TableView<Person> tableView){
        tableView.setItems(obsList_Person);
    }

    // REMOVES ALL PERSON OBJECTS FROM THE OBSERVABLE LIST
    public static void removeDataOnTable(){
        obsList_Person.clear();
    }

    // REMOVES ONE PERSON OBJECT FROM THE OBSERVABLE LIST
    public static void removeDataOnTable(Person data){
        ArrayList<Person> list = new ArrayList<>(BuildTable.getObsList_Person());
        for(Person person:list){
            if(person.equals(data)){
                BuildTable.getObsList_Person().remove(data);
            }
            if(data == null){
                throw new IllegalArgumentException("Please choose a record to delete");
            }
        }
    }

    // ENABLES THE USER TO FILTER TABLE BY FNAME, LNAME, ETC.
    public static void filterCatOnChange(ComboBox<String> filterOptions) {
        // PREVENTS THE COMBOBOX FROM HAVING DUPLICATES
        if(!listLoaded){
            String[] filterCats = {"First Name", "Last Name", "Email", "Phone", "Birth Date", "Country", "Country-code", "Age"};
            filterCategories.addAll(Arrays.asList(filterCats));
            listLoaded = true;
        }

        filterOptions.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            filterCategory = newValue;
        });
    }

    // THIS METHOD FILTERS THE TABLE USING THE INPUT VALUE FROM A TEXTFIELD
    public static void filterDataOnTable(TableView<Person> tableView, TextField filterTextField){
        FilteredList<Person> filteredList = new FilteredList<>(obsList_Person, person -> true);
        // KEEPS TRACK OF THE FILTER TEXTFIELD VALUE
        filterTextField.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate((person)->{
                // PERSON OBJECT ATTRIBUTES
                String fname = person.getFname().toLowerCase();
                String lname = person.getLname().toLowerCase();
                String email = person.getEmail().toLowerCase();
                String phone = person.getPhone();
                String birthdate = person.getBirthdate();
                String countryName = person.getCountryName().toLowerCase();
                String countryCode = person.getCountryCode();
                String age = Integer.toString(person.getAge());
                String filter = newValue.toLowerCase();

                // IF ANY ATTRIBUTE VALUE OF THE PERSON OBJECT CONTAINS THE FILTER, IT SETS THE PREDICATE TO TRUE
                // THAT PERSON OBJECT IS THEN ADDED TO THE FILTERED LIST
                // IF NONE OF THE ATTRIBUTE VALUES CONTAINS THE FILTER, THE FILTERED LIST IS EMPTY
                switch (filterCategory) {
                    case "First Name": if(fname.contains(filter)){ return true; } break;
                    case "Last Name": if(lname.contains(filter)){ return true; } break;
                    case "Email": if(email.contains(filter)){ return true; } break;
                    case "Phone": if(phone.contains(filter)){ return true; } break;
                    case "Birth Date": if(birthdate.contains(filter)){ return true; } break;
                    case "Country": if(countryName.contains(filter)){ return true; } break;
                    case "Country-code": if(countryCode.contains(filter)){ return true; } break;
                    case "Age": if(age.contains(filter)){ return true; } break;
                }
                return newValue.isEmpty() || newValue.isBlank();
            });
        });

        // THIS CONVERTS THE THE FILTERED LIST INTO A SORTED LIST
        SortedList<Person> sortedList = new SortedList<>(filteredList);
        // THIS SORTS THE SORTED LIST ALPHABETICALLY
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        // THIS DISPLAYS THE SORTED LIST ON THE TABLE VIEW WHICH SHOWS OBJECTS THAT ONLY MATCHES THE FILTER
        tableView.setItems(sortedList);
    }

    // EVERYTHING IN THIS OBSERVABLE LIST ARE THE ITEMS DISPLAYED ON THE TABLE VIEW
    public static ObservableList<Person> getObsList_Person() { return obsList_Person; }

    // THIS IS THE ITEMS OF THE FILTER COMBOBOX
    public static ObservableList<String> getFilterCategories() { return filterCategories; }
}

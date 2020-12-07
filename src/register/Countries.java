package register;

import files.FileText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Countries {
    private static boolean loaded = false;
    private static String countryCode;
    private static String countryName;
    private static ArrayList<String> countryCodes = new ArrayList<>();
    private static ArrayList<String> countryNames = new ArrayList<>();
    private static ObservableList<String> ObsCountryNames = FXCollections.observableArrayList();

    public static void countryOnChange(ComboBox<String> countryOptions, TextField txtCountryCode) {
        // WHEN WE SWITCH SCENES, MEANING LOADING ANOTHER FXML FILE,
        // THIS METHOD RUNS AGAIN AND AGAIN BECAUSE IT IS CALLED IN THE INITIALIZE METHOD
        // MEANING THE FOR LOOP RUNS OVER AND OVER TOO, ADDING THE SAME ELEMENTS INTO THE THE LISTS

        // ONE OF THE LISTS IS USED FOR A COMBOBOX,
        // IF THE LIST CONTAINS DUPLICATES, THE COMBOBOX WILL HAVE DUPLICATES TOO

        // TO PREVENT THIS FROM HAPPENING, WE ADDED AN IF STATEMENT
        // WHEN THE BOOLEAN IS FALSE, IT RUNS THE FOR LOOP AND SETS THE BOOLEAN TO TRUE
        // WHEN THIS METHOD IS CALLED AGAIN,
        // IT NO LONGER MEETS THE CONDITION OF THE IF STATEMENT BECAUSE THE BOOLEAN VALUE IS NOW TRUE
        // IT WILL NOT LOAD THE FOR LOOP AGAIN, THE LISTS WON'T HAVE ANY DUPLICATES

        // THE BOOLEAN NEEDS TO BE DEFINED OUTSIDE THE METHOD
        // WHEN IT'S DEFINED INSIDE THE METHOD, THE BOOLEAN VALUE RESETS TO FALSE

        if(!loaded){
            // READS THE CONTENTS OF THE COUNTRIES TEXT FILE AND SAVES ITS VALUES TO LISTS
            FileText fileReader = new FileText();
            ArrayList<String> fileContents = fileReader.read("countries.txt");
            for (String lines : fileContents) {
                ObsCountryNames.add(lines.split(";")[0]);
                countryNames.add(lines.split(";")[0]);
                countryCodes.add(lines.split(";")[1]);
            }
            loaded = true;
        }
        // KEEPS TRACK OF THE COMBOBOX VALUE
        countryOptions.getSelectionModel().selectedIndexProperty().addListener((observable,oldValue,newValue)->{
            txtCountryCode.setText("+" + countryCodes.get(newValue.intValue()));
            countryCode = countryCodes.get(newValue.intValue());
            countryName = ObsCountryNames.get(newValue.intValue());
        });
    }

    public static String getCountryName() {
        return countryName;
    }

    public static String getCountryCode() {
        return countryCode;
    }

    public static ArrayList<String> getCountryCodes() {
        return countryCodes;
    }

    public static ArrayList<String> getCountryNames() {
        return countryNames;
    }

    public static ObservableList<String> getObsCountryNames() {
        return ObsCountryNames;
    }
}

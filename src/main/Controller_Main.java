package main;

import cssOverriding.CustomAlertDialogs;
import files.FileHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import register.Countries;
import register.Person;
import table.BuildTable;
import validations.Validator;

import java.io.File;
import java.net.URL;
import java.time.DateTimeException;
import java.util.ResourceBundle;

public class Controller_Main implements Initializable {

    CustomAlertDialogs customAlert = new CustomAlertDialogs();
    @FXML private AnchorPane anchorPane;
    @FXML private TextField fname;
    @FXML private TextField lname;
    @FXML private TextField email;
    @FXML private TextField phone;
    @FXML private TextField birthdate;
    @FXML private TextField countryCode;
    @FXML public ComboBox<String> countryOptions;
    Alert success = new Alert(Alert.AlertType.INFORMATION);
    Alert warning = new Alert(Alert.AlertType.WARNING);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    Validator data = new Validator();
    private boolean newRecordAdded = false;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        // KEEPS TRACK OF THE VALUE OF THE COMBOBOX
        Countries.countryOnChange(countryOptions, countryCode);
        countryOptions.setItems(Countries.getObsCountryNames());
        countryOptions.setValue("Norway");

        // CHANGES THE APPEARANCE OF THE ALERT DIALOGS
        customAlert.modify(success,confirm,warning);
    }

    /** THIS METHOD HANDLES THE REGISTRATION OF PEOPLE */

    @FXML void register(ActionEvent event) {
        try {
            String txtFname = fname.getText();
            String txtLname = lname.getText();
            String txtEmail = email.getText();
            String txtPhone = phone.getText();
            String txtCountryName = Countries.getCountryName();
            String txtCountryCode = Countries.getCountryCode();
            String txtBirthdate = birthdate.getText();

            // INPUT VALIDATIONS OCCURS WHEN A PERSON OBJECT IS CREATED. CHECK PERSON CLASS
            Person person = new Person(txtFname,txtLname,txtEmail,txtPhone,txtBirthdate,txtCountryName,txtCountryCode);
            // PREVENTS USER FROM REGISTERING PEOPLE WITH THE SAME EMAIL AND NUMBER
            data.validate_registration(person);
            // WHEN EVERYTHING IS VALID, THE PERSON OBJECT IS ADDED TO AN OBSERVABLE LIST
            BuildTable.addDataToObedList(person);
            // RESET TEXTFIELDS
            resetTextFields();

            // THE TWO LINES OF CODE AFTER THESE COMMENTS OCCURS ONLY IF A FILE HAS BEEN OPENED
            // THE USER CAN GO BACK IN FORTH AS MUCH AS THEY WANT TO THE REGISTRATION WINDOW,
            // BUT AS LONG AS THEY DON'T REGISTER A NEW PERSON, THE FILE WILL STAY UNMODIFIED

            // FOR A FILE TO BE CONSIDERED MODIFIED, A NEW PERSON MUST BE REGISTERED
            // THAT IS WHY THE newRecordAdded IS DEFINED TO TELL US IF SOMEONE NEW HAS BEEN REGISTERED
            // AFTER THAT, THE OPENED FILE IS NOW MODIFIED. THE BOOLEAN MODIFIED IS NOW SET TO TRUE

            File filepath = FileHandler.getCurrentFilepath();
            if(filepath != null && newRecordAdded){ FileHandler.setModified(true); }

            success.setHeaderText("Registration Successful!");
            success.showAndWait();
        } catch (IllegalArgumentException | DateTimeException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
        }
    }

    /** THE FOLLOWING METHODS HANDLES WINDOW INTERACTION */

    private double coordinateX;

    private double coordinateY;

    @FXML void window_table(ActionEvent event) {
        // LOADS AN FXML FILE AND OPENS A NEW WINDOW SHOWING THE TABLEVIEW
        Loader load = new Loader();
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        load.window("_table.fxml", "TableView", stage);
    }

    @FXML void window_drag(MouseEvent event){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(event.getScreenX() - coordinateX);
        stage.setY(event.getScreenY() - coordinateY);
    }

    @FXML void window_press(MouseEvent event) {
        coordinateX = event.getSceneX();
        coordinateY = event.getSceneY();
    }

    @FXML void window_close(){
        // ONLY OCCURS WHEN CLOSING THE PROGRAM WITH UNSAVED CHANGES
        if(FileHandler.isModified()){
            FileHandler.saveChanges(confirm,success);
            Platform.exit();
            System.exit(0);
        }
        // ASK IF THEY WANT TO QUIT THE APPLICATION
        ButtonType buttonYES = new ButtonType("Yes");
        ButtonType buttonNO = new ButtonType("No");
        confirm.getButtonTypes().setAll(buttonYES, buttonNO);
        confirm.setHeaderText("Are you sure you want to quit the application?");
        confirm.showAndWait();
        if(confirm.getResult().getText().equals("Yes")){
            Platform.exit();
            System.exit(0);
        }
    }

    void resetTextFields(){
        // RESET THE TEXTFIELDS
        fname.setText("");
        lname.setText("");
        email.setText("");
        phone.setText("");
        birthdate.setText("");

        // WHEN THE TEXTFIELD RESETS, IT MEANS THAT A NEW RECORD HAS BEEN ADDED
        newRecordAdded = true;
    }
}

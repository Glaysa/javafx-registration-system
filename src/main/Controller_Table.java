package main;

import cssOverriding.CustomAlertDialogs;
import customExceptions.InvalidDuplicatesException;
import files.FileHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import register.Countries;
import register.Person;
import table.BuildTable;
import validations.Validator;

import java.net.URL;
import java.time.DateTimeException;
import java.util.ResourceBundle;

public class Controller_Table implements Initializable {

    CustomAlertDialogs customAlert = new CustomAlertDialogs();
    @FXML private Label fileNameIndicator;
    @FXML private AnchorPane anchorPane;
    @FXML private TextField filterValue;
    @FXML private ComboBox<String> filterOptions;
    @FXML private TableView<Person> tableView;
    @FXML private TableColumn<Person, String> col_fname;
    @FXML private TableColumn<Person, String> col_lname;
    @FXML private TableColumn<Person, String> col_birthdate;
    @FXML private TableColumn<Person, String> col_email;
    @FXML private TableColumn<Person, String> col_phone;
    @FXML private TableColumn<Person, String> col_country;
    Alert success = new Alert(Alert.AlertType.INFORMATION);
    Alert warning = new Alert(Alert.AlertType.WARNING);
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    Validator data = new Validator();

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        // INSERTS OBSERVABLE LIST TO TABLEVIEW
        BuildTable.insertDataToTable(tableView);

        // KEEPS TRACK OF THE COMBOBOX FILTER CATEGORY
        BuildTable.filterCatOnChange(filterOptions);
        filterOptions.setItems(BuildTable.getFilterCategories());
        filterOptions.setValue("First Name");

        // FILTERS TABLEVIEW USING THE FILTER CONDITION FROM A TEXTFIELD
        BuildTable.filterDataOnTable(tableView,filterValue);

        // ENABLES THE TABLEVIEW AND TABLE CELLS TO BE EDITED
        tableView.setEditable(true);
        col_fname.setCellFactory(TextFieldTableCell.forTableColumn());
        col_lname.setCellFactory(TextFieldTableCell.forTableColumn());
        col_birthdate.setCellFactory(TextFieldTableCell.forTableColumn());
        col_email.setCellFactory(TextFieldTableCell.forTableColumn());
        col_phone.setCellFactory(TextFieldTableCell.forTableColumn());
        col_country.setCellFactory(ComboBoxTableCell.forTableColumn(Countries.getObsCountryNames()));

        // CHANGES THE APPEARANCE OF THE ALERT DIALOGS
        customAlert.modify(success,confirm,warning);

        // IF A USER OPENED A FILE, DISPLAY ITS NAME AUTOMATICALLY WITH A MODIFICATION INDICATOR
        if(FileHandler.getCurrentFilepath() != null){
            if(FileHandler.isModified()){
                fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");
            } else {
                fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Saved");
            }
        } else {
            fileNameIndicator.setText("");
        }
    }

    /** THE FOLLOWING METHODS HANDLES THE SAVING AND OPENING OF JOBJ AND TEXT FILES */

    @FXML void saveChanges(ActionEvent event){
        String filter = filterValue.getText();
        try {
            // DO NOT ALLOW THE USER TO SAVED THE CHANGES WHEN THE TABLE IS FILTERED
            FileHandler.saveChanges(success,filter);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Saved");
        } catch (NullPointerException | IllegalArgumentException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
        }
    }

    @FXML void saveFileAs(ActionEvent event) {
        try {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            String filter = filterValue.getText();
            if(!filter.isEmpty()){
                // WHEN THE USER IS TRYING TO SAVED A FILTERED TABLE
                FileHandler.saveFileAs(stage,success,filter,tableView);
            } else {
                // WHEN THE USER IS TRYING TO SAVED AN UNFILTERED TABLE
                FileHandler.saveFileAs(stage,success);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
        }
    }

    @FXML void openFile(ActionEvent event) {
        try {
            // THE STAGE IS NECESSARY FOR OPENING A FILE CHOOSER
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            FileHandler.openFile(stage,success,tableView,filterValue);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Saved");
        } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
        }
    }

    /** THE FOLLOWING METHODS HANDLES THE DELETION OF RECORDS ON THE TABLE */

    @FXML void delete_AllRecords(ActionEvent event) {
        try {
            // DELETES ALL RECORDS AFTER CONFIRMATION
            FileHandler.deleteAll(confirm);
            // WHEN ALL RECORDS ARE THE DELETED, THE FILE IS NOW MODIFIED
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");
            // REFRESH THE TABLE TO SEE THE CHANGES
            tableView.refresh();
        } catch (IllegalArgumentException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
        }
    }

    @FXML void delete_Record(ActionEvent event) {
        try {
            // THE RECORD TO DELETE
            Person data = tableView.getSelectionModel().getSelectedItem();
            // DELETES THE RECORD AFTER CONFIRMATION
            FileHandler.delete(data,confirm);
            // WHEN A RECORD IS DELETED THE FILE IS NOW MODIFIED
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");
            // REFRESH THE TABLE TO SEE CHANGES
            tableView.refresh();
        } catch (IllegalArgumentException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
        }
    }

    /** THE FOLLOWING METHODS HANDLES VALIDATIONS WHEN TABLE CELLS ARE UPDATED DIRECTLY ON THE TABLEVIEW */

    @FXML private void txtfNameEdited(TableColumn.CellEditEvent<Person, String> event) {
        try {
            // THE USER INPUT ON THE TABLE CELL
            String newFnameValue = event.getNewValue();
            // VALIDATES AND UPDATES THE TABLE CELL VALUE OF FNAME
            event.getRowValue().setFname(newFnameValue);
            // WHEN THE UPDATE IS SUCCESSFUL, THE FILE IS NOW MODIFIED
            FileHandler.setModified(true);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");

            success.setHeaderText("First name has been changed to " + newFnameValue);
            success.showAndWait();
            tableView.refresh();
        } catch (IllegalArgumentException e){
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        }
    }

    @FXML private void txtlNameEdited(TableColumn.CellEditEvent<Person, String> event) {
        try {
            // THE USER INPUT ON THE TABLE CELL
            String newLnameValue = event.getNewValue();
            // VALIDATES AND UPDATES THE TABLE CELL VALUE OF LNAME
            event.getRowValue().setLname(newLnameValue);
            // WHEN THE UPDATE IS SUCCESSFUL, THE FILE IS NOW MODIFIED
            FileHandler.setModified(true);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");

            success.setHeaderText("Last name has been changed to " + newLnameValue);
            success.showAndWait();
            tableView.refresh();
        } catch (IllegalArgumentException e){
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        }
    }

    @FXML private void txtDateEdited(TableColumn.CellEditEvent<Person, String> event) {
        try {
            // THE USER INPUT ON THE TABLE CELL
            String newDateValue = event.getNewValue();
            // VALIDATES AND UPDATES THE TABLE CELL VALUE OF BIRTH DATE AND AGE
            event.getRowValue().setBirthdate(newDateValue);
            event.getRowValue().setAge(newDateValue);
            // WHEN THE UPDATE IS SUCCESSFUL, THE FILE IS NOW MODIFIED
            FileHandler.setModified(true);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");

            success.setHeaderText("Birth date has been changed to: " + newDateValue + "\nAge has been changed to: " + event.getRowValue().getAge());
            success.showAndWait();
            tableView.refresh();
        } catch (IllegalArgumentException | DateTimeException | ArrayIndexOutOfBoundsException e){
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        }
    }

    @FXML private void txtEmailEdited(TableColumn.CellEditEvent<Person, String> event) {
        try {
            // THE USER INPUT ON THE TABLE CELL
            String newEmailValue = event.getNewValue();
            // FINDS IF THERE ARE OTHER PEOPLE WHO ALREADY HAVE THE SAME EMAIL
            data.findDuplicate_email(newEmailValue);
            // VALIDATES AND UPDATES THE TABLE CELL VALUE OF EMAIL
            event.getRowValue().setEmail(newEmailValue);
            // WHEN THE UPDATE IS SUCCESSFUL, THE FILE IS NOW MODIFIED
            FileHandler.setModified(true);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");

            success.setHeaderText("Email has been changed to " + newEmailValue);
            success.showAndWait();
            tableView.refresh();
        } catch (IllegalArgumentException e){
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        }
    }

    @FXML private void txtPhoneEdited(TableColumn.CellEditEvent<Person, String> event) {
        try {
            // THE USER INPUT ON THE TABLE CELL
            String newPhoneValue = event.getNewValue();

            // THE PHONE NUMBER IS A COMBINATION OF A COUNTRY CODE AND THE NUMBER ITSELF
            // WHEN THE TABLE CELL VALUE OF NUMBER IS CHANGED, ONLY THE NUMBER COLUMN IS AFFECTED
            // THIS FINDS IF THERE ARE OTHER PEOPLE WHO ALREADY HAVE THE SAME NUMBER
            // THIS CHECKS BOTH THE COUNTRY CODE AND NUMBER
            data.findDuplicate_phone(newPhoneValue, event.getRowValue().getCountryName());

            // VALIDATES AND UPDATES THE TABLE CELL VALUE OF PHONE NUMBER
            event.getRowValue().setPhone(newPhoneValue);
            // WHEN THE UPDATE IS SUCCESSFUL, THE FILE IS NOW MODIFIED
            FileHandler.setModified(true);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");

            success.setHeaderText("Phone has been changed to " + newPhoneValue);
            success.showAndWait();
            tableView.refresh();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        }
    }

    @FXML private void txtCountryEdited(TableColumn.CellEditEvent<Person, String> event)  {
        String newCountryValue = event.getNewValue();
        String oldCountryValue = event.getOldValue();

        try {
            // UPDATES THE COUNTRY
            event.getRowValue().setCountryName(newCountryValue);

            // WHEN THE COUNTRY CHANGES, THE COUNTRY CODE THAT CORRESPONDS WITH THE PHONE NUMBER CHANGES AS WELL
            // WHEN THE COUNTRY CHANGES, 3 COLUMNS ARE AFFECTED: COUNTRY, COUNTRY CODE AND PHONE NUMBER
            // THIS FINDS IF THERE OTHER PEOPLE WHO ALREADY HAVE THE SAME NUMBER
            // THIS CHECKS BOTH THE COUNTRY CODE AND NUMBER
            data.findDuplicate_phone(event.getRowValue().getPhone(), event.getRowValue().getCountryName());

            // WHEN THE COUNTRY IS UPDATED, THE COUNTRY CODE NEED TO BE UPDATED AS WELL
            event.getRowValue().setCountryCode(event.getRowValue().getCountryCode(), newCountryValue);
            // WHEN THE UPDATE IS SUCCESSFUL, THE FILE IS NOW MODIFIED
            FileHandler.setModified(true);
            fileNameIndicator.setText(FileHandler.getCurrentFilename() + " - Modified");

            success.setHeaderText("Country has been changed to " + newCountryValue + "\nCountry code has been changed to: " + event.getRowValue().getCountryCode());
            success.showAndWait();
            tableView.refresh();
        } catch (InvalidDuplicatesException e){
            // IF THE COUNTRY CODE + PHONE NUMBER ALREADY EXISTS DO NOT UPDATE THE COUNTRY
            event.getRowValue().setCountryName(oldCountryValue);

            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        } catch (IllegalArgumentException e) {
            warning.setHeaderText(e.getMessage());
            warning.showAndWait();
            tableView.refresh();
        }
    }

    /** THE FOLLOWING METHODS HANDLES WINDOW INTERACTION */

    private double coordinateX;

    private double coordinateY;

    @FXML void window_home(ActionEvent event) {
        // LOADS AN FXML FILE AND OPENS A NEW WINDOW SHOWING THE REGISTRATION FORM
        Loader load = new Loader();
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        load.window("_home.fxml", "TableView", stage);
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

    @FXML void window_minimize(ActionEvent event){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setIconified(true);
    }
}

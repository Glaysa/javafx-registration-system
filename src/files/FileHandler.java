package files;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import register.Person;
import table.BuildTable;

import java.io.File;
import java.util.ArrayList;

public class FileHandler {
    private static File firstFilepath;
    private static File currentFilepath;
    private static String currentFilename;
    private static boolean modified;

    /** THIS METHOD OPENS A JOBJ AND TEXT FILE */

    public static void openFile(Stage stage, Alert success, TableView<Person> tableView, TextField filterValue){
        // SET UP THE OPEN DIALOG WINDOW
        FileChooser fileChooser = new FileChooser();
        // DEFINED THE FILE VARIABLE OUTSIDE THE METHOD BECAUSE WE NEED ACCESS TO IT IN OTHER METHODS
        currentFilepath = fileChooser.showOpenDialog(stage);

        if(currentFilepath != null){
            // BEFORE LOADING A NEW LIST, WE NEED TO CLEAR THE TABLE VIEW
            BuildTable.removeDataOnTable();

            // GET THE FILE EXTENSION
            String filename = currentFilepath.getName();
            String fileExtension = filename.substring(filename.lastIndexOf("."));

            // PREVENTS LOSING THE FILENAME WHEN THE USER CHANGES SCENES
            currentFilename = filename;

            // WE NEED TO SAVE THE CURRENT FILEPATH INTO ANOTHER VARIABLE
            // BECAUSE WHEN THE USER OPENS A NEW OPEN DIALOG BUT NOT CHOOSE ANY FILE TO OPEN
            // THE CURRENT FILEPATH IS SET TO NULL, THEN WE CAN NO LONGER MAKE ANY MODIFICATIONS TO THE FILE WE OPENED FIRST
            firstFilepath = currentFilepath;

            // READ FILE AND DISPLAY IT ON THE TABLEVIEW
            if(fileExtension.equals(".jobj")){
                // READS THE JOBJ FILE
                FileObject.read(currentFilepath);
                // DISPLAYS IT TO THE TABLEVIEW
                BuildTable.insertDataToTable(tableView);
                // ENABLES THE USER TO FILTER THE TABLE
                BuildTable.filterDataOnTable(tableView,filterValue);
                // RETURNS A SUCCESS ALERT
                success.setHeaderText("File Opened Successfully");
                success.showAndWait();

            } else if(fileExtension.equals(".txt")){
                // READS THE TEXT FILE
                FileText.read(currentFilepath);
                // DISPLAYS IT ON THE TABLEVIEW
                BuildTable.insertDataToTable(tableView);
                // ENABLES THE USER TO FILTER THE TABLE
                BuildTable.filterDataOnTable(tableView,filterValue);
                // RETURNS A SUCCESS ALERT
                success.setHeaderText("File Opened Successfully");
                success.showAndWait();

            } else {
                // IF THE FILE IS NOT A JOBJ OR TEXT FILE, THROW AN EXCEPTION
                throw new IllegalArgumentException("Please choose a 'jobj' file or a 'txt' file");
            }
        } else {
            // SINCE WE SAVED THE CURRENT FILEPATH AS THE FIRST FILE PATH
            // WHEN THE USER OPENS A NEW OPEN DIALOG, BUT CANCELS
            // THE CURRENT FILEPATH WILL NOT BE NULL BECAUSE WE ARE GIVING IT THE VALUE OF THE FIRST FILEPATH
            currentFilepath = firstFilepath;

            // IF THE USER DID NOT CHOOSE A FILE THROW AN EXCEPTION
            throw new NullPointerException("Filepath not given. Nothing to open");
        }

    }

    /** THIS METHOD IS USED TO SAVE ALL RECORDS WHEN THE TABLE IS NOT FILTERED */

    public static void saveFileAs(Stage stage, Alert success){
        // THIS WILL BE THE CONTENT OF THE FILE
        ArrayList<Person> fileContents = new ArrayList<>(BuildTable.getObsList_Person());
        if(fileContents.isEmpty()){ throw new IllegalArgumentException("There is nothing to save"); }

        // DEFINE WHICH TYPE OF FILE EXTENSIONS THE FILE CAN BE SAVED AS
        FileChooser.ExtensionFilter f1 = new FileChooser.ExtensionFilter("Jobj File", "*.jobj");
        FileChooser.ExtensionFilter f2 = new FileChooser.ExtensionFilter("Text File", "*.txt");

        // SET UP THE SAVE DIALOG WINDOW
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(f1,f2);
        File filepath = fileChooser.showSaveDialog(stage);

        if(filepath != null){
            // GET THE FILE EXTENSION
            String fileName = filepath.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            // IF THE FILE IS SAVED AS A JOBJ FILE, USE THE FILE OBJECT WRITER
            if(fileExtension.equals(".jobj")) {
                FileObject.write(fileContents, filepath);
                success.setHeaderText("File Saved as JavaObject file at: \n" + filepath.getAbsolutePath());
                success.showAndWait(); }

            // IF THE FILE IS SAVED AS A TEXT FILE, USE THE FILE TEXT WRITER
            if(fileExtension.equals(".txt")){
                FileText.write(fileContents, filepath);
                success.setHeaderText("File Saved as Text file at: \n" + filepath.getAbsolutePath());
                success.showAndWait(); }

        } else {
            // IF THE USER CANCELS TO SAVE THE FILE, THROW AN EXCEPTION
            throw new NullPointerException("File not saved");
        }

    }

    /** THIS METHOD IS USED TO SAVE ALL RECORDS WHEN THE TABLE IS FILTERED */

    public static void saveFileAs(Stage stage, Alert success, String filterValue, TableView<Person> tableView){
        // THIS WILL BE THE CONTENT OF THE FILE
        ArrayList<Person> fileContents = new ArrayList<>(tableView.getItems());
        if(fileContents.isEmpty()){ throw new IllegalArgumentException("There is nothing to save"); }

        // DEFINE WHICH TYPE OF FILE EXTENSIONS THE FILE CAN BE SAVED AS
        FileChooser.ExtensionFilter f1 = new FileChooser.ExtensionFilter("Jobj File", "*.jobj");
        FileChooser.ExtensionFilter f2 = new FileChooser.ExtensionFilter("Text File", "*.txt");

        // SET UP THE SAVE DIALOG WINDOW
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(f1,f2);
        File filepath = fileChooser.showSaveDialog(stage);

        if(filepath != null && !filterValue.isEmpty()){
            // GET THE FILE EXTENSION
            String fileName = filepath.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            // IF THE FILE IS SAVED AS A JOBJ FILE, USE THE FILE OBJECT WRITER
            if(fileExtension.equals(".jobj")) {
                FileObject.write(fileContents, filepath);
                success.setHeaderText("File Saved as JavaObject file at: \n" + filepath.getAbsolutePath());
                success.showAndWait(); }

            // IF THE FILE IS SAVED AS A TEXT FILE, USE THE FILE TEXT WRITER
            if(fileExtension.equals(".txt")){
                FileText.write(fileContents, filepath);
                success.setHeaderText("File Saved as Text file at: \n" + filepath.getAbsolutePath());
                success.showAndWait(); }

        } else {
            // IF THE USER CANCELS TO SAVE THE FILE, THROW AN EXCEPTION
            throw new NullPointerException("File not saved");
        }

    }

    /** THIS METHOD IS USED TO SAVE THE CHANGES MADE ON A FILE */

    public static void saveChanges(Alert success, String filterValue){
        // WHEN THE TABLE IS FILTERED DON'T LET THE USER TO SAVE THE CHANGES, IT NEEDS TO BE SAVED AS A NEW FILE
        if(!filterValue.isEmpty()){
            throw new IllegalArgumentException("You have filtered the table.\nSave it as a new file instead"); }

        // GETS THE CURRENT FILEPATH OF THE FILE THE USER OPENED
        File loadedFile = currentFilepath;

        if(loadedFile != null){
            // GET FILE EXTENSION
            String fileName = loadedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            // GET THE TABLE VIEW ITEMS: THIS IS THE CONTENT OF THE FILE
            ArrayList<Person> fileContents = new ArrayList<>(BuildTable.getObsList_Person());

            // IF THE FILE IS OPENED AS A JOBJ FILE, USE THE FILE OBJECT WRITER TO SAVE THE CHANGES
            if(fileExtension.equals(".jobj")) {
                FileObject.write(fileContents, loadedFile);
                success.setHeaderText("Changes Saved.\nFile at: " + loadedFile.getAbsolutePath());
                success.showAndWait();
                // ONCE THE CHANGES ARE SAVED, IT IS NO LONGER A MODIFIED FILE
                FileHandler.setModified(false); }

            // IF FILE IS OPENED AS A TEXT FILE, USE THE FILE TEXT WRITER TO SAVE THE CHANGES
            if(fileExtension.equals(".txt")){
                FileText.write(fileContents, loadedFile);
                success.setHeaderText("Changes saved.\nFile at: " + loadedFile.getAbsolutePath());
                success.showAndWait();
                // ONCE THE CHANGES ARE SAVED, IT IS NO LONGER A MODIFIED FILE
                FileHandler.setModified(false); }

        } else {
            // IF THE USER TRIES TO SAVE CHANGES WITHOUT OPENING A FILE, THROW AN EXCEPTION
            throw new NullPointerException("Changes not saved\nOpen a file to modify");
        }
    }

    /** THIS METHOD IS USED WHEN A USER QUITS THE PROGRAM WITHOUT SAVING THE CHANGES THEY MADE */

    public static void saveChanges(Alert confirm, Alert success){
        // GETS THE CURRENT FILEPATH OF THE FILE THE USER OPENED
        File loadedFile = currentFilepath;

        if(loadedFile != null){
            // GET FILE EXTENSION
            String fileName = loadedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));

            // GET THE TABLE VIEW ITEMS: THIS IS THE CONTENT OF THE FILE
            ArrayList<Person> fileContents = new ArrayList<>(BuildTable.getObsList_Person());

            // IF THE FILE IS OPENED AS A JOBJ FILE, USE THE FILE OBJECT WRITER TO SAVE THE CHANGES
            if(fileExtension.equals(".jobj")) {
                // ASK IF THEY WANT TO SAVE THE CHANGES BEFORE QUITTING THE PROGRAM
                ButtonType buttonYES = new ButtonType("Yes");
                ButtonType buttonNO = new ButtonType("No");

                confirm.getButtonTypes().setAll(buttonYES, buttonNO);
                confirm.setHeaderText("Do you want to save all changes before quitting?");
                confirm.showAndWait();

                // IF YES SAVE THE CHANGES, OTHERWISE DISCARD THE CHANGES
                if(confirm.getResult().getText().equals("Yes")) {
                    success.setHeaderText("All Changes has been saved");
                    success.showAndWait();
                    FileObject.write(fileContents, loadedFile);
                    FileHandler.setModified(false);
                } else {
                    success.setHeaderText("Changes discarded.");
                    success.showAndWait();
                }
            }

            // IF FILE IS OPENED AS A TEXT FILE, USE FILE TEXT WRITER TO SAVE THE CHANGES
            if(fileExtension.equals(".txt")){
                // ASK IF THEY WANT TO SAVE THE CHANGES BEFORE QUITTING THE PROGRAM
                ButtonType buttonYES = new ButtonType("Yes");
                ButtonType buttonNO = new ButtonType("No");

                confirm.getButtonTypes().setAll(buttonYES, buttonNO);
                confirm.setHeaderText("Do you want save all changes before quitting?");
                confirm.showAndWait();

                // IF YES SAVE THE CHANGES, OTHERWISE DISCARD THE CHANGES
                if(confirm.getResult().getText().equals("Yes")) {
                    success.setHeaderText("All Changes has been saved");
                    success.showAndWait();
                    FileText.write(fileContents, loadedFile);
                    FileHandler.setModified(false);
                } else {
                    success.setHeaderText("Changes discarded.");
                    success.showAndWait();
                }
            }
        } else {
            // IF THE USER TRIES TO SAVE CHANGES WITHOUT OPENING A FILE, THROW AN EXCEPTION
            throw new NullPointerException("Changes not saved\nOpen a file to modify");
        }
    }

    /** THIS METHOD DELETES ONE RECORD */

    public static void delete(Person toRemove, Alert confirm){
        if(toRemove == null){ throw new IllegalArgumentException("Please select a record to delete");}

        // ASK FOR CONFIRMATION
        ButtonType buttonYES = new ButtonType("Yes");
        ButtonType buttonNO = new ButtonType("No");

        confirm.getButtonTypes().setAll(buttonYES, buttonNO);
        confirm.setHeaderText("Are you sure you want to delete this record?");
        confirm.showAndWait();

        if(confirm.getResult().getText().equals("Yes")) {
            BuildTable.removeDataOnTable(toRemove);
            FileHandler.setModified(true);
        }
    }

    /** THIS METHOD DELETES ALL RECORDS */

    public static void deleteAll(Alert confirm){
        if(BuildTable.getObsList_Person().isEmpty()){
            throw new IllegalArgumentException("There is nothing to delete");
        } else {
            ButtonType buttonYES = new ButtonType("Yes");
            ButtonType buttonNO = new ButtonType("No");

            confirm.getButtonTypes().setAll(buttonYES, buttonNO);
            confirm.setHeaderText("Are you sure you want to delete all records?");
            confirm.showAndWait();

            if(confirm.getResult().getText().equals("Yes")){
                BuildTable.removeDataOnTable();
                FileHandler.setModified(true);
            }
        }
    }

    /** THESE METHODS HELP US KEEP TRACK IF A FILE IS MODIFIED OR NOT */

    public static void setModified(boolean status){ modified = status; }

    public static File getCurrentFilepath() { return currentFilepath; }

    public static String getCurrentFilename() { return currentFilename; }

    public static boolean isModified() { return modified; }
}

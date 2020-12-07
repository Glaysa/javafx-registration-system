package files;

import register.Person;
import table.BuildTable;

import java.io.*;
import java.util.ArrayList;

public class FileObject {

    /** OBJ FILE WRITER */
    public static void write(ArrayList<Person> personList, File filePath){
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream OBJ_outstream = new ObjectOutputStream(file);
            for(Person person : personList){
                OBJ_outstream.writeObject(person);
            }
        } catch(IOException e){
            System.err.println("A problem occurred during serialization: " + e.getMessage());
        }
    }

    /** OBJ FILE READER */
    @SuppressWarnings("InfiniteLoopStatement") // IGNORES INFINITE WHILE LOOP
    public static void read(File filepath) {
        try {
            FileInputStream file = new FileInputStream(filepath);
            ObjectInputStream OBJ_inStream = new ObjectInputStream(file);

            // AS LONG AS IT'S NOT THE END OF FILE, KEEP READING
            // THIS IS NECESSARY BECAUSE THIS THROWS AN END OF FILE EXCEPTION
            while(true){
                Person person = (Person) OBJ_inStream.readObject();
                BuildTable.addDataToObedList(person);
            }
        } catch (EOFException ignored) {
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}

package files;

import register.Person;
import table.BuildTable;

import java.io.*;
import java.util.ArrayList;

public class FileText {

    /** USED TO WRITE A PERSON OBJECT ON A TXT FILE */
    public static void write(ArrayList<Person> personList, File filepath){
        try {
            FileWriter fileWriter = new FileWriter(filepath, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(Person person : personList){
                bufferedWriter.write(person.toString());
            }
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException | NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    /** USED TO READ A PERSON OBJECT FROM A TXT FILE */
    public static void read(File filepath)  {
        String lines;
        String[] values;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            while((lines = in.readLine()) != null){
                values = lines.split(";");

                // WHEN THE USER TRIES TO OPEN A TEXT NOT CONTAINING A PERSON OBJECT
                if(values.length != 8){ throw new ArrayIndexOutOfBoundsException("File not valid"); }

                String fname = values[0];
                String lname = values[1];
                String email = values[2];
                String phone = values[3];
                String birthdate = values[4];
                String countryName = values[5];
                String countryCode = values[6];
                String age = values[7];

                Person person = new Person(fname,lname,email,phone,birthdate,countryName,countryCode);
                BuildTable.addDataToObedList(person);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /** USED TO READ A SIMPLE FILE: countries.txt */
    public ArrayList<String> read(String filepath)  {
        ArrayList<String> list = new ArrayList<>();
        String lines;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            while((lines = in.readLine()) != null){ list.add(lines); }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}

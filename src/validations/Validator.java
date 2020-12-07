package validations;

import customExceptions.InvalidBirthDateException;
import customExceptions.InvalidDuplicatesException;
import files.FileText;
import register.Countries;
import register.Person;
import table.BuildTable;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;

public class Validator implements Serializable {
    private transient String fname;
    private transient String lname;
    private transient String email;
    private transient String phone;
    private transient String countryName;
    private transient String countryCode;
    private transient String birthdate;
    private transient int age;

    public void validate_fname(String fname){
        if(!fname.isBlank() && fname.matches("[^\\d]+")){
            this.fname = fname;
        } else {
            throw new IllegalArgumentException("Invalid First Name");
        }
    }

    public void validate_lname(String lname){
        if(!lname.isBlank() && lname.matches("[^\\d]+")){
            this.lname = lname;
        } else {
            throw new IllegalArgumentException("Invalid Last Name");
        }
    }

    public void validate_email(String email){
        if(!email.isBlank() && email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid Email");
        }
    }

    public void validate_phone(String phone){
        if(!phone.isEmpty() && !phone.isBlank() && phone.length() < 20 && phone.matches("[\\d+(]+([\\d()-]+(?: [\\d()-]+)*)")){
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Invalid phone");
        }
    }

    public void validate_countryName(String countryName){
        if(!countryName.isBlank()){
            this.countryName = countryName;
        } else {
            throw new IllegalArgumentException("Please select your country");
        }
    }

    public void validate_countryCode(String countryCode, String countryName){
        if(countryCode != null){
            FileText reader = new FileText();
            ArrayList<String> fileContents = reader.read("countries.txt");
            ArrayList<String> countryNames = new ArrayList<>();
            ArrayList<String> countryCodes = new ArrayList<>();
            for(String lines:fileContents){
                countryNames.add(lines.split(";")[0]);
                countryCodes.add(lines.split(";")[1]);
            }
            for(int i = 0; i < countryNames.size(); i++){
                if(countryName.equals(countryNames.get(i))){
                    this.countryCode = "+" + countryCodes.get(i);
                }
            }
        } else {
            throw new IllegalArgumentException("Please select your country");
        }
    }

    public void validate_birthdate(String birthdate){
        try {
            int day = Integer.parseInt(birthdate.split("\\.")[0]);
            int month = Integer.parseInt(birthdate.split("\\.")[1]);
            int year = Integer.parseInt(birthdate.split("\\.")[2]);
            int lengthOfMonth = YearMonth.of(year,month).lengthOfMonth();
            int currentYear = Year.now().getValue();

            if(day < 1 || day > lengthOfMonth){ throw new InvalidBirthDateException("Invalid Day"); }
            if(year < 1900 || year > currentYear){ throw new InvalidBirthDateException("Invalid Year"); }
            this.birthdate = birthdate;

        } catch (InvalidBirthDateException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid Date format");
        } catch (DateTimeException e) {
            throw new DateTimeException("Invalid Month");
        }
    }

    public void validate_age(String birthdate){
        try {
            int day = Integer.parseInt(birthdate.split("\\.")[0]);
            int month = Integer.parseInt(birthdate.split("\\.")[1]);
            int year = Integer.parseInt(birthdate.split("\\.")[2]);

            Calendar now = Calendar.getInstance();
            int current_day = now.get(Calendar.DAY_OF_MONTH);
            int current_month = now.get(Calendar.MONTH);
            int current_year = now.get(Calendar.YEAR);
            int current_age = current_year - year;

            boolean calculated_age = (month > current_month) && (current_day < day);
            if(calculated_age){ current_age--; }
            this.age = current_age;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Age");
        }
    }

    public void validate_registration(Person newPerson){
        ArrayList<Person> personList = new ArrayList<>(BuildTable.getObsList_Person());
        for(Person person : personList){
            boolean email = person.getEmail().equals(newPerson.getEmail());
            boolean phone = person.getPhone().equals(newPerson.getPhone());
            boolean countryCode = person.getCountryCode().equals(newPerson.getCountryCode());
            if(email){
                throw new InvalidDuplicatesException("A person with the same email is already registered.");
            }
            if(phone && countryCode){
                throw new InvalidDuplicatesException("A person with the same number is already registered.");
            }
        }
    }

    public void findDuplicate_email(String txtEmail){
        ArrayList<Person> personList = new ArrayList<>(BuildTable.getObsList_Person());
        for(Person person : personList){
            boolean email = person.getEmail().equals(txtEmail);
            if(email){
                throw new InvalidDuplicatesException("A person with the same email already exists.");
            }
        }
    }

    public void findDuplicate_phone(String txtPhone, String txtCountryName){
        ArrayList<Person> personList = new ArrayList<>(BuildTable.getObsList_Person());
        for(Person person : personList){
            ArrayList<String> countryCodes = Countries.getCountryCodes();
            ArrayList<String> countryNames = Countries.getCountryNames();
            for(int i = 0; i < countryNames.size(); i++){
                if(txtCountryName.equals(countryNames.get(i))){
                    boolean phone = person.getPhone().equals(txtPhone);
                    boolean countryCode = person.getCountryCode().equals("+"+countryCodes.get(i));
                    if(phone && countryCode){
                        throw new InvalidDuplicatesException("A person with the same number already exists.");
                    }
                }
            }
        }
    }

    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCountryName() { return countryName; }
    public String getCountryCode() { return countryCode; }
    public String getBirthdate() { return birthdate; }
    public int getAge() { return age; }
}

/* Email Regex Source: https://howtodoinjava.com/regex/java-regex-validate-email-address/ */

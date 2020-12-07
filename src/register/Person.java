package register;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import validations.Validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 1;
    private transient SimpleStringProperty fname;
    private transient SimpleStringProperty lname;
    private transient SimpleStringProperty email;
    private transient SimpleStringProperty phone;
    private transient SimpleStringProperty birthdate;
    private transient SimpleStringProperty countryName;
    private transient SimpleStringProperty countryCode;
    private transient SimpleIntegerProperty age;
    private Validator data = new Validator();

    public Person(String fname, String lname, String email, String phone,String birthdate, String countryName, String countryCode){
        data.validate_fname(fname);
        data.validate_lname(lname);
        data.validate_email(email);
        data.validate_phone(phone);
        data.validate_birthdate(birthdate);
        data.validate_countryCode(countryCode, countryName);
        data.validate_countryName(countryName);
        data.validate_age(birthdate);

        this.fname = new SimpleStringProperty(data.getFname());
        this.lname = new SimpleStringProperty(data.getLname());
        this.email = new SimpleStringProperty(data.getEmail());
        this.phone = new SimpleStringProperty(data.getPhone());
        this.countryName = new SimpleStringProperty(data.getCountryName());
        this.countryCode = new SimpleStringProperty(data.getCountryCode());
        this.birthdate = new SimpleStringProperty(data.getBirthdate());
        this.age = new SimpleIntegerProperty(data.getAge());
    }

    public void setFname(String fname) {
        data.validate_fname(fname);
        this.fname.set(fname);
    }
    public void setLname(String lname) {
        data.validate_lname(lname);
        this.lname.set(lname);
    }
    public void setBirthdate(String birthdate){
        data.validate_birthdate(birthdate);
        this.birthdate.set(birthdate);
    }
    public void setEmail(String email){
        data.validate_email(email);
        this.email.set(email);
    }
    public void setPhone(String phone){
        data.validate_phone(phone);
        this.phone.set(phone);
    }
    public void setCountryCode(String countryCode, String countryName){
        data.validate_countryCode(countryCode, countryName);
        this.countryCode.set(data.getCountryCode());
    }
    public void setCountryName(String countryName){
        data.validate_countryName(countryName);
        this.countryName.set(countryName);
    }
    public void setAge(String birthdate){
        data.validate_age(birthdate);
        this.age.set(data.getAge());
    }

    public String getFname() { return fname.getValue(); }
    public String getLname() { return lname.getValue(); }
    public String getEmail() { return email.getValue(); }
    public String getPhone() { return phone.getValue(); }
    public String getCountryName() { return countryName.getValue(); }
    public String getCountryCode() { return countryCode.getValue(); }
    public String getBirthdate() { return birthdate.getValue(); }
    public int getAge() { return age.getValue(); }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s\n",
                fname.getValue(), lname.getValue(), email.getValue(), phone.getValue(),
                birthdate.getValue(), countryName.getValue(), countryCode.getValue(), age.getValue());
    }

    // NECESSARY BECAUSE THE OBJECT ATTRIBUTES ARE OF TYPE --SIMPLE PROPERTY--
    // --SIMPLE PROPERTY-- CANNOT BE SERIALIZED
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(getFname());
        s.writeUTF(getLname());
        s.writeUTF(getEmail());
        s.writeUTF(getPhone());
        s.writeUTF(getBirthdate());
        s.writeUTF(getCountryName());
        s.writeUTF(getCountryCode());
        s.writeInt(getAge());
    }

    // NECESSARY BECAUSE THE OBJECT ATTRIBUTES ARE OF TYPE --SIMPLE PROPERTY--
    // --SIMPLE PROPERTY-- CANNOT BE DE-SERIALIZED
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        String fname = s.readUTF();
        String lname = s.readUTF();
        String email = s.readUTF();
        String phone = s.readUTF();
        String birthdate = s.readUTF();
        String countryName = s.readUTF();
        String countryCode = s.readUTF();
        int age = s.readInt();

        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.birthdate = new SimpleStringProperty(birthdate);
        this.countryName = new SimpleStringProperty(countryName);
        this.countryCode = new SimpleStringProperty(countryCode);
        this.age = new SimpleIntegerProperty(age);
    }
}

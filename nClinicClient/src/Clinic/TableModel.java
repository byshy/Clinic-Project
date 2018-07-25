package Clinic;

import javafx.beans.property.StringProperty;

// this class is a model for table information
public class TableModel {

    private StringProperty id,name,age,sex,address,connect;

    public TableModel(StringProperty id, StringProperty name, StringProperty age, StringProperty sex, StringProperty address, StringProperty connect) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.connect = connect;
    }

    public String getId() {
        return id.getValueSafe();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAge() {
        return age.get();
    }

    public StringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public String getSex() {
        return sex.get();
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getConnect() {
        return connect.get();
    }

    public StringProperty connectProperty() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect.set(connect);
    }
}

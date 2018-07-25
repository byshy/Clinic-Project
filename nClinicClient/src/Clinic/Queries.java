package Clinic;

import javafx.beans.property.StringProperty;

// this class is a model for table information
public class Queries {

    private StringProperty id,pstiantID,name,visit_type,payment,reserveDate,reserveTime,attendDate,attendTime,paymentDate,attend,attend_type;

    public Queries(StringProperty id, StringProperty pstiantID, StringProperty name, StringProperty visit_type,
                   StringProperty payment, StringProperty reserveDate, StringProperty reserveTime,
                   StringProperty attendDate, StringProperty attendTime, StringProperty paymentDate,
                   StringProperty attend, StringProperty attend_type) {
        this.id = id;
        this.pstiantID = pstiantID;
        this.name = name;
        this.visit_type = visit_type;
        this.payment = payment;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.attendDate = attendDate;
        this.attendTime = attendTime;
        this.paymentDate = paymentDate;
        this.attend = attend;
        this.attend_type = attend_type;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getPatiantID() {
        return pstiantID.get();
    }

    public StringProperty pstiantIDProperty() {
        return pstiantID;
    }

    public void setPstiantID(String pstiantID) {
        this.pstiantID.set(pstiantID);
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

    public String getVisit_type() {
        if (visit_type.getValue() == null){
            return " ";
        }else {
            return visit_type.get();
        }
    }

    public StringProperty visit_typeProperty() {
        return visit_type;
    }

    public void setVisit_type(String visit_type) {
        this.visit_type.set(visit_type);
    }

    public String getPayment() {
        return payment.get();
    }

    public StringProperty paymentProperty() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment.set(payment);
    }

    public String getReserveDate() {
        return reserveDate.get();
    }

    public StringProperty reserveDateProperty() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate.set(reserveDate);
    }

    public String getReserveTime() {
        return reserveTime.get();
    }

    public StringProperty reserveTimeProperty() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime.set(reserveTime);
    }

    public String getAttendDate() {
        return attendDate.get();
    }

    public StringProperty attendDateProperty() {
        return attendDate;
    }

    public void setAttendDate(String attendDate) {
        this.attendDate.set(attendDate);
    }

    public String getAttendTime() {
        return attendTime.get();
    }

    public StringProperty attendTimeProperty() {
        return attendTime;
    }

    public void setAttendTime(String attendTime) {
        this.attendTime.set(attendTime);
    }

    public String getPaymentDate() {
        return paymentDate.get();
    }

    public StringProperty paymentDateProperty() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate.set(paymentDate);
    }

    public String getAttend() {
        if (attend.getValue() == null ){
            return " ";
        }else {
            return attend.get();
        }
    }

    public StringProperty attendProperty() {
        return attend;
    }

    public void setAttend(String attend) {
        this.attend.set(attend);
    }

    public String getAttend_type() {
        if (attend_type.getValue() == null ){
            return " ";
        }else {
            return attend_type.get();
        }
    }

    public StringProperty attend_typeProperty() {
        return attend_type;
    }

    public void setAttend_type(String attend_type) {
        this.attend_type.set(attend_type);
    }

}

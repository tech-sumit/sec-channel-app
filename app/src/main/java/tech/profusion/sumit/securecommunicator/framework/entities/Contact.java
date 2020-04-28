package tech.profusion.sumit.securecommunicator.framework.entities;

import android.os.Bundle;

/**
 * Created by Sumit Agrawal on 13-01-2018.
 */

public class Contact {
    private String name;
    private String contactNo;
    private String password;

    public Contact(){

    }

    public Contact(String name,String contactNo,String password){
        this.name=name;
        this.contactNo=contactNo;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

        public String getContactNo() {
        return contactNo;
    }

    public Bundle getAll(){
        Bundle bundle=new Bundle();
        bundle.putString(Constants.COLUMN_CONTACT_NO, contactNo);
        bundle.putString(Constants.COLUMN_NAME, name);
        bundle.putString(Constants.COLUMN_PASSWORD, password);
        return bundle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAll(Bundle bundle){
        bundle.getString(Constants.COLUMN_NAME);
        bundle.getString(Constants.COLUMN_CONTACT_NO);
        bundle.getString(Constants.COLUMN_PASSWORD);
    }

}

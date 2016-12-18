package com.lingoville.meridian;

import com.lingoville.meridian.Data.TenantsContract;

/**
 * Created by m88to on 12/9/2016.
 */

public class Tenants {
    private int roomNumber;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private String email;

    public String[] projection = {
            TenantsContract.TenantEntry._ID,
            TenantsContract.TenantEntry.COLUMN_ROOMNUMBER,
            TenantsContract.TenantEntry.COLUMN_FIRSTNAME,
            TenantsContract.TenantEntry.COLUMN_LASTNAME,
            TenantsContract.TenantEntry.COLUMN_EMAIL,
            TenantsContract.TenantEntry.COLUMN_PHONENUMBER,
            TenantsContract.TenantEntry.COLUMN_MOVEIN,
            TenantsContract.TenantEntry.COLUMN_MOVEOUT
    };

    public Tenants(){
        roomNumber = -1;
        firstName = "Chang";
        lastName = "Hong";
        phoneNumber = 1111111111;
        email = "changki.hong@gmail.com";
    }

    public Tenants (int rm, String fn, String ln, int pn, String e ){
        roomNumber = rm;
        firstName = fn;
        lastName = ln;
        phoneNumber = pn;
        email = e;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return roomNumber/100;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public  String getEmail() {
        return email;
    }


}

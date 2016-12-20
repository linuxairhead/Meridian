package com.lingoville.meridian.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Meridian app.
 */
public final class TenantsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private TenantsContract(){}


    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.lingoville.meridian";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * as the ContentProvider hasn't been given any information on what to do with "staff".     *
     */
    public static final String PATH_RoomInfo = "RoomInfo";

    public static final String PATH_Tenant = "Tenants";

    public static final String PATH_Finance = "Finance";

    /**
     * Inner class that defines constant values for the tenants database table.
     * Each entry in the table contain tenant infomation.
     */
    public static final class TenantEntry implements BaseColumns{

        public static final Uri  ROOM_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RoomInfo);

        public static final Uri  TENANT_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_Tenant);

        public static final Uri  FINANCE_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_Finance);

        /** Name of database table for room information */
        public final static String ROOM_TABLE_NAME = "RoomTable";

        /** Name of database table for tenants */
        public final static String Tenants_TABLE_NAME = "TenantsTable";

        /** Name of database table for finance statement */
        public final static String Finance_TABLE_NAME = "FinanceTable";
        /*
          For Room Info Table
         */
                /*
                * Room Vancancy
                */
                public final static String COLUMN_Vancant = "Room_Vancancy";

        /*
          For Tenant Table
         */
                /*
                * Unique ID number for Tenant
                */
                public final static String _ID = BaseColumns._ID;

                /*
                 * Tenant Room Number
                 */
                public final static String COLUMN_ROOMNUMBER ="Room_Number";

                /*
                * Tenant First Name
                */
                public final static String COLUMN_FIRSTNAME = "First_Name";

                /*
                * Tenant Last Name
                */
                public final static String COLUMN_LASTNAME = "Last_Name";

                /*
                * Tenant Phone Number
                */
                public final static String COLUMN_PHONENUMBER = "Phone_Number";

                /*
                * Tenant Email Address
                */
                public final static String COLUMN_EMAIL = "Email";

                /*
                * Tenant Moving In Date
                */
                public final static String COLUMN_MOVEIN = "Move_In_Date";

                /*
                * Tenant Moving out Date
                */
                public final static String COLUMN_MOVEOUT = "Move_Out_Date";


      /*
      For Finance Table
     */

                /*
                * Date of Transaction
                 */
                public final static String COLUMN_DATE = "Date";

                /*
                 * Type of Transation - Deposit, Rent, Utility
                 */
                public final static String COLUMN_TRANSATION_TYPE = "Transaction_Type";

                /*
                 * The amount of Transation
                 */
                public final static String COLUMN_AMOUNT = "Amount";

                /*
                *  Tranaction Type
                 */
                public static final int TRANSATION_DEPOSIT = 0;
                public static final int TRANSATION_RENT = 1;
                public static final int TRANSATION_UTILITY = 2;
    }
}

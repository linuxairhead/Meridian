Bug Report

12/18/2016 Transaction Info was able to print only for the particular room
		OnCreateloader where does query occur when the CursorLoader

12/17/2016 TenantsDbHelper 
		When Finance Table was created, double check the space for Query Command
		String SQL_CREATE_FINANCE_TABLE =
                	"CREATE TABLE " + TenantsContract.TenantEntry.Finance_TABLE_NAME + " ("
                        + TenantsContract.TenantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " INTEGER NOT NULL, " <-- white space was need
                        + TenantsContract.TenantEntry.COLUMN_DATE + " TEXT NOT NULL, "
                        + TenantsContract.TenantEntry.COLUMN_TRANSATION_TYPE + " TEXT NOT NULL, "
                        + TenantsContract.TenantEntry.COLUMN_AMOUNT +" INTEGER NOT NULL );";


	   

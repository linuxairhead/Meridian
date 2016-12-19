Bug Report

12/19/2016 Bug Reported, 
		1. From Main Activity create new Tenant by choosing one of the room.
		2. It will take to TransactionInfoActivity.
		3. From TransactionInfoActivity, create new Transaction by choosing the '+'
		4. Input new transaction, will take back to TransactionInfoActivity.
		5. Bug, it won't display the transaction info for the room.
		Miss spell the string for putExtra for Intent Object. from RoomNumber to Room_Number

12/18/2016 Transaction Info was update with LoaderManager

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


	   

Bug Report

12/30/2016 TenantEditActivity
		Implemented starting camera app by choosing floating fab 
		
12/29/2016 TenantEditActivity
		Implemented new floating fab button for tenant image 

12/28/2016 TenantEditActivity, TransactionEditActivity
		Remove Room Number TextView for Edit Activity 

12/28/2016 Reorganize the navigation between activity

12/28/2016 Redesign the TenantEditActivity View

12/26/2016 Design update

12/25/2016 MainFragment, TenantEditActivity, TenantInfoActivity ...
		Improve User Nevigation 
		
12/25/2016 MainFragment
		Bug Fixed: RoomTable db query and varify fixed

12/25/2016 MainActivity, TenantInfoActivity
		Remove unwanted MainMenu

12/25/2016 MainImageAdapter
		Bug Fixed: Main Image randomly set improperly *****

12/24/2016 MainFragment
		MainFragment clean unnecessary variable

12/24/2016 MainFragment, TenantEditActivity
		Continue migrate RoomTable db from TenantEditActivity to MainFragment

12/24/2016 MainFragment, MainActivity
		Implemented the Fragment for the Main and Migrate RoomTable related db work
		
12/23/2016 MainActivity, MainImageAdapter
		Implemented If the room has occupied, set a man image to the room

12/23/2016 MainImageAdapter, TenantEditActivity
		Miscellaneous UX change
		
12/22/2016 TenantEditActivity, DatePickerDialogClass
		Bug Fixed: previous commit cause bug.
		
12/22/2016 TenantEditActivity,	08623f4686b24da4a840e66bcdce8381f0d1e7aa
		Bug Fixed: downgrade lib for DateFormat, SimpleDateFormte, Date.

12/22/2016 DatePickerDialogClass, TenantEditActivity, TransactionEditActivity 	5b4cada9cb01c9a31b707eb19a3cc933345845b2
		Secure Apk Build Error Fixed  

12/22/2016 MainImageAdapter		ffdfe609b241816443384a1235bfc7af1ea02334
		Bug Fixed: UI fixed for Small Screen Device
		
12/21/2016 TransactionEditActivity 	228893fac97739e1e839ed6a11ea83a574ea0e82
		Bug Fixed: Transaction Spinner won't set proper value for Edit
		
12/21/2016 DatePickerDialogClass, TransactionEditActivity
		Implemented DatePicker for TransactionEditActivity

12/21/2016 TenantProvider, TransactionInfoActivity
		TransactionInfoActivity drop down Delete handle at TenantProvider
		
12/21/2016 TenantProvider
		TransactionInfoActivity drop down Edit handle at TenantProvider

12/21/2016 TenantEditActivity
		Bug Fixed: when program was first used, failed to verify RoomVacant Table
		
12/21/2016 TransactionInfoActivity	d6de89018933ff80626881f98ff492d19a752e17
		Enabled Transaction drop down menu 

12/21/2016 TenantProvider	598ec957ec66cf6238422cd7ac2d220b920aaf80
		1. when Tenant was deleted, update RoomInfo Table

12/21/2016 TenantInfoActivity	2b5d58332f414eb88f614809760385228414be87
		Enabled delete option but still has bug, 
		1. When tenant removed from db, it also need to change the flag on RoomInfo Table
		2. When tenant removed from db, the transaction table for room need to reset as well.

12/20/2016 TenantInfoActivity	6147e147f270e5d2826706a6bae5bdb9afdc48f6
		Enabled Tenant drop down menu and fixed edit option

12/20/2016 TenantsContract
		Include default Projection for each table

12/20/2016 Created RoomVacant Table to keep track of the Vacancy room
		And By choosing "List of Vacant room", it view the vacant room from TenantInfoActivity
 
12/19/2016 Bug Fixed, 4877ee380fff6de6b073b7b7ee2561dfe34d00a2
		1. From Main Activity create new Tenant by choosing one of the room.
		2. It will take to TransactionInfoActivity.
		3. From TransactionInfoActivity, create new Transaction by choosing the '+'
		4. Input new transaction, will take back to TransactionInfoActivity.
		5. Bug, it won't display the transaction info for the room.
		Miss spell the string for putExtra for Intent Object. from RoomNumber to Room_Number

12/18/2016 TransactionInfo : update with LoaderManager

12/18/2016 TransactionInfo : able to print only for the particular room
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


	   

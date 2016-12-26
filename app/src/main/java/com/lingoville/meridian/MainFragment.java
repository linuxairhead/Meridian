package com.lingoville.meridian;

import android.content.ContentValues;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.lingoville.meridian.Data.TenantsContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainFragment.class.getSimpleName();

    /* Tenant Data Loader */
    private static final int CURRENT_TENANT_LOADER = 0;

    private MainImageAdapter mImageAdapter;

    private GridView gridView;

    private View fragmentView;

    private int mCurrentRoomNumber;

    private int mNumberOfRoom;

    private static boolean mRoomInit = false;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {

        Log.d(LOG_TAG, "newInstance");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        getLoaderManager().initLoader(CURRENT_TENANT_LOADER, null, this);

        mNumberOfRoom = roomNumber.length;

         /* initRoom will only initilized once */
        if(!mRoomInit) initRoomTable();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_main, container, false);

        // initialize the grid view
        gridView = (GridView)fragmentView.findViewById(R.id.gridview);

        mImageAdapter = new MainImageAdapter(getActivity());

        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        Log.d(LOG_TAG, "onButtonPressed");

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(LOG_TAG, "onAttach");

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(LOG_TAG, "onDetach");

        mListener = null;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(LOG_TAG, "onCreateLoader");
        /*
        * This loader will execute the ContentProvider's query method on a background thread
        */
        return new android.support.v4.content.CursorLoader(getActivity(),                           // Parent activity context
                TenantsContract.TenantEntry.ROOM_CONTENT_URI,   // Provider content URI to query
                TenantsContract.TenantEntry.RoomTableProjection,       // Columns to include in the resulting Cursor
                null,                                                                                       // No selection clause
                null,                                                                                       // No selection arguments
                null);                                                                                       // Default sort order
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");

        mImageAdapter.setCursor(data);

        gridView.setAdapter(mImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + mImageAdapter.getRoomNumber(position), Toast.LENGTH_SHORT).show();
                mCurrentRoomNumber = roomNumber[position];
                /*
                 * check for whether the current room has occupied or not.
                 * if occupied, call transactionInfoActivity to view transaction history
                 * if not occupied, continue edit activity with new tenant title
                 */
                if (occupiedRoom().matches("true")) {
                    Intent transactionIntent = new Intent(getActivity(), TransactionInfoActivity.class);
                    transactionIntent.putExtra("Room_Number", mCurrentRoomNumber);
                    getActivity().finish();
                    startActivity(transactionIntent);
                } else {
                    Intent tenantIntent = new Intent(getActivity(), TenantEditActivity.class);
                    tenantIntent.putExtra("Room_Number", mCurrentRoomNumber);
                    getActivity().finish();
                    startActivity(tenantIntent);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private int[] roomNumber = {
            101, 102, 103, 104, 105,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311,
            401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
            501, 502, 503, 504
    };

    /*
    *  This should be initialized only once for entire program, even if the program restarted
    */
    private void initRoomTable() {

        Log.d(LOG_TAG, "initRoomTable");

        /* verify the room table has been initilized or not */
        String selection = TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " = ?";
        String [] selectionArgs = new String[] { Integer.toString(roomNumber[0])};
        Cursor cursor = getActivity().getContentResolver().
                query(TenantsContract.TenantEntry.ROOM_CONTENT_URI,
                            TenantsContract.TenantEntry.RoomTableProjection,
                            selection,
                            selectionArgs,
                            null );

        // if cursor return counter more then zero, the table was already initialized */
        if(cursor.getCount() != 0) {
            mRoomInit = true;
            return;
        }

        /*
         * If this program is using for the first time and db doesn't contain RoomTable,
         * following will be initialied all the room with false as COLUMN_Vancant
         */
        if( !mRoomInit ) {
            // Create the content value class by reading from user input editor
            ContentValues values = new ContentValues();

            for (int counter = 0; counter < mNumberOfRoom; counter++) {

                values.put(TenantsContract.TenantEntry.COLUMN_ROOMNUMBER, roomNumber[counter]);

                values.put(TenantsContract.TenantEntry.COLUMN_Vacancy, "false");

                getActivity().getContentResolver().insert(TenantsContract.TenantEntry.ROOM_CONTENT_URI, values);
            }
            mRoomInit = true;
        }
    }

    private String occupiedRoom() {

        Log.d(LOG_TAG, "occupiedRoom");

        String selection = TenantsContract.TenantEntry.COLUMN_ROOMNUMBER + " = ?";

        String [] selectionArgs = new String[] { Integer.toString(mCurrentRoomNumber)};

        Cursor cursor = getActivity().getContentResolver().
                query(TenantsContract.TenantEntry.ROOM_CONTENT_URI,
                            TenantsContract.TenantEntry.RoomTableProjection,
                            selection,
                            selectionArgs,
                            null );

        cursor.moveToFirst();

        // Get the tenant entry and check whether the room is occupied or not.
        return cursor.getString(cursor.getColumnIndex(TenantsContract.TenantEntry.COLUMN_Vacancy));

    }
}

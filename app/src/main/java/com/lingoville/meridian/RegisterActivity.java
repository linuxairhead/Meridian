package com.lingoville.meridian;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static com.lingoville.meridian.RegisterActivity.RegisterPropertyFragment.*;


public class RegisterActivity extends AppCompatActivity {

    public static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 3;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Button mBeforeButton;

    private Button mNextButton;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setSectionPagerAdapter(int position) {
        mViewPager.setCurrentItem(position);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterUserFragment extends Fragment {

        public static final String LOG_TAG = RegisterUserFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private Button mNextButton;

        public RegisterUserFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RegisterUserFragment newInstance(int sectionNumber) {
            RegisterUserFragment fragment = new RegisterUserFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_image_register, container, false);

            mNextButton = (Button) rootView.findViewById(R.id.register_next);
            mNextButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Next");
                    RegisterActivity ra = (RegisterActivity) getActivity();
                    ra.setSectionPagerAdapter(1);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterUserNameFragment extends Fragment {

        public static final String LOG_TAG = RegisterUserNameFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private EditText firstName;

        private EditText lastName;

        private Button mBeforeButton;

        private Button mNextButton;

        public RegisterUserNameFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RegisterUserNameFragment newInstance(int sectionNumber) {
            RegisterUserNameFragment fragment = new RegisterUserNameFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d(LOG_TAG, "onCreateView");
            rootView = inflater.inflate(R.layout.fragment_name_register, container, false);
            firstName = (EditText) rootView.findViewById(R.id.register_firstName);
            lastName = (EditText) rootView.findViewById(R.id.register_lastName);

            mBeforeButton = (Button) rootView.findViewById(R.id.name_before);
            mBeforeButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Before");
                    RegisterActivity ra = (RegisterActivity) getActivity();
                    ra.setSectionPagerAdapter(0);
                }
            });

            mNextButton = (Button) rootView.findViewById(R.id.name_next);
            mNextButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Next");
                    RegisterActivity ra = (RegisterActivity) getActivity();
                    ra.setSectionPagerAdapter(2);
                }
            });
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterUserInfoFragment extends Fragment {

        public static final String LOG_TAG = RegisterUserInfoFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private EditText mPhoneNumber;

        private EditText mEmailAddress;

        private Button mBeforeButton;

        private Button mNextButton;

        public RegisterUserInfoFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RegisterUserInfoFragment newInstance(int sectionNumber) {
            RegisterUserInfoFragment fragment = new RegisterUserInfoFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d(LOG_TAG, "onCreateView");

            rootView = inflater.inflate(R.layout.fragment_info_register, container, false);
            mPhoneNumber = (EditText) rootView.findViewById(R.id.register_phoneNumber);
            mEmailAddress = (EditText) rootView.findViewById(R.id.register_emailAddress);

            mBeforeButton = (Button) rootView.findViewById(R.id.info_before);
            mBeforeButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Before");
                    RegisterActivity ra = (RegisterActivity) getActivity();
                    ra.setSectionPagerAdapter(1);
                }
            });

            mNextButton = (Button) rootView.findViewById(R.id.info_next);
            mNextButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Next");
                    RegisterActivity ra = (RegisterActivity) getActivity();
                    ra.setSectionPagerAdapter(3);
                }
            });

            retrievePhoneNumber backGround = new retrievePhoneNumber();
            backGround.execute();

            Log.d(LOG_TAG, "onCreate Email is --->");

            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(getActivity()).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String possibleEmail = account.name;
                    mEmailAddress.setText(possibleEmail);
                    Log.d(LOG_TAG, "onCreate " + possibleEmail);
                }
            }

            return rootView;
        }

        private class retrievePhoneNumber extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... mVoid) {
                Log.d(LOG_TAG, "doInBackground");

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                        Log.d(LOG_TAG, "onCreate Phone Number Permission is reqiured");
                    }
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
                 }
                return "PERMISSION_REQUESTED";
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d(LOG_TAG, "onPostExecute");

                while (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {  }
                TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
                mPhoneNumber.setText(tMgr.getLine1Number());
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterPropertyFragment extends Fragment implements View.OnClickListener {

        public static final String LOG_TAG = RegisterPropertyFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private LinearLayout newRootView;

        private Spinner mNumFloor;

        private TextView mUnitQ;

        private ArrayList<LinearLayout> linearLayoutArray;

        private static int previousSelectedFloor = 0;

        private int currentNumFloor = 0;

        private Button mNextButton;

        private Button mBeforeButton;

        private String floor[] = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th"  };

        private Integer unit[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

        public RegisterPropertyFragment() {   }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RegisterPropertyFragment newInstance(int sectionNumber) {
            RegisterPropertyFragment fragment = new RegisterPropertyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Log.d(LOG_TAG, "onCreateView");

            /*  initialize rootView by inflate the fragment_property_register layout*/
            rootView = inflater.inflate(R.layout.fragment_property_register, container, false);

            /* newRootView will hold the all the element view and
             * will dynamically add more spinner view for floor */
            newRootView = (LinearLayout) rootView.findViewById(R.id.parentProperty);

            /* initialize mNumFloor spinner to get the number of floor from user
            *  spinnerFloorArrayAdapter will have floor number as item and it will set to mNumFloor spinner.
            * */
            mNumFloor = (Spinner) rootView.findViewById(R.id.register_numberOfFloor);
            ArrayAdapter<String> spinnerFloorArrayAdapter = new ArrayAdapter<String>(getActivity(),   android.R.layout.simple_spinner_item, floor);
            spinnerFloorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            mNumFloor.setAdapter(spinnerFloorArrayAdapter);
            linearLayoutArray = new ArrayList<LinearLayout>();
            mNumFloor.setSelection(0);
            mNumFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                /*
                 * When the user selected the nNumFloor, it will dynamically initialize floors with newFloorSpinner
                 */
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    /* Get the number of floor user selected */
                    currentNumFloor = mNumFloor.getSelectedItemPosition();

                    /* If the user changed the number of floor need to resize the view
                     *  Case (previousSelectedFloor < currentNumFloor)
                     *           user selected more unit than previous selected, then only need to add extra unit
                     *           so increase index to currentNumFloor
                     *
                     *   Case (previousSelectedFloor > currentNumFloor)
                     *           user selected less unit than previous selected, then only need to remove the extra unit
                     */
                    if( previousSelectedFloor <=currentNumFloor ) {

                        for (int index = previousSelectedFloor; index <= currentNumFloor ; index++) {
                            Log.d(LOG_TAG, "onCreateView index " + index  + " floor " + floor[index]);
                            //use linearlayout inflater to create new property_units on the fly
                            LinearLayout newFloor = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.property_units, null);

                            /*initialize newFloorText from newFloor inflater*/
                            TextView newFloorText = (TextView) newFloor.findViewById(R.id.register_floorNum);
                            newFloorText.setText("Unit for " + floor[index] + " Floor");

                            /*initialize newFloorspinner from newFloor inflater*/
                            Spinner newFloorSpinner = (Spinner) newFloor.findViewById(R.id.register_numberOfUnit);
                            // Application of the Array to the Spinner
                            ArrayAdapter<Integer> spinnerUnitArrayAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, unit);
                            spinnerUnitArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            newFloorSpinner.setAdapter(spinnerUnitArrayAdapter);

                            /* add newFloor to rootView */
                            newRootView.addView(newFloor);
                            linearLayoutArray.add(newFloor);
                        }
                    } else {
                        int index = linearLayoutArray.size()-1;

                        for ( ; index > currentNumFloor ; index-- ) {
                            LinearLayout linearView = (LinearLayout)linearLayoutArray.remove(index);
                            newRootView.removeView(linearView);
                        }
                    }
                    /* new previous selected floor
                    *  this variable will be used when the user changed num of floor*/
                    previousSelectedFloor = ++currentNumFloor;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mBeforeButton = (Button) rootView.findViewById(R.id.property_before);
            mBeforeButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Before");
                    RegisterActivity ra = (RegisterActivity) getActivity();
                    ra.setSectionPagerAdapter(2);
                }
            });

            mNextButton = (Button) rootView.findViewById(R.id.property_save);
            mNextButton.setOnClickListener( new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "onButtonPressed Next");

                }
            });
            return rootView;
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onButtonPressed");
            RegisterActivity ra = (RegisterActivity) getActivity();
            ra.setSectionPagerAdapter(0);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RegisterUserFragment.newInstance(position + 1);
                case 1:
                    return RegisterUserNameFragment.newInstance(position + 1);
                case 2:
                    return RegisterUserInfoFragment.newInstance(position + 1);
                default:
                    return RegisterPropertyFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Picture";
                case 1:
                    return "Name";
                case 2:
                    return "Contact Info";
                default:
                    return "Property";
            }
        }
    }
}

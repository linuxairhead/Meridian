package com.lingoville.meridian;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity  {

    public static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 3;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Button    nextButton;


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

    }

    private void setSectionPagerAdapter(int position) {
        mViewPager.setCurrentItem(position);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterUserFragment extends Fragment implements View.OnClickListener {

        public static final String LOG_TAG = RegisterUserFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private Button nextButton;

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

            nextButton = (Button) rootView.findViewById(R.id.register_next);
            nextButton.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onButtonPressed");
            RegisterActivity ra = (RegisterActivity) getActivity();
            ra.setSectionPagerAdapter(1);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterUserNameFragment extends Fragment implements View.OnClickListener{

        public static final String LOG_TAG = RegisterUserNameFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private EditText firstName;

        private EditText lastName;

        private Button nextButton;

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
            firstName = (EditText)rootView.findViewById(R.id.register_firstName);
            lastName = (EditText)rootView.findViewById(R.id.register_lastName);

            nextButton = (Button) rootView.findViewById(R.id.name_next);
            nextButton.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "onButtonPressed");
            RegisterActivity ra = (RegisterActivity) getActivity();
            ra.setSectionPagerAdapter(2);
        }

    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterUserInfoFragment extends Fragment implements View.OnClickListener{

        public static final String LOG_TAG = RegisterUserInfoFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private EditText phoneNumber;

        private EditText emailAddress;

        private Button nextButton;

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
/*
            Log.d(LOG_TAG, "onCreate Email is --->" );

            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(getActivity()).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String possibleEmail = account.name;
                    Log.d(LOG_TAG, "onCreate " + possibleEmail);
                }
            }

            Log.d(LOG_TAG, "onCreate Phone Number is --->");

            if( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                do {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
                }while ( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED);
            } else {
                TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                Log.d(LOG_TAG, "onCreate Phone Number " + mPhoneNumber);
            }
            */
            rootView = inflater.inflate(R.layout.fragment_info_register, container, false);
            phoneNumber = (EditText) rootView.findViewById(R.id.register_phoneNumber);
            emailAddress = (EditText) rootView.findViewById(R.id.register_emailAddress);

            nextButton = (Button) rootView.findViewById(R.id.info_next);
            nextButton.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
        Log.d(LOG_TAG, "onButtonPressed");
            RegisterActivity ra = (RegisterActivity) getActivity();
            ra.setSectionPagerAdapter(3);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RegisterPropertyFragment extends Fragment implements View.OnClickListener{

        public static final String LOG_TAG = RegisterPropertyFragment.class.getSimpleName();

        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        private Button nextButton;

        public RegisterPropertyFragment() {
        }

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
            rootView = inflater.inflate(R.layout.fragment_property_fragment, container, false);

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

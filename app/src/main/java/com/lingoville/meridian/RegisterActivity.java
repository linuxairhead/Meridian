package com.lingoville.meridian;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    public static final String LOG_TAG = RegisterActivity.class.getSimpleName();

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
            rootView = inflater.inflate(R.layout.fragment_register, container, false);

            nextButton = (Button) rootView.findViewById(R.id.registerNext);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

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
            rootView = inflater.inflate(R.layout.fragment_register2, container, false);
            //nextButton = (Button) rootView.findViewById(R.id.registerNext);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            //nextButton.setOnClickListener(this);
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
                default:
                    return RegisterPropertyFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}

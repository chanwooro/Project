package com.bowlingclub;

import java.util.Arrays;
import java.util.List;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class CopyOfTeamView extends FragmentActivity {
	//To create horizontal Swipe fragments
    static MyAdapter mAdapter;
    static ViewPager mPager;
    private static int totalpage = 0; 
    
    private XYPlot plot;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teamview);
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        Intent intent = getIntent();
        totalpage = Integer.parseInt(intent.getExtras().get("Pages").toString());

        mPager = (ViewPager)findViewById(R.id.pager);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        
        mPager.setAdapter(mAdapter);

    }

    public static class MyAdapter extends FragmentStatePagerAdapter {
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}
		
		@Override
		public int getCount() {
			//Need a fix
			return totalpage;
		}
		
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int i) {
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment = new PageFragment();
			Bundle args = new Bundle();
			args.putInt(PageFragment.ARG_PAGE_NUMBER, i + 1);
			fragment.setArguments(args);
			return fragment;
		}	
	}

    
    public static class PageFragment  extends Fragment {
    	// The fragment argument representing the section number for this fragment.
    	public static final String ARG_PAGE_NUMBER = "section_number";

    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View rootView = inflater.inflate(R.layout.page, container, false);
    		TextView PageNumber = (TextView) rootView.findViewById(R.id.page_number);
    		TextView PageContent = (TextView) rootView.findViewById(R.id.page_content);
    	    		Bundle args = getArguments();


    		
    		PageContent.setText("Hello world");
    		
    		if (args.getInt(ARG_PAGE_NUMBER) == 1)
    		{
    			PageNumber.setText("OVERVIEW");	
    		}
    		else
    		{
    			PageNumber.setText("TEAM " + Integer.toString(args.getInt(ARG_PAGE_NUMBER)-1) + " out of " + (totalpage-1));	
    		}
	
			return rootView;
    	}
    }
}


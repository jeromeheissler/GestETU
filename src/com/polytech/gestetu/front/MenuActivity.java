package com.polytech.gestetu.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.polytech.gestetu.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends FragmentActivity implements
ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";


	public final static String ITEM_TITLE = "title";  
	public final static String ITEM_CAPTION = "caption";
	public static Map<String,?> createItem(String title, String caption) {  
		Map<String,String> item = new HashMap<String,String>();  
		item.put(ITEM_TITLE, title);  
		item.put(ITEM_CAPTION, caption);  
		return item;  
	} 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_section_promotion)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_section_students)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_section_mark)
				.setTabListener(this));

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
				tab.getPosition() + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.container, fragment).commit();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		ListView lvListe;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{

			switch (Integer.parseInt(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))))
			{
			case 1:{
				ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.menu, container, false);

				lvListe =  (ListView) rootView.findViewById(R.id.listView);

				/* List<Map<String,?>> security = new LinkedList<Map<String,?>>();  
		           security.add((createItem("titre 1 ", "sous titre du titre1"));  
		           security.add(createItem("titre 2", "Sous titre du titre 2"));  
		           security.add(createItem("Titre 3", "sous titre du titre 3 un peu lon pour avoir un retour à la ligne"));
				 */  

				List<Map<String,?>> security = new LinkedList<Map<String,?>>();  
				security.add(createItem("DI", "")); 
				security.add(createItem("DA", ""));  
				security.add(createItem("DP", ""));			            

				// creation de nom objet de type ListSeparer 
				ListSeparer adapter = new ListSeparer(this.getActivity().getBaseContext());  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2015", new SimpleAdapter(this.getActivity().getBaseContext(), security, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2014", new SimpleAdapter(this.getActivity().getBaseContext(), security, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2013", new SimpleAdapter(this.getActivity().getBaseContext(), security, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2012", new SimpleAdapter(this.getActivity().getBaseContext(), security, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				lvListe.setAdapter(adapter);

				lvListe.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						Toast toast = Toast.makeText(view.getContext(), "test", Toast.LENGTH_SHORT);
						toast.show();
						Log.e("onItemClick ", "onItemClick ");
					}
				});

				return rootView;
			}
			case 2:{
				ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.menu, container, false);

				lvListe = (ListView) rootView.findViewById(R.id.listView);
				return rootView;
			}             
			case 3:{
				ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.menu, container, false);

				lvListe = (ListView) rootView.findViewById(R.id.listView);
				return rootView;
			}

			}
			return container;
		}
	}
}
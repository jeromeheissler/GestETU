package com.polytech.gestetu.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.R;
import com.polytech.gestetu.models.Promotion;
import com.polytech.gestetu.models.Student;
import com.polytech.gestetu.services.RetrievesPromotionsService;
import com.polytech.gestetu.services.RetrievesStudentsService;
import com.polytech.gestetu.services.WebServiceRestClient;
import com.polytech.gestetu.services.WebServiceRestClient.RequestMethod;

import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

public class MenuActivity extends FragmentActivity implements
ActionBar.TabListener, SearchView.OnQueryTextListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	//déclaration des variables globales
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private SearchView mSearchView;
	private static TextView mStatusView;
	private static final String TAG = "MenuActivity";
	private static ProgressDialog mDialog; 
	private static List<Promotion> promotions = null;
	private static List<Student> students = null;
	public static MenuActivity menuActivity;
	private MenuItem menuItem;

	public final static String ITEM_TITLE = "title";  
	public final static String ITEM_CAPTION = "caption";
	public final static String ITEM_CAPTION2 = "caption2";
	public static Map<String,?> createItem(String title, String caption) {  
		Map<String,String> item = new HashMap<String,String>();  
		item.put(ITEM_TITLE, title);  
		item.put(ITEM_CAPTION, caption);  
		return item;  
	} 

	public static Map<String,?> createItem(String title, String caption, String caption2) {  
		Map<String,String> item = new HashMap<String,String>();  
		item.put(ITEM_TITLE, title);  
		item.put(ITEM_CAPTION, caption); 
		item.put(ITEM_CAPTION2, caption2);
		return item;  
	} 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu);

		menuActivity = this;

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

		//mStatusView =  (TextView) findViewById(R.id.status_text);

		/*// Get a reference to the AutoCompleteTextView in the layout
		AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
	    autoCompleteTextView.setThreshold(1);
		// Get the string array
		String[] countries = getResources().getStringArray(R.array.countries_array);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
		autoCompleteTextView.setAdapter(adapter);*/
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
		//getMenuInflater().inflate(R.menu.activity_main, menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_load:
			menuItem = item;
			menuItem.setActionView(R.layout.progressbar);
			menuItem.expandActionView();
			TestTask task = new TestTask();
			task.execute("test");
			break;

		case R.id.sortByLastName:  
			if (GlobalVars.getSortByLastName() == false)
			{
				GlobalVars.setSortByLastName(true);
			}

			if (GlobalVars.getSortByFirstName() == true)
			{
				GlobalVars.setSortByFirstName(false);
			}
			break;
			
		case R.id.sortByFirstName:  
			if (GlobalVars.getSortByLastName() == true)
			{
				GlobalVars.setSortByLastName(false);
			}

			if (GlobalVars.getSortByFirstName() == false)
			{
				GlobalVars.setSortByFirstName(true);
			}
			break;

		default:
			break;
		}
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
		AutoCompleteTextView autoCompleteTextView;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
				Bundle savedInstanceState) 
		{

			switch (Integer.parseInt(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))))
			{
			case 1:{
				promotions = new ArrayList<Promotion>();

				//affichage d'une progressDialog
				mDialog = new ProgressDialog(this.getActivity());
				mDialog.setMessage("Patientez s'il-vous-plait...");
				mDialog.setCancelable(false);
				mDialog.show();

				//Initialize thread class
				RetrievesPromotionsService retrievesPromotionsService = new RetrievesPromotionsService();
				//Start thread
				retrievesPromotionsService.start();

				//Step 4: Call join() to wait until fib thread finishes
				try{
					retrievesPromotionsService.join();
				}catch(InterruptedException ie){}

				int mHandlerMessage = retrievesPromotionsService.getMHandler();

				if (mHandlerMessage != 0)
				{
					mHandler.sendEmptyMessage(mHandlerMessage);
				}

				//Step 5: call getRetrievesPromotions() to get the return value
				promotions = retrievesPromotionsService.getRetrievesPromotions();

				Log.d(TAG, "Promotions size = " + promotions.size());

				ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.menu, container, false);

				ListView lvListe =  (ListView) rootView.findViewById(R.id.listView);

				// creation de nom objet de type ListSeparer 
				ListSeparer adapter = new ListSeparer(GlobalVars.getAppContext());  

				if(promotions.size() > 0)
				{
					List<Integer> promotionYear = new ArrayList<Integer>();
					for(int indexPromotion = 0; indexPromotion < promotions.size(); indexPromotion++)
					{
						if (!promotionYear.contains(promotions.get(indexPromotion).getAnnee()))
						{
							promotionYear.add(promotions.get(indexPromotion).getAnnee());
						}
					}

					for(int indexPromotionYear = 0; indexPromotionYear < promotionYear.size(); indexPromotionYear++)
					{
						List<Map<String,?>> promotionData = new LinkedList<Map<String,?>>();  

						for(int indexPromotion = 0; indexPromotion < promotions.size(); indexPromotion++)
						{
							if (promotionYear.get(indexPromotionYear) == promotions.get(indexPromotion).getAnnee())
							{
								promotionData.add(createItem(promotions.get(indexPromotion).getLabel(), promotionYear.get(indexPromotionYear).toString()));  

								//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
								adapter.addSection(promotionYear.get(indexPromotionYear).toString(), new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
										new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

							}
						}
					}
				}
				else
				{
					List<Map<String,?>> promotionData = new LinkedList<Map<String,?>>();  
					promotionData.add(createItem("There is no promotion", "")); 

					//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
					adapter.addSection("Information", new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
							new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
				}

				lvListe.setAdapter(adapter);

				if(promotions.size() > 0)
				{
					lvListe.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

							Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
									+ " item : " + parent.getAdapter().getItem(position)
									+ " nb item in list : " + parent.getAdapter().getCount()
									, Toast.LENGTH_SHORT);
							toast.show();
							Log.e(TAG, "onItemClick item : " + parent.getAdapter().getItem(position));
						}
					});
				}
				else
				{
					// proposer l'ajout d'une promotion ??

				}
				/*List<Map<String,?>> promotionData = new LinkedList<Map<String,?>>();  
				promotionData.add(createItem(promotions.get(0).getId(), "")); 
				promotionData.add(createItem("DA", ""));  
				promotionData.add(createItem("DP", ""));	

				// creation de nom objet de type ListSeparer 
				ListSeparer adapter = new ListSeparer(GlobalVars.getAppContext());  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2015", new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2014", new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2013", new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("2012", new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				lvListe.setAdapter(adapter);

				lvListe.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
								+ " item : " + parent.getAdapter().getItem(position)
								+ " nb item in list : " + parent.getAdapter().getCount()
								, Toast.LENGTH_SHORT);
						toast.show();
						Log.e("onItemClick ", "onItemClick ");
					}
				});*/

				return rootView;
			}
			case 2:{
				boolean sortByLastname = GlobalVars.getSortByLastName(); //nom
				boolean sortByFirstname = GlobalVars.getSortByFirstName(); //prenom

				students = new ArrayList<Student>();

				//affichage d'une progressDialog
				mDialog = new ProgressDialog(this.getActivity());
				mDialog.setMessage("Patientez s'il-vous-plait...");
				mDialog.setCancelable(false);
				mDialog.show();

				//Initialize thread class
				RetrievesStudentsService retrievesStudentsService = new RetrievesStudentsService();
				//Start thread
				retrievesStudentsService.start();

				//Step 4: Call join() to wait until fib thread finishes
				try{
					retrievesStudentsService.join();
				}catch(InterruptedException ie){}

				int mHandlerMessage = retrievesStudentsService.getMHandler();

				if (mHandlerMessage != 0)
				{
					mHandler.sendEmptyMessage(mHandlerMessage);
				}

				//Step 5: call getRetrievesStudents() to get the return value
				students = retrievesStudentsService.getRetrievesStudents();

				Log.d(TAG, "Students size = " + students.size());

				ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.menu, container, false);

				lvListe =  (ListView) rootView.findViewById(R.id.listView);

				// creation de nom objet de type ListSeparer 
				ListSeparer adapter = new ListSeparer(GlobalVars.getAppContext());  

				if(students.size() > 0)
				{
					List<String> studentAlphabet = new ArrayList<String>();

					if (sortByLastname == true)
					{
						for(int indexStudent = 0; indexStudent < students.size(); indexStudent++)
						{
							String letter = students.get(indexStudent).getLastname().substring(0, 1);
							if (!studentAlphabet.contains(letter))
							{
								studentAlphabet.add(letter);
							}
						}

						for(int indexLetter = 0; indexLetter < studentAlphabet.size(); indexLetter++)
						{
							List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  

							for(int indexStudent = 0; indexStudent < students.size(); indexStudent++)
							{				
								if (studentAlphabet.get(indexLetter).equals(students.get(indexStudent).getLastname().substring(0, 1)))
								{
									studentData.add(createItem(students.get(indexStudent).getLastname() + " " + students.get(indexStudent).getFirstname(), 
											students.get(indexStudent).getNumStu()));  

									//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
									adapter.addSection(studentAlphabet.get(indexLetter), new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.student_list_item,  
											new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
								}
							}
						}
					}
					else if (sortByFirstname == true)
					{
						for(int indexStudent = 0; indexStudent < students.size(); indexStudent++)
						{
							String letter = students.get(indexStudent).getFirstname().substring(0, 1);
							if (!studentAlphabet.contains(letter))
							{
								studentAlphabet.add(letter);
							}
						}

						for(int indexLetter = 0; indexLetter < studentAlphabet.size(); indexLetter++)
						{
							List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  

							for(int indexStudent = 0; indexStudent < students.size(); indexStudent++)
							{				
								if (studentAlphabet.get(indexLetter).equals(students.get(indexStudent).getFirstname().substring(0, 1)))
								{
									studentData.add(createItem(students.get(indexStudent).getFirstname() + " " + students.get(indexStudent).getLastname(), 
											students.get(indexStudent).getNumStu()));  

									//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
									adapter.addSection(studentAlphabet.get(indexLetter), new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.student_list_item,  
											new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
								}
							}
						}
					}
				}
				else
				{
					List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  
					studentData.add(createItem("There is no student", "")); 

					//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
					adapter.addSection("Information", new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.promotion_list_item,  
							new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
				}

				lvListe.setAdapter(adapter);

				if(students.size() > 0)
				{
					lvListe.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

												/*Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
								+ " item : " + parent.getAdapter().getItem(position)
								+ " nb item in list : " + parent.getAdapter().getCount()
								, Toast.LENGTH_SHORT);
						toast.show();
						Log.e("onItemClick ", "onItemClick ");*/
							
							Log.d(TAG,  parent.getAdapter().getItem(position).toString());
							String [] splitItem = parent.getAdapter().getItem(position).toString().split(",");
							String [] splitCaption = splitItem[0].split("=");
							String [] splitTitle = splitItem[1].split("=");
							
							String numSTU = splitCaption[1];
							String studentName = splitTitle[1].substring(0, splitTitle[1].length()-1);
							Log.d(TAG,  numSTU);
							Log.d(TAG,  studentName);
							
							int indexStudent = 0;
							boolean studentFind = false;
							Student student = null;
							while(indexStudent < students.size() && studentFind == false)
							{
								//Log.d(TAG, students.get(indexStudent).getNumStu() + " | " + numSTU);
								
								if (students.get(indexStudent).getNumStu().equals(numSTU))
								{
									student = students.get(indexStudent);
									studentFind = true;
								}
								
								indexStudent++;
							}
							
							Log.d(TAG, "studentFind : " + studentFind);
							
							// creation de nom objet de type ListSeparer 
							ListSeparer adapter2 = new ListSeparer(parent.getContext()); 
										
							List<Map<String,?>> Information = new LinkedList<Map<String,?>>();  
							Information.add(createItem(studentName, numSTU, "Promotion " + student.getPromotion().getAnnee()));
							
							//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
							adapter2.addSection("Information", new SimpleAdapter(parent.getContext(), Information, R.layout.student_list_item_click1,  
									new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 }));  

							
							List<Map<String,?>> Marks = new LinkedList<Map<String,?>>(); 
									
							if (student.getLstTest().size() > 0)
							{
								for (int indexTest = 0; indexTest < student.getLstTest().size(); indexTest++)
								{						 
									Marks.add(createItem(student.getLstTest().get(indexTest).getSubject().getName(), Float.toString(student.getLstTest().get(indexTest).getNote())));
								}
								
	
								//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
								adapter2.addSection("Marks", new SimpleAdapter(parent.getContext(), Marks, R.layout.student_list_item_click2,  
										new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 })); 

							}
							else
							{
								List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  
								studentData.add(createItem("There is no mark", "")); 

								//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
								adapter2.addSection("Marks", new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.promotion_list_item,  
										new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
							}
							
							/*List<Map<String,?>> Information = new LinkedList<Map<String,?>>();  
							Information.add(createItem("Almeras Antoine", "21004735", "Promotion 2013"));  	

							List<Map<String,?>> Marks = new LinkedList<Map<String,?>>();  
							Marks.add(createItem("RF1", "12,5"));
							Marks.add(createItem("Analyse de gros volume de données", "12,5"));*/ 

							lvListe.setAdapter(adapter2);

							//lvListe.setClickable(false);
							lvListe.setEnabled(false);
							//lvListe.setSelector(android.R.color.transparent); 

						}
					});
				}
				else
				{
					// proposer l'ajout d'un étudiant ??			
				}


				/*List<Map<String,?>> AData = new LinkedList<Map<String,?>>();  
				AData.add(createItem("Almeras Antoine", "21004735")); 

				List<Map<String,?>> HData = new LinkedList<Map<String,?>>();  
				HData.add(createItem("Heissler Jérôme", "21004735"));  
				HData.add(createItem("Havard Thibaud", "XXXXXXXX"));	

				List<Map<String,?>> NData = new LinkedList<Map<String,?>>();  
				NData.add(createItem("Rosado Nicolas", "21004735"));

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("A", new SimpleAdapter(this.getActivity().getBaseContext(), AData, R.layout.student_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("H", new SimpleAdapter(this.getActivity().getBaseContext(), HData, R.layout.student_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("R", new SimpleAdapter(this.getActivity().getBaseContext(), NData, R.layout.student_list_item,  
						new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));   
				 */

				return rootView;
			}             
			case 3:{

				ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.takemark, container, false);	

				// Get a reference to the AutoCompleteTextView in the layout
				AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.autocomplete_country);
				autoCompleteTextView.setThreshold(1);
				// Get the string array
				String[] countries = getResources().getStringArray(R.array.countries_array);
				// Create the adapter and set it to the AutoCompleteTextView 
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity().getBaseContext(), android.R.layout.simple_list_item_1, countries);
				autoCompleteTextView.setAdapter(adapter);

				autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(arg0.getContext(),(CharSequence)arg0.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
					}
				});

				/*ViewGroup rootView = (ViewGroup) inflater.inflate(
						R.layout.menu, container, false);		

				lvListe =  (ListView) rootView.findViewById(R.id.listView);

				List<Map<String,?>> StudentNumber = new LinkedList<Map<String,?>>();  
				StudentNumber.add(createItem("Typing some texts", ""));  	

				List<Map<String,?>> Mark = new LinkedList<Map<String,?>>();  
				Mark.add(createItem("12,5", ""));

				List<Map<String,?>> Matter = new LinkedList<Map<String,?>>();  
				Matter.add(createItem("Option", ""));

				// creation de nom objet de type ListSeparer 
				ListSeparer adapter = new ListSeparer(this.getActivity().getBaseContext());  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("StudentNumber", new SimpleAdapter(this.getActivity().getBaseContext(), StudentNumber, R.layout.takemark,  
						new String[] { ITEM_TITLE }, new int[] { R.id.autocomplete_country}));  

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("Marks", new SimpleAdapter(this.getActivity().getBaseContext(), Mark, R.layout.takemark,  
						new String[] { ITEM_TITLE }, new int[] { R.id.autocomplete_country})); 

				//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
				adapter.addSection("Matter", new SimpleAdapter(this.getActivity().getBaseContext(), Matter, R.layout.takemark,  
						new String[] { ITEM_TITLE }, new int[] { R.id.autocomplete_country})); 

				lvListe.setAdapter(adapter);

				lvListe.setClickable(false);
				lvListe.setSelector(android.R.color.transparent); */

				return rootView;
			}

			}
			return container;
		}
	}

	private void setupSearchView(MenuItem searchItem) {

		if (isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
		} else {
			searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

			SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
			for (SearchableInfo inf : searchables) {
				if (inf.getSuggestAuthority() != null
						&& inf.getSuggestAuthority().startsWith("applications")) {
					info = inf;
				}
			}
			mSearchView.setSearchableInfo(info);
		}

		mSearchView.setOnQueryTextListener(this);
	}

	public boolean onQueryTextChange(String newText) {
		mStatusView.setText("Query = " + newText);

		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		mStatusView.setText("Query = " + query + " : submitted");
		return false;
	}

	public boolean onClose() {
		mStatusView.setText("Closed!");
		return false;
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

	/** 
	 * Méthode pour la progressDialog
	 */
	static Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);

			//selon le message reçu par le thread, on ferme la progress bar et on affiche l'erreur appropriée
			switch (msg.what)
			{
			case 10:
				mDialog.dismiss();
				break;

			case 20:
				mDialog.dismiss();
				//on affiche dans un Toast que le login a bien réussi
				Toast.makeText(GlobalVars.getAppContext(), "Retrieval of promotions successful", Toast.LENGTH_SHORT).show();
				break;

			case 21:
				mDialog.dismiss();
				//on affiche dans un Toast que le login a bien réussi
				Toast.makeText(GlobalVars.getAppContext(), "Retrieval of students successful ", Toast.LENGTH_SHORT).show();
				break;

			case 30:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbPost = new AlertDialog.Builder(menuActivity);
				adbPost.setTitle("Error");
				adbPost.setMessage("Informations d'identification incorrectes. \nMerci de vérifier votre identifiant et votre mot de passe.\n\nEn cas de problème persistant, contacter votre administrateur.");
				adbPost.setPositiveButton("Ok", null);
				adbPost.show();
				break;

			case 40:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbPromotionsNetwork = new AlertDialog.Builder(menuActivity);
				adbPromotionsNetwork.setTitle("Error");
				adbPromotionsNetwork.setMessage("Retrieval of promotions impossible, following a network problem.\nThank you for your connectivity check.");
				adbPromotionsNetwork.setPositiveButton("Ok", null);
				adbPromotionsNetwork.show();
				break;

			case 41:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbStudentsNetwork = new AlertDialog.Builder(menuActivity);
				adbStudentsNetwork.setTitle("Error");
				adbStudentsNetwork.setMessage("Retrieval of students impossible, following a network problem.\nThank you for your connectivity check.");
				adbStudentsNetwork.setPositiveButton("Ok", null);
				adbStudentsNetwork.show();
				break;

			case 50:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbRetrievesPromotions = new AlertDialog.Builder(menuActivity);
				adbRetrievesPromotions.setTitle("Error");
				adbRetrievesPromotions.setMessage("Problem occurred when retrieving promotions.\n\nIf the problem persists, contact your administrator.");
				adbRetrievesPromotions.setPositiveButton("Ok", null);
				adbRetrievesPromotions.show();
				break;

			case 51:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbRetrievesStudents = new AlertDialog.Builder(menuActivity);
				adbRetrievesStudents.setTitle("Error");
				adbRetrievesStudents.setMessage("Problem occurred when retrieving students.\n\nIf the problem persists, contact your administrator.");
				adbRetrievesStudents.setPositiveButton("Ok", null);
				adbRetrievesStudents.show();
				break;
			}
		}
	};

	/*public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {

        	//ferme l'activity en cours (login) et ouvre le menu
			finish();	

			Intent menu = new Intent(getBaseContext(), MenuActivity.class);					
			startActivity(menu);

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }*/

	private class TestTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulate something long running
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			menuItem.collapseActionView();
			menuItem.setActionView(null);
		}
	};
}


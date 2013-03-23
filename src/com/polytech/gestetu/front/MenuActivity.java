package com.polytech.gestetu.front;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.R;
import com.polytech.gestetu.models.Promotion;
import com.polytech.gestetu.models.Student;
import com.polytech.gestetu.services.ComparableAlphabet;
import com.polytech.gestetu.services.RetrievesPromotionsService;
import com.polytech.gestetu.services.RetrievesStudentsInPromotionService;
import com.polytech.gestetu.services.RetrievesStudentsService;

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
	public final static String ITEM_CAPTION3 = "caption3";
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
	
	public static Map<String,?> createItem(String title, String caption, String caption2, String caption3) {  
		Map<String,String> item = new HashMap<String,String>();  
		item.put(ITEM_TITLE, title);  
		item.put(ITEM_CAPTION, caption); 
		item.put(ITEM_CAPTION2, caption2);
		item.put(ITEM_CAPTION3, caption3);
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
		
		if (GlobalVars.getType().equals("teacher"))
		{
			actionBar.addTab(actionBar.newTab().setText(R.string.tab_section_mark)
				.setTabListener(this));
		}
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
		//MenuItem searchItem = menu.findItem(R.id.action_search);
		//mSearchView = (SearchView) searchItem.getActionView();
		//setupSearchView(searchItem);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			menuItem = item;
			menuItem.setActionView(R.layout.progressbar);
			menuItem.expandActionView();
			RefreshTask refreshTask = new RefreshTask();
			refreshTask.execute("refresh");
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
			
		/*case R.id.edit_student:  
			//création de notre item  
			Intent editStudentActivity = new Intent(this.getBaseContext(), EditStudentActivity.class);
             // on appelle notre activité
			startActivity(editStudentActivity);
		break;*/	
		
		case R.id.add_student:  
			//création de notre item  
			Intent addStudentActivity = new Intent(this.getBaseContext(), AddStudentActivity.class);
             // on appelle notre activité
			startActivity(addStudentActivity);
		break;	
			
		case R.id.delete_student:  
			
		break;	
		
		case R.id.close_app:  
				finish();
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

				lvListe =  (ListView) rootView.findViewById(R.id.listView);

				// creation de nom objet de type ListSeparer 
				ListSeparer adapter = new ListSeparer(GlobalVars.getAppContext());  

				if(promotions.size() > 0)
				{
					List<Integer> promotionsYear = new ArrayList<Integer>();
					for(int indexPromotion = 0; indexPromotion < promotions.size(); indexPromotion++)
					{
						if (!promotionsYear.contains(promotions.get(indexPromotion).getAnnee()))
						{
							promotionsYear.add(promotions.get(indexPromotion).getAnnee());
						}
					}

					for(int indexPromotionYear = 0; indexPromotionYear < promotionsYear.size(); indexPromotionYear++)
					{
						List<Map<String,?>> promotionData = new LinkedList<Map<String,?>>();  

						for(int indexPromotion = 0; indexPromotion < promotions.size(); indexPromotion++)
						{
							if (promotionsYear.get(indexPromotionYear) == promotions.get(indexPromotion).getAnnee())
							{
								//promotionData.add(createItem(promotions.get(indexPromotion).getLabel(), promotionYear.get(indexPromotionYear).toString()));  
								promotionData.add(createItem(promotions.get(indexPromotion).getLabel(), promotions.get(indexPromotion).getId()));  
							}
						}
						
						//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
						adapter.addSection(promotionsYear.get(indexPromotionYear).toString(), new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
								new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
					}
				}
				else
				{
					List<Map<String,?>> promotionData = new LinkedList<Map<String,?>>();  
					promotionData.add(createItem("There is no promotion", "")); 

					//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
					adapter.addSection("Information", new SimpleAdapter(GlobalVars.getAppContext(), promotionData, R.layout.promotion_list_item,  
							new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
					
					//lvListe.setClickable(false);
					lvListe.setEnabled(false);
					//lvListe.setSelector(android.R.color.transparent);
				}

				lvListe.setAdapter(adapter);

				if(promotions.size() > 0)
				{
					lvListe.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

							/*Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
									+ " item : " + parent.getAdapter().getItem(position)
									+ " nb item in list : " + parent.getAdapter().getCount()
									, Toast.LENGTH_SHORT);
							toast.show();
							Log.e(TAG, "onItemClick item : " + parent.getAdapter().getItem(position));*/
							
							Log.d(TAG,  parent.getAdapter().getItem(position).toString());
							String [] splitItem = parent.getAdapter().getItem(position).toString().split(",");
							String [] splitCaption = splitItem[0].split("=");
							String [] splitTitle = splitItem[1].split("=");

							String idPromo = splitCaption[1];
							String promoName = splitTitle[1].substring(0, splitTitle[1].length()-1);
							Log.d(TAG, idPromo);
							Log.d(TAG, promoName);
							
							// récupérer la liste des etudiants pour cette promo (api/promo/id)
							// après avoir parser le fichier json afficher ensuite cette liste
							
							//Initialize thread class
							RetrievesStudentsInPromotionService retrievesStudentsInPromotionService = new RetrievesStudentsInPromotionService(idPromo);
							//Start thread
							retrievesStudentsInPromotionService.start();

							//Step 4: Call join() to wait until fib thread finishes
							try{
								retrievesStudentsInPromotionService.join();
							}catch(InterruptedException ie){}

							int mHandlerMessage = retrievesStudentsInPromotionService.getMHandler();

							if (mHandlerMessage != 0)
							{
								mHandler.sendEmptyMessage(mHandlerMessage);
							}

							//Step 5: call getRetrievesPromotions() to get the return value
							ArrayList<Student> studentsInPromotionService = retrievesStudentsInPromotionService.getRetrievesStudentsInPromotion();

							Log.d(TAG, "studentsInPromotionService size = " + studentsInPromotionService.size()); 
							
							// creation de nom objet de type ListSeparer 
							ListSeparer adapter2 = new ListSeparer(parent.getContext()); 

							if(studentsInPromotionService.size() > 0)
							{
								boolean sortByLastname = GlobalVars.getSortByLastName(); //nom
								boolean sortByFirstname = GlobalVars.getSortByFirstName(); //prenom
								
								List<String> studentAlphabet = new ArrayList<String>();

								if (sortByLastname == true)
								{
									for(int indexStudent = 0; indexStudent < studentsInPromotionService.size(); indexStudent++)
									{
										String letter = studentsInPromotionService.get(indexStudent).getLastname().substring(0, 1);
										if (!studentAlphabet.contains(letter))
										{
											studentAlphabet.add(letter);
										}
									}
									
									//SortABC
									ComparableAlphabet comparableAlphabet = new ComparableAlphabet();
								    Collections.sort(studentAlphabet, comparableAlphabet);
									
									for(int indexLetter = 0; indexLetter < studentAlphabet.size(); indexLetter++)
									{
										List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  

										for(int indexStudent = 0; indexStudent < studentsInPromotionService.size(); indexStudent++)
										{				
											/*String lastName = students.get(indexStudent).getLastname();
											String currentLetter = studentAlphabet.get(indexLetter);
											String currentFirstLetterSTU = students.get(indexStudent).getLastname().substring(0, 1);*/

											if (studentAlphabet.get(indexLetter).equals(studentsInPromotionService.get(indexStudent).getLastname().substring(0, 1)))
											{						
												studentData.add(createItem(studentsInPromotionService.get(indexStudent).getLastname() + " " + studentsInPromotionService.get(indexStudent).getFirstname(), 
														studentsInPromotionService.get(indexStudent).getNumStu(), studentsInPromotionService.get(indexStudent).getId()));
											}
										}

										//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
										adapter2.addSection(studentAlphabet.get(indexLetter), new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.student_list_item,  
												new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 }));  
									}
								}
								else if (sortByFirstname == true)
								{
									for(int indexStudent = 0; indexStudent < studentsInPromotionService.size(); indexStudent++)
									{
										String letter = studentsInPromotionService.get(indexStudent).getFirstname().substring(0, 1);
										if (!studentAlphabet.contains(letter))
										{
											studentAlphabet.add(letter);
										}
									}

									//SortABC
									ComparableAlphabet comparableAlphabet = new ComparableAlphabet();
								    Collections.sort(studentAlphabet, comparableAlphabet);
									
									for(int indexLetter = 0; indexLetter < studentAlphabet.size(); indexLetter++)
									{
										List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  

										for(int indexStudent = 0; indexStudent < studentsInPromotionService.size(); indexStudent++)
										{				
											/*String lastName = students.get(indexStudent).getFirstname();
											String currentLetter = studentAlphabet.get(indexLetter);
											String currentFirstLetterSTU = students.get(indexStudent).getFirstname().substring(0, 1);*/

											if (studentAlphabet.get(indexLetter).equals(studentsInPromotionService.get(indexStudent).getFirstname().substring(0, 1)))
											{
												studentData.add(createItem(studentsInPromotionService.get(indexStudent).getFirstname() + " " + studentsInPromotionService.get(indexStudent).getLastname(), 
														studentsInPromotionService.get(indexStudent).getNumStu(), studentsInPromotionService.get(indexStudent).getId()));  
											}
										}

										//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
										adapter2.addSection(studentAlphabet.get(indexLetter), new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.student_list_item,  
												new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 }));  
									}
								}
							}
							else
							{
								List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  
								studentData.add(createItem("There is no student", "")); 

								//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
								adapter2.addSection("Information", new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.promotion_list_item,  
										new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 })); 
								
								//lvListe.setClickable(false);
								lvListe.setEnabled(false);
								//lvListe.setSelector(android.R.color.transparent);
							}

							lvListe.setAdapter(adapter2);
							
							lvListe.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

									/*Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
										+ " item : " + parent.getAdapter().getItem(position)
										+ " nb item in list : " + parent.getAdapter().getCount()
										, Toast.LENGTH_SHORT);
								toast.show();
								Log.e("onItemClick ", "onItemClick ");*/
									
									students = new ArrayList<Student>();
									
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

									Log.d(TAG,  parent.getAdapter().getItem(position).toString());
									String [] splitItem = parent.getAdapter().getItem(position).toString().split(",");
									String [] splitCaption2 = splitItem[0].split("=");
									String [] splitCaption = splitItem[1].split("=");
									String [] splitTitle = splitItem[2].split("=");

									String idSTU = splitCaption2[1];
									String numSTU = splitCaption[1];
									String studentName = splitTitle[1].substring(0, splitTitle[1].length()-1);
									Log.d(TAG, idSTU);
									Log.d(TAG, numSTU);
									Log.d(TAG, studentName);

									int indexStudent = 0;
									boolean studentFind = false;
									Student student = null;
									while(indexStudent < students.size() && studentFind == false)
									{
										//Log.d(TAG, students.get(indexStudent).getNumStu() + " | " + numSTU);

										if (students.get(indexStudent).getId().equals(idSTU))
										{
											student = students.get(indexStudent);
											studentFind = true;
										}

										indexStudent++;
									}

									Log.d(TAG, "studentFind : " + studentFind);

									// creation de nom objet de type ListSeparer 
									ListSeparer adapter3 = new ListSeparer(parent.getContext()); 

									List<Map<String,?>> Information = new LinkedList<Map<String,?>>();  
									Information.add(createItem(studentName, numSTU, "Promotion " + student.getPromotion().getLabel() + " " + student.getPromotion().getAnnee(), student.getId()));

									//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
									adapter3.addSection("Information", new SimpleAdapter(parent.getContext(), Information, R.layout.student_list_item_click1,  
											new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 }));  
										 
									List<Map<String,?>> Marks = new LinkedList<Map<String,?>>(); 

									if (student.getLstTest().size() > 0)
									{
										for (int indexTest = 0; indexTest < student.getLstTest().size(); indexTest++)
										{						 
											Marks.add(createItem(student.getLstTest().get(indexTest).getSubject().getName(), Float.toString(student.getLstTest().get(indexTest).getNote())));
										}


										//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
										adapter3.addSection("Marks", new SimpleAdapter(parent.getContext(), Marks, R.layout.student_list_item_click2,  
												new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 })); 

									}
									else
									{
										List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  
										studentData.add(createItem("There is no mark", "")); 

										//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
										adapter3.addSection("Marks", new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.promotion_list_item,  
												new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 }));  
															
									}
									
									lvListe.setAdapter(adapter3);
									
									lvListe.setOnItemClickListener(new OnItemClickListener() {
										public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
											/*Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
													+ " item : " + parent.getAdapter().getItem(position)
													+ " nb item in list : " + parent.getAdapter().getCount()
													, Toast.LENGTH_SHORT);
											toast.show();
											Log.e(TAG, parent.getAdapter().getItem(position).toString());*/
											
											Log.d(TAG,  parent.getAdapter().getItem(position).toString());
											String [] splitItem = parent.getAdapter().getItem(position).toString().split(",");
											
											if (splitItem.length == 4)
											{
												// Information
												String [] splitCaption3 = splitItem[0].split("=");
												String [] splitCaption2 = splitItem[1].split("=");
												String [] splitCaption = splitItem[2].split("=");
												String [] splitTitle = splitItem[3].split("=");
			
												String idSTU = splitCaption3[1];
												String promotion = splitCaption2[1];
												String numSTU = splitCaption[1];
												String studentName = splitTitle[1].substring(0, splitTitle[1].length()-1);
												Log.d(TAG, idSTU);
												Log.d(TAG, promotion);
												Log.d(TAG, numSTU);
												Log.d(TAG, studentName);
												
												//ferme l'activity en cours (login) et ouvre le menu
												//finish();	

												//création de notre item  
												Intent editStudentActivity = new Intent(parent.getContext(), EditStudentActivity.class);
		 						                // objet qui vas nous permettre de passe des variables ici la variable  
									            Bundle objetbunble = new Bundle();
									            objetbunble.putString("idSTU",idSTU);
									            objetbunble.putString("promotion",promotion);
									            objetbunble.putString("numSTU",numSTU);
									            objetbunble.putString("studentName",studentName);
									            // on passe notre objet a notre activities
									            editStudentActivity.putExtras(objetbunble );
									             // on appelle notre activité
												startActivity(editStudentActivity);
												
												/*// creation de nom objet de type ListSeparer 
												ListSeparer adapter3 = new ListSeparer(parent.getContext()); 

												List<Map<String,?>> Information = new LinkedList<Map<String,?>>();  
												Information.add(createItem(studentName, numSTU, promotion));

												//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
												adapter3.addSection("Information", new SimpleAdapter(parent.getContext(), Information, R.layout.student_item_click_edit1,  
														new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.editTextView1, R.id.editTextView2, R.id.editTextView3 }));
												
												
												lvListe.setAdapter(adapter3);*/
											}
											else if (splitItem.length == 2)
											{
												// Marks
												String [] splitCaption = splitItem[0].split("=");
												String [] splitTitle = splitItem[1].split("=");
												
												if (splitCaption.length == 2)
												{
													String subject = splitCaption[1];
													Log.d(TAG, subject);
												}
												String mark = splitTitle[1].substring(0, splitTitle[1].length()-1);
												Log.d(TAG, mark);
											}

										}
									});
									
									/*List<Map<String,?>> DataEmpty = new LinkedList<Map<String,?>>();
									DataEmpty.add(createItem("",""));
									
									//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
									adapter3.addSection("", new SimpleAdapter(parent.getContext(), DataEmpty, R.layout.student_list_item_click3,  
											new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.textView1, R.id.textView2 })); 

						
									Button button_content_new = (Button)view.findViewById(R.id.button_content_new);
									button_content_new.setOnClickListener(new View.OnClickListener() {
								            public void onClick(View v) {
												//on affiche dans un Toast que le login a bien réussi
												//Toast.makeText(GlobalVars.getAppContext(), "button_content_new", Toast.LENGTH_SHORT).show();
								            	Log.d(TAG, "button_content_new");
								             }
								        });   
									
									Button button_content_edit = (Button)view.findViewById(R.id.button_content_edit);
									button_content_edit.setOnClickListener(new View.OnClickListener() {
								            public void onClick(View v) {
												//on affiche dans un Toast que le login a bien réussi
												//Toast.makeText(GlobalVars.getAppContext(), "button_content_edit", Toast.LENGTH_SHORT).show();
												Log.d(TAG, "button_content_edit");
								             }
								        });     
									
									Button button_content_discard = (Button)view.findViewById(R.id.button_content_discard);
									button_content_discard.setOnClickListener(new View.OnClickListener() {
								            public void onClick(View v) {
												//on affiche dans un Toast que le login a bien réussi
												//Toast.makeText(GlobalVars.getAppContext(), "button_content_discard", Toast.LENGTH_SHORT).show();
												Log.d(TAG, "button_content_discard");
								             }
								        }); */
									
									/*List<Map<String,?>> Information = new LinkedList<Map<String,?>>();  
									Information.add(createItem("Almeras Antoine", "21004735", "Promotion 2013"));  	

									List<Map<String,?>> Marks = new LinkedList<Map<String,?>>();  
									Marks.add(createItem("RF1", "12,5"));
									Marks.add(createItem("Analyse de gros volume de données", "12,5"));*/ 


									//lvListe.setClickable(false);
									//lvListe.setEnabled(false);
									//lvListe.setSelector(android.R.color.transparent); 

								}
							});
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
						
						//SortABC
						ComparableAlphabet comparableAlphabet = new ComparableAlphabet();
					    Collections.sort(studentAlphabet, comparableAlphabet);
						
						for(int indexLetter = 0; indexLetter < studentAlphabet.size(); indexLetter++)
						{
							List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  

							for(int indexStudent = 0; indexStudent < students.size(); indexStudent++)
							{				
								/*String lastName = students.get(indexStudent).getLastname();
								String currentLetter = studentAlphabet.get(indexLetter);
								String currentFirstLetterSTU = students.get(indexStudent).getLastname().substring(0, 1);*/

								if (studentAlphabet.get(indexLetter).equals(students.get(indexStudent).getLastname().substring(0, 1)))
								{						
									studentData.add(createItem(students.get(indexStudent).getLastname() + " " + students.get(indexStudent).getFirstname(), 
											students.get(indexStudent).getNumStu(), students.get(indexStudent).getId()));
								}
							}

							//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
							adapter.addSection(studentAlphabet.get(indexLetter), new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.student_list_item,  
									new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 }));  
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

						//SortABC
						ComparableAlphabet comparableAlphabet = new ComparableAlphabet();
					    Collections.sort(studentAlphabet, comparableAlphabet);
						
						for(int indexLetter = 0; indexLetter < studentAlphabet.size(); indexLetter++)
						{
							List<Map<String,?>> studentData = new LinkedList<Map<String,?>>();  

							for(int indexStudent = 0; indexStudent < students.size(); indexStudent++)
							{				
								/*String lastName = students.get(indexStudent).getFirstname();
								String currentLetter = studentAlphabet.get(indexLetter);
								String currentFirstLetterSTU = students.get(indexStudent).getFirstname().substring(0, 1);*/

								if (studentAlphabet.get(indexLetter).equals(students.get(indexStudent).getFirstname().substring(0, 1)))
								{
									studentData.add(createItem(students.get(indexStudent).getFirstname() + " " + students.get(indexStudent).getLastname(), 
											students.get(indexStudent).getNumStu(), students.get(indexStudent).getId()));  
								}
							}

							//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
							adapter.addSection(studentAlphabet.get(indexLetter), new SimpleAdapter(GlobalVars.getAppContext(), studentData, R.layout.student_list_item,  
									new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2}, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 }));  
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
					
					//lvListe.setClickable(false);
					lvListe.setEnabled(false);
					//lvListe.setSelector(android.R.color.transparent);
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
							String [] splitCaption2 = splitItem[0].split("=");
							String [] splitCaption = splitItem[1].split("=");
							String [] splitTitle = splitItem[2].split("=");

							String idSTU = splitCaption2[1];
							String numSTU = splitCaption[1];
							String studentName = splitTitle[1].substring(0, splitTitle[1].length()-1);
							Log.d(TAG, idSTU);
							Log.d(TAG, numSTU);
							Log.d(TAG, studentName);

							int indexStudent = 0;
							boolean studentFind = false;
							Student student = null;
							while(indexStudent < students.size() && studentFind == false)
							{
								//Log.d(TAG, students.get(indexStudent).getNumStu() + " | " + numSTU);

								if (students.get(indexStudent).getId().equals(idSTU))
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
							Information.add(createItem(studentName, numSTU, "Promotion " + student.getPromotion().getLabel() + " " + student.getPromotion().getAnnee(), student.getId()));

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
							//lvListe.setEnabled(false);
							//lvListe.setSelector(android.R.color.transparent);
							
							lvListe.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									/*Toast toast = Toast.makeText(view.getContext(), "position in list : " + position
											+ " item : " + parent.getAdapter().getItem(position)
											+ " nb item in list : " + parent.getAdapter().getCount()
											, Toast.LENGTH_SHORT);
									toast.show();
									Log.e(TAG, parent.getAdapter().getItem(position).toString());*/
									
									Log.d(TAG,  parent.getAdapter().getItem(position).toString());
									String [] splitItem = parent.getAdapter().getItem(position).toString().split(",");
									
									if (splitItem.length == 4)
									{
										// Information
										String [] splitCaption3 = splitItem[0].split("=");
										String [] splitCaption2 = splitItem[1].split("=");
										String [] splitCaption = splitItem[2].split("=");
										String [] splitTitle = splitItem[3].split("=");
	
										String idSTU = splitCaption3[1];
										String promotion = splitCaption2[1];
										String numSTU = splitCaption[1];
										String studentName = splitTitle[1].substring(0, splitTitle[1].length()-1);
										Log.d(TAG, idSTU);
										Log.d(TAG, promotion);
										Log.d(TAG, numSTU);
										Log.d(TAG, studentName);
										
										//ferme l'activity en cours (login) et ouvre le menu
										//finish();	

										//création de notre item  
										Intent editStudentActivity = new Intent(parent.getContext(), EditStudentActivity.class);
 						                // objet qui vas nous permettre de passe des variables ici la variable  
							            Bundle objetbunble = new Bundle();
							            objetbunble.putString("idSTU",idSTU);
							            objetbunble.putString("promotion",promotion);
							            objetbunble.putString("numSTU",numSTU);
							            objetbunble.putString("studentName",studentName);
							            // on passe notre objet a notre activities
							            editStudentActivity.putExtras(objetbunble );
							             // on appelle notre activité
										startActivity(editStudentActivity);
										
										/*// creation de nom objet de type ListSeparer 
										ListSeparer adapter3 = new ListSeparer(parent.getContext()); 

										List<Map<String,?>> Information = new LinkedList<Map<String,?>>();  
										Information.add(createItem(studentName, numSTU, promotion));

										//ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
										adapter3.addSection("Information", new SimpleAdapter(parent.getContext(), Information, R.layout.student_item_click_edit1,  
												new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_CAPTION2 }, new int[] { R.id.editTextView1, R.id.editTextView2, R.id.editTextView3 }));
										
										
										lvListe.setAdapter(adapter3);*/
									}
									else if (splitItem.length == 2)
									{
										// Marks
										String [] splitCaption = splitItem[0].split("=");
										String [] splitTitle = splitItem[1].split("=");
										
										if (splitCaption.length == 2)
										{
											String subject = splitCaption[1];
											Log.d(TAG, subject);
										}
										String mark = splitTitle[1].substring(0, splitTitle[1].length()-1);
										Log.d(TAG, mark);
									}

								}
							});
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

				int mHandlerMessageStudents = retrievesStudentsService.getMHandler();

				if (mHandlerMessageStudents != 0)
				{
					mHandler.sendEmptyMessage(mHandlerMessageStudents);
				}

				//Step 5: call getRetrievesStudents() to get the return value
				students = retrievesStudentsService.getRetrievesStudents();

				Log.d(TAG, "Students size = " + students.size());
				
				String [] studentsNumSTU = new String[students.size()];
				
				for (int indexStudent = 0; indexStudent < students.size(); indexStudent++)
				{
					studentsNumSTU[indexStudent] = students.get(indexStudent).getNumStu();
					Log.d(TAG, studentsNumSTU[indexStudent]);
				}
				
				// Get a reference to the AutoCompleteTextView in the layout
				AutoCompleteTextView autoCompleteTextViewNumSTU = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextViewStudentNumberTakeMark);
				autoCompleteTextViewNumSTU.setThreshold(1);
				// Create the adapter and set it to the AutoCompleteTextView 
				ArrayAdapter<String> adapterNumSTU = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, studentsNumSTU);
				autoCompleteTextViewNumSTU.setAdapter(adapterNumSTU);
				
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
				
			case 22:
				mDialog.dismiss();
				//on affiche dans un Toast que le login a bien réussi
				Toast.makeText(GlobalVars.getAppContext(), "Retrieval of students of the promotion successful ", Toast.LENGTH_SHORT).show();
				break;

			case 30:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbPost = new AlertDialog.Builder(menuActivity);
				adbPost.setTitle("Error");
				adbPost.setMessage("Incorrect credentials. \nThank you verify your username and password. \n\nIf the problem persists, contact your administrator.");
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
				
			case 42:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbStudentsInPromotionNetwork = new AlertDialog.Builder(menuActivity);
				adbStudentsInPromotionNetwork.setTitle("Error");
				adbStudentsInPromotionNetwork.setMessage("Retrieval of students of the promotion impossible, following a network problem.\nThank you for your connectivity check.");
				adbStudentsInPromotionNetwork.setPositiveButton("Ok", null);
				adbStudentsInPromotionNetwork.show();
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
				
			case 52:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbRetrievesStudentsInPromotion = new AlertDialog.Builder(menuActivity);
				adbRetrievesStudentsInPromotion.setTitle("Error");
				adbRetrievesStudentsInPromotion.setMessage("Problem occurred when retrieving students of the promotion.\n\nIf the problem persists, contact your administrator.");
				adbRetrievesStudentsInPromotion.setPositiveButton("Ok", null);
				adbRetrievesStudentsInPromotion.show();
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

	private class RefreshTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulate something long running
			//try {
				//Thread.sleep(2000);
				
				// When the given tab is selected, show the tab contents in the
				// container view.
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
						menuActivity.getActionBar().getSelectedNavigationIndex() + 1);
				fragment.setArguments(args);
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
				
			/*} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
				
			return null;	
	
		}

		@Override
		protected void onPostExecute(String result) {
			menuItem.collapseActionView();
			menuItem.setActionView(null);
		}
	}
}


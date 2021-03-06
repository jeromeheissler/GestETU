package com.polytech.gestetu.front;

import java.util.ArrayList;
import java.util.List;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.R;
import com.polytech.gestetu.models.Promotion;
import com.polytech.gestetu.models.Student;
import com.polytech.gestetu.services.AddStudentService;
import com.polytech.gestetu.services.EditStudentService;
import com.polytech.gestetu.services.RetrievesPromotionsService;
import com.polytech.gestetu.services.RetrievesStudentsService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudentActivity extends Activity {

	private static final String TAG = "AddStudentActivity";
	private static ProgressDialog mDialog; 
	private static ProgressDialog mDialog2; 
	private static List<Promotion> promotions = null;
	private static List<Student> students = null;
	public static AddStudentActivity studentActivity;
	private static Student studentFind = null;
	private static EditText textViewLastName;
	private static EditText textViewFirstName;
	private static EditText textViewNumSTU;
	private static EditText textViewEmail;
	//private static AutoCompleteTextView  autoCompleteTextViewPromotion;
	private static Spinner spinnerPromotions;
	
	/** 
	 * M�thode appel�e en premier lorsque l'activit� est cr�e
	 * @param Bundle savedInstanceState contient l��tat de sauvegarde enregistr� lors de la derni�re ex�cution de l'activit�
	 * @return l'attribut response
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_add);
		
		studentActivity = this;
		
		promotions = new ArrayList<Promotion>();

		//affichage d'une progressDialog
		mDialog = new ProgressDialog(this);
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

		int mHandlerMessagePromotions = retrievesPromotionsService.getMHandler();

		if (mHandlerMessagePromotions != 0)
		{
			mHandler.sendEmptyMessage(mHandlerMessagePromotions);
		}

		//Step 5: call getRetrievesPromotions() to get the return value
		promotions = retrievesPromotionsService.getRetrievesPromotions();

		Log.d(TAG, "Promotions size = " + promotions.size());
		
		/*students = new ArrayList<Student>();
		
		//affichage d'une progressDialog
		mDialog2 = new ProgressDialog(this);
		mDialog2.setMessage("Patientez s'il-vous-plait...");
		mDialog2.setCancelable(false);
		mDialog2.show();

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

		String [] studentsLastName = new String[students.size()];
		String [] studentsFirstName = new String[students.size()];
		String [] studentsNumSTU = new String[students.size()];
		
		for (int indexStudent = 0; indexStudent < students.size(); indexStudent++)
		{
			studentsLastName[indexStudent] = students.get(indexStudent).getLastname();
			studentsFirstName[indexStudent] = students.get(indexStudent).getFirstname();
			studentsNumSTU[indexStudent] = students.get(indexStudent).getNumStu();
			Log.d(TAG, studentsLastName[indexStudent]);
			Log.d(TAG, studentsFirstName[indexStudent]);
			Log.d(TAG, studentsNumSTU[indexStudent]);
		}*/
		
		String [] promotionsYear = new String[promotions.size()];
		
		for (int indexPromotion = 0; indexPromotion < promotions.size(); indexPromotion++)
		{
			promotionsYear[indexPromotion] = promotions.get(indexPromotion).getLabel() + " " + promotions.get(indexPromotion).getAnnee();
			Log.d(TAG, promotionsYear[indexPromotion]);
		}
		
		spinnerPromotions = (Spinner) findViewById(R.id.SpinnerPromotion);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, promotionsYear);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPromotions.setAdapter(dataAdapter);
		
		// Get a reference to the AutoCompleteTextView in the layout
		//textViewLastName = (EditText) findViewById(R.id.TextViewLastName);
		/*autoCompleteTextViewLastName.setThreshold(1);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapterLastName = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentsLastName);
		autoCompleteTextViewLastName.setAdapter(adapterLastName);*/
			
		// Get a reference to the AutoCompleteTextView in the layout
		//textViewFirstName = (EditText) findViewById(R.id.TextViewFirstName);
		/*autoCompleteTextViewFirstName.setThreshold(1);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapterFirstName = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentsFirstName);
		autoCompleteTextViewFirstName.setAdapter(adapterFirstName);*/
		
		// Get a reference to the AutoCompleteTextView in the layout
		//textViewNumSTU = (EditText) findViewById(R.id.TextViewNumSTU);
		/*autoCompleteTextViewNumSTU.setThreshold(1);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapterNumSTU = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentsNumSTU);
		autoCompleteTextViewNumSTU.setAdapter(adapterNumSTU);*/
		
		//textViewEmail = (EditText) findViewById(R.id.TextViewEmail);
		
		/*// Get a reference to the AutoCompleteTextView in the layout
		autoCompleteTextViewPromotion = (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextViewPromotion);
		autoCompleteTextViewPromotion.setThreshold(1);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapterPromotion = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, promotionsYear);
		autoCompleteTextViewPromotion.setAdapter(adapterPromotion);*/

		Button btnSubmit = (Button) findViewById(R.id.buttonValidate);

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    
				if (studentFind == null)
				{
					studentFind = new Student();
				}
				
				textViewLastName = (EditText) findViewById(R.id.TextViewLastName);
				studentFind.setLastname(textViewLastName.getText().toString());
				textViewFirstName = (EditText) findViewById(R.id.TextViewFirstName);
				studentFind.setFirstname(textViewFirstName.getText().toString());
				textViewNumSTU = (EditText) findViewById(R.id.TextViewNumSTU);
				studentFind.setNumStu(textViewNumSTU.getText().toString());
				textViewEmail = (EditText) findViewById(R.id.TextViewEmail);
				studentFind.setEmail(textViewEmail.getText().toString());
				
				String spinnerString = String.valueOf(spinnerPromotions.getSelectedItem());
				Log.e(TAG, spinnerString);
				String [] promotionString = spinnerString.split(" ");
				
				Promotion promotion = new Promotion();
				//promotion.setAnnee(Integer.parseInt(promotionString[1]));
				//promotion.setLabel(promotionString[0]);		
				
				int indexPromotion = 0;
				boolean promotionFindBool = false;
				while(indexPromotion < promotions.size() && promotionFindBool == false)
				{
					//Log.d(TAG, students.get(indexStudent).getNumStu() + " | " + numSTU);

					if (promotions.get(indexPromotion).getAnnee() == Integer.parseInt(promotionString[1]) && promotions.get(indexPromotion).getLabel().equals(promotionString[0]))
					{
						promotionFindBool = true;
						studentFind.setPromotion(promotions.get(indexPromotion));
					}

					indexPromotion++;
				}

				Log.d(TAG, "promotionFindBool : " + promotionFindBool);
				
				//affichage d'une progressDialog
				mDialog = new ProgressDialog(studentActivity);
				mDialog.setMessage("Patientez s'il-vous-plait...");
				mDialog.setCancelable(false);
				mDialog.show();

				//Initialize thread class
				AddStudentService addStudentService = new AddStudentService(studentFind);
				//Start thread
				addStudentService.start();

				//Step 4: Call join() to wait until fib thread finishes
				try{
					addStudentService.join();
				}catch(InterruptedException ie){}

				int mHandlerMessagePromotions = addStudentService.getMHandler();

				if (mHandlerMessagePromotions != 0)
				{
					mHandler.sendEmptyMessage(mHandlerMessagePromotions);
				}

				//Step 5: call getRetrievesPromotions() to get the return value
				String statusOK = addStudentService.getStatusOk();

				Log.d(TAG, "statusOK = " + statusOK);
				
				
				//ferme l'activity en cours (login) et ouvre le menu
				finish();	

				Intent menu = new Intent(getBaseContext(), MenuActivity.class);					
				startActivity(menu);
			}
		});  
	}
	
	/** 
	 * M�thode pour la progressDialog
	 */
	static Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			super.handleMessage(msg);

			//selon le message re�u par le thread, on ferme la progress bar et on affiche l'erreur appropri�e
			switch (msg.what)
			{
			case 10:
				mDialog.dismiss();
				break;

			case 20:
				mDialog.dismiss();
				//on affiche dans un Toast que le login a bien r�ussi
				Toast.makeText(GlobalVars.getAppContext(), "Retrieval of promotions successful", Toast.LENGTH_SHORT).show();
				break;

			case 21:
				mDialog2.dismiss();
				//on affiche dans un Toast que le login a bien r�ussi
				Toast.makeText(GlobalVars.getAppContext(), "Retrieval of students successful ", Toast.LENGTH_SHORT).show();
				break;

			case 22:
				mDialog2.dismiss();
				//on affiche dans un Toast que le login a bien r�ussi
				Toast.makeText(GlobalVars.getAppContext(), "Add of students successful ", Toast.LENGTH_SHORT).show();
				break;
				
			case 32:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbEditStudentProblem = new AlertDialog.Builder(studentActivity);
				adbEditStudentProblem.setTitle("Error");
				adbEditStudentProblem.setMessage("Add of student impossible.\nThank you verify the attributes of your student.");
				adbEditStudentProblem.setPositiveButton("Ok", null);
				adbEditStudentProblem.show();
				break;
				
			case 40:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbPromotionsNetwork = new AlertDialog.Builder(studentActivity);
				adbPromotionsNetwork.setTitle("Error");
				adbPromotionsNetwork.setMessage("Retrieval of promotions impossible, following a network problem.\nThank you for your connectivity check.");
				adbPromotionsNetwork.setPositiveButton("Ok", null);
				adbPromotionsNetwork.show();
				break;

			case 41:
				mDialog2.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbStudentsNetwork = new AlertDialog.Builder(studentActivity);
				adbStudentsNetwork.setTitle("Error");
				adbStudentsNetwork.setMessage("Retrieval of students impossible, following a network problem.\nThank you for your connectivity check.");
				adbStudentsNetwork.setPositiveButton("Ok", null);
				adbStudentsNetwork.show();
				break;
				
			case 42:
				mDialog2.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbEditStudentNetwork = new AlertDialog.Builder(studentActivity);
				adbEditStudentNetwork.setTitle("Error");
				adbEditStudentNetwork.setMessage("Add of student impossible, following a network problem.\nThank you for your connectivity check.");
				adbEditStudentNetwork.setPositiveButton("Ok", null);
				adbEditStudentNetwork.show();
				break;

			case 50:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbRetrievesPromotions = new AlertDialog.Builder(studentActivity);
				adbRetrievesPromotions.setTitle("Error");
				adbRetrievesPromotions.setMessage("Problem occurred when retrieving promotions.\n\nIf the problem persists, contact your administrator.");
				adbRetrievesPromotions.setPositiveButton("Ok", null);
				adbRetrievesPromotions.show();
				break;

			case 51:
				mDialog2.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbRetrievesStudents = new AlertDialog.Builder(studentActivity);
				adbRetrievesStudents.setTitle("Error");
				adbRetrievesStudents.setMessage("Problem occurred when retrieving students.\n\nIf the problem persists, contact your administrator.");
				adbRetrievesStudents.setPositiveButton("Ok", null);
				adbRetrievesStudents.show();
				break;
				
			case 52:
				mDialog2.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbEditStudent = new AlertDialog.Builder(studentActivity);
				adbEditStudent.setTitle("Error");
				adbEditStudent.setMessage("Problem occurred durint the add of student.\n\nIf the problem persists, contact your administrator.");
				adbEditStudent.setPositiveButton("Ok", null);
				adbEditStudent.show();
				break;
			}
		}
	};

}


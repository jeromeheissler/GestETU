package com.polytech.gestetu.services;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.models.Promotion;
import com.polytech.gestetu.models.Student;
import com.polytech.gestetu.services.WebServiceRestClient.RequestMethod;

public class AddStudentService extends Thread {
	
	//d�claration des variables globales
	private static final String TAG = "AddStudentService";
	// variable qui va permettre d'envoyer un message pour stopper la progress bar et afficher un message d'erreur
	private static int mHandler = 0;
	
	private Student student;
	
	public AddStudentService(Student student)
	{
		this.student = student;
	}
	
	//Step 1: create a variable to hold return value
	private String statusOK;

	//Step 2: add getRetrievesPromotions() method
	public String getStatusOk(){
		return statusOK;
	}

	//Step 3: Calculate and assign the value to a variable
	@Override
	public void run(){
		Process();
	}

	private void Process(){
		//retrievesPromotions = new ArrayList<Promotion>();
		//retrievesPromotions = null;
		
		try {
			boolean disponibiliteReseauOk = false;

			disponibiliteReseauOk = WebServiceRestClient.isConnected();
			Log.d(TAG, "disponibilit� r�seau : " + disponibiliteReseauOk);
			if (disponibiliteReseauOk == true)
			{
				/*****************   debut requete PUT   ******************/
				int responseCode = 0;	
				String url = GlobalVars.getUrl();
				String uriPut = "api/student/";
				String address = url + uriPut;
				
				WebServiceRestClient webServiceRestPut = new WebServiceRestClient(address);    
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonObjectStudent = mapper.writeValueAsString(student);  
				Log.d(TAG, "jsonObjectStudent : " + jsonObjectStudent);
				
				// param
				webServiceRestPut.AddParam("t", GlobalVars.getTokenValue());
				webServiceRestPut.AddParam("student", jsonObjectStudent);
				
				try {
					webServiceRestPut.Execute(RequestMethod.PUT);
					// r�cup�ration du code http renvoy� apr�s ex�cution de la requ�te http 
					// qui permet d'identifier les �ventuelles erreurs survenues lors de l'ex�cution
					responseCode = webServiceRestPut.getResponseCode();

				} catch (Exception e) {
					Log.e(TAG, "Erreur webServiceRestPut \n" + e.getMessage());
				}			
				
				//si le code retourn� par la requ�te GET est 401 -> erreur
				if (responseCode == 401) 
				{
					Log.e(TAG, "La requ�te n�cessite une identification de l'utilisateur. (code HTTP " + responseCode + " : Non autoris�)");
					Log.e(TAG, "Une authentification compl�te est n�cessaire pour acc�der � cette ressource");
				}
				//sinon si le code retourn� par la requ�te GET est 200 -> ok
				else if (responseCode == 200)
				{
					Log.i(TAG, "La requ�te HTTP a �t� trait�e avec succ�s. (code HTTP " + responseCode + " : OK)");
					String reponsePut = webServiceRestPut.getResponse();
					Log.d(TAG, "R�ponse PUT : " + reponsePut);
					
					
					/*****************   fin requete POST   ******************/

					// parse de la r�ponse JSON 
					/*String jsonFormattedString = reponseGet.replace("\\\"", "\"");
					
					Log.d(TAG, "R�ponse GET after backslash filter : " + jsonFormattedString);
					
					jsonFormattedString = jsonFormattedString.replace("\"{", "{");
					jsonFormattedString = jsonFormattedString.replace("}\"", "}");
					
					Log.d(TAG, "R�ponse GET after accolade filter : " + jsonFormattedString);

					// parse de la string contenant un document json vers une structure json 
					JSONArray jsonArray = new JSONArray(jsonFormattedString);*/
					
					JSONObject jsonObject = new JSONObject(reponsePut);
					
					Log.d(TAG, "jsonObject : " + jsonObject.toString());
					
					String status = jsonObject.getString("status");
					if (status.equals("200"))
					{
						mHandler = 22;				
					}
					else
					{
						mHandler = 32;
					}
				}
			}
			else
			{
				mHandler = 42;
				Log.e(TAG, "R�cup�ration des promotions impossible suite � un probl�me r�seau");
			}
		}
		catch (Exception e)
		{
			mHandler = 52;
			Log.e(TAG, "Probl�me survenue lors de la r�cup�ration des promotions \n" + e.getMessage());
		}
	}
	
	/**
	 * Accesseur GET de la variable mHandler 
	 * @return variable qui va permettre d'envoyer un message pour stopper la progress bar et afficher un message d'erreur
	 */
	public int getMHandler() {
		return mHandler;
	}
}

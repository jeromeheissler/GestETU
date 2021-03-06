package com.polytech.gestetu.services;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.models.Student;
import com.polytech.gestetu.services.WebServiceRestClient.RequestMethod;

public class RetrievesStudentsInPromotionService extends Thread {
	
	//d�claration des variables globales
	private static final String TAG = "RetrievesStudentsInPromotionService";
	// variable qui va permettre d'envoyer un message pour stopper la progress bar et afficher un message d'erreur
	private static int mHandler = 0;
	private String idPromotion;
	
	//Step 1: create a variable to hold return value
	private ArrayList<Student> retrievesStudentsInPromotion;

	//Step 2: add getRetrievesStudentsInPromotion() method
	public ArrayList<Student> getRetrievesStudentsInPromotion(){
		return retrievesStudentsInPromotion;
	}

	//Step 3: Calculate and assign the value to a variable
	@Override
	public void run(){
		Process();
	}

	public RetrievesStudentsInPromotionService(String idPromotion)
	{
		this.idPromotion = idPromotion;
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
				/*****************   debut requete GET   ******************/
				int responseCode = 0;	
				String url = GlobalVars.getUrl();
				String uriGet = "api/promotion/" + idPromotion;
				String address = url + uriGet;
				
				WebServiceRestClient webServiceRestGet = new WebServiceRestClient(address);    
				
				// header 
				webServiceRestGet.AddHeader("Accept", "application/json");
				webServiceRestGet.AddHeader("Content-type", "application/json");
				
				try {
					webServiceRestGet.Execute(RequestMethod.GET);
					// r�cup�ration du code http renvoy� apr�s ex�cution de la requ�te http 
					// qui permet d'identifier les �ventuelles erreurs survenues lors de l'ex�cution
					responseCode = webServiceRestGet.getResponseCode();

				} catch (Exception e) {
					Log.e(TAG, "Erreur webServiceRestGet \n" + e.getMessage());
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
					String reponseGet = webServiceRestGet.getResponse();
					Log.d(TAG, "R�ponse GET : " + reponseGet);
					
					
					/*****************   fin requete GET   ******************/

					// parse de la r�ponse JSON 
					/*String jsonFormattedString = reponseGet.replace("\\\"", "\"");
					
					Log.d(TAG, "R�ponse GET after backslash filter : " + jsonFormattedString);
					
					jsonFormattedString = jsonFormattedString.replace("\"{", "{");
					jsonFormattedString = jsonFormattedString.replace("}\"", "}");
					
					Log.d(TAG, "R�ponse GET after accolade filter : " + jsonFormattedString);*/

					// parse de la string contenant un document json vers une structure json 
					//JSONArray jsonArray = new JSONArray(jsonFormattedString);
					
					JSONObject json = new JSONObject(reponseGet);
					JSONObject jsonObjectPromotion = json.getJSONObject("promotion"); 
					JSONArray jsonArray = jsonObjectPromotion.getJSONArray("students"); 
					
					retrievesStudentsInPromotion = new ArrayList<Student>();
					
					Log.d(TAG, "jsonArray : " + jsonArray.toString());
					
					for (int i = 0; i < jsonArray.length(); i++)
					{	
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						ObjectMapper mapper = new ObjectMapper();
						Log.d(TAG, "jsonObject : " + jsonObject.toString() );
						Student student = mapper.readValue(jsonObject.toString(), Student.class);
						Log.d(TAG, "my retrieves students in my promotion : " + student.getId() + " " + student.getFirstname() + " " + student.getLastname());
						
						retrievesStudentsInPromotion.add(student);
						Log.d(TAG, "my retrieves students in my promotion add to studentsInPromotion OK ! count : " + retrievesStudentsInPromotion.size());
					}
					mHandler = 22;
				}
			}
			else
			{
				mHandler = 42;
				retrievesStudentsInPromotion = new ArrayList<Student>();
				Log.e(TAG, "R�cup�ration des �tudiants de la promotion impossible suite � un probl�me r�seau");
			}
		}
		catch (Exception e)
		{
			mHandler = 52;
			Log.e(TAG, "Probl�me survenue lors de la r�cup�ration des �tudiants de la promotion \n" + e.getMessage());
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
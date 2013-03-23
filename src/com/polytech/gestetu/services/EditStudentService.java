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

public class EditStudentService extends Thread {
	
	//déclaration des variables globales
	private static final String TAG = "EditStudentService";
	// variable qui va permettre d'envoyer un message pour stopper la progress bar et afficher un message d'erreur
	private static int mHandler = 0;
	
	private Student student;
	
	public EditStudentService(Student student)
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
			Log.d(TAG, "disponibilité réseau : " + disponibiliteReseauOk);
			if (disponibiliteReseauOk == true)
			{
				/*****************   debut requete POST   ******************/
				int responseCode = 0;	
				String url = GlobalVars.getUrl();
				String uriPost = "api/student/" + student.getId();
				String address = url + uriPost;
				
				WebServiceRestClient webServiceRestPost = new WebServiceRestClient(address);    
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonObjectStudent = mapper.writeValueAsString(student);  
				Log.d(TAG, "jsonObjectStudent : " + jsonObjectStudent);
				
				// param
				webServiceRestPost.AddParam("t", GlobalVars.getTokenValue());
				webServiceRestPost.AddParam("student", jsonObjectStudent);
				
				try {
					webServiceRestPost.Execute(RequestMethod.POST);
					// récupération du code http renvoyé après exécution de la requête http 
					// qui permet d'identifier les éventuelles erreurs survenues lors de l'exécution
					responseCode = webServiceRestPost.getResponseCode();

				} catch (Exception e) {
					Log.e(TAG, "Erreur webServiceRestGet \n" + e.getMessage());
				}			
				
				//si le code retourné par la requête GET est 401 -> erreur
				if (responseCode == 401) 
				{
					Log.e(TAG, "La requête nécessite une identification de l'utilisateur. (code HTTP " + responseCode + " : Non autorisé)");
					Log.e(TAG, "Une authentification complète est nécessaire pour accéder à cette ressource");
				}
				//sinon si le code retourné par la requête GET est 200 -> ok
				else if (responseCode == 200)
				{
					Log.i(TAG, "La requête HTTP a été traitée avec succès. (code HTTP " + responseCode + " : OK)");
					String reponsePost = webServiceRestPost.getResponse();
					Log.d(TAG, "Réponse POST : " + reponsePost);
					
					
					/*****************   fin requete POST   ******************/

					// parse de la réponse JSON 
					/*String jsonFormattedString = reponseGet.replace("\\\"", "\"");
					
					Log.d(TAG, "Réponse GET after backslash filter : " + jsonFormattedString);
					
					jsonFormattedString = jsonFormattedString.replace("\"{", "{");
					jsonFormattedString = jsonFormattedString.replace("}\"", "}");
					
					Log.d(TAG, "Réponse GET after accolade filter : " + jsonFormattedString);

					// parse de la string contenant un document json vers une structure json 
					JSONArray jsonArray = new JSONArray(jsonFormattedString);*/
					
					JSONObject jsonObject = new JSONObject(reponsePost);
					
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
				Log.e(TAG, "Récupération des promotions impossible suite à un problème réseau");
			}
		}
		catch (Exception e)
		{
			mHandler = 52;
			Log.e(TAG, "Problème survenue lors de la récupération des promotions \n" + e.getMessage());
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

package com.polytech.gestetu.services;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.models.Promotion;
import com.polytech.gestetu.services.WebServiceRestClient.RequestMethod;

public class RetrievesPromotionsService extends Thread {
	
	//d�claration des variables globales
	private static final String TAG = "RetrievesPromotionsService";
	// variable qui va permettre d'envoyer un message pour stopper la progress bar et afficher un message d'erreur
	private static int mHandler = 0;
	
	//Step 1: create a variable to hold return value
	private ArrayList<Promotion> retrievesPromotions;

	//Step 2: add getFibonacci() method
	public ArrayList<Promotion> getRetrievesPromotions(){
		return retrievesPromotions;
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
				/*****************   debut requete GET   ******************/
				int responseCode = 0;	
				String url = GlobalVars.getUrl();
				String uriGet = "api/promotion";
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
					String jsonFormattedString = reponseGet.replace("\\\"", "\"");
					
					Log.d(TAG, "R�ponse GET after backslash filter : " + jsonFormattedString);
					
					jsonFormattedString = jsonFormattedString.replace("\"{", "{");
					jsonFormattedString = jsonFormattedString.replace("}\"", "}");
					
					Log.d(TAG, "R�ponse GET after accolade filter : " + jsonFormattedString);

					// parse de la string contenant un document json vers une structure json 
					JSONArray jsonArray = new JSONArray(jsonFormattedString);
					
					retrievesPromotions = new ArrayList<Promotion>();
					
					for (int i = 0; i < jsonArray.length(); i++)
					{	
						Log.d(TAG, "jsonArray : " + jsonArray.toString());

						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						ObjectMapper mapper = new ObjectMapper();
						Log.d(TAG, "jsonObject : " + jsonObject.toString() );
						Promotion promotion = mapper.readValue(jsonObject.toString(), Promotion.class);
						Log.d(TAG, "my retrieves promotion : " + promotion.getId() + " " + promotion.getLabel() + " " + promotion.getAnnee());
						
						retrievesPromotions.add(promotion);
						Log.d(TAG, "my retrieves promotion add to promotions OK ! count : " + retrievesPromotions.size());
					}
					mHandler = 20;
				}
			}
			else
			{
				mHandler = 40;
				retrievesPromotions = new ArrayList<Promotion>();
				Log.e(TAG, "R�cup�ration des promotions impossible suite � un probl�me r�seau");
			}
		}
		catch (Exception e)
		{
			mHandler = 50;
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
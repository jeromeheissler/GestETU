package com.polytech.gestetu.services;

import org.json.JSONObject;

import android.util.Log;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.services.WebServiceRestClient.RequestMethod;

public class LoginService extends Thread {
	
	//déclaration des variables globales
	private static final String TAG = "LoginService";
	// variable qui va permettre d'envoyer un message pour stopper la progress bar et afficher un message d'erreur
	private static int mHandler = 0;
	private String email;
	private String password;
	
	public LoginService(String email, String password)
	{
		this.email = email;
		this.password = password;
	}
	
	//Step 1: create a variable to hold return value
	private String retrievesToken;

	//Step 2: add getRetrievesPromotions() method
	public String getRetrievesToken(){
		return retrievesToken;
	}

	//Step 3: Calculate and assign the value to a variable
	@Override
	public void run(){
		Process();
	}

	private void Process(){
		
		try {
			boolean disponibiliteReseauOk = false;

			disponibiliteReseauOk = WebServiceRestClient.isConnected();
			Log.d(TAG, "disponibilité réseau : " + disponibiliteReseauOk);
			if (disponibiliteReseauOk == true)
			{
				/*****************   debut requete POST   ******************/
				int responseCode = 0;	
				String url = GlobalVars.getUrl();
				String uriPost = "api/login";
				String address = url + uriPost;
				
				WebServiceRestClient webServiceRestPost = new WebServiceRestClient(address);    
				
				// param
				webServiceRestPost.AddParam("email", email);
				webServiceRestPost.AddParam("password", password);
				
				try {
					webServiceRestPost.Execute(RequestMethod.POST);
					// récupération du code http renvoyé après exécution de la requête http 
					// qui permet d'identifier les éventuelles erreurs survenues lors de l'exécution
					responseCode = webServiceRestPost.getResponseCode();

				} catch (Exception e) {
					Log.e(TAG, "Erreur webServiceRestPost \n" + e.getMessage());
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
					if (status.equals("400"))
					{
						mHandler = 30;				
					}
					else if (status.equals("200"))
					{
						Log.d(TAG, "jsonObject type : " + jsonObject.getString("type"));
						String type = jsonObject.getString("type");
						
						GlobalVars.setType(type);
						
						Log.d(TAG, "jsonObject token : " + jsonObject.getString("token"));
						retrievesToken = jsonObject.getString("token");
					
						mHandler = 20;
					}
				}
			}
			else
			{
				mHandler = 40;
				Log.e(TAG, "Récupération du token impossible suite à un problème réseau");
			}
		}
		catch (Exception e)
		{
			mHandler = 50;
			Log.e(TAG, "Problème survenue lors de la récupération du token \n" + e.getMessage());
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
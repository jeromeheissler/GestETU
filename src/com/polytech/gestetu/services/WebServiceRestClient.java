package com.polytech.gestetu.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.polytech.gestetu.GlobalVars;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * Fournit les services clients permettant de se connecter au Web service REST. <br/>
 * V�rifie aussi la connectivit� r�seau. <br/><br/>
 *
 * En architecture REST nous avons les m�thodes suivantes : <br/>
 * GET : pour r�cup�rer des ressources sur le serveur <br/>
 * POST : pour cr�er une nouvelle ressource sur le serveur <br/>
 * PUT : pour mettre � jour les ressources existantes sur le serveur <br/>
 * DELETE : pour supprimer des ressources sur le serveur 
 *
 * @author Nicolas Rosado <nicolas.rosado@atos.net>
 */

public class WebServiceRestClient 
{
	//d�claration des variables globales
	private static final String TAG = "WebServiceRestClient";
	private ArrayList <NameValuePair> params;
	private ArrayList <NameValuePair> headers;
	private String url;
	private int responseCode;
	private String message;
	private String response;

	/** 
	 * Accesseur GET de l'attribut response. 
	 * @param aucun
	 * @return l'attribut response
	 */
	public String getResponse() {
		return response;
	}

	/** 
	 * Accesseur GET de l'attribut message. 
	 * @return l'attribut message
	 */
	public String getErrorMessage() {
		return message;
	}

	/** 
	 * Accesseur GET de l'attribut responseCode. 
	 * @return l'attribut responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/** 
	 * Constructeur de la classe WebServiceRestClient. Initialise par d�faut les attributs de la classe
	 * @param url	url contenant l'adresse du Web service REST et la requ�te lui �tant destin�e
	 */
	public WebServiceRestClient(String url)
	{
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	/** 
	 * Ajoute un couple de nom/valeur repr�sentant les param�tres associ�es � la requ�te
	 * @param name	nom du couple de param�tre de la requ�te
	 * @param value	valeur du couple de param�tre de la requ�te
	 */
	public void AddParam(String name, String value)
	{
		params.add(new BasicNameValuePair(name, value));
	}

	/** 
	 * Ajoute un couple de nom/valeur repr�sentant les en-t�tes associ�es � la requ�te
	 * @param name	nom du couple d'en-t�te de la requ�te
	 * @param value	valeur du couple d'en-t�te de la requ�te
	 */
	public void AddHeader(String name, String value)
	{
		headers.add(new BasicNameValuePair(name, value));
	}

	/** 
	 * Enum�ration qui registre les diff�rents types de m�thodes possible pour les requ�tes
	 */
	public enum RequestMethod
	{
		GET, 
		POST,
		PUT
	}

	/** 
	 * Ajoute les param�tres et les en-t�tes associ�es � la requ�te selon la m�thode choisie
	 * @param method	m�thode choisie pour la requ�te
	 * @throws Exception
	 */
	public void Execute(RequestMethod method) throws Exception
	{	
		switch(method) {
		case GET:
		{
			//ajout des param�tres
			String combinedParams = "";
			if(!params.isEmpty()){
				combinedParams += "?";
				for(NameValuePair p : params)
				{
					String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
					if(combinedParams.length() > 1)
					{
						combinedParams  +=  "&" + paramString;
					}
					else
					{
						combinedParams += paramString;
					}
				}
			}

			//Log.d(TAG, "url : " + url);
			//Log.d(TAG, "param : " + combinedParams);
			//Log.d(TAG, "url complete : " + url + combinedParams);

			HttpGet requestGet = new HttpGet(url + combinedParams);

			//ajout des en-t�tes
			for(NameValuePair h : headers)
			{
				requestGet.addHeader(h.getName(), h.getValue());
			}

			executeRequest(requestGet, url);

			break;
		}
		case POST:
		{
			//Log.d(TAG, "url : " + url);

			HttpPost requestPost = new HttpPost(url);

			//add headers
			for(NameValuePair h : headers)
			{
				requestPost.addHeader(h.getName(), h.getValue());
			}

			if(!params.isEmpty()){
				requestPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			executeRequest(requestPost, url);

			break;
		}
		case PUT:
		{
			//Log.d(TAG, "url : " + url);

			HttpPut requestPut = new HttpPut(url);

			//add headers
			for(NameValuePair h : headers)
			{
				requestPut.addHeader(h.getName(), h.getValue());
			}

			if(!params.isEmpty()){
				requestPut.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			executeRequest(requestPut, url);

			break;
		}
		}
	}

	/** 
	 * Ex�cute la requ�te http
	 * @param request	contient les param�tres et les en-t�tes associ�es à la requ�te selon la m�thode choisie
	 * @param url	url contenant l'adresse du Web service REST et la requête lui �tant destin�e
	 */
	private void executeRequest(HttpUriRequest request, String url)
	{    
		boolean disponibiliteReseauOk = false;

		disponibiliteReseauOk = isConnected();
		if (disponibiliteReseauOk == true)
		{
			HttpParams httpParameters = new BasicHttpParams();

			// timeout de connexion
			// D�finit le d�lai en millisecondes jusqu'� ce qu'une connexion est �tablie.
			// La valeur par d�faut est z�ro, cela signifie que le d�lai d'attente n'est pas utilis�.
			int timeoutConnection = GlobalVars.getTimeOutConnection();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

			// timeout de retour
			// D�finit le d�lai d'attente socket par d�faut (SO_TIMEOUT)
			// En millisecondes, ce qui est le d�lai d'attente pour les donn�es.
			int timeoutSocket = GlobalVars.getTimeOutSocket();
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient client = new DefaultHttpClient(httpParameters);

			try 
			{	
				HttpResponse httpResponse = client.execute(request);

				responseCode = httpResponse.getStatusLine().getStatusCode();
				message = httpResponse.getStatusLine().getReasonPhrase();

				//Log.d(TAG, "message : " + message);
				//Log.d(TAG, "responseCode : " + httpResponse.getStatusLine().getStatusCode());

				HttpEntity entity = httpResponse.getEntity();

				if (entity != null) 
				{
					InputStream instream = entity.getContent();
					response = convertStreamToString(instream);

					//Log.d(TAG, "response : " + response);

					// la fermeture du flux d'entr�e va d�clencher la lib�ration de connexion
					instream.close();
				}   

			} catch (ClientProtocolException e)  {
				client.getConnectionManager().shutdown();
				Log.e(TAG, "Erreur ClientProtocolException \n" + e.getMessage());
			} catch (IOException e) {
				client.getConnectionManager().shutdown();
				Log.e(TAG, "Erreur IOException \n" + e.getMessage());
			}
		}
	}

	/** 
	 * Convertit la r�ponse afin de pouvoir la rendre lisible
	 * @param is	r�ponse http renvoy�e par la requ�te adress�e au Web service REST
	 * @return la r�ponse http convertit en String
	 */
	private static String convertStreamToString(InputStream is) 
	{    	
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Log.e(TAG, "Erreur IOException \n" + e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(TAG, "Erreur IOException \n" + e.getMessage());
			}
		}

		return sb.toString();
	}

	/**
	 * V�rifie si l'appareil est connect�, indique si la connectivit� r�seau existe et 
	 * qu'il est possible d'�tablir des connexions et transmettre des donn�es. <br/>
	 * Requiert l'autorisation, dans le AndroidManifest.xml, android.permission.ACCESS_NETWORK_STATE
	 */
	public static boolean isConnected()
	{
		boolean isConnected = false;
		// le contexte de l'activit� est utilis� pour v�rifier la connectivit� du r�seau
		Context context = GlobalVars.getAppContext();

		// Acc�der au service de connectivit�
		String service = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(service);
		//boolean b = connectivity.getBackgroundDataSetting();

		NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
		if(activeNetwork == null)
		{
			Log.e(TAG, "Le r�seau n'est pas actif");
		}
		else 
		{
			//int type = activeNetwork.getType();
			//Log.i(TAG, "type = " + type);

			// Sp�cifier que le r�seau en question est de type WIFI
			int network = ConnectivityManager.TYPE_WIFI;
			NetworkInfo mobileNetwork = connectivity.getNetworkInfo(network);

			// Ici on obtient l'�tat : CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED, SUSPENDED ou UNKNOWN
			NetworkInfo.State state = mobileNetwork.getState();
			NetworkInfo.DetailedState d_state = mobileNetwork.getDetailedState();

			// On affiche l'�tat du r�seau 
			//Log.i(TAG, "Etat du r�seau : " + state.name());

			//if (state.name() == "CONNECTED")
			if ((state.name() == "CONNECTED") || (state.name() == "UNKNOWN")) //  TODO : UNKNOWN pour l'�mulateur � sup apr�s test
			{
				// On affiche l'�tat du r�seau 
				//Log.i(TAG, "Etat du r�seau : " + state.name());
				//Log.i(TAG, "Disponibilit� du r�seau OK");
				isConnected =  true;
			}
			else
			{
				// On affiche l'�tat du r�seau 
				Log.e(TAG, "Etat du r�seau : " + state.name());
				Log.e(TAG, "R�seau indisponible");
			}
		}

		return isConnected;
	}

	/** 
	 * Ajoute les param�tres et les en-t�tes associ�es � la requ�te m�thode POST 
	 * lors de l'envoie d'un document JSON 
	 * @param jsonData	document JSON � envoyer
	 * @throws Exception
	 */
	public void SendJsonPost(String jsonData) throws Exception
	{	
		//Log.d(TAG, "url : " + url);

		HttpPost requestPost = new HttpPost(url);

		//add headers
		for(NameValuePair h : headers)
		{
			requestPost.addHeader(h.getName(), h.getValue());
		}

		if(!params.isEmpty()){
			requestPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}

		requestPost.setEntity(new ByteArrayEntity(jsonData.getBytes("UTF8")));

		executeRequest(requestPost, url);
	}

	/** 
	 * Ajoute les param�tres et les en-t�tes associ�es � la requ�te m�thode PUT 
	 * lors de l'envoie d'un document JSON 
	 * @param jsonData	document JSON � envoyer
	 * @throws Exception
	 */
	public void SendJsonPut(String jsonData) throws Exception
	{	
		//Log.d(TAG, "url : " + url);

		HttpPut requestPut = new HttpPut(url);

		//add headers
		for(NameValuePair h : headers)
		{
			requestPut.addHeader(h.getName(), h.getValue());
		}

		if(!params.isEmpty()){
			requestPut.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}	

		StringEntity stringEntity = new StringEntity(jsonData, "UTF-8");
		requestPut.setEntity(stringEntity);

		executeRequest(requestPut, url);
	}
}

package com.polytech.gestetu;

import android.app.Application;
import android.content.Context;

/**
 * Fichier de configuration de l'application
 * Fournit les variables globales à toute l'application
 *
 * @author Nicolas Rosado <nicolas.rosado@atos.net>
 */

public class GlobalVars extends Application {

	//déclaration des variables globales
	private static final String TAG = "GlobalVars";
	
	// URL utilisée par l'application	
	private final static String url = "http://gestetu.dyndns.org:80/"; // url web service GESTNOTE 
	
	// Numéro de version de l'application
	private final static double applicationVersion = 0.1;
	
	// login de l'utilisateur connecté
	private static String login;
	
	// password de l'utilisateur connecté
	private static String password;
	
	private static boolean sortByLastName = true;
	
	private static boolean sortByFirstName = false;

	// timeout de connexion
	// Définit le délai en millisecondes jusqu'à  ce qu'une connexion est établie.
	// La valeur par défaut est zéro, cela signifie que le délai d'attente n'est pas utilisé.
	private static final int timeOutConnection = 10000;
	
	// timeout de retour
	// Définit le délai d'attente socket par défaut (SO_TIMEOUT)
	// En millisecondes, ce qui est le délai d'attente pour les données.
	private static final int timeOutSocket = 20000;
	
	// contexte de l'application
    private static Context context;
    
    /**
	 * Méthode appelée au lancement de l'application
     */
	@Override
    public void onCreate()
    {
    	super.onCreate();
        // Initialisation du contexte de l'application
        GlobalVars.context = getApplicationContext();
    }
    
	/** 
	 * Accesseur GET de l'attribut url
	 * @return l'attribut url
	 */
	public static String getUrl() {
		return url;
	}
	
	/** 
	 * Accesseur GET de l'attribut applicationVersion
	 * @return l'attribut applicationVersion
	 */
	public static double getApplicationVersion() {
		return applicationVersion;
	}
	
	/** 
	 * Accesseur GET de l'attribut login
	 * @return l'attribut login
	 */
	public static String getLogin() {
		return login;
	}

	/** 
	 * Accesseur SET de l'attribut login
	 * @param l'attribut login
	 */
	public static void setLogin(String login) {
		GlobalVars.login = login;
	}
	
	/** 
	 * Accesseur GET de l'attribut password
	 * @return l'attribut password
	 */
	public static String getPassword() {
		return password;
	}

	/** 
	 * Accesseur SET de l'attribut password
	 * @param l'attribut password
	 */
	public static void setPassword(String password) {
		GlobalVars.password = password;
	}
	
	/** 
	 * Accesseur GET de l'attribut sortByLastName
	 * @return l'attribut sortByLastName
	 */
	public static boolean getSortByLastName() {
		return sortByLastName;
	}

	/** 
	 * Accesseur SET de l'attribut sortByLastName
	 * @param l'attribut sortByLastName
	 */
	public static void setSortByLastName(boolean sortByLastName) {
		GlobalVars.sortByLastName = sortByLastName;
	}

	/** 
	 * Accesseur GET de l'attribut sortByFirstName
	 * @return l'attribut sortByFirstName
	 */
	public static boolean getSortByFirstName() {
		return sortByFirstName;
	}

	/** 
	 * Accesseur SET de l'attribut sortByFirstName
	 * @param l'attribut sortByFirstName
	 */
	public static void setSortByFirstName(boolean sortByFirstName) {
		GlobalVars.sortByFirstName = sortByFirstName;
	}
	
	/** 
	 * Accesseur GET de l'attribut timeOutConnection
	 * @return l'attribut timeOutConnection
	 */
	public static int getTimeOutConnection() {
		return timeOutConnection;
	}

	/** 
	 * Accesseur GET de l'attribut timeOutSocket
	 * @return l'attribut timeOutSocket
	 */
	public static int getTimeOutSocket() {
		return timeOutSocket;
	}

	/**
	 * Accesseir GET de l'attribut context
	 * @return le context de l'application
	 */
    public static Context getAppContext() {
        return GlobalVars.context;
    }
}

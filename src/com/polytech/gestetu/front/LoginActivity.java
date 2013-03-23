package com.polytech.gestetu.front;

import java.util.ArrayList;
import java.util.List;

import com.polytech.gestetu.GlobalVars;
import com.polytech.gestetu.R;
import com.polytech.gestetu.models.Student;
import com.polytech.gestetu.services.LoginService;
import com.polytech.gestetu.services.RetrievesStudentsService;

import android.R.menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";
	private static ProgressDialog mDialog; 
	private static String token;
	public static LoginActivity activityLogin;
	private static EditText email;
	private static EditText password;
	
	/**
	 * Méthode appelée en premier lorsque l'activité est crée
	 * @param Bundle savedInstanceState contient l’état de sauvegarde enregistré lors de la dernière exécution de l'activité
	 * @return l'attribut response
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		activityLogin = this;
		
		email = (EditText)findViewById(R.id.editTextEmail);
		password = (EditText)findViewById(R.id.editTextPassword);
		
        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent webBrowser = new Intent(android.content.Intent.ACTION_VIEW);
                webBrowser.setData(Uri.parse("http://gestetu.dyndns.org/signup"));
                    startActivity(webBrowser);
             }
        });     
        
        Button buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
        		//affichage d'une progressDialog
        		mDialog = new ProgressDialog(v.getContext());
        		mDialog.setMessage("Patientez s'il-vous-plait...");
        		mDialog.setCancelable(false);
        		mDialog.show();

        		//Initialize thread class
        		LoginService loginService = new LoginService(email.getText().toString(), password.getText().toString());
        		//Start thread
        		loginService.start();

        		//Step 4: Call join() to wait until fib thread finishes
        		try{
        			loginService.join();
        		}catch(InterruptedException ie){}

        		int mHandlerMessage = loginService.getMHandler();

        		if (mHandlerMessage != 0)
        		{
        			mHandler.sendEmptyMessage(mHandlerMessage);
        		}

        		//Step 5: call getRetrievesStudents() to get the return value
        		token = loginService.getRetrievesToken();

        		Log.d(TAG, "token value : " + token);
        		
        		if (token != null)
        		{
	        		GlobalVars.setTokenValue(token);
	            	
					//ferme l'activity en cours (login) et ouvre le menu
					finish();	
	
					Intent menu = new Intent(getBaseContext(), MenuActivity.class);					
					startActivity(menu);
        		}
             }
        });  
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
				Toast.makeText(GlobalVars.getAppContext(), "Authentification succeful", Toast.LENGTH_SHORT).show();
				break;

			case 30:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbPost = new AlertDialog.Builder(activityLogin);
				adbPost.setTitle("Error");
				adbPost.setMessage("Incorrect credentials. \nThank you verify your username and password. \n\nIf the problem persists, contact your administrator.");
				adbPost.setPositiveButton("Ok", null);
				adbPost.show();
				break;

			case 40:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbPromotionsNetwork = new AlertDialog.Builder(activityLogin);
				adbPromotionsNetwork.setTitle("Error");
				adbPromotionsNetwork.setMessage("Retrieval of token login impossible, following a network problem.\nThank you for your connectivity check.");
				adbPromotionsNetwork.setPositiveButton("Ok", null);
				adbPromotionsNetwork.show();
				break;

			case 50:
				mDialog.dismiss();
				//on affiche une boite de dialogue pour signaler l'erreur
				AlertDialog.Builder adbRetrievesPromotions = new AlertDialog.Builder(activityLogin);
				adbRetrievesPromotions.setTitle("Error");
				adbRetrievesPromotions.setMessage("Problem occurred when retrieving token login.\n\nIf the problem persists, contact your administrator.");
				adbRetrievesPromotions.setPositiveButton("Ok", null);
				adbRetrievesPromotions.show();
				break;
			}
		}
	};

}

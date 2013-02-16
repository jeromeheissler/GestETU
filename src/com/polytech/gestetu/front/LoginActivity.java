package com.polytech.gestetu.front;

import com.polytech.gestetu.R;

import android.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	/**
	 * M�thode appel�e en premier lorsque l'activit� est cr�e
	 * @param Bundle savedInstanceState contient l��tat de sauvegarde enregistr� lors de la derni�re ex�cution de l'activit�
	 * @return l'attribut response
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
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
            	
				//ferme l'activity en cours (login) et ouvre le menu
				finish();	

				Intent menu = new Intent(getBaseContext(), MenuActivity.class);					
				startActivity(menu);
             }
        });  
	}

}

package com.polytech.gestetu.front;

import com.polytech.gestetu.R;

import android.app.Activity;
import android.os.Bundle;

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
	}

}

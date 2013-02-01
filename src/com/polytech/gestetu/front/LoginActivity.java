package com.polytech.gestetu.front;

import com.polytech.gestetu.R;

import android.app.Activity;
import android.os.Bundle;

public class LoginActivity extends Activity {
	
	/**
	 * Méthode appelée en premier lorsque l'activité est crée
	 * @param Bundle savedInstanceState contient l’état de sauvegarde enregistré lors de la dernière exécution de l'activité
	 * @return l'attribut response
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

}

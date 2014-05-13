package fr.drusy.wisper;

import org.cocos2dx.lib.Cocos2dxActivity;

import android.os.Bundle;

public class Wisper extends Cocos2dxActivity{

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
    static {
         System.loadLibrary("game");
    }
}

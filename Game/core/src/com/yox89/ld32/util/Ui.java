package com.yox89.ld32.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Ui {

	private static final String UI_INVENTORY_STRING = "Mirrors left: ";
	
	private Stage uiStage;
	private Label inventoryLabel;
	private int numberTotalMirrors;

	public Ui(Stage uiStage, int numberTotalMirrors) {
		this.numberTotalMirrors = numberTotalMirrors;
		this.uiStage = uiStage;
		inventoryLabel = new Label("Inventory", 
				new LabelStyle(new BitmapFont(), Color.WHITE)) {
			
			@Override
			public void act(float delta) {
				super.act(delta);

			}
		};
		uiStage.addActor(inventoryLabel);
		inventoryLabel.setText( UI_INVENTORY_STRING + numberTotalMirrors + "/" + numberTotalMirrors );
		inventoryLabel.setFontScale(1f);
		inventoryLabel.setPosition(20, 20);
	}

	public void setMirrorsLeftText(String mirrorsLeft) {
		inventoryLabel.setText( UI_INVENTORY_STRING + mirrorsLeft + "/" + numberTotalMirrors );
		
	}

}

package com.yox89.ld32.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.yox89.ld32.screens.TiledLevelScreen;

public class Ui {

	private static final String UI_INVENTORY_STRING = "Mirrors left: ";
	
	private Stage uiStage;
	private Label inventoryLabel;
	private int numberTotalMirrors;

	public Ui(TiledLevelScreen tiledLevelScreen,Stage uiStage, int numberTotalMirrors) {
		this.numberTotalMirrors = numberTotalMirrors;
		this.uiStage = uiStage;
		
		final Texture img = tiledLevelScreen.manage(new Texture("ui_tab.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		UiImageActor leftUiImage = new UiImageActor(img,70f);
		UiImageActor rightUiImage = new UiImageActor(img,70f,true);
		uiStage.addActor(leftUiImage);
		uiStage.addActor(rightUiImage);
		rightUiImage.setPosition(uiStage.getWidth()- rightUiImage.getWidth(), 0);
		
		inventoryLabel = new Label(UI_INVENTORY_STRING + numberTotalMirrors + "/" + numberTotalMirrors, 
				new LabelStyle(new BitmapFont(), Color.WHITE));
		uiStage.addActor(inventoryLabel);
		inventoryLabel.setFontScale(1f);
		inventoryLabel.setPosition(20, 5);
		
		Label restartInfo = new Label("'R' to restart", 
				new LabelStyle(new BitmapFont(), Color.WHITE));
		uiStage.addActor(restartInfo);
		restartInfo.setFontScale(1f);
		restartInfo.setPosition(uiStage.getWidth()- (20 +restartInfo.getWidth()), 5);
		restartInfo.addAction(Actions.forever(Actions.sequence(
				Actions.fadeOut(3f, Interpolation.pow5In),
				Actions.delay(3f),
				Actions.fadeIn(3f,  Interpolation.pow5In))));
		
		Label menuInfo = new Label("'Q' for menu", 
				new LabelStyle(new BitmapFont(), Color.WHITE));
		uiStage.addActor(menuInfo);
		menuInfo.setFontScale(1f);
		menuInfo.setPosition(uiStage.getWidth()- (20 +restartInfo.getWidth()), 5);
		menuInfo.addAction(Actions.fadeOut(0f));
		menuInfo.addAction(Actions.forever(Actions.sequence(
				Actions.fadeIn(3f, Interpolation.pow5In),
				Actions.delay(3f),
				Actions.fadeOut(3f,  Interpolation.pow5In))));

	}

	public void setMirrorsLeftText(String mirrorsLeft) {
		inventoryLabel.setText( UI_INVENTORY_STRING + mirrorsLeft + "/" + numberTotalMirrors );
		
	}

	
	private class UiImageActor extends Actor {

		private final Texture mTexture;
		private boolean flippedHorizontally;

		public UiImageActor(Texture tex, final float size) {
			this(tex,size,false);
					
		}
		public UiImageActor(Texture tex, final float size, boolean flippedHorizontally) {
			this.flippedHorizontally = flippedHorizontally;
			mTexture = tex;
			setSize(size*2, size*0.8f);
					
		}


		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), flippedHorizontally, false);
		}
	}
}

package com.yox89.ld32.util;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.yox89.ld32.screens.TiledLevelScreen;

public class Ui {

	private static final String MAP_PROPERTIES_TIP = "tip";
	private static final String UI_INVENTORY_STRING = "x ";

	private static final float UI_HORIZONTAL_MARGIN = 20f;
	public static final float UI_TEXT_GHOSTLY_FALL_DISTANCE = 15f;

	private Stage uiStage;
	private HashMap<String, Label> inventoryLabels = new HashMap<String, Label>();
	private HashMap<String, UiImageActor> inventoryImages = new HashMap<String, UiImageActor>();
	private MirrorInventory mirrorInventory;
	private Label tipText;
	private boolean fluffVisible;
	private Stage gameStage;
	private String activeMirrorType = MirrorInventory.MIRROR_TYPE_NORMAL;

	public Ui(TiledLevelScreen tiledLevelScreen, Stage gameStage,
			Stage uiStage, MirrorInventory mirrorInventory,
			MapProperties properties, int currentLevelId) {

		this.mirrorInventory = mirrorInventory;

		this.uiStage = uiStage;
		this.gameStage = gameStage;

		final Texture img = tiledLevelScreen.manage(new Texture("ui_tab.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		UiImageActor leftUiImage = new UiImageActor(img, 140f, 56f);
		uiStage.addActor(leftUiImage);

		final Texture normalMirrorIconTexture = tiledLevelScreen
				.manage(new Texture("mirror.png"));
		normalMirrorIconTexture.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		UiImageActor normalMirrorIcon = new UiImageActor(
				normalMirrorIconTexture, 16f, 16f);
		uiStage.addActor(normalMirrorIcon);
		normalMirrorIcon.setPosition(UI_HORIZONTAL_MARGIN, 5);
		normalMirrorIcon.setRotation(45);

		Label normalMirrorsLabel = new Label(
				UI_INVENTORY_STRING
						+ mirrorInventory
								.getMirrorsLeft(MirrorInventory.MIRROR_TYPE_NORMAL),
				new LabelStyle(new BitmapFont(), Color.WHITE));
		inventoryImages.put(MirrorInventory.MIRROR_TYPE_NORMAL,
				normalMirrorIcon);
		inventoryLabels.put(MirrorInventory.MIRROR_TYPE_NORMAL,
				normalMirrorsLabel);
		uiStage.addActor(normalMirrorsLabel);
		normalMirrorsLabel.setFontScale(1f);
		normalMirrorsLabel.setPosition(UI_HORIZONTAL_MARGIN * 2, 5);

		final Texture splitterMirrorIconTexture = tiledLevelScreen
				.manage(new Texture("mirror_splitter.png"));
		normalMirrorIconTexture.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);
		UiImageActor splitterMirrorIcon = new UiImageActor(
				splitterMirrorIconTexture, 16f, 16f);
		uiStage.addActor(splitterMirrorIcon);
		splitterMirrorIcon.setPosition(UI_HORIZONTAL_MARGIN * 4, 5);
		splitterMirrorIcon.setRotation(45);

		Label splitterMirrorsLabel = new Label(
				UI_INVENTORY_STRING
						+ mirrorInventory
								.getMirrorsLeft(MirrorInventory.MIRROR_TYPE_SPLITTER),
				new LabelStyle(new BitmapFont(), Color.WHITE));
		inventoryImages.put(MirrorInventory.MIRROR_TYPE_SPLITTER,
				splitterMirrorIcon);
		inventoryLabels.put(MirrorInventory.MIRROR_TYPE_SPLITTER,
				splitterMirrorsLabel);
		uiStage.addActor(splitterMirrorsLabel);
		splitterMirrorsLabel.setFontScale(1f);
		splitterMirrorsLabel.setPosition(UI_HORIZONTAL_MARGIN * 5, 5);
		splitterMirrorsLabel.setColor(1f, 1f, 1f, 0.5f);

		UiImageActor rightUiImage = new UiImageActor(img, 140f, 56f, true);
		uiStage.addActor(rightUiImage);
		rightUiImage.setPosition(uiStage.getWidth() - rightUiImage.getWidth(),
				0);

		Label restartInfo = new Label("'R' to restart", new LabelStyle(
				new BitmapFont(), Color.WHITE));
		uiStage.addActor(restartInfo);
		restartInfo.setPosition(uiStage.getWidth()
				- (UI_HORIZONTAL_MARGIN + restartInfo.getWidth()), 5);
		restartInfo.addAction(Actions.forever(Actions.sequence(
				Actions.fadeOut(3f, Interpolation.pow5In), Actions.delay(3f),
				Actions.fadeIn(3f, Interpolation.pow5In))));

		Label menuInfo = new Label("'Q' for menu", new LabelStyle(
				new BitmapFont(), Color.WHITE));
		uiStage.addActor(menuInfo);
		menuInfo.setPosition(uiStage.getWidth()
				- (UI_HORIZONTAL_MARGIN + restartInfo.getWidth()), 5);
		menuInfo.addAction(Actions.fadeOut(0f));
		menuInfo.addAction(Actions.forever(Actions.sequence(
				Actions.fadeIn(3f, Interpolation.pow5In), Actions.delay(3f),
				Actions.fadeOut(3f, Interpolation.pow5In))));

		String tip = (properties.containsKey(MAP_PROPERTIES_TIP)) ? ""
				+ properties.get(MAP_PROPERTIES_TIP) : "";
		tipText = new Label(tip.replace("#", "\n"), new LabelStyle(
				new BitmapFont(), Color.WHITE));

		tipText.setFontScale(1.25f);
		tipText.setAlignment(Align.center);

		uiStage.addActor(tipText);
		fluffVisible = true;
		tipText.setPosition(uiStage.getWidth() / 2 - tipText.getMinWidth()
				/ (2 * tipText.getFontScaleX()), 75);

		tipText.addAction(Actions.sequence(Actions.delay(10f), Actions
				.parallel(Actions.fadeOut(1f, Interpolation.sine),
						Actions.moveBy(0, -UI_TEXT_GHOSTLY_FALL_DISTANCE, 1f))));

		setActiveMirrorType(MirrorInventory.MIRROR_TYPE_NORMAL);

		if (currentLevelId == 1) {
			final UiImageActor arrow = new UiImageActor(
					tiledLevelScreen.manage(new Texture("arrow.png")), 32f, 64f);
			arrow.setPosition(leftUiImage.getX() + leftUiImage.getWidth() / 3,
					leftUiImage.getY() + leftUiImage.getHeight());

			final SequenceAction bounceSeq = Actions.sequence(
					Actions.moveBy(0f, -20f, .5f, Interpolation.sine),
					Actions.moveBy(0f, 20f, .5f, Interpolation.sine));
			final RepeatAction bounce = Actions.repeat(3, bounceSeq);
			arrow.addAction(Actions.sequence(
					bounce,
					Actions.parallel(Actions.fadeOut(.45f),
							Actions.moveBy(0f, -20f, .5f, Interpolation.sine)),
					Actions.removeActor()));

			uiStage.addActor(arrow);
		}
	}

	public void removeFluffText() {
		if (fluffVisible) {
			fluffVisible = false;
			tipText.addAction(Actions.delay(1f, Actions.parallel(
					Actions.fadeOut(1f, Interpolation.sine),
					Actions.moveBy(0, -UI_TEXT_GHOSTLY_FALL_DISTANCE, 1f))));
		}
	}

	private class UiImageActor extends Actor {

		private final Texture mTexture;
		private boolean flippedHorizontally;

		public UiImageActor(Texture tex, final float width, final float height) {
			this(tex, width, height, false);

		}

		public UiImageActor(Texture tex, final float width, final float height,
				boolean flippedHorizontally) {
			this.flippedHorizontally = flippedHorizontally;
			mTexture = tex;
			setSize(width, height);

		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setColor(getColor());
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), flippedHorizontally, false);
		}
	}

	public void showMutter(String muttering, Vector2 pos) {
		Label innerThoughts = new Label(muttering, new LabelStyle(
				new BitmapFont(), Color.WHITE));
		uiStage.addActor(innerThoughts);
		pos = gameStage.stageToScreenCoordinates(pos);
		pos = uiStage.screenToStageCoordinates(pos);
		innerThoughts.setPosition(pos.x - innerThoughts.getWidth() / 2,
				pos.y + 5);
		innerThoughts
				.addAction(Actions.sequence(Actions.delay(0.5f), Actions
						.parallel(Actions.fadeOut(2f, Interpolation.sine),
								Actions.moveBy(0,
										-UI_TEXT_GHOSTLY_FALL_DISTANCE, 4f))));

	}

	public void setActiveMirrorType(final String mirrorType) {
		this.activeMirrorType = mirrorType;
		for (String type : inventoryLabels.keySet()) {
			if (type.equals(mirrorType)) {
				inventoryLabels.get(type).setColor(1f, 1f, 1f, 1f);
				inventoryImages.get(type).setColor(1f, 1f, 1f, 1f);
			} else {
				inventoryLabels.get(type).setColor(1f, 1f, 1f, 0.5f);
				inventoryImages.get(type).setColor(1f, 1f, 1f, .5f);
			}
		}

	}

	public void performMirrorAction(int add) {
		performMirrorAction(add, activeMirrorType);

	}

	public void performMirrorAction(int add, final String mirrorType) {
		mirrorInventory.addMirror(add, mirrorType);
		final Label activeLabel = inventoryLabels.get(mirrorType);
		activeLabel.addAction(Actions.sequence(

		Actions.moveBy(0, 5f, 0.2f, Interpolation.sineIn),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						activeLabel.setText(UI_INVENTORY_STRING
								+ mirrorInventory.getMirrorsLeft(mirrorType));

					}
				}), Actions.moveBy(0, -5f, 0.4f, Interpolation.bounceOut)));

	}

	public boolean hasMirrorLeft() {
		return mirrorInventory.hasMirrorLeft(activeMirrorType);

	}

	public String getActiveMirrorType() {
		return activeMirrorType;
	}

}

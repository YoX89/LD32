package com.yox89.ld32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.util.Assets;

public class FloorActor extends Actor implements Disposable {

	private final Texture mTexture;

	public FloorActor() {
		setTouchable(Touchable.disabled);
		mTexture = new Texture(Gdx.files.internal("cobblestone.png"));
		mTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(mTexture, getX(), getY(), getWidth(), getHeight(), 0, 0,
				getWidth(), getHeight());

		batch.setShader(null);
	}

	@Override
	public void dispose() {
		mTexture.dispose();
	}
}

package com.yox89.ld32.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class ShadyActor extends Actor implements Disposable {

	ShaderProgram shader;
	private final Texture mTexture;

	private float mTime;

	public ShadyActor(Texture texture) {
		mTexture = texture;
		mTime = 0f;
		rebuildShader();
		if (shader == null) {
			throw new IllegalArgumentException("Error compiling shader: "
					+ shader.getLog());
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mTime += delta;
	}

	private void rebuildShader() {
		String vertexShader = "attribute vec4 "
				+ ShaderProgram.POSITION_ATTRIBUTE
				+ ";\n" //
				+ "attribute vec4 "
				+ ShaderProgram.COLOR_ATTRIBUTE
				+ ";\n" //
				+ "attribute vec2 "
				+ ShaderProgram.TEXCOORD_ATTRIBUTE
				+ "0;\n" //
				+ "uniform float time;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = "
				+ ShaderProgram.COLOR_ATTRIBUTE
				+ ";\n" //
				+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
				+ "   v_texCoords = "
				+ ShaderProgram.TEXCOORD_ATTRIBUTE
				+ "0;\n" //
				+ "   gl_Position =  (u_projTrans * "
				+ ShaderProgram.POSITION_ATTRIBUTE + ") + vec4(1, 1, 0, 0.75 + cos(time)/4.0);\n" //
				+ "}\n";
		String fragmentShader = "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
				+ "}";

		ShaderProgram.pedantic = false;
		final ShaderProgram sh = new ShaderProgram(vertexShader, fragmentShader);
		if (sh.isCompiled()) {
			if (shader != null) {
				shader.dispose();
			}
			shader = sh;
		} else {
			System.err.println("Unable to compile: " + sh.getLog());
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		// TODO don't constantly rebuild shader when we release. Keep around for debugging only
		rebuildShader();

		batch.setShader(shader);
		shader.setUniformf("time", mTime);

		batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation(), 0, 0, mTexture.getWidth(), mTexture.getHeight(),
				false, false);

		batch.setShader(null);
	}

	@Override
	public void dispose() {
		if (shader != null) {
			shader.dispose();
		}
	}
}

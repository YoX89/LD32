package com.yox89.ld32.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Assets {

	private static Array<Texture> sTextures = new Array<Texture>();
	private static ObjectMap<String, TextureRegion> sRegions = new ObjectMap<String, TextureRegion>();

	public static TextureAtlas atlas;

	public static TextureRegion background;

	public static TextureRegion alphabet, smiley, earthCore, island, pole,
			silo, cloud, beaver, blood, pool, tower, top_fin_rocket;
	public static SoundRef beaver_death;

	public static void init() throws IOException {
		atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));

		background = find("background");

		alphabet = find("alphabet");
		smiley = find("smiley");
		earthCore = find("earthcore");
		island = find("island");
		pole = find("pole");
		silo = find("silo");
		cloud = find("cloud");
		beaver = find("beaver");
		blood = find("blood");
		pool = find("pool");
		tower = find("tower");
		top_fin_rocket = find("rocket");
		beaver_death = new SoundRef(Gdx.audio.newSound(Gdx.files
				.internal("aaaah.ogg")));

		final XmlReader reader = new XmlReader();
		FileHandle root = Gdx.files.internal("Spritesheet");
		for (FileHandle child : root.list()) {
			String name = child.name();
			if (name.endsWith(".xml")) {
				final Element sheetDef = reader.parse(child);
				final FileHandle imgHandle = root.child(name.substring(0,
						name.indexOf('.'))
						+ ".png");
				Texture tex = new Texture(imgHandle);
				sTextures.add(tex);

				for (int i = 0; i < sheetDef.getChildCount(); i++) {
					final Element regionDef = sheetDef.getChild(i);
					final String regionName = regionDef.getAttribute("name");
					final TextureRegion region = new TextureRegion(tex,
							regionDef.getInt("x"), regionDef.getInt("y"),
							regionDef.getInt("width"),
							regionDef.getInt("height"));

					sRegions.put(
							regionName.substring(0, regionName.indexOf('.')),
							region);
				}
			}
		}
	}

	private static TextureRegion find(String name) {
		return atlas.findRegion(name);
	}

	public static TextureRegion get(String name) {
		return sRegions.get(name);
	}

	public static class SoundRef {
		public Sound sound;

		public SoundRef(Sound s) {
			this.sound = s;
		}

		public void play() {
			sound.play(0.5f);
		}

		public void dispose() {
			sound.dispose();
		}
	}

	public static void dispose() {
		for (Texture tex : sTextures) {
			tex.dispose();
		}
		sTextures.clear();
		sRegions.clear();

		atlas.dispose();
		beaver_death.dispose();

	}
}

package com.yox89.ld32.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Assets {
	private static Array<Disposable> sDisposables = new Array<Disposable>();

	private static ObjectMap<String, TextureRegion> sRegions = new ObjectMap<String, TextureRegion>();

	public static TextureAtlas atlas;

	public static TextureRegion background;

	public static TextureRegion alphabet, smiley, earthCore, island, pole,
			silo, cloud, beaver, blood, pool, tower, top_fin_rocket;
	public static SoundRef beaver_death, laser;
	public static Music bg_music;

	public static Texture sound_on, sound_off, skipLevelButton, ectoplasm;

	private static boolean sSoundEnabled = false;

	public static ParticlePool particlePool;

	public static void init() throws IOException {
		atlas = manage(new TextureAtlas(Gdx.files.internal("pack.atlas")));

		particlePool = new ParticlePool();
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
		ectoplasm = manage(new Texture(Gdx.files.internal("ectoplasm.png")));
		beaver_death = manage(new SoundRef(Gdx.audio.newSound(Gdx.files
				.internal("aaaah.ogg"))));
		laser = manage(new SoundRef(Gdx.audio.newSound(Gdx.files
				.internal("sfx/laser.wav"))));
		bg_music = manage(Gdx.audio.newMusic(Gdx.files.internal("LD32.ogg")));
		bg_music.setLooping(true);
		if (sSoundEnabled) {
			bg_music.play();
		}

		sound_on = manage(new Texture(Gdx.files.internal("sound_on.png")));
		sound_off = manage(new Texture(Gdx.files.internal("sound_off.png")));
		skipLevelButton = manage(new Texture(
				Gdx.files.internal("skip_level_btn.png")));

		final XmlReader reader = new XmlReader();
		FileHandle root = Gdx.files.internal("Spritesheet");
		for (FileHandle child : root.list()) {
			String name = child.name();
			if (name.endsWith(".xml")) {
				final Element sheetDef = reader.parse(child);
				final FileHandle imgHandle = root.child(name.substring(0,
						name.indexOf('.'))
						+ ".png");
				Texture tex = manage(new Texture(imgHandle));

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

	private static <T extends Disposable> T manage(T d) {
		sDisposables.add(d);
		return d;

	}

	private static TextureRegion find(String name) {
		return atlas.findRegion(name);
	}

	public static TextureRegion get(String name) {
		return sRegions.get(name);
	}

	public static class SoundRef implements Disposable {
		public Sound sound;

		public SoundRef(Sound s) {
			this.sound = s;
		}

		public void play() {
			if (sSoundEnabled) {
				sound.play(0.5f);
			}
		}

		public void dispose() {
			sound.dispose();
		}
	}

	public static void dispose() {
		for (Disposable d : sDisposables) {
			d.dispose();
		}
		sDisposables.clear();
		sRegions.clear();
	}

	public static boolean isSoundEnabled() {
		return sSoundEnabled;
	}

	public static void setSoundEnabled(boolean enabled) {
		if (enabled != sSoundEnabled) {
			sSoundEnabled = enabled;
			if (!sSoundEnabled) {
				bg_music.pause();
			} else {
				bg_music.play();
			}
		}
	}
}

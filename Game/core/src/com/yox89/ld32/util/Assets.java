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

	public static SoundRef beaver_death, laser;
	public static Music bg_music;

	public static Texture sound_on, sound_off, skipLevelButton;

	public static TextureRegion ectoplasm;

	private static boolean sSoundEnabled = true;

	public static ParticlePool particlePool;

	public static void init() throws IOException {

		particlePool = new ParticlePool();

		ectoplasm = new TextureRegion(manage(new Texture(
				Gdx.files.internal("ectoplasm.png"))));
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
		final String[] sheetNames = new String[] { "spritesheet_backtiles.xml",
				"spritesheet_balls.xml", "spritesheet_coins.xml",
				"spritesheet_paddles.xml", "spritesheet_particles.xml",
				"spritesheet_pipes.xml", "spritesheet_tilesBlack.xml",
				"spritesheet_tilesBlue.xml", "spritesheet_tilesGreen.xml",
				"spritesheet_tilesGrey.xml", "spritesheet_tilesOranges.xml",
				"spritesheet_tilesPink.xml", "spritesheet_tilesRed.xml",
				"spritesheet_tilesYellow.xml", };
		for (String sheetName : sheetNames) {
			final FileHandle child = Gdx.files.internal("Spritesheet/"
					+ sheetName);
			final String name = child.name();
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

package com.yox89.ld32.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Assets {

	private static Array<Texture> sTextures = new Array<Texture>();
	private static ObjectMap<String, TextureRegion> sRegions = new ObjectMap<String, TextureRegion>();

	public static void init() throws IOException {

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

	public static TextureRegion get(String name) {
		return sRegions.get(name);
	}

	public static void dispose() {
		for (Texture tex : sTextures) {
			tex.dispose();
		}
		sTextures.clear();
		sRegions.clear();
	}
}

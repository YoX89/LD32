package com.yox89.ld32.util;

import java.util.HashMap;

import com.badlogic.gdx.maps.MapProperties;

public class MirrorInventory {
	
	public static final String MIRROR_TYPE_NORMAL = "mirror_normal";
	public static final String MIRROR_TYPE_SPLITTER= "mirror_splitter";
	private HashMap<String, int[]> mirrorTypes;
	
	
	public MirrorInventory(MapProperties properties) {
		mirrorTypes = new HashMap<String, int[]>();
		int normalMirrors = properties.containsKey(MIRROR_TYPE_NORMAL) ?Integer.parseInt(""+properties.get(MIRROR_TYPE_NORMAL)):0;
		mirrorTypes.put(MIRROR_TYPE_NORMAL,new int[]{normalMirrors,normalMirrors});
		
	}
	
	public int getMirrorsLeft(String mirrorType){
		return mirrorTypes.containsKey(mirrorType)?mirrorTypes.get(mirrorType)[0]:0;
		
	}
	
	public void addMirror(int amount, String mirrorType){
		if(mirrorTypes.containsKey(mirrorType)){
			mirrorTypes.get(mirrorType)[0]+=amount; 
		}
	}	
	
	public boolean hasMirrorLeft(String mirrorType){
		return mirrorTypes.containsKey(mirrorType)?mirrorTypes.get(mirrorType)[0]>0:false;
	}

	public int getMirrorsTotal(String mirrorType) {
		return mirrorTypes.containsKey(mirrorType)?mirrorTypes.get(mirrorType)[1]:0;
	}
}

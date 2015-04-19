package com.cyntaks.GDXGIDE.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * MultiFont is used to treat a number of different BitmapFonts as one. In particular, this class is used to
 * manage fonts with identical types but different styles and sizes. This way, a user may create a font which
 * should be Courier but might vary in size and style without having to manage separate objects for each size
 * and style.
 * 
 * MultiFont requires a directory within the /fonts directory containing all the variations of a
 * particular font, the different styles and sizes of it in other words. The .png and .fnt files for
 * the font (as required by BitmapFont) must have exactly the same filenames aside from their extensions.
 * Additionally, their filenames (before the extension), must be suffixed with a hyphen and a number
 * indicating size and style, e.g. "sylfaen-24_plain.fnt" and "sylfaen-24_plain.png".
 * 
 * @author weston
 */

public class TypeFace {
	private HashMap<String, BitmapFont> fonts;
	private HashMap<String, int[]> sizesForStyles;
	private static final String FIRST_SEPARATOR = "-";
	private static final String SECOND_SEPARATOR = "_";
	
	public static final String STYLE_PLAIN = "plain";
	public static final String STYLE_BOLD = "bold";
	public static final String STYLE_ITALIC = "italic";
	private String typeName;
	
	/**
	 * @param fontDirectory the name of the directory within /fonts containing the appropriate font files.
	 */
	public TypeFace(String fontDirectory) {
		this.typeName = fontDirectory;
		fonts = new HashMap<String, BitmapFont>();
		sizesForStyles = new HashMap<String, int[]>();
		
		
		//I use this block to convert filenames to the correct format
		/*FileHandle[] temp = Gdx.files.internal("fonts/" + fontDirectory + "/temp").list();
		for (int i = 0; i < temp.length; i++) {
			String name = temp[i].nameWithoutExtension();
			if(temp[i].extension().equals("fnt"))
				name = name.substring(0, name.lastIndexOf(fontDirectory) + fontDirectory.length()) + "-" + name.substring(name.lastIndexOf(fontDirectory) + fontDirectory.length()) + "_plain." + temp[i].extension();
			else if(temp[i].extension().equals("png")) {
				name = name.substring(0, name.lastIndexOf(fontDirectory) + fontDirectory.length()) + "-" + name.substring(name.lastIndexOf(fontDirectory) + fontDirectory.length());
				name = name.substring(0, name.lastIndexOf("_") + 1) + "plain." + temp[i].extension();
			}
				
			temp[i].copyTo(Gdx.files.absolute("c:/" + name));
		}*/
		
		FileHandle[] files = Gdx.files.internal("fonts/" + fontDirectory).list();
		ArrayList<String> pairs = new ArrayList<String>();
		ArrayList<Integer> used = new ArrayList<Integer>();
		
		for (int i = 0; i < files.length; i++) {
			String first = null;
			if(!used.contains(i)) {
				int dotIndex = files[i].name().lastIndexOf(".");
				if(dotIndex > -1)
					first = files[i].name().substring(0, dotIndex);
			}
			if(first != null) {
				for (int j = i+1; j < files.length; j++) {
					String second = null;
					int dotIndex = files[j].name().lastIndexOf(".");
					if(dotIndex > -1)
						second = files[j].name().substring(0, dotIndex);
					if(second != null && second.equalsIgnoreCase(first)) {
						pairs.add(second);
						used.add(j);
					}
				}
			}
		}
		
		String directory = "fonts/" + fontDirectory + "/";
		for (String s : pairs) {
			if(filesPresent(files, s))
				fonts.put(s.substring(s.lastIndexOf(FIRST_SEPARATOR) + 1, s.length()), new BitmapFont(Gdx.files.internal(directory + s + ".fnt"), Gdx.files.internal(directory + s + ".png"), true));
		}
		
		collectSizesForStyles();
		
		///////////!!! -- add error handling!!!
	}
	
	private void collectSizesForStyles() {
		Iterator<String> iter = fonts.keySet().iterator();
		HashMap<String, ArrayList<Integer>> stylesToSizes = new HashMap<String, ArrayList<Integer>>();
		while(iter.hasNext()) { //go through the names of all fonts loaded (format: "size_style")
			String next = iter.next();
			String style = extractStyle(next);
			int size = extractSize(next);
			if(!stylesToSizes.containsKey(style))
				stylesToSizes.put(style, new ArrayList<Integer>());
			stylesToSizes.get(style).add(size);
		}
		
		Iterator<Entry<String, ArrayList<Integer>>> entries = stylesToSizes.entrySet().iterator();
		while(entries.hasNext()) {
			Entry<String, ArrayList<Integer>> entry = entries.next();
			ArrayList<Integer> tempSizes = entry.getValue();
			String style = entry.getKey();
			Collections.sort(tempSizes);
			int[] sizes = new int[tempSizes.size()];
			for (int i = 0; i < sizes.length; i++) {
				sizes[i] = tempSizes.get(i);
			}
			sizesForStyles.put(style, sizes);
		}
	}
	
	private int extractSize(String full) {
		return Integer.parseInt(full.substring(0, full.lastIndexOf(SECOND_SEPARATOR)));
	}
	
	private String extractStyle(String full) {
		return full.substring(full.lastIndexOf(SECOND_SEPARATOR) + 1);
	}
	
	public int[] getSizesForStyle(String style) {
		return sizesForStyles.get(style);
	}
	
	private boolean filesPresent(FileHandle[] files, String name) {
		boolean hasPNG = false;
		boolean hasFNT = false;
		for (int i = 0; i < files.length; i++) {
			if(files[i].name().equalsIgnoreCase(name + ".png"));
				hasPNG = true;
			if(files[i].name().equalsIgnoreCase(name + ".fnt"));
				hasFNT = true;
		}
		
		return hasPNG && hasFNT;
	}
	
	public BitmapFont getFont(int size, String style) {
		BitmapFont font = fonts.get("" + size + SECOND_SEPARATOR + style);
		if(font == null)
			throw new IllegalArgumentException(typeName + "| Size: " + size + " or style: " + style + " for font is invalid.");
		return font;
	}
}

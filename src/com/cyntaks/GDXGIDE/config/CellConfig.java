package com.cyntaks.GDXGIDE.config;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.CellBehavior;
import com.cyntaks.GDXGIDE.ResourceManager;
import com.cyntaks.GDXGIDE.text.TypeFace;
import com.cyntaks.GDXGIDE.util.ConfigFile;
import com.cyntaks.GDXGIDE.util.CorporealBox;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;

public class CellConfig {
	private String behaviorName;

	private String typeFaceName;
	private int fontSize;
	private String fontStyle;
	private Color fontColor;
	private boolean lockFontSize;

	private String titleTypeFaceName;
	private int titleFontSize;
	private String titleFontStyle;
	private Color titleFontColor;
	private boolean titleLockFontSize;

	private String underGraphicPath;
	private Color underGraphicColor;

	private String overGraphicPath;
	private Color overGraphicColor;

	private Color selectedColor;
	private Color finalizedSelectionColor;

	private float selectSpeed;
	private float deselectSpeed;
	
	private String cellClass;
	
	private ConfigFile map;

	private boolean loaded;
	
	public CellConfig(ConfigFile map) {
		this.map = map;
	}
	
	public void load() {
		if(!loaded) {
			setBehaviorName(map.getString("behaviorName"));
			
			setTypeFaceName(map.getString("typeFaceName"));
			setFontSize(map.getInt("fontSize"));
			fontStyle = map.getString("fontStyle");
			if(fontStyle != null) {
				if(fontStyle.equalsIgnoreCase("plain"))
					fontStyle = TypeFace.STYLE_PLAIN;
				else if(fontStyle.equalsIgnoreCase("bold"))
					fontStyle = TypeFace.STYLE_BOLD;
				else if(fontStyle.equalsIgnoreCase("italic"))
					fontStyle = TypeFace.STYLE_ITALIC;
			}
			float r = map.getFloat("fontRed");
			float g = map.getFloat("fontGreen");
			float b = map.getFloat("fontBlue");
			float a = map.getFloat("fontAlpha");
			setFontColor(new Color(r, g, b, a));
			setLockFontSize(map.getBoolean("lockFontSize"));
		
			setTitleTypeFaceName(map.getString("titleTypeFaceName"));
			setTitleFontSize(map.getInt("titleFontSize"));
			titleFontStyle = map.getString("titleFontStyle");
			if(titleFontStyle != null) {
				if(titleFontStyle.equalsIgnoreCase("plain"))
					titleFontStyle = TypeFace.STYLE_PLAIN;
				else if(titleFontStyle.equalsIgnoreCase("bold"))
					titleFontStyle = TypeFace.STYLE_BOLD;
				else if(titleFontStyle.equalsIgnoreCase("italic"))
					titleFontStyle = TypeFace.STYLE_ITALIC;
			}
			r = map.getFloat("titleFontRed");
			g = map.getFloat("titleFontGreen");
			b = map.getFloat("titleFontBlue");
			a = map.getFloat("titleFontAlpha");
			setTitleFontColor(new Color(r, g, b, a));
			setTitleLockFontSize(map.getBoolean("titleLockFontSize"));
			
			setUnderGraphicPath(map.getString("underGraphicPath"));
			r = map.getFloat("underGraphicRed");
			g = map.getFloat("underGraphicGreen");
			b = map.getFloat("underGraphicBlue");
			a = map.getFloat("underGraphicAlpha");
			setUnderGraphicColor(new Color(r, g, b, a));
		
			setOverGraphicPath(map.getString("overGraphicPath"));
			r = map.getFloat("overGraphicRed");
			g = map.getFloat("overGraphicGreen");
			b = map.getFloat("overGraphicBlue");
			a = map.getFloat("overGraphicAlpha");
			setOverGraphicColor(new Color(r, g, b, a));

			ResourceManager.queueTypeFace(typeFaceName, typeFaceName);
			ResourceManager.queueTypeFace(titleTypeFaceName, titleTypeFaceName);
			
			r = map.getFloat("selectedRed");
			g = map.getFloat("selectedGreen");
			b = map.getFloat("selectedBlue");
			a = map.getFloat("selectedAlpha");
			setSelectedColor(new Color(r, g, b, a));
			
			r = map.getFloat("finalizedSelectionRed");
			g = map.getFloat("finalizedSelectionGreen");
			b = map.getFloat("finalizedSelectionBlue");
			a = map.getFloat("finalizedSelectionAlpha");
			setFinalizedSelectionColor(new Color(r, g, b, a));
	
			setSelectSpeed(map.getFloat("selectSpeed"));
			setDeselectSpeed(map.getFloat("deselectSpeed"));

			setCellClass(map.getString("cellClass"));
			
			loaded = true;
		}
	}
	
	public CellBehavior getBehaviorInstance() {
		if(loaded) {
			CellBehavior behavior = null;
			
			try {
				behavior = (CellBehavior)Class.forName(behaviorName).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return behavior;
		} else {
			System.err.println("Must load CellConfig before retreiving behavior instance");
			return null;
		}
	}
	
	public TypeFace getTypeFace() {
		return ResourceManager.getTypeFace(typeFaceName);
	}
	
	public TypeFace getTitleTypeFace() {
		return ResourceManager.getTypeFace(titleTypeFaceName);
	}
	
	public CorporealBox getUnderGraphicInstance() {
		if(underGraphicPath != null) {
			CorporealBox under = new CorporealBox(new VariableSizeBox(underGraphicPath));
			//System.out.println(underGraphicPath + ": r" + underGraphicColor.r + ", g: " + underGraphicColor.g + ", b: " + underGraphicColor.b);
			under.setColor(underGraphicColor.r, underGraphicColor.g, underGraphicColor.b, underGraphicColor.a);
			return under;
		} else
			return null;
	}
	
	public CorporealBox getOverGraphicInstance() {
		if(overGraphicPath != null) {
			CorporealBox over = new CorporealBox(new VariableSizeBox(overGraphicPath));
			over.setColor(overGraphicColor.r, overGraphicColor.g, overGraphicColor.b, overGraphicColor.a);
			return over;
		} else
			return null;
	}

	public String getBehaviorName() {
		return behaviorName;
	}

	public void setBehaviorName(String behaviorName) {
		this.behaviorName = behaviorName;
	}

	public String getTypeFaceName() {
		return typeFaceName;
	}

	public void setTypeFaceName(String typeFaceName) {
		this.typeFaceName = typeFaceName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isLockFontSize() {
		return lockFontSize;
	}

	public void setLockFontSize(boolean lockFontSize) {
		this.lockFontSize = lockFontSize;
	}
	
	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	
	public String getTitleFontStyle() {
		return titleFontStyle;
	}

	public void setTitleFontStyle(String titleFontStyle) {
		this.titleFontStyle = titleFontStyle;
	}

	public String getTitleTypeFaceName() {
		return titleTypeFaceName;
	}

	public void setTitleTypeFaceName(String titleTypeFaceName) {
		this.titleTypeFaceName = titleTypeFaceName;
	}

	public int getTitleFontSize() {
		return titleFontSize;
	}

	public void setTitleFontSize(int titleFontSize) {
		this.titleFontSize = titleFontSize;
	}

	public boolean isTitleLockFontSize() {
		return titleLockFontSize;
	}

	public void setTitleLockFontSize(boolean titleLockFontSize) {
		this.titleLockFontSize = titleLockFontSize;
	}

	public String getUnderGraphicPath() {
		return underGraphicPath;
	}

	public void setUnderGraphicPath(String underGraphicPath) {
		this.underGraphicPath = underGraphicPath;
	}

	public Color getUnderGraphicColor() {
		return underGraphicColor;
	}

	public void setUnderGraphicColor(Color underGraphicColor) {
		this.underGraphicColor = underGraphicColor;
	}

	public String getOverGraphicPath() {
		return overGraphicPath;
	}

	public void setOverGraphicPath(String overGraphicPath) {
		this.overGraphicPath = overGraphicPath;
	}

	public Color getOverGraphicColor() {
		return overGraphicColor;
	}

	public void setOverGraphicColor(Color overGraphicColor) {
		this.overGraphicColor = overGraphicColor;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Color getTitleFontColor() {
		return titleFontColor;
	}

	public void setTitleFontColor(Color titleFontColor) {
		this.titleFontColor = titleFontColor;
	}

	public float getSelectSpeed() {
		return selectSpeed;
	}

	public void setSelectSpeed(float selectSpeed) {
		this.selectSpeed = selectSpeed;
	}

	public float getDeselectSpeed() {
		return deselectSpeed;
	}

	public void setDeselectSpeed(float deselectSpeed) {
		this.deselectSpeed = deselectSpeed;
	}
	
	public boolean isLoaded() {
		return loaded;
	}

	public String getCellClass() {
		return cellClass;
	}

	public void setCellClass(String cellClass) {
		this.cellClass = cellClass;
	}

	public Color getFinalizedSelectionColor() {
		return finalizedSelectionColor;
	}

	public void setFinalizedSelectionColor(Color finalizedSelectionColor) {
		this.finalizedSelectionColor = finalizedSelectionColor;
	}
}

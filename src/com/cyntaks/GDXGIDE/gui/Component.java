package com.cyntaks.GDXGIDE.gui;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.Cell;
import com.cyntaks.GDXGIDE.GNode;
import com.cyntaks.GDXGIDE.Grid;
import com.cyntaks.GDXGIDE.TextCell;
import com.cyntaks.GDXGIDE.text.GLString;
import com.cyntaks.GDXGIDE.text.TypeFace;
import com.cyntaks.GDXGIDE.util.CorporealBox;
import com.cyntaks.GDXGIDE.util.GLImage;
import com.cyntaks.GDXGIDE.util.VariableSizeBox;

public class Component {
	private CorporealBox underGraphic;
	private CorporealBox overGraphic;
	private String underGraphicPath;
	private String overGraphicPath;
	private GLImage icon;
	public static final int CENTER_ICON = 0;
	public static final int STRETCH_ICON= 1;
	private int iconDrawStyle = CENTER_ICON;
	private String iconPath;
	private GNode node;
	private TextCell cell;
	private int hAlign = GLString.CENTER;
	private int vAlign = GLString.CENTER;
	private TypeFace typeFace;
	private Color fontColor;
	private int fontSize;
	private boolean fontSizeChanged;
	private boolean created;
	private float minWidth = -Float.MAX_VALUE;
	private float minHeight = -Float.MAX_VALUE;
	private float maxWidth = Float.MAX_VALUE;
	private float maxHeight = Float.MAX_VALUE;
	private boolean adjustToContents;
	private int eventType = -1;
	
	public Component() {
		setNode(new GNode(""));
	}
	
	public Component(String text) {
		if(text == null)
			text = "";
		setNode(new GNode(text));
	}
	
	public Component(String text, int eventType) {
		this(text);
		this.eventType = eventType;
	}
	
	public Component(String text, int eventType, String iconPath) {
		this(text, eventType);
		this.iconPath = iconPath;
	}
	
	public Component(String text, int eventType, String iconPath, String underGraphicPath) {
		this(text, eventType, iconPath);
		this.underGraphicPath = underGraphicPath;
	}
	
	public Component(String text, int eventType, String iconPath, String underGraphicPath, String overGraphicPath) {
		this(text, eventType, iconPath, underGraphicPath);
		this.overGraphicPath = overGraphicPath;
	}
	
	public Cell createCell(Grid grid) {
		this.cell = new TextCell(grid, node, hAlign, vAlign);
		cell.setComponent(this);
		cell.setConfig(grid.getCellConfig());
		cell.setDebug(true);
		
		if(fontColor != null)
			setFontColor(fontColor);
		if(typeFace != null)
			setTypeFace(typeFace);
		if(fontSizeChanged)
			setFontSize(fontSize);
		
		if(this.overGraphic == null && overGraphicPath != null)
			this.overGraphic = new CorporealBox(new VariableSizeBox(overGraphicPath));
		setOverGraphic(overGraphic);
		
		if(this.underGraphic == null && underGraphicPath != null)
			this.underGraphic = new CorporealBox(new VariableSizeBox(underGraphicPath));
		setUnderGraphic(underGraphic);
		
		if(this.icon == null && iconPath != null) {
			this.icon = new GLImage(iconPath);
			icon.setDrawStyle(iconDrawStyle);
		}
		if(this.icon != null) {
			setIcon(icon);
		}
		
		cell.reAlignText(hAlign, vAlign);
		cell.setAdjustToContents(adjustToContents);
		
		created = true;
		
		return cell;
	}
	
	public void setTextAlignment(int hAlign, int vAlign) {
		this.hAlign = hAlign;
		this.vAlign = vAlign;
		
		if(cell != null) {
			cell.reAlignText(hAlign, vAlign);
		}
	}
	
	public void setFontSize(int size) {
		this.fontSize = size;
		fontSizeChanged = true;
		
		if(cell != null) {
			cell.getText().setFontSize(size);
		}
	}
	
	public void setFontColor(Color color) {
		this.fontColor = color;
		
		if(cell != null) {
			cell.getText().setColor(color);
		}
	}
	
	public void setTypeFace(TypeFace face) {
		this.typeFace = face;
		
		if(cell != null) {
			cell.getText().setTypeFace(face);
		}
	}

	public GNode getNode() {
		return node;
	}

	public void setNode(GNode node) {
		assert !created : "Can't setNode(node) after the Component has been created";
		this.node = node;
	}

	public CorporealBox getUnderGraphic() {
		return underGraphic;
	}

	public void setUnderGraphic(CorporealBox underGraphic) {
		this.underGraphic = underGraphic;
		
		if(cell != null) {
			cell.setUnderGraphic(underGraphic);
		}
	}

	public CorporealBox getOverGraphic() {
		return overGraphic;
	}

	public void setOverGraphic(CorporealBox overGraphic) {
		this.overGraphic = overGraphic;
		
		if(cell != null) {
			cell.setOverGraphic(overGraphic);
		}
	}

	public GLImage getIcon() {
		return icon;
	}

	public void setIcon(GLImage icon) {
		this.icon = icon;
		icon.setDrawStyle(iconDrawStyle);
		
		if(cell != null) {
			ContentLine imageLine = cell.getDisplay().getContentLine(CellDisplay.LAYER_BOTTOM, CellDisplay.LINE_X_CENTER);
			imageLine.setAttachLocation(ContentLine.ATTACH_MIDDLE);
			imageLine.add(icon);
		}
	}
	
	public TextCell getCell() {
		assert cell != null : "must call createCell() before retrieving it";
		return cell;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public float getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(float minWidth) {
		this.minWidth = minWidth;
	}

	public float getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(float minHeight) {
		this.minHeight = minHeight;
	}

	public float getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(float maxWidth) {
		this.maxWidth = maxWidth;
	}

	public float getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(float maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	public void setWidth(float width) {
		setMinWidth(width);
		setMaxWidth(width);
	}
	
	public float getWidth() {
		return getMaxWidth(); //could equall well have called getMinWidth()
	}
	
	public void setHeight(float height) {
		setMinHeight(height);
		setMaxHeight(height);
	}
	
	public float getHeight() {
		return getMaxHeight();
	}

	public boolean isAdjustToContents() {
		return adjustToContents;
	}

	public void setAdjustToContents(boolean adjustToContents) {
		this.adjustToContents = adjustToContents;
		if(cell != null) {
			cell.setAdjustToContents(adjustToContents);
		}
	}
	
	public void cellWidthChanged(float width) {
		if(icon != null && iconDrawStyle == STRETCH_ICON)
			icon.setWidth(width);
	}
	
	public void cellHeightChanged(float height) {
		if(icon != null && iconDrawStyle == STRETCH_ICON)
			icon.setHeight(height);
	}
	
	public void setIconDrawStyle(int style) {
		this.iconDrawStyle = style;
	}
	
	public int getIconDrawStyle() {
		return this.iconDrawStyle;
	}
}

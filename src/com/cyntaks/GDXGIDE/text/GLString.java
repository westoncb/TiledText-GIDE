package com.cyntaks.GDXGIDE.text;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.cyntaks.GDXGIDE.Corpus;
import com.cyntaks.GDXGIDE.GIDEApp;

/**
 * GLStrings are used to display strings confined to certain widths and/or heights, it uses a generalized form of "word wrapping" to carry this
 * out. Instead of wrapping on word boundaries (which it can do), it attempts to wrap on textual units called atoms. An atom (atom is the notion,
 * GLStringAtom is the class I use to express it) is just some unit of text, and a glstring is a collection of atoms. GLString can build atoms
 * automatically given a piece of a text and a delimiter for breaking it up, or it can take in a custom made collection of GLStringAtoms.
 * 
 * When being fit to some width constraint, GLStringAtom will try to keep atoms in one piece, placing any atom that crosses the width boundary onto
 * the next line. However, if a single atom is wider than the width constraint, it will be broken into parts so that none of its parts exceeds the
 * width constraint.
 * 
 * When being restricted to a height constraint, GLString will attempt reducing font-size to make the string fit, and then truncating. Both of
 * these methods can be enabled/disabled.
 * 
 * Additionally, GLStringAtoms use a TextAppearance to configure their display, which includes such properties as typeface, font size, font style,
 * and color. Since it's the atoms which contain this info. rather than the string, a single string can be displayed in multiple colors, for example.
 * 
 * @author Weston Beecroft
 *
 */

public class GLString extends Corpus{
	public static final boolean TEST_BOUNDS = false;
	private ArrayList<ArrayList<GLStringAtom>> lines; //should never access this directly, use getAtomsAsLines() instead
	private boolean recalculateLines; //should be set to true any time the layout of the text changes
	private GLStringAtom[] atoms;
	private GLStringAtom[] truncatedAtoms;

	private boolean skipHAlign = true; //the calculations for horizontally aligning text aren't free; don't carry them out unnecessarily
	private int horizontalAlignment;
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	private int verticalAlignment;
	public static final int TOP = 0;
	public static final int BOTTOM = 2;

	private boolean appendEllipsisDuringTruncation;
	private boolean allowTruncation;
	private boolean truncated; //if the text is too large to fit within its vertical constraints, it may be truncated
	private boolean containsSplitAtoms; //if an atom is larger than the width constraint for the string, it will be split onto multiple lines
	private boolean lockFontSize = true; //in attempting to fit a string within its vertical bounds, font-size may be reduced if this is false
	//using x,y from the superclass instead//private float x, y; //upper-left corner of the square containing the string
	private float widthConstraint = Integer.MAX_VALUE, heightConstraint = Integer.MAX_VALUE; //these define a square which the string is fit within
	private float textWidth, textHeight; //actual bounds of the string as it is currently displayed
	private float relTextX, relTextY; //for positioning text within the constraints square (carrying out vertical/horizontal alignment)
	private boolean hyphenateSplitAtoms;
	
	private static LinkedList<GLStringAtom> scratchList = new LinkedList<GLStringAtom>();
	private static long atomIDCounter; //used for assigning IDs to split atoms (for rejoining them later)
	private static final String TRUNCATION_SUFFIX = "...";
	private static final String LINE_CONNECTOR = "-";
	
	public GLString(GLStringAtom[] atoms, boolean markupAtoms) {
		this.atoms = markupAtoms ? markupAtoms(atoms) : atoms;
		truncatedAtoms = new GLStringAtom[0];
		recalculateLines = true;
		allowTruncation = true;
		lines = new ArrayList<ArrayList<GLStringAtom>>();
		
		layoutText();
	}
	
	public GLString(String text, TextAppearance appearance, String atomDelimiter) {
		this(text, appearance.getTypeFace(), appearance.getFontStyle(), appearance.getFontSize(), appearance.getColor(), atomDelimiter);
	}
	
	public GLString(String text, TypeFace typeFace, String style, int size, Color color, String atomDelimiter) {
		this(getAtomsFromString(text, typeFace, style, size, color, atomDelimiter), true);
	}
	
	/**
	 * Treats the whole string as a single atom, or uses " " (space) as a default delimiter.
	 * @param text
	 * @param typeFace
	 * @param style
	 * @param size
	 * @param color
	 */
	public GLString(String text, TypeFace typeFace, String style, int size, Color color, boolean useSpaceDelimiter) {
		this(text, typeFace, style, size, color, " ");
	}
	
	/**
	 * Text to be displayed by GLString may contain non-visible characters used to configure the display of the text -- newline
	 * or tab characters are examples; This method will insert "meta atoms" into a sequence of atoms which are intended to convey
	 * the meanings of the original non-visible characters from the text.
	 * 
	 * @param atoms The collection of atoms to mark up
	 * @return The marked up atoms
	 */
	private GLStringAtom[] markupAtoms(GLStringAtom[] atoms) {
		ArrayList<GLStringAtom> temp = new ArrayList<GLStringAtom>(); //not sure how many atoms we'll have in total at this point
		for (int i = 0; i < atoms.length; i++) {
			String text = atoms[i].getText();
			int index = 0;
			int newlineIndex = text.indexOf("\n");
			while(newlineIndex != -1 && index < text.length()) {
				String sub = text.substring(index, newlineIndex);
				if(sub.length() > 0) {
					if(sub.length() + 1 == text.length()) { //newline was at the end of the text -- no need to form new atom
						temp.add(atoms[i]);
					}
					else {
						GLStringAtom newAtom = new GLStringAtom(sub, atoms[i].getTypeFace(), atoms[i].getFontStyle(), atoms[i].getFontSize(), atoms[i].getColor());
						temp.add(newAtom);
					}
				}
				temp.add(new GLStringAtom(GLStringAtom.NEWLINE, false));
				text = text.replaceFirst("\n", "");
				index += sub.length(); 
				newlineIndex = text.indexOf("\n");
			}
			if(text.length() > index) {
				if(index == 0) { //there was no newline
					temp.add(atoms[i]);
				}
				else {
					GLStringAtom newAtom = new GLStringAtom(text.substring(index, text.length()), atoms[i].getTypeFace(), atoms[i].getFontStyle(), atoms[i].getFontSize(), atoms[i].getColor());
					temp.add(newAtom);
				}
			}
		}
		
		atoms = new GLStringAtom[temp.size()];
		for (int i = 0; i < atoms.length; i++) {
			atoms[i] = temp.get(i);
		}
		
		return atoms;
	}
	
	/**
	 * Given some text, a delimiter, and some display parameters(typeface, etc.), this method will build a collection
	 * of GLStringAtoms. The delimiter is a string used to separate the text into discrete atoms; if you want your atoms
	 * to be words, use the delimiter " ", which will break up the text along word boundaries.
	 * 
	 * @param text
	 * @param typeFace
	 * @param style
	 * @param size
	 * @param color
	 * @param atomDelimitter
	 * @return
	 */
	private static GLStringAtom[] getAtomsFromString(String text, TypeFace typeFace, String style, int size, Color color, String atomDelimiter) {
		if(atomDelimiter == null) {
			return getAtomsFromString(text, typeFace, style, size, color);
		}
		
		
		String[] atomStrings = null;
		GLStringAtom[] atoms = null;
		
		if(atomDelimiter != null) {
			atomStrings = text.split(atomDelimiter);
			atoms = new GLStringAtom[atomStrings.length];
		} else {
			atomStrings = new String[1];
			atoms = new GLStringAtom[1];
			atomStrings[0] = text;
		}
		
		for(int i = 0; i < atoms.length; i++) {
			if(i == atoms.length -1)
				atomDelimiter = "";
			//color = new Color(getRand(1), getRand(), getRand(), 1);
			atoms[i] = new GLStringAtom(atomStrings[i] + atomDelimiter, new TextAppearance(typeFace, style, size, color));
		}
		
		return atoms;
	}
	
	public static ArrayList<String> getStringAsLinesAndNewlines(String text) {
		ArrayList<String> pieces = new ArrayList<String>();
		int index = 0;
		int newlineIndex = text.indexOf("\n");
		while(newlineIndex != -1 && index < text.length()) {
			String sub = text.substring(index, newlineIndex);
			if(sub.length() > 0) {
				String newString = new String(sub);
				pieces.add(newString);
			}
			pieces.add("\n");
			text = text.replaceFirst("\n", "");
			index += sub.length(); 
			newlineIndex = text.indexOf("\n");
		}
		
		if(text.length() > index) {
			String newString = text.substring(index, text.length());
			pieces.add(newString);
		}
		
		return pieces;
	}
	
	private static GLStringAtom[] getAtomsFromString(String text, TypeFace typeFace, String style, int size, Color color) {
		ArrayList<String> strings = getStringAsLinesAndNewlines(text);
		GLStringAtom[] atoms = new GLStringAtom[strings.size()];
		
		int index = 0;
		for (String string : strings) {
			if(string.equals("\n")) {
				atoms[index] = new GLStringAtom(GLStringAtom.NEWLINE, false);
			} else {
				atoms[index] = new GLStringAtom(string, typeFace, style, size, color);
			}
			
			index++;
		}
		
		return atoms;
	}
	
	private String trim(String string, String toTrim, boolean endOnly) {
		if(!endOnly && string.startsWith(toTrim))
			string = string.substring(toTrim.length());
		if(string.endsWith(toTrim))
			string = string.substring(0, string.length() - (toTrim.length()));
		return string;
	}
	
	private static float getRand(int num) {
		if(num == 1) {
			return (float)Math.random()*0.8f;
		} else {
			return (float)(Math.random()*0.35 + 0.65f);
		}
	}
	
	private static float getRand() {
		return getRand((int)Math.round(Math.random()));
	}
	
	/**
	 * Draws the string. Width and height are unused.
	 */
	public void draw(float x, float y, float width, float height, float r, float g, float b, float a) { 
		super.draw(x, y, super.getWidth(), super.getHeight(), r, g, b, a);

		if(super.isDebug()) {
			Color old = GIDEApp.SPRITE_BATCH.getColor();
			GIDEApp.SPRITE_BATCH.setColor(.6f, .6f, 0, 0.2f);
			debugBox.draw(this.getTextBoundsX(), this.getTextBoundsY(), getTextWidth(), getTextHeight());
			GIDEApp.SPRITE_BATCH.setColor(old);
		}
		
		for(int i = 0; i < atoms.length; i++) {
			atoms[i].translate(x + atoms[i].getLeftOffset(), y + relTextY);
			atoms[i].draw();
			atoms[i].translate(-(x + atoms[i].getLeftOffset()), -(y + relTextY));
		}
	}
	
	/**
	 * There are multiple ways to determine line height; rather than code the calculation in place where it's needed,
	 * I use this method.
	 * @param atom
	 * @return
	 */
	private float getAtomLineHeight(GLStringAtom atom) {
		if(atom.getFont() != null)
			return atom.getFont().getCapHeight() + atom.getFont().getXHeight();
		else
			return 0;
	}
	
	public void enforceWidthConstraint() {
		enforceWidthConstraint(true);
	}
	/**
	 * Redefines line boundaries so that the string is displayed within the specified width constraint.
	 */
	public void enforceWidthConstraint(boolean restoreOriginal) {
		if(restoreOriginal)
			restoreOriginalString();

		int x = 0; //this is the x-insertion point for atoms
		for (int i = 0; i < atoms.length; i++) {
			GLStringAtom atom = atoms[i];
			boolean offEdge = x + atom.getWidth() > widthConstraint;
			boolean needAtomSplit = offEdge && atom.getWidth() > widthConstraint;
			if(atom.getType() == GLStringAtom.NEWLINE || offEdge) { //we have to move to the next line
				
				if(needAtomSplit) {
					containsSplitAtoms = true;
					long splitID = atomIDCounter++;
					boolean keepSplitting = true;
					while(keepSplitting) {
						int freeRoom = (int)(widthConstraint - x);
						int visibleGlyphCount = atom.getFont().computeVisibleGlyphs(atom.getText(), 0, atom.getText().length(), freeRoom);
						if(visibleGlyphCount < atom.getText().length() && visibleGlyphCount > 0) {

							//create first atom
							String firstAtomText = atom.getText().substring(0, visibleGlyphCount);
							boolean lastCharIsConnector = firstAtomText.endsWith(LINE_CONNECTOR);
							boolean insertHyphen = hyphenateSplitAtoms && firstAtomText.length() > 1 && !lastCharIsConnector;
							if(insertHyphen) { //removing the last glyph since we're inserting a hyphen
								firstAtomText = firstAtomText.substring(0, firstAtomText.length() - 1);
								visibleGlyphCount--; //adds that removed glyph to the second atom
							}
							GLStringAtom first = new GLStringAtom(firstAtomText, atom.getTypeFace(), atom.getFontStyle(), atom.getFontSize(), atom.getColor());
							first.setSplitID(splitID);
							scratchList.add(first);
							if(insertHyphen) {
								GLStringAtom connector = new GLStringAtom(LINE_CONNECTOR, first.getTypeFace(), first.getFontStyle(), first.getFontSize(), first.getColor());
								connector.setTransient(true);
								scratchList.add(connector);
							}
							scratchList.add(GLStringAtom.getNewlineAtom(true));
							
							//create second
							atom = new GLStringAtom(atom.getText().substring(visibleGlyphCount), atom.getTypeFace(), atom.getFontStyle(), atom.getFontSize(), atom.getColor());
							atom.setSplitID(splitID);
							
							x = 0;
						} else { //if we're here, 'atom' is the last part of an atom which has been split up
							scratchList.add(atom);
							keepSplitting = false;
							if(atom.getType() == GLStringAtom.NEWLINE)
								x = 0;
							else
								x = (int)atom.getWidth();
						}
					}
				} else { //we encountered a newline indicator
					if(atom.getType() == GLStringAtom.NEWLINE) {
						x = 0;
					} else { //we have reached the end of the line and need to wrap, without splitting
						x = (int)atom.getWidth();
						scratchList.add(GLStringAtom.getNewlineAtom(true));
					}
					
					scratchList.add(atom);
				}
				
			} else { //stayed on the same line
				scratchList.add(atom);
				x += atom.getWidth();
			}
		}
		
		if(atoms.length != scratchList.size()) { //this will happen if we had to split some atoms
			atoms = new GLStringAtom[scratchList.size()];
			for (int i = 0; i < atoms.length; i++) {
				atoms[i] = scratchList.get(i);
			}	
		}
		scratchList.clear();

		recalculateLines = true;
		layoutText(); //the above changes the sequence of atoms only, placeAtoms() will update their actual display positions
	}
	
	/**
	 * Takes the sequence of visible atoms and updates their display positions based on the meta atoms
	 * in the collection (tabs, etc.). Since the line boundaries will be different after this,
	 * alignText() is called at the end.
	 */
	public void layoutText() {
		this.textWidth = 0;
		this.textHeight = 0;
		
		ArrayList<ArrayList<GLStringAtom>> lines = this.getAtomsAsLines();
		int y = 0;
		for (ArrayList<GLStringAtom> line : lines) {
			int tallestAtom = 0;
			int x = 0;
			for (GLStringAtom atom : line) {
				if(getAtomLineHeight(atom) > tallestAtom)
					tallestAtom = (int)getAtomLineHeight(atom); 
				atom.setPosition(x, y);
				x += atom.getWidth();
				if(x > this.textWidth)
					this.textWidth = x;
			}
			y += tallestAtom;
		}
		this.textHeight = y;
		alignText();
	}
	
	/**
	 * Attempts to restrict the string to its specified height constraint. If lockFontSize is not enabled, a font size that will allow
	 * the string to fit will be sought. If the smallest available font is not enough, and allowTruncation is enabled, the string will
	 * be truncated to fit. If allowTruncation is disabled, there's a change the string will be taller than its height constraint. If 
	 * lockFontSize is not enabled but allowTruncation is, the string will be truncated directly.
	 */
	public void enforceHeightConstraint() {
		enforceHeightConstraint(true);
	}
	
	/**
	 * Attempts to restrict the string to its specified height constraint. If lockFontSize is not enabled, a font size that will allow
	 * the string to fit will be sought. If the smallest available font is not enough, and allowTruncation is enabled, the string will
	 * be truncated to fit. If allowTruncation is disabled, there's a change the string will be taller than its height constraint. If 
	 * lockFontSize is not enabled but allowTruncation is, the string will be truncated directly.
	 * 
	 * @param restore Determines whether the string is returned to its original form before enforcing the height constraints.
	 * 		  		  You may want this set to false if you have some other modification to the layout which took place before this call
	 * 				  that you'd like to keep. For example, if enforceWidthConstraint() were called before this. If restore is set to true,
	 * 				  any prior formatting will be lost.
	 */
	private void enforceHeightConstraint(boolean restore) {
		if(restore)
			restoreOriginalString();
		
		while(textHeight > heightConstraint) {
			boolean okToTruncate = false;
			if(lockFontSize || (!lockFontSize && !decreaseFontSize(1, true)))
				okToTruncate = this.allowTruncation;
			
			if(okToTruncate)
				truncateTextVertically();
			else
				heightConstraint = textHeight;
		}

		recalculateLines = true;
		layoutText();
	}
	
	/**
	 * Enforces the width and height constraints.
	 */
	public void enforceSpatialConstraints() {
		restoreOriginalString();
		
		if(textWidth > widthConstraint)
			enforceWidthConstraint(false);
		if(textHeight > heightConstraint)
			enforceHeightConstraint(false);
		
		layoutText();
	}
	
	private void truncateEverything(ArrayList<ArrayList<GLStringAtom>> lines) {
		int atomCount = 0;
		for (int i = 0; i < lines.size(); i++) {
			atomCount += lines.get(i).size();
		}
		
		truncatedAtoms = new GLStringAtom[atomCount];
		int index = 0;
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(i).size(); j++) {
				truncatedAtoms[index++] = lines.get(i).get(j);
			}
		}
		
		ArrayList<GLStringAtom> lastLine = lines.get(lines.size()-1);
		GLStringAtom last = lastLine.get(lastLine.size()-1);
		GLStringAtom suffixAtom = new GLStringAtom(TRUNCATION_SUFFIX, last.getTypeFace(), last.getFontStyle(), last.getFontSize(), last.getColor());
		suffixAtom.setTransient(true);
		
		atoms = new GLStringAtom[1];
		atoms[0] = suffixAtom;
		
		recalculateLines = true;
		truncated = true;
		textHeight = 0;
		
		/*System.out.print("FINISHED TRUNCATE ALL (t-atoms): ");
		for (int i = 0; i < truncatedAtoms.length; i++) {
			System.out.print(truncatedAtoms[i]);
		}
		System.out.println();
		*/
	}
	
	/**
	 * Carries out the work of splitting the atoms into a portion which should be displayed and a portion which shouldn't. If 
	 * appendEllipsisDuringTruncation is enabled, the visible set of atoms will have appended to it a transient atom used to display an ellipsis.
	 * To have the non-visible atoms rejoined to the visible, use restoreOriginalString().
	 */
	private void truncateTextVertically() {
		//System.out.println("TRUNCATE V");
		ArrayList<ArrayList<GLStringAtom>> lines = getAtomsAsLines();
		ArrayList<Integer> heights = getLineHeights(lines);
		
		
		int height = 0;
		int index = -1;
		while(height < heightConstraint && index+1 < heights.size()) {
			height += heights.get(++index);
		}
		
		if(index <= 0) {
			truncateEverything(lines);
			return;
		}
		
		textHeight = height - heights.get(index); //need to update bounds height so that the algorithm will accept the new height
		
		int linesToKeep = index;
		int atomsToKeepPreliminary = 0;
		for (int i = 0; i < linesToKeep; i++) {
			atomsToKeepPreliminary += lines.get(i).size();
		}
		
		if(linesToKeep < 1) //for the case when width/height constraints are too small to display anything
			linesToKeep = 1;
		
		ArrayList<GLStringAtom> lastLine = lines.get(linesToKeep - 1);
		int trailingMetaAtoms = 0;
		GLStringAtom last = lastLine.get(lastLine.size() - 1);
		while(last.isMetaAtom())
			last = lastLine.get(lastLine.size() - 1 - (++trailingMetaAtoms));
		GLStringAtom suffixAtom = new GLStringAtom(TRUNCATION_SUFFIX, last.getTypeFace(), last.getFontStyle(), last.getFontSize(), last.getColor());
		suffixAtom.setTransient(true);
		int suffixWidth = (int)suffixAtom.getWidth();
		float remainingWidth = widthConstraint - calcLineWidth(lastLine);
		int horizontalTruncateCount = 0;
		int truncateWidth = 0;
		int position = lastLine.size() - 1;
		while(position >= 0 && truncateWidth + remainingWidth < suffixWidth) {
			GLStringAtom atom = lastLine.get(position);
			truncateWidth += atom.getWidth();
			position--;
	
			if(!atom.isMetaAtom())
				horizontalTruncateCount++;
		}
		
		horizontalTruncateCount += trailingMetaAtoms;
		GLStringAtom[] horizontalTruncation = new GLStringAtom[horizontalTruncateCount];
		for (int i = 0; i < horizontalTruncation.length; i++) { //remove the replaced from the last line
			horizontalTruncation[i] = lastLine.remove((lastLine.size()-1));
		}
		lastLine.add(suffixAtom);
		atomsToKeepPreliminary++;
		
		if(linesToKeep < lines.size()) {
			ArrayList<GLStringAtom> firstTruncatedLine = lines.get(linesToKeep);
			for (int i = horizontalTruncation.length - 1; i > -1; i--) {
				firstTruncatedLine.add(0, horizontalTruncation[i]);
			}
		}
			
		//Re-build the main atoms array; these will be displayed.
		int atomsToKeep = atomsToKeepPreliminary - horizontalTruncateCount;
		atoms = new GLStringAtom[atomsToKeep];
		index = 0;
		for (int i = 0; i < linesToKeep; i++) {
			for (int j = 0; j < lines.get(i).size(); j++) {
				if(index < atoms.length) {
					atoms[index] = lines.get(i).get(j);
					index++;
				}
			}
		}
			
		int totalAtomsToTruncate = 0;//horizontalTruncateCount;
		for (int i = linesToKeep; i < lines.size(); i++) {
			totalAtomsToTruncate += lines.get(i).size();
		}

		truncatedAtoms = new GLStringAtom[totalAtomsToTruncate];
		index = 0;
		for (int i = linesToKeep; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(i).size(); j++) {
				truncatedAtoms[index] = lines.get(i).get(j);
				index++;
			}
		}
		
		for (int i = 0; i < horizontalTruncation.length; i++) {
			//truncatedAtoms[index++] = horizontalTruncation[i];
		}
		
		truncated = true;
		
		/*System.out.print("FINISHED TRUNCATE V : ");
		for (int i = 0; i < atoms.length; i++) {
			System.out.print(atoms[i]);
		}
		System.out.println();*/
	}
	
	private float calcLineWidth(ArrayList<GLStringAtom> line) {
		float width = 0;
		for (GLStringAtom atom : line) {
			width += atom.getWidth();
		}
		
		return width;
	}
	
	/**
	 * Breaks the atoms into a 2-dimensional ArrayList of atoms, the ArrayLists contained in the outer collection each
	 * represent a line. The atoms are broken into lines in this way by watching for newline meta atoms.
	 * @return
	 */
	private ArrayList<ArrayList<GLStringAtom>> getAtomsAsLines() {
		if(recalculateLines) {
			lines.clear();
			ArrayList<GLStringAtom> line = new ArrayList<GLStringAtom>();
			for (int i = 0; i < atoms.length; i++) {
				line.add(atoms[i]);
				if(atoms[i].getType() == GLStringAtom.NEWLINE || i == atoms.length - 1) {
					lines.add(line);
					line = new ArrayList<GLStringAtom>();
				}
			}
			recalculateLines = false;
		}
		return lines;
	}
	
	private ArrayList<Integer> getLineWidths(ArrayList<ArrayList<GLStringAtom>> lines) {
		ArrayList<Integer> widths = new ArrayList<Integer>();
		for (int i = 0; i < lines.size(); i++) {
			int width = 0;
			for (int j = 0; j < lines.get(i).size(); j++) {
				width += lines.get(i).get(j).getWidth();
			}
			widths.add(width);
		}
		
		return widths;
	}
	
	private ArrayList<Integer> getLineHeights(ArrayList<ArrayList<GLStringAtom>> lines) {
		ArrayList<Integer> heights = new ArrayList<Integer>();
		for (int i = 0; i < lines.size(); i++) {
			int height = 0;
			for (int j = 0; j < lines.get(i).size(); j++) {
				if(getAtomLineHeight(lines.get(i).get(j)) > height)
					height = (int)getAtomLineHeight(lines.get(i).get(j));
			}
			heights.add(height);
		}
		
		return heights;
	}
	
	/**
	 * Aligns the text vertically and horizontally -- should be called after placeAtoms() has been called; the bounds of the string will be
	 * incorrect if they have changed since placeAtoms() was last called.
	 */
	private void alignText() {
		verticallyAlignText();
		if(!skipHAlign)
			horizontallyAlignText();
	}
	
	private void verticallyAlignText() {
		switch (verticalAlignment) {
		case TOP:
			relTextY = 0;
			break;
		case CENTER:
			relTextY = (heightConstraint - textHeight)/2;
			break;
		case BOTTOM:
			relTextY = (heightConstraint - textHeight);
			break;
		}
	}
	
	private void horizontallyAlignText() {
		ArrayList<ArrayList<GLStringAtom>> lines = getAtomsAsLines();
		ArrayList<Integer> widths = getLineWidths(lines);
		
		int smallestLeftOffset = Integer.MAX_VALUE;
		switch(horizontalAlignment) {
		case LEFT:
			for (int i = 0; i < lines.size(); i++) {
				for (int j = 0; j < lines.get(i).size(); j++) {
					lines.get(i).get(j).setLeftOffset(0);
				}
			}
			smallestLeftOffset = 0;
			skipHAlign = true;
			break;
		case CENTER:
			for (int i = 0; i < lines.size(); i++) {
				int leftOffset = (int)(widthConstraint - widths.get(i))/2;
				if(leftOffset < smallestLeftOffset)
					smallestLeftOffset = leftOffset;
				for (int j = 0; j < lines.get(i).size(); j++) {
					lines.get(i).get(j).setLeftOffset((int)(widthConstraint - widths.get(i))/2);
				}
			}
			break;
		case RIGHT:
			for (int i = 0; i < lines.size(); i++) {
				int leftOffset = (int)(widthConstraint - widths.get(i));
				if(leftOffset < smallestLeftOffset)
					smallestLeftOffset = leftOffset;
				for (int j = 0; j < lines.get(i).size(); j++) {
					lines.get(i).get(j).setLeftOffset(leftOffset);
				}
			}
			break;
		}
		
		relTextX = smallestLeftOffset;
	}
	
	/**
	 * Removes all transient atoms from the given collection.
	 * 
	 * @param atoms
	 * @return
	 */
	private GLStringAtom[] stripTransients(GLStringAtom[] atoms) {
		int transientCount = 0;
		for (int i = 0; i < atoms.length; i++) {
			if(atoms[i].isTransient()) {
				atoms[i] = null;
				transientCount++;
			}
		}
		
		GLStringAtom[] newAtoms = new GLStringAtom[atoms.length - transientCount];
		int permanentCount = 0;
		for (int i = 0; i < atoms.length; i++) {
			if(atoms[i] != null)
				newAtoms[permanentCount++] = atoms[i];
		}
		
		return newAtoms;
	}
	
	/**
	 * This method does the work of restoring a string of atoms to its original form, removing
	 * all marks left by formatting for a prior display (e.g. marks for line boundaries).
	 * To be more precise, it strips all transient atoms from the string; if the string has been
	 * truncated, the truncated portion is added back in; and if any atoms have been split (generally
	 * for fitting within specified width constraints), they are rejoined.
	 */
	private void restoreOriginalString() {
		atoms = stripTransients(atoms);
		truncatedAtoms = stripTransients(truncatedAtoms);
		
		GLStringAtom[] full = null;
		if(truncated) {
			full = new GLStringAtom[atoms.length + truncatedAtoms.length];
			System.arraycopy(atoms, 0, full, 0, atoms.length);
			System.arraycopy(truncatedAtoms, 0, full, atoms.length, truncatedAtoms.length);
			truncatedAtoms = new GLStringAtom[0];
		} else
			full = atoms;

		if(containsSplitAtoms) {
			ArrayList<GLStringAtom> temp = new ArrayList<GLStringAtom>();
			for (int i = 0; i < full.length; i++) {
				if(full[i].isSplit()) {
					long splitID = full[i].getSplitID();
					String text = "";
					while (i < full.length && full[i].getSplitID() == splitID) { //the text of split atoms with the same id will be re-joined here
						String curText = full[i].getText();
						curText = trim(full[i].getText(), LINE_CONNECTOR, true);
						text += curText;
						
						i++; //this will increment one time too many (due to not knowing when we're at the last atom in the set); adjusted for below
					}

					GLStringAtom last = full[i-1];
					GLStringAtom reconstituted = new GLStringAtom(text, last.getTypeFace(), last.getFontStyle(), last.getFontSize(), last.getColor());
					temp.add(reconstituted);
					i--; //and here's the correction
				} else
					temp.add(full[i]);
			}
			
			atoms = new GLStringAtom[temp.size()];
			for (int i = 0; i < atoms.length; i++) {
				atoms[i] = temp.get(i);
			}
			containsSplitAtoms = false;
		} else
			atoms = full;
		
		recalculateLines = true;
		
		/*System.out.print("RESTORED STRING : ");
		for (int i = 0; i < atoms.length; i++) {
			System.out.print(atoms[i]);
		}
		System.out.println();
		*/
	}
	
	public void changeAppearance(int size, String style, TypeFace typeFace) {
		for(int i = 0; i < atoms.length; i++) {
			atoms[i].setFontSize(size);
			atoms[i].setFontStyle(style);
			atoms[i].setTypeFace(typeFace);
		}
		
		enforceSpatialConstraints();
	}
	
	public boolean decreaseFontSize(int increment) {
		return decreaseFontSize(increment, false);
	}
	
	private boolean decreaseFontSize(int increment, boolean skipHeightEnforcement) {
		boolean success = false;
		
		for (int i = 0; i < atoms.length; i++) { //consider it a success as long as one atom changed its size
			if(atoms[i].setFontSize(atoms[i].getFontSize() - increment))
				success = true;
		}
		
		if(success) {
			enforceWidthConstraint();
			if(!skipHeightEnforcement)
				enforceHeightConstraint();
		}
		
		return success;
	}
	
	public boolean setFontSize(int size) {
		boolean worked = false;
		for(int i = 0; i < atoms.length; i++) {
			if(atoms[i].setFontSize(size))
				worked = true;
		}
		enforceSpatialConstraints();
		
		return worked; //may have to take into account enforceSpatialConstraints() for this, which would be ugly...
	}

	
	public void setFontStyle(String style) {
		for(int i = 0; i < atoms.length; i++) {
			atoms[i].setFontStyle(style);
		}
		enforceSpatialConstraints();
	}
	
	public void setTypeFace(TypeFace typeFace) {
		for(int i = 0; i < atoms.length; i++) {
			atoms[i].setTypeFace(typeFace);
		}
		enforceSpatialConstraints();
	}
	
	public void setColor(Color color) {
		for (int i = 0; i < atoms.length; i++) {
			atoms[i].setColor(color);
		}
	}
	
	public float getTextWidth() {
		return textWidth;
	}
	
	public float getTextHeight() {
		return textHeight;
	}

	public float getWidthConstraint() {
		return widthConstraint;
	}
	
	public float getHeightConstraint() {
		return heightConstraint;
	}

	public void setSpatialConstraints(float widthConstraint, float heightConstraint) {
		this.widthConstraint = widthConstraint;
		this.heightConstraint = heightConstraint;
		enforceSpatialConstraints();
	}
	
	public float getTextBoundsX() {
		return super.getX() + relTextX;
	}
	
	public float getTextBoundsY() {
		return super.getY() + relTextY;
	}
	
	public void setPosition(float x, float y){
		super.setX(x);
		super.setY(y);
	}

	public boolean isLockFontSize() {
		return lockFontSize;
	}

	public void setLockFontSize(boolean lockFontSize) {
		this.lockFontSize = lockFontSize;
	}

	public boolean isHyphenateSplitAtoms() {
		return hyphenateSplitAtoms;
	}

	public void setHyphenateSplitAtoms(boolean hyphenateSplitAtoms) {
		this.hyphenateSplitAtoms = hyphenateSplitAtoms;
	}
	
	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
		if(horizontalAlignment != LEFT)
			skipHAlign = false;
	}

	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(int verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}
	
	public String toString() {
		String text = "atoms:\n";
		for (int i = 0; i < atoms.length; i++) {
			text += atoms[i].toString();
		}
		text += "\ntruncated:\n";
		for (int i = 0; i < truncatedAtoms.length; i++) {
			text += truncatedAtoms[i].toString();
		}
		
		return text;
	}
	
	public String getText() {
		String text = "";
		for(GLStringAtom atom : atoms)
			text += atom.getText();
		
		return text;
	}

	public boolean isAllowTruncation() {
		return allowTruncation;
	}

	public void setAllowTruncation(boolean allowTruncation) {
		this.allowTruncation = allowTruncation;
	}

	public boolean appendEllipsisDuringTruncation() {
		return appendEllipsisDuringTruncation;
	}

	public void setAppendEllipsisDuringTruncation(
			boolean appendEllipsisDuringTruncation) {
		this.appendEllipsisDuringTruncation = appendEllipsisDuringTruncation;
	}
	
	public float getWidth() {
		return (widthConstraint == Integer.MAX_VALUE) ? textWidth : widthConstraint;
	}
	public void setWidth(float width) {
		setSpatialConstraints(width, heightConstraint);
	}
	
	public float getHeight() {
		return (heightConstraint == Integer.MAX_VALUE) ? textHeight : heightConstraint;
	}
	public void setHeight(float height) {
		setSpatialConstraints(widthConstraint, height);
	}
	
	public void setSpatialConstraintsToStringBounds() {
		layoutText();
		this.setSpatialConstraints(this.getTextWidth(), this.getTextHeight());
	}
	
	public GLStringAtom[] getAtoms() {
		return atoms;
	}
}
package com.cyntaks.GDXGIDE.gui;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;

public class ProgressBar 
{
  private static Texture border;
  private static Texture center;
  private static Texture tip;
  private static Texture background;
  
  private static int width;
  private static int height;
  
  private int totalTasks;
  private int completeTasks;
  private LinkedList<String> taskNames;
  private boolean showTaskNames;
  
  static
  {
    //background = ImageLoader.loadImage("images/system/background.gif", Transparency.BITMASK);
    //border = ImageLoader.loadImage("images/system/border.gif", Transparency.BITMASK);
    //center = ImageLoader.loadImage("images/system/center.gif", Transparency.BITMASK);
    //tip = ImageLoader.loadImage("images/system/tip.gif", Transparency.BITMASK);
      
    width = border.getWidth();
    height = border.getHeight();
  }
  
  public ProgressBar()
  {
    taskNames = new LinkedList<String>();
  }
  
  public void init(int numTasks)
  {
    showTaskNames = true;
    totalTasks = numTasks;
    completeTasks = 0;
    taskNames.clear();
  }
  public void init(String[] taskNames)
  {
    showTaskNames = true;
    init(taskNames.length);
    for (int i = 0; i < taskNames.length; i++) 
    {
      this.taskNames.addFirst(taskNames[i]);
    }
  }
  public void init(String[] taskNames, int textColor)
  {
    init(taskNames);
    //this.textColor = textColor;
  }
  public void init(String[] taskNames, int textColor, int font)
  {
    init(taskNames, textColor);
    //this.font = font;
  }
  
  public void addTask()
  {
    totalTasks++;
  }
  public void addTask(String name)
  {
    addTask();
    taskNames.addFirst(name);
  }
  
  public void taskCompleted()
  {
    completeTasks++;
    if(!taskNames.isEmpty())
      taskNames.removeLast();
  }
  
  public void reset()
  {
    totalTasks = 0;
    completeTasks = 0;
    taskNames.clear();
    showTaskNames = false;
  }
  
  public void render(int x, int y)
  {
    float completeRatio = 0;
    if(completeTasks != 0)
      completeRatio = (float)(completeTasks+1)/totalTasks;
    if(completeRatio > 1)
      completeRatio = 1;
      
    /*g2.drawImage(background, x, y, null);
    g2.drawImage(center, x, y, (int)(x+width*completeRatio-tip.getWidth()), y+height,
                 0, 0, (int)(width*completeRatio-tip.getWidth()), height,
                 null);
                           
    Shape clipBounds = g2.getClip();
    g2.setClip(x, y, width, height);
    g2.drawImage(tip, (int)(x+width*completeRatio-tip.getWidth()), y, null);
    g2.drawImage(border, x, y, null);
    g2.setClip(clipBounds);
      
    if(showTaskNames && !taskNames.isEmpty())
    {
      g2.setFont(font);
      g2.setColor(textColor);
        
      String currentString = (String)taskNames.getLast();
      FontMetrics metrics = g2.getFontMetrics(font);
      int stringWidth = (int)metrics.getStringBounds(currentString, g2).getWidth();
      int stringHeight = metrics.getHeight();
        
      boolean elipses = false;
      while(stringWidth > width-4)
      {
        if(elipses)
          currentString = currentString.substring(0, currentString.length()-5)+"...";
        else
        {
          currentString = currentString.substring(0, currentString.length()-2)+"...";
          elipses = true;
        }
        stringWidth = (int)metrics.getStringBounds(currentString, g2).getWidth();
      }
      g2.drawString(currentString, x+width/2-stringWidth/2, y+(height/2-stringHeight/2+metrics.getAscent()));
    }*/
  }

  public int getFont()
  {
    return 0;
  }

  public void setFont(int font)
  {
    //this.font = font;
  }

  public void setTextColor(int textColor)
  {
    //this.textColor = textColor;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }
}
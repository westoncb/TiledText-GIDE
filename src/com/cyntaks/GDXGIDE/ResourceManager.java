package com.cyntaks.GDXGIDE;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.cyntaks.GDXGIDE.gui.ProgressBar;
import com.cyntaks.GDXGIDE.text.TypeFace;

public final class ResourceManager 
{
  private static HashMap<String, Object> resources; //all loaded resources
  private static HashMap<String, String> resourcePaths; //resource paths linked to names
                                        //from the 'resources' map
  private static HashMap<String, Task> taskMap; //for associating a name with a Task object
  private static LinkedList<ResourceManager.ResourceParameters> resourceQueue;
  
  private static final int IMAGE = 0;
  private static final int SOUND = 1;
  private static final int OBJECT = 2;
  private static final int TYPE_FACE = 3;
  private static final int XML = 4;
  private static final int TASK = 5;
  
  static
  {
    resources = new HashMap<String, Object>();
    resourcePaths = new HashMap<String, String>();
    resourceQueue = new LinkedList<ResourceManager.ResourceParameters>();
    taskMap = new HashMap<String, Task>();
  }
  
  ////
  // public interface
  ////
  public static void queueTexture(String path, String name, TextureFilter filter) {
    if(!queueUnnecessary(path, name, "image_")) {
    	ResourceManager.ResourceParameters parameters = new ResourceManager.ResourceParameters(IMAGE, path, name, filter);
        resourceQueue.addFirst(parameters);
    }
  }
  
  private static boolean queueUnnecessary(String path, String name, String prefix) {
	  Object obj = getResourceFromPath(path);
	  if(obj != null && getResource(prefix+name) != null) {
		  return true;
	  }
	  
	  if(queueHasResource(path, name))
		  return true;
	  
	 return false;
  }
  
  private static boolean queueHasResource(String path, String name) {
	  for (ResourceManager.ResourceParameters param : resourceQueue) {
		if(param.getPath().equals(path) && param.getName().equals(name))
			return true;
	  }
	  
	  return false;
  }
  
  public static void loadTexture(String path, String name, TextureFilter filter)
  {
    Object image = getResourceFromPath(path);
    
    if(image != null)
    {
      resources.put("image_"+name, image);
    }
    else
    {
      image = new Texture(Gdx.files.internal(path));
      ((Texture)image).setFilter(filter, filter);
      resources.put("image_"+name, image);
      resourcePaths.put(path, "image_"+name);
    }
  }
  
  public static void queueTask(Task task, String name) {
	  ResourceManager.ResourceParameters parameters = new ResourceManager.ResourceParameters(TASK, name, name);
      resourceQueue.addFirst(parameters);
      taskMap.remove(name); //in case we had an old task with the same name loaded
      taskMap.put(name, task);
  }
  
  public static void loadTask(String name) {
	  Task task = taskMap.get(name);
	  Object result = task.doTask();
	  resources.put("task_"+name, result);
  }
  
  public static void queueSound(String path, String name)
  {
	  if(!queueUnnecessary(path, name, "sound_")) {
		  ResourceManager.ResourceParameters parameters = new ResourceManager.ResourceParameters(SOUND, path, name);
		  resourceQueue.addFirst(parameters);
	  }
  }
  
  public static void loadSound(String path, String name)
  {
	Object clip = getResourceFromPath(path);
	
	if(clip != null)
		resources.put("sound_"+name, clip);
	else
	{
		clip = Gdx.audio.newSound(Gdx.files.internal(path));
		resources.put("sound_"+name, clip);
		resourcePaths.put(path, "sound_"+name);
	}
  }
  
  public static void queueTypeFace(String directoryName, String name)
  {
	  if(!queueUnnecessary(directoryName, name, "font_")) {
		  ResourceManager.ResourceParameters parameters = new ResourceManager.ResourceParameters(TYPE_FACE, directoryName, name);
		  resourceQueue.addFirst(parameters);
	  }
  }
  
  public static void loadTypeFace(String directoryName, String name)
  {
    Object font = getResourceFromPath(directoryName);
    
    if(font != null)
    {
      resources.put("font_"+name, font);
    }
    else
    {
      font = new TypeFace(directoryName);
      resources.put("font_"+name, font);
      resourcePaths.put(directoryName, "font_"+name);
    }
  }
  
  public static void queueObject(String path, String name)
  {
	  if(!queueUnnecessary(path, name, "object_")) {
		  ResourceManager.ResourceParameters parameters = new ResourceManager.ResourceParameters(OBJECT, path, name);
		  resourceQueue.addFirst(parameters);
	  }
  }
  
  public static void loadObject(String path, String name)
  {
    Object object = getResourceFromPath(path);
    
    if(object != null)
    {
      object = copyObject(object);
      resources.put("object_"+name, object);
    }
    else
    {
        InputStream stream = ResourceManager.class.getResourceAsStream("/"+path);
        BufferedInputStream bi = new BufferedInputStream(stream);
        try 
        {
          ObjectInputStream oi = new ObjectInputStream(bi);
          try 
          {
            object = oi.readObject();
            resources.put("object_"+name, object);
            resourcePaths.put(path, "object_"+name);
          } catch (Exception ex) 
          {
            ex.printStackTrace();
          } finally 
          {
            oi.close();
          }
        } catch (IOException ex) 
        {
          ex.printStackTrace();
        }
    }
  }
  
  public static void queueXML(String path, String name)
  {
	  if(!queueUnnecessary(path, name, "xml_")) {
		  ResourceManager.ResourceParameters parameters = new ResourceManager.ResourceParameters(XML, path, name);
		  resourceQueue.addFirst(parameters);
	  }
  }
  
  public static void loadXML(String path, String name)
  {
    Object document = getResourceFromPath(path);
    
    if(document != null)
    {
      resources.put("xml_"+name, document);
    }
    else
    {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			FileHandle handle = Gdx.files.internal(path);
			File file = new File(handle.path());
			doc = db.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
      resources.put("xml_"+name, doc);
      resourcePaths.put(path, "xml_"+name);
    }
  }
  
  public static void loadQueuedResources(ProgressBar progressBar, boolean showFileNames)
  {
    if(progressBar != null)
    {
      if(showFileNames)
        progressBar.init(getResourceNames());
      else
        progressBar.init(resourceQueue.size());
    }
    while(areResourcesQueued())
    {
      loadNextQueuedResource();
      if(progressBar != null)
        progressBar.taskCompleted();
    }
  }
  
  public static String getNextQueuedResourceName() {
	  ResourceManager.ResourceParameters parameters = (ResourceParameters)resourceQueue.getLast();
	  return parameters.getPath();
  }
  
  public static void loadNextQueuedResource() {
	  ResourceManager.ResourceParameters parameters = (ResourceParameters)resourceQueue.removeLast();
	  switch (parameters.getResourceType()) 
      {
        case IMAGE:
          loadTexture(parameters.getPath(), parameters.getName(), parameters.getFilter());
        break;
        case SOUND:
          loadSound(parameters.getPath(), parameters.getName());
        break;
        case OBJECT:
          loadObject(parameters.getPath(), parameters.getName());
        break;
        case TYPE_FACE:
        	loadTypeFace(parameters.getPath(), parameters.getName());
        break;
        case XML:
        	loadXML(parameters.getPath(), parameters.getName());
        break;
        case TASK:
        	loadTask(parameters.getName());
        break;
      }
  }
  
  public static boolean areResourcesQueued() {
	  return !resourceQueue.isEmpty();
  }
  
  public static void loadQueuedResources()
  {
    loadQueuedResources(null, false);
  }
  
  public static Texture getTexture(String name)
  {
    return (Texture)resources.get("image_"+name);
  }
  
  public static Sound getSound(String name)
  {
    return (Sound)resources.get("sound_"+name);
  }
  
  public static TypeFace getTypeFace(String name) {
	  return  (TypeFace)resources.get("font_"+name);
  }
  
  public static Document getXML(String name) {
	  return (Document)resources.get("xml_"+name);
  }
  
  public static Object getTaskResult(String name) {
	  return resources.get("task_"+name);
  }
  
  public static Object getObject(String name)
  {
    Object object = copyObject(resources.get("object_"+name));
    
    return object;
  }
  
  private static Object copyObject(Object object)
  {
    try 
    {
      //cloning with serialization
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      ObjectOutputStream oo = new ObjectOutputStream(bo);
      oo.writeObject(object);
      oo.close();
      ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
      ObjectInputStream oi = new ObjectInputStream(bi);
      object = oi.readObject();
      oi.close();
    } catch (IOException ex) 
    {
      ex.printStackTrace();
      return null;
    } catch (ClassNotFoundException ex) 
    {
      ex.printStackTrace();
      return null;
    }
    
    return object;
  }
  
  public static Object getResource(String name)
  {
    return resources.get(name);
  }
  
  public static Texture releaseImage(String name)
  {
	  Texture tex = (Texture)resources.get("image_"+name);
	  tex.dispose();
    return (Texture)resources.remove("image_"+name);
  }
  
  public static Sound releaseSound(String name)
  {
    Sound sound  = (Sound)resources.remove("sound_"+name);
    sound.dispose();
    return sound;
  }
  
  public static BitmapFont releaseFont(String name)
  {
	  BitmapFont font = (BitmapFont)resources.get("font_"+name);
	  font.dispose();
    return (BitmapFont)resources.remove("font_"+name);
  }
  
  public static Document releaseXML(String name) {
    return (Document)resources.remove("xml_"+name);
  }
  
  public static Object releaseObject(String name)
  {
    return resources.remove("object_"+name);
  }
  
  public static Object releaseResource(String name)
  {
    return resources.remove(name);
  }
  
  public static void releaseAllResources()
  {
    resources.clear();
  }
  
  public String[] getResourceList()
  {
    ArrayList list = new ArrayList();
    
    Iterator iter = resources.keySet().iterator();
    while(iter.hasNext())
      list.add(iter.next());
      
    String[] names = new String[list.size()];
    for (int i = 0; i < names.length; i++) 
    {
      names[i] = list.get(i).toString();
    }
    
    return names;
  }
  
  ////
  // private methods
  ////
  private static String[] getResourceNames()
  {
    String[] names = new String[resourceQueue.size()];
    for (int i = 0; i < names.length; i++) 
    {
      names[i] = ((ResourceParameters)resourceQueue.get(names.length-1-i)).getPath(); 
    }
    
    return names;
  }
  
  private static Object getResourceFromPath(String path)
  {
    String name = (String)resourcePaths.get(path);
    if(name != null)
      return resources.get(name);
    else
      return null;
  }
  
  private static class ResourceParameters
  {
    private int resourceType;
    private String path;
    private String name;
    private TextureFilter filter;
    
    ResourceParameters(int resourceType, String path, String name)
    {
      this.resourceType = resourceType;
      this.path = path;
      this.name = name;
    }
    
    ResourceParameters(int resourceType, String path, String name, TextureFilter filter) {
    	this(resourceType, path, name);
    	this.filter = filter;
    }
    
    int getResourceType()
    {
      return resourceType;
    }
    
    String getPath()
    {
      return path;
    }
    
    String getName()
    {
      return name;
    }
    
    TextureFilter getFilter() {
    	return filter;
    }
  }
  
  public static void destroy()
  {
    resources.clear();
    resourcePaths.clear();
    resourceQueue.clear();
  }
}
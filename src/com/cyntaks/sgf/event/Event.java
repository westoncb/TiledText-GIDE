package com.cyntaks.sgf.event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Events can be used to measure a set amount of time or time intervals. Events
 * are also used with Interpolators to regularly update an EventListener with 
 * the Interpolators value as time passes. If not interpolator is not specified, 
 * the value given will be the ratio of the Event's time passed to total time.
 * 
 * @author Weston Beecroft
 */

public class Event implements Serializable
{
  //public static final long serialVersionUID = 1L;
  
  public static final int INFINITE = -1;
  public static final int EVERY_FRAME = -1;
  public final int ID;
  private final float TOTAL_TIME;
  private float timePassed;
	private int updateFrequency; // how often the listeners should be updated
									// (milliseconds)
  private int lastUpdate;
  private ArrayList<EventListener> eventListeners;
  private boolean repeat; //whether the event should repeat or not
  private boolean alive;
  private Interpolator interpolator;
  
  private float initialValue;
  private float currentValue;
  private float destValue;
  private float updateSpeed;
  private boolean speedRelativeToDistance; //rate of update proportional to difference between current and destination values 
  private boolean replaceable;
  private boolean timeless;
  private float closenessRequirement;
  private int type;
  
  public Event(int ID, int type, float startValue, float destValue, float updateSpeed, float closenessRequirement, boolean speedRelativeToDistance, EventListener listener) {
	  this.ID = ID;
	  this.type = type;
	  this.initialValue = startValue;
	  this.currentValue = startValue;
	  this.destValue = destValue;
	  this.updateSpeed = updateSpeed;
	  this.closenessRequirement = closenessRequirement;
	  this.TOTAL_TIME = -1;
	  this.speedRelativeToDistance = speedRelativeToDistance;
	  this.repeat = false;
	  
	  timeless = true;
	  alive = true;
	  eventListeners = new ArrayList<EventListener>();
	  addEventListener(listener);
	  EventManager.getInstance().addEvent(this);
  }
  
	/**
	 * Constructs a new Event with no initial listener and no interpolator
	 * 
	 * @param ID
	 *            the Event's ID
	 * @param eventLength
	 *            the total event length (milliseconds)
	 * @param updateFrequency
	 *            the frequency (milliseconds) at which listener's should be
	 *            updated
	 * @param repeat
	 *            whether the event should repeat or not when it finishes
	 */
  public Event(int ID, int type, int eventLength, int updateFrequency, boolean repeat)
  {
    this.ID = ID;
    this.type = type;
    if(eventLength == INFINITE)
      this.TOTAL_TIME = eventLength;
    else
      this.TOTAL_TIME = eventLength/1000f;
    
    this.updateFrequency = updateFrequency;
    this.repeat = repeat;
    alive = true;
    eventListeners = new ArrayList<EventListener>();
    EventManager.getInstance().addEvent(this);
  }

	/**
	 * constructs a new Event with no Interpolator and an initial EventListener
	 * 
	 * @param ID
	 *            the Event's ID
	 * @param eventLength
	 *            the total event length (milliseconds)
	 * @param updateFrequency
	 *            the frequency (milliseconds) at which listener's should be
	 *            updated
	 * @param repeat
	 *            whether the event should repeat or not when it finishes
	 * @param listener
	 *            initial EventListener
	 */
  public Event(int ID, int type, int eventLength, int updateFrequency, boolean repeat, EventListener listener)
  {
    this(ID, type, eventLength, updateFrequency, repeat);
    addEventListener(listener);
  }

	/**
	 * constructs a new Event with the given Interpolator and no initial
	 * EventListener
	 * 
	 * @param ID
	 *            the Event's ID
	 * @param eventLength
	 *            the total event length (milliseconds)
	 * @param updateFrequency
	 *            the frequency (milliseconds) at which listener's should be
	 *            updated
	 * @param repeat
	 *            whether the event should repeat or not when it finishes
	 * @param interpolator
	 *            the Event's Interpolator
	 */
  public Event(int ID, int type, int eventLength, int updateFrequency, boolean repeat, Interpolator interpolator)
  {
    this(ID, type, eventLength, updateFrequency, repeat);
    this.interpolator = interpolator;
  }

	/**
	 * constructs a new Event with the given Interpolator and initial
	 * EventListener
	 * 
	 * @param ID
	 *            the Event's ID
	 * @param eventLength
	 *            the total event length (milliseconds)
	 * @param updateFrequency
	 *            the frequency (milliseconds) at which listener's should be
	 *            updated
	 * @param repeat
	 *            whether the event should repeat or not when it finishes
	 * @param interpolator
	 *            the Event's Interpolator
	 * @param listener
	 *            initial EventListener
	 */
  public Event(int ID, int type, int eventLength, int updateFrequency, boolean repeat, Interpolator interpolator, EventListener listener)
  {
    this(ID, type, eventLength, updateFrequency, repeat, interpolator);
    addEventListener(listener);
  }

	/**
	 * updates the Event based on the given time delta. All listeners will be
	 * updated if it is time according to this Event's update frequency. If the
	 * Event has ended, then all EventListeners will be notified and this Event
	 * will be removed from the EventManager during the next frame.
	 * 
	 * @param delta
	 *            the amount of time (milliseconds) that passed during the last
	 *            frame
	 */
  void update(float delta)
  {
    if(alive)
    {
      if(timeless)
    	  updateTimeless(delta);
      else
    	  updateTimeBased(delta);
    }
  }
  
  private void updateTimeless(float delta) {
	  float scale = 1;
	  if(speedRelativeToDistance)
		  scale = Math.abs(destValue - currentValue);
	  float adder = scale*updateSpeed*delta;
	  
		if(destValue < currentValue)
			adder *= -1;
		currentValue += adder;
		
		if(Math.abs(destValue - currentValue) <= closenessRequirement || 
			   (initialValue > destValue && currentValue < destValue) || 
			   (initialValue < destValue && currentValue > destValue)) {
		
			alive = false;
			for (int i = 0, n = eventListeners.size(); i < n; i++) 
	        {
	             ((EventListener)eventListeners.get(i)).update(ID, type, destValue); //update each listener
	        }
			kill();
			
			return;
		}
		
		for (int i = 0, n = eventListeners.size(); i < n; i++) 
        {
             ((EventListener)eventListeners.get(i)).update(ID, type, currentValue); //update each listener
        }
  }
  
  private void updateTimeBased(float delta) {
	  timePassed += delta;
      if(interpolator != null)
        interpolator.update(timePassed/TOTAL_TIME);
      if(TOTAL_TIME != INFINITE && timePassed >= TOTAL_TIME) //event is over
      {
        timePassed = 0; //reset
        if(interpolator != null)
          interpolator.reset();
        for (int i = 0, n = eventListeners.size(); i < n; i++) 
        {
            ((EventListener)eventListeners.get(i)).finish(ID, type); //notify each listener that the event is finished
        }
        if(!repeat)
          alive = false; //event can be killed now
      }
      else //event isn't over so update as usual
      {
        lastUpdate += delta;
        if(updateFrequency == EVERY_FRAME || lastUpdate > updateFrequency) //time for to give a new update
        {
          lastUpdate = lastUpdate%updateFrequency; //reset
          float value = (float)timePassed/TOTAL_TIME; //the percentange of time passed is the value if we don't have an interpolator
          if(interpolator != null)
            value = interpolator.getValue(); //get the interpolator's value if available
          for (int i = 0, n = eventListeners.size(); i < n; i++) 
          {
             ((EventListener)eventListeners.get(i)).update(ID, type, value); //update each listener
          }
        }
      }
  }
  
  public void addEventListener(EventListener listener)
  {
    eventListeners.add(listener);
  }
  
  public boolean removeEventListener(EventListener listener)
  {
    return eventListeners.remove(listener);
  }
  
  public void killSilently() {
	  alive = false;
  }
  
  public void kill()
  {
    alive = false;
    for (int i = 0, n = eventListeners.size(); i < n; i++) 
    {
        ((EventListener)eventListeners.get(i)).finish(ID, type); //notify each listener that the event is finished
    }
  }

  public boolean isRepeat()
  {
    return repeat;
  }

  public void setRepeat(boolean repeat)
  {
    this.repeat = repeat;
  }

  public int getEventLength()
  {
    return (int)(TOTAL_TIME*1000);
  }

  public int getUpdateFrequency()
  {
    return updateFrequency;
  }

  public void setUpdateFrequency(int updateFrequency)
  {
    this.updateFrequency = updateFrequency;
  }
  
  public Interpolator getInterpolator()
  {
    return interpolator;
  }
  
  public void setInterpolator(Interpolator newInterpolator)
  {
    this.interpolator = newInterpolator;
  }

  public boolean isAlive()
  {
    return alive;
  }
  
  public boolean isReplaceable() {
	  return replaceable;
  }
  
  public void setReplaceable(boolean replaceable) {
	  this.replaceable = replaceable;
  }
  
  public ArrayList<EventListener> getEventListeners() {
	  return eventListeners;
  }
  
  public String toString() {
	  return "rep: " + replaceable + ", id: " + ID + ", type: " + type + ", rate: " + updateSpeed + ", cur: " + this.currentValue + ", dest: " + this.destValue + ", diff: " + (this.destValue - this.currentValue);
  }
  
  public int getType() {
	  return type;
  }
  
  public float getDestValue() {
	  return destValue;
  }
  
  public float getCurrentValue() {
	  return currentValue;
  }
}
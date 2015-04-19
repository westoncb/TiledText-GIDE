package com.cyntaks.sgf.event;

import java.util.ArrayList;

/**
 * updates all of the games Events each frame and removes them if they are
 * no longer alive.
 * 
 * @author Weston Beecroft
 */

public final class EventManager
{
  /**
   * the single available instance of this class
   */
  private static EventManager INSTANCE = new EventManager();
  private ArrayList<Event> events;
  
  private EventManager()
  {
    events = new ArrayList<Event>();
  }
  
  public static final EventManager getInstance()
  {
    if(INSTANCE == null)
      INSTANCE = new EventManager();
    return INSTANCE;
  }
  
  /**
   * updates each Event and removes them if they are not alive
   * @param delta the amount of time (seconds) passed during the last frame
   */
  public void updateOccurred(float delta)
  {
    for (int i = 0; i < events.size(); i++) 
    {
      Event event = events.get(i);
      if(!event.isAlive())
        removeEvent(event);
      else
        event.update(delta);
    }
  }
  
  /**
   * adds the given Event to this EventManager
   * @param event the Event to be added
   */
  public void addEvent(Event event)
  {
	for (int i = 0; i < events.size(); i++) {
		Event current = events.get(i);
		if(current.isReplaceable() && current.getType() == event.getType()) {
			ArrayList<EventListener> listeners1 = current.getEventListeners();
			ArrayList<EventListener> listeners2 = event.getEventListeners();
			
			boolean same = true;
			for (int j = 0; j < listeners1.size(); j++) { //try to discover whether the events are really controlling the same thing
				for (int j2 = 0; j2 < listeners2.size(); j2++) {
					if(listeners1.get(j) != listeners2.get(j2)) {
						same = false;
						break;
					}
				}
				if(!same)
					break;
			}
			
			if(same) {
				current.killSilently();
				removeEvent(current);
			}
		}
	}
	
    events.add(event);
  }
  
  /**
   * removes the given event from this EventManager
   * @param event the Event to be removed
   * @return whether the event was found or not
   */
  public boolean removeEvent(Event event)
  {
    return events.remove(event);
  }
  
  /**
   * removes the Event at the given index from this EventManager
   * @param index index of the Event to be removed
   * @return the Event which was removed
   */
  public Event removeEvent(int index)
  {
    return (Event)events.remove(index);
  }
  
  public void destroy()
  {
    clear();
    INSTANCE = null;
  }
  
  public void clear()
  {
    events.clear();
  }
}
package com.cyntaks.sgf.event;

/**
 * an EventListener will be updated at regular intervals from each Event
 * that it is registered with as well as when the Event ends.
 * 
 * @author Weston Beecroft
 */

public interface EventListener 
{
  /**
   * callback from an Event that this EventListener is registered with
   * @param id the Event's id
   * @param value the Event's current value
   */
  public void update(int id, int type,  float value);
  
  /**
   * callback from an Event when it finishes
   * @param id the event's id
   */
  public void finish(int id, int type);
}
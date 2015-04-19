package com.cyntaks.sgf.event;

/**
 * used to Interpolate between two or more values. This should be used along with
 * and Event object, which will notify all registered EventListeners of this Interpolators
 * current value.
 * 
 * @author Weston Beecroft
 */

public interface Interpolator 
{
  /**
   * updates the interpolator
   * @param ratio the ratio of how much time has passed to the Interpolator's total time
   */
  public void update(float ratio);
  
  /**
   * resets the Interpolator
   */
  public void reset();
  
  /**
   * retrieves the Interpolator's current value
   * @return the Interpolators current value
   */
  public float getValue();
}
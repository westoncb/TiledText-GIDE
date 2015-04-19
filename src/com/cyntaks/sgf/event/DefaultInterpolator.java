package com.cyntaks.sgf.event;
import java.io.Serializable;

/**
 * A linear interpolator, it can actually do linear, quadratic, and cubic
 * interpolation though.
 * 
 * @author Weston Beecroft
 */

public class DefaultInterpolator implements Interpolator, Serializable
{
  public static final long serialVersionUID = 1L;
  
  private float startVal;
  private float midVal1;
  private float midVal2;
  private float endVal;
  private float value;
  
  private int type;
  
  private static final int LINEAR = 0;
  private static final int QUADRATIC = 1;
  private static final int CUBIC = 2;
  private static final int LINEAR_COMPLETE = 3;
  
  public DefaultInterpolator(float startValue, float endValue)
  {
    this.startVal = startValue;
    this.endVal = endValue;
    value = 0.0f;
    type = LINEAR;
  }
  
  public DefaultInterpolator(float startValue, float midValue, float endValue)
  {
    this(startValue, endValue);
    this.midVal1 = midValue;
    type = QUADRATIC;
  }
  
  public DefaultInterpolator(float startValue, float midValue1, float midValue2, float endValue)
  {
    this(startValue, midValue1, endValue);
    this.midVal2 = midValue2;
    type = CUBIC;
  }
  
  public void update(float ratio)
  {
    if(ratio > 1)
      ratio = 1;
    if(ratio < 0)
      ratio = 0;
    float a = 1-ratio;
    
    switch (type) 
    {
      case LINEAR:
      {
        value = (startVal*a) + (endVal*ratio);
      }
      break;
      case QUADRATIC:
      {
        value = startVal*a*a + midVal1*2*a*ratio + endVal*ratio*ratio;
      }
      break;
      case CUBIC:
      {
        value = startVal*a*a*a + midVal1*3*a*a*ratio + midVal2*3*a*ratio*ratio + endVal*ratio*ratio*ratio;
      }
      break;
      case LINEAR_COMPLETE:
      {
        value = startVal + (endVal-startVal)*ratio;
      }
      break;
    }
  }
  
  public void reset()
  {
    value = 0;
  }
  
  public float getValue()
  {
    return value;
  }
}
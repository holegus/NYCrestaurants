package com.hmelinks.prod;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public class RestaurantMarker extends SimplePointMarker {

	private float radius = 10;
	
	public RestaurantMarker(float longitude, float latitude){
		super();
		this.setLocation(longitude, latitude);
	}
	
	public void drawMarker(PGraphics pg, float x, float y) {
		// save previous styling
		
		pg.pushStyle();
		pg.ellipse(x, y, 2*radius, 2*radius);
			
		// reset to previous styling
		pg.popStyle();
		
	}
}

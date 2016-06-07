package com.hmelinks.prod;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class DrawMap extends PApplet
{
	UnfoldingMap mainMap;
	private static final long serialVersionUID = 1L;
	
	@Override
	public void setup() {
		size(800, 600, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3

		// This sets the background color for the Applet.  
		// Play around with these numbers and see what happens!
		this.background(400, 400, 400);
		
		// Select a map provider
		AbstractMapProvider provider = new Google.GoogleTerrainProvider();
		// Set a zoom level
		int zoomLevel = 10;
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		mainMap = new UnfoldingMap(this, 50, 50, 700, 500, provider);

		mainMap.zoomAndPanTo(zoomLevel, new Location(40.762390, -73.968491));
		MapUtils.createDefaultEventDispatcher(this, mainMap);

	}
	
	/** Draw the Applet window.  */
	@Override
	public void draw() {
		
		mainMap.draw();
	}
}

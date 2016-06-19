// Get geolocation from the document like this: //"address" : { "building" : "461" , "coord" : [ -74.138492 , 40.631136] , "street" : "Port Richmond Ave" , "zipcode" : "10302"}
//Work in progress
package com.hmelinks.prod;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class MainMap extends PApplet{
	
	UnfoldingMap mainMap;
	private static final long serialVersionUID = 1L;
	private List<Marker> markers;

	//public static void main(String[] args) throws UnknownHostException {
	public void setupDB() throws UnknownHostException {	
		// My current position in NYC - Empire State Building as example
		float myLat = 40.7484f;
		float myLon = -73.9857f;
				
		CurrentLocation myPoint = new CurrentLocation(myLat, myLon);
		
		//Authentication string - meantime not in use
		//String userName = "olegTests";
		//String database = "test";
		//char[] password = new char[] {'o','l','e','g','T','e','s','t','s','P','W','1','2','3','4','5','6'};
		//MongoCredential credential = MongoCredential.createCredential(userName, database, password);
		
		//MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("HFAVWRMONQA1", 27017), new ServerAddress("HFAVWRMONQA1", 27011), new ServerAddress("HFAVWRMONQA1", 27012)));
		MongoClient mongoClient = new MongoClient ("HFAVWRMONQA1", 27017);
		
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		
		DB db = mongoClient.getDB("test");
		DBCollection coll = db.getCollection("restaurants");
		
		BasicDBObject query = new BasicDBObject();
	
		query.put("cuisine", "Chinese");
				
		DBCursor cursor = coll.find(query);
		
		float lat1 = (float) myPoint.closestPoints(1)[0];
		float lat2 = (float) myPoint.closestPoints(1)[1];
		float lon1 = (float) myPoint.closestPoints(1)[2];
		float lon2 = (float) myPoint.closestPoints(1)[3];
		
		markers = new ArrayList<Marker>();
		
		try {
				  
			while(cursor.hasNext()) {
					
			BasicBSONObject addressObj = (BasicBSONObject) cursor.next().get("address");
			BasicDBList addressList = (BasicDBList) addressObj.get("coord");
				
			double longi  = (double) addressList.get(0);
			double lati = (double) addressList.get(1);
			float restLongitude  = (float)longi;
			float restLatitude = (float)lati;
			
			if ((restLatitude < lat2 && restLatitude > lat1) && (restLongitude < lon2 && restLongitude > lon1)) {
				
				markers.add(new RestaurantMarker(restLatitude, restLongitude));
				System.out.println(restLatitude + "," + restLongitude + " box LAT is " + lat1 + ", " + lat2 + " box LON is " + lon1 + "," + lon2 );
			}
			
		}
		} catch (NullPointerException e) {
			
			System.err.println("NullPointerException: " + e.getMessage());
			
		} finally {
		  cursor.close();
		}
		
		for (Marker marker : markers) {
			System.out.printf(("Coordinates x: %f and y: %f \n"), marker.getLocation().getLat(), marker.getLocation().getLon());
		}
		mongoClient.close();
	
	}
		@Override
		public void setup() { 
		
		size(800, 600, P2D);  // Set up the Applet window to be 800x600

		// This sets the background color for the Applet.  
		this.background(400, 400, 400);
		
		// Select a map provider
		AbstractMapProvider provider = new Google.GoogleTerrainProvider();
		// Set a zoom level
		int zoomLevel = 10;
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		mainMap = new UnfoldingMap(this, 50, 50, 700, 500, provider);

		mainMap.zoomAndPanTo(zoomLevel, new Location(40.762390, -73.968491));
		MapUtils.createDefaultEventDispatcher(this, mainMap);
		
		//mainMap.addMarkers(markers);
		
	}
	
	/** Draw the Applet window.  */
	@Override
	public void draw() {
		
		mainMap.draw();
	}
}


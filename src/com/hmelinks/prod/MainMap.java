// Get geolocation from the document like this: //"address" : { "building" : "461" , "coord" : [ -74.138492 , 40.631136] , "street" : "Port Richmond Ave" , "zipcode" : "10302"}
//Work in progress
package com.hmelinks.prod;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedHashMap;
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

import de.fhpotsdam.unfolding.geo.Location;


public class MainMap {

	public static void main(String[] args) throws UnknownHostException {
		
		// My current position in NYC - Empire State Building as example
		double myLat = 40.7484;
		double myLon = -73.9857;
		
		CurrentLocation myPoint = new CurrentLocation(myLat, myLon);
		
		//Authentication string - meantime not in use
		//String userName = "olegTests";
		//String database = "test";
		//char[] password = new char[] {'o','l','e','g','T','e','s','t','s','P','W','1','2','3','4','5','6'};
		//MongoCredential credential = MongoCredential.createCredential(userName, database, password);
		
		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("HFAVWRMONQA1", 27017), new ServerAddress("HFAVWRMONQA1", 27011), new ServerAddress("HFAVWRMONQA1", 27012)));
		//MongoClient mongoClient = new MongoClient ("HFAVWRMONQA1", 27017);
		
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		
		DB db = mongoClient.getDB("test");
		DBCollection coll = db.getCollection("restaurants");
		
		BasicDBObject query = new BasicDBObject();
	
		query.put("cuisine", "Chinese");
				
		DBCursor cursor = coll.find(query);
		
		double lat1 = myPoint.closestPoints(1)[0];
		double lat2 = myPoint.closestPoints(1)[1];
		double lon1 = myPoint.closestPoints(1)[2];
		double lon2 = myPoint.closestPoints(1)[3];
		
		try {
				  
			while(cursor.hasNext()) {
					
			BasicBSONObject addressObj = (BasicBSONObject) cursor.next().get("address");
			BasicDBList addressList = (BasicDBList) addressObj.get("coord");
				
			 double restLongitude  = (double) addressList.get(0);
			 double restLatitude = (double) addressList.get(1);
			
			if ((restLatitude < lat2 && restLatitude > lat1) && (restLongitude < lon2 && restLongitude > lon1)) {
			  System.out.println(restLatitude + "," + restLongitude + " box LAT is " + lat1 + ", " + lat2 + " box LON is " + lon1 + "," + lon2 );
			}
			
		}
		} catch (NullPointerException e) {
			
			System.err.println("NullPointerException: " + e.getMessage());
			
		} finally {
		  cursor.close();
		}
		
	mongoClient.close();

	}
}


package com.hmelinks.prod;

public class CurrentLocation {

	public double latitude;
	public double longitude;
	
	public CurrentLocation(double lat, double lon) {
		
		this.latitude = lat;
		this.longitude = lon;
				
	}
	
	//Counts distance between two points
	//public double distance(double other_lat, double other_lon){
		
	//	return getDist(this.latitude, this.longitude, other_lat, other_lon, "K");
	/* public double getDist(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	} */

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	//Counts the surrounding box.
	public double[] closestPoints(double distance_in_km) {
		
			// Figure out the corners of a box surrounding our lat/lng.
			double radius = 6371; // of earth in km
			double[] arrayLocation = new double[4];

			// bearings
			int due_north = 0;
			int due_south = 180;
			int due_east = 90;
			int due_west = 270;

			// convert latitude and longitude into radians
			double lat_r = deg2rad(this.latitude);
			double lon_r = deg2rad(this.longitude);

			// find the northmost, southmost, eastmost and westmost corners distance_in_km away
			// original formula from
			// http://www.movable-type.co.uk/scripts/latlong.html

			double northmost  = Math.asin(Math.sin(lat_r) * Math.cos(distance_in_km/radius) + Math.cos(lat_r) * Math.sin(distance_in_km/radius) * Math.cos(due_north));
			double southmost  = Math.asin(Math.sin(lat_r) * Math.cos(distance_in_km/radius) + Math.cos(lat_r) * Math.sin (distance_in_km/radius) * Math.cos(due_south));

			double eastmost = lon_r + Math.atan2(Math.sin(due_east)*Math.sin(distance_in_km/radius)*Math.cos(lat_r),Math.cos(distance_in_km/radius)-Math.sin(lat_r)*Math.sin(lat_r));
			double westmost = lon_r + Math.atan2(Math.sin(due_west)*Math.sin(distance_in_km/radius)*Math.cos(lat_r),Math.cos(distance_in_km/radius)-Math.sin(lat_r)*Math.sin(lat_r));

			northmost = rad2deg(northmost);
			southmost = rad2deg(southmost);
			eastmost = rad2deg(eastmost);
			westmost = rad2deg(westmost);

			// sort the lat and long so that we can use them for a between query
			if (northmost > southmost) {
				arrayLocation[0] = southmost; //lat1
				arrayLocation[1] = northmost; //lat2

			} else {
				arrayLocation[0] = northmost;
				arrayLocation[1] = southmost;
			}

			if (eastmost > westmost) {
				arrayLocation[2] = westmost; //lon1
				arrayLocation[3] = eastmost; //lon2

			} else {
				arrayLocation[2] = eastmost;
				arrayLocation[3] = westmost;
			}
			
			return arrayLocation;
	}
}

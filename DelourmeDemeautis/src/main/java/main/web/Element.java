package main.web;

public class Element {

	String videolocation;
	String thumbnailLocation;
	
	public Element() {
		
	}
	
	public Element( String videolocation, String thumbnailLocation ) {
		this.videolocation = videolocation;
		this.thumbnailLocation = thumbnailLocation;
	}
	
	public String getVideolocation() {
		return videolocation;
	}
	public void setVideolocation(String videolocation) {
		this.videolocation = videolocation;
	}
	public String getThumbnailLocation() {
		return thumbnailLocation;
	}
	public void setThumbnailLocation(String thumbnailLocation) {
		this.thumbnailLocation = thumbnailLocation;
	}
	
}

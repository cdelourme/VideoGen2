package main.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.videoGen.AlternativesMedia;
import org.xtext.example.mydsl.videoGen.MandatoryMedia;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.MediaDescription;
import org.xtext.example.mydsl.videoGen.OptionalMedia;

import main.model.VideoTools;

public class ShortMedia implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String type ;
	List<String> videolocation;
	List<String> thumbnailLocation;
	
	public ShortMedia( Media m1) throws Exception {
		videolocation = new ArrayList<String>();
		thumbnailLocation = new ArrayList<String>();
		type = "";
		
		if (m1 != null) {
			type = m1.getClass().getName();
			if (m1 instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m1;
				MediaDescription md = mand.getDescription();
				videolocation.add( md.getLocation());
				thumbnailLocation.add(VideoTools.videoToImage(md.getLocation()));
				
			}else if (m1 instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m1;
				MediaDescription md = option.getDescription();
				videolocation.add( md.getLocation());
				thumbnailLocation.add(VideoTools.videoToImage(md.getLocation()));
				
			}else if (m1 instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m1;
				EList<MediaDescription> liste = alter.getMedias();
				for(MediaDescription m:liste) {
					videolocation.add(m.getLocation());
					thumbnailLocation.add(VideoTools.videoToImage(m.getLocation()));
				}
			}
		}
	}
	
}

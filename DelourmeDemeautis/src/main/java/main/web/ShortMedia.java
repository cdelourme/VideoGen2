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
	List<Element> elements;

	
	public ShortMedia( Media m1) throws Exception {
		elements = new ArrayList<Element>();
		type = "";
		
		if (m1 != null) {
			type = m1.getClass().getName();
			if (m1 instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m1;
				MediaDescription md = mand.getDescription();
				elements.add(new Element(md.getLocation() , VideoTools.videoToImage(md.getLocation())));

				
			}else if (m1 instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m1;
				MediaDescription md = option.getDescription();
				elements.add(new Element(md.getLocation() , VideoTools.videoToImage(md.getLocation())));
				
			}else if (m1 instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m1;
				EList<MediaDescription> liste = alter.getMedias();
				for(MediaDescription md:liste) {
					elements.add(new Element(md.getLocation() , VideoTools.videoToImage(md.getLocation())));
				}
			}
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	
}

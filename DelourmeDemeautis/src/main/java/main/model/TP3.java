package main.model;

import static org.junit.Assert.*;



import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Ignore;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativesMedia;
import org.xtext.example.mydsl.videoGen.ImageDescription;
import org.xtext.example.mydsl.videoGen.MandatoryMedia;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.MediaDescription;
import org.xtext.example.mydsl.videoGen.OptionalMedia;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import com.google.common.io.Files;

public class TP3 {

	String filename = "csv-file.csv";
	
	public String[] addMandatory( String[] tab ) {
		String[] tabRetour = new String[tab.length];
		int index = 0;
		for(String s:tab) {
			if (s == null)
				s="";
			tabRetour[index] = s + ";TRUE";
			index ++;
		}
		return tabRetour;
	}
	
	public String[] addOptionnal( String[] tab ) {
		String[] tabRetour = new String[tab.length * 2];
		int index = 0;
		for(String s:tab) {
			tabRetour[index] = s + ";TRUE";
			tabRetour[index+1] = s + ";FALSE";
			index += 2;
		}
		return tabRetour;
	}
	
	public String[] addAlternatives( String[] tab , int nbChoix) {
		String[] tabRetour = new String[tab.length * nbChoix];
		int index = 0;
		int i=0;
		for(String s:tab) {
			int nb = nbChoix;
			while(nb > 0) {
				String line = "";
				for(i=0;i<nbChoix;i++) {
					if (i == nb-1)
						line += ";TRUE";
					else
						line += ";FALSE";
				}
				tabRetour[index] = s + line;
				index++;
				nb--;
			}
		}
		
		return tabRetour;
	}
	
	public Long getFileSize(String filename) {
		File f = new File(filename);
		Long value = 0L;
		if (f.exists()) value = f.length();
		return value;
	}
	
	@Test
	public void generationCSVFile() throws Exception {
		
		String listeId = "";
		String[] listeCycle = new String[1];
		Map<String,String> listLocation = new LinkedHashMap<String,String>();
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/etude.videogen"));
		assertNotNull(videoGen);
		
		for (Media m : videoGen.getMedias()) {
			
			if (m instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m;
				MediaDescription md = mand.getDescription();
				
				if(md instanceof ImageDescription) {
					ImageDescription img = (ImageDescription)md;
					System.out.println("Mandatory image : " + img.getImageid());
					listeId += ";" + img.getImageid();
					listeCycle = addMandatory( listeCycle );
					listLocation.put( img.getImageid(),img.getLocation());

				}else if(md instanceof VideoDescription) {
					VideoDescription video = (VideoDescription)md;
					System.out.println("Mandatory video : " + video.getVideoid());
					listeId += ";" + video.getVideoid();
					listeCycle = addMandatory( listeCycle );
					listLocation.put( video.getVideoid(),video.getLocation());
				}
			}else if (m instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m;

				MediaDescription md = option.getDescription();
				if(md instanceof ImageDescription) {
					ImageDescription img = (ImageDescription)md;
					System.out.println("Optional image : " + img.getImageid());
					listeId += ";" + img.getLocation();
					listeCycle = addOptionnal( listeCycle );
					listLocation.put( img.getImageid(),img.getLocation());
					
				}else if(md instanceof VideoDescription) {
					VideoDescription video = (VideoDescription)md;
					System.out.println("Optional video : " +video.getVideoid());
					listeId += ";" + video.getVideoid();
					listeCycle = addOptionnal( listeCycle );
					listLocation.put( video.getVideoid(),video.getLocation());
				}
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				for( MediaDescription md : liste ) {
					if(md instanceof ImageDescription) {
						ImageDescription img = (ImageDescription)md;
						System.out.println("Alternatives image : " + img.getImageid());
						listeId += ";" + img.getImageid();
						listLocation.put( img.getImageid(),img.getLocation());
						
					}else if(md instanceof VideoDescription) {
						VideoDescription video = (VideoDescription)md;
						System.out.println("Alternatives video : " +video.getVideoid());
						listeId += ";" + video.getVideoid();
						listLocation.put( video.getVideoid(),video.getLocation());
					}
				}
				listeCycle = addAlternatives( listeCycle , liste.size());
			}
		}
		
				
		File f = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(listeId + ";size;realsize \n");
		int index = 1;
		
		String output = "video/output.mp4";
		String outputGIF = "video/output.gif";

		
		for (String line : listeCycle) {
			Long cumulSize = 0L;
			Long realSize = 0L;
			Long gifSize = 0L;
			int indexColonne = -1;
			List<String> concatLocation = new ArrayList<String>();
			
			// liste de media à concatener 
			List<String> medias = new ArrayList<String>();
			
			for (String str : line.split(";")) {
				if (str.equals("TRUE")) {
					//size
					Object[] keys = listLocation.keySet().toArray();
					String location = listLocation.get( keys[indexColonne] );
					concatLocation.add(location);
					cumulSize += getFileSize(location);
					//realsize
					medias.add(location);
				}
				indexColonne++;
			}
			VideoTools.concatenerMedia(concatLocation, output);
			realSize = getFileSize(output);
			
			VideoTools.convertMediaToGIF( output , outputGIF ,15, "640x480");
			gifSize = getFileSize(outputGIF);
			
			bw.write(index + line + ";" + cumulSize +";"+ realSize +";"+ gifSize +"\n");
			index++;
		}
		bw.close();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		 
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}

}

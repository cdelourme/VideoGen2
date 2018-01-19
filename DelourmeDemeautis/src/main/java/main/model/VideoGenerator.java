package main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.xtext.example.mydsl.videoGen.AlternativesMedia;
import org.xtext.example.mydsl.videoGen.MandatoryMedia;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.MediaDescription;
import org.xtext.example.mydsl.videoGen.OptionalMedia;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import main.web.ShortMedia;

public class VideoGenerator {

	static VideoGeneratorModel modelVideoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
	
	public static VideoGeneratorModel getVideoGeneratorModel() {
		return modelVideoGen;
	}
	

	public static String generateVideo(VideoGeneratorModel videoGen) {
		String output = "output"+new Random().nextInt(9999)+".mp4";
		try {
			VideoGenerator videoG = new VideoGenerator();
			videoG.generation(videoGen,"src/main/webapp/public/video/" + output);
		}catch (Exception e) {
			System.err.println("Erreur Generation Video " + e.getMessage());
		}
		
		return output;
	}
	
	
	/*
	 *  Generate a random video from a specific videogen file
	 */
	public void generation(VideoGeneratorModel videoGen, String output) throws Exception {
		
		List<String> listLocation = new ArrayList<String>();
		
		for (Media m : videoGen.getMedias()) {
			
			if (m instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m;
				MediaDescription md = mand.getDescription();
				listLocation.add( md.getLocation());
				
			}else if (m instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m;
				MediaDescription md = option.getDescription();
				
				if (getRandomInt(2) == 0)			// 50%
					listLocation.add( md.getLocation());
				
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				int nb = getRandomInt(liste.size());
				listLocation.add(liste.get(nb).getLocation());
			}
		}
		
		VideoTools.concatenerMedia(listLocation, output);

	}
	
	/*
	 *  Export a videogen file for the web interface
	 */
	public static List<ShortMedia> getListeVideo(VideoGeneratorModel videoGen) throws Exception {
		
		List<ShortMedia> listMedia = new ArrayList<ShortMedia>();
		
		for (Media m : videoGen.getMedias()) {
			listMedia.add(new ShortMedia(m));
		}
		return listMedia;
	}

	/*
	 *  Generate a random number like new Random().nextInt(9999)
	 */
	public int getRandomInt(int nbPossibilities) {
		
		Random random = new Random();
		int nb = random.nextInt();
		if (nb<0) nb = (nb * -1);
		return nb  % nbPossibilities;
	}
	

}

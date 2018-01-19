package main.web;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import main.model.VideoGenerator;
import main.model.VideoTools;

@RestController
public class Serveur {

	@RequestMapping("/generate")
	public @ResponseBody String getVideo() {
		VideoGeneratorModel videoGen = VideoGenerator.getVideoGeneratorModel();
		System.out.println("Let's go for a new random video :)");
		return "{\"output\": \"" + VideoGenerator.generateVideo(videoGen) + "\"}";
	}
	
	@RequestMapping("/custom")
	public @ResponseBody List<ShortMedia>  getListeVideo() {
		System.out.println("All pictures are now available. No problem, enjoy!");
		VideoGeneratorModel videoGen = VideoGenerator.getVideoGeneratorModel();
		List<ShortMedia> liste = null;
		
		try {
			liste = VideoGenerator.getListeVideo(videoGen);
			//return "{\"output\": \"" +  + "\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return liste;
	}

	@RequestMapping(value = "/generateVOD", method = RequestMethod.POST)
	public @ResponseBody String ReceptionPlaylist(@RequestBody List<String> requestedListe) {
		String returnString = "yourVideo.mp4";
		System.out.println("A VOD is requested with these videos ....");
		for(String s: requestedListe) {
			System.out.println("\t"+s);
		}
		try {
			VideoTools.concatenerMedia(requestedListe, "src/main/webapp/public/video/" + returnString);
			System.out.println("VOD generated : " + returnString); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"output\": \"" + returnString + "\"}";
	}
	
}

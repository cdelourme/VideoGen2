package main.web;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import main.model.VideoGenerator;

@RestController
public class Serveur {

	@RequestMapping("/generate")
	public @ResponseBody String getVideo() {
		VideoGeneratorModel videoGen = VideoGenerator.getVideoGeneratorModel();
		return "{\"output\": \"" + VideoGenerator.generateVideo(videoGen) + "\"}";
	}
	
	@RequestMapping("/custom")
	public @ResponseBody List<ShortMedia>  getListeVideo() {
		System.out.println("GetImages()");
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
	
}

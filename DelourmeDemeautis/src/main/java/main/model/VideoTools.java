package main.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VideoTools {

	public static String videoToImage(String videoPath) throws InterruptedException, IOException {
		String newImage = "thumbnail/" + videoPath + ".png";
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("ffmpeg -y -i "+ videoPath + " -r 1 -t 00:00:01 -ss 00:00:02 -f image2 "+newImage );
		p.waitFor();
		return newImage;
	}

	
	/*
	 * Generation d'une video qui rassemble plusieurs videos 
	 */
	public static void concatenerMedia(List<String> listeMedia , String output) throws IOException, InterruptedException {
		
		// S'assurer que toutes les videos sont au format mepg4
		// ffmpeg -y -i v102.mp4 -acodec copy v102a.mp4
		// sinon pas de son et video bancale
		
		Runtime run = Runtime.getRuntime();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("maList.txt"));
		for (String path:listeMedia) {
			bw.write("file \'" + path  + "\'\n");
		}
		bw.close();
		Process p = run.exec("ffmpeg -y -f concat -safe 0 -i maList.txt -c copy " + output);
		p.waitFor();
	}
}

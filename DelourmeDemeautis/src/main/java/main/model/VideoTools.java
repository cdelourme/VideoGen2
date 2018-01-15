package main.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VideoTools {

	public static String videoToImage(String videoPath) throws InterruptedException, IOException {
		String newImage = "thumbnail/" + videoPath + ".png";
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("ffmpeg -y -i "+ videoPath + " -r 1 -t 00:00:01 -ss 00:00:02 -f image2 "+ "src/main/webapp/public/" + newImage );
		p.waitFor();
		return newImage;
	}

	
	/*
	 * Generation d'une video qui rassemble plusieurs videos 
	 */
	public static void concatenerMedia(List<String> listeMedia , String output) throws IOException, InterruptedException {
		
		// #1	S'assurer que toutes les videos sont au format mepg4
		// 		ffmpeg -y -i v102.mp4 -acodec copy v102a.mp4
		// 		sinon pas de son et video bancale
		// #2	Pour eviter les décalage de son, il faut avoir un timescale correct
		//		ffmpeg -i titlefile.mp4 -vf setdar=16/9 -video_track_timescale 29971 -ac 48000 newtitle.mp4
		// #3	Une concaténation simple nous génère des erreurs de son donc : https://superuser.com/questions/855276/join-2-video-file-by-command-or-code
		// 		ffmpeg -i introlq.mp4 -i lq1.mp4 -filter_complex "[0:v][0:a][1:v][1:a]concat=n=2:v=1:a=1 [v] [a]" -map "[v]" -map "[a]"  output.mp4
		//ffmpeg -y  -i video/v100a.mp4 -i video/v101a.mp4 -i video/v107a.mp4 -i video/v102a.mp4 -filter_complex [0:v][0:a][1:v][1:a][2:v][2:a][3:v][3:a]concat=n=4:v=1:a=1 [v] [a] -map [v] -map [a] src/main/webapp/public/output2781.mp4
		
		Runtime run = Runtime.getRuntime();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("maList.txt"));
		//String param1 = " -filter_complex ";
		//String param0 = "";
		//int index = 0;
		for (String path:listeMedia) {
		//	param1 += "["+index+":v]["+index+":a]";
		//	param0 += " -i " + path;
		//	index++;
			bw.write("file \'" + path  + "\'\n");
		}
		//String param2 = "concat=n=" + index +":v=1:a=1 [v] [a]\" -map \"[v]\" -map \"[a]\" ";
		//String param2 = "concat=n=" + index +":v=1:a=1 [v] [a] -map [v] -map [a] ";
		bw.close();
		//-safe 0 -pix_fmt yuv420p
		// -vsync 0 -safe 0
		Process p = run.exec("ffmpeg -y -f concat -i maList.txt -c copy " + output);
		//Process p = run.exec("ffmpeg -f -y " + param0 + param1 + param2 + output);
		//System.out.println("ffmpeg -y " + param0 + param1 + param2 + output);
		p.waitFor();
	}
}

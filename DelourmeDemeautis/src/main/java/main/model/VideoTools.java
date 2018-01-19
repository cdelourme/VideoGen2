package main.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class VideoTools {

	/*
	 *  Generate a picture from a video
	 */
	public static String videoToImage(String videoPath) throws InterruptedException, IOException {
		String newImage = "thumbnail/" + videoPath + ".png";
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("ffmpeg -y -i "+ videoPath + " -r 1 -t 00:00:01 -ss 00:00:02 -f image2 "+ "src/main/webapp/public/" + newImage );
		p.waitFor();
		return newImage;
	}

	
	/*
	 *  Generate a single video from many videos.  
	 */
	public static void concatenerMedia(List<String> listeMedia , String output) throws IOException, InterruptedException {
		
		// #1	S'assurer que toutes les videos sont au format mepg4
		// 		ffmpeg -y -i v102.mp4 -acodec copy v102a.mp4
		// 		sinon pas de son et video bancale
		// #2	Pour éviter les décalages de son, il faut avoir un timescale correct
		//		ffmpeg -i titlefile.mp4 -vf setdar=16/9 -video_track_timescale 29971 -ac 48000 newtitle.mp4
		// #3	piste à explorer : https://superuser.com/questions/855276/join-2-video-file-by-command-or-code
		// 		ffmpeg -i introlq.mp4 -i lq1.mp4 -filter_complex "[0:v][0:a][1:v][1:a]concat=n=2:v=1:a=1 [v] [a]" -map "[v]" -map "[a]"  output.mp4
		
		Runtime run = Runtime.getRuntime();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("maList.txt"));
		for (String path:listeMedia) {
			bw.write("file \'" + path  + "\'\n");
		}
		bw.close();
		Process p = run.exec("ffmpeg -y -f concat -i maList.txt -c copy " + output);
		p.waitFor();
	}
	
	/*
	 * 	Convert a video to GIF, possible to modify some parameters, fps and scale. 
	 */
	public static void convertMediaToGIF(String m1, String output, int fps, String scale) throws IOException, InterruptedException {
		
		Runtime run = Runtime.getRuntime();
		
		//version normale basse qualité
		Process p = run.exec("ffmpeg -y -i "+ m1 +" -pix_fmt rgb24 -r "+ fps +" -s "+scale+" -f gif " + output);

		/* Version haute qualité avec gestion d'une palette
		 * Trop couteuse en temps
			//String command = "ffmpeg -v warning -i "+m1+" -vf fps="+fps+",scale="+scale+":-1:flags=lanczos,palettegen=stats_mode=diff -y video/palette.png";
			String command = "ffmpeg -y -ss 1 -i "+m1+" -vf fps=1,scale=160:-1:flags=lanczos,palettegen video/palette.png";
			Process p = run.exec(command);
			p.waitFor();
			//command = "ffmpeg -i "+m1+" -i video/palette.png -lavfi fps="+fps+",scale="+scale+":-1:flags=lanczos,paletteuse=dither=bayer:bayer_scale=5:diff_mode=rectangle -y " + output;
			command = "ffmpeg -y -ss 1 -i "+m1+" -i video/palette.png -filter_complex fps=1,scale=160:-1:flags=lanczos[x];[x][1:v]paletteuse " + output;
			System.out.println("GIF -> " + command);
			p = run.exec(command);
		*/		
		
		p.waitFor();
	}
	
	
	/*
	 *  Give the duration of a video
	 */
	public float getVideoDuration( String videoPath ) throws IOException, InterruptedException {
        
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " + videoPath);
		BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ligne = output.readLine();		
		p.waitFor();
		
		float nb = 0;
		if (ligne != null && ligne != "")
			nb = new Float(ligne).floatValue();
		
		//System.out.println(videoPath + " : " + nb);
		return nb;
	}
}

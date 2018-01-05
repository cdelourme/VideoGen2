import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class TP2 {

	
	public void lireVideo(String videoPath) throws IOException, InterruptedException {
	
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("vlc " + videoPath);
		p.waitFor();	
	}
	
	public void lirePlaylist(File playlist) throws IOException, InterruptedException {
		
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("vlc " + playlist);
		p.waitFor();	
	}
	
	public int getRandomInt(int nbPossibilities) {
		
		Random random = new Random();
		int nb = random.nextInt();
		if (nb<0) nb = (nb * -1);
		return nb  % nbPossibilities;
	}
	
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
	
	public float dureeVarianteLaPlusLongue(VideoGeneratorModel videoGen ) throws IOException, InterruptedException {
		
		float f = 0;

		for (Media m : videoGen.getMedias()) {
			
			if (m instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m;
				MediaDescription md = mand.getDescription();
				f += getVideoDuration( md.getLocation());		// Obligatoire
				
			}else if (m instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m;
				MediaDescription md = option.getDescription();
				f += getVideoDuration( md.getLocation());
				
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				List<Float> listDuration = new ArrayList<Float>();
				
				for( MediaDescription md : liste ) {
					if(md instanceof ImageDescription) {
//						ImageDescription img = (ImageDescription)md;			
					}else if(md instanceof VideoDescription) {
						VideoDescription video = (VideoDescription)md;
						listDuration.add(new Float(getVideoDuration( video.getLocation())));
					}
				}
				f += listDuration.stream().reduce(Float::max).get().floatValue();
			}
		}
		return f;
	}
	
	
	public File generatePlaylist(VideoGeneratorModel videoGen ) throws IOException {
		
		File f = new File("playlist.m3u");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		for (Media m : videoGen.getMedias()) {
			
			if (m instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m;
				MediaDescription md = mand.getDescription();
				bw.write( md.getLocation() + "\n");		// Obligatoire
				
			}else if (m instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m;
				MediaDescription md = option.getDescription();
				
				if (getRandomInt(2) == 0)			// 50%
					bw.write( md.getLocation() + "\n");
				
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				int nb = getRandomInt(liste.size());
				assertTrue("Alternatives random correct : ", (nb>=0 && nb < liste.size()));
				bw.write(liste.get(nb).getLocation() + "\n");
			}
		}
		bw.close();
		return f;
	}
	
	
	public String videoToImage(String videoPath) throws InterruptedException, IOException {
		String newImage = "thumbnail/" + videoPath + ".png";
		Runtime run = Runtime.getRuntime();
		Process p = run.exec("ffmpeg -y -i "+ videoPath + " -r 1 -t 00:00:01 -ss 00:00:02 -f image2 "+newImage );
		p.waitFor();
		return newImage;
	}
	
	@Ignore
	public void lireVideoSequentiel() throws Exception {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
		assertNotNull(videoGen);
		
	
		for (Media m : videoGen.getMedias()) {
			
			if (m instanceof MandatoryMedia){
				MandatoryMedia mand = (MandatoryMedia)m;
				MediaDescription md = mand.getDescription();
				lireVideo(md.getLocation());		// Obligatoire
				
			}else if (m instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m;
				MediaDescription md = option.getDescription();
				
				if (getRandomInt(2) == 0)			// 50%
					lireVideo(md.getLocation());
				
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				int nb = getRandomInt(liste.size());
				assertTrue("Alternatives random correct : ", (nb>=0 & nb < liste.size()));
				lireVideo(liste.get(nb).getLocation());
			}
		}
	}
	
	
	@Ignore
	public void testDurationVideo() throws IOException, InterruptedException {
		
		float nb = getVideoDuration("video/v1.mp4");
		assertTrue(nb > 0);
	}
	
	@Ignore
	public void testVideoToImage() throws IOException, InterruptedException {
		
		String img = videoToImage("video/v1.mp4");
		System.out.println( "Image generee : " + img );
		assertFalse(img == "");
	}
	
	@Test
	public void testPlaylist() throws IOException, InterruptedException {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
		assertNotNull(videoGen);
		File f = generatePlaylist(videoGen);
		assertNotNull(f);
		lirePlaylist( f );
		
	}
	
	@Ignore
	public void testRechercheDureelaPlusLongue() throws IOException, InterruptedException {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
		assertNotNull(videoGen);
		System.out.println( "Taille maximale video : " + dureeVarianteLaPlusLongue(videoGen));	
	}
	
}

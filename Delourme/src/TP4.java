import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class TP4 {

	String filename = "csv-file.csv";
	
	public int nbVariantes( VideoGeneratorModel videoGen ){
		
		int nb = 1;
		assertNotNull(videoGen);
		
		for (Media m : videoGen.getMedias()) {
			
			if (m instanceof OptionalMedia){
				nb = nb * 2;
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				if (liste.size() > 0 ) {
					nb = nb * liste.size();
				}
			}
		}
		return nb;
	}
	
	public int nbLigneFichier( File f) {
		int nb = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			 
			while ((line = br.readLine()) != null) {
				nb += 1;
			}
			br.close();
		}catch (Exception e) {
			System.err.println("Erreur NbLigneFichier");
		}		
		return nb-1;
	}
	
	@Test
	public void testNombreLignes() {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
		assertNotNull(videoGen);
		System.out.println( "Nb variantes : " + nbVariantes(videoGen) );
		System.out.println("Nb lignes du fichier : " + nbLigneFichier(new File(filename)));
		assertEquals(nbVariantes(videoGen),nbLigneFichier(new File(filename)));
	}
	
	
	@Test
	public void testFichierVideo() throws IOException {
		FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
		FFmpegProbeResult probeResult = ffprobe.probe("video/v1.mp4");

		FFmpegFormat format = probeResult.getFormat();
		System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs", 
			format.filename, 
			format.format_long_name,
			format.duration
		);
	}
	
	@Test
	public void launchVideo() throws IOException {
		FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
		FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
		FFmpegProbeResult probeResult = ffprobe.probe("video/v1.mp4");
		

		FFmpegBuilder builder = new FFmpegBuilder()

		  .setInput(probeResult)     // Filename, or a FFmpegProbeResult
		  .overrideOutputFiles(true) // Override the output if it exists

		  .addOutput("output.mp4")   // Filename for the destination
		    .setFormat("mp4")        // Format is inferred from filename, or can be set
		    .setTargetSize(1250_000)  // Aim for a 250KB file

		    .disableSubtitle()       // No subtiles

		    .setAudioChannels(1)         // Mono audio
		    .setAudioCodec("aac")        // using the aac codec
		    .setAudioSampleRate(48_000)  // at 48KHz
		    .setAudioBitRate(32768)      // at 32 kbit/s

		    .setVideoCodec("libx264")     // Video using x264
		    .setVideoFrameRate(24, 1)     // at 24 frames per second
		    .setVideoResolution(640, 480) // at 640x480 resolution

		    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
		    .done();

		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

		// Run a one-pass encode
//		executor.createJob(builder).run();

		// Or run a two-pass encode (which is slower at the cost of better quality)
		executor.createTwoPassJob(builder).run();
	}
	
	 static public void main(String[] args) throws IOException {
		FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
		FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
		FFmpegProbeResult probeResult = ffprobe.probe("../video/v1.mp4");
		

		FFmpegBuilder builder = new FFmpegBuilder()

		  .setInput(probeResult)     // Filename, or a FFmpegProbeResult
		  .overrideOutputFiles(true) // Override the output if it exists

		  .addOutput("output.mp4")   // Filename for the destination
		    .setFormat("mp4")        // Format is inferred from filename, or can be set
		    .setTargetSize(1250_000)  // Aim for a 250KB file

		    .disableSubtitle()       // No subtiles

		    .setAudioChannels(1)         // Mono audio
		    .setAudioCodec("aac")        // using the aac codec
		    .setAudioSampleRate(48_000)  // at 48KHz
		    .setAudioBitRate(32768)      // at 32 kbit/s

		    .setVideoCodec("libx264")     // Video using x264
		    .setVideoFrameRate(24, 1)     // at 24 frames per second
		    .setVideoResolution(640, 480) // at 640x480 resolution

		    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
		    .done();

		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

		// Run a one-pass encode
//		executor.createJob(builder).run();

		// Or run a two-pass encode (which is slower at the cost of better quality)
		executor.createTwoPassJob(builder).run();
	}
}

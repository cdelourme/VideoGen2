package main.model;
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
	
	public int nbVariantesFichier( File f) {
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
		return nb-1; // on retire les entetes
	}
	
	@Test
	public void testNombreLignes() {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
		assertNotNull(videoGen);
		System.out.println( "Nb variantes : " + nbVariantes(videoGen) );
		System.out.println("Nb lignes du fichier : " + nbVariantesFichier(new File(filename)));
		assertEquals(nbVariantes(videoGen),nbVariantesFichier(new File(filename)));
	}
}

import static org.junit.Assert.*;



import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativesMedia;
import org.xtext.example.mydsl.videoGen.ImageDescription;
import org.xtext.example.mydsl.videoGen.MandatoryMedia;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.MediaDescription;
import org.xtext.example.mydsl.videoGen.OptionalMedia;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

public class TP3 {

	String filename = "csv-file.csv";
	@Test
	public void generationCSVFile() throws Exception {
		
		String listeId = "";
		String[] listeCycle = new String[1];
		
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("samples/example1.videogen"));
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

				}else if(md instanceof VideoDescription) {
					VideoDescription video = (VideoDescription)md;
					System.out.println("Mandatory video : " + video.getVideoid());
					listeId += ";" + video.getVideoid();
					listeCycle = addMandatory( listeCycle );
				}
			}else if (m instanceof OptionalMedia){
				OptionalMedia option = (OptionalMedia)m;

				MediaDescription md = option.getDescription();
				if(md instanceof ImageDescription) {
					ImageDescription img = (ImageDescription)md;
					System.out.println("Optional image : " + img.getImageid());
					listeId += ";" + img.getLocation();
					listeCycle = addOptionnal( listeCycle );
					
				}else if(md instanceof VideoDescription) {
					VideoDescription video = (VideoDescription)md;
					System.out.println("Optional video : " +video.getVideoid());
					listeId += ";" + video.getVideoid();
					listeCycle = addOptionnal( listeCycle );
				}
			}else if (m instanceof AlternativesMedia){
				AlternativesMedia alter = (AlternativesMedia)m;
				EList<MediaDescription> liste = alter.getMedias();
				for( MediaDescription md : liste ) {
					if(md instanceof ImageDescription) {
						ImageDescription img = (ImageDescription)md;
						System.out.println("Alternatives image : " + img.getImageid());
						listeId += ";" + img.getImageid();
						
					}else if(md instanceof VideoDescription) {
						VideoDescription video = (VideoDescription)md;
						System.out.println("Alternatives video : " +video.getVideoid());
						listeId += ";" + video.getVideoid();
					}
				}
				listeCycle = addAlternatives( listeCycle , liste.size());
			}
		}
		
				
		File f = new File(filename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(listeId + "\n");
		int index = 1;
		for (String s : listeCycle) {
			bw.write(index + s + "\n");
			index++;
		}
		bw.close();
		
		ajoutColonneSize(new File(filename));
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		 
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
	
	public void ajoutColonneSize( File csvFile ) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String line;
		
		int indexLigne = 0;
		Map<String,Long> id = new LinkedHashMap();
		while ((line = br.readLine()) != null) {
			
			Long tailleVariante = 0L;
			int indexColonne = 0;
			for (String str : line.split(";")) {
				if (indexLigne == 0) {
					//lecture des id
					//alimente la map avec id/taille
					if (str != "") {
						Long value = 0L;
						File f = new File(str);
						if (f.exists()) value = f.length();
						id.put(str, new Long(value));
					}
				}
				else {
					if( str.equals("TRUE") ) {
						Object[] keys = id.keySet().toArray();
						System.out.println("index colonne : " + keys[indexColonne]);
						tailleVariante += id.get( keys[indexColonne] );
						
						//Attention on gere des id et non des nom de fichier !!!!!
					}
				}
				indexColonne += 1;
				System.out.println( "variante " + indexLigne + " : " + tailleVariante + "\n");
			}
			System.out.println(line);
			indexLigne += 1;
		}
		br.close();
	}
	
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
	
}

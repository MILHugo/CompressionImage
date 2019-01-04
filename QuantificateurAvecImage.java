package V1;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class QuantificateurAvecImage {

    private Integer[][] matrixEchelonnée;
    private Integer[][] matrixQuantifiée;
    private Double[][] imageCapté;
    private String urlEntre;
    private String urlSortie;
    private Integer lvlOfQuantification;
    private Integer numVer;

    public QuantificateurAvecImage(String url, Integer lvlOfQuantification) {
	this.urlEntre = url;
	this.urlSortie = new File(url).getParent() + "\\Final_" + new File(url).getName();
	System.out.println(urlSortie);
	this.lvlOfQuantification = lvlOfQuantification;
	System.out.println(quantifier());
    }

    private void marchThroughImage(BufferedImage image) {
	Integer imageSize;
	int w = image.getWidth();
	int h = image.getHeight();
	if (w > h) {
	    imageSize = h;
	} else {
	    imageSize = w;
	}

	imageCapté = new Double[imageSize][imageSize];
	System.out.println("width, height: " + w + ", " + h);

	for (int i = 0; i < imageSize; i++) {
	    for (int j = 0; j < imageSize; j++) {
		// System.out.println("x,y: " + i + ", " + j);
		int pixel = image.getRGB(j, i);
		Color mycoor = new Color(pixel);
		imageCapté[i][j] = (double) mycoor.getRed();
	    }
	}
	this.matrixEchelonnée = new Integer[w][h];
	this.matrixQuantifiée = new Integer[w][h];
    }

    private String quantifier() {
	try {
	    marchThroughImage(ImageIO.read(new File(urlEntre)));
	} catch (IOException e) {
	}
	numVer = 0;
	//absoluteAll();
	System.out.println("AbsoluteAll ☑");
	scaleTo();
	System.out.println("ScaleTo ☑");
	leveliseTo();
	System.out.println("LeveliseTo ☑");
	// draw(matrixEchelonnée);
	draw(matrixQuantifiée);
	// return "---------------------------Matrice de base" +
	// toStringofMatrix(imageCapté);
	return "";
    }

    private void draw(Object[][] unematrix) {
	numVer++;
	try {
	    BufferedImage image = new BufferedImage(imageCapté.length, imageCapté.length, BufferedImage.TYPE_INT_RGB);
	    for (int i = 0; i < unematrix.length; i++) {
		for (int j = 0; j < unematrix[0].length; j++) {
		    int a = (int) unematrix[i][j];
		    Color newColor = new Color(a, a, a);
		    image.setRGB(j, i, newColor.getRGB());
		}
	    }
	    // BufferedImage resized = resize(image, 500, 500);
	    ImageIO.write((RenderedImage) image, "jpg", new File(urlSortie));
	} catch (Exception e) {
	}
    }

    private void absoluteAll() {
	for (int i = 0; i < matrixQuantifiée.length; i++) {
	    for (int j = 0; j < matrixQuantifiée[0].length; j++) {
		imageCapté[i][j] = Math.abs(imageCapté[i][j]);
	    }
	}
    }

    private void scaleTo() {
	Double Vmax = 0D;
	for (int i = 0; i < imageCapté.length; i++) {
	    for (int j = 0; j < imageCapté[0].length; j++) {
		if (imageCapté[i][j] > Vmax)
		    Vmax = imageCapté[i][j];
	    }
	}
	System.out.println("\nVMAX=" + Vmax);

	for (int i = 0; i < imageCapté.length; i++) {
	    for (int j = 0; j < imageCapté[0].length; j++) {
		matrixEchelonnée[i][j] = (int) Math.round(imageCapté[i][j] / Vmax * 255);
	    }
	}
    }

    private void leveliseTo() {
	Integer lengthOfEachLvl = 255 / (lvlOfQuantification );
	ArrayList<Integer> levels = new ArrayList<Integer>();
	Integer totallenght = 0;
	for (int k = 0; k < lvlOfQuantification; k++) {
	    totallenght += lengthOfEachLvl;
	    if (k == lvlOfQuantification - 1) {
		while (totallenght < 255)
		    totallenght += 1;
	    }
	    levels.add(totallenght);
	}
	System.out.println(levels);

	for (int i = 0; i < imageCapté.length; i++) {
	    for (int j = 0; j < imageCapté[0].length; j++) {
		matrixQuantifiée[i][j] = 0;
		for (int h = 0; h < levels.size(); h++) {
		    if (matrixEchelonnée[i][j].intValue() >= levels.get(h)) {
			matrixQuantifiée[i][j] = levels.get(h);
		    }
		}
	    }
	}
    }

    private void toStringofMatrix(Object[][] uneMatrix) {
	String text = "";
	for (int i = 0; i < uneMatrix.length; i++) {
	    text += "\n";
	    for (int j = 0; j < uneMatrix[0].length; j++) {
		text += "\t\t" + uneMatrix[i][j];
	    }
	}
	//return text;
    }
}
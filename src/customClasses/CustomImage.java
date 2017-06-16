package customClasses;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

import cs355.model.image.CS355Image;

public class CustomImage extends CS355Image {

	@Override
	public BufferedImage getImage() {
		BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < this.getWidth(); i++){
			for(int j = 0; j < this.getHeight(); j++){
				bi.getRaster().setPixel(i, j, this.getPixel(i, j, null));
			}
		}
		return bi;
	}

	@Override
	public void edgeDetection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sharpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void medianBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void uniformBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void grayscale() {
		// TODO Auto-generated method stub

	}

	@Override
	public void contrast(int amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void brightness(int amount) {
		// TODO Auto-generated method stub

	}

}

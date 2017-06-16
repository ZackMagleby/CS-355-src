package customClasses;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import cs355.model.image.CS355Image;

public class CustomImage extends CS355Image {

	@Override
	public BufferedImage getImage() {
		BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB );
		int[] data;
		for(int i = 0; i < this.getWidth(); i++){
			for(int j = 0; j < this.getHeight(); j++){
				bi.setRGB(i, j, this.getPixel(i, j, null)[0]);
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

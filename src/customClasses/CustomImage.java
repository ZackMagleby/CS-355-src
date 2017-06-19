package customClasses;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Arrays;

import com.sun.javafx.image.IntPixelAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import cs355.model.image.CS355Image;
import jdk.management.resource.internal.UnassignedContext;

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
		int[] rgb = new int[3];
		int[][][] temp = new int[this.getWidth()][this.getHeight()][3];
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				getPixel(i, j, rgb);
				
				int[] topLeft = {0,0,0};
				int[] topMid = {0,0,0};
				int[] topRight = {0,0,0};
				int[] left = {0,0,0};
				int[] right = {0,0,0};
				int[] botLeft = {0,0,0};
				int[] botMid = {0,0,0};
				int[] botRight = {0,0,0};
				
				if(i == 0 && j == 0){
					right = getPixel(i, j+1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(i == 0 && j == this.getHeight()-1){
					left = getPixel(i, j-1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
				}
				else if(i == this.getWidth()-1 && j == 0){
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					right = getPixel(i, j+1, null);
				}
				else if(i == this.getWidth()-1 && j == this.getHeight()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					left = getPixel(i, j-1, null);
				}
				else if(j == 0){
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					right = getPixel(i, j+1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(j == this.getHeight()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					left = getPixel(i, j-1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
				}
				else if(i == 0){
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(i == this.getWidth()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
				}
				else{
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				int redValue = ((topMid[0]*-1)+(left[0]*-1)+(right[0]*-1)+(botMid[0]*-1)+(rgb[0]*6))/2;
				int greenValue = ((topMid[1]*-1)+(left[1]*-1)+(right[1]*-1)+(botMid[1]*-1)+(rgb[1]*6))/2;
				int blueValue = ((topMid[2]*-1)+(left[2]*-1)+(right[2]*-1)+(botMid[2]*-1)+(rgb[2]*6))/2;
				if(redValue > 255){
					redValue = 255;
				}
				if(greenValue > 255){
					greenValue = 255;
				}
				if(blueValue > 255){
					blueValue = 255;
				}
				if(redValue < 0){
					redValue = 0;
				}
				if(greenValue < 0){
					greenValue = 0;
				}
				if(blueValue < 0){
					blueValue = 0;
				}
				int[] newColor = {redValue,greenValue,blueValue};
				temp[i][j] = newColor;
			}
		}
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				setPixel(i, j, temp[i][j]);				
			}
		}
	}

	@Override
	public void medianBlur() {
		int[] rgb = new int[3];
		int[][][] temp = new int[this.getWidth()][this.getHeight()][3];
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				getPixel(i, j, rgb);
				
				int[] topLeft = {0,0,0};
				int[] topMid = {0,0,0};
				int[] topRight = {0,0,0};
				int[] left = {0,0,0};
				int[] right = {0,0,0};
				int[] botLeft = {0,0,0};
				int[] botMid = {0,0,0};
				int[] botRight = {0,0,0};
				
				if(i == 0 && j == 0){
					right = getPixel(i, j+1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(i == 0 && j == this.getHeight()-1){
					left = getPixel(i, j-1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
				}
				else if(i == this.getWidth()-1 && j == 0){
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					right = getPixel(i, j+1, null);
				}
				else if(i == this.getWidth()-1 && j == this.getHeight()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					left = getPixel(i, j-1, null);
				}
				else if(j == 0){
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					right = getPixel(i, j+1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(j == this.getHeight()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					left = getPixel(i, j-1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
				}
				else if(i == 0){
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(i == this.getWidth()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
				}
				else{
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				int[] allRed = {topLeft[0], topMid[0], topRight[0], left[0], rgb[0], right[0], botLeft[0], botMid[0], botRight[0]};
				int[] allGreen = {topLeft[1], topMid[1], topRight[1], left[1], rgb[1], right[1], botLeft[1], botMid[1], botRight[1]};
				int[] allBlue = {topLeft[2], topMid[2], topRight[2], left[2], rgb[2], right[2], botLeft[2], botMid[2], botRight[2]};
				Arrays.sort(allRed);
				Arrays.sort(allGreen);
				Arrays.sort(allBlue);
				int[] medColor = {allRed[4], allGreen[4], allBlue[4]};
				double dist1 = distance(medColor, topLeft);
				double dist2 = distance(medColor, topMid);
				double dist3 = distance(medColor, topRight);
				double dist4 = distance(medColor, left);
				double dist5 = distance(medColor, rgb);
				double dist6 = distance(medColor, right);
				double dist7 = distance(medColor, botLeft);
				double dist8 = distance(medColor, botMid);
				double dist9 = distance(medColor, botRight);
				double[] allDist = {dist1,dist2,dist3,dist4,dist5,dist6,dist7,dist8,dist9};
				Arrays.sort(allDist);
				double maxDist = allDist[8];
				int[] colorToUse = {0,0,0};
				if(maxDist == dist1){
					colorToUse = topLeft;
				}
				else if(maxDist == dist2){
					colorToUse = topMid;
				}
				else if(maxDist == dist3){
					colorToUse = topRight;
				}
				else if(maxDist == dist4){
					colorToUse = left;
				}
				else if(maxDist == dist5){
					colorToUse = rgb;
				}
				else if(maxDist == dist6){
					colorToUse = right;
				}
				else if(maxDist == dist7){
					colorToUse = botLeft;
				}
				else if(maxDist == dist8){
					colorToUse = botMid;
				}
				else if(maxDist == dist9){
					colorToUse = botRight;
				}
				
				temp[i][j] = colorToUse;
			}
		}
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				setPixel(i, j, temp[i][j]);				
			}
		}
	}

	@Override
	public void uniformBlur() {
		int[] rgb = new int[3];
		int[][][] temp = new int[this.getWidth()][this.getHeight()][3];
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				getPixel(i, j, rgb);
				
				int[] topLeft = {0,0,0};
				int[] topMid = {0,0,0};
				int[] topRight = {0,0,0};
				int[] left = {0,0,0};
				int[] right = {0,0,0};
				int[] botLeft = {0,0,0};
				int[] botMid = {0,0,0};
				int[] botRight = {0,0,0};
				
				if(i == 0 && j == 0){
					right = getPixel(i, j+1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(i == 0 && j == this.getHeight()-1){
					left = getPixel(i, j-1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
				}
				else if(i == this.getWidth()-1 && j == 0){
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					right = getPixel(i, j+1, null);
				}
				else if(i == this.getWidth()-1 && j == this.getHeight()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					left = getPixel(i, j-1, null);
				}
				else if(j == 0){
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					right = getPixel(i, j+1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(j == this.getHeight()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					left = getPixel(i, j-1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
				}
				else if(i == 0){
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				else if(i == this.getWidth()-1){
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
				}
				else{
					topLeft = getPixel(i-1, j-1, null);
					topMid = getPixel(i-1, j, null);
					topRight = getPixel(i-1, j+1, null);
					left = getPixel(i, j-1, null);
					right = getPixel(i, j+1, null);
					botLeft = getPixel(i+1, j-1, null);
					botMid = getPixel(i+1, j, null);
					botRight = getPixel(i+1, j+1, null);
				}
				int totalRed = topLeft[0] + topMid[0] + topRight[0] + left[0] + rgb[0] + right[0] + botLeft[0] + botMid[0] + botRight[0];
				int totalGreen = topLeft[1] + topMid[1] + topRight[1] + left[1] +  rgb[1] + right[1] + botLeft[1] + botMid[1] + botRight[1];
				int totalBlue = topLeft[2] + topMid[2] + topRight[2] + left[2] +  rgb[2] + right[2] + botLeft[2] + botMid[2] + botRight[2];
				int[] averages = {totalRed/9, totalGreen/9, totalBlue/9};
				temp[i][j] = averages;
			}
		}
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				setPixel(i, j, temp[i][j]);				
			}
		}
	}

	@Override
	public void grayscale() {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		// Do the following for each pixel:
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				// Get the color from the image.
				getPixel(i, j, rgb);
				// Convert to HSB.
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// Do whatever operation you’re supposed to do...
				
				// Convert back to RGB.
				Color c = Color.getHSBColor(hsb[0], 0, hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();
				// Set the pixel.
				setPixel(i, j, rgb);				
			}
		}
	}

	@Override
	public void contrast(int amount) {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		// Do the following for each pixel:
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				// Get the color from the image.
				getPixel(i, j, rgb);
				// Convert to HSB.
				double step1 = ((((double)amount)/100)+100)/100;
				double step2 = Math.pow(step1, 4);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// Do whatever operation you’re supposed to do...
				double step3 = step2 * (hsb[2] - 128);
				double step4 = step3 + 128;
				// Convert back to RGB.
				Color c = Color.getHSBColor(hsb[0], hsb[1], (float)step4);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();
				// Set the pixel.
				setPixel(i, j, rgb);				
			}
		}
	}

	@Override
	public void brightness(int amount) {
		// Preallocate the arrays.
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		// Do the following for each pixel:
		for(int i = 0; i<this.getWidth(); i++){
			for(int j = 0; j<this.getHeight(); j++){
				// Get the color from the image.
				getPixel(i, j, rgb);
				// Convert to HSB.
				float convertedAmount = ((float)amount)/100;
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// Do whatever operation you’re supposed to do...
				hsb[2] += convertedAmount;
				float tempValue2 = hsb[2];
				if(hsb[2] > 1){
					tempValue2 = 1;
				}
				if(hsb[2] < -1){
					tempValue2 = -1;
				}
				// Convert back to RGB.
				Color c = Color.getHSBColor(hsb[0], hsb[1], tempValue2);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();
				// Set the pixel.
				setPixel(i, j, rgb);				
			}
		}
	}
	
	private double distance(int[] start, int[] end){
		double dis1 = Math.pow(start[0] + end[0], 2);
		double dis2 = Math.pow(start[1] + end[1], 2);
		double dis3 = Math.pow(start[2] + end[2], 2);
		return Math.sqrt(dis1 + dis2 + dis3);
	}
}

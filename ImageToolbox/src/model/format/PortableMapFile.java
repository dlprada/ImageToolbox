package model.format;

import java.awt.image.BufferedImage;

public abstract class PortableMapFile {
	protected int imageType;
	protected int width, height, maxval;
	protected int[] data;

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMaxval() {
		return maxval;
	}

	public void setMaxval(int maxval) {
		this.maxval = maxval;
	}

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	/**
	 * Builds a BufferedImage from the object
	 * 
	 * @return The built BufferedImage
	 * 
	 */
	public BufferedImage buildImage() {
		// Generates a BufferedImage on the correct size and format
		BufferedImage output = new BufferedImage(getWidth(), getHeight(), this.imageType);

		// Transfers the pixel matrix to the object
		output.getRaster().setPixels(0, 0, getWidth(), getHeight(), getData());

		return output;
	}
}

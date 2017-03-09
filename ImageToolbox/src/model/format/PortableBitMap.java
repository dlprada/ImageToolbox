package model.format;

import java.awt.image.BufferedImage;

public class PortableBitMap extends PortableMapFile {

	public PortableBitMap() {
		this.imageType = BufferedImage.TYPE_BYTE_BINARY;
	}

	/**
	 * Since Java interprets 0 as black and 1 as white, as opposed of the PBM
	 * format, the bytes from the pixel matrix must be flipped before building
	 * the image.
	 * 
	 * @return The built BufferedImage
	 */
	@Override
	public BufferedImage buildImage() {
		// Backup the original pixel matrix
		int[] origPixels = this.data.clone();

		// Flips the image bytes
		for (int i = 0; i < this.data.length; i++)
			this.data[i] = (this.data[i] + 1) % 2;

		BufferedImage rendered = super.buildImage();

		// Restores the original (unflipped) bytes to the object
		this.setData(origPixels);

		return rendered;
	}
}

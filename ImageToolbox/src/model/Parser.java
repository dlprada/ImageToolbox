package model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import model.format.PortableBitMap;
import model.format.PortableMapFile;

public class Parser {
	// Whitespace characters on the Netpbm project: \t, \n, \v, \f, \r, Space
	private final char[] WHITESPACE_CHARACTERS = { 32, 9, 10, 11, 12, 13 };
	private final char LINEFEED_CHARACTER = 10;
	private final char COMMENTARY_CHARACTER = '#';

	/**
	 * Tests if the desired character is a whitespace character
	 * 
	 * @param character
	 *            Character as integer that will be tested
	 * @return true if the character is a whitespace character
	 */
	private boolean isWhitespace(int character) {
		for (int i = 0; i < WHITESPACE_CHARACTERS.length; i++) {
			if ((char) character == WHITESPACE_CHARACTERS[i])
				return true;
		}

		return false;

	}

	/**
	 * Parses a PBM file.
	 * 
	 * @param filename
	 *            Path to the PBM file to be parsed
	 * @return The parsed PortableMapFile object
	 */
	public PortableMapFile parse(String imagePath) {
		File imageFile = new File(imagePath);
		PortableMapFile output = null;

		try {
			Reader r = new FileReader(imageFile);

			int[] tempData = null;
			int dataPosition = 0;
			int currentCharacter;
			int segmentsRead = 0;
			boolean isCommentary = false;
			String buffer = "";

			readcharacter: while ((currentCharacter = r.read()) != -1) {
				// Check if the actual character is a commentary delimiter. If it is, seeks for the linefeed character
				if ((char) currentCharacter == COMMENTARY_CHARACTER || isCommentary) {

					if (currentCharacter == LINEFEED_CHARACTER)
						isCommentary = false;

					else
						isCommentary = true;

					continue readcharacter;

				}

				// While the actual character isn't a whitespace character, inserts it into a buffer
				while (!isWhitespace(currentCharacter)) {
					buffer += (char) currentCharacter;

					continue readcharacter;
				}

				// If there is nothing in the buffer, returns to the beginning
				if (buffer.equals(""))
					continue;

				//Reads the buffered segments, and builds the object
				segmentsRead++;
				switch (segmentsRead) {

				// The first segment will always contain the MagicNumber, which defines the file format
				case 1:
					if (buffer.equals("P1"))
						output = new PortableBitMap();

					//else if (buffer.equals("P2")) output = new PortableGrayscaleMap();

					//else if (buffer.equals("P3")) output = new PortablePixelMap();

					break;

				// The second segment will always contain the image width
				case 2:
					output.setWidth(Integer.valueOf(buffer));

					break;

				// The third segment will always contain the image height
				case 3:
					output.setHeight(Integer.valueOf(buffer));

					// A portable pixelmap will have 3 segments per pixel (each containing it's red, green and blue values), so it should be 3 times larger
					//if (output instanceof PortablePixelMap)
					//tempData = new int[output.getWidth() * output.getHeight() * 3];

					//else
					tempData = new int[output.getWidth() * output.getHeight()];

					break;

				// The fourth segment will contain the maximum pixel value
				case 4:
					// A portable bitmap will not have this segment, because it can only be represent by two values
					if (!(output instanceof PortableBitMap)) {
						output.setMaxval(Integer.valueOf(buffer));
					} else {
						output.setMaxval(1);
					}

					break; // modificado

				// The fifth segment and on will contain the pixel data
				default:

					tempData[dataPosition] = (int) ((Integer.valueOf(buffer) / output.getMaxval()) * 255); //modificado

					dataPosition++;

					break;
				}

				// Flushes the buffer
				buffer = "";
			}

			output.setData(tempData);

			r.close();
		} catch (IOException e) {
			System.out.println("Error: Could not read file: " + imagePath + "(" + e.getMessage() + ")");
		} catch (NumberFormatException n) {
			System.out.println("Error: Invalid PBM file.");
		}

		return output;
	}
}

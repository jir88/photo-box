/**
 * 
 */
package main;

import java.io.File;

/**
 * @author john
 *
 */
public class Photo {
	/**
	 * Define where this image is located.
	 */
	private File myLocation;
	
	public Photo(File location) {
		myLocation = location;
	}

	/**
	 * Gets the location of this photo in the file system.
	 * @return the location
	 */
	public File getLocation() {
		return myLocation;
	}

	/**
	 * Redefines the location of this photo in the file system.
	 * TODO: change to use move location method to avoid overwriting.
	 * @param location the location to set
	 */
	public void setLocation(File location) {
		myLocation = location;
	}

}

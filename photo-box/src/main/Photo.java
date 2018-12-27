/**
 * 
 */
package main;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 * @author john
 *
 */
public class Photo {
	/**
	 * Define where this image is located.
	 */
	private File myLocation;
	/**
	 * The metadata stored in this photo, such as camera settings and
	 */
	private Metadata myMetadata;
	/**
	 * A small version of this image.
	 */
	private BufferedImage myThumbnail;
	
	/**
	 * Constructs a Photo object for a particular image file.
	 * @param location the image file location
	 * @throws IOException if an error occurs while reading the file
	 * @throws ImageProcessingException if an error occurs while parsing the 
	 * image metadata
	 */
	public Photo(File location) throws ImageProcessingException, IOException {
		// store the photo's location
		myLocation = location;
		
		// read the photo metadata
		myMetadata = ImageMetadataReader.readMetadata(myLocation);
		
		// set the thumbnail to null -- we load it lazily if requested
		myThumbnail = null;
	}
	
	/**
	 * Gets the date and time when this photo was originally taken.
	 * @return the time, or null if it is not encoded in the metadata
	 */
	public Date getDate() {
		// declare all variables
		Collection<ExifSubIFDDirectory> subIFDDirs;
		Collection<ExifIFD0Directory> ifd0Dirs;
//		Collection<FileSystemDirectory> fileDirs;
		
		// first try the [Exif SubIFD]Date/Time Original tag
		subIFDDirs = 
				myMetadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
		// loop over directories and check each for the tag
		for(ExifSubIFDDirectory dir : subIFDDirs) {
			if(dir.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
				// TODO: improve to incorporate time zone tag, if available
				return dir.getDateOriginal();
			}
		}
		
		// next try the [Exif IFD0] Date/Time tag
		ifd0Dirs = 
				myMetadata.getDirectoriesOfType(ExifIFD0Directory.class);
		// loop over directories and check each for the tag
		for(ExifIFD0Directory dir : ifd0Dirs) {
			if(dir.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
				// TODO: improve to incorporate time zone tag, if available
				return dir.getDate(ExifIFD0Directory.TAG_DATETIME);
			}
		}
		
		// finally, fall back to the file modification date [File] File Modified Date
		// FIXME: Use the NIO system to attempt getting file creation date!
//		fileDirs = 
//				myMetadata.getDirectoriesOfType(FileSystemDirectory.class);
//		// loop over directories and check each for the tag
//		for(FileSystemDirectory dir : fileDirs) {
//			if(dir.containsTag(FileSystemDirectory.TAG_FILE_MODIFIED_DATE)) {
//				// TODO: improve to incorporate time zone tag, if available
//				return dir.getDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE);
//			}
//		}
		return null;
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
	
	/**
	 * Gets the metadata associated with this photo.
	 * @return the metadata
	 */
	public Metadata getMetadata() {
		return myMetadata;
	}

	/**
	 * Changes the metadata associated with this photo.
	 * @param metadata the new metadata
	 */
	public void setMetadata(Metadata metadata) {
		myMetadata = metadata;
	}
	
	/**
	 * Get a small thumbnail of this photo. If the aspect ratio does not match
	 * the size requested, the photo is scaled to fit.
	 * @param size the size of the thumbnail, in pixels
	 * @return the thumbnail
	 * @throws IOException if there is an error while loading the thumbnail
	 */
	public BufferedImage getThumbnail(Dimension size) throws IOException {
		BufferedImage img;
		AffineTransformOp scaleOp;
		double heightScale, widthScale, imgScale;
		double imgAspect, desiredAspect;
		
		// if we already have a thumbnail loaded, check to see if it is the right size
		if(myThumbnail != null) {
			// calculate aspect ratios for thumbnail and target sizes
			imgAspect = ((double)myThumbnail.getWidth())/myThumbnail.getHeight();
			desiredAspect = size.getWidth()/size.getHeight();
			// if thumb is wider than the desired size
			if(imgAspect > desiredAspect) {
				// we just need to make sure the thumb's width is correct
				if(myThumbnail.getWidth() == size.width) {
					return myThumbnail;
				}
			} else { // thumb is narrower, make sure height is correct
				if(myThumbnail.getHeight() == size.height) {
					return myThumbnail;
				}
			}
//			if(myThumbnail.getHeight() == size.height &&
//					myThumbnail.getWidth() == size.width) {
//				// thumbnail is the right size, so just return it
//				return myThumbnail;
//			}
		} 
		// thumbnail isn't loaded or is wrong size, so we need to load it now
		
		// read full image from file
		img = ImageIO.read(myLocation);
		// figure out how much it needs to be scaled
		heightScale = size.getHeight()/img.getHeight();
		widthScale = size.getWidth()/img.getWidth();
		// use whichever axis needs to be scaled the most
		imgScale = Math.min(heightScale, widthScale);
		// create an image scaling object
		scaleOp = new AffineTransformOp(
				AffineTransform.getScaleInstance(imgScale, imgScale), 
				AffineTransformOp.TYPE_BILINEAR);
		// draw the scaled image
		myThumbnail = scaleOp.filter(img, null);
		// return the thumbnail
		return myThumbnail;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// convert photo metadata to a string
		return "Photo[location = " + myLocation + "]";
	}

	/**
	 * Checks to see if a file is an image.
	 */
	public static boolean isPhoto(File location) {
		String name;
		
		// does this file exist?
		if(!location.exists()) {
			return false;
		}
		// is it a directory?
		if(location.isDirectory()) {
			return false;
		}
		// is it a picture?
		// FIXME: need to make this actually detect file formats!!
		name = location.getName();
		return name.matches(".*\\.(jpg|JPG|jpeg|JPEG)$");
	}

}

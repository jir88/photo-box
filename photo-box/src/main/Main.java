/**
 * 
 */
package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;

/**
 * @author john
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// declare what variables we are going to define later
		File loc;
		Photo pic;
		File[] lst;
		List<Photo> picLst;
		
		// sanity check argument
		if(args.length == 0) { // if no argument provided
			System.err.println("Please provide a directory name!");
			System.exit(1); // exit with an arbitrary error code
		}
		// check if argument is an existing directory
		loc = new File(args[0]);
		picLst = Main.loadPhotos(loc);
		Date date = picLst.get(0).getDate();
		Calendar calDate = Calendar.getInstance();
		
		System.out.println(picLst); // print the array list of photos
		
//		// look for date when first picture in list was taken
//		for(Directory dir : picLst.get(0).getMetadata().getDirectories()) {
//			for(Tag tag : dir.getTags()) {
//				System.out.println(tag);
//			}
//		}
	}
	
	public static List<Photo> loadPhotos(File dir) {
		// declare what variables we are going to define later
		Photo pic;
		File[] lst;
		ArrayList<Photo> picLst;
		
		// check if argument is an existing directory
		if(!dir.exists() || !dir.isDirectory()) { // if file doesn't exist or isn't a directory
			System.err.println("Argument provided is not an exisiting directory!");
			// return an empty list, since there clearly aren't any photos here
			return new ArrayList<>();
		}
		
		// list all of the files in the directory
		lst = dir.listFiles(); // assign list of files to array called lst
		picLst = new ArrayList<Photo>(); // create a default arraylist for photos
		for(File f : lst) { // for each File, f, in lst:
			try {
				if(Photo.isPhoto(f)) { // if file is a photo
					pic = new Photo(f); // make a new photo object
					picLst.add(pic); // and add it to the array list
//					System.out.println(pic.getDate());
				}
			} catch(IOException e) {
				System.err.println("Failed to read image!\n" + e);
			} catch (ImageProcessingException e) {
				System.err.println("Error reading image metadata!\n" + e);
			}
		}
		return picLst;
	}

}

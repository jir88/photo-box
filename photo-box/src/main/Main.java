/**
 * 
 */
package main;

import java.io.File;

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
		
		System.out.println("Hello World");
		if(args.length == 0) {
			System.out.println("no arguments provided");
		}
		else if(!args[0].equals("import")) {
			System.out.println("Unexpected Arugment: " + args[0]);
		}
		else {
			System.out.println("everything is fine");
		}
		
		//creates a new object of type File and assigns it to variable loc
		loc = new File("/home/john/test.jpg");
		pic = new Photo(loc);
		
		System.out.println(pic.getLocation());
	}

}

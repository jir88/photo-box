/**
 * 
 */
package main;

/**
 * @author john
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	}

}

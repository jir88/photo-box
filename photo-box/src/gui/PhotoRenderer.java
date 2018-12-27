package gui;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A 'stamp' component for drawing photos with widgets and a pretty border.
 * @author john
 *
 */
public class PhotoRenderer extends JLabel {
	private static final long serialVersionUID = 461861465874963320L;
	
	private ImageIcon myPhotoIcon;

	/**
	 * Construct a new photo rendering stamp.
	 */
	public PhotoRenderer() {
		this.setOpaque(true);
		// create a pretty border		
		this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		// create an icon to display the photo with
		myPhotoIcon = new ImageIcon();
		this.setIcon(myPhotoIcon);
	}
	
	/**
	 * Return a component that has been configured to display a particular image.
	 * @param img the image to display
	 * @param isSelected whether this item is selected
	 * @param hasFocus whether this item also has the keyboard focus
	 * @return the component
	 */
	public Component getPhotoRendererComponent(BufferedImage img,
			boolean isSelected, boolean hasFocus) {
		// update the icon
		myPhotoIcon.setImage(img);
		
		return this;
	}
}

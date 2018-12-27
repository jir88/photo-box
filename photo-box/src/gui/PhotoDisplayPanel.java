package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import main.Photo;

public class PhotoDisplayPanel extends JComponent implements Scrollable{
	private static final long serialVersionUID = -6550963745349019127L;
	
	public static final Dimension DEFAULT_THUMB_SIZE = new Dimension(256, 128);
	public static final int DEFAULT_COLUMN_SPACING = 3;
	
	/**
	 * Component used as a 'stamp' to draw each image.
	 */
	private PhotoRenderer myStamp;
	private List<Photo> myPhotos;
	private Dimension myThumbSize;
	private int myCols;
	private int myColSpacing;

	/**
	 * Create a photo display panel with no images displayed.
	 */
	public PhotoDisplayPanel() {
		// start with an empty list of photos
		this(new ArrayList<Photo>());
	}
	
	/**
	 * Create a photo display panel with some images to display.
	 * @param pics a list of images to display
	 */
	public PhotoDisplayPanel(List<Photo> pics) {
		myPhotos = pics;
		// default thumbnail size
		myThumbSize = DEFAULT_THUMB_SIZE;
		// default number of columns
		myCols = 3;
		// default spacing between columns
		myColSpacing = DEFAULT_COLUMN_SPACING;
		
		this.setDoubleBuffered(false);
		this.setOpaque(true);
		
		// set up the 'stamp' component
		myStamp = new PhotoRenderer();

		// don't want a layout manager here...
		this.setLayout(null);
		// try to force a minimum size
		this.setMinimumSize(myThumbSize);
		this.setSize(myThumbSize);

		// lay out the panel for the first time
		this.updateLayout();
		
		// listen for when this panel is added to a container
		AncestorListener al = new AncestorListener() {
			
			@Override
			public void ancestorRemoved(AncestorEvent event) {}
			
			@Override
			public void ancestorMoved(AncestorEvent event) {}
			
			@Override
			public void ancestorAdded(AncestorEvent event) {
				final Container parent;
				
				// get the newly-added parent/ancestor
				parent = PhotoDisplayPanel.this.getParent();
				System.out.println("Ancestor added: " + event);
				System.out.println("New ancestor:");
				System.out.println(parent);
				
				// if the parent is a JViewport, listen for resize events on it
				if(parent instanceof JViewport) {
					parent.addComponentListener(new ComponentListener() {
						
						@Override
						public void componentShown(ComponentEvent e) {}
						
						@Override
						public void componentResized(ComponentEvent e) {
							Dimension viewSize, ownSize;
							
							System.out.println("Viewport resized: " + e);
							// update width to temporarily match viewport
							viewSize = parent.getSize();
							ownSize = PhotoDisplayPanel.this.getSize();
							ownSize.width = viewSize.width;
							PhotoDisplayPanel.this.setSize(ownSize);
							PhotoDisplayPanel.this.updateLayout();
						}
						
						@Override
						public void componentMoved(ComponentEvent e) {}
						
						@Override
						public void componentHidden(ComponentEvent e) {}
					});
				}
				
				PhotoDisplayPanel.this.updateLayout();
			}
		};
		this.addAncestorListener(al);
	}
	
	/**
	 * Get the displayed thumbnail size in pixels.
	 * @return the size that thumbnails are rendered
	 */
	public Dimension getThumbSize() {
		return myThumbSize;
	}

	/**
	 * Set the size to paint thumbnails. Pictures of a different aspect ratio
	 * will be cropped to fit.
	 * @param thumbSize the new thumbnail size, in pixels
	 */
	public void setThumbSize(Dimension thumbSize) {
		myThumbSize = thumbSize;
	}
	
	/**
	 * Gets the on-screen width of one column of pictures.
	 * @return the width in pixels
	 */
	private int getColumnWidth() {
		int stampWidth;
		Insets stampInsets;
		
		stampInsets = myStamp.getInsets();
		stampWidth = (int) Math.ceil(myThumbSize.getWidth() + stampInsets.left + stampInsets.right);
		
		// column width is stamp width plus spacing between columns
		return stampWidth + myColSpacing;
	}

	/**
	 * Lay out the panel to display the current list of images.
	 */
	private void updateLayout() {
		double rowsNeeded;
		int stampWidth, stampHeight;
		Dimension size, newSize;
		Insets stampInsets;
		// TODO: implement spacing between pictures!
		// FIXME: set stamp's size properly!
		
		// find out how much extra space the stamp's border takes up
		stampInsets = myStamp.getInsets();
		stampWidth = this.getColumnWidth();
		stampHeight = (int) Math.ceil(myThumbSize.getHeight() + stampInsets.top + stampInsets.bottom);
		
		// get the current size of the panel so we know how much space we have
		size = this.getSize();
		if (size.width == 0 || size.height == 0) {
			return; // panel has no size, so we can't really lay out the photos
		}
		// figure out how many pictures we can shove on one row
		myCols = (int) Math.floor(size.getWidth()/stampWidth);
		// if we can't even put one picture, just draw part of an image anyway
		if(myCols < 1) {
			myCols = 1;
		}
		// calculate how many rows we need
		rowsNeeded = Math.ceil((double)(myPhotos.size()/myCols));
		// update preferred size
		newSize = new Dimension(this.getWidth(), 
				(int)(rowsNeeded*stampHeight));
		this.setSize(newSize);
		this.setPreferredSize(newSize);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2;
		Rectangle clip;
		Point picLoc;
		Insets stampInsets;
		ListIterator<Photo> picItr;
		int row, col;
		int topRow, bottomRow;
		int stampWidth, stampHeight;
		
		// call the JPanel paint method to paint the background, etc.
		super.paintComponent(g);
		
		// if we don't have any pictures, don't try to paint any
		if(myPhotos.isEmpty()) {
			return;
		}
		
		// make sure we have the layout updated
//		this.updateLayout();
		
		// now make a copy of Graphics so we don't mess it up
		g2 = (Graphics2D)g.create();
		
		// calculate the size of the stamp component
		stampInsets = myStamp.getInsets();
		stampWidth = this.getColumnWidth();
		stampHeight = (int) Math.ceil(myThumbSize.getHeight() + stampInsets.top + stampInsets.bottom);
		
		// figure out which rows are inside the area we need to paint
		clip = g2.getClipBounds();
		topRow = Math.floorDiv(clip.y, stampHeight);
		bottomRow = (int) Math.ceil((clip.y + clip.height)/stampHeight);
		
		// figure out which image will be painted in the top-left corner
		if(myCols*topRow > myPhotos.size()) {
			// we don't have any images to draw here, so return
			return;
		}
		picItr = myPhotos.listIterator(myCols*topRow);
		
		// set the location of the first photo
		picLoc = new Point(0, topRow*stampHeight);
		
		row = topRow;
		col = 0;
		// while there is another picture to paint and we've still got rows to paint
		while(picItr.hasNext() && row <= bottomRow) {
			// set location of picture
			picLoc.y = row*stampHeight;
			picLoc.x = col*stampWidth;
			// paint the picture
			this.paintPhotoThumb(g2, picItr.next(), picLoc, false);
			// move to the next location in the grid
			col++;
			// if we've hit the end of a row, wrap around
			if(col >= myCols) {
				col = 0;
				row++;
			}
		}
	}

	/**
	 * Paint a thumbnail image of a photo in the specified rectangle.
	 * @param pic the photo to render
	 * @param loc the location to render it
	 * @param isSelected whether the photo is selected
	 */
	private void paintPhotoThumb(Graphics2D g2, Photo pic, Point loc,
			boolean isSelected) {
		// TODO: need to render 'edited versions available' icon
		
		BufferedImage thumb;
		Component c;
		
		int x = loc.x;
		int y = loc.y;
		int w = this.getThumbSize().width;
		int h = this.getThumbSize().height;
		
		try {
			// get a properly-sized thumbnail
			thumb = pic.getThumbnail(myThumbSize);
			// put it in the stamp
			c = myStamp.getPhotoRendererComponent(thumb, isSelected, 
					this.isFocusOwner());
	        
			// paint the stamp
			
	        // put the stamp in the right spot
	        c.setBounds(x, y, w, h);

	        boolean wasDoubleBuffered = false;
	        if ((c instanceof JComponent) && ((JComponent)c).isDoubleBuffered()) {
	            wasDoubleBuffered = true;
	            ((JComponent)c).setDoubleBuffered(false);
	        }

	        Graphics cg = g2.create(x, y, w, h);
	        try {
	            c.paint(cg);
	        }
	        finally {
	            cg.dispose();
	        }

	        if (wasDoubleBuffered && (c instanceof JComponent)) {
	            ((JComponent)c).setDoubleBuffered(true);
	        }

	        c.setBounds(-w, -h, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return; // TODO: draw a 'missing image' icon here!
		}
	}
	
	// ------- Methods to make this panel behave better in a scroll pane -------

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// we don't actually care what size the viewport is, so just return preferred size
		return this.getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		int nextRow, stampWidth, stampHeight;
		Insets stampInsets;
		
		// calculate the size of the stamp component
		stampInsets = myStamp.getInsets();
		stampWidth = this.getColumnWidth();
		stampHeight = (int) Math.ceil(myThumbSize.getHeight() + stampInsets.top + stampInsets.bottom);
		
		// if we're scrolling up or down
		if(orientation == SwingConstants.VERTICAL) {
			// if scrolling down
			if(direction > 0) {
				// figure out how far the top of the viewport is from the top of the next row
				// what row are we aiming for?
				nextRow = (visibleRect.y)/stampHeight + 1;
				return nextRow*stampHeight - visibleRect.y;
			} else { // scrolling up
				// what is the next row up?
				nextRow = (visibleRect.y)/stampHeight - 1;
				// how far do we have to move to get there?
				return visibleRect.y - nextRow*stampHeight;
			}
		} else { // we're scrolling left or right
			if(direction > 0) { // scrolling to the right
				// what column are we aiming for?
				nextRow = (visibleRect.x)/stampWidth + 1;
				// how far do we have to move to get there?
				return nextRow*stampWidth - visibleRect.x;
			} else { // scrolling to the left
				// what is the next column to the left?
				nextRow = (visibleRect.x)/stampWidth - 1;
				// how far do we have to move to get there?
				return visibleRect.x - nextRow*stampWidth;
			}
		}
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		int stampWidth, stampHeight;
		Insets stampInsets;
		
		// calculate the size of the stamp component
		stampInsets = myStamp.getInsets();
		stampWidth = this.getColumnWidth();
		stampHeight = (int) Math.ceil(myThumbSize.getHeight() + stampInsets.top + stampInsets.bottom);
		
		if(orientation == SwingConstants.VERTICAL) {
			return stampHeight;
		} else {
			return stampWidth;
		}
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

}

package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Main;

import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -4428832150514821377L;
	private JPanel myContentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle("Photo Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 871, 494);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmImport = new JMenuItem("Import...");
		mntmImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mnFile.add(mntmImport);
		
		JMenuItem mntmNewAlbum = new JMenuItem("New Album...");
		mnFile.add(mntmNewAlbum);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmQuit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmEditPhoto = new JMenuItem("Edit Photo");
		mnEdit.add(mntmEditPhoto);
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences...");
		mnEdit.add(mntmPreferences);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
		myContentPane = new JPanel();
		myContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(myContentPane);
		myContentPane.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		myContentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnImport = new JButton("Import");
		toolBar.add(btnImport);
		
		JSplitPane treeSplitPane = new JSplitPane();
		myContentPane.add(treeSplitPane, BorderLayout.CENTER);
		
		JSplitPane photosSplitPane = new JSplitPane();
		treeSplitPane.setRightComponent(photosSplitPane);
		
		// put in the custom album display panel here
		// as an initial test, we'll just arbitrarily display some pictures here
		File picDir = new File("/home/john/Documents/miscellaneous/art");
		PhotoDisplayPanel photoDispPanel = new PhotoDisplayPanel(Main.loadPhotos(picDir));
		
		JScrollPane photoScrollPane = new JScrollPane();
		photoScrollPane.setViewportView(photoDispPanel);
		photosSplitPane.setLeftComponent(photoScrollPane);
		
		JScrollPane scrollPane = new JScrollPane();
		photosSplitPane.setRightComponent(scrollPane);
		
		JPanel metadataPanel = new JPanel();
		scrollPane.setViewportView(metadataPanel);
		
		JLabel lblFileName = new JLabel("File Name:");
		
		JLabel lblCreated = new JLabel("Created: ");
		
		JSeparator separator = new JSeparator();
		
		JLabel lblComments = new JLabel("Comments:");
		
		JTextArea txtrInsertCommentHere = new JTextArea();
		txtrInsertCommentHere.setText("Insert comment here.");
		GroupLayout gl_metadataPanel = new GroupLayout(metadataPanel);
		gl_metadataPanel.setHorizontalGroup(
			gl_metadataPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_metadataPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_metadataPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_metadataPanel.createSequentialGroup()
							.addGroup(gl_metadataPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblFileName)
								.addComponent(lblCreated)
								.addGroup(Alignment.TRAILING, gl_metadataPanel.createSequentialGroup()
									.addGroup(gl_metadataPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(txtrInsertCommentHere, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
										.addComponent(separator, GroupLayout.PREFERRED_SIZE, 84, Short.MAX_VALUE))
									.addGap(73)))
							.addGap(203))
						.addGroup(gl_metadataPanel.createSequentialGroup()
							.addComponent(lblComments)
							.addContainerGap(342, Short.MAX_VALUE))))
		);
		gl_metadataPanel.setVerticalGroup(
			gl_metadataPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_metadataPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblFileName)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblCreated)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblComments)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtrInsertCommentHere)
					.addGap(166))
		);
		metadataPanel.setLayout(gl_metadataPanel);
		
		JTree photoOrgTree = new JTree();
		treeSplitPane.setLeftComponent(photoOrgTree);
	}
}

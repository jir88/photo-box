package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
		setBounds(100, 100, 519, 337);
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

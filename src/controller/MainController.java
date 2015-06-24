package controller;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainController extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
	JButton openButton, openButton1, Execute;
	JTextArea log;
	JTextField iOSProxy;
	JLabel inputLabel, outputLabel,iosProxyLabel,screenLabel;
	JFileChooser fc;
	String output = null,proxyURL;
	private File inputFile = null;
	private File outputFile;
	ReadExcel excel = new ReadExcel();
	JRadioButton Off,Fail,Both;

	@SuppressWarnings("deprecation")
	public MainController() {
		super(new GridLayout(6, 2));
		log = new JTextArea(3, 18);
		//log.setMargin(new Insets(5, 5, 5, 5));
		//log.setBounds(0, 0, 24, 10);
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter ft = new FileNameExtensionFilter(
				"Microsoft Excel Files", "xls");
		fc.addChoosableFileFilter(ft);
		fc.setAcceptAllFileFilterUsed(true);
		inputLabel = new JLabel("Input Path");
		outputLabel = new JLabel("Report Path");
		iosProxyLabel = new JLabel("iOS proxy");
		openButton = new JButton("Browse");
		openButton.addActionListener(this);
		openButton1 = new JButton("Browse");
		openButton1.addActionListener(this);
		iOSProxy =new JTextField();
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			iOSProxy.disable();
		}
		iOSProxy.setColumns(20);		
		iOSProxy.addActionListener(this);
		screenLabel = new JLabel("Take Screenshots:");
		Off = new JRadioButton("Off");
		Off.addActionListener(this);
		Fail = new JRadioButton("Only Failed ");
		Fail.addActionListener(this);
		Both = new JRadioButton("All");
		Both.addActionListener(this);
		
		Execute = new JButton("Execute");
		
		Execute.addActionListener(this);

		JPanel buttonPanel = new JPanel(); // use FlowLayout
		JPanel buttonPanel1 = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		JPanel buttonPanel3 = new JPanel();
		JPanel screenshotPanel = new JPanel();
		screenshotPanel.setSize(50, 50);
		buttonPanel.add(inputLabel);
		buttonPanel.add(openButton);
		buttonPanel1.add(outputLabel);
		buttonPanel1.add(openButton1);
		buttonPanel2.add(iosProxyLabel);
		buttonPanel2.add(iOSProxy);
		screenshotPanel.add(screenLabel);
		screenshotPanel.add(Both);
		screenshotPanel.add(Fail);		
		screenshotPanel.add(Off);
		buttonPanel3.add(Execute);
		add(buttonPanel);
		add(buttonPanel1);
		add(buttonPanel2);
		add(screenshotPanel);
		add(buttonPanel3);
		add(logScrollPane);

	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showOpenDialog(MainController.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				inputFile = fc.getSelectedFile();
				String extn = getExtension(inputFile);
				if (extn.equalsIgnoreCase("xls")) {
					excel.setInputFile(inputFile.toString());
					log.append("Opening: " + inputFile.getName() + "."
							+ newline);
				} else {
					JOptionPane.showMessageDialog(this,
							"Choose Excel Files only");
				}
			} else {
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		} else if (e.getSource() == openButton1) {
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(MainController.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				outputFile = fc.getSelectedFile();
				try {
					output = outputFile.getCanonicalPath().toString();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println(output);
				excel.setoutputFile(output);
				log.append("Saving: " + outputFile.getName() + "." + newline);
			} else {
				log.append("Save command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}else if(e.getSource()== iOSProxy){
			try {
				proxyURL=iOSProxy.getText();
				System.out.println(iOSProxy.getText());
				excel.setiOSProxy(proxyURL);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else if(e.getSource() == Off){
			Fail.setSelected(false);
			Both.setSelected(false);
			excel.setScreenFlag("Off");
			
		}else if(e.getSource() == Fail){
			Off.setSelected(false);
			Both.setSelected(false);
			excel.setScreenFlag("Fail");
		}else if(e.getSource() == Both){
			Off.setSelected(false);
			Fail.setSelected(false);
			excel.setScreenFlag("Both");
		}else if (e.getSource() == Execute) {
			try {
				if(inputFile==null){
					JOptionPane.showMessageDialog(this, "Select Input Excel File");
				}else if (output== null) {
					JOptionPane.showMessageDialog(this, "Select Output Path");
				} else if(excel.getScreenFlag()== null){
					JOptionPane.showMessageDialog(this, "Select any screenshot option");
				}else {					
					excel.mainControll(excel);
					JOptionPane.showMessageDialog(this,
							"Execution completed successfully");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void createAndShowGUI() {
		File dir = new File(".");
		String path = null;
		JFrame frame = new JFrame("eCompat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setIconImage(image);
		frame.setResizable(false);
		frame.setBounds(200, 200, 500, 500);
		// frame.setSize(15000, 5000);
		frame.add(new MainController());
		frame.pack();
		String OS = System.getProperty("os.name").toLowerCase();
		try{
			if (OS.indexOf("win") >= 0)
				path = dir.getCanonicalPath() + "\\lib\\tataicon.GIF";
			else if (OS.indexOf("mac") >= 0)
				path = dir.getCanonicalPath() + "/tataicon.GIF";
		}catch(Exception e){
			System.out.println("Tata Logo is Not Available");		
		}		
		ImageIcon img = new ImageIcon(path);
		frame.setIconImage(img.getImage());
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.getDefaults();
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());					
					UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
					// UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				createAndShowGUI();
			}
		});
	}
}
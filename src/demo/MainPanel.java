package demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class MainPanel extends JPanel {
	private static final long serialVersionUID = 8920997326735022832L;
	private JTextArea processText = new JTextArea();
	private JScrollPane processScrollPane = new JScrollPane(processText,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JTextArea resultText = new JTextArea();
	private JScrollPane resultScrollPane = new JScrollPane(resultText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	public MainPanel(String processing,String result) {
		this.setLayout(new GridLayout(2,1));
		processScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "处理过程"));
		resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "处理结果"));
		this.add(processScrollPane);
		this.add(resultScrollPane);
		processText.setEditable(false);
		resultText.setEditable(false);
		Font font = new Font(Font.SERIF, 0, 16);
		processText.setFont(font);
		resultText.setFont(font);
		processText.setText(processing);
		resultText.setText(result);
	}
}

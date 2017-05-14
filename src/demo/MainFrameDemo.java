package demo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import decision.DecisionTree;


public class MainFrameDemo {
	public static void main(String[] args) {
	ArrayList<String []>  array= new ArrayList<String[]> ();
	FileReader fileReader;
	BufferedReader bufferReader;
	String temps;
	int countDataIndex=0;//��������
	int countLength=0;//�����к�
	try {
		fileReader = new FileReader("data.txt");
		bufferReader= new BufferedReader(fileReader);
		while((temps=bufferReader.readLine())!=null){
			String[] tempArray=temps.split(",|��");
			if(countLength==0){
				countLength++;
				countDataIndex=tempArray.length;
				array.add(tempArray);
			}else if(tempArray.length==countDataIndex){
				countLength++;
				array.add(tempArray);
			}else{
				countLength++;
				JOptionPane.showMessageDialog(null, "<html><font color='RED'>���棺</font><br><font color='GREEN'>��"+countLength+"�е�������©ȱ���벹ȫ�����ٳ��ԣ�</font>");
				System.exit(-1);
			}
		}
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
		DecisionTree decisionTree = new DecisionTree();
		decisionTree.create(array.toArray(), 4);
		String processing=decisionTree.getProcess_buffer().toString();
		String result =decisionTree.getResult_buffer().toString(); 
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
		MainPanel panel=new MainPanel(processing,result);
		JFrame frame = new JFrame("�����ھ�C4.5�㷨��ʾϵͳ");
		frame.setSize(screen.width, screen.height);
		frame.add(panel);
		frame.repaint();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TreePainter treeframe = new TreePainter(decisionTree.getVertexs(),decisionTree.getEdges());
		treeframe.setBounds((screen.width-600)/2, (screen.height-500)/2, 600, 500);
		treeframe.setVisible(true);
		treeframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

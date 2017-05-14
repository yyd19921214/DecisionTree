package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import decision.DecisionTree;


public class ConsoleDemo {
	public static void main(String[] args) {
		ArrayList<String []>  array= new ArrayList<String[]> ();//��������ÿһ�е�����
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
				if(countLength==0){//����ǵ�һ�м���ͷ�У���ô�����ɴ˵õ����ݵ�����
					countLength++;
					countDataIndex=tempArray.length;
					array.add(tempArray);
				}else if(tempArray.length==countDataIndex){
					countLength++;
					array.add(tempArray);
				}else{
					countLength++;
					System.err.println("���棺\n��"+countLength+"�е�������©ȱ���벹ȫ�����ٳ��ԣ�");
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
		System.out.println("--------------------C4.5�Q�ߘ�--------------------");
		System.out.println(decisionTree.getResult_buffer().toString());
		System.out.println("----------------------�����^��----------------------");
		System.out.println(decisionTree.getProcess_buffer().toString());
	}
}

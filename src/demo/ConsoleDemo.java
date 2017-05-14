package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import decision.DecisionTree;


public class ConsoleDemo {
	public static void main(String[] args) {
		ArrayList<String []>  array= new ArrayList<String[]> ();//用来保存每一行的数据
		FileReader fileReader;
		BufferedReader bufferReader;
		String temps;
		int countDataIndex=0;//计算列数
		int countLength=0;//计算行号
		try {
			fileReader = new FileReader("data.txt");
			bufferReader= new BufferedReader(fileReader);
			while((temps=bufferReader.readLine())!=null){
				String[] tempArray=temps.split(",|，");
				if(countLength==0){//如果是第一行即表头行，那么可以由此得到数据的列数
					countLength++;
					countDataIndex=tempArray.length;
					array.add(tempArray);
				}else if(tempArray.length==countDataIndex){
					countLength++;
					array.add(tempArray);
				}else{
					countLength++;
					System.err.println("警告：\n第"+countLength+"行的数据有漏缺，请补全数据再尝试！");
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
		System.out.println("--------------------C4.5決策樹--------------------");
		System.out.println(decisionTree.getResult_buffer().toString());
		System.out.println("----------------------執行過程----------------------");
		System.out.println(decisionTree.getProcess_buffer().toString());
	}
}

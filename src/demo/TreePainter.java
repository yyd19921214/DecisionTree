package demo;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import pojo.Edge;
import pojo.TreeVertex;
import pojo.Vertex;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class TreePainter extends JFrame
{

	private static final long serialVersionUID = -2707712944901661771L;
	private static ArrayList<TreeVertex> treevertexs = new ArrayList<TreeVertex>();
	public TreePainter(ArrayList<Vertex> vertexs, ArrayList<Edge> edges) {
		super("C4.5������");
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		Collections.reverse(vertexs);
		graph.getModel().beginUpdate();
		for(int i=0;i<vertexs.size();i++){//�O��ÿ�����Ķ��Ӹ���
			Vertex v= vertexs.get(i);
			v.setSubcount(countSubBySameParentName(vertexs,v.getValue()));
			System.out.println(v);
		}
		try
		{
			for(int i=0;i<vertexs.size();i++){
				Vertex v= vertexs.get(i);
				Object ov;
				if(v.getParent()==null){//��ǰ����Ǹ��ڵ�
					ov = graph.insertVertex(parent, null,v.getValue(),250 , 50, 80,30);//ֱ�ӻ��ڶ�������λ��
				}
				else{
					Vertex parentV=findParentByValue(vertexs,v.getParent());//���Ǹ��ڵ��Ҫ�ҵ����ĸ��ף��Եõ������ж��ٸ����
					int currentRowCount = parentV.getSubcount();
//					if(currentRowCount>=0){//˵����ͬһ�еĽ��
						ov = graph.insertVertex(parent, null,v.getValue(),currentRowCount*100 , 50*(i+currentRowCount+1), 80,30);
//					}
//					else{
//						ov = graph.insertVertex(parent, null,v.getValue(),currentRowCount*100 , 50*((i+3)%5), 80,30);
//					}
					parentV.setSubcount(--currentRowCount);//���¸����δ����Ķ��Ӹ���
				}
				TreeVertex tv = new TreeVertex(v.getValue(),ov);
				treevertexs.add(tv);
			}
			for(int i=0;i<edges.size();i++){
				Edge edge = edges.get(i);
				graph.insertEdge(parent, null, edge.getValue(), findVertexByValue(edge.getSource()),findVertexByValue(edge.getTarget()));
				
			}
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}
	/**
	 * �������
	 * @param value
	 * @return
	 */
	private Object findVertexByValue(String value){
		for(int i=0;i<treevertexs.size();i++)//�õ�source��target
		{
			String temp=treevertexs.get(i).getValue();
			if(temp.equals(value)){
				Object v=treevertexs.get(i).getVertex();
				if(countVertexByValue(temp)>1)//������������ͬ���ƵĽ��
				{
					treevertexs.remove(i);//ȥ���ý��
				}
				return v;
			}
		}
		return null;
	}
	/**
	 * ���Ҹ���
	 * @param value
	 * @return
	 */
	private Vertex findParentByValue(ArrayList<Vertex> vertexs,String parent){
		for(int i=0;i<vertexs.size();i++)
		{
			Vertex temp=vertexs.get(i);
			if(temp.getValue().equals(parent)){
				return temp;
			}
		}
		return null;
	}
	/**
	 * ����ָ�����Ƶ���������
	 * @param value
	 * @return count
	 */
	private int countVertexByValue(String value){
		int count =0;
		for(int i=0;i<treevertexs.size();i++)
		{
			if(treevertexs.get(i).getValue().equals(value)){
				count++;
			}
		}
		return count;
	}
	/**
	 * ������ͬ���H�Ľ�����
	 * @param parent
	 * @return count
	 */
	private int countSubBySameParentName(ArrayList<Vertex> vertexs,String parent){
		int count =0;
		for(int i=0;i<vertexs.size();i++)
		{
			Vertex temp=vertexs.get(i);
			if(temp.getParent()!=null&&temp.getParent().equals(parent)){
				count++;
			}
		}
		return count;
	}
}

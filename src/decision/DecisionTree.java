package decision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import pojo.Edge;
import pojo.Vertex;


public class DecisionTree {

	/**
	 * �����
	 */
	private DecisionTreeNode root;

	/**
	 * visableArray
	 */
	private boolean[] visable;

	private static final int NOT_FOUND = -1;

	/**
	 * ��ʼ����ʵ�������У���DATA_START_LINE��ʼ�Ĳ������ݣ�֮ǰ������������
	 */
	private static final int DATA_START_LINE = 1;

	/**
	 * ѵ����
	 */
	private Object[] trainingArray;
	/**
	 * ������������
	 */
	private String[] columnHeaderArray;
	/**
	 * ��ǰ���ɵľ������Ľ�����
	 */
	private int countNode=0;
	/**
	 * ҪԤ��������е�����ֵ
	 */
	private int preNodeIndex;
	/**
	 * ��ǵ�ǰ������Ϊ0������ֵ
	 */
	private String trueattr; 
	/**
	 * �����Ϣ
	 */
	private ArrayList<Vertex> vertexs = new ArrayList<Vertex>();
	/**
	 * @return �����Ϣ
	 */
	public ArrayList<Vertex> getVertexs() {
		return vertexs;
	}
	/**
	 * ����Ϣ
	 */
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	/**
	 * @return ����Ϣ
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	/**
	 * ����������������
	 */
	private StringBuffer process_buffer = new StringBuffer();

	/**
	 * 
	 * @return process_buffer ����������������
	 */
	public StringBuffer getProcess_buffer() {
		return process_buffer;
	}

	/**
	 * �����������
	 */
	private StringBuffer result_buffer = new StringBuffer();

	/**
	 * 
	 * @return process_buffer �����������
	 */
	public StringBuffer getResult_buffer() {
		return result_buffer;
	}

	/**
	 * ��ʼ��C4.5���������ݣ�����ѵ����
	 * 
	 * @param array
	 *            �������������� ����
	 * @param index
	 *            Ԥ��ֵ������ֵ���кţ���0��ʼ����������һ�м���Ϊindex=0
	 */
	public void create(Object[] array, int index) {
		this.trainingArray = Arrays.copyOfRange(array, DATA_START_LINE,
				array.length);// ����ǰ���ݻ���Ϊѵ������ע����ʱ�����ݺ�������Ҫ���ֿ������������ڵ�һ��
		init(array, index);
		createDecisionTree(this.trainingArray);// ע�⣬�����trainArrayֻ�����ݲ��֣��Ѿ��������������ݷ���
		printDecisionTree(root);
	}

	/**
	 * ��ʼ��Ԥ������������ֵ�������������ϣ���Ԥ��������Ϊvisable״̬�������ڼ���MaxGain��A��ʱ�����Լ����������
	 * 
	 * @param dataArray
	 *            ��������
	 * @param index
	 *            Ԥ��ֵ������
	 */
	public void init(Object[] dataArray, int index) {
		this.preNodeIndex = index;
		this.columnHeaderArray = (String[]) dataArray[0];
		visable = new boolean[((String[]) dataArray[0]).length];
		/**
		 * ����Ԥ��������Ϊvisable״̬Ϊtrue,��������Ϊfalse
		 */
		for (int i = 0; i < visable.length; i++) {
			if (i == index) {
				visable[i] = true;
			} else {
				visable[i] = false;
			}
		}
	}

	/**
	 * @param array
	 */
	public void createDecisionTree(Object[] array) {
		Object[] maxgain = getMaxGainRatio(array,"ALL");//��ȡ���������������
		if (root == null) {
			root = new DecisionTreeNode();
			/*
			 * ����һ��Ҫ��ʾ��˵�����ڵ�ĸ��ڵ�Ϊnull����Ϊ���������ʱ��Ҫʹ���Ƿ�Ϊnullֵ���ж��Ƿ�Ϊ���ڵ�
			 */
			root.parentNode = null;
			root.parentArrtibute = null;
			root.arrtibutesArray = getArrtibutesArray(((Integer) maxgain[1])
					.intValue());
			root.nodeName = getColumnHeaderNameByIndex(((Integer) maxgain[1])
					.intValue());
			root.childNodesArray = new DecisionTreeNode[root.arrtibutesArray.length];
			insertDecisionTree(array, root);
		}
	}

	/**
	 * @param array
	 *            ѵ����
	 * @return Object[] ���ص�ǰ��MaxGain(S,A)
	 */
	public Object[] getMaxGainRatio(Object[] array,String processingData) {
		Object[] result = new Object[2];
		double gain = 0;//��ʼֵΪ0
		int index = -1;

		for (int i = 0; i < visable.length; i++) {
			if (!visable[i]) {// �����Ԥ��������
				// TODO �����ǽ�ID3�㷨ת��Ϊ C4.5�㷨�Ĺؼ����裬������Ϣ������gainRatio
				double value = gainRatio(array, i, this.preNodeIndex,processingData);
				if (gain < value) {
					gain = value;
					index = i;//��ȡ��������ʻ��ֵ����Ե��±�
				}
			}
		}
		result[0] = gain;
		result[1] = index;
		if (index >= 0) {
			process_buffer.append("����õ�MaxGainRatio:" + gain + ",���ɵ�"+(++countNode)+"�����"+ this.columnHeaderArray[index]+"\n");
			process_buffer.append("*************************************************���ָ���***************************************\n");
		}else{
			process_buffer.append("����õ�MaxGainRatio:" + gain + ",������"+processingData+"Ϊ��֧�ĵ�"+(++countNode)+"�����"+this.columnHeaderArray[preNodeIndex]+":"+trueattr+"\n");
			process_buffer.append("*************************************************���ָ���***************************************\n");
		}
		
		// TODO �������ܽ���Ԥ�������������Ե�GainRatio��С��0
		if (index != -1) {
			visable[index] = true;//��ʱ�������Ѿ����л��֣��Ժ��ٲ��뻮��
		}
		return result;
	}

	/**
	 * @param array
	 * @param parentNode
	 */
	public void insertDecisionTree(Object[] array, DecisionTreeNode parentNode) {
		String[] arrtibutes = parentNode.arrtibutesArray;
		for (int i = 0; i < arrtibutes.length; i++) {
			Object[] pickArray = pickUpAndCreateSubArray(array, arrtibutes[i],
					getColumnHeaderIndexByName(parentNode.nodeName));
			//ע�����ڴ����Data��������һ�ű�����ͨ����һ�β����Ľ�㻮��Ϊ ��ͬ���� ����±�
			Object[] info = getMaxGainRatio(pickArray,arrtibutes[i]);
			double gain = ((Double) info[0]).doubleValue();
			if (gain != 0) {
				int index = ((Integer) info[1]).intValue();
				DecisionTreeNode currentNode = new DecisionTreeNode();
				currentNode.parentNode = parentNode;
				currentNode.parentArrtibute = arrtibutes[i];
				currentNode.arrtibutesArray = getArrtibutesArray(index);
				currentNode.nodeName = getColumnHeaderNameByIndex(index);
				currentNode.childNodesArray = new DecisionTreeNode[currentNode.arrtibutesArray.length];
				parentNode.childNodesArray[i] = currentNode;
				insertDecisionTree(pickArray, currentNode);
			} else {
				DecisionTreeNode leafNode = new DecisionTreeNode();
				leafNode.parentNode = parentNode;
				leafNode.parentArrtibute = arrtibutes[i];
				leafNode.arrtibutesArray = new String[0];
				leafNode.nodeName = getLeafNodeName(pickArray, this.preNodeIndex);
				leafNode.childNodesArray = new DecisionTreeNode[0];
				parentNode.childNodesArray[i] = leafNode;
			}
		}
	}
	private Vertex findVertexBySameParent(String parent){
		for(int i=0;i<vertexs.size();i++)//�õ�source��target
		{
			Vertex temp=vertexs.get(i);
			if(temp.getParent()!=null&&temp.getParent().equals(parent)){
				return temp;
			}
		}
		return null;
	}
	/**
	 * @param node
	 */
	public void printDecisionTree(DecisionTreeNode node) {
		if (node.parentNode != null) {
			result_buffer.append("������㣺" + node.nodeName + "[��ǰ���ĸ����Ϊ��"
					+ node.parentNode.nodeName + "]\n");
			Vertex v = new Vertex();
			v.setValue(node.nodeName);
			Vertex sameParentV = findVertexBySameParent(node.parentNode.nodeName);
			v.setParent(node.parentNode.nodeName);
			vertexs.add(vertexs.indexOf(sameParentV)+1,v);
		} else {
			result_buffer.append("����㣺" + node.nodeName + "\n");
			Vertex v = new Vertex();
			v.setValue(node.nodeName);
			vertexs.add(v);
		}
		DecisionTreeNode[] childs = node.childNodesArray;
		for (int i = 0; i < childs.length; i++) {
			if (childs[i] != null) {
				result_buffer.append(node.nodeName + "�ķ�֧"
						+ childs[i].parentArrtibute + "\n");
				printDecisionTree(childs[i]);
				Edge e = new Edge();
				e.setValue(childs[i].parentArrtibute);
				e.setSource(node.nodeName);
				e.setTarget(childs[i].nodeName);
				edges.add(e);
			}
		}
	}

	/**
	 * @param array
	 * @param arrtibute
	 * @param index
	 * @return Object[]
	 */
	public Object[] pickUpAndCreateSubArray(Object[] array, String arrtibute,
			int index) {
		List<String[]> list = new ArrayList<String[]>();
		for (int i = 0; i < array.length; i++) {
			String[] strs = (String[]) array[i];
			if (strs[index].equals(arrtibute)) {
				list.add(strs);
			}
		}
		return list.toArray();
	}

	/**
	 * gain(A)
	 * 
	 * @param array
	 *            ѵ����
	 * @param index
	 *            ��ǰ����������
	 * @return double ��Ϣ���� Gain(A)
	 */
	public double gain(Object[] array, int index, int nodeIndex,String processingData) {
		int[] counts = CountSameValueArrays(array, nodeIndex);
		String[] attr = getArrtibutesArray(nodeIndex);
		process_buffer.append("ѵ����S("+processingData+")��Ԥ�������в�ֵͬ�ĸ�����\n");
		for (int i = 0; i < counts.length; i++) {
			process_buffer.append(attr[i] + ":" + counts[i] + "\t");
			if(counts[i]>0){
				trueattr=attr[i];
			}
		}
		process_buffer.append("\n");
		process_buffer.append("����ѵ��������S("+processingData+")=sum(Si)=" + array.length+"\n");
		process_buffer.append("����pi=Si/S\n");
		for (int i = 0; i < counts.length; i++) {
			process_buffer.append("p" + i + "=" + counts[i] + "/" + array.length
					+ "\t");
		}
		process_buffer.append("\n");
		String[] arrtibutes = getArrtibutesArray(index);
		double gainValue = 0;
		double infoD = entropy_S(array, counts, true,processingData);
		double infoaD = entropyA_S(array, index, nodeIndex, arrtibutes);
		gainValue = infoD - infoaD;
		process_buffer.append("������Ϣ����ֵGain(A)=Gain("
				+ this.columnHeaderArray[index] + ")=Entropy(S)-Entropy(S,"
				+ this.columnHeaderArray[index] + ")=" + infoD + "-" + infoaD
				+ "=" + gainValue+"\n");
		process_buffer.append("================================================�ָ���===============================================\n");
		return gainValue;
	}

	/**
	 * @param array
	 *            ѵ����
	 * @param nodeIndex
	 *            Ԥ�����������
	 * @return ����ֵ��ͬ��Ԥ��ֵ��������
	 */
	public int[] CountSameValueArrays(Object[] array, int nodeIndex) {
		String[] arrti = getArrtibutesArray(nodeIndex);
		int[] counts = new int[arrti.length];
//		for (int i = 0; i < counts.length; i++) {
//			counts[i] = 0;
//		}
		for (int i = 0; i < array.length; i++) {
			String[] strs = (String[]) array[i];
			for (int j = 0; j < arrti.length; j++) {
				if (strs[nodeIndex].equals(arrti[j])) {
					counts[j]++;
				}
			}
		}
		return counts;
	}

	/**
	 * ��Ϣ������ gainRatio = Gain(A)/splitE(A)
	 * 
	 * @param array
	 *            ѵ����
	 * @param index
	 *            ��ǰ���������к�
	 * @param nodeIndex
	 *            Ԥ��������
	 * @return
	 */
	//gainRatio(array, i, this.preNodeIndex,processingData);
	public double gainRatio(Object[] array, int index, int nodeIndex,String processData) {
		// ���㵱ǰ�е���Ϣ����Gain(A)
		double gain = gain(array, index, nodeIndex,processData);
		int[] counts = CountSameValueArrays(array, index);
		double splitInfo = splitE_A(array, counts);
		double gainRatio = 0;
		if (splitInfo != 0) {
			gainRatio = gain / splitInfo;
			process_buffer.append("����" + this.columnHeaderArray[index]
					+ "�����е���Ϣ������gainRatio=Gain(A)/SplitE(A)=" + gain + " / "
					+ splitInfo + "=" + gainRatio+"\n");
			process_buffer.append("================================================�ָ���===============================================\n");
			return gainRatio;
		}
		return 0;
	}

	/**
	 * infoD = -E(pi*log2 pi)
	 * 
	 * @param array
	 * @param counts         //������ÿ��ֵ����Ŀ
	 * @return
	 */
	public double entropy_S(Object[] array, int[] counts, boolean entropyS,String processingData) {
		double infod = 0;
		double infoD = 0;
		String infoD_str = "";
		for (int i = 0; i < counts.length; i++) {
			infod = info(counts[i], array.length);// ��ǰ����ķ�����Ϣ
			process_buffer.append("���� p" + i + "*log2 p" + i + "= (" + counts[i]
					+ "/" + array.length + ")*log2 (" + counts[i] + "/"
					+ array.length + ")=" + infod+"\n");
			infoD += infod;
			if (infoD_str.equals("")) {// �����һ��
				infoD_str += "(" + infod + ")";
			} else {
				infoD_str += "+(" + infod + ")";
			}
		}
		if (entropyS) {
			process_buffer.append("����ѵ����S("+processingData+")����Ϣ�أ�Entropy(S)=Entropy(p1,p2...,pm)=-sum(pi*log2 pi)=-("
							+ infoD_str + ")=" + (-infoD)+"\n");
		} else {
			process_buffer.append("���������Ϣ��SplitE(A)=-sum(pi*log2 pi)=-("
					+ infoD_str + ")=" + (-infoD)+"\n");
		}
		process_buffer.append("================================================�ָ���===============================================\n");
		return (-infoD);
	}

	/**
	 * entropyA_S = sum(|Dj| / |D|) * info(Dj)
	 * 
	 * @param array
	 * @param index
	 * @param arrtibutes
	 * @return
	 */
	public double entropyA_S(Object[] array, int index, int nodeIndex,
			String[] arrtibutes) {
		double sv_total = 0;
		String infoDjStr = "";
		double infoDj = 0;
		for (int i = 0; i < arrtibutes.length; i++) {
			infoDj = infoDj(array, index, nodeIndex, arrtibutes[i],
					array.length);
			sv_total += infoDj;
			if (infoDjStr.equals("")) {// �����һ��
				infoDjStr += "(" + infoDj + ")";
			} else {
				infoDjStr += "+(" + infoDj + ")";
			}
		}
		process_buffer.append("��������" + this.columnHeaderArray[index]
				+ "����S�������Ӽ�����Ϣ�أ�Entropy(S," + this.columnHeaderArray[index]
				+ ")=sum((Si/S)*Entropy(Si))=" + infoDjStr + "=" + sv_total+"\n");
		process_buffer.append("================================================�ָ���===============================================\n");
		return sv_total;
	}

	/**
	 * splitE_A = -sum��i=1~k�� |Di|/|D|*log2(|Di|/|D|) E��i=1~k����ʾ������� i��1��k
	 * 
	 * @param array
	 * @param counts
	 * @return
	 */
	public double splitE_A(Object[] array, int[] counts) {
		return entropy_S(array, counts, false,"ALL");
	}

	/**
	 * ((|Dj| / |D|) * Info(Dj))
	 * 
	 * @param array
	 * @param index
	 * @param arrtibute
	 * @param allTotal
	 * @return double
	 */
	public double infoDj(Object[] array, int index, int nodeIndex,
			String arrtibute, int allTotal) {
		String[] arrtibutes = getArrtibutesArray(nodeIndex);
		int[] counts = new int[arrtibutes.length];
		for (int i = 0; i < counts.length; i++) {
			counts[i] = 0;
		}

		for (int i = 0; i < array.length; i++) {
			String[] strs = (String[]) array[i];
			if (strs[index].equals(arrtibute)) {
				for (int k = 0; k < arrtibutes.length; k++) {
					if (strs[nodeIndex].equals(arrtibutes[k])) {
						counts[k]++;
					}
				}
			}
		}
		for (int k = 0; k < arrtibutes.length; k++) {
			process_buffer.append("����������" + this.columnHeaderArray[index] + "����ֵ"
					+ arrtibute + "Ϊ[" + arrtibutes[k] + "]�ĸ���Ϊ��" + counts[k]+"\n");
		}
		int total = 0;
		double infoDj = 0;
		double infoDjt = 0;
		String infoDjStr = "";
		process_buffer.append("����������" + this.columnHeaderArray[index] + "����ֵΪ"
				+ arrtibute + "��������");
		for (int i = 0; i < counts.length; i++) {
			if (i == 0) {
				process_buffer.append(counts[i] + "");
			} else {
				process_buffer.append("+" + counts[i]);
			}
			total += counts[i];
		}
		process_buffer.append("=" + total+"\n");

		for (int i = 0; i < counts.length; i++) {
			infoDjt = info(counts[i], total);
			if (i == 0) {
				infoDjStr += "(" + infoDjt + ")";
			} else {
				infoDjStr += "+(" + infoDjt + ")";
			}
			infoDj += infoDjt;
		}
		process_buffer.append("����Entropy(Si)=Entropy(" + arrtibute + ")=-(");
		for (int i = 0; i < counts.length; i++) {
			if (i == 0) {
				process_buffer.append("(log2 " + counts[i] + "/" + total + ")");
			} else {
				process_buffer.append("+(log2 " + counts[i] + "/" + total + ")");
			}
		}
		process_buffer.append(")=-" + infoDjStr + "=" + (-infoDj)+"\n");
		double entropyA_S = DecisionTree.getPi(total, allTotal) * (-infoDj);
		process_buffer.append("����(Si/S)*Entropy(Si)=(" + total + "/" + allTotal
				+ ")*(" + (-infoDj) + ")=" + entropyA_S+"\n");
		process_buffer.append("================================================�ָ���===============================================\n");
		return entropyA_S;
	}

	/**
	 * @param index
	 *            ָ�� ���к�
	 * @return String[] ����ָ���кŵ�����ֵ����
	 */
	public String[] getArrtibutesArray(int index) {
		TreeSet<String> set = new TreeSet<String>(new Comparator<Object>() {
			@Override
			public int compare(Object obj1, Object obj2) {
				String str1 = (String) obj1;
				String str2 = (String) obj2;
				return str1.compareTo(str2);
			}

		});
		for (int i = 0; i < trainingArray.length; i++) {
			String[] strs = (String[]) trainingArray[i];
			set.add(strs[index]);
		}
		String[] result = new String[set.size()];
		return set.toArray(result);
	}

	/**
	 * 
	 * @param index
	 *            ָ���������к�
	 * @return String ����ָ���е�������
	 */
	public String getColumnHeaderNameByIndex(int index) {
		for (int i = 0; i < columnHeaderArray.length; i++) {
			if (i == index) {
				return columnHeaderArray[i];
			}
		}
		return null;
	}

	/**
	 * @param array
	 * @param nodeIndex
	 *            ָ���Ľ���
	 * @return String ����Ҷ�ӽڵ���
	 */
	public String getLeafNodeName(Object[] array, int nodeIndex) {
		if (array != null && array.length > 0) {
			String[] strs = (String[]) array[0];
			return strs[nodeIndex];
		}
		return null;
	}

	/**
	 * @param name
	 *            ָ������������
	 * @return int ����ָ�����������������ţ��кţ�
	 */
	public int getColumnHeaderIndexByName(String name) {
		for (int i = 0; i < columnHeaderArray.length; i++) {
			if (name.equals(columnHeaderArray[i])) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	/**
	 * ��Ϣ�� entropy: info(T)=(i=1...k)pi*log��2��pi
	 * 
	 * @param x
	 *            ��Siǰֵ
	 * @param total
	 *            S��ֵ
	 * @return double ���ص�ǰֵ�� ��Ϣ���� ��ǰ�����еļ��ϵ���Ϣ���ѵĺͼ�Ϊ
	 */
	public double info(int x, int total) {
		double info = 0;
		if (x == 0) {
			process_buffer.append(0 + "/" + total + "=0.0\n");
			return 0;
		}
		double x_pi = getPi(x, total);
		process_buffer.append(x + "/" + total + "=" + x_pi+"\n");
		info = (x_pi * logYBase2(x_pi));
		return info;
	}

	/**
	 * log2y
	 * 
	 * @param y
	 * @return double
	 */
	public static double logYBase2(double y) {
		double log2Y = Math.log(y) / Math.log(2);
		return log2Y;
	}

	/**
	 * pi=|C(i,d)|/|D|
	 * 
	 * @param x
	 *            ��Siǰֵ
	 * @param total
	 *            S��ֵ
	 * @return double ���ص�ǰֵ��ռ�����ı���
	 */
	public static double getPi(int x, int total) {
		double pi = x / (double) total;
		return pi; // �������ǿ��ת�������������Ϊ ����
	}
}
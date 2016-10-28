package property.tree;

import java.util.ArrayList;
import java.util.HashMap;

import entity.band.Band;

public class Tree<E> {

	
	private Node<E>	root;
	private Node<E>	presentNode;	// present node

	public Tree() {
		root = null;
		presentNode = null;
	}

	public Tree(E data) {

		root = new Node<E>(data);
		presentNode = root;

	}

	public void SetRoot(E data) {

		root = new Node<E>(data);
		presentNode = root;

	}

	public void SetPN(Node<E> n) {

		presentNode = n;

	}

	public Node<E> GetRoot() {

		return root;

	}

	public Node<E> GetPresnetNode() {

		return presentNode;

	}

	public void NodeChangeData(E data) {

		presentNode.SetData(data);

	}

	public void ParentInsertChild(E data) {

		Node<E> tmp;

		if (presentNode == null)

			presentNode.SetChild(new Node<E>(data));

		else {

			tmp = presentNode.GetChild();

			if (tmp == null) {

				presentNode.SetChild(new Node<E>(data));

				return;

			}

			while (true) {

				if (tmp.GetSibling() == null) {

					tmp.SetSibling(new Node<E>(data));

					break;

				}

				tmp = tmp.GetSibling();

			}

		}

	}

	public void ParentDeleteChild(E data) {

		Node<E> tmp = null;

		Node<E> pre = null;

		tmp = presentNode.GetChild();

		if (tmp == null) {

			System.out.println("data가 없습니다.");

			return;

		}

		if (tmp.GetData() == data) {

			presentNode.SetChild(tmp.GetSibling());

			return;

		}

		while (true) {

			pre = tmp;

			tmp = tmp.GetSibling();

			if (tmp == null)

				return;

			if (tmp.GetData() == data) {

				pre.SetSibling(tmp.GetSibling());

				break;

			}

		}

	}

	public void PrintChild() {

		Node<E> tmp = presentNode.GetChild();

		if (tmp == null)

			System.out.println("child가 없습니다.");

		else {

			while (true) {

				if (tmp == null)

					break;

				System.out.println(tmp.GetData());

				tmp = tmp.GetSibling();

			}

			System.out.println("\n");

		}

	}
	
	public HashMap<Integer,ArrayList<Band>> toHashMapOfInteger(){
		
		if(presentNode.GetData() instanceof Band)
			return getSubNodes((Node<Band>)presentNode);
	
		return new HashMap<>();
	}

	private HashMap<Integer,ArrayList<Band>> getSubNodes(Node<Band> node){
		
		Integer upperNodeId = node.GetData().getBandId();		
		HashMap<Integer,ArrayList<Band>> nodes = new HashMap<Integer, ArrayList<Band>>();
		ArrayList<Band> ary = new ArrayList<Band>();
		
		while(node.GetSibling()!=null){
			ary.add(node.GetData());
			node = node.GetSibling();
		}		
		
		while(node.GetChild()!=null){
			HashMap<Integer,ArrayList<Band>> subGroups = getSubNodes(node);
			for(Integer key : subGroups.keySet())
				nodes.put(key, subGroups.get(key));
		}
			
		
		
		nodes.put(upperNodeId, ary);
		
		return nodes;
	}
}

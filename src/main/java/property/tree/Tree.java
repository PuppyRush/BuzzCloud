package property.tree;

import java.util.ArrayList;
import java.util.HashMap;

import entity.band.Band;
import entity.band.Band.BundleBand;
import entity.interfaces.Entity;

public class Tree<E extends Entity> {

	
	private Node<E>	root;
	private Node<E>	presentNode;	// present node
	private ArrayList<E> datas;
	
	public Tree() {
		datas = new ArrayList<>();
		root = null;
		presentNode = null;
	}

	public Tree(E data) {

		root = new Node<E>(data);
		presentNode = root;
		datas = new ArrayList<E>();
		datas.add(data);

	}

	public void SetRoot(E data) {

		root = new Node<E>(data);
		presentNode = root;

	}

	public void SetPN(Node<E> n) {
		root.SetChild(n);
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

	public ArrayList<E> getDatas(){
		getDatasRecursirve(root);
		return datas;
	}
	
	private ArrayList<BundleBand> getDatasRecursirve(Node<E> upperNode){
				
		if(upperNode.GetChild() == null)
			return new ArrayList<>();
		
		ArrayList<Node<E>> siblingAry = new ArrayList<Node<E>>();		
		ArrayList<BundleBand> bundleBands = new ArrayList<>();		
		
		Node<E> nextNode = upperNode.GetChild();
		while(nextNode.GetSibling()!=null){
			datas.add( nextNode.GetData());
			siblingAry.add(nextNode);
			nextNode = nextNode.GetSibling();	
		}		
		siblingAry.add(nextNode);
		datas.add( nextNode.GetData());
		
		for(Node<E> siblingNode : siblingAry)
			bundleBands.addAll( getDatasRecursirve(siblingNode));
	
		return bundleBands;
		
	}

	
	public ArrayList<BundleBand> getSubRelationNodes(){
		ArrayList<BundleBand> bands = getSubNodesRecursive(root);
		return bands;
	}

	
	private ArrayList<BundleBand> getSubNodesRecursive(Node<E> upperNode){
		
		if(upperNode.GetChild() == null){
			return new ArrayList<>();
		}
		
		ArrayList<BundleBand> bundleBands = new ArrayList<>();		
		ArrayList<Node<E>> siblingAry = new ArrayList<Node<E>>();
	
		Node<E> nextNode = upperNode.GetChild();
		while(nextNode.GetSibling()!=null){
			bundleBands.add(new BundleBand( (Band)nextNode.GetData(),(Band)upperNode.GetData() ));
			siblingAry.add(nextNode);
			nextNode = nextNode.GetSibling();
		}	
		siblingAry.add(nextNode);
		bundleBands.add(new BundleBand( (Band)nextNode.GetData(),(Band)upperNode.GetData() ));
		
		if(siblingAry.size()>0){
			for(Node<E> siblingNode : siblingAry)
				bundleBands.addAll( getSubNodesRecursive(siblingNode));
		}
		
		return bundleBands;
	}
	

}

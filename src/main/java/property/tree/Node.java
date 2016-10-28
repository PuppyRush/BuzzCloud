package property.tree;


public class Node<E> {

	private E	data;
	private Node<E>	child;
	private Node<E>	sibling;

	public Node(E data) {

		this.data = data;

		child = null;

		sibling = null;

	}

	public void SetData(E data) {

		this.data = data;

	}

	public void SetChild(Node<E> child) {

		this.child = child;

	}

	public void SetSibling(Node<E> sibling) {

		this.sibling = sibling;

	}

	public E GetData() {

		return data;

	}

	public Node<E> GetChild() {

		return child;

	}

	public Node<E> GetSibling() {

		return sibling;

	}

};
package cufy.util;

import cufy.util.Node.Key;
import cufy.util.polygon.Digon;
import org.junit.jupiter.api.Test;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

public class NodesTest {
	@Test
	public void head_tail() {
		Node<String> x = new HashNode<>("X");
		Node<String> y = new HashNode<>("Y");
		Node<String> z = new HashNode<>("Z");

		x.putNode(Digon.END, y);
		y.putNode(Digon.END, z);

		assertSame(
				x,
				Nodes.head(Digon.END, y),
				"Wrong head"
		);
		assertSame(
				z,
				Nodes.tail(Digon.END, y),
				"Wrong tail"
		);

		z.putNode(Digon.END, x);

		assertSame(
				z,
				Nodes.head(Digon.END, y),
				"Wrong head; infinity flips the view"
		);
		assertSame(
				x,
				Nodes.tail(Digon.END, y),
				"Wrong tail; infinity flips the view"
		);
	}

	@Test
	public void sort() {
		Key key = Digon.END;
		Node<String> nodeA = new HashNode<>("A");
		Node<String> nodeB = new HashNode<>("B");
		Node<String> nodeC = new HashNode<>("C");
		Node<String> nodeD = new HashNode<>("D");
		Node<String> nodeE = new HashNode<>("E");
		Node<String> nodeF = new HashNode<>("F");
		Node<String> nodeG = new HashNode<>("G");

		Nodes.concat(key, nodeB, nodeD, nodeC, nodeG, nodeA, nodeF, nodeE, nodeB);

		assertTrue(
				Nodes.isInfinite(key, nodeD) /*any node must work*/,
				"Infinite concatenation has not worked"
		);

		Nodes.sort(key, nodeB);

		assertTrue(
				Nodes.isInfinite(key, nodeD) /*any node must work*/,
				"Sorting lost previously infinite behaviour"
		);

		Nodes.asDeque(key, nodeE)
			 .stream()
			 .map(Node::get)
			 .forEach(System.out::println);
	}

	//asDeque

	@Test
	public void asDeque_pollLast_single() {
		Node<String> node = new HashNode<>();
		Deque<Node<String>> deque = Nodes.asDeque(Digon.END, node);

		//single
		assertThrows(
				UnsupportedOperationException.class,
				deque::pollLast,
				"pollLast expected to not remove the sole node"
		);
	}

	@Test
	public void asDeque_pollLast_single_infinite() {
		Node<String> node = new HashNode<>();
		node.putNode(Digon.END, node);
		Deque<Node<String>> deque = Nodes.asDeque(Digon.END, node);

		//single
		assertThrows(
				UnsupportedOperationException.class,
				deque::pollLast,
				"pollLast expected to not remove the sole node"
		);
	}

	@Test
	public void asDeque_pollLast_multi() {
		Node<String> nodeA = new HashNode<>();
		Node<String> nodeB = new HashNode<>();
		Node<String> nodeC = new HashNode<>();
		Nodes.concat(Digon.END, nodeA, nodeB, nodeC);
		Deque<Node<String>> deque = Nodes.asDeque(Digon.END, nodeA);

		//multi
		assertSame(
				nodeC,
				deque.pollLast(),
				"pollLast expected to return the last " +
				"item when a multiple items is stored"
		);
		//multi => multi -1
		assertSame(
				2,
				deque.size(),
				"After pollLast on a multiple deque, " +
				"deque must have 1 less item"
		);
		assertSame(
				nodeB,
				deque.pollLast(),
				"pollLast expected to return the last " +
				"item when a multiple items is stored"
		);
		//multi => multi -1
		assertSame(
				1,
				deque.size(),
				"After pollLast on a multiple deque, " +
				"deque must have 1 less item"
		);
		//single
		assertThrows(
				UnsupportedOperationException.class,
				deque::pollLast,
				"pollLast expected to not remove the sole node"
		);
	}

	@Test
	public void asDeque_pollFirst_single() {
		Node<String> node = new HashNode<>();
		Deque<Node<String>> deque = Nodes.asDeque(Digon.END, node);

		//single
		assertThrows(
				UnsupportedOperationException.class,
				deque::pollFirst,
				"pollFirst expected to not remove the sole node"
		);
	}

	@Test
	public void asDeque_pollFirst_single_infinite() {
		Node<String> node = new HashNode<>();
		node.putNode(Digon.END, node);
		Deque<Node<String>> deque = Nodes.asDeque(Digon.END, node);

		//single
		assertThrows(
				UnsupportedOperationException.class,
				deque::pollFirst,
				"pollFirst expected to not remove the sole node"
		);
	}

	@Test
	public void asDeque_pollFirst_multi() {
		Node<String> nodeA = new HashNode<>();
		Node<String> nodeB = new HashNode<>();
		Node<String> nodeC = new HashNode<>();
		Nodes.concat(Digon.END, nodeA, nodeB, nodeC);
		Deque<Node<String>> deque = Nodes.asDeque(Digon.END, nodeC);

		//multi
		assertSame(
				nodeA,
				deque.pollFirst(),
				"pollFirst expected to return the last " +
				"item when a multiple items is stored"
		);
		//multi => multi -1
		assertSame(
				2,
				deque.size(),
				"After pollFirst on a multiple deque, " +
				"deque must have 1 less item"
		);
		assertSame(
				nodeB,
				deque.pollFirst(),
				"pollFirst expected to return the last " +
				"item when a multiple items is stored"
		);
		//multi => multi -1
		assertSame(
				1,
				deque.size(),
				"After pollFirst on a multiple deque, " +
				"deque must have 1 less item"
		);
		//single
		assertThrows(
				UnsupportedOperationException.class,
				deque::pollFirst,
				"pollFirst expected to not remove the sole node"
		);
	}

	//unmodifiable

	@Test
	public void unmodifiable_put() {
		Node<String> node = new HashNode<>();
		Node<String> uNode = Nodes.unmodifiableNode(node);

		assertThrows(
				UnsupportedOperationException.class,
				() -> {
					//noinspection ConstantConditions
					uNode.put(Digon.START, "Awesome");
				},
				"Unmodifiable node was successfully modified throw its `put` method"
		);
	}
}

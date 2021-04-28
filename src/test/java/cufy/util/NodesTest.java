package cufy.util;

import cufy.util.polygon.Digon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NodesTest {
	@Test
	public void unmodifiable0() {
		Node<String> node = new HashNode<>();
		Node<String> uNode = Nodes.unmodifiableNode(node);

		assertThrows(
				UnsupportedOperationException.class,
				() -> {
					//noinspection ConstantConditions
					uNode.put(Nodes.key("left"), "Awesome");
				},
				"Unmodifiable node was successfully modified throw its `put` method"
		);
	}

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

		assertThrows(
				IllegalArgumentException.class,
				() -> Nodes.head(Digon.END, y),
				"Headless node must have no head"
		);
		assertThrows(
				IllegalArgumentException.class,
				() -> Nodes.tail(Digon.END, y),
				"Tailless node must have no  tail"
		);
	}
}

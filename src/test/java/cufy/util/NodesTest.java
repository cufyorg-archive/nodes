package cufy.util;

import org.junit.jupiter.api.Test;

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
}

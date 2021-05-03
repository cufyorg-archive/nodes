package cufy.util;

import cufy.util.AbstractNode.SimpleLink;
import cufy.util.Node.Key;
import cufy.util.Node.Link;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void test0() {
		Node<String> north = new HashNode<>("N");
		Node<String> northEast = new HashNode<>("NE");
		Node<String> northWest = new HashNode<>("NW");

		Node<String> south = new HashNode<>("S");
		Node<String> southEast = new HashNode<>("SE");
		Node<String> southWest = new HashNode<>("SW");

		Node<String> east = new HashNode<>("E");
		Node<String> eastNorth = new HashNode<>("EN");
		Node<String> eastSouth = new HashNode<>("ES");

		Node<String> west = new HashNode<>("W");
		Node<String> westNorth = new HashNode<>("WN");
		Node<String> westSouth = new HashNode<>("WS");

		Node<String> center = new HashNode<>();

		//center
		center.putNode(Compass.NORTH, north);
		center.putNode(Compass.SOUTH, south);
		center.putNode(Compass.EAST, east);
		center.putNode(Compass.WEST, west);
		//north
		north.putNode(Compass.EAST, northEast);
		north.putNode(Compass.WEST, northWest);
		//south
		south.putNode(Compass.EAST, southEast);
		south.putNode(Compass.WEST, southWest);
		//east
		east.putNode(Compass.NORTH, eastNorth);
		east.putNode(Compass.SOUTH, eastSouth);
		//west
		west.putNode(Compass.NORTH, westNorth);
		west.putNode(Compass.SOUTH, westSouth);

		//corners
		northEast.putNode(Compass.SOUTH, eastNorth);
		northWest.putNode(Compass.SOUTH, westNorth);
		southEast.putNode(Compass.NORTH, eastSouth);
		southWest.putNode(Compass.NORTH, westSouth);

		int i = 0;
	}

	@Test
	public void opposites_put() {
		Node<String> node = new HashNode<>("C");

		//put value
		node.put(Compass.SOUTH, "S");

		assertSame(
				node,
				node.getNode(Compass.SOUTH)
					.getNode(Compass.NORTH),
				"South was mapped to north?"
		);

		node.clear();

		//put node
		node.putNode(Compass.SOUTH, new HashNode<>("S"));

		assertSame(
				node,
				node.getNode(Compass.SOUTH)
					.getNode(Compass.NORTH),
				"South was mapped to north?"
		);

		node.clear();

		//put link
		Link<String> link = new SimpleLink<>(Compass.NORTH);
		node.linkSet().add(link);
		new HashNode<>("S").linkSet().add(link.getOpposite());

		assertSame(
				node,
				node.getNode(Compass.SOUTH)
					.getNode(Compass.NORTH),
				"South was mapped to north?"
		);

		node.clear();
	}

	@Test
	public void opposites_contains() {
		Node<String> node = new HashNode<>("C");
		Node<String> other = new HashNode<>("S");
		Link<String> link = new SimpleLink<>(Compass.NORTH);

		node.linkSet().add(link);
		other.linkSet().add(link.getOpposite());

		//containsKey
		assertTrue(
				node.containsKey(Compass.SOUTH),
				"containsKey is looking at the wrong side"
		);
		assertFalse(
				node.containsKey(Compass.NORTH),
				"containsKey is seeing the mirror"
		);

		//containsLink
		assertTrue(
				node.linkSet().contains(link),
				"containsLink is looking at the wrong side"
		);
		assertFalse(
				node.linkSet().contains(link.getOpposite()),
				"containsLink is NOT seeing the mirror"
		);

		//containsNode
		assertTrue(
				node.containsNode(other),
				"containsNode is looking at the wrong side"
		);
		assertFalse(
				node.containsNode(node),
				"containsNode is seeing the mirror"
		);

		//containValue
		assertTrue(
				node.containsValue("S"),
				"containsValue is looking at the wrong side"
		);
		assertFalse(
				node.containsValue("C"),
				"containsValue is seeing the mirror"
		);
	}

	public enum Compass implements Key {
		WEST("EAST"),
		EAST("WEST"),
		NORTH("SOUTH"),
		SOUTH("NORTH");

		private final String opposite;

		Compass(String opposite) {
			Objects.requireNonNull(opposite, "opposite");
			this.opposite = opposite;
		}

		@NotNull
		@Override
		public Key opposite() {
			return Compass.valueOf(this.opposite);
		}
	}
}

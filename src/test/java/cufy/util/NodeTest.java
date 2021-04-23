package cufy.util;

import cufy.util.Node.Key;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Objects;

@SuppressWarnings("JUnitTestNG")
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

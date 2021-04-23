///*
// *	Copyright 2021 Cufy
// *
// *	Licensed under the Apache License, Version 2.0 (the "License");
// *	you may not use this file except in compliance with the License.
// *	You may obtain a copy of the License at
// *
// *	    http://www.apache.org/licenses/LICENSE-2.0
// *
// *	Unless required by applicable law or agreed to in writing, software
// *	distributed under the License is distributed on an "AS IS" BASIS,
// *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *	See the License for the specific language governing permissions and
// *	limitations under the License.
// */
//package cufy.util;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.*;
//
///**
// * An implementation of {@link NodeX} backed by a {@link HashMap}.
// *
// * @param <V> the type of the value of the node.
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2021.03.11
// */
//public class HashNodeX<V> implements NodeX<V> {
//	/**
//	 * A wrapped set from the entry set of {@link #map}.
//	 *
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@NotNull
//	protected final Set<NodeX.Link<V>> links = new LinkSet();
//	/**
//	 * Mappings for the relations of this node and the nodes relating to this node.
//	 *
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@NotNull
//	protected final Map<NodeX.Relation, @NotNull NodeX<V>> map = new HashMap<>();
//	/**
//	 * The value of this node.
//	 *
//	 * @since 0.0.1 ~2021.03.11
//	 */
//	@Nullable
//	protected V value;
//
//	@Override
//	public boolean equals(Object object) {
//		return object == this;
//	}
//
//	@Nullable
//	@Override
//	public NodeX<V> get(@NotNull NodeX.Relation relation) {
//		Objects.requireNonNull(relation, "relation");
//		return this.map.get(relation);
//	}
//
//	@Nullable
//	@Override
//	public V get() {
//		return this.value;
//	}
//
//	@Override
//	public boolean has(@NotNull NodeX<V> node) {
//		Objects.requireNonNull(node, "node");
//		return this.map.containsValue(node);
//	}
//
//	@Override
//	public boolean has(@NotNull NodeX.Relation relation) {
//		Objects.requireNonNull(relation, "relation");
//		return this.map.containsKey(relation);
//	}
//
//	@Override
//	public int hashCode() {
//		//noinspection NonFinalFieldReferencedInHashCode
//		return Objects.hashCode(this.value);
//	}
//
//	@Override
//	@NotNull
//	public Set<NodeX.Link<V>> links() {
//		//noinspection AssignmentOrReturnOfFieldWithMutableType
//		return this.links;
//	}
//
//	@Nullable
//	@Override
//	public NodeX<V> put(@NotNull NodeX.Relation relation, @NotNull NodeX<V> node) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(node, "node");
//		NodeX<V> n = this.map.get(relation);
//
//		//if new not put to this
//		if (n != node) {
//			NodeX.Relation opposite = relation.opposite();
//
//			//if previous not removed from this
//			if (n != null) {
//				//1. remove previous from this
//				this.map.remove(relation);
//
//				//if this not removed from previous
//				if (n.get(opposite) == this)
//					//2. remove this from previous
//					n.remove(opposite);
//			}
//
//			//3. put new to this
//			this.map.put(relation, node);
//
//			//if this not put to new
//			if (node.get(opposite) != this)
//				//4. put this to new
//				node.put(opposite, this);
//		}
//
//		return n;
//	}
//
//	@Nullable
//	@Override
//	public NodeX<V> remove(@NotNull NodeX.Relation relation) {
//		Objects.requireNonNull(relation, "relation");
//		NodeX<V> n = this.map.get(relation);
//
//		//if previous node not removed from this
//		if (n != null) {
//			NodeX.Relation opposite = relation.opposite();
//
//			//1. remove previous from this
//			this.map.remove(relation);
//
//			//if this not removed from previous
//			if (n.get(opposite) == this)
//				//2. remove this from previous
//				n.remove(opposite);
//		}
//
//		return n;
//	}
//
//	@Nullable
//	@Override
//	public V set(@Nullable V value) {
//		V v = this.value;
//		this.value = value;
//		return v;
//	}
//
//	@NotNull
//	@Override
//	public String toString() {
//		return "{:" + this.value + "}";
//	}
//
//	/**
//	 * An internal implementation of {@link NodeX.Link} backed by the containing node.
//	 *
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	protected class HashLink implements NodeX.Link<V> {
//		/**
//		 * The relation.
//		 *
//		 * @since 0.0.1 ~2021.04.16
//		 */
//		@NotNull
//		protected final NodeX.Relation relation;
//
//		/**
//		 * Construct a simple link with the given {@code relation}.
//		 *
//		 * @param relation the relation.
//		 * @since 0.0.1 ~2021.04.16
//		 */
//		public HashLink(@NotNull NodeX.Relation relation) {
//			Objects.requireNonNull(relation, "relation");
//			this.relation = relation;
//		}
//
//		@Override
//		public boolean equals(@Nullable Object object) {
//			if (object == this)
//				return true;
//			if (object instanceof NodeX.Link)
//				try {
//					NodeX.Link link = (NodeX.Link) object;
//
//					NodeX<V> node = HashNodeX.this.get(this.relation);
//					return node == link.getNode() &&
//						   Objects.equals(this.relation, link.getRelation());
//				} catch (IllegalStateException ignored) {
//					//just in case
//				}
//
//			return false;
//		}
//
//		@NotNull
//		@Override
//		public NodeX<V> getNode() {
//			NodeX<V> node = HashNodeX.this.get(this.relation);
//
//			if (node == null)
//				throw new IllegalStateException("Link removed");
//
//			return node;
//		}
//
//		@NotNull
//		@Override
//		public NodeX.Relation getRelation() {
//			return this.relation;
//		}
//
//		@Override
//		public int hashCode() {
//			NodeX<V> node = HashNodeX.this.get(this.relation);
//			return this.relation.hashCode() ^
//				   Objects.hashCode(node);
//		}
//
//		@Nullable
//		@Override
//		public NodeX<V> setNode(@NotNull NodeX<V> node) {
//			return HashNodeX.this.put(this.relation, node);
//		}
//
//		@NotNull
//		@Override
//		public String toString() {
//			NodeX<V> node = HashNodeX.this.get(this.relation);
//			return this.relation + " -> " + node;
//		}
//	}
//
//	/**
//	 * An internal implementation of {@link Set} of {@link NodeX.Link} backed by {@link
//	 * #entrySet} and the containing node.
//	 *
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	protected class LinkSet extends AbstractSet<NodeX.Link<V>> {
//		/**
//		 * An entry set from {@link #map}.
//		 *
//		 * @since 0.0.1 ~2021.04.16
//		 */
//		@NotNull
//		protected final Set<Map.Entry<NodeX.Relation, NodeX<V>>>
//				entrySet = HashNodeX.this.map.entrySet();
//
//		@Override
//		public boolean contains(Object object) {
//			return this.entrySet.contains(object);
//		}
//
//		@Override
//		public boolean isEmpty() {
//			return this.entrySet.isEmpty();
//		}
//
//		@Override
//		public Iterator<NodeX.Link<V>> iterator() {
//			return new LinkSetIterator();
//		}
//
//		@Override
//		public int size() {
//			return this.entrySet.size();
//		}
//
//		/**
//		 * An internal implementation of {@link Iterator} of {@link NodeX.Link} backed by
//		 * {@link #entrySet} and the containing node.
//		 *
//		 * @author LSafer
//		 * @version 0.0.1
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		protected class LinkSetIterator implements Iterator<NodeX.Link<V>> {
//			/**
//			 * An iterator from {@link #entrySet}.
//			 *
//			 * @since 0.0.1 ~2021.04.16
//			 */
//			@NotNull
//			protected final Iterator<Map.Entry<NodeX.Relation, NodeX<V>>>
//					iterator = LinkSet.this.entrySet.iterator();
//
//			/**
//			 * The last link returned from {@link #next()}.
//			 *
//			 * @since 0.0.1 ~2021.04.16
//			 */
//			@Nullable
//			protected NodeX.Link<V> last;
//
//			@Override
//			public boolean hasNext() {
//				return this.iterator.hasNext();
//			}
//
//			@Override
//			public NodeX.Link<V> next() {
//				return this.last = new HashLink(this.iterator.next().getKey());
//			}
//
//			@Override
//			public void remove() {
//				NodeX.Link<V> last = this.last;
//				this.last = null;
//
//				if (last == null)
//					throw new IllegalStateException("remove");
//
//				HashNodeX.this.remove(last.getRelation());
//			}
//		}
//	}
//
//}
///* Intentionally Removed */
////	@Nullable
////	protected Collection<Node<V>> nodes;
////	@Nullable
////	protected Set<Relation> relationSet;
////
////	@Override
////	public boolean isEmpty() {
////		return this.map.isEmpty();
////	}
////
////	@NotNull
////	@Override
////	public Collection<Node<V>> nodes() {
////		return this.nodes == null ?
////			   this.nodes = new Nodes() :
////			   this.nodes;
////	}
////
////	@Override
////	public void putAll(@NotNull Node<V> node) {
////		Objects.requireNonNull(node, "node");
////		node.forEach(this::put);
////	}
////
////	@NotNull
////	@Override
////	public Set<Node.Relation> relationSet() {
////		return this.relationSet == null ?
////			   this.relationSet = new RelationSet() :
////			   this.relationSet;
////	}
////
////	@Override
////	public void remove() {
////		this.links().clear();
////	}
////	@Override
////	public int size() {
////		return this.map.size();
////	}
////	/**
////	 * An internal implementation of {@link Collection} of {@link Node} backed by {@link
////	 * #links()}.
////	 *
////	 * @author LSafer
////	 * @version 0.0.1
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	protected class Nodes extends AbstractCollection<Node<V>> {
////		/**
////		 * A {@link #links() link set}.
////		 *
////		 * @since 0.0.1 ~2021.04.17
////		 */
////		@NotNull
////		protected final Set<Node.Link<V>>
////				linkSet = HashNode.this.links();
////
////		@Override
////		public boolean contains(Object object) {
////			return this.linkSet.contains(object);
////		}
////
////		@Override
////		public boolean isEmpty() {
////			return this.linkSet.isEmpty();
////		}
////
////		@Override
////		public Iterator<Node<V>> iterator() {
////			return new NodesIterator();
////		}
////
////		@Override
////		public int size() {
////			return this.linkSet.size();
////		}
////
////		/**
////		 * An internal implementation of {@link Iterator} of {@link Node} backed by {@link
////		 * #linkSet}.
////		 *
////		 * @author LSafer
////		 * @version 0.0.1
////		 * @since 0.0.1 ~2021.04.17
////		 */
////		protected class NodesIterator implements Iterator<Node<V>> {
////			/**
////			 * An iterator from {@link #linkSet}.
////			 *
////			 * @since 0.0.1 ~2021.04.17
////			 */
////			@NotNull
////			protected final Iterator<Node.Link<V>>
////					iterator = Nodes.this.linkSet.iterator();
////
////			@Override
////			public boolean hasNext() {
////				return this.iterator.hasNext();
////			}
////
////			@Override
////			public Node<V> next() {
////				return this.iterator.next().getNode();
////			}
////
////			@Override
////			public void remove() {
////				this.iterator.remove();
////			}
////		}
////	}
////
////	/**
////	 * An internal implementation of {@link Collection} of {@link Node.Relation} backed by
////	 * {@link #links()}.
////	 *
////	 * @author LSafer
////	 * @version 0.0.1
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	protected class RelationSet extends AbstractSet<Node.Relation> {
////		/**
////		 * A {@link #links() link set}.
////		 *
////		 * @since 0.0.1 ~2021.04.17
////		 */
////		@NotNull
////		protected final Set<Node.Link<V>>
////				linkSet = HashNode.this.links();
////
////		@Override
////		public boolean contains(Object object) {
////			return this.linkSet.contains(object);
////		}
////
////		@Override
////		public boolean isEmpty() {
////			return this.linkSet.isEmpty();
////		}
////
////		@Override
////		public Iterator<Node.Relation> iterator() {
////			return new RelationIterator();
////		}
////
////		@Override
////		public int size() {
////			return this.linkSet.size();
////		}
////
////		/**
////		 * An internal implementation of {@link Iterator} of {@link Node.Relation} backed
////		 * by {@link #linkSet}.
////		 *
////		 * @author LSafer
////		 * @version 0.0.1
////		 * @since 0.0.1 ~2021.04.17
////		 */
////		protected class RelationIterator implements Iterator<Node.Relation> {
////			/**
////			 * An iterator from {@link #linkSet}.
////			 *
////			 * @since 0.0.1 ~2021.04.17
////			 */
////			@NotNull
////			protected final Iterator<Node.Link<V>>
////					iterator = RelationSet.this.linkSet.iterator();
////
////			@Override
////			public boolean hasNext() {
////				return this.iterator.hasNext();
////			}
////
////			@Override
////			public Node.Relation next() {
////				return this.iterator.next().getRelation();
////			}
////
////			@Override
////			public void remove() {
////				this.iterator.remove();
////			}
////		}
////	}
//
///* Junk */
////	protected final Map<K, Node.Link<K, V>> links = new HashMap<>();
//
////
////	/**
////	 * An internal method cleaning links in {@link #links} that does not point to this
////	 * node.
////	 *
////	 * @since 0.0.1 ~2021.04.16
////	 */
////	protected void clean() {
////		this.links.values().removeIf(link -> link.getNode() != this);
////	}
////
////	@Override
////	public void putLink(@NotNull K key, @NotNull Link<K, V> link) {
////		Objects.requireNonNull(key, "key");
////		Objects.requireNonNull(link, "link");
////		Link<K, V> old = this.links.put(key, link);
////
////		if (old != null)
////			old.removeNode();
////	}
////
////	/**
////	 * A simple link that deals with any kind of key and any kind of node and does not
////	 * have any special effects or support special cases.
////	 *
////	 * @param <K> the type of the key of the link and the type of the keys in the node of
////	 *            it.
////	 * @param <V> the type of the value of the node of the link.
////	 * @author LSafer
////	 * @version 0.0.1
////	 * @since 0.0.1 ~2021.03.11
////	 */
////	public static class SimpleLink<K extends Relation, V> implements Node.Link<K, V> {
////		/**
////		 * The opposite link.
////		 *
////		 * @since 0.0.1 ~2021.04.15
////		 */
////		@NotNull
////		protected final Node.Link<K, V> opposite;
////		/**
////		 * The node this link is pointing to.
////		 *
////		 * @since 0.0.1 ~2021.03.11
////		 */
////		@Nullable
////		protected Node<K, V> node;
////
////		/**
////		 * Construct a new link with no nodes set.
////		 *
////		 * @since 0.0.1 ~2021.04.16
////		 */
////		public SimpleLink() {
////			this.opposite = new SimpleLink<>(this);
////		}
////
////		/**
////		 * Construct a new link with the node of it initialized to the given {@code node}
////		 * with no opposite node set.
////		 *
////		 * @param node the node of the constructed link.
////		 * @throws NullPointerException if the given {@code node} is null.
////		 * @since 0.0.1 ~2021.04.16
////		 */
////		public SimpleLink(@NotNull Node<K, V> node) {
////			Objects.requireNonNull(node, "node");
////			this.opposite = new SimpleLink<>(this);
////			this.node = node;
////		}
////
////		/**
////		 * Construct a new link with the node of it initialized to the given {@code node}
////		 * and with the opposite node initialized to the given {@code other}.
////		 *
////		 * @param node  the node of the constructed link.
////		 * @param other the node of the opposite link of the constructed link.
////		 * @throws NullPointerException if the given {@code node} or {@code other} is
////		 *                              null.
////		 * @since 0.0.1 ~2021.04.16
////		 */
////		public SimpleLink(@NotNull Node<K, V> node, Node<K, V> other) {
////			Objects.requireNonNull(node, "node");
////			Objects.requireNonNull(other, "other");
////			this.opposite = new SimpleLink<>(this, other);
////			this.node = node;
////		}
////
////		/**
////		 * An internal method to construct a new opposite link for the given {@code
////		 * opposite}.
////		 *
////		 * @param opposite the opposite link of the constructed link.
////		 * @throws NullPointerException if the given {@code opposite} is null.
////		 * @since 0.0.1 ~2021.04.16
////		 */
////		@SuppressWarnings("CopyConstructorMissesField")
////		protected SimpleLink(@NotNull SimpleLink<K, V> opposite) {
////			this.opposite = opposite;
////		}
////
////		/**
////		 * An internal method to construct a new opposite link for the given {@code
////		 * opposite} with its node initialized to the given {@code node}.
////		 *
////		 * @param opposite the opposite link of the constructed link.
////		 * @param node     the initial node of the constructed link.
////		 * @throws NullPointerException if the given {@code opposite} or {@code node} is
////		 *                              null.
////		 * @since 0.0.1 ~2021.04.16
////		 */
////		protected SimpleLink(@NotNull SimpleLink<K, V> opposite, @NotNull Node<K, V> node) {
////			Objects.requireNonNull(opposite, "opposite");
////			Objects.requireNonNull(node, "node");
////			this.opposite = opposite;
////			this.node = node;
////		}
////
////		@Override
////		public boolean equals(@Nullable Object object) {
////			Objects.requireNonNull(object, "object");
////			if (object == this)
////				return true;
////			if (object instanceof Node.Link) {
////				Node.Link<?, ?> link = (Node.Link<?, ?>) object;
////
////				//noinspection NonFinalFieldReferenceInEquals
////				return Objects.equals(this.node, link.getNode()) &&
////					   Objects.equals(this.opposite.getNode(), link.opposite().getNode());
////			}
////
////			return false;
////		}
////
////		@Nullable
////		@Override
////		public Node<K, V> getNode() {
////			return this.node;
////		}
////
////		@Override
////		public int hashCode() {
////			return Objects.hashCode(this.node) ^
////				   Objects.hashCode(this.opposite.getNode());
////		}
////
////		@NotNull
////		@Override
////		public Node.Link<K, V> opposite() {
////			return this.opposite;
////		}
////
////		@Nullable
////		@Override
////		public Node<K, V> removeNode() {
////			Node<K, V> node = this.node;
////			this.node = null;
////			return node;
////		}
////
////		@Nullable
////		@Override
////		public Node<K, V> setNode(@NotNull Node<K, V> node) {
////			Objects.requireNonNull(node, "node");
////			Node<K, V> n = this.node;
////			this.node = node;
////			return n;
////		}
////
////		@NotNull
////		@Override
////		public String toString() {
////			return this.node + " -> " + this.opposite.getNode();
////		}
////	}

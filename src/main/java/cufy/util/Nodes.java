/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package cufy.util;

import cufy.util.AbstractNode.SimpleKey;
import cufy.util.AbstractNode.SimpleLink;
import cufy.util.Node.Key;
import cufy.util.Node.Link;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.*;

/**
 * A utility interface containing common utilities for {@link Node}s.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.04.27
 */
public interface Nodes {
	//query

	/**
	 * Return the most next node after the given {@code node} with respect to the given
	 * {@code key}.
	 * <br>
	 * If the given {@code node} is relating to itself. Then, the node before the given
	 * {@code node} will be returned. (even if it was the node itself)
	 *
	 * @param key  the key to follow.
	 * @param node the node to get its tail.
	 * @param <V>  the type of the value of the node.
	 * @return the mot next node after the given {@code node} with respect to the given
	 *        {@code key}.
	 * @throws NullPointerException if the given {@code key} or {@code node} is null.
	 * @since 0.0.4 ~2021.05.03
	 */
	@NotNull
	@Contract(pure = true)
	static <V> Node<V> tail(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Node<V> tail = node;
		while (true) {
			Node<V> next = tail.getNode(key);

			if (next == null || next == node)
				return tail;

			tail = next;
		}
	}

	/**
	 * Return the most previous node after the given {@code node} with respect to the
	 * given {@code key}.
	 * <br>
	 * If the given {@code node} is relating to itself. Then, the node after the given
	 * {@code node} will be returned. (even if it was the node itself)
	 *
	 * @param key  the key to follow.
	 * @param node the node to get its head.
	 * @param <V>  the type of the value of the node.
	 * @return the mot previous node after the given {@code node} with respect to the
	 * 		given {@code key}.
	 * @throws NullPointerException if the given {@code key} or {@code node} is null.
	 * @since 0.0.4 ~2021.05.03
	 */
	@NotNull
	@Contract(pure = true)
	static <V> Node<V> head(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		//it is the same logic after all!
		return Nodes.tail(key.opposite(), node);
	}

	/**
	 * Determine if the given {@code node} relates to itself with respect to the given
	 * {@code key} or not.
	 *
	 * @param key  the key to follow.
	 * @param node the node to be checked.
	 * @param <V>  the type of the value.
	 * @return true, if the given {@code node} relates to itself with respect to the given
	 *        {@code key}. False, otherwise.
	 * @throws NullPointerException if the given {@code key} or {@code node} is null.
	 * @since 0.0.4 ~2021.05.03
	 */
	@Contract(pure = true)
	static <V> boolean isInfinite(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Node<V> next = node;
		while ((next = next.getNode(key)) != null)
			if (next == node)
				return true;
		return false;
	}

	//mutate

	/**
	 * Rearrange the nodes in the given {@code nodes} array to follow the order of the
	 * array.
	 * <br>
	 * Null nodes are skipped (as if they does not exist).
	 *
	 * @param key   the key to rearrange the nodes with.
	 * @param nodes an array containing the nodes to be rearranged.
	 * @param <V>   the type of the values of the nodes.
	 * @throws NullPointerException          if the given {@code key} or {@code nodes} is
	 *                                       null.
	 * @throws IllegalArgumentException      if a node rejected a link or the given {@code
	 *                                       key} or its opposite.
	 * @throws UnsupportedOperationException if a node refused to perform a necessary
	 *                                       operation.
	 * @since 0.0.4 ~2021.05.03
	 * @deprecated specification might change.
	 */
	@SuppressWarnings("OverloadedVarargsMethod")
	@SafeVarargs
	@Deprecated
	static <V> void concat(@NotNull Key key, Node<V> @NotNull ... nodes) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(nodes, "nodes");

		//clean start
		for (Node<V> node : nodes)
			if (node != null) {
				node.remove(key);
				node.remove(key.opposite());
			}

		//loop until first non-null node
		for (int i = 0; i < nodes.length; i++) {
			Node<V> node = nodes[i];

			//skip if null
			if (node != null) {
				//foreach remaining non-null node
				for (int j = i + 1; j < nodes.length; j++) {
					Node<V> next = nodes[j];

					//skip if null
					if (next != null) {
						//link the next node to the previous node
						node.putNode(key, next);

						node = next;
					}
				}

				//yay, eof
				return;
			}
		}
	}

	/**
	 * Rearrange the nodes returned from an iterator of the given {@code nodes} iterable
	 * to follow the same order as they are returned from the iterator.
	 * <br>
	 * Null nodes are skipped (as if they does not exist).
	 *
	 * @param key   the key to rearrange the nodes with.
	 * @param nodes an iterable containing the nodes to be rearranged.
	 * @param <V>   the type of the values of the nodes.
	 * @throws NullPointerException          if the given {@code key} or {@code nodes} is
	 *                                       null.
	 * @throws IllegalArgumentException      if a node rejected a link or the given {@code
	 *                                       key} or its opposite.
	 * @throws UnsupportedOperationException if a node refused to perform a necessary
	 *                                       operation.
	 * @since 0.0.4 ~2021.05.03
	 * @deprecated specification might change.
	 */
	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated
	static <V> void concat(@NotNull Key key, @NotNull Iterable<Node<V>> nodes) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(nodes, "nodes");

		//clean start
		for (Node<V> node : nodes)
			if (node != null) {
				node.remove(key);
				node.remove(key.opposite());
			}

		Iterator<Node<V>> iterator = nodes.iterator();

		//loop until first non-null node
		while (iterator.hasNext()) {
			Node<V> node = iterator.next();

			//skip if null
			if (node != null) {
				//foreach remaining non-null node
				while (iterator.hasNext()) {
					Node<V> next = iterator.next();

					//skip if null
					if (next != null) {
						//link the next node to the previous node
						node.putNode(key, next);

						node = next;
					}
				}

				//yay, eof
				return;
			}
		}
	}

	/**
	 * Pop the given {@code node} from its relatives with the given {@code key} without
	 * cutting the chain.
	 * <br>
	 * This method will link the node previous to the given {@code node} to the node next
	 * to it with respect to the given {@code key}.
	 * <br>
	 * If any exception occurs. The exception will fall throw this method with nothing
	 * changed.
	 *
	 * @param key  the key of the chain to pop the node from.
	 * @param node the node to be popped.
	 * @param <V>  the type of the value of the node.
	 * @throws NullPointerException          if the given {@code key} or {@code node} is
	 *                                       null.
	 * @throws IllegalArgumentException      if {@code node.getNode(key)} or {@code
	 *                                       node.getNode(key.opposite())} rejected a key
	 *                                       or a link required for the operation.
	 * @throws UnsupportedOperationException if {@code node.getNode(key)} or {@code
	 *                                       node.getNode(key.opposite())} refused to
	 *                                       perform a required operation.
	 * @since 0.0.3 ~2021.04.29
	 * @deprecated specification might change.
	 */
	@Contract(mutates = "param2")
	@Deprecated
	static <V> void pop(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Node<V> next = node.getNode(key);
		Node<V> previous = node.getNode(key.opposite());

		if (previous != null)
			if (next == null)
				previous.remove(key);
			else
				previous.putNode(key, next);
		else if (next != null)
			node.remove(key);
	}

	/**
	 * Set the given {@code other} node to be at the place of the given {@code node} with
	 * respect to the given {@code key}.
	 *
	 * @param key   the key of the chain to replace the given {@code node} with the given
	 *              {@code other} at.
	 * @param node  the node to be replaced.
	 * @param other the node to take the place of {@code node}.
	 * @param <V>   the type of the value of the node.
	 * @throws NullPointerException          if the given {@code key} or {@code node} or
	 *                                       {@code other} is null.
	 * @throws IllegalArgumentException      if {@code other}, {@code node.getNode(key)}
	 *                                       or {@code node.getNode(key.opposite())}
	 *                                       rejected a key or a link required for the
	 *                                       operation.
	 * @throws UnsupportedOperationException if {@code other}, {@code node.getNode(key)}
	 *                                       or {@code node.getNode(key.opposite())}
	 *                                       refused to perform a required * operation.
	 * @since 0.0.4 ~2021.05.03
	 * @deprecated specification might change.
	 */
	@Contract(mutates = "param2,param3")
	@Deprecated
	static <V> void set(@NotNull Key key, @NotNull Node<V> node, @NotNull Node<V> other) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(other, "other");
		Node<V> next = node.getNode(key);
		Node<V> previous = node.getNode(key.opposite());

		if (previous != null)
			previous.putNode(key, other);
		if (next != null)
			other.putNode(key, next);
	}

	/**
	 * Push the given {@code other} node after the given {@code node} with respect to the
	 * given {@code key}. The previous node after the given {@code node} will be after the
	 * given {@code other} node.
	 *
	 * @param key   the key to where to push the {@code other} node.
	 * @param node  the node to push the {@code other} node after.
	 * @param other the node to be pushed.
	 * @param <V>   the type of the value of the nodes.
	 * @throws NullPointerException          if the given {@code key} or {@code node} or
	 *                                       {@code other} is null.
	 * @throws IllegalArgumentException      if {@code node}, {@code node.getNode(key)} or
	 *                                       {@code other} rejected a key or a link
	 *                                       required for the operation.
	 * @throws UnsupportedOperationException if {@code node}, {@code node.getNode(key)} or
	 *                                       {@code other} refused to perform a required
	 *                                       operation.
	 * @since 0.0.3 ~2021.04.28
	 * @deprecated specification might change.
	 */
	@Contract(mutates = "param2,param3")
	@Deprecated
	static <V> void add(@NotNull Key key, @NotNull Node<V> node, @NotNull Node<V> other) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(other, "other");
		Node<V> next = node.putNode(key, other);

		if (next != null)
			other.putNode(key, next);
	}

	/**
	 * Push the head of the given {@code other} node after the given {@code node} with
	 * respect to the given {@code key}. The previous node after the given {@code node}
	 * will be after the tail of the given {@code other} node.
	 *
	 * @param key   the key to where to push the {@code other} node.
	 * @param node  the node to push the {@code other} node after.
	 * @param other the node to be pushed.
	 * @param <V>   the type of the value of the nodes.
	 * @throws NullPointerException          if the given {@code key} or {@code node} or
	 *                                       {@code other} is null.
	 * @throws IllegalArgumentException      if {@code node}, {@code node.getNode(key)},
	 *                                       {@code Nodes.head(other)} or {@code
	 *                                       Nodes.tail(other)} rejected a key or a link
	 *                                       required for the operation.
	 * @throws UnsupportedOperationException if {@code node}, {@code node.getNode(key)},
	 *                                       {@code Nodes.head(other)} or {@code
	 *                                       Nodes.tail(other)} refused to perform a
	 *                                       required operation.
	 * @since 0.0.3 ~2021.04.28
	 * @deprecated specification might change.
	 */
	@Contract(mutates = "param2,param3")
	@Deprecated
	static <V> void addAll(@NotNull Key key, @NotNull Node<V> node, @NotNull Node<V> other) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(other, "other");

		Node<V> head = Nodes.head(key, other);
		Node<V> tail = Nodes.tail(key, other);

		//put `other` after `node`
		Node<V> old = node.putNode(key, head);

		if (old != null)
			//put `old` after the tail of `other`
			tail.putNode(key, old);
	}

	//sort

	/**
	 * Sort the relatives of the given {@code node} with respect to the given {@code key}
	 * using the given {@code comparator}.
	 *
	 * @param key        the key to follow.
	 * @param node       the node to sort its relatives.
	 * @param comparator the comparator to be used for sorting.
	 * @param <V>        the type of the value of the node.
	 * @throws NullPointerException          if the given {@code key} or {@code node} or
	 *                                       {@code comparator} is null.
	 * @throws IllegalArgumentException      if a node rejected a link or the given {@code
	 *                                       key} or its opposite.
	 * @throws UnsupportedOperationException if a node refused to perform a necessary
	 *                                       operation.
	 * @since 0.0.4 ~2021.05.03
	 */
	static <V> void sort(@NotNull Key key, @NotNull Node<V> node, @NotNull Comparator<Node<V>> comparator) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Objects.requireNonNull(comparator, "comparator");
		LinkedList<Node<V>> list = Nodes.collect(key, node);

		list.sort(comparator);

		if (Nodes.isInfinite(key, node))
			//add the last to the first to make `Nodes.concat` retain the infinity illusion
			list.addFirst(list.getLast());

		Nodes.concat(key, list);
	}

	/**
	 * Sort the relatives of the given {@code node} with respect to the given {@code key}
	 * by the natural order of their values.
	 *
	 * @param key  the key to follow.
	 * @param node the node to sort its relatives.
	 * @param <V>  the type of the value of the node.
	 * @throws NullPointerException          if the given {@code key} or {@code node} or
	 *                                       {@code comparator} is null; if a node has a
	 *                                       {@code null} value.
	 * @throws IllegalArgumentException      if a node rejected a link or the given {@code
	 *                                       key} or its opposite.
	 * @throws UnsupportedOperationException if a node refused to perform a necessary
	 *                                       operation.
	 * @since 0.0.4 ~2021.05.03
	 */
	static <V extends Comparable<? super V>> void sort(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		LinkedList<Node<V>> list = Nodes.collect(key, node);

		list.sort(Comparator.comparing(Node::get));

		if (Nodes.isInfinite(key, node))
			//add the last to the first to make `Nodes.concat` retain the infinity illusion
			list.addFirst(list.getLast());

		Nodes.concat(key, list);
	}

	//struct

	/**
	 * Construct a new key.
	 *
	 * @param toString         the toString of the constructed key.
	 * @param oppositeToString the toString of the opposite of the constructed key.
	 * @return a new key as specified above.
	 * @throws NullPointerException if the given {@code toString} or {@code
	 *                              oppositeToString} is null.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	static Key key(@NotNull String toString, @NotNull String oppositeToString) {
		return new SimpleKey(toString, oppositeToString);
	}

	/**
	 * Construct a new link.
	 *
	 * @param key the key of the constructed link.
	 * @param <V> the type of the value of the node of the constructed link.
	 * @return a new link.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static <V> Link<V> link(@NotNull Key key) {
		return new SimpleLink<>(key);
	}

	//unmodifiable

	/**
	 * Get an unmodifiable view of the given {@code node}.
	 *
	 * @param node the node to get an unmodifiable view for.
	 * @param <V>  the type of the value of the node.
	 * @return an unmodifiable view of the given {@code node}.
	 * @throws NullPointerException if the given {@code node} is null.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@UnmodifiableView
	@Contract(value = "_->new", pure = true)
	static <V> Node<V> unmodifiableNode(@NotNull Node<V> node) {
		return new UnmodifiableNode<>(node);
	}

	/**
	 * Get an unmodifiable view of the given {@code link}.
	 *
	 * @param link the link to get an unmodifiable view of.
	 * @param <V>  the type of the value of the node of the link.
	 * @return an unmodifiable view of the given {@code link}.
	 * @throws NullPointerException if the given {@code link} is null.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@UnmodifiableView
	@Contract(value = "_->new", pure = true)
	static <V> Link<V> unmodifiableLink(@NotNull Link<V> link) {
		Objects.requireNonNull(link, "link");
		return new UnmodifiableLink<>(link);
	}

	/**
	 * An unmodifiable link implementation that delegates to another link but wraps the
	 * node returned by it using the {@link #unmodifiableNode(Node)} method. The link will
	 * be always reserved.
	 *
	 * @param <V>
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.04.28
	 */
	class UnmodifiableLink<V> implements Link<V>, Serializable {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -2267321588558427335L;

		/**
		 * The wrapped link.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@NotNull
		private final Link<V> link;
		/**
		 * The opposite link. (also unmodifiable)
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@NotNull
		private final Link<V> opposite;

		/**
		 * A lazily initialized filler node this link is pointing to when the original
		 * link is pointing to nothing.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		private transient Node<V> filler;

		/**
		 * Construct a new unmodifiable link delegating to the given {@code link}.
		 *
		 * @param link the link to be wrapped by the constructed link.
		 * @throws NullPointerException if the given {@code link} is null.
		 * @since 0.0.1 ~2021.04.28
		 */
		public UnmodifiableLink(@NotNull Link<V> link) {
			Objects.requireNonNull(link, "link");
			this.link = link;
			this.opposite = new UnmodifiableLink<>(link.getOpposite());
		}

		/**
		 * An internal method to construct an opposite link.
		 *
		 * @param link     the link to be wrapped.
		 * @param opposite the opposite link.
		 * @throws NullPointerException if the given {@code link} or {@code opposite} is
		 *                              null.
		 * @since 0.0.1 ~2021.04.28
		 */
		protected UnmodifiableLink(@NotNull Link<V> link, @NotNull Link<V> opposite) {
			Objects.requireNonNull(link, "link");
			Objects.requireNonNull(opposite, "opposite");
			this.link = link;
			this.opposite = opposite;
		}

		@Override
		public boolean equals(@Nullable Object object) {
			return super.equals(object);
		}

		@Override
		public int hashCode() {
			return this.link.hashCode();
		}

		@NotNull
		@Override
		public String toString() {
			return this.link.toString();
		}

		@NotNull
		@Override
		public Key getKey() {
			return this.link.getKey();
		}

		@NotNull
		@Override
		public Link<V> getOpposite() {
			return this.opposite;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec if the original link is pointing to a node. Then, an unmodifiable
		 * 		node wrapping that node is returned. Otherwise, a new singleton node with
		 * 		this link is returned. This way, this link can always throw {@link
		 *        UnsupportedOperationException} when {@link #removeNode()} or {@link
		 *        #setNode(Node)} is called without breaking the specification.
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		@Override
		public Node<V> getNode() {
			Node<V> node = this.link.getNode();

			if (node == null) {
				if (this.filler == null) {
					//this way we can skip strict relation initiation
					SingletonNode<V> singleton = new SingletonNode<>();
					//noinspection AccessingNonPublicFieldOfAnotherObject
					singleton.link = this;
					this.filler = singleton;
				}

				node = this.filler;
			}

			return Nodes.unmodifiableNode(node);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec immediately throws {@link UnsupportedOperationException}.
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		@Override
		public Node<V> removeNode() {
			throw new UnsupportedOperationException("removeNode");
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec immediately throws {@link UnsupportedOperationException}.
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		@Override
		public Node<V> setNode(@NotNull Node<V> node) {
			throw new UnsupportedOperationException("setNode");
		}
	}

	/**
	 * An unmodifiable node delegating to another node.
	 *
	 * @param <V> the type of the value of the node.
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.04.28
	 */
	class UnmodifiableNode<V> extends AbstractNode<V> implements Serializable {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 624657152304453632L;

		/**
		 * The node wrapped by this.
		 *
		 * @since 0.0.1 ~2021.04.27
		 */
		@NotNull
		private final Node<V> node;

		/**
		 * A lazily initialized unmodifiable link set delegating to the link set of {@link
		 * #node}.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		private transient Set<Link<V>> linkSet;

		/**
		 * Construct a new unmodifiable node delegating to the given {@code node}.
		 *
		 * @param node the node to be wrapped by the constructed node.
		 * @throws NullPointerException if the given {@code node} is null.
		 * @since 0.0.1 ~2021.04.27
		 */
		public UnmodifiableNode(@NotNull Node<V> node) {
			Objects.requireNonNull(node, "node");
			this.node = node;
		}

		@Override
		public int hashCode() {
			return this.node.hashCode();
		}

		@NotNull
		@Override
		public String toString() {
			return this.node.toString();
		}

		@Nullable
		@Override
		public V get() {
			return this.node.get();
		}

		@Nullable
		@Override
		public V set(@Nullable V value) {
			throw new UnsupportedOperationException("set");
		}

		@NotNull
		@Override
		public Set<Link<V>> linkSet() {
			if (this.linkSet == null)
				this.linkSet = new AbstractSet<Link<V>>() {
					@Override
					public boolean isEmpty() {
						return UnmodifiableNode.this.node.isEmpty();
					}

					@Override
					public Iterator<Link<V>> iterator() {
						Iterator<Link<V>> i = UnmodifiableNode.this.node.linkSet().iterator();
						return new Iterator<Link<V>>() {
							@Override
							public boolean hasNext() {
								return i.hasNext();
							}

							@Override
							public Link<V> next() {
								return Nodes.unmodifiableLink(i.next());
							}
						};
					}

					@Override
					public int size() {
						return UnmodifiableNode.this.node.size();
					}
				};

			//noinspection AssignmentOrReturnOfFieldWithMutableType
			return this.linkSet;
		}
	}

	//singleton

	/**
	 * Construct a new node that has only the given {@code link} and always refuse to
	 * change it.
	 *
	 * @param link the only link of the returned node.
	 * @param <V>  the type of the value of the node.
	 * @return a new singleton node.
	 * @throws NullPointerException          if the given {@code link} is null.
	 * @throws UnsupportedOperationException if current node of the given {@code link}
	 *                                       refused to remove the given {@code link}.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@Contract(value = "_->new", mutates = "param")
	static <V> Node<V> singletonNode(@NotNull Link<V> link) {
		return new SingletonNode<>(link);
	}

	/**
	 * A node that has a single unchangeable relationship (link). But, with a changeable
	 * value.
	 *
	 * @param <V> the type of the value of the node.
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.04.28
	 */
	class SingletonNode<V> extends AbstractNode<V> implements Serializable {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 2512099034526153652L;

		/**
		 * The single link this node has.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@NotNull
		private Link<V> link;
		/**
		 * The value of this node.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		private V value;

		/**
		 * A lazily initialized singleton set containing {@link #link}.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		@Nullable
		private transient Set<Link<V>> linkSet;

		/**
		 * An internal constructor to be used by links in this class to skip initiation
		 * part.
		 *
		 * @since 0.0.1 ~2021.04.28
		 */
		protected SingletonNode() {
			//noinspection ConstantConditions
			this.link = null;
		}

		/**
		 * Construct a new singleton node with its single link being the given {@code
		 * link}.
		 *
		 * @param link the single link of this node.
		 * @throws NullPointerException if the given {@code link} is null.
		 * @since 0.0.1 ~2021.04.28
		 */
		public SingletonNode(@NotNull Link<V> link) {
			Objects.requireNonNull(link, "link");
			if (link.getNode() != null)
				link.removeNode();
			link.setNode(this);
			this.link = link;
		}

		@Nullable
		@Override
		public V get() {
			return this.value;
		}

		@Nullable
		@Override
		public V set(@Nullable V value) {
			V v = this.value;
			this.value = value;
			return v;
		}

		@NotNull
		@Override
		public Set<Link<V>> linkSet() {
			if (this.linkSet == null)
				this.linkSet = Collections.singleton(this.link);

			//noinspection AssignmentOrReturnOfFieldWithMutableType
			return this.linkSet;
		}
	}

	//view

	/**
	 * Collect the nodes relative to the given {@code node} with respect to the given
	 * {@code key} at this moment in a new mutable {@link LinkedList linked list}.
	 *
	 * @param key  the key to be followed.
	 * @param node the node to collect its relatives.
	 * @param <V>  the type of the value of the node.
	 * @return a linked list containing the current relatives of the given {@code node}
	 * 		with respect to the given {@code key}.
	 * @throws NullPointerException if the given {@code key} or {@code node} is null.
	 * @since 0.0.4 ~2021.05.03
	 */
	@Contract(value = "_,_->new", pure = true)
	static <V> LinkedList<Node<V>> collect(@NotNull Key key, @NotNull Node<V> node) {
		Deque deque = Nodes.asDeque(key, node);
		return new LinkedList<>(deque);
	}

	/**
	 * Return a deque view of the relatives of the given {@code node} with respect to the
	 * given {@code key}.
	 * <br>
	 * The returned deque will not allow removing the given {@code node}.
	 * <br>
	 * If the given {@code node} indirectly relate to itself. Then, the deque will have a
	 * reserved view. Since the last node will be the node before the given {@code node}
	 * and the first node will be the node after it.
	 *
	 * @param key  the key the returned deque will be following.
	 * @param node the sole node of the returned deque.
	 * @param <V>  the type of the value of the node.
	 * @return a deque view of the relatives of the given {@code node} with respect to the
	 * 		given {@code key}.
	 * @throws NullPointerException if the given {@code key} or {@code node} is null.
	 * @since 0.0.4 ~2021.05.03
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	static <V> Deque<Node<V>> asDeque(@NotNull Key key, @NotNull Node<V> node) {
		return new NodeDeque<>(key, node);
	}

	/**
	 * A deque view of the relatives of a specific node with respect to a specific key.
	 * <div style="padding: 10px">
	 *     <h3>Forbidden Emptiness</h3>
	 *     The sole node was explicitly made unchangeable. To make the sole node fixed and
	 *     make the behaviour of the deque more predictable. Predictable not only means to
	 *     those who control the nodes using this deque, but for those who are directly
	 *     processing the nodes outside this deque. When they are both directly accessing the
	 *     nodes and using the deque at the same time. If the deque was sole-node-flexible,
	 *     the deque might change its sole node multiple times without the user noticing and
	 *     with that the user will be frustrated about that behaviour. So, because of that,
	 *     this deque implementation was built to have a fixed sole node therefore a fixed
	 *     point of view.
	 * </div>
	 * <div style="padding: 10px">
	 *     <h3>Special Punishment Exceptions</h3>
	 *     This interface follows the interface {@link Deque} on everything except that it
	 *     uses {@link UnsupportedOperationException} to indicate that an attempt of removing
	 *     the sole node occurred. Since, the interface {@link Deque} does not specify any
	 *     specified exception for such a thing, the overall specification of the interface
	 *     {@link Collection} was taken.
	 * </div>
	 * <div style="padding: 10px">
	 *     <h3>To Infinity and Beyond</h3>
	 *     If the sole node is directly or indirectly relating to itself with respect to the
	 *     key. The deque must retain that relation.
	 * </div>
	 *
	 * @param <V> the type of the value of the nodes.
	 * @author LSafer
	 * @version 0.0.4
	 * @since 0.0.4 ~2021.04.29
	 */
	class NodeDeque<V> extends AbstractQueue<Node<V>> implements Deque<Node<V>> {
		/**
		 * The key of the chain the {@link #node} is in and this deque is following.
		 *
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		private final Key key;
		/**
		 * The sole node of this deque.
		 *
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		private final Node<V> node;

		/**
		 * Construct a new deque view of the relatives of the given {@code node} with
		 * respect to the given {@code key}.
		 *
		 * @param key  the key the constructed deque will follow.
		 * @param node the sole node for the constructed deque.
		 * @throws NullPointerException if the given {@code key} or {@code node} is null.
		 * @since 0.0.4 ~2021.05.02
		 */
		public NodeDeque(@NotNull Key key, @NotNull Node<V> node) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(node, "node");
			this.key = key;
			this.node = node;
		}

		//object

		@Override
		public String toString() {
			return this.key.opposite() + " to " + this.key + " of " + this.node;
		}

		//query

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will just throw an {@link
		 *        UnsupportedOperationException}. Since, the sole node must not be removed.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(value = "->fail", mutates = "this")
		@Override
		public void clear() {
			throw new UnsupportedOperationException("clear");
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will count the nodes from the sole node until
		 * 		reaching the last node. Then, count back from the sole node until reaching
		 * 		the first node. If the sole node was examined while counting forward, the
		 * 		counting will stop and the count number will be declared as the size of this
		 * 		deque. Since, the method already examined all the nodes and if the method
		 * 		continued counting it will examine just previously counted nodes.
		 * @since 0.0.4 ~2021.05.01
		 */
		@Range(from = 1, to = Integer.MAX_VALUE)
		@Contract(pure = true)
		@Override
		public int size() {
			Key key = this.key;
			Node<V> node = this.node;

			int size = 1;

			//forward
			Node<V> next = node;
			while ((next = next.getNode(key)) != null)
				if (next == node)
					//infinite loop detected
					return size;
				else
					size++;

			Key opposite = key.opposite();

			//backwards
			Node<V> prev = node;
			while ((prev = prev.getNode(opposite)) != null)
				//no need for infinite loop check
				size++;

			return size;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will always return {@code false}.
		 * @since 0.0.4 ~2021.05.01
		 */
		@Contract(value = "->false", pure = true)
		@Override
		public boolean isEmpty() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will check if the given {@code object} is a
		 * 		node or not. If not, return {@code false}. If so, loop forward from the sole
		 * 		node to the last node (return {@code false} if sole node reached) and if the
		 * 		given node is met, return {@code true}. Otherwise, do the same thing but
		 * 		backwards from the sole node to the first node. If the give node was not met,
		 * 		return {@code false}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(value = "null->false", pure = true)
		@Override
		public boolean contains(@Nullable Object object) {
			if (object instanceof Node) {
				Key key = this.key;
				Key opposite = this.key.opposite();
				Node<V> sole = this.node;

				if (object == sole)
					return true;

				Node<V> next = sole;
				while ((next = next.getNode(key)) != null)
					if (next == object)
						return true;
					else if (next == sole)
						return false;

				Node<V> prev = sole;
				while ((prev = prev.getNode(opposite)) != null)
					if (prev == object)
						return true;
					else if (prev == sole)
						//concurrent extra guard
						return false;
			}

			return false;
		}

		//remove/poll

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation will loop forward from the sole node to the
		 * 		node with either no node or the sole node after it. If no node was after that
		 * 		node, then that node will simply be removed. If the node after that node was
		 * 		the sole node, then the sole node will be linked to the node before that node
		 * 		(retaining the infinity effect).
		 * @since 0.0.4 ~2021.05.01
		 */
		@SuppressWarnings("DuplicatedCode")
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> removeLast() {
			Key key = this.key;
			Node<V> sole = this.node;

			//forward
			Node<V> last = sole;
			Node<V> prev = null;
			while (true) {
				Node<V> next = last.getNode(key);

				if (next == null) {
					if (last == sole)
						//...[S] => throw
						throw new UnsupportedOperationException("sole");

					//...[S]...[X] => ...[S]...[X-1]
					prev.remove(key);
					return last;
				}
				if (next == sole) {
					if (last == sole)
						//~[S]~ => throw
						throw new UnsupportedOperationException("sole");

					//~[S]...[X]~ => ~[S]...[X-1]~
					prev.putNode(key, sole);
					return last;
				}

				//shift forward
				prev = last;
				last = next;
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation will loop backwards from the sole node to the
		 * 		node with either no node or the sole node before it. If no node was before
		 * 		that node, then that node will simply be removed. If the node before that
		 * 		node was the sole node, then the sole node will be linked to the node after
		 * 		that node (retaining the infinity effect).
		 * @since 0.0.4 ~2021.05.01
		 */
		@SuppressWarnings("DuplicatedCode")
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> removeFirst() {
			Key opposite = this.key.opposite();
			Node<V> sole = this.node;

			//backward
			Node<V> first = sole;
			Node<V> next = null;
			while (true) {
				Node<V> prev = first.getNode(opposite);

				if (prev == null) {
					if (first == sole)
						//[S]... => throw
						throw new UnsupportedOperationException("sole");

					//[X]...[S]... => [X+1]...[S]...
					next.remove(opposite);
					return first;
				}
				if (prev == sole) {
					if (first == sole)
						//~[S]~ => throw
						throw new UnsupportedOperationException("sole");

					//~[X]...[S]~ => ~[X+1]...[S]~
					next.putNode(opposite, sole);
					return first;
				}

				//shift backward
				next = first;
				first = prev;
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation just delegates to {@link #removeLast()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> pollLast() {
			return this.removeLast();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation just delegates to {@link #removeFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> pollFirst() {
			return this.removeFirst();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation delegates to {@link #removeFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> remove() {
			return this.removeFirst();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation delegates to {@link #removeFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> poll() {
			return this.removeFirst();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @throws UnsupportedOperationException if this deque has only its sole node.
		 * @implSpec this implementation delegates to {@link #removeFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(mutates = "this")
		@Override
		public Node<V> pop() {
			return this.removeFirst();
		}

		//add/offer

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will loop forward until reaching a node with
		 * 		either no node or the sole node after it. If so, then the given {@code node}
		 * 		will be linked after that node. Additionally, if that reached node has the
		 * 		sole node after it then the sole node will be linked after the given {@code
		 * 		node}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@SuppressWarnings("DuplicatedCode")
		@Contract(mutates = "this,param")
		@Override
		public void addLast(@NotNull Node<V> node) {
			Objects.requireNonNull(node, "node");
			Key key = this.key;
			Node<V> sole = this.node;

			//forward
			Node<V> last = sole;
			while (true) {
				Node<V> next = last.getNode(key);

				if (next == null) {
					//...[S]...[X] => ...[S}...[X]-[node]
					last.putNode(key, node);
					return;
				}
				if (next == sole) {
					//~[S]...[X]~ => ~[S]...[X]-[node]~
					last.putNode(key, node);
					node.putNode(key, sole);
					return;
				}

				//shift forward
				last = next;
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will loop backward until reaching a node with
		 * 		either no node or the sole node before it. If so, then the given {@code node}
		 * 		will be linked before that node. Additionally, if that reached node has the
		 * 		sole node before it then the sole node will be linked before the given {@code
		 * 		node}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@SuppressWarnings("DuplicatedCode")
		@Contract(mutates = "this,param")
		@Override
		public void addFirst(@NotNull Node<V> node) {
			Objects.requireNonNull(node, "node");
			Key opposite = this.key.opposite();
			Node<V> sole = this.node;

			//backwards
			Node<V> first = sole;
			while (true) {
				Node<V> prev = first.getNode(opposite);

				if (prev == null) {
					//[X]...[S] => [node]-[X]...[S}
					first.putNode(opposite, node);
					return;
				}
				if (prev == sole) {
					//~[X]...[S]~ => ~[node]-[X]...[S]~
					first.putNode(opposite, node);
					node.putNode(opposite, sole);
					return;
				}

				//shift backwards
				first = prev;
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation just delegates to {@link #addLast(Node)}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(value = "_->true", mutates = "this,param")
		@Override
		public boolean offerLast(@NotNull Node<V> node) {
			this.addLast(node);
			return true;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation just delegates to {@link #addFirst(Node)}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(value = "_->true", mutates = "this,param")
		@Override
		public boolean offerFirst(@NotNull Node<V> node) {
			this.addFirst(node);
			return true;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation delegates to {@link #addLast(Node)}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(value = "_->true", mutates = "this,param")
		@Override
		public boolean add(@NotNull Node<V> node) {
			this.addLast(node);
			return true;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation delegates to {@link #addLast(Node)}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(value = "_->true", mutates = "this,param")
		@Override
		public boolean offer(@NotNull Node<V> node) {
			this.addLast(node);
			return true;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation delegates to {@link #addFirst(Node)}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@Contract(mutates = "this,param")
		@Override
		public void push(@NotNull Node<V> node) {
			this.addFirst(node);
		}

		//get/peek

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will loop forward until reaching a node with
		 * 		either no node or the sole node after it then return that node.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		public Node<V> getLast() {
			Key key = this.key;
			Node<V> sole = this.node;

			//forward
			Node<V> last = sole;
			while (true) {
				Node<V> next = last.getNode(key);

				if (next == null || next == sole)
					return last;

				last = next;
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will loop backward until reaching a node with
		 * 		either no node or the sole node before it then return that node.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		public Node<V> getFirst() {
			Key opposite = this.key.opposite();
			Node<V> sole = this.node;

			//backward
			Node<V> first = sole;
			while (true) {
				Node<V> prev = first.getNode(opposite);

				if (prev == null || prev == sole)
					return first;

				first = prev;
			}
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation just delegates to {@link #getLast()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		public Node<V> peekLast() {
			return this.getLast();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation just delegates to {@link #getFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		public Node<V> peekFirst() {
			return this.getFirst();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation delegates to {@link #getFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		public Node<V> peek() {
			return this.getFirst();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation delegates to {@link #getFirst()}.
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		public Node<V> element() {
			return this.getFirst();
		}

		//remove

		@SuppressWarnings({"DuplicatedCode", "OverlyComplexMethod", "OverlyLongMethod"})
		@Contract(value = "null->false;", mutates = "this,param")
		@Override
		public boolean remove(@Nullable Object object) {
			if (object instanceof Node) {
				Key key = this.key;
				Key opposite = key.opposite();
				Node<V> sole = this.node;

				if (object == sole)
					throw new UnsupportedOperationException("sole");

				Node<V> prev;
				Node<V> next;

				//forward
				prev = sole;
				next = sole;
				while ((next = next.getNode(key)) != null) {
					if (next == sole)
						//loop detected
						return false;

					if (next == object) {
						//node found!
						Node<V> after = next.getNode(key);

						if (after == null)
							//end removal
							prev.remove(key);
						else
							//pop removal
							prev.putNode(key, after);

						return true;
					}

					//shift forward
					prev = next;
				}

				//backwards
				prev = sole;
				next = sole;
				while ((prev = prev.getNode(opposite)) != null) {
					if (prev == sole)
						//loop detect (extra guard)
						return false;

					if (prev == object) {
						//node found!
						Node<V> before = next.getNode(opposite);

						if (before == null)
							//end removal
							next.remove(opposite);
						else
							//pop removal
							next.putNode(opposite, before);

						return true;
					}

					//shift backward
					next = prev;
				}
			}

			return false;
		}

		@Contract(value = "null->false;", mutates = "this,param")
		@Override
		public boolean removeLastOccurrence(@Nullable Object object) {
			return this.remove(object);
		}

		@Contract(value = "null->false", mutates = "this,param")
		@Override
		public boolean removeFirstOccurrence(@Nullable Object object) {
			return this.remove(object);
		}

		//iterator

		@NotNull
		@Contract(pure = true)
		@Override
		public Iterator<Node<V>> iterator() {
			Key key = this.key;
			Node<V> sole = this.node;
			Node<V> first = this.getFirst();
			return new NodeIterator<>(key, sole, first);
		}

		@NotNull
		@Contract(pure = true)
		@Override
		public Iterator<Node<V>> descendingIterator() {
			Key opposite = this.key.opposite();
			Node<V> sole = this.node;
			Node<V> last = this.getLast();
			return new NodeIterator<>(opposite, sole, last);
		}
	}

	/**
	 * An iterator iterating over the nodes after a specific node with respect to a
	 * specific key.
	 *
	 * @param <V> the type of the value of the nodes.
	 * @since 0.0.4 ~2021.05.02
	 */
	class NodeIterator<V> implements Iterator<Node<V>> {
		/**
		 * The key of the chain the {@link #node} is in and this iterator is following.
		 *
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		private final Key key;
		/**
		 * The sole node of the collection. Must not be removed. When reached, a loop
		 * occurred.
		 *
		 * @since 0.0.4 ~2021.05.02
		 */
		@NotNull
		private final Node<V> node;
		/**
		 * The next node to be returned by {@link #next}. (null: no next)
		 *
		 * @since 0.0.4 ~2021.05.02
		 */
		@Nullable
		private Node<V> next;
		/**
		 * The last node returned by {@link #next()}.
		 *
		 * @since 0.0.4 ~2021.05.02
		 */
		@Nullable
		private Node<V> previous;

		/**
		 * Construct a new iterator iterating over the nodes after the given {@code node}
		 * with respect to the given {@code key}.
		 *
		 * @param key   the key the constructed iterator will follow.
		 * @param node  the sole node for the constructed iterator.
		 * @param first the first node to iterator from.
		 * @throws NullPointerException if the given {@code key} or {@code node} or {@code
		 *                              first} is null.
		 * @since 0.0.4 ~2021.05.02
		 */
		public NodeIterator(@NotNull Key key, @NotNull Node<V> node, @NotNull Node<V> first) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(node, "node");
			Objects.requireNonNull(first, "first");
			this.key = key;
			this.node = node;
			this.next = first;
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public Node<V> next() {
			Key key = this.key;
			Node<V> sole = this.node;
			Node<V> next = this.next;

			if (next == null)
				//end reached
				throw new NoSuchElementException("next");

			//the sole is always the last item.
			this.next = next == sole ? null : next.getNode(key);
			this.previous = next;
			return next;
		}

		@Override
		public void remove() {
			Key key = this.key;
			Node<V> sole = this.node;
			Node<V> remove = this.previous;

			if (remove == null)
				throw new IllegalStateException("remove");
			if (remove == sole)
				throw new UnsupportedOperationException("sole");

			Node<V> prev = remove.getNode(key);
			Node<V> next = remove.getNode(key);

			if (prev != null)
				if (next == null)
					// ...[prev]-[remove] => ...[prev]
					prev.remove(key);
				else
					// ...[prev]-[remove]-[next]... => ...[prev]-[next]...
					prev.putNode(key, next);
			else if (next != null)
				// [remove]-[next]... => [next]...
				remove.remove(key);

			this.previous = null;
		}
	}
}
//
//	/**
//	 * Return the most node to the given {@code key} relative to the given {@code node}.
//	 * It works by recursively getting the tail of the node relative to the given {@code
//	 * node} with the given {@code key} until reaching a node with no node relative to it
//	 * with the given {@code key}. The tail of such node is itself and the tail of the
//	 * nodes previous to it is it itself.
//	 *
//	 * @param key  the key of where to get the tail.
//	 * @param node the node to get its tail.
//	 * @param <V>  the type of the value of the node.
//	 * @return the tail node to the given {@code node} with respect to the given {@code
//	 * 		key}.
//	 * @throws NullPointerException     if the given {@code key} or {@code node} is null.
//	 * @throws IllegalArgumentException when a node is relative to itself (direct or
//	 *                                  indirect relation) with respect to the given
//	 *                                  {@code key}.
//	 * @since 0.0.3 ~2021.04.28
//	 */
//	@NotNull
//	@Contract(pure = true)
//	static <V> Node<V> tail(@NotNull Key key, @NotNull Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		Node<V> next = node.getNode(key);
//		if (next == null)
//			// `node` is the tail of itself
//			return node;
//		else
//			//noinspection ErrorNotRethrown
//			try {
//				// the tail of `next` is the tail of `node`
//				return Nodes.tail(key, next);
//			} catch (StackOverflowError | IllegalArgumentException ignore) {
//				// no tail >:(
//				throw new IllegalArgumentException("Tailless Node");
//			}
//	}
//
//	/**
//	 * Return the most node to the opposite of the given {@code key} relative to the given
//	 * {@code node}. It works by getting the tail of the given node with respect to the
//	 * opposite of the given {@code key}.
//	 *
//	 * @param key  the key of where to get the tail.
//	 * @param node the node to get its tail.
//	 * @param <V>  the type of the value of the node.
//	 * @return the tail node to the given {@code node} with respect to the given {@code
//	 * 		key}.
//	 * @throws NullPointerException     if the given {@code key} or {@code node} is null.
//	 * @throws IllegalArgumentException when a node is relative to itself (direct or
//	 *                                  indirect relation) with respect to the given
//	 *                                  {@code key}.
//	 * @since 0.0.3 ~2021.04.28
//	 */
//	@NotNull
//	@Contract(pure = true)
//	static <V> Node<V> head(@NotNull Key key, @NotNull Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		//the head is the opposite of the tail
//		try {
//			return Nodes.tail(key.opposite(), node);
//		} catch (IllegalArgumentException ignore) {
//			throw new IllegalArgumentException("Headless Node");
//		}
//	}
//
//	/**
//	 * Construct a new key.
//	 *
//	 * @return a new key.
//	 * @since 0.0.1 ~2021.04.28
//	 */
//	@NotNull
//	@Contract(value = "->new", pure = true)
//	static Key key() {
//		return new SimpleKey();
//	}
//
//	/**
//	 * Construct a new key.
//	 *
//	 * @param toString the toString of the constructed key.
//	 * @return a new key as specified above.
//	 * @throws NullPointerException if the given {@code toString} is null.
//	 * @since 0.0.1 ~2021.04.28
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	static Key key(@NotNull String toString) {
//		return new SimpleKey(toString);
//	}

//	static <V> Iterator<Node<V>> iterator(Key key, Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		Iterator<Node<V>> i = Nodes.listIterator(key, node);
//		return new Iterator<Node<V>>() {
//			@Override
//			public boolean hasNext() {
//				return i.hasNext();
//			}
//
//			@Override
//			public Node<V> next() {
//				return i.next();
//			}
//
//			@Override
//			public void remove() {
//				i.remove();
//			}
//		};
//	}
//
//	static <V> ListIterator<Node<V>> listIterator(Key key, Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		Key opposite = key.opposite();
//		return new ListIterator<Node<V>>() {
//			/**
//			 * The current node. This node is the next node if {@link #next} is true and
//			 * the previous node otherwise.
//			 *
//			 * @since 0.0.3 ~2021.04.29
//			 */
//			@Nullable
//			private Node<V> n = node;
//			/**
//			 * True, if {@link #n} is the next node to be returned.
//			 *
//			 * @since 0.0.3 ~2021.04.29
//			 */
//			private boolean next = true;
//			/**
//			 * True, if {@link #remove()} and {@link #set(Node)} can be performed.
//			 *
//			 * @since 0.0.3 ~2021.04.29
//			 */
//			private boolean returned;
//
//			@Override
//			public boolean hasNext() {
//				Node<V> n = this.n;
//				//not empty and either `n` is next or `n` has next
//				return n != null &&
//					   (this.next || n.getNode(key) != null);
//			}
//
//			@Override
//			public boolean hasPrevious() {
//				Node<V> n = this.n;
//				//not empty and either `n` is previous or `n` has previous
//				return n != null &&
//					   (!this.next || this.n.getNode(opposite) != null);
//			}
//
//			@Override
//			public Node<V> next() {
//				Node<V> n = this.n;
//
//				if (n == null)
//					//empty
//					throw new NoSuchElementException("next");
//
//				//`n` is next ? `n` : `n.next`
//				Node<V> next = this.next ? n : n.getNode(key);
//
//				if (next == null)
//					//at the end
//					throw new NoSuchElementException("next");
//
//				//set `next` to be previous
//				this.n = next;
//				this.next = false;
//				this.returned = true;
//				return next;
//			}
//
//			@Override
//			public Node<V> previous() {
//				Node<V> n = this.n;
//
//				if (n == null)
//					//empty
//					throw new NoSuchElementException("previous");
//
//				//`n` is previous ? `n` : `n.previous`
//				Node<V> previous = this.next ? n.getNode(opposite) : n;
//
//				if (previous == null)
//					//at the beginning
//					throw new NoSuchElementException("previous");
//
//				//set `previous` to be next
//				this.n = previous;
//				this.next = true;
//				this.returned = true;
//				return previous;
//			}
//
//			@Override
//			public int nextIndex() {
//				Node<V> n = this.n;
//
//				if (n == null)
//					//empty
//					return 0;
//
//				//start from 1 if `n` is previous
//				int i = this.next ? 0 : 1;
//
//				//until reaching the head
//				while (true) {
//					n = n.getNode(opposite);
//
//					if (n == null)
//						//`n` is the head
//						return i;
//
//					i++;
//				}
//			}
//
//			@Override
//			public int previousIndex() {
//				//is not it obvious?! :)
//				return this.nextIndex() - 1;
//			}
//
//			@Override
//			public void remove() {
//				Node<V> n = this.n;
//
//				if (!this.returned || n == null)
//					//nothing to be removed
//					throw new IllegalStateException("remove");
//
//				Node<V> previous = n.getNode(opposite);
//				Node<V> next = n.getNode(key);
//
//				try {
//					//the `removal` operation
//					Nodes.pop(key, n);
//				} catch (IllegalArgumentException e) {
//					throw new UnsupportedOperationException("remove", e);
//				}
//
//				//regardless of what `n` does represent,
//				//`n.previous` is previous and `n.next` is next
//				if (previous != null) {
//					//set `previous` to be previous
//					this.n = previous;
//					this.next = false;
//					this.returned = false;
//				} else if (next != null) {
//					//set `next` to be next
//					this.n = next;
//					this.next = true;
//					this.returned = false;
//				} else {
//					//set `null` to be next
//					this.n = null;
//					this.next = true;
//					this.returned = false;
//				}
//			}
//
//			@Override
//			public void set(@NotNull Node<V> node) {
//				Objects.requireNonNull(node, "node");
//				Node<V> n = this.n;
//
//				if (!this.returned || n == null)
//					//nothing to replace
//					throw new IllegalStateException("set");
//
//				//the `set` operation
//				Nodes.set(key, n, node);
//
//				//set `node` as `n`
//				this.n = node;
//			}
//
//			@Override
//			public void add(@NotNull Node<V> node) {
//				Objects.requireNonNull(node, "node");
//				Node<V> n = this.n;
//
//				if (n == null) {
//					//set `node` to be previous
//					this.n = node;
//					this.next = false;
//					this.returned = false;
//				} else if (this.next) {
//					//add `node` before `n`
//					Nodes.add(opposite, n, node);
//
//					//set `n` to be next
//					this.n = n; //redundant
//					this.next = true; //redundant
//					this.returned = false;
//				} else {
//					//add `node` after `n`
//					Nodes.add(key, n, node);
//
//					//set `node` to be previous
//					this.n = node;
//					this.next = false; //redundant
//					this.returned = false;
//				}
//			}
//		};
//	}
//
//	static <V> Spliterator<Node<V>> spliterator(Key key, Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		return Spliterators.spliteratorUnknownSize(
//				Nodes.iterator(key, node),
//				Spliterator.NONNULL | Spliterator.ORDERED
//		);
//	}
//
//	static <V> Stream<Node<V>> stream(Key key, Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		return StreamSupport.stream(
//				Nodes.spliterator(key, node),
//				false
//		);
//	}
//
//	static <V> Node<V>[] toArray(Key key, Node<V> node) {
//		Objects.requireNonNull(key, "key");
//		Objects.requireNonNull(node, "node");
//		return Nodes.stream(key, node)
//					.toArray(Node[]::new);
//	}

//common utilities

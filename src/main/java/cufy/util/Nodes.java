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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

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
	//op

	/**
	 * Push the head of the given {@code other} node after the given {@code node} with
	 * respect to the given {@code key}. The previous node after the given {@code node}
	 * will be after the tail of the given {@code other} node.
	 * <br>
	 * If the given {@code other} node has no head or no tail, then an {@link
	 * IllegalArgumentException} will be thrown with nothing changed.
	 * <br>
	 * If an exception occurred while putting the {@code other} node after the given
	 * {@code node}, then the exception will fall throw this method with nothing changed.
	 * If an exception occurred while putting the previous node after the given {@code
	 * node} after the tail of the {@code other} node, then the exception will fall throw
	 * this method with the {@code other} node replacing the previous node but the
	 * previous node not put after the {@code other} node.
	 *
	 * @param key   the key to where to push the {@code other} node.
	 * @param node  the node to push the {@code other} node after.
	 * @param other the node to be pushed.
	 * @param <V>   the type of the value of the nodes.
	 * @throws NullPointerException          if the given {@code key} or {@code node} or
	 *                                       {@code other} is null.
	 * @throws IllegalArgumentException      when a node relative to the given {@code
	 *                                       other} is relative to itself (direct or
	 *                                       indirect relation) with respect to the given
	 *                                       {@code key} or its opposite;
	 *                                       <br>
	 *                                       if {@code node}, {@code node.getNext(Key)},
	 *                                       {@code Nodes.head(other)} or {@code
	 *                                       Nodes.tail(other)} rejected a key or a link
	 *                                       required for the operation.
	 * @throws UnsupportedOperationException if {@code node}, {@code node.getNext(key)},
	 *                                       {@code Nodes.head(other)} or {@code
	 *                                       Nodes.tail(other)} refused to perform the
	 *                                       required for the operation.
	 * @since 0.0.3 ~2021.04.28
	 */
	@Contract(mutates = "param2,param3")
	static <V> void push(@NotNull Key key, @NotNull Node<V> node, @NotNull Node<V> other) {
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

	/**
	 * Return the most node to the given {@code key} relative to the given {@code node}.
	 * It works by recursively getting the tail of the node relative to the given {@code
	 * node} with the given {@code key} until reaching a node with no node relative to it
	 * with the given {@code key}. The tail of such node is itself and the tail of the
	 * nodes previous to it is it itself.
	 *
	 * @param key  the key of where to get the tail.
	 * @param node the node to get its tail.
	 * @param <V>  the type of the value of the node.
	 * @return the tail node to the given {@code node} with respect to the given {@code
	 * 		key}.
	 * @throws NullPointerException     if the given {@code key} or {@code node} is null.
	 * @throws IllegalArgumentException when a node is relative to itself (direct or
	 *                                  indirect relation) with respect to the given
	 *                                  {@code key}.
	 * @since 0.0.3 ~2021.04.28
	 */
	@NotNull
	@Contract(pure = true)
	static <V> Node<V> tail(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Node<V> next = node.getNode(key);
		if (next == null)
			// `node` is the tail of itself
			return node;
		else
			//noinspection ErrorNotRethrown
			try {
				// the tail of `next` is the tail of `node`
				return Nodes.tail(key, next);
			} catch (StackOverflowError | IllegalArgumentException ignore) {
				// no tail >:(
				throw new IllegalArgumentException("Tailless Node");
			}
	}

	/**
	 * Return the most node to the opposite of the given {@code key} relative to the given
	 * {@code node}. It works by getting the tail of the given node with respect to the
	 * opposite of the given {@code key}.
	 *
	 * @param key  the key of where to get the tail.
	 * @param node the node to get its tail.
	 * @param <V>  the type of the value of the node.
	 * @return the tail node to the given {@code node} with respect to the given {@code
	 * 		key}.
	 * @throws NullPointerException     if the given {@code key} or {@code node} is null.
	 * @throws IllegalArgumentException when a node is relative to itself (direct or
	 *                                  indirect relation) with respect to the given
	 *                                  {@code key}.
	 * @since 0.0.3 ~2021.04.28
	 */
	@NotNull
	@Contract(pure = true)
	static <V> Node<V> head(@NotNull Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		//the head is the opposite of the tail
		try {
			return Nodes.tail(key.opposite(), node);
		} catch (IllegalArgumentException ignore) {
			throw new IllegalArgumentException("Headless Node");
		}
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
		public boolean equals(@Nullable Object obj) {
			return super.equals(obj);
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

	//misc

	/**
	 * Construct a new key.
	 *
	 * @return a new key.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Key key() {
		return new SimpleKey();
	}

	/**
	 * Construct a new key.
	 *
	 * @param toString the toString of the constructed key.
	 * @return a new key as specified above.
	 * @throws NullPointerException if the given {@code toString} is null.
	 * @since 0.0.1 ~2021.04.28
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Key key(@NotNull String toString) {
		return new SimpleKey(toString);
	}

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
}

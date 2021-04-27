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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This class provides a skeletal implementation of the {@link Node} interface, to
 * minimized the effort required to implement this interface.
 * <br>
 * To Implement an unmodifiable node, the programmer needs only to extend this class and
 * provide an implementation for {@link #get()} and {@link #set(Object)} methods which are
 * accessors for the value of the node additionally the {@link #linkSet()} method, which
 * returns a set-view of the links pointing to the node. Typically, the returned set will,
 * in turn, be implemented atop {@link AbstractSet}. This set should not support the
 * {@link Set#add add} or {@link Set#remove remove} methods, and its iterator should not
 * support the {@code remove} method.
 * <br>
 * To implement a modifiable node, the programmer must additionally override this class's
 * {@link #putLink(Link)}, {@link #putNode(Key, Node)}, {@link #put(Key, V)} methods
 * (which otherwise throws an {@link UnsupportedOperationException}), and the iterator
 * returned by {@code linkSet().iterator()} must additionally implement its {@code remove}
 * method.
 * <br>
 * The programmer should generally provide a void (no argument) constructor, as per the
 * recommendation in the {@link Node} interface specification.
 * <br>
 * The documentation for each non-abstract method in this class describes its
 * implementation in detail. Each of these methods may be overridden if the node being
 * implemented admits a more efficient implementation.
 *
 * @param <V> the type of the value of this node.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.04.22
 */
public abstract class AbstractNode<V> implements Node<V> {
	/**
	 * A lazily initialized nodes collection view accessing the nodes related to this
	 * node.
	 *
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	protected Collection<Node<V>> nodes;
	/**
	 * A lazily initialized key set view accessing the keys of the links pointing to this
	 * node.
	 *
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	protected Set<Key> keySet;
	/**
	 * A lazily initialized nodes collection view accessing the values of the nodes
	 * related to this node.
	 *
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	protected Collection<V> values;
	/**
	 * A lazily initialized key set view accessing the entries of this node.
	 *
	 * @since 0.0.1 ~2021.04.27
	 */
	@Nullable
	protected Set<Entry<V>> entrySet;

	// Object

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns {@code true}, if the given {@code object}
	 * 		is {@code this} or returns {@code false}, otherwise.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Override
	public boolean equals(@Nullable Object object) {
		return object == this;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns the {@code XOR} of the hash code of the
	 * 		value returned from {@code value()} and the size of the set returned from {@code
	 * 		linkSet()}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.get()) ^
			   this.linkSet().size();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns the value of this node inside two curly
	 * 		brackets started by a colon. Like, {@code "{:" + this.getValue() + "}"}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Override
	public String toString() {
		return "{:" + this.get() + "}";
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec returns a shallow copy of this map. The views are not copied.
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Override
	protected Object clone() throws CloneNotSupportedException {
		AbstractNode<?> clone = (AbstractNode<?>) super.clone();
		clone.keySet = null;
		clone.nodes = null;
		return clone;
	}

	// Links

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec This implementation iterates over {@code linkSet()} searching for the
	 * 		given {@code link}. If such link is found, {@code true} is returned. If the
	 * 		iteration terminates without finding such a link, {@code false} is returned. Note
	 * 		that this implementation requires linear time in the size of the node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Override
	public boolean containsLink(@NotNull Link<V> link) {
		Objects.requireNonNull(link, "link");
		for (Link<V> l : this.linkSet())
			if (l == link)
				return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation iterates over {@code linkSet()} searching for a
	 * 		link with the opposite of the given {@code key}. If such link is found, the found
	 * 		link will be returned. If the iteration terminates without finding such a link,
	 *        {@code null} is returned. Note that this implementation requires linear time in
	 * 		the size of this node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public Link<V> getLink(@NotNull Key key) {
		Objects.requireNonNull(key, "key");
		for (Link<V> l : this.linkSet())
			if (l.getKey().equals(key))
				return l;
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation iterates over {@code linkSet()} searching for the
	 * 		given {@code link}. If the link was found, the link with be removed from the
	 * 		collection (and this node) with the iterator's {@code remove} operation, and
	 *        {@code true} will be returned. If the iteration terminates without finding such a
	 * 		link, {@code false} is returned. Note that this implementation requires linear
	 * 		time in the size of the node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Override
	public boolean removeLink(@NotNull Link<V> link) {
		Objects.requireNonNull(link, "link");
		Iterator<Link<V>> i = this.linkSet().iterator();
		while (i.hasNext()) {
			Link<V> l = i.next();

			if (l == link) {
				i.remove();
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation always throws an {@link UnsupportedOperationException}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public Link<V> putLink(@NotNull Link<V> link) {
		throw new UnsupportedOperationException("put");
	}

	@NotNull
	@Override
	public abstract Set<Link<V>> linkSet();

	// Nodes

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec This implementation iterates over {@code linkSet()} searching for a
	 * 		link with its opposite pointing to the given {@code node}. If such link is found,
	 *        {@code true} is returned. If the iteration terminates without finding such a
	 * 		link, {@code false} is returned. Note that this implementation requires linear
	 * 		time in the size of the node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Override
	public boolean containsNode(@NotNull Node<V> node) {
		Objects.requireNonNull(node, "node");
		for (Link<V> l : this.linkSet())
			if (l.getOpposite().getNode() == node)
				return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation iterates over {@code linkSet()} searching for a
	 * 		link with the opposite of the given {@code key}. If such link is found, the node
	 * 		of the opposite link of the found link will be returned. If the iteration
	 * 		terminates without finding such a link, {@code null} is returned. Note that this
	 * 		implementation requires linear time in the size of this node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public Node<V> getNode(@NotNull /*opposite*/ Key key) {
		Objects.requireNonNull(key, "key");
		Key opposite = key.opposite();
		for (Link<V> l : this.linkSet())
			if (l.getKey().equals(opposite))
				return l.getOpposite().getNode();
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation iterates over {@code linkSet()} searching for links
	 * 		with its opposite pointing to the given {@code node}. If any link was found, the
	 * 		links will be removed from the collection (and this node) with the iterator's
	 *        {@code remove} operation, and {@code true} will be returned. If the iteration
	 * 		terminates without finding any link, {@code false} is returned. Note that this
	 * 		implementation requires linear time in the size of the node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Override
	public boolean removeNode(@NotNull Node<V> node) {
		Objects.requireNonNull(node, "node");
		Iterator<Link<V>> i = this.linkSet().iterator();
		boolean b = false;
		while (i.hasNext()) {
			Link<V> l = i.next();

			if (l.getOpposite().getNode() == node) {
				i.remove();
				b = true;
			}
		}

		return b;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation always throws an {@link UnsupportedOperationException}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public Node<V> putNode(@NotNull /*opposite*/ Key key, @NotNull Node<V> node) {
		throw new UnsupportedOperationException("put");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns a collection that subclasses {@link
	 *        AbstractCollection}. The subclass's iterator method returns a "wrapper object"
	 * 		over this node's {@code linkSet()} iterator. The {@code size} method delegates to
	 * 		this node's {@code size} method and the {@code contains} method delegates to this
	 * 		node's {@link #containsNode} method.
	 * 		<br>
	 * 		The collection is created the first time this method is called, and returned in
	 * 		response to all subsequent calls. No synchronization is performed, so there is a
	 * 		slight chance that multiple calls to this method will not all return the same
	 * 		set. (which is OK most of the time)
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Override
	public Collection<Node<V>> nodes() {
		if (this.nodes == null)
			this.nodes = new AbstractCollection<Node<V>>() {
				@Override
				public void clear() {
					//noinspection ConstantConditions
					AbstractNode.this.clear();
				}

				@Override
				public boolean contains(Object object) {
					return AbstractNode.this.containsNode((Node<V>) object);
				}

				@Override
				public boolean isEmpty() {
					return AbstractNode.this.isEmpty();
				}

				@Override
				public Iterator<Node<V>> iterator() {
					Iterator<Link<V>> i = AbstractNode.this.linkSet().iterator();
					return new Iterator<Node<V>>() {
						@Override
						public boolean hasNext() {
							return i.hasNext();
						}

						@Override
						public Node<V> next() {
							return i.next().getOpposite().getNode();
						}

						@Override
						public void remove() {
							i.remove();
						}
					};
				}

				@Override
				public int size() {
					return AbstractNode.this.size();
				}
			};

		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.nodes;
	}

	// Map

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation calls {@code linkSet().clear()}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Override
	public void clear() {
		this.linkSet().clear();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns {@code size() == 0}.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns {@code linkSet().size()}.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Override
	public int size() {
		return this.linkSet().size();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec This implementation iterates over {@code linkSet()} searching for a
	 * 		link with the opposite of the given {@code key}. If such link is found, {@code
	 * 		true} is returned. If the iteration terminates without finding such a link,
	 *        {@code false} is returned. Note that this implementation requires linear time in
	 * 		the size of the node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Override
	public boolean containsKey(@NotNull /*opposite*/ Key key) {
		Objects.requireNonNull(key, "key");
		Key opposite = key.opposite();
		for (Link<V> l : this.linkSet())
			if (l.getKey().equals(opposite))
				return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec This implementation iterates over {@code linkSet()} searching for a
	 * 		link with its opposite pointing to a node that has the given {@code value}. If
	 * 		such link is found, {@code true} is returned. If the iteration terminates without
	 * 		finding such a link, {@code false} is returned. Note that this implementation
	 * 		requires linear time in the size of the node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Override
	public boolean containsValue(@Nullable V value) {
		for (Link<V> l : this.linkSet())
			if (Objects.equals(l.getOpposite().getValue(), value))
				return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation iterates over {@code linkSet()} searching for a
	 * 		link with the opposite of the given {@code key}. If such link is found, the value
	 * 		of the node of the opposite link of the found link will be returned. If the
	 * 		iteration terminates without finding such a link, {@code null} is returned. Note
	 * 		that this implementation requires linear time in the size of this node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public V get(@NotNull /*opposite*/ Key key) {
		Objects.requireNonNull(key, "key");
		Key opposite = key.opposite();
		for (Link<V> l : this.linkSet())
			if (l.getKey().equals(opposite))
				return l.getOpposite().getValue();
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation always throws an {@link UnsupportedOperationException}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public V put(@NotNull /*opposite*/ Key key, @Nullable V value) {
		throw new UnsupportedOperationException("put");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation iterates over {@code linkSet()} searching for a
	 * 		link with the opposite of the given {@code key}. If such a link is found, the
	 * 		link with be removed from the collection (and this node) with the iterator's
	 *        {@code remove} operation, and the the value of the node of the opposite of the
	 * 		removed link will be returned. If the iteration terminates without finding such a
	 * 		link, {@code null} is returned. Note that this implementation requires linear
	 * 		time in the size of the node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Override
	public V remove(@NotNull /*opposite*/ Key key) {
		Objects.requireNonNull(key, "key");
		Key opposite = key.opposite();
		Iterator<Link<V>> i = this.linkSet().iterator();
		while (i.hasNext()) {
			Link<V> l = i.next();

			if (l.getKey().equals(opposite)) {
				i.remove();
				return l.getOpposite().getValue();
			}
		}

		return null;
	}

	//	/**
	//	 * {@inheritDoc}
	//	 *
	//	 * @implSpec this implementation iterates over {@code linkSet()} searching for links
	//	 * 		with its opposite pointing to a node with the given {@code value}. If any link
	//	 * 		was found, the links will be removed from the collection (and this node) with the
	//	 * 		iterator's {@code remove} operation, and {@code true} will be returned. If the
	//	 * 		iteration terminates without finding any link, {@code false} is returned. Note
	//	 * 		that this implementation requires linear time in the size of the node.
	//	 * @since 0.0.1 ~2021.04.23
	//	 */
	//	@Override
	//	public boolean removeValue(@Nullable V value) {
	//		Iterator<Link<V>> i = this.linkSet().iterator();
	//		boolean b = false;
	//		if (value == null)
	//			while (i.hasNext()) {
	//				Link<V> l = i.next();
	//
	//				if (l.getOpposite().getValue() == null) {
	//					i.remove();
	//					b = true;
	//				}
	//			}
	//		else
	//			while (i.hasNext()) {
	//				Link<V> l = i.next();
	//
	//				if (value.equals(l.getOpposite().getValue())) {
	//					i.remove();
	//					b = true;
	//				}
	//			}
	//
	//		return b;
	//	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns a set that subclasses {@link AbstractSet}.
	 * 		The subclass's iterator method returns a "wrapper object" over this node's {@code
	 * 		linkSet()} iterator. The {@code size} method delegates to this node's {@code
	 * 		size} method and the {@code contains} method delegates to this node's {@link
	 *        #containsKey} method.
	 * 		<br>
	 * 		The set is created the first time this method is called, and returned in response
	 * 		to all subsequent calls. No synchronization is performed, so there is a slight
	 * 		chance that multiple calls to this method will not all return the same set.
	 * 		(which is OK most of the time)
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Override
	public Set<Key> keySet() {
		if (this.keySet == null)
			this.keySet = new AbstractSet<Key>() {
				@Override
				public void clear() {
					//noinspection ConstantConditions
					AbstractNode.this.clear();
				}

				@Override
				public boolean contains(Object object) {
					return AbstractNode.this.containsKey((Key) object);
				}

				@Override
				public boolean isEmpty() {
					return AbstractNode.this.isEmpty();
				}

				@Override
				public Iterator<Key> iterator() {
					Iterator<Link<V>> i = AbstractNode.this.linkSet().iterator();
					return new Iterator<Key>() {
						@Override
						public boolean hasNext() {
							return i.hasNext();
						}

						@Override
						public Key next() {
							return i.next().getOpposite().getKey();
						}

						@Override
						public void remove() {
							i.remove();
						}
					};
				}

				@Override
				public int size() {
					return AbstractNode.this.size();
				}
			};

		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.keySet;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns a collection that subclasses {@link
	 *        AbstractCollection}. The subclass's iterator method returns a "wrapper object"
	 * 		over this node's {@code linkSet()} iterator. The {@code size} method delegates to
	 * 		this node's {@code size} method and the {@code contains} method delegates to this
	 * 		node's {@link #containsValue} method.
	 * 		<br>
	 * 		The collection is created the first time this method is called, and returned in
	 * 		response to all subsequent calls. No synchronization is performed, so there is a
	 * 		slight chance that multiple calls to this method will not all return the same
	 * 		set. (which is OK most of the time)
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Override
	public Collection<V> values() {
		if (this.values == null)
			this.values = new AbstractCollection<V>() {
				@Override
				public void clear() {
					//noinspection ConstantConditions
					AbstractNode.this.clear();
				}

				@Override
				public boolean contains(Object object) {
					return AbstractNode.this.containsValue((V) object);
				}

				@Override
				public boolean isEmpty() {
					return AbstractNode.this.isEmpty();
				}

				@Override
				public Iterator<V> iterator() {
					Iterator<Link<V>> i = AbstractNode.this.linkSet().iterator();
					return new Iterator<V>() {
						@Override
						public boolean hasNext() {
							return i.hasNext();
						}

						@Override
						public V next() {
							return i.next().getOpposite().getValue();
						}

						@Override
						public void remove() {
							i.remove();
						}
					};
				}

				@Override
				public int size() {
					return AbstractNode.this.size();
				}
			};

		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.values;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @implSpec this implementation returns a set that subclasses {@link AbstractSet}.
	 * 		The subclass's iterator method returns a "wrapper object" over this node's {@code
	 * 		linkSet()} iterator. The iterator's {@code next()} method will return a new
	 *        {@link LinkEntry} wrapping the link returned by the original iterator. The {@code
	 * 		size} method delegates to this node's {@code size} method.
	 * 		<br>
	 * 		The set is created the first time this method is called, and returned in response
	 * 		to all subsequent calls. No synchronization is performed, so there is a slight
	 * 		chance that multiple calls to this method will not all return the same set.
	 * 		(which is OK most of the time)
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Override
	public Set<Entry<V>> entrySet() {
		if (this.entrySet == null)
			this.entrySet = new AbstractSet<Entry<V>>() {
				@Override
				public void clear() {
					//noinspection ConstantConditions
					AbstractNode.this.clear();
				}

				@Override
				public boolean isEmpty() {
					return AbstractNode.this.isEmpty();
				}

				@Override
				public Iterator<Entry<V>> iterator() {
					Iterator<Link<V>> i = AbstractNode.this.linkSet().iterator();
					return new Iterator<Entry<V>>() {
						@Override
						public boolean hasNext() {
							return i.hasNext();
						}

						@Override
						public Entry<V> next() {
							return new LinkEntry<>(i.next().getOpposite());
						}

						@Override
						public void remove() {
							i.remove();
						}
					};
				}

				@Override
				public int size() {
					return AbstractNode.this.size();
				}
			};

		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.entrySet;
	}

	// Classes

	/**
	 * A basic implementation of the interface {@link Key}.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.04.22
	 */
	public static class SimpleKey implements Key {
		/**
		 * The opposite key of this key.
		 *
		 * @since 0.0.1 ~2021.04.22
		 */
		@NotNull
		protected final Key opposite;

		/**
		 * Construct a new key.
		 *
		 * @since 0.0.1 ~2021.04.22
		 */
		public SimpleKey() {
			this.opposite = new SimpleKey(this);
		}

		/**
		 * An internal constructor to construct an opposite key.
		 *
		 * @param opposite the opposite key.
		 * @throws NullPointerException if the given {@code opposite} is null.
		 * @since 0.0.1 ~2021.04.22
		 */
		protected SimpleKey(@NotNull Key opposite) {
			Objects.requireNonNull(opposite, "opposite");
			this.opposite = opposite;
		}

		@NotNull
		@Override
		public Key opposite() {
			return this.opposite;
		}

		@Override
		public boolean equals(@Nullable Object object) {
			return object == this;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(this);
		}

		@NotNull
		@Override
		public String toString() {
			return Integer.toHexString(System.identityHashCode(this));
		}
	}

	/**
	 * A basic implementation of the interface {@link Link}.
	 *
	 * @param <V> the type of the value of the node of the link.
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.04.18
	 */
	public static class SimpleLink<V> implements Link<V> {
		/**
		 * The key of this link.
		 *
		 * @since 0.0.1 ~2021.04.18
		 */
		@NotNull
		protected final Key key;
		/**
		 * The opposite link.
		 *
		 * @since 0.0.1 ~2021.04.18
		 */
		@NotNull
		protected final Link<V> opposite;
		/**
		 * The node this link is pointing to now.
		 *
		 * @since 0.0.1 ~2021.04.18
		 */
		@Nullable
		protected Node<V> node;

		/**
		 * Construct a new link with the given {@code key} and the given {@code opposite}
		 * key.
		 *
		 * @param key the key of the constructed link.
		 * @throws NullPointerException if the given {@code key} is null.
		 * @since 0.0.1 ~2021.04.18
		 */
		public SimpleLink(@NotNull Key key) {
			Objects.requireNonNull(key, "key");
			this.key = key;
			this.opposite = new SimpleLink<>(key.opposite(), this);
		}

		/**
		 * An internal method to construct an opposite link.
		 *
		 * @param key      the key of the constructed link.
		 * @param opposite the opposite link of the constructed link.
		 * @throws NullPointerException if the given {@code key} or {@code opposite} is
		 *                              null.
		 * @since 0.0.1 ~2021.04.18
		 */
		protected SimpleLink(@NotNull Key key, @NotNull Link<V> opposite) {
			Objects.requireNonNull(key, "key");
			Objects.requireNonNull(opposite, "opposite");
			this.key = key;
			this.opposite = opposite;
		}

		@Override
		public boolean equals(@Nullable Object object) {
			return object == this;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(this.key) ^
				   Objects.hashCode(this.node);
		}

		@NotNull
		@Override
		public String toString() {
			return this.key + "=>" + this.node;
		}

		@NotNull
		@Override
		public Key getKey() {
			return this.key;
		}

		@NotNull
		@Override
		public Link<V> getOpposite() {
			return this.opposite;
		}

		@Nullable
		@Override
		public Node<V> getNode() {
			return this.node;
		}

		@Nullable
		@Override
		public Node<V> removeNode() {
			Node<V> n = this.node;

			if (n != null && n.containsLink(this))
				n.removeLink(this);
			else
				this.node = null;

			return n;
		}

		@Nullable
		@Override
		public Node<V> setNode(@NotNull Node<V> node) {
			Objects.requireNonNull(node, "node");
			Node<V> n = this.node;

			if (n != null && n != node || !node.containsLink(this))
				node.putLink(this);
			else
				this.node = node;

			return n;
		}
	}

	/**
	 * An entry wrapping a link.
	 *
	 * @param <V>
	 */
	public static class LinkEntry<V> implements Entry<V> {
		/**
		 * The link this entry is delegating to.
		 *
		 * @since 0.0.1 ~2021.04.27
		 */
		@NotNull
		protected final Link<V> link;

		/**
		 * Construct a new entry delegating to the given {@code link}. The key of the
		 * constructed entry will be the key of the given {@code link} and the value will
		 * be the value of the node the given {@code link} is pointing to.
		 *
		 * @param link the link to delegated to.
		 * @throws NullPointerException if the given {@code link} is null.
		 * @since 0.0.1 ~2021.04.27
		 */
		public LinkEntry(@NotNull Link<V> link) {
			Objects.requireNonNull(link, "link");
			this.link = link;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec if the given {@code object} is this, {@code true} is returned. If
		 * 		the given {@code object} is an {@link Entry} and has an equal key and value,
		 *        {@code true} is returned. Otherwise, {@code false} is returned.
		 * @since 0.0.1 ~2021.04.27
		 */
		@Override
		public boolean equals(@Nullable Object object) {
			if (object == this)
				return true;
			if (object instanceof Entry) {
				Entry entry = (Entry) object;

				return Objects.equals(this.link.getKey(), entry.getKey()) &&
					   Objects.equals(this.link.getValue(), entry.getValue());
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will use {@link Objects#hashCode(Object)} with
		 * 		the key and the value of this entry. Then, returns the value of {@code
		 * 		XOR}-ing the hash codes.
		 * @since 0.0.1 ~2021.04.27
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(this.link.getKey()) ^
				   Objects.hashCode(this.link.getValue());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will return {@code link.getKey() + "=" +
		 * 		link.getValue()}.
		 * @since 0.0.1 ~2021.04.27
		 */
		@NotNull
		@Override
		public String toString() {
			return this.link.getKey() + "=" +
				   this.link.getValue();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will return {@code link.getKey()}.
		 * @since 0.0.1 ~2021.04.27
		 */
		@NotNull
		@Override
		public Key getKey() {
			return this.link.getKey();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @implSpec this implementation will return {@code link.getValue()}.
		 * @since 0.0.1 ~2021.04.27
		 */
		@Nullable
		@Override
		public V getValue() {
			return this.link.getValue();
		}

		/**
		 * {@inheritDoc}
		 *
		 * @return 0.0.1 ~2021.04.27
		 * @implSpec this implementation will return {@code link.set(value)}.
		 */
		@Nullable
		@Override
		public V setValue(@Nullable V value) {
			return this.link.setValue(value);
		}
	}
}

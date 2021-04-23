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
 * A node implementation backed by a {@link HashMap}.
 *
 * @param <V> the type of the value of the node.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.04.18
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class HashNode<V> extends AbstractNode<V> {
	/**
	 * A map containing the links pointing to this node with the respected keys.
	 *
	 * @since 0.0.1 ~2021.04.22
	 */
	@NotNull
	protected final Map<Key, Link<V>> map = new HashMap<>();

	/**
	 * A lazily initialized link set view accessing the links pointing to this node.
	 *
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	protected Set<Link<V>> linkSet;

	/**
	 * The currently set value of this node.
	 *
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	protected V value;

	/**
	 * Construct a new node with its value initialized to {@code null}.
	 *
	 * @since 0.0.1 ~2021.04.24
	 */
	public HashNode() {

	}

	/**
	 * Construct a new node with its value initialized to the given {@code value}.
	 *
	 * @param value the initial value of the constructed node.
	 * @since 0.0.1 ~2021.04.24
	 */
	public HashNode(@Nullable V value) {
		this.value = value;
	}

	// Links

	@Nullable
	@Override
	public Link<V> putLink(@NotNull Link<V> link) {
		Objects.requireNonNull(link, "link");
		Key key = link.getKey();

		//remove the node on `link`
		Node<V> n = link.getNode();

		if (n != null && n != this)
			//only if necessary
			link.removeNode();

		//replace the link in this with `link`
		Link<V> l = this.map.put(key, link);

		if (l != null && l != link)
			//only if necessary
			l.removeNode();

		//set this to `link`
		if (link.getNode() != this)
			//only if necessary
			link.setNode(this);

		return l;
	}

	@NotNull
	@Override
	public Set<Link<V>> linkSet() {
		if (this.linkSet == null) {
			Collection<Link<V>> links = this.map.values();
			this.linkSet = new AbstractSet<Link<V>>() {
				@Override
				public boolean contains(Object object) {
					return HashNode.this.containsLink((Link<V>) object);
				}

				@Override
				public boolean isEmpty() {
					return links.isEmpty();
				}

				@Override
				public Iterator<Link<V>> iterator() {
					Iterator<Link<V>> iterator = links.iterator();
					return new Iterator<Link<V>>() {
						/**
						 * The last returned link from {@link #next()}.
						 *
						 * @since 0.0.1 ~2021.04.22
						 */
						@Nullable
						private Link<V> last;

						@Override
						public boolean hasNext() {
							return iterator.hasNext();
						}

						@Override
						public Link<V> next() {
							return this.last = iterator.next();
						}

						@Override
						public void remove() {
							iterator.remove();

							Link<V> last = this.last;
							this.last = null;

							if (last == null)
								throw new IllegalStateException("remove");
							if (last.getNode() == HashNode.this)
								last.removeNode();
						}
					};
				}

				@Override
				public int size() {
					return links.size();
				}
			};
		}

		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.linkSet;
	}

	// Nodes

	@Nullable
	@Override
	public Node<V> putNode(@NotNull /*opposite*/ Key key, @NotNull Node<V> node) {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(node, "node");
		Key opposite = key.opposite();

		//get the previous link
		Link<V> l = this.map.get(opposite);

		//see if it can be recycled
		if (l != null && l.getOpposite().getNode() == null) {
			//no need to create new link
			node.putLink(l.getOpposite());
			return null;
		}

		//create new link
		Link<V> link = new SimpleLink<>(opposite);

		//add its opposite to `node`
		node.putLink(link.getOpposite());

		//add it to this
		this.map.put(opposite, link);

		//add this to it
		link.setNode(this);

		//remove the previous link
		if (l != null) {
			l.removeNode();
			//the opposite of the previous link has not been touched
			return l.getOpposite().getNode();
		}

		//no previous node to be returned
		return null;
	}

	// Values

	@Nullable
	@Override
	public V putValue(@NotNull /*opposite*/ Key key, @Nullable V value) {
		Objects.requireNonNull(key, "key");
		Key opposite = key.opposite();

		//get the link
		Link<V> l = this.map.get(opposite);

		//see if create new link required
		if (l == null) {
			//create new link
			l = new SimpleLink<>(opposite);

			//add it to this
			this.map.put(opposite, l);

			//add this to it
			l.setNode(this);
		}

		//get the node
		Node<V> n = l.getOpposite().getNode();

		//see if create new node required
		if (n == null) {
			//create new node
			n = new HashNode<>();

			//add the opposite link to `node`
			n.putLink(l.getOpposite());
		}

		//set the value to the node
		return n.setValue(value);
		/*
		No worries about `fail->no changes` contract.
		If the node was created by this node. It is a HashNode so it will accept any value and any link.
		Otherwise, no link nor node will be changed. Since `l` and `n` will not be null anyway!
		*/
	}

	// Value

	@Nullable
	@Override
	public V getValue() {
		return this.value;
	}

	@Nullable
	@Override
	public V setValue(@Nullable V value) {
		V v = this.value;
		this.value = value;
		return v;
	}
}

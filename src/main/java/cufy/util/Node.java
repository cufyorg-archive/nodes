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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Set;

/**
 * A node is a none-centralized data point floating in space and building a structure by
 * relating to other nodes.
 * <br>
 * <div style="padding: 10px">
 *     <h3>Nodes and links</h3>
 *     Nodes uses {@link Link}s as bridge between another. They use {@link Link} like a
 *     disposal item, once they finish from them, they forget them. Unused links are
 *     safely garbage collected because they are not referenced by their previous users.
 *     A node shares a strict relation between it and every link pointing to them. At
 *     standard circumstances (with no cheating) a node have all the links pointing to it
 *     and have no link not pointing to it.
 * </div>
 * <div style="padding: 10px">
 *     <h3>Yuck relationship</h3>
 *     Nodes do not like to directly touch each other. They use links to have a
 *     relationship. So, when a node wants to have a relation or remove a relation with
 *     another node, it deals with the link responsible for that relation (on its side)
 *     and that link will obey it. This way, the node do not have to ask the other node
 *     to accept it or leave it.
 * </div>
 * <div style="padding: 10px">
 *     <h3>Garbage Links</h3>
 *     A node is not allowed to just remove the links it contains without a command from
 *     the user. Even if a link in a node is pointing to nothing in the other side. The
 *     node is not allowed to remove itself from that link (internally).
 * </div>
 * <div style="padding: 10px">
 *     <h3>Opposite Keys</h3>
 *     The Node interface provides the main interface functions that accept keys with a
 *     flipped behaviour. Like, when checking if a key is in a node. The check will be
 *     performed on the opposite of that key. This way, the user will not be confused
 *     by the node's side links and the relatives side of the links. For example, calling
 *     {@code node.containsKey(LEFT)} will check if this node has a node left to it
 *     instead of checking if the node is on the left of another node. You will see a
 *     comment containing {@code opposite} before an opposite key parameter.
 * </div>
 * <div style="padding: 10px">
 *     <h3>Viewing Points</h3>
 *     Aside from the actual data structure (since it is implementation specific) a node
 *     has many viewing points (described below):
 *     <ul>
 *         <li>
 *             <b>Self</b>
 *             The view of the node itself. The value of the node.
 *         </li>
 *         <li>
 *             <b>Keys</b>
 *             An additional viewing point viewing the keys the node has.
 *         </li>
 *         <li>
 *             <b>Links</b>
 *             The most important viewing point viewing the links pointing to the node.
 *         </li>
 *         <li>
 *             <b>Nodes</b>
 *             An additional viewing point viewing the nodes relating to the node.
 *         </li>
 *         <li>
 *             <b>Values</b>
 *             A shortcut viewing point viewing the values of the other nodes relating to the node.
 *         </li>
 *     </ul>
 * </div>
 *
 * @param <V> the type of the value of the node.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.04.18
 */
public interface Node<V> {
	// Object

	/**
	 * Determine if the given {@code object} equals this node.
	 * <br>
	 * An object equals a node if that object is the same reference of that node.
	 *
	 * @param object the object to be checked.
	 * @return true, if the given {@code object} equals this.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(value = "null->false", pure = true)
	@Override
	boolean equals(@Nullable Object object);

	/**
	 * Calculate the hash code of this node.
	 * <br>
	 * Usually, the hash code of a node is the {@code XOR} of the hash code of its value
	 * and the number of links pointing to it.
	 *
	 * @return the hash code of this node.
	 * @since 0.0.1 ~2021.04.20
	 */
	@Contract(pure = true)
	@Override
	int hashCode();

	/**
	 * Return a string representation of this node.
	 * <br>
	 * Usually the string representation of a node mainly contains the string
	 * representation of its value and nothing to do with the links pointing to it.
	 *
	 * @return the string representation of this node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	String toString();

	// Collection

	/**
	 * Remove all the relations of this node.
	 * <br>
	 * The removal will only occur on this node. So, the links will not be removed from
	 * the other nodes. In other words, the other nodes will retain the links pointing to
	 * this node.
	 *
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(mutates = "this")
	void clear();

	/**
	 * Returns {@code true} if this node has no relatives.
	 * <br>
	 * Equivalent to {@code node.linkSet().isEmpty()}
	 *
	 * @return true, if this node has no relatives.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(pure = true)
	boolean isEmpty();

	/**
	 * The number of direct relatives this node has.
	 * <br>
	 * Equivalent to {@code node.linkSet().size()}.
	 *
	 * @return the number of direct relatives to this node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(pure = true)
	int size();

	// Keys

	/**
	 * Check if this node has a link with the given {@code key}.
	 *
	 * @param key the key of the link to be checked.
	 * @return true, if this node has a link with the given {@code key} pointing to it.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(pure = true)
	boolean containsKey(@NotNull /*opposite*/ Key key);

	/**
	 * Remove the link with the opposite of the given {@code key} from this node.
	 *
	 * @param key the opposite key of the link to be removed
	 * @return the link with the opposite of the given {@code key} that has been removed.
	 * 		Or {@code null} if no link was removed.
	 * @throws NullPointerException          if the given {@code key} is null.
	 * @throws UnsupportedOperationException if this node refuses to remove the link with
	 *                                       the opposite of the given {@code key}.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	@Contract(mutates = "this")
	Link<V> removeKey(@NotNull /*opposite*/ Key key);

	/**
	 * A non-null view of the keys of the links pointing to this node.
	 * <br>
	 * The returned set is a reflection of this node. So, any changes to this node will be
	 * reflected in the returned set and vice versa.
	 * <br>
	 * The returned set will only permit {@code non-null} {@link Key} elements. So, any
	 * operation like {@code contains} will fail when {@code null} or a non {@link Key} is
	 * passed to it.
	 * <br>
	 * Additionally the returned set will not support the {@code add} and {@code addAll}
	 * operation. But, the returned set will support the {@code remove} and {@code
	 * removeAll} operations.
	 * <br>
	 * An iterator of the returned set will throw {@link ConcurrentModificationException}
	 * when used after a modification is applied to this node after the creation of that
	 * iterator.
	 *
	 * @return a view of the keys in this node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@NotNull
	@Contract(pure = true)
	Set<Key> keySet();

	// Links

	/**
	 * Check if this node has the given {@code link} (same reference).
	 *
	 * @param link the link to be checked.
	 * @return true, if the given {@code link} is pointing to this node.
	 * @throws NullPointerException if the given {@code link} is null.
	 * @since 0.0.1 ~2021.04.20
	 */
	@Contract(pure = true)
	boolean containsLink(@NotNull Link<V> link);

	/**
	 * Get the link pointing to this node with the opposite of the given {@code key}.
	 *
	 * @param key the opposite key of the link.
	 * @return the link pointing to this node with the opposite of the given {@code key}.
	 * 		Or {@code null} if no link in this node has the opposite of the given {@code
	 * 		key}.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.0.1 ~2021.04.20
	 */
	@Nullable
	@Contract(pure = true)
	Link<V> getLink(@NotNull /*opposite*/ Key key);

	/**
	 * Remove the given {@code link} from this node.
	 * <br>
	 * This method will remove the given {@code link} from this then remove this from the
	 * given {@code link}.
	 * <br>
	 * If this node rejected to remove the given {@code link}. Then, an exception will be
	 * thrown with nothing changed.
	 * <br>
	 * The method might {@link Link#removeNode() remove the node} of the given {@code
	 * link} after doing its work when the given {@code link} is pointing to this. This
	 * behaviour insures strict relations.
	 * <br><br>
	 * Basic Implementation:
	 * <pre>
	 *     K key = link.getKey();
	 *     Link l = this.map.get(key);
	 *     if (l == link) {
	 *          this.map.remove(key);
	 *          if (link.getNode() == this)
	 *               link.removeNode();
	 *          return true;
	 *     }
	 *     return false;
	 * </pre>
	 *
	 * @param link the link to be removed.
	 * @return true, if this node changed due to this method call.
	 * @throws NullPointerException          if the given {@code link} is null.
	 * @throws UnsupportedOperationException if this node refuses to remove the given
	 *                                       {@code link}.
	 * @since 0.0.1 ~2021.04.18
	 */
	@Contract(mutates = "this,param")
	boolean removeLink(@NotNull Link<V> link);

	/**
	 * Put the given {@code link} to point to this node.
	 * <br>
	 * This method will remove the node the given {@code link} is pointing to from the
	 * given {@code link} then remove this node from the current link with the same key as
	 * the given {@code link} from both sides then put the given {@code link} to this node
	 * then set this node to the given {@code link}.
	 * <br>
	 * If the node the given {@code link} is pointing to rejected to remove the given
	 * {@code link} or if this node rejected to put the given {@code link}. Then, an
	 * exception will be thrown with nothing changed.
	 * <br>
	 * The method might {@link Link#removeNode() remove} the node of the given {@code
	 * link} before its work when the given {@code link} is pointing to neither this node
	 * nor {@code null}. Also, it might {@link Link#setNode(Node) set} this node to the
	 * given {@code link} after its work when the given {@code link} is not pointing to
	 * this. This behaviour insures strict relations.
	 * <br><br>
	 * Basic Implementation:
	 * <pre>
	 *     K key = link.getKey();
	 *     Node n = link.getNode();
	 *     if (n != null && n != this)
	 *          //be polite and remove the rival node first
	 *          link.removeNode();
	 *     Link l = this.map.put(key, link);
	 *     if (l != null && l != link)
	 *          //now we can abandon our friend
	 *          l.removeNode();
	 *     if (link.getNode() != this)
	 *          //hey new fiend, glad to meet you
	 *          link.setNode(this);
	 *     return l;
	 * </pre>
	 *
	 * @param link the link to be put.
	 * @return the previous link pointing to this node with the same key as the given
	 *        {@code link}. Or {@code null} if none.
	 * @throws NullPointerException          if the given {@code link} is null.
	 * @throws IllegalArgumentException      if this node rejected the given {@code link};
	 *                                       if this node rejected the key of the given
	 *                                       {@code link}.
	 * @throws UnsupportedOperationException if this node refused to put the given {@code
	 *                                       link}; If current node of the given {@code
	 *                                       link} refused to remove the given {@code
	 *                                       link}.
	 * @since 0.0.1 ~2021.04.18
	 */
	@Nullable
	@Contract(mutates = "this,param")
	Link<V> putLink(@NotNull Link<V> link);

	/**
	 * A non-null view of the links pointing to this node.
	 * <br>
	 * The returned set is a reflection of this node. So, any changes to this node will be
	 * reflected in the returned set and vice versa.
	 * <br>
	 * The returned set will only permit {@code non-null} {@link Link} elements. So, any
	 * operation like {@code contains} will fail when {@code null} or a non {@link Set} is
	 * passed to it.
	 * <br>
	 * Additionally the returned set will not support the {@code add} and {@code addAll}
	 * operation. But, the returned set will support the {@code remove} and {@code
	 * removeAll} operations.
	 * <br>
	 * An iterator of the returned set will throw {@link ConcurrentModificationException}
	 * when used after a modification is applied to this node after the creation of that
	 * iterator.
	 *
	 * @return a view of the links pointing to this node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@NotNull
	@Contract(pure = true)
	Set<Link<V>> linkSet();

	// Nodes

	/**
	 * Check if this node has a relation (link) between it an the given {@code node}.
	 *
	 * @param node the node to be checked.
	 * @return true, if the given {@code node} has a relation to this node.
	 * @throws NullPointerException if the given {@code node} is null.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(pure = true)
	boolean containsNode(@NotNull Node<V> node);

	/**
	 * Get the node relating to this node with the opposite of the given {@code key}.
	 *
	 * @param key the opposite key of the link linking this node and the returned node.
	 * @return the node relating to this node with the opposite of the given {@code key}.
	 * 		Or {@code null} if no such node.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	@Contract(pure = true)
	Node<V> getNode(@NotNull /*opposite*/ Key key);

	/**
	 * Remove all the links pointing to this node and the given {@code node} from this
	 * node.
	 * <br>
	 * The removal will only occur on this node. So, the links will not be removed from
	 * the given {@code node}. In other words, the given {@code node} will retain the
	 * links pointing to this node.
	 *
	 * @param node the node to remove all the links between it and this node.
	 * @return true, if this node changed due to this method call.
	 * @throws NullPointerException          if the given {@code node} is null.
	 * @throws UnsupportedOperationException if this node refuses to remove a link between
	 *                                       it and the given {@code node}.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(mutates = "this,param")
	boolean removeNode(@NotNull Node<V> node);

	/**
	 * Create a new relation between this node and the given {@code node}.
	 * <br>
	 * This method will create a new link (if this contains an unused link then that
	 * unused link WILL be used instead) then add its opposite to the given {@code node}
	 * then remove the previous link with the opposite of the given {@code key} from this
	 * node then add the newly created link to this node.
	 * <br>
	 * If the given {@code node} rejected the newly created link or this node rejected to
	 * have a new relation with the opposite of the given {@code key}. Then, an exception
	 * will be thrown with nothing changed.
	 * <br>
	 * The method might {@link Node#putLink(Link) put} the opposite of the newly created
	 * link to the given {@code node} and {@link Link#removeLink(Link) remove} this node
	 * from the previous link in this node with the opposite of the given {@code key} and
	 * {@link Link#setNode(Node) set} this node to the newly created link.
	 *
	 * @param key  the key of the new relation (its opposite is the side of this node).
	 * @param node the node for this node to have a relation with with the given {@code
	 *             key}.
	 * @return the previous node that has a relation with this node with the opposite of
	 * 		the given {@code key}.
	 * @throws NullPointerException          if the given {@code key} or {@code node} is
	 *                                       null.
	 * @throws IllegalArgumentException      if this node rejected the opposite of the
	 *                                       given {@code key}; if the given {@code node}
	 *                                       rejected the link created by this node; if
	 *                                       the given {@code node} rejected the given
	 *                                       {@code key}.
	 * @throws UnsupportedOperationException if this node refused to have a link with the
	 *                                       opposite of the given {@code key}; if the
	 *                                       given {@code node} refused to have a link
	 *                                       with the given {@code key}.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	@Contract(mutates = "this,param2")
	Node<V> putNode(@NotNull /*opposite*/ Key key, @NotNull Node<V> node);

	/**
	 * A non-null view of the nodes related to this node.
	 * <br>
	 * The returned collection is a reflection of this node. So, any changes to this node
	 * will be reflected in the returned collection and vice versa.
	 * <br>
	 * The returned collection will only permit {@code non-null} {@code Node} elements.
	 * So, any operation like {@code contains} will fail when {@code null} or a non {@link
	 * Node} is passed to it.
	 * <br>
	 * Additionally the returned collection will not support the {@code add} and {@code
	 * addAll} operation. But, the returned collection will support the {@code remove} and
	 * {@code removeAll} operations.
	 * <br>
	 * An iterator of the returned collection will throw {@link
	 * ConcurrentModificationException} when used after a modification is applied to this
	 * node after the creation of that iterator.
	 *
	 * @return a view of the nodes related to this node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@NotNull
	@Contract(pure = true)
	Collection<Node<V>> nodes();

	// Values

	/**
	 * Check if this node has a relation (link) between it and a node with the given
	 * {@code value}.
	 *
	 * @param value the value to be checked.
	 * @return true, if this node has a relation with a node that has the given {@code
	 * 		value}.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Contract(pure = true)
	boolean containsValue(@NotNull V value);

	/**
	 * Get the value of the node relating to this node with the opposite of the given
	 * {@code key}.
	 *
	 * @param key the opposite key of the link linking this node and the node its value to
	 *            be gotten.
	 * @return the value of the node relating to this node with the opposite of the given
	 *        {@code key}. Or {@code null} if no such value. A {@code null} can also indicate
	 * 		that the found node has explicitly set {@code null} as its value.
	 * @throws NullPointerException if the given {@code key} is null.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Contract(pure = true)
	V getValue(@NotNull /*opposite*/ Key key);

	/**
	 * Remove all the links pointing to this node and a node with the given {@code
	 * value}.
	 * <br>
	 * The removal will only occur in this node. So, the links will not be removed from
	 * the nodes with the given {@code value}.
	 *
	 * @param value the value of the nodes to be removed.
	 * @return true, if this node changed due to this method call.
	 * @throws UnsupportedOperationException if this node refuses to remove a link between
	 *                                       it and the nodes with the given {@code
	 *                                       value}.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Contract(mutates = "this")
	boolean removeValue(@Nullable V value);

	/**
	 * Create a new relation of the given {@code key} to a new node with the given {@code
	 * value}.
	 * <br>
	 * This method will create a new node (if this has a relation with a node with the
	 * opposite key then that node WILL be used instead) then set its value to the given
	 * {@code value} thn create a new link (if this contains an unused link then that
	 * unused link WILL be used instead) then add its opposite to the newly created node
	 * then remove the previous link with the opposite of the given {@code key} from this
	 * node then add the newly created link to this node.
	 * <br>
	 * If this node rejected either the opposite of the given {@code key} or the given
	 * {@code value}. Then, an exception will be thrown with nothing changed.
	 *
	 * @param key   the key of the new relation (its opposite is the side of this node).
	 * @param value the initial value of the node that will be created.
	 * @return the value of the previous node that was relating to this node with the
	 * 		opposite of the given {@code key}.
	 * @throws NullPointerException          if the given {@code key} is null; if the
	 *                                       given {@code value} is null and the newly
	 *                                       created node does not support null values.
	 * @throws IllegalArgumentException      if this node rejected the opposite of the
	 *                                       given {@code key}; if the newly created node
	 *                                       rejected the given {@code key}; if the newly
	 *                                       created node rejected the link created by
	 *                                       this node; if the newly created node rejected
	 *                                       the given {@code value}.
	 * @throws ClassCastException            if the given {@code value} is of an
	 *                                       inappropriate type for the newly created
	 *                                       node.
	 * @throws UnsupportedOperationException if this node refused to have a link with the
	 *                                       opposite of the given {@code key}; if the
	 *                                       newly created node refused to have a link
	 *                                       with the given {@code key}; if the newly
	 *                                       created node refused to change its value.
	 * @since 0.0.1 ~2021.04.23
	 */
	@Nullable
	@Contract(mutates = "this")
	V putValue(@NotNull /*opposite*/ Key key, @Nullable V value);

	/**
	 * A nullable view of the values of the nodes related to this node.
	 * <br>
	 * The returned collection is a reflection of this node. So, any changes to this node
	 * will be reflected in the returned collection and vice versa.
	 * <br>
	 * The returned collection will only permit {@code non-null} {@code Node} elements.
	 * So, any operation like {@code contains} will fail when {@code null} or a non {@link
	 * Node} is passed to it.
	 * <br>
	 * Additionally the returned collection will not support the {@code add} and {@code
	 * addAll} operation. But, the returned collection will support the {@code remove} and
	 * {@code removeAll} operations.
	 * <br>
	 * An iterator of the returned collection will throw {@link
	 * ConcurrentModificationException} when used after a modification is applied to this
	 * node after the creation of that iterator.
	 *
	 * @return a view of the values of the related nodes to this node.
	 * @since 0.0.1 ~2021.04.23
	 */
	@NotNull
	@Contract(pure = true)
	Collection<V> values();

	// Value

	/**
	 * Return the value of this node.
	 *
	 * @return the value of this node.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	@Contract(pure = true)
	V getValue();

	/**
	 * Set the value of this node to be the given {@code value}.
	 *
	 * @param value the new value of this node.
	 * @return the previous value of this node.
	 * @throws NullPointerException          if the given {@code value} is null and this
	 *                                       node does not support null value.
	 * @throws IllegalArgumentException      if this node rejected the given {@code
	 *                                       value}.
	 * @throws ClassCastException            if the given {@code value} is of an
	 *                                       inappropriate type for this node.
	 * @throws UnsupportedOperationException if this node refused to change its value.
	 * @since 0.0.1 ~2021.04.22
	 */
	@Nullable
	@Contract(mutates = "this")
	V setValue(@Nullable V value);

	// Interfaces

	/**
	 * A key used in nodes.
	 * <br>
	 * Since nodes has strict relations then their keys has two sides for each end in the
	 * relations of the nodes.
	 *
	 * @author LSafer
	 * @version 0.0.1 ~2021.04.22
	 * @since 0.0.1 ~2021.04.22
	 */
	@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
	interface Key {
		/**
		 * The opposite key of this key.
		 *
		 * @return the opposite key of this key.
		 * @since 0.0.1 ~2021.04.22
		 */
		@NotNull
		@Contract(pure = true)
		Key opposite();

		/**
		 * Determine if the given {@code object} equals this key.
		 * <br>
		 * An object equals a key when that object is the same reference as that key. Or
		 * if that object is a key and have the same value (implementation specific) of
		 * that key and an {@link Object#equals(Object) equal} {@link #opposite()
		 * opposite}.
		 *
		 * @param object the object to be checked.
		 * @return true, if the given {@code object} equals this.
		 * @since 0.0.1 ~2021.04.22
		 */
		@Contract(pure = true)
		@Override
		boolean equals(@Nullable Object object);

		/**
		 * Calculate the hash code of this key.
		 *
		 * @return the hash code of this key.
		 * @since 0.0.1 ~2021.04.22
		 */
		@Contract(pure = true)
		@Override
		int hashCode();

		/**
		 * Return a string representation of this key.
		 *
		 * @return a string representation of this key.
		 * @since 0.0.1 ~2021.04.22
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		String toString();
	}

	/**
	 * A node link is a stateful independent structure that glues two nodes together.
	 * <br>
	 * <div style="padding: 10px">
	 * <h3>A Link and its Opposite</h3>
	 * Links are working in dues. That means, a link have a mirror twin that is exclusive
	 * to it and points to it. In short, a link has an {@link #getOpposite() opposite}
	 * link that has it as its {@link #getOpposite() opposite}.
	 * <br>
	 * So that, the below code is true:
	 * <pre>
	 *     link.{@link #getOpposite()}.{@link #getOpposite()} == link
	 * </pre>
	 * </div>
	 * <div style="padding: 10px">
	 * <h3>Remove and Isolate</h3>
	 * When a link does not link to any node, then the link might be ignored no problem or
	 * used in any other node. Since, once a link is removed from a node then that node
	 * will have nothing to do with that link. So, to remove a link it is enough to remove
	 * one of the nodes it is linking and to completely remove a link it is required to
	 * remove both linked nodes.
	 * </div>
	 * <div style="padding: 10px">
	 * <h3>Links are Polite</h3>
	 * When a link attempts to do an operation, it first asks the node involved in that
	 * operation for approval. Like, when a link is about to remove the node it is
	 * pointing to it must first see if that node has it. If so, then the link asks that
	 * node to remove it (by saying "please mighty node you might remove me Y_Y") and if
	 * that node removes it it will call it back to reattempt the operation. Otherwise, the
	 * link will remove that node since that node does not accept it no more.
	 * </div>
	 *
	 * @param <V> the type of the value of the nodes the link is linking together.
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.04.18
	 */
	interface Link<V> {
		/**
		 * Determine if the given {@code object} equals this link.
		 * <br>
		 * An object equals a link if that object is a link and has an {@link
		 * Object#equals(Object) equal} {@link #getKey() key} and the same {@link
		 * #getNode() node} as that link.
		 *
		 * @param object the object to be checked.
		 * @return true, if the given {@code object} equals this.
		 * @since 0.0.1 ~2021.04.20
		 */
		@Contract(value = "null->false", pure = true)
		@Override
		boolean equals(@Nullable Object object);

		/**
		 * Calculate the hash code of this link.
		 * <br>
		 * Usually, the hash code of a link is the {@code XOR} of the hash code of its key
		 * and the hash code of the node it is pointing to currently.
		 *
		 * @return the hash code of this link.
		 * @since 0.0.1 ~2021.04.20
		 */
		@Contract(pure = true)
		@Override
		int hashCode();

		/**
		 * Return a string representation of this link.
		 * <br>
		 * Usually the string representation of a link contains the string representation
		 * of its key followed by the string representation of the current node it is
		 * pointing to.
		 *
		 * @return a string representation of this link.
		 * @since 0.0.1 ~2021.04.20
		 */
		@NotNull
		@Contract(pure = true)
		@Override
		String toString();

		/**
		 * Returns the key of this link. Two links with the same key must not link the
		 * same {@link #getNode() node}.
		 * <br>
		 * The key of a link must always be the same reference.
		 *
		 * @return the key of this link.
		 * @since 0.0.1 ~2021.04.18
		 */
		@NotNull
		@Contract(pure = true)
		Key getKey();

		/**
		 * Returns the opposite side of this link. The opposite side will always be the
		 * same reference and will always return {@code this} as its opposite.
		 *
		 * @return the opposite link of this link.
		 * @since 0.0.1 ~2021.04.18
		 */
		@NotNull
		@Contract(pure = true)
		Link<V> getOpposite();

		/**
		 * Returns the node this link is holding (pointing to).
		 *
		 * @return the linked node. Or {@code null} if this link is currently pointing to
		 * 		nothing.
		 * @since 0.0.1 ~2021.04.18
		 */
		@Nullable
		@Contract(pure = true)
		Node<V> getNode();

		/**
		 * Remove the node this link is pointing to.
		 * <br>
		 * This method will remove the current node from both sides.
		 * <br>
		 * If the current node rejected to remove this link. Then, an exception will be
		 * thrown with nothing changed.
		 * <br>
		 * The method might {@link Node#removeLink(Link) remove} this link from the
		 * current node instead of doing its work when the current node does have this
		 * link. This behaviour insures strict relations.
		 * <br><br>
		 * Basic Implementation:
		 * <pre>
		 *     Node n = this.node;
		 *     if (n != null && n.hasLink(this))
		 *          n.removeLink(this);
		 *     else
		 *          this.node = null;
		 *     return n;
		 * </pre>
		 *
		 * @return the node this link was pointing to. Or {@code null} if this link was
		 * 		pointing to nothing.
		 * @throws UnsupportedOperationException if the current node refused to remove
		 *                                       this link.
		 * @since 0.0.1 ~2021.04.18
		 */
		@Nullable
		@Contract(mutates = "this")
		Node<V> removeNode();

		/**
		 * Set this link to point to the given {@code node}.
		 * <br>
		 * This method will remove the previous node from both sides then set the given
		 * {@code node} to both sides.
		 * <br>
		 * If the previous node rejected to remove this link or if the given {@code node}
		 * rejected to put this link. Then, an exception will be thrown with nothing
		 * changed.
		 * <br>
		 * The method might {@link Node#putLink(Link) put} this link to the given {@code
		 * node} instead of doing its work when the current node is not the given {@code
		 * node} neither {@code null} or the given {@code node} does have this link. This
		 * behaviour insures strict relations.
		 * <br><br>
		 * Basic Implementation:
		 * <pre>
		 *     Node n = this.node;
		 *     if (n != null && n != node || !node.hasLink(this))
		 *          node.putLink(this);
		 *     else
		 *          this.node = node;
		 *     return n;
		 * </pre>
		 *
		 * @param node the node to be set.
		 * @return the previous node set to this. Or {@code null} if this link was
		 * 		pointing to nothing.
		 * @throws NullPointerException          if the given {@code node} is null.
		 * @throws IllegalArgumentException      if the given {@code node} rejected this
		 *                                       link; if the given {@code node} rejected
		 *                                       the key of this link.
		 * @throws UnsupportedOperationException if the given {@code node} refused to put
		 *                                       this link; If the current node refused to
		 *                                       remove this link.
		 * @since 0.0.1 ~2021.04.18
		 */
		@Nullable
		@Contract(mutates = "this,param")
		Node<V> setNode(@NotNull Node<V> node);

		/**
		 * Return the value of the node this link is pointing to.
		 *
		 * @return the value of the node this link is pointing to. Or {@code null} if this
		 * 		link is not pointing to a node. {@code null} also might indicate that the
		 * 		node has its value set to {@code null}.
		 * @since 0.0.1 ~2021.04.22
		 */
		@Nullable
		@Contract(pure = true)
		default V getValue() {
			Node<V> node = this.getNode();
			return node == null ? null : node.getValue();
		}

		/**
		 * Set the value of the node this link is pointing to to the given {@code value}.
		 *
		 * @param value the value to be set.
		 * @return the previous value of the node this link is pointing to.
		 * @throws IllegalStateException         if this link is currently pointing to
		 *                                       nothing.
		 * @throws NullPointerException          if the given {@code value} is null and
		 *                                       the node does not support null value.
		 * @throws IllegalArgumentException      if the node rejected the given {@code
		 *                                       value}.
		 * @throws ClassCastException            if the given {@code value} is of an
		 *                                       inappropriate type for the node.
		 * @throws UnsupportedOperationException if the node refused to change its value.
		 * @since 0.0.1 ~2021.04.22
		 */
		@Nullable
		@Contract(mutates = "this")
		default V setValue(@Nullable V value) {
			Node<V> node = this.getNode();
			if (node == null)
				throw new IllegalStateException("setValue");
			return node.setValue(value);
		}
	}
}

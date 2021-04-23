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
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ConcurrentModificationException;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//
///**
// * A node is a value carrier at a position in a multidimensional collection.
// * <br>
// * A node is a {@link Map map}-a-like structure with the keys begin the {@link Relation
// * relations} and the values being other nodes. Nodes has a {@link #get() sole value} and
// * strict relations. Strict relations means that when a node (A) maps a relation (Ar) to
// * another node (B) that other node (B) must map the {@link Relation#opposite() opposite
// * relation} (Br) of that relation (Ar) to that node (A).
// * <br>
// * <br>
// * <h3>Q/A:</h3>
// * <br>
// * <ul>
// *     <li>
// *         <b>Q-</b> It is so much like Map. So, why haven't you just extended the
// *         interface Map and increased compatibility?.
// *     </li>
// *     <li>
// *         <b>A-</b> It is all about specifications. Nodes can easily compile no-problem
// *         when extending the interface Map but the problem is when dealing with
// *         operations like {@link Map#equals(Object)} and {@link Map.Entry#equals(Object)}
// *         because even though these are so basic operations it is a headache when trying
// *         to check if a node equals a random implementation of Map while maintaining the
// *         proper behaviour when checking if a node equals another node. Specially that
// *         nodes has a really complicated system of equality.
// *     </li>
// * </ul>
// *
// * @param <V> the type of the value of the node.
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2021.03.01
// */
//public interface NodeX<V> {
//	// temporary default methods (Copy/Paste from Map)
//
//	/**
//	 * Attempts to compute a mapping for the specified relation and its current mapped
//	 * node (or {@code null} if there is no current mapping).
//	 * <br>
//	 * If the function returns {@code null}, the mapping is removed (or remains absent if
//	 * initially absent). If the function itself throws an (unchecked) exception, the
//	 * exception is rethrown, and the current mapping is left unchanged.
//	 *
//	 * @param relation relation with which the specified node is to be associated.
//	 * @param function the function to compute a node.
//	 * @return the new value associated with the specified relation, or null if none.
//	 * @throws NullPointerException          if the given {@code relation} or {@code
//	 *                                       function} is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the new
//	 *                                       node with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Nullable
//	@Contract(mutates = "this")
//	default NodeX<V> compute(@NotNull Relation relation, @NotNull BiFunction<@NotNull Relation, @Nullable NodeX<V>, @Nullable NodeX<V>> function) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(function, "function");
//		NodeX<V> oldValue = this.get(relation);
//
//		NodeX<V> newValue = function.apply(relation, oldValue);
//
//		if (newValue != null)
//			this.put(relation, newValue);
//		else if (oldValue != null /*no null nodes (values)*/)
//			this.remove(relation);
//
//		return newValue;
//	}
//
//	/**
//	 * If the specified relation is not already associated with a node, attempts to
//	 * compute its node using the given mapping function and enters it into this node
//	 * unless {@code null}.
//	 * <br>
//	 * If the function returns {@code null} no mapping is recorded. If the function itself
//	 * throws an (unchecked) exception, the exception is rethrown, and no mapping is
//	 * recorded.
//	 *
//	 * @param relation relation with which the specified node is to be associated.
//	 * @param function the function to compute a node.
//	 * @return the current (existing or computed) node associated with the specified
//	 * 		relation, or null if the computed node is null.
//	 * @throws NullPointerException          if the given {@code relation} or {@code
//	 *                                       function} is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the new
//	 *                                       node with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Nullable
//	@Contract(mutates = "this")
//	default NodeX<V> computeIfAbsent(@NotNull Relation relation, @NotNull Function<@NotNull Relation, @Nullable NodeX<V>> function) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(function, "function");
//
//		NodeX<V> v = this.get(relation);
//
//		if (v == null) {
//			NodeX<V> newValue = function.apply(relation);
//
//			if (newValue != null)
//				this.put(relation, newValue);
//
//			return newValue;
//		}
//
//		return v;
//	}
//
//	/**
//	 * If the node for the specified relation is present, attempts to compute a new
//	 * mapping given the relation and its current mapped node.
//	 * <br>
//	 * If the function returns {@code null}, the mapping is removed. If the function
//	 * itself throws an (unchecked) exception, the exception is rethrown, and the current
//	 * mapping is left unchanged.
//	 *
//	 * @param relation relation with which the specified node is to be associated.
//	 * @param function the function to compute a node.
//	 * @return the new node associated with the specified relation, or null if none.
//	 * @throws NullPointerException          if the given {@code relation} or {@code
//	 *                                       function} is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the new
//	 *                                       node with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Nullable
//	@Contract(mutates = "this")
//	default NodeX<V> computeIfPresent(@NotNull Relation relation, @NotNull BiFunction<@NotNull Relation, @NotNull NodeX<V>, @Nullable NodeX<V>> function) {
//		Objects.requireNonNull(function);
//		NodeX<V> oldValue = this.get(relation);
//
//		if (oldValue != null) {
//			NodeX<V> newValue = function.apply(relation, oldValue);
//
//			if (newValue != null)
//				this.put(relation, newValue);
//			else
//				this.remove(relation);
//
//			return newValue;
//		}
//
//		return null;
//	}
//
//	/**
//	 * If the specified relation is not already associated with a node, associates it with
//	 * the given {@code node}. Otherwise, replaces the associated node with the results of
//	 * the given remapping function, or removes if the result is {@code null}.
//	 * <br>
//	 * If the function returns {@code null} the mapping is removed. If the function itself
//	 * throws an (unchecked) exception, the exception is rethrown, and the current mapping
//	 * is left unchanged.
//	 *
//	 * @param relation relation with which the resulting node is to be associated.
//	 * @param node     the node to be merged with the existing node associated with the
//	 *                 relation or, if no existing node, to be associated with the
//	 *                 relation.
//	 * @param function the function to recompute a node if present.
//	 * @return the new node associated with the specified relation, or null if no node is
//	 * 		associated with the relation.
//	 * @throws NullPointerException          if the given {@code relation} or {@code node}
//	 *                                       or {@code function} is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the new
//	 *                                       node with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Nullable
//	@Contract(mutates = "this,param2")
//	default NodeX<V> merge(@NotNull Relation relation, @NotNull NodeX<V> node, @NotNull BiFunction<@NotNull NodeX<V>, @NotNull NodeX<V>, @Nullable NodeX<V>> function) {
//		Objects.requireNonNull(function, "function");
//		Objects.requireNonNull(node, "node");
//		NodeX<V> oldValue = this.get(relation);
//		NodeX<V> newValue = oldValue == null ?
//							node :
//							function.apply(oldValue, node);
//
//		if (newValue == null)
//			this.remove(relation);
//		else
//			this.put(relation, newValue);
//
//		return newValue;
//	}
//
//	/**
//	 * If the specified relation is not already associated with a node associates it with
//	 * the given node and returns {@code null}, else returns the current node.
//	 *
//	 * @param relation relation with which the specified node is to be associated.
//	 * @param node     node to be associated with the specified relation.
//	 * @return the previous node associated with the specified relation, or {@code null}
//	 * 		if there was no mapping for the relation.
//	 * @throws NullPointerException          if the given {@code relation} or {@code node}
//	 *                                       is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the given
//	 *                                       {@code node} with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Nullable
//	@Contract(mutates = "this,param2")
//	default NodeX<V> putIfAbsent(@NotNull Relation relation, @NotNull NodeX<V> node) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(node, "node");
//		NodeX<V> n = this.get(relation);
//
//		if (n == null)
//			n = this.put(relation, node);
//
//		return n;
//	}
//
//	/**
//	 * Removes the link for the specified relation only if it is currently mapped to the
//	 * specified node.
//	 *
//	 * @param relation relation with which the specified node is associated.
//	 * @param node     node expected to be associated with the specified relation.
//	 * @return {@code true} if the node was removed.
//	 * @throws NullPointerException          if the given {@code relation} or {@code node}
//	 *                                       is null.
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Contract(mutates = "this,param2")
//	default boolean remove(@NotNull Relation relation, @NotNull NodeX<V> node) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(node, "node");
//		NodeX<V> n = this.get(relation);
//
//		if (!Objects.equals(n, node))
//			return false;
//
//		this.remove(relation);
//		return true;
//	}
//
//	/**
//	 * Replaces the link for the specified relation only if currently mapped to the
//	 * specified {@code oldNode}.
//	 *
//	 * @param relation relation with which the specified value is associated.
//	 * @param oldNode  node expected to be associated with the specified relation.
//	 * @param newNode  node to be associated with the specified relation.
//	 * @return {@code true} if the node was replaced.
//	 * @throws NullPointerException          if the given {@code relation} or {@code
//	 *                                       oldNode} or {@code newNode} is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the given
//	 *                                       {@code newNode} with the given {@code
//	 *                                       relation}. (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Contract(mutates = "this,param2,param3")
//	default boolean replace(@NotNull Relation relation, @NotNull NodeX<V> oldNode, @NotNull NodeX<V> newNode) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(oldNode, "oldNode");
//		Objects.requireNonNull(newNode, "newNode");
//		NodeX<V> n = this.get(relation);
//
//		if (!Objects.equals(n, oldNode))
//			return false;
//
//		this.put(relation, newNode);
//		return true;
//	}
//
//	/**
//	 * Replaces the link for the specified relation only if it is currently mapped to some
//	 * node.
//	 *
//	 * @param relation relation with which the specified node is associated.
//	 * @param node     node to be associated with the specified relation.
//	 * @return the previous node associated with the specified relation, or {@code null}
//	 * 		if there was no mapping for the relation.
//	 * @throws NullPointerException          if the given {@code relation} or {@code node}
//	 *                                       is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the given
//	 *                                       {@code node} with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Nullable
//	@Contract(mutates = "this,param2")
//	default NodeX<V> replace(@NotNull Relation relation, @NotNull NodeX<V> node) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(node, "node");
//		NodeX<V> n = this.get(relation);
//
//		if (n != null)
//			n = this.put(relation, node);
//
//		return n;
//	}
//
//	/**
//	 * Replaces each link's node with the result of invoking the given function on that
//	 * link until all links have been processed or the function throws an exception.
//	 * Exceptions thrown by the function are relayed to the caller.
//	 *
//	 * @param function the function to apply to each link.
//	 * @throws NullPointerException            if the given {@code function} is null or
//	 *                                         returned a null.
//	 * @throws ConcurrentModificationException if an entry is found to be removed during
//	 *                                         iteration.
//	 * @throws IllegalArgumentException        if this node rejected relating to a new
//	 *                                         node with a {@code relation}. (optional)
//	 * @throws UnsupportedOperationException   if this node does not support changing its
//	 *                                         relatives. (optional)
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Contract(mutates = "this")
//	default void replaceAll(@NotNull BiFunction<@NotNull Relation, @NotNull NodeX<V>, @NotNull NodeX<V>> function) {
//		Objects.requireNonNull(function, "function");
//
//		for (Link<V> link : this.links()) {
//			Relation relation;
//			NodeX<V> node;
//
//			try {
//				relation = link.getRelation();
//				node = link.getNode();
//			} catch (IllegalStateException e) {
//				throw new ConcurrentModificationException(e);
//			}
//
//			node = function.apply(relation, node);
//
//			try {
//				link.setNode(node);
//			} catch (IllegalStateException e) {
//				throw new ConcurrentModificationException(e);
//			}
//		}
//	}
//
//	//abstract methods
//
//	/**
//	 * An object equals to a node when they are the same reference.
//	 *
//	 * @param object the object to be checked.
//	 * @return if the given {@code object} equals this.
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@Contract(value = "null->false", pure = true)
//	@Override
//	boolean equals(@Nullable Object object);
//
//	/**
//	 * Calculate the hash code of this node. The hash code should not represent any data
//	 * regarding the nodes related to it.
//	 * <br>
//	 * It is recommended for a node to have a hash code equals to the hash code of its
//	 * value.
//	 *
//	 * @return a hash code value for this node.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@Contract(pure = true)
//	@Override
//	int hashCode();
//
//	/**
//	 * Return a string representation of this node. The string representation should not
//	 * contain any data regarding the nodes related to it.
//	 * <br>
//	 * It is recommended for a node to have its string representation equal to:
//	 * <pre>
//	 *     toString = "{:" + node.value + "}"
//	 * </pre>
//	 *
//	 * @return a string representation of this node.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(pure = true)
//	@Override
//	String toString();
//
//	/**
//	 * Get the node related to this node with the given {@code relation}.
//	 *
//	 * @param relation the relation of the node to be returned.
//	 * @return the node relating to this node with the given {@code relation}.
//	 * @throws NullPointerException if the given {@code relation} is null.
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Nullable
//	@Contract(pure = true)
//	NodeX<V> get(@NotNull Relation relation);
//
//	/**
//	 * Get the value of this node.
//	 *
//	 * @return the value of this node.
//	 * @since 0.0.1 ~2021.02.24
//	 */
//	@Nullable
//	@Contract(pure = true)
//	V get();
//
//	/**
//	 * Check if this node relates to the given {@code node} (reference) with any
//	 * relation.
//	 *
//	 * @param node the node to be checked.
//	 * @return true, if this node relates to the given {@code node} in any relation.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Contract(pure = true)
//	boolean has(@NotNull NodeX<V> node);
//
//	/**
//	 * Check if this node has a node relating to it with the given {@code relation}.
//	 *
//	 * @param relation the relation to be checked.
//	 * @return true, if this node relates to a node with the given {@code relation}.
//	 * @throws NullPointerException if the given {@code relation} is null.
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Contract(pure = true)
//	boolean has(@NotNull Relation relation);
//
//	/**
//	 * Returns a set view of the links this node has. The set is backed by this node, so
//	 * changes to the node are reflected in the set, and vice-version.
//	 * <br>
//	 * The returned set will not support the {@link Set#add} nor the {@link Set#addAll}
//	 * operations.
//	 *
//	 * @return a set view of the relations of this node.
//	 * @since 0.0.1 ~2021.04.17
//	 */
//	@NotNull
//	@Contract(pure = true)
//	Set<Link<V>> links();
//
//	/**
//	 * Remove the node relating to this node with the given {@code relation} from this
//	 * node. (if it does not already)
//	 * <br>
//	 * Then, remove this node from relating to the removed node with {@link
//	 * Relation#opposite() opposite} of the given {@code relation}. (if it does not
//	 * already)
//	 * <br>
//	 * Then, put the node relating to this node with the given {@code relation} to be the
//	 * given {@code node}. (if it does not already)
//	 * <br>
//	 * Then, associate this node to relate to the given {@code node} with the {@link
//	 * Relation#opposite() opposite} of the given {@code relation}. (if it does not
//	 * already)
//	 *
//	 * @param relation the relation for the given {@code node} to relate to this node.
//	 * @param node     the node to be relating to this node.
//	 * @return the previous node relating to this node with the given {@code relation}. Or
//	 *        {@code null} if no such node.
//	 * @throws NullPointerException          if the given {@code relation} or {@code node}
//	 *                                       is null.
//	 * @throws IllegalArgumentException      if this node rejected relating to the given
//	 *                                       {@code node} with the given {@code relation}.
//	 *                                       (optional)
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Nullable
//	@Contract(mutates = "this,param2")
//	NodeX<V> put(@NotNull Relation relation, @NotNull NodeX<V> node);
//
//	/**
//	 * Remove the node relating to this node with the given {@code relation} from this
//	 * node. (if it does not already)
//	 * <br>
//	 * Then, remove this node from relating to the removed node with {@link
//	 * Relation#opposite() opposite} of the given {@code relation}. (if it does not
//	 * already)
//	 *
//	 * @param relation the relation of the node to be removed.
//	 * @return the previous node relating to this node with the given {@code relation}. Or
//	 *        {@code null} if no such node.
//	 * @throws NullPointerException          if the given {@code relation} is null.
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       relatives. (optional)
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	@Nullable
//	@Contract(mutates = "this")
//	NodeX<V> remove(@NotNull Relation relation);
//
//	/**
//	 * Set the value of this node to be the given {@code value}.
//	 *
//	 * @param value the value of this node.
//	 * @return the previous value of this node.
//	 * @throws NullPointerException          if the given {@code value} is null and this
//	 *                                       node does not allow having a null value.
//	 * @throws IllegalArgumentException      if this node rejected the given {@code value}
//	 *                                       for some aspect of it.
//	 * @throws ClassCastException            if the class of the given {@code value}
//	 *                                       prevents it from being set.
//	 * @throws UnsupportedOperationException if this node does not support changing its
//	 *                                       value.
//	 * @since 0.0.1 ~2021.02.24
//	 */
//	@Nullable
//	@Contract(mutates = "this")
//	V set(@Nullable V value);
//
//	/**
//	 * A link is a representation of a one sided relation between a node and the node.
//	 *
//	 * @param <V> the type of the value of the linked node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.04.16
//	 */
//	interface Link<V> {
//		/**
//		 * An object equals a link when that object is a link and has an equal relations
//		 * and the same node (in reference).
//		 *
//		 * @param object the object to be checked.
//		 * @return true, if the given {@code object} equals this.
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@Contract(pure = true, value = "null->false")
//		@Override
//		boolean equals(@Nullable Object object);
//
//		/**
//		 * Calculate the hash code of this link.
//		 * <br>
//		 * It is recommended for a link to have its hash code equals the hash code of its
//		 * relation xor-ed with the hash code of the node it is pointing to (currently).
//		 *
//		 * @return the hash code of this link.
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@Contract(pure = true)
//		@Override
//		int hashCode();
//
//		/**
//		 * Returns a string representation of this link.
//		 * <br>
//		 * Typically the representation equals to:
//		 * <pre>
//		 *     toString = this.relation + " -> " + this.node
//		 * </pre>
//		 *
//		 * @return a string representation of this link.
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@NotNull
//		@Contract(pure = true)
//		@Override
//		String toString();
//
//		/**
//		 * Return the linked node.
//		 *
//		 * @return the linked node. Or {@code null} if currently no linked node.
//		 * @throws IllegalStateException if the backing node no longer has this link.
//		 * @since 0.0.1 ~2021.04.16
//		 */
//		@NotNull
//		@Contract(pure = true)
//		NodeX<V> getNode();
//
//		/**
//		 * The relation of the link.
//		 *
//		 * @return the relation.
//		 * @throws IllegalStateException implementations may, but are not required to,
//		 *                               throw this exception if the entry has been
//		 *                               removed from the backing map. (optional)
//		 * @since 0.0.1 ~2021.04.16
//		 */
//		@NotNull
//		@Contract(pure = true)
//		Relation getRelation();
//
//		/**
//		 * Remove the node relating to the node with {@link #getRelation() relation} from
//		 * the node. (if it does not already)
//		 * <br>
//		 * Then, remove the node from relating to the removed node with {@link
//		 * Relation#opposite() opposite} of {@link #getRelation() relation}. (if it does
//		 * not already)
//		 * <br>
//		 * Then, put the node relating to the node with {@link #getRelation() relation} to
//		 * be the given {@code node}. (if it does not already)
//		 * <br>
//		 * Then, associate the node to relate to the given {@code node} with the {@link
//		 * Relation#opposite() opposite} of {@link #getRelation() relation}. (if it does
//		 * not already)
//		 *
//		 * @param node the node to be relating to the node.
//		 * @return the previous node relating to the node with the given {@code relation}.
//		 * 		Or {@code null} if no such node.
//		 * @throws NullPointerException          if the given {@code node} is null.
//		 * @throws IllegalArgumentException      if the backing node rejected to relate to
//		 *                                       the given {@code node} with the {@link
//		 *                                       #getRelation() relation} of this link.
//		 *                                       (optional)
//		 * @throws UnsupportedOperationException if this node does not support changing
//		 *                                       its relatives. (optional)
//		 * @throws IllegalStateException         implementations may, but are not required
//		 *                                       to, throw this exception if the entry has
//		 *                                       been removed from the backing map.
//		 *                                       (optional)
//		 * @since 0.0.1 ~2021.04.16
//		 */
//		@Nullable
//		@Contract(mutates = "this,param")
//		NodeX<V> setNode(@NotNull NodeX<V> node);
//	}
//
//	/**
//	 * A key is a constant that has an opposite and links two nodes together.
//	 *
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.04.15
//	 */
//	@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
//	interface Relation {
//		/**
//		 * An object equals a relation when they are the same reference.
//		 *
//		 * @param object the object to be checked.
//		 * @return true, if the given {@code object} equals this.
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@Contract(pure = true, value = "null->false")
//		@Override
//		boolean equals(@Nullable Object object);
//
//		/**
//		 * Returns the hash code of this relation. The hash code must have no
//		 * representation of the {@link #opposite()} relation.
//		 *
//		 * @return the hash code of this relation.
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@Contract(pure = true)
//		@Override
//		int hashCode();
//
//		/**
//		 * Return a string representation of this relation. The representation must have
//		 * no representation of the {@link #opposite()} relation.
//		 *
//		 * @return a string representation of this relation.
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@NotNull
//		@Contract(pure = true)
//		@Override
//		String toString();
//
//		/**
//		 * Returns the opposite of this key. The opposite shall be a constant reference
//		 * that never changes. The opposite of the returned key shall be this reference.
//		 *
//		 * @return the opposite of this key.
//		 * @since 0.0.1 ~2021.04.15
//		 */
//		@NotNull
//		@Contract(pure = true)
//		NodeX.Relation opposite();
//	}
//}
///*Removed Intentionally*/
////
////	/**
////	 * Remove any relation between this node and the given {@code node}.
////	 *
////	 * @param node the node to remove every relation between this node and it.
////	 * @return true, if any relation was removed.
////	 * @throws NullPointerException if the given {@code node} is null.
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	@Contract(mutates = "this,param")
////	boolean remove(@NotNull Node<V> node);
////
////	/**
////	 * Removes all the relations of this node. (make it lonely :P)
////	 *
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	@Contract(mutates = "this")
////	void remove();
////
////	/**
////	 * Return the number of relations this node has.
////	 *
////	 * @return the number of the relatives of this node.
////	 * @since 0.0.1 ~2021.04.16
////	 */
////	@Contract(pure = true)
////	int size();
////
////	/**
////	 * Performs the given action for each link in this node until all links have been
////	 * processed or the action throws an exception. Unless otherwise specified by the
////	 * implementing class, actions are performed in the order of link set iteration (if an
////	 * iteration order is specified.) Exceptions thrown by the action are relayed to the
////	 * caller.
////	 *
////	 * @param consumer the action to be performed for each link.
////	 * @throws NullPointerException            if the given {@code consumer} is null.
////	 * @throws ConcurrentModificationException if a link is found to be removed during
////	 *                                         iteration.
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	@Contract(pure = true)
////	default void forEach(@NotNull BiConsumer<@NotNull Relation, @NotNull Node<V>> consumer) {
////		Objects.requireNonNull(consumer, "consumer");
////
////		for (Link<V> link : this.linkSet()) {
////			Relation relation;
////			Node<V> node;
////
////			try {
////				relation = link.getRelation();
////				node = link.getNode();
////			} catch (IllegalStateException e) {
////				throw new ConcurrentModificationException(e);
////			}
////
////			consumer.accept(relation, node);
////		}
////	}
////
////	/**
////	 * Returns the node to which the specified relation is mapped, or {@code defaultValue} if
////	 * this map contains no mapping for the key.
////	 *
////	 * @param key          the key whose associated value is to be returned
////	 * @param defaultValue the default mapping of the key
////	 * @return the value to which the specified key is mapped, or {@code defaultValue} if
////	 * 		this map contains no mapping for the key
////	 * @throws ClassCastException   if the key is of an inappropriate type for this map
////	 *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
////	 * @throws NullPointerException if the specified key is null and this map does not
////	 *                              permit null keys (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
////	 * @implSpec The default implementation makes no guarantees about synchronization or
////	 * 		atomicity properties of this method. Any implementation providing atomicity
////	 * 		guarantees must override this method and document its concurrency properties.
////	 * @since 1.8
////	 */
////	default V getOrDefault(Object key, V defaultValue) {
////		V v;
////		return (v = this.get(key)) != null || containsKey(key)
////			   ? v
////			   : defaultValue;
////	}
////
////	/**
////	 * Steal all the relations of the given {@code node}.
////	 *
////	 * @param node the node to steal its relations.
////	 * @throws NullPointerException          if the given {@code node} is null.
////	 * @throws IllegalArgumentException      if this node rejected relating to a {@code
////	 *                                       node} mapped to a {@code relation}.
////	 *                                       (optional)
////	 * @throws UnsupportedOperationException if this node does not support changing its
////	 *                                       relatives. (optional)
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	@Contract(mutates = "this,param")
////	void putAll(@NotNull Node<V> node);
////
////	/**
////	 * Return a set of the relations this node has. The returned collection is a view of
////	 * this node so any changes occurs to this node will apply to the returned view and
////	 * any changes to the returned view will apply to this node.
////	 * <br>
////	 * The returned set will not support adding relations.
////	 *
////	 * @return a set view of the relations this node has.
////	 * @since 0.0.1 ~2021.04.16
////	 */
////	@NotNull
////	@Contract(pure = true)
////	Set<Relation> relationSet();
////
////	/**
////	 * Returns a collection view of the nodes relating to this node. The returned
////	 * collection is a view of this node so any changes occurs to this node will apply to
////	 * the returned view and any changes to the returned view will apply to this node.
////	 * <br>
////	 * The returned collection will not support adding nodes.
////	 *
////	 * @return a collection view of the nodes relating to this node.
////	 * @since 0.0.1 ~2021.04.16
////	 */
////	@NotNull
////	@Contract(pure = true)
////	Collection<Node<V>> nodes();
////
////	/**
////	 * Returns true if this node has no relatives.
////	 *
////	 * @return true, if this node has no relatives.
////	 * @since 0.0.1 ~2021.04.17
////	 */
////	@Contract(pure = true)
////	boolean isEmpty();
//
///* Junk */
////	/**
////	 * Associate the given {@code link} to this node.
////	 * <br>
////	 * If the given {@code link} does not point to this node then the link will be treated
////	 * as a junk link and will be ignored by all the operations and might be removed
////	 * later. But, if the link started pointing to this node before this node started
////	 * cleaning, then the link will not be removed. It is an implementation specific for
////	 * when and how the cleaning process will occur. So, it is better to provide a link
////	 * that points to this node instead of making it point to this node after calling this
////	 * method. Also, the link will be removed everytime this node starts cleaning at the
////	 * time the link is not pointing to this node.
////	 *
////	 * @param key  the key to associate the link with.
////	 * @param link the link to be put.
////	 * @throws NullPointerException if the given {@code key} or {@code link} is null.
////	 * @since 0.0.1 ~2021.04.16
////	 */
////	void putLink(@NotNull K key, @NotNull Link<K, V> link);
//
////	/**
////	 * A link is an entry that associates some kind of a key to a node.
////	 *
////	 * @param <K> the type of the key of the link and the type of the keys of the node
////	 *            it's holding.
////	 * @param <V> the type of the value of the node the link is holding.
////	 * @author LSafer
////	 * @version 0.0.1
////	 * @since 0.0.1 ~2021.03.11
////	 */
////	interface Link<K extends Relation, V> {
////		/**
////		 * Determine if the given {@code object} equal this link. An object equal to a
////		 * link if that object is a link and has the same {@link #getNode() node} and the
////		 * same {@link #opposite() opposite} {@link #getNode() node}.
////		 *
////		 * @param object the object to be matched.
////		 * @return true, if the given {@code object} equals this link.
////		 * @since 0.0.1 ~2021.03.01
////		 */
////		@Contract(value = "null->false", pure = true)
////		@Override
////		boolean equals(@Nullable Object object);
////
////		/**
////		 * Calculate the hash code of this link.
////		 * <br>
////		 * It is recommended for a link to have a hash code equals to the hash code of its
////		 * key {@code XOR}-ed with the hash code of the node it is pointing to.
////		 *
////		 * @return the hash code of this link.
////		 * @since 0.0.1 ~2021.03.11
////		 */
////		@Contract(pure = true)
////		@Override
////		int hashCode();
////
////		/**
////		 * Return a string representation of this link.
////		 * <br>
////		 * It is recommended for a link to have its string representation equal to:
////		 * <pre>
////		 *     toString = link.key + "->" + link.node.toString()
////		 * </pre>
////		 *
////		 * @return a string representation of this link.
////		 * @since 0.0.1 ~2021.03.11
////		 */
////		@NotNull
////		@Contract(pure = true)
////		@Override
////		String toString();
////
////		/**
////		 * Get the node this link is pointing to.
////		 *
////		 * @return the node this link is pointing to. Or {@code null} if currently not
////		 * 		pointing to any node.
////		 * @since 0.0.1 ~2021.03.11
////		 */
////		@Nullable
////		@Contract(pure = true)
////		Node<K, V> getNode();
////
////		/**
////		 * Get the other side of this link.
////		 * <br>
////		 * A link must always has the same opposite link (in reference).
////		 * <br>
////		 * The opposite link must have the original link as its opposite link.
////		 *
////		 * @return the other side of this link.
////		 * @since 0.0.1 ~2021.04.15
////		 */
////		@NotNull
////		@Contract(pure = true)
////		Link<K, V> opposite();
////
////		/**
////		 * Remove the node of this.
////		 *
////		 * @return the removed node. Or {@code null} if no node was removed.
////		 * @since 0.0.1 ~2021.04.16
////		 */
////		@Nullable
////		@Contract(mutates = "this")
////		Node<K, V> removeNode();
////
////		/**
////		 * Set the given {@code node} as the node of this link.
////		 *
////		 * @param node the node of this link.
////		 * @return the previous node associated to this link.
////		 * @throws NullPointerException          if the given {@code node} is null.
////		 * @throws IllegalArgumentException      if this link rejected the given {@code
////		 *                                       node} for some aspect of it.
////		 * @throws UnsupportedOperationException if this link does not allow changing the
////		 *                                       node it is pointing to.
////		 * @since 0.0.1 ~2021.03.11
////		 */
////		@Nullable
////		@Contract(mutates = "this")
////		Node<K, V> setNode(@NotNull Node<K, V> node);
////	}
////
////	/**
////	 * @param <K>
////	 * @param <V>
////	 */
////	interface LinkTree<K extends Key, V> {
////		Link<K, V> get(K key);
////
////		Link<K, V> put(Link<K, V> link);
////
////		boolean remove(Link<K, V> link);
////	}
////
////	/**
////	 * Set the given {@code node} to be the node related to this node with the given
////	 * {@code key}.
////	 *
////	 * @param key  the key to set the given {@code node} to relate to this node by.
////	 * @param node the node to be set.
////	 * @return the previous node relating to this node.
////	 * @since 0.0.1 ~2021.03.11
////	 */
////	@Nullable
////	@Contract(mutates = "this,param2")
////	Node<K, V> putNode(@NotNull K key, @NotNull Node<K, V> node);
////
////	Node<K, V> removeNode(@NotNull K key);
////
////	/**
////	 * Get the node related to this node with the given {@code key}.
////	 *
////	 * @param key the key of the node to get.
////	 * @return the node related to this node with the given {@code key}.
////	 * @throws NullPointerException if the given {@code key} is null.
////	 * @since 0.0.1 ~2021.03.11
////	 */
////	@Nullable
////	@Contract(pure = true)
////	Node<K, V> getNode(@NotNull K key);
//
////	Set<Link<K, V>> linkSet();

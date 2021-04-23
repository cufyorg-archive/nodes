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
//import org.jetbrains.annotations.UnmodifiableView;
//
//import java.io.Serializable;
//import java.util.AbstractSet;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * Static utilities for {@link Node}s.
// *
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2021.04.17
// */
//public interface Nodes {
//	@UnmodifiableView
//	static <V> Node<V> unmodifiableNode(Node<V> node) {
//		return new UnmodifiableNode<>(node);
//	}
//
//	/**
//	 * @param <V>
//	 */
//	class UnmodifiableNode<V> implements Node<V> {
//		@Nullable
//		@Override
//		public Node<V> get(@NotNull Node.Relation relation) {
//			return null;
//		}
//
//		@Nullable
//		@Override
//		public V get() {
//			return null;
//		}
//
//		@Override
//		public boolean has(@NotNull Node<V> node) {
//			return false;
//		}
//
//		@Override
//		public boolean has(@NotNull Node.Relation relation) {
//			return false;
//		}
//
//		@NotNull
//		@Override
//		public Set<Node.Link<V>> links() {
//			return null;
//		}
//
//		@Nullable
//		@Override
//		public Node<V> put(@NotNull Node.Relation relation, @NotNull Node<V> node) {
//			return null;
//		}
//
//		@Nullable
//		@Override
//		public Node<V> remove(@NotNull Node.Relation relation) {
//			return null;
//		}
//
//		@Nullable
//		@Override
//		public V set(@Nullable V value) {
//			return null;
//		}
//
//		/**
//		 * A {@link Node.Link} unmodifiable wrapper class to insure immutability of a
//		 * node.
//		 *
//		 * @author LSafer
//		 * @version 0.0.1
//		 * @since 0.0.1 ~2021.04.17
//		 */
//		@UnmodifiableView
//		protected class UnmodifiableLink implements Node.Link<V>, Serializable {
//			@SuppressWarnings("JavaDoc")
//			private static final long serialVersionUID = 8481565030595505108L;
//
//			/**
//			 * The wrapped link.
//			 *
//			 * @since 0.0.1 ~2021.04.17
//			 */
//			@NotNull
//			//do not touch my private stuff !@#$% >:-(
//			private final Node.Link<V> link;
//
//			/**
//			 * Construct a new unmodifiable link wrapper for the given {@code link}.
//			 *
//			 * @param link the link to be wrapped.
//			 * @since 0.0.1 ~2021.04.16
//			 */
//			public UnmodifiableLink(@NotNull Node.Link<V> link) {
//				Objects.requireNonNull(link, "link");
//				this.link = link;
//			}
//
//			@Override
//			public boolean equals(@Nullable Object object) {
//				//do not delegate to the wrapped link
//				//because the wrapped link MIGHT call
//				//equals of the given object. Witch
//				//is a huge risk
//				if (object == this || object == this.link)
//					return true;
//				if (object instanceof Node.Link)
//					try {
//						Node.Link link = (Node.Link) object;
//
//						Node.Relation relation = this.link.getRelation();
//						Node<V> node = this.link.getNode();
//						return node == link.getNode() &&
//							   Objects.equals(relation, link.getRelation());
//					} catch (IllegalStateException ignored) {
//						//just in case
//					}
//
//				return false;
//			}
//
//			@NotNull
//			@Override
//			public Node<V> getNode() {
//				Node<V> node = this.link.getNode();
//				//to remain 'strict'
//				return node instanceof UnmodifiableNode ?
//					   node :
//					   Nodes.unmodifiableNode(this.link.getNode());
//			}
//
//			@NotNull
//			@Override
//			public Node.Relation getRelation() {
//				return this.link.getRelation();
//			}
//
//			@Override
//			public int hashCode() {
//				return this.link.hashCode();
//			}
//
//			@Nullable
//			@Override
//			public Node<V> setNode(@NotNull Node<V> node) {
//				throw new UnsupportedOperationException("setNode");
//			}
//
//			@NotNull
//			@Override
//			public String toString() {
//				return this.link.toString();
//			}
//		}
//
//		protected class UnmodifiableLinkSet extends AbstractSet<Node.Link<V>> {
//
//		}
//	}
//}

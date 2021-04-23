//A previous attempt to create a nodes utilities class

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
//import java.util.Objects;
//
///**
// * @author LSafer
// * @version 0.0.0
// * @since 0.0.0 ~2021.03.01
// */
//public final class Nodes {
//	/**
//	 * Construct a new triple dimension view of the given {@code node}.
//	 *
//	 * @param node the node to create the view for.
//	 * @param <T>  the type of the value of the node.
//	 * @return a new triple dimension view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> CubeNode<T> flatCubeNode(@NotNull CubeNode<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new FlatCubeNode<>(node);
//	}
//
//	/**
//	 * Construct a new single dimension view of the given {@code node}.
//	 *
//	 * @param node the node to create the view for.
//	 * @param <T>  the type of the value of the node.
//	 * @return a new single dimension view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> LineNode<T> flatLineNode(@NotNull LineNode<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new FlatLineNode<>(node);
//	}
//
//	/**
//	 * Construct a new dimensionless view of the given {@code node}.
//	 *
//	 * @param node the node to create the view for.
//	 * @param <T>  the type of the value of the node.
//	 * @return a new dimensionless view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> Node<T> flatNode(@NotNull Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new FlatNode<>(node);
//	}
//
//	/**
//	 * Construct a new double dimension view of the given {@code node}.
//	 *
//	 * @param node the node to create the view for.
//	 * @param <T>  the type of the value of the node.
//	 * @return a new double dimension view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> SquareNode<T> flatSquareNode(@NotNull SquareNode<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new FlatSquareNode<>(node);
//	}
//
////	public static <T> LineNode<T> horizontalNode(@NotNull SquareNode<T> node) {
////		Objects.requireNonNull(node, "node");
////		return new FlatLineNode<>(node);
////	}
//
//	/**
//	 * Return a triple dimension unmodifiable view of the given {@code node}.
//	 *
//	 * @param node the node to get an unmodifiable view of it.
//	 * @param <T>  the type of the value of the given {@code node}.
//	 * @return a triple dimension unmodifiable view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> CubeNode<T> unmodifiableCubeNode(@NotNull CubeNode<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new UnmodifiableCubeNode<>(node);
//	}
//
//	/**
//	 * Return a single dimension unmodifiable view of the given {@code node}.
//	 *
//	 * @param node the node to get an unmodifiable view of it.
//	 * @param <T>  the type of the value of the given {@code node}.
//	 * @return a single dimension unmodifiable view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> LineNode<T> unmodifiableLineNode(@NotNull LineNode<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new UnmodifiableLineNode<>(node);
//	}
//
//	/**
//	 * Return a dimensionless unmodifiable view of the given {@code node}.
//	 *
//	 * @param node the node to get an unmodifiable view of it.
//	 * @param <T>  the type of the value of the given {@code node}.
//	 * @return a dimensionless unmodifiable view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> Node<T> unmodifiableNode(@NotNull Node<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new UnmodifiableNode<>(node);
//	}
//
//	/**
//	 * Return a double dimension unmodifiable view of the given {@code node}.
//	 *
//	 * @param node the node to get an unmodifiable view of it.
//	 * @param <T>  the type of the value of the given {@code node}.
//	 * @return a double dimension unmodifiable view of the given {@code node}.
//	 * @throws NullPointerException if the given {@code node} is null.
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	@NotNull
//	@Contract(value = "_->new", pure = true)
//	public static <T> SquareNode<T> unmodifiableSquareNode(@NotNull SquareNode<T> node) {
//		Objects.requireNonNull(node, "node");
//		return new UnmodifiableSquareNode<>(node);
//	}
//
////	/**
////	 * @param node
////	 * @param <T>
////	 * @return
////	 */
////	public static <T> LineNode<T> verticalLineNode(@NotNull SquareNode<T> node) {
////		Objects.requireNonNull(node, "node");
////
////	}
//
//	/**
//	 * A view node wrapping another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class FlatCubeNode<T> extends FlatSquareNode<T> implements CubeNode<T> {
//		/**
//		 * The node this node is delegating to.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final CubeNode<T> cube;
//
//		/**
//		 * Construct a new node node view delegating to the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected FlatCubeNode(@NotNull CubeNode<T> node) {
//			super(node);
//			this.cube = node;
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> getAfter() {
//			CubeNode<T> after = this.cube.getAfter();
//			return after == null ? null : new FlatCubeNode<>(after);
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> getBefore() {
//			CubeNode<T> before = this.cube.getBefore();
//			return before == null ? null : new FlatCubeNode<>(before);
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> removeAfter() {
//			CubeNode<T> after = this.cube.removeAfter();
//			return after == null ? null : new FlatCubeNode<>(after);
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> removeBefore() {
//			CubeNode<T> before = this.cube.removeBefore();
//			return before == null ? null : new FlatCubeNode<>(before);
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> setAfter(@NotNull CubeNode<T> node) {
//			CubeNode<T> after = this.cube.setAfter(node);
//			return after == null ? null : new FlatCubeNode<>(after);
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> setBefore(@NotNull CubeNode<T> node) {
//			CubeNode<T> before = this.cube.setBefore(node);
//			return before == null ? null : new FlatCubeNode<>(before);
//		}
//	}
//
//	/**
//	 * A view node wrapping another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class FlatLineNode<T> extends FlatNode<T> implements LineNode<T> {
//		/**
//		 * The node this node is delegating to.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final LineNode<T> line;
//
//		/**
//		 * Construct a new node node view delegating to the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected FlatLineNode(@NotNull LineNode<T> node) {
//			super(node);
//			this.line = node;
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> getNext() {
//			LineNode<T> next = this.line.getNext();
//			return next == null ? null : new FlatLineNode<>(next);
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> getPrevious() {
//			LineNode<T> previous = this.line.getPrevious();
//			return previous == null ? null : new FlatLineNode<>(previous);
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> removeNext() {
//			LineNode<T> next = this.line.removeNext();
//			return next == null ? null : new FlatLineNode<>(next);
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> removePrevious() {
//			LineNode<T> previous = this.line.removePrevious();
//			return previous == null ? null : new FlatLineNode<>(previous);
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> setNext(@NotNull LineNode<T> node) {
//			LineNode<T> next = this.line.setNext(node);
//			return next == null ? null : new FlatLineNode<>(next);
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> setPrevious(@NotNull LineNode<T> node) {
//			LineNode<T> previous = this.line.setPrevious(node);
//			return previous == null ? null : new FlatLineNode<>(previous);
//		}
//	}
//
//	/**
//	 * A view node wrapping another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class FlatNode<T> implements Node<T> {
//		/**
//		 * The node this node is delegating to.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final Node<T> node;
//
//		/**
//		 * Construct a new node node view delegating to the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected FlatNode(@NotNull Node<T> node) {
//			Objects.requireNonNull(node, "node");
//			this.node = node;
//		}
//
//		@Override
//		public boolean equals(@Nullable Object object) {
//			return this == object || this.node.equals(object);
//		}
//
//		@Nullable
//		@Override
//		public T get() {
//			return this.node.get();
//		}
//
//		@Override
//		public int hashCode() {
//			return this.node.hashCode();
//		}
//
//		@Nullable
//		@Override
//		public T set(@Nullable T value) {
//			return this.node.set(value);
//		}
//
//		@NotNull
//		@Override
//		public String toString() {
//			return this.node.toString();
//		}
//	}
//
//	/**
//	 * A view node wrapping another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class FlatSquareNode<T> extends FlatLineNode<T> implements SquareNode<T> {
//		/**
//		 * The node this node is delegating to.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final SquareNode<T> square;
//
//		/**
//		 * Construct a new node node view delegating to the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected FlatSquareNode(@NotNull SquareNode<T> node) {
//			super(node);
//			this.square = node;
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> getAbove() {
//			SquareNode<T> above = this.square.getAbove();
//			return above == null ? null : new FlatSquareNode<>(above);
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> getBelow() {
//			SquareNode<T> below = this.square.getBelow();
//			return below == null ? null : new FlatSquareNode<>(below);
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> removeAbove() {
//			SquareNode<T> above = this.square.removeAbove();
//			return above == null ? null : new FlatSquareNode<>(above);
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> removeBelow() {
//			SquareNode<T> below = this.square.removeBelow();
//			return below == null ? null : new FlatSquareNode<>(below);
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> setAbove(@NotNull SquareNode<T> node) {
//			SquareNode<T> above = this.square.setAbove(node);
//			return above == null ? null : new FlatSquareNode<>(above);
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> setBelow(@NotNull SquareNode<T> node) {
//			SquareNode<T> below = this.square.setBelow(node);
//			return below == null ? null : new FlatSquareNode<>(below);
//		}
//	}
//
////	/**
////	 * @param <T>
////	 */
////	public static class SwappedXYSquareNode<T> implements SquareNode<T> {
////		/**
////		 * The original node.
////		 *
////		 * @since 0.0.1 ~2021.03.01
////		 */
////		@NotNull
////		protected final SquareNode<T> node;
////
////		/**
////		 * Construct a new rotated square node view of the given {@code node}.
////		 *
////		 * @param node the node to be wrapped.
////		 * @throws NullPointerException if the given {@code node} is null.
////		 * @since 0.0.1
////		 */
////		public SwappedXYSquareNode(@NotNull SquareNode<T> node) {
////			Objects.requireNonNull(node, "node");
////			this.node = node;
////		}
////
////		@Override
////		public boolean equals(@Nullable Object object) {
////			return this == object || this.node.equals(object);
////		}
////
////		@Nullable
////		@Override
////		public T getValue() {
////			return this.node.getValue();
////		}
////
////		@Override
////		public int hashCode() {
////			return this.node.hashCode();
////		}
////
////		@Nullable
////		@Override
////		public T setValue(@Nullable T value) {
////			return this.node.setValue(value);
////		}
////
////		@Nullable
////		@Override
////		public LineNode<T> getNext() {
////			Node<T> node = this.node.getBelow();
////			return node == null ? null : new SwappedXYSquareNode<>(node);
////		}
////		@Nullable
////		@Override
////		public  LineNode<T> getPrevious() {
////			return this.node.getAbove();
////		}
////		@Nullable
////		@Override
////		public  LineNode<T> removeNext() {
////			return this.node.removeBelow();
////		}
////
////		@Override
////		public @Nullable LineNode<T> removePrevious() {
////			return null;
////		}
////
////		@Override
////		public @Nullable LineNode<T> setNext(@NotNull LineNode<T> node) {
////			return null;
////		}
////
////		@Override
////		public @Nullable LineNode<T> setPrevious(@NotNull LineNode<T> node) {
////			return null;
////		}
////
////		@NotNull
////		@Override
////		public String toString() {
////			return this.node.toString();
////		}
////	}
//
//	/**
//	 * An unmodifiable view of another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class UnmodifiableCubeNode<T> extends UnmodifiableSquareNode<T> implements CubeNode<T> {
//		/**
//		 * The original node.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final CubeNode<T> cube;
//
//		/**
//		 * Construct a new unmodifiable node view of the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected UnmodifiableCubeNode(@NotNull CubeNode<T> node) {
//			super(node);
//			this.cube = node;
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> getAfter() {
//			CubeNode<T> after = this.cube.getAfter();
//			return after == null ? null : new FlatCubeNode<>(after);
//		}
//
//		@Nullable
//		@Override
//		public CubeNode<T> getBefore() {
//			CubeNode<T> before = this.cube.getBefore();
//			return before == null ? null : new FlatCubeNode<>(before);
//		}
//
//		@Nullable
//		@Contract("->fail")
//		@Override
//		public CubeNode<T> removeAfter() {
//			throw new UnsupportedOperationException("removeAfter");
//		}
//
//		@Nullable
//		@Contract("->fail")
//		@Override
//		public CubeNode<T> removeBefore() {
//			throw new UnsupportedOperationException("removeBefore");
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public CubeNode<T> setAfter(@NotNull CubeNode<T> node) {
//			throw new UnsupportedOperationException("setAfter");
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public CubeNode<T> setBefore(@NotNull CubeNode<T> node) {
//			throw new UnsupportedOperationException("setBefore");
//		}
//	}
//
//	/**
//	 * An unmodifiable view of another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class UnmodifiableLineNode<T> extends UnmodifiableNode<T> implements LineNode<T> {
//		/**
//		 * The original node.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final LineNode<T> line;
//
//		/**
//		 * Construct a new unmodifiable node view of the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected UnmodifiableLineNode(@NotNull LineNode<T> node) {
//			super(node);
//			this.line = node;
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> getNext() {
//			LineNode<T> next = this.line.getNext();
//			return next == null ? null : new UnmodifiableLineNode<>(next);
//		}
//
//		@Nullable
//		@Override
//		public LineNode<T> getPrevious() {
//			LineNode<T> previous = this.line.getPrevious();
//			return previous == null ? null : new UnmodifiableLineNode<>(previous);
//		}
//
//		@Nullable
//		@Contract("->fail")
//		@Override
//		public LineNode<T> removeNext() {
//			throw new UnsupportedOperationException("removeNext");
//		}
//
//		@Nullable
//		@Contract("->fail")
//		@Override
//		public LineNode<T> removePrevious() {
//			throw new UnsupportedOperationException("removePrevious");
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public LineNode<T> setNext(@NotNull LineNode<T> node) {
//			throw new UnsupportedOperationException("setNext");
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public LineNode<T> setPrevious(@NotNull LineNode<T> node) {
//			throw new UnsupportedOperationException("setPrevious");
//		}
//	}
//
//	/**
//	 * An unmodifiable view of another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class UnmodifiableNode<T> implements Node<T> {
//		/**
//		 * The original node.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final Node<T> node;
//
//		/**
//		 * Construct a new unmodifiable node view of the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected UnmodifiableNode(@NotNull Node<T> node) {
//			Objects.requireNonNull(node, "node");
//			this.node = node;
//		}
//
//		@Override
//		public boolean equals(@Nullable Object object) {
//			return this == object || this.node.equals(object);
//		}
//
//		@Nullable
//		@Override
//		public T get() {
//			return this.node.get();
//		}
//
//		@Override
//		public int hashCode() {
//			return this.node.hashCode();
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public T set(@Nullable T value) {
//			throw new UnsupportedOperationException("setValue");
//		}
//
//		@NotNull
//		@Override
//		public String toString() {
//			return this.node.toString();
//		}
//	}
//
//	/**
//	 * An unmodifiable view of another node.
//	 *
//	 * @param <T> the type of the value of the node.
//	 * @author LSafer
//	 * @version 0.0.1
//	 * @since 0.0.1 ~2021.03.01
//	 */
//	public static class UnmodifiableSquareNode<T> extends UnmodifiableLineNode<T> implements SquareNode<T> {
//		/**
//		 * The original node.
//		 *
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		@NotNull
//		protected final SquareNode<T> square;
//
//		/**
//		 * Construct a new unmodifiable node view of the given {@code node}.
//		 *
//		 * @param node the node to be wrapped.
//		 * @throws NullPointerException if the given {@code node} is null.
//		 * @since 0.0.1 ~2021.03.01
//		 */
//		protected UnmodifiableSquareNode(@NotNull SquareNode<T> node) {
//			super(node);
//			this.square = node;
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> getAbove() {
//			SquareNode<T> above = this.square.getAbove();
//			return above == null ? null : new FlatSquareNode<>(above);
//		}
//
//		@Nullable
//		@Override
//		public SquareNode<T> getBelow() {
//			SquareNode<T> below = this.square.getBelow();
//			return below == null ? null : new FlatSquareNode<>(below);
//		}
//
//		@Nullable
//		@Contract("->fail")
//		@Override
//		public SquareNode<T> removeAbove() {
//			throw new UnsupportedOperationException("removeAbove");
//		}
//
//		@Nullable
//		@Contract("->fail")
//		@Override
//		public SquareNode<T> removeBelow() {
//			throw new UnsupportedOperationException("removeBelow");
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public SquareNode<T> setAbove(@NotNull SquareNode<T> node) {
//			throw new UnsupportedOperationException("setAbove");
//		}
//
//		@Nullable
//		@Contract("_->fail")
//		@Override
//		public SquareNode<T> setBelow(@NotNull SquareNode<T> node) {
//			throw new UnsupportedOperationException("setBelow");
//		}
//	}
//}

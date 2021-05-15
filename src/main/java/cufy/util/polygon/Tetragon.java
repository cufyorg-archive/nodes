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
package cufy.util.polygon;

import cufy.util.Node.Key;
import org.jetbrains.annotations.NotNull;

/**
 * The sides of a tetragon.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.04.28
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Tetragon extends Digon {
	/**
	 * The side at the top of the tetragon. (Y+)
	 *
	 * @since 0.0.2 ~2021.04.28
	 */
	@NotNull
	Tetragon TOP = new Tetragon() {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -990754514165955484L;

		@NotNull
		@Override
		public Key opposite() {
			return Tetragon.BOTTOM;
		}

		@NotNull
		@Override
		public String toString() {
			return "TOP";
		}
	};
	/**
	 * The side at the end of the tetragon. (Y-)
	 *
	 * @since 0.0.2 ~2021.04.28
	 */
	@NotNull
	Tetragon BOTTOM = new Tetragon() {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 4951271688223864399L;

		@NotNull
		@Override
		public Key opposite() {
			return Tetragon.TOP;
		}

		@NotNull
		@Override
		public String toString() {
			return "BOTTOM";
		}
	};
}

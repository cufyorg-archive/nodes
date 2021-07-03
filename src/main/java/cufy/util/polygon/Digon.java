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
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * The sides of a digon.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.04.28
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Digon extends Key, Serializable {
	/**
	 * The side at the start of the digon. (X-)
	 *
	 * @since 0.0.2 ~2021.04.28
	 */
	@NotNull
	Digon START = new Digon() {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = -4182475862803837047L;

		@Override
		public boolean equals(@Nullable Object object) {
			return object == this ||
				   object instanceof Digon &&
				   object.toString().equals(this.toString());
		}

		@NotNull
		@Override
		public Key opposite() {
			return Digon.END;
		}

		@NotNull
		@Override
		public String toString() {
			return "START";
		}
	};
	/**
	 * The side to the end of the digon. (X+)
	 *
	 * @since 0.0.2 ~2021.04.28
	 */
	@NotNull
	Digon END = new Digon() {
		@SuppressWarnings("JavaDoc")
		private static final long serialVersionUID = 7728357571108058781L;

		@Override
		public boolean equals(@Nullable Object object) {
			return object == this ||
				   object instanceof Digon &&
				   object.toString().equals(this.toString());
		}

		@NotNull
		@Override
		public Key opposite() {
			return Digon.START;
		}

		@NotNull
		@Override
		public String toString() {
			return "END";
		}
	};
}

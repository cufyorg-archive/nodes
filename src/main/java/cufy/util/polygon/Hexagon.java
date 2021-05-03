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
 * The sides of a hexagon.
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.04.28
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Hexagon extends Tetragon {
	/**
	 * The side at the back of the hexagon. (Z-)
	 *
	 * @since 0.0.2 ~2021.04.28
	 */
	@NotNull
	Hexagon BACK = new Hexagon() {
		@NotNull
		@Override
		public String toString() {
			return "BACK";
		}

		@NotNull
		@Override
		public Key opposite() {
			return Hexagon.FRONT;
		}
	};
	/**
	 * The side at the front of the hexagon. (Z+)
	 *
	 * @since 0.0.2 ~2021.04.28
	 */
	@NotNull
	Hexagon FRONT = new Hexagon() {
		@NotNull
		@Override
		public String toString() {
			return "FRONT";
		}

		@NotNull
		@Override
		public Key opposite() {
			return Hexagon.BACK;
		}
	};
}

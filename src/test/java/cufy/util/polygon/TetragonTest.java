package cufy.util.polygon;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TetragonTest {
	@Test
	public void serial() throws Exception {
		Digon[] digons = {
				Digon.START,
				Digon.END,
				Tetragon.BOTTOM,
				Tetragon.TOP,
				Hexagon.BACK,
				Hexagon.FRONT
		};

		for (int i = 0; i < digons.length; i++) {
			Digon oDigon = digons[i];

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(oDigon);
			oos.close();

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Digon sDigon = (Digon) ois.readObject();
			ois.close();

			assertNotSame(
					oDigon,
					sDigon,
					"Poor clone"
			);
			assertEquals(
					oDigon,
					sDigon,
					"Poor equals"
			);

			for (int j = 0; j < digons.length; j++) {
				assertNotSame(
						digons[j],
						sDigon,
						"Poor clone"
				);

				if (i == j)
					assertEquals(
							digons[j],
							sDigon,
							"Poor equals"
					);
				else
					assertNotEquals(
							digons[j],
							sDigon,
							"Generis equals"
					);
			}
		}
	}
}

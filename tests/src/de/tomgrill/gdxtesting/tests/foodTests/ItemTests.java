package de.tomgrill.gdxtesting.tests.foodTests;

import de.tomgrill.gdxtesting.GdxTestRunner;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.graphics.Texture;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemTests {

	static Item item;
	static String itemID = "<main>:lettuce.png";
	static String texturePath = FileControl.getAssetPath(itemID, "items");
	static String name = "Lettuce";
	static int value = 1;

	@BeforeClass
	public static void setup() {
		item = new Item(itemID, name, texturePath, value);
	}

	@Test
	public void t00_updateSprite() {
		// Update the sprite
		item.updateSprite(new Texture(texturePath));
		// Check if the sprite is not null
		assertNotNull("Item sprite didn't update", item.sprite);
	}

	@Test
	public void t01_updateSpriteSize() {
		float initialH = item.getHeight();
		float initialW = item.getWidth();
		// Update the sprite size
		item.setSize(12345.67f, 891011.12f);
		// Check if the sprite is not null
		assertNotEquals("Item sprite height didn't update", initialH, item.getHeight());
		assertNotEquals("Item sprite width didn't update", initialW, item.getWidth());
	}

}

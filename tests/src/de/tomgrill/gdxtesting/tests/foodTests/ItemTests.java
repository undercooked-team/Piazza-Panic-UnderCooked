package de.tomgrill.gdxtesting.tests.foodTests;

import com.badlogic.gdx.assets.AssetManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Item;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemTests {

	static Item item;
	static AssetManager assetManager;
	static TextureManager textureManager;
	static String itemID = "<main>:item/lettuce.png";
	static String texturePath = itemID;
	static String name = "Lettuce";
	static int value = 1;

	@BeforeClass
	public static void setup() {
		// Set up variables
		item = new Item(itemID, name, texturePath, value);
		assetManager = new AssetManager();
		textureManager = new TextureManager(assetManager);
		// Load the item's texture
		textureManager.loadAsset("items", texturePath);
		assetManager.finishLoading();
	}

	@Test
	public void t00_updateSprite() {
		// Update the sprite
		item.updateSprite(textureManager.getAsset(item.getTexturePath()));
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

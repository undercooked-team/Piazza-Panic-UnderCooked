package de.tomgrill.gdxtesting.tests.foodTests;

import de.tomgrill.gdxtesting.GdxTestRunner;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Items;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsTests {
	static Items items;
	static TextureManager textureManager;
	static AssetManager assetManager;

	@BeforeClass
	public static void setup() {
		items = new Items();
		assetManager = new AssetManager();
		textureManager = new TextureManager(assetManager);
	}

	@Test
	public void t00_addItemNormalCases() {
		String itemID = "<main>:lettuce.png";
		String texturePath = FileControl.getAssetPath(itemID, "items");
		String name = "Lettuce";
		int value = 1;
		items.addItem(itemID, name, texturePath, value);
		// Check if the item was added
		assertNotNull("Item was not added.", items.getItem(itemID));

		String itemID2 = "<main>:patty_cooked.png";
		String texturePath2 = FileControl.getAssetPath(itemID, "items");
		String name2 = "Cooked Patty";
		int value2 = 2;
		items.addItem(itemID2, name2, texturePath2, value2);
		// Check if the item was added
		assertNotNull("Item was not added.", items.getItem(itemID));
	}

	@Test
	public void t01_addItemDuplicateCases() {
		String itemID = "<main>:lettuce.png";
		String texturePath = FileControl.getAssetPath(itemID, "items");
		String name = "Lettuce";
		int value = 1;
		items.addItem(itemID, name, texturePath, value);
		// Check if the item was added
		assertNotNull("Item was not added.", items.getItem(itemID));

		String itemID2 = "<main>:patty_cooked.png";
		String texturePath2 = FileControl.getAssetPath(itemID, "items");
		String name2 = "Cooked Patty";
		int value2 = 2;
		items.addItem(itemID2, name2, texturePath2, value2);
		// Check if the item was added
		assertNotNull("Item was not added.", items.getItem(itemID));
	}

	@Test
	public void t02_getItemErrorCases() {
		String itemID = "I'm not an itemID :D";
		String texturePath = FileControl.getAssetPath(itemID, "items");
		String name = "Something";
		int value = -1;
		items.addItem(itemID, name, texturePath, value);
		// Check if the item was added
		assertNull("Invalid item was added.", items.getItem(itemID));

		String itemID2 = "neither am I";
		String texturePath2 = FileControl.getAssetPath(itemID, "items");
		String name2 = "Else";
		int value2 = -2;
		items.addItem(itemID2, name2, texturePath2, value2);
		// Check if the item was added
		assertNull("Invalid item was added.", items.getItem(itemID));
	}

	@Test
	public void t10_load() {
		items.load(textureManager, "");
		assetManager.isFinished();

		for (ObjectMap.Entry<String, Item> item : items.getItems()) {
			assertNotNull(String.format("Item %s had a null texture when it shouldn't.", item.key),
					item.value.getTexturePath());
		}
	}

	@Test
	public void t20_unload() {
		items.unload(textureManager);
		assetManager.isFinished();

		assertFalse("Lettuce texture was still loaded when it should've unloaded.",
				assetManager.isLoaded(FileControl.getAssetPath("<main>:lettuce.png", "items"), Texture.class));

		assertEquals("Items was not cleared.", items.getItems(), new ObjectMap<String, Item>());
	}

	@Test
	public void t30_addItemAsset() {
		String itemID = "<main>:lettuce.png";
		String texturePath = FileControl.getAssetPath(itemID, "items");

		items.addItemAsset(texturePath);
		// Check if the item was added
		assertNotNull("Item was not added.", items.getItem(itemID));
	}

}

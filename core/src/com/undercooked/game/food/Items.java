package com.undercooked.game.food;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * All available ingredient.
 */
public class Items {

	/** An {@link ObjectMap} of the {@link Item}s. */
	private final ObjectMap<String, Item> items;

	/**
	 * Constructor for the class, which sets up the {@link #items} {@link ObjectMap}.
	 */
	public Items() {
		this.items = new ObjectMap<>();
	}

	/**
	 * If the item id doesn't exist yet, it creates a new {@link Item}
	 * and adds it to the {@link #items}, returning the newly created {@link Item}.
	 * <br>
	 * If the item id already exists, it instead returns the already created {@link Item}.
	 * 
	 * @param ID {@link String} : The item's id.
	 * @param name {@link String} : The display name of the item.
	 * @param texturePath {@link String} : The path to the {@link com.badlogic.gdx.graphics.Texture}.
	 * @param value {@code int} : The value of the {@link Item}.
	 * @return {@link Item} : The newly created {@link Item} if the id didn't yet exist,
	 * 						  or the {@link Item} that was created for the id already.
	 */
	public Item addItem(String ID, String name, String texturePath, int value) {
		// If the item already exists, return that
		if (items.containsKey(ID)) {
			return items.get(ID);
		}
		Item newItem = new Item(ID, name, texturePath, value);
		System.out.println(String.format("New Item (%s, %s) with texturePath '%s' and value '%d'",
				name, ID, texturePath, value));
		items.put(ID, newItem);
		return newItem;
	}

	/**
	 * Adds an {@link Item} using the {@link #addItem(String, String, String, int)}
	 * by loading it from a {@link JsonValue} at an asset path, if the id isn't created
	 * already.
	 * <br>
	 * If the item id already exists, it instead returns the already created {@link Item}.
	 * <br>
	 * The id for the {@link Item} is the asset path.
	 *
	 * @param assetPath {@link String} : The path to the asset.
	 * @return {@link Item} : The newly created {@link Item} if the id didn't yet exist,
	 * 	 * 				      or the {@link Item} that was created for the id already.
	 */
	public Item addItemAsset(String assetPath) {
		if (items.containsKey(assetPath)) {
			return items.get(assetPath);
		}
		JsonValue ingredientRoot = FileControl.loadJsonAsset(assetPath, "items");
		if (ingredientRoot == null) {
			return null;
		}
		JsonFormat.formatJson(ingredientRoot, DefaultJson.itemFormat());
		Item newItem = addItem(assetPath,
				ingredientRoot.getString("name"),
				ingredientRoot.getString("texture_path"),
				ingredientRoot.getInt("value"));
		newItem.setSize(ingredientRoot.getFloat("width"), ingredientRoot.getFloat("height"));
		return newItem;
	}

	/**
	 * Returns the {@link Item} mapped to the id provided.
	 *
	 * @param itemID {@link String} : The id of the {@link Item}.
	 * @return {@link Item} : The {@link Item} mapped to the id, or
	 * 						  {@code null} if it doesn't exist.
	 */
	public Item getItem(String itemID) {
		return items.get(itemID);
	}

	/**
	 * Set to load all of the {@link Item}s' {@link com.badlogic.gdx.graphics.Texture}s
	 * through the {@link TextureManager}.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
	 * @param textureGroup {@link String} : The texture group to load to.
	 */
	public void load(TextureManager textureManager, String textureGroup) {
		// Loop through all ingredients and load their textures
		for (Item item : items.values()) {
			System.out.println(String.format("Loading texture %s for item %s.", item.getTexturePath(), item.name));
			textureManager.loadAsset(textureGroup, item.getTexturePath(), "textures");
		}
	}

	/**
	 * Post load all of the {@link Item}s using the {@link TextureManager}.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
	 */
	public void postLoad(TextureManager textureManager) {
		// Loop through all ingredients and set their textures
		for (Item item : items.values()) {
			System.out.println(String.format("Giving texture %s to item %s", item.getTexturePath(), item.name));
			item.updateSprite(textureManager.getAsset(item.getTexturePath()));
			// item.updateSprite(textureManager.getAsset("<main>:station/blank.png"));
		}
	}

	/**
	 * Unload all of the {@link Item}s using the {@link TextureManager}.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
	 */
	public void unload(TextureManager textureManager) {
		// Loop through all the ingredients and unload their textures
		for (Item item : items.values()) {
			textureManager.unloadTexture(item.getTexturePath());
		}
		// Clear the ingredients map, as none of them are loaded now
		items.clear();
	}

}

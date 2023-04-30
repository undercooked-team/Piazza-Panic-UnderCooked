package com.undercooked.game.station;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.interactions.StationInteractControl;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.map.MapManager;

/**
 * The class for the Stations that appear on the {@link Map}.
 */
public class Station extends MapEntity {
	/**
	 * The station's {@link StationInteractControl}, for its interactions.
	 * @see Interactions
	 */
	StationInteractControl interactControl;

	/** The {@link StationData} of the {@link Station}. */
	private StationData stationData;

	/** The {@link Item}s that the {@link Station} is holding. */
	public ItemStack items;
	
	/** The {@link Cook}s that are locked to the {@link Station}. */
	private final Array<Cook> lockedCooks;
	
	/** 
	 * The amount of money needed to unlock the {@link Station}.
	 * Set with {@link #setPrice(int)}.
	 * <br><br>
	 * If the price is &lt;= 0, then the {@link Station} will be
	 * enabled.
	 * <br>If the price is &gt; 0, it will be disabled.
	 */
	private int price;

	/**
	 * Whether the {@link Station} is disabled or not.
	 * <br><br>
	 * If it is disabled, it will be drawn darker. Interactions
	 * will not work, and items can't be dropped onto or picked up
	 * from them.
	 */
	private boolean disabled;

	/** If the {@link Station} has collision or not. */
	public boolean hasCollision;
	public Station(StationData stationData) {
		super();
		setStationData(stationData);
		this.texturePath = stationData.getTexturePath();
		this.items = new ItemStack();
		this.lockedCooks = new Array<>();
		this.setBasePath(stationData.getDefaultBase());
		setWidth(stationData.getWidth());
		setHeight(stationData.getHeight());
	}

	/**
	 * Updates the interactions of the {@link Station}.
	 *
	 * @param delta {@code float} : The time since the last frame.
	 * @param powerUpMultiplier {@code float} : The multiplier from power ups.
	 */
	public void update(float delta,float powerUpMultiplier) {
		// Only continue if not disabled
		if (disabled)
			return;
		if (lockedCooks.size == 0) {
			interactControl.update(null, powerUpMultiplier);
			return;
		}
		interactControl.update(lockedCooks.get(0), powerUpMultiplier);
	}

	@Override
	public InteractResult interact(Cook cook, String keyID, InputType inputType) {
		// If disabled, stop here
		if (disabled)
			return InteractResult.NONE;

		if (interactControl == null) {
			// If it doesn't have an interaction control, then stop.
			return InteractResult.STOP;
		}
		return interactControl.interact(cook, keyID, inputType);
	}

	/**
	 * Called when the player is trying to buy the {@link Station}.
	 * 
	 * @param money {@code int} : The money provided.
	 */
	public boolean buy(int money) {
		// If money < price, then the purchase has failed
		if (money < price) {
			return false;
		}
		// Otherwise, the purchase was a success and the station can
		// be enabled.
		disabled = false;
		return true;
	}

	/**
	 * Returns where an item should appear visually on the {@link Station}
	 * offset from the middle of it.
	 *
	 * @param num {@code int} : The position to get.
	 * @return {@link Vector2} : Relative position from the center of
	 * 							 the {@link Station}.
	 */
	public Vector2 itemPos(int num) {
		// Make sure num is in range
		num = Math.abs(num);
		num %= 8;
		switch (num) {
			case 0:
				return new Vector2(-16, -16);
			case 1:
				return new Vector2(16, -16);
			case 2:
				return new Vector2(-16, 16);
			case 3:
				return new Vector2(16, 16);
			case 4:
				return new Vector2(0, -16);
			case 5:
				return new Vector2(-16, 0);
			case 6:
				return new Vector2(16, 0);
			case 7:
				return new Vector2(0, 16);
			default:
				return new Vector2(0, 0);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// If it's disabled, change the draw colour to be darker
		if (disabled) {
			batch.setColor(0.4f, 0.4f, 0.4f, 1f);
		}
		// Draw the Sprite
		super.draw(batch);
		// Then draw the items on top of the station
		for (int i = 0; i < items.size(); i++) {
			Vector2 itemPos = itemPos(i);
			Item thisItem = items.get(i);
			thisItem.draw(batch, pos.x + sprite.getWidth() / 2f + itemPos.x - thisItem.getWidth() / 2f,
					pos.y + sprite.getHeight() / 2f + itemPos.y - thisItem.getHeight() / 2f);
		}
		// If it's disabled, reset the draw colour and stop
		if (disabled) {
			batch.setColor(1, 1, 1, 1);
			return;
		}
		// And then draw the interaction
		interactControl.draw(batch);
	}

	@Override
	public void drawPost(SpriteBatch batch) {
		// Do post drawing for the interaction
		interactControl.drawPost(batch);
	}

	@Override
	public void draw(ShapeRenderer shape) {
		// Draw the interaction
		interactControl.draw(shape);
	}

	/**
	 * Creates the {@link StationInteractControl} for the {@link Station}.
	 *
	 * @param audioManager {@link AudioManager} : The {@link AudioManager} to use.
	 * @param gameItems {@link Items} : The {@link Items} to of the game.
	 */
	public void makeInteractionController(AudioManager audioManager, Items gameItems) {
		this.interactControl = new StationInteractControl(this, audioManager, gameItems);
		updateStationInteractions();
	}

	/**
	 * Returns whether the {@link Station} has a specific {@link Item}
	 * (or any {@link Item}) a number of times.
	 * 
	 * @param itemID {@link String} : The itemID to check for.
	 * @param number {@code int} : The number of {@link Item}s needed.
	 * @return {@code boolean} : {@code true} if it does,
	 *         {@code false} if it does not.
	 */
	public boolean hasItem(String itemID, int number) {
		// If itemID is null, just compare the size and number
		if (itemID == null) {
			return items.size() >= number;
		}
		// If it's <= 0, then return true
		if (number <= 0) {
			return true;
		}
		// Otherwise, if itemID is not null then check the number
		// of times that itemID occurs.
		// Check all items
		int numFound = 0;
		for (Item item : items) {
			// If item's ID and itemID match...
			if (item.getID() == itemID) {
				// Increase numFound
				numFound += 1;
			}
		}
		// Return comparison of numFound and number
		return numFound >= number;
	}

	/**
	 * Returns if the {@link Station} can hold items or not.
	 * @param number {@link int} : The number of items that are trying to
	 *                             be added.
	 * @return {@code boolean} : {@code true} if the items can be held,
	 * 							 {@code false} if they cannot.
	 */
	public boolean canHoldItems(int number) {
		if (number <= 0) {
			return true;
		}
		// Return whether adding it goes above the limit or not
		return !(items.size() + number > stationData.getHoldCount());
	}

	/**
	 * Returns if the {@link Station} can hold an item or not.
	 * @return {@code boolean} : {@code true} if an item can be held,
	 * 							 {@code false} if it cannot.
	 */
	public boolean canHoldItem() {
		return canHoldItems(1);
	}

	/**
	 * Adds an {@link Item} to the {@link Station}'s {@link ItemStack},
	 * if it can hold it.
	 *
	 * @param item {@link Item} : The {@link Item} to add.
	 * @return {@code boolean} : {@code true} if the {@link Item} could be added,
	 * 							 {@code false} if it cannot.
	 */
	public boolean addItem(Item item) {
		// Only continue if it CAN add an item
		if (!canHoldItem()) {
			return false;
		}
		// Add the item
		items.add(item);
		// Update Station Interactions.
		updateStationInteractions();
		return true;
	}

	/**
	 * Takes the topmost {@link Item} from the {@link Station}'s
	 * {@link ItemStack}.
	 *
	 * @return {@link Item} : The {@link Item} that was taken.
	 */
	public Item takeItem() {
		// Only continue if it has an item
		if (items.size() <= 0) {
			return null;
		}
		// Return the item that was popped.
		Item returnItem = items.pop();
		// Update Station Interactions.
		updateStationInteractions();
		// Return the item that was taken.
		return returnItem;
	}

	/**
	 * Clears the {@link Station}'s {@link ItemStack}.
	 */
	public void clear() {
		// Clear all items
		items.clear();
		// Update station interactions
		updateStationInteractions();
	}

	/**
	 * Updates the current interaction that is being done
	 * by the {@link Station}.
	 */
	public void updateStationInteractions() {
		interactControl.setCurrentInteraction(interactControl.findValidInteraction(items));
	}

	/**
	 * Set the {@link StationData} that the {@link Station} is
	 * using.
	 *
	 * @param stationData {@link StationData} : The {@link StationData} to use.
	 */
	public void setStationData(StationData stationData) {
		this.stationData = stationData;
		this.id = stationData.getID();
		setPrice(stationData.getPrice());
	}

	/**
	 * Set whether the {@link Station} is disabled or not.
	 * @param disabled {@code boolean} : Whether the {@link Station} is disabled
	 *                                   or not.
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * Returns {@code true} or {@code false} depending on if the
	 * {@link Station} has that number of items or not.
	 * 
	 * @param number {@code int} : The number of {@link Item}s.
	 * @return {@code boolean} : {@code true} if it has that many {@link Item}s,
	 *         {@code false} if it does not.
	 */
	public boolean hasItem(int number) {
		return hasItem(null, number);
	}

	/**
	 * Returns a {@code boolean} on whether the {@link Station}
	 * has an item or not.
	 * 
	 * @return {@code boolean} : {@code true} if it has an {@link Item},
	 *         * {@code false} if it does not.
	 */
	public boolean hasItem() {
		return hasItem(null, 1);
	}

	/**
	 * Updates the {@link Station}'s possible interactions that can
	 * be taken
	 */
	public void updateInteractions() {
		this.interactControl.updatePossibleInteractions(stationData.getID());
	}

	/**
	 * Sets the {@link Interactions} class instance that will be used for
	 * the {@link StationInteractControl}.
	 *
	 * @param interactions {@link Interactions} : The {@link Interactions} to use.
	 */
	public void setInteractions(Interactions interactions) {
		this.interactControl.setInteractions(interactions);
	}

	/**
	 * Set the price of the {@link Station}.
	 * <br><br>
	 * If the price is &lt;= 0, the {@link Station} will be enabled.
	 * <br>If the price is &gt; 0, then the {@link Station} will be disabled.
	 * @param price {@code int} : The price of the {@link Station}.
	 */
	public void setPrice(int price) {
		this.price = price;
		// Set it to be disabled or not depending on the price
		this.disabled = (price > 0);
	}

	/**
	 * @return {@code int} : The price of the {@link Station}.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return {@code boolean} : {@code true} if the {@link Station} is disabled,
	 * 							 {@code false} if not.
	 */
	public boolean isDisabled() {
		return this.disabled;
	}

	/**
	 * @return {@code boolean} : {@code true} if a {@link Cook} is locked,
	 * 							 {@code false} if not.
	 */
	public boolean hasCookLocked() {
		return this.lockedCooks.size > 0;
	}

	/**
	 * @param cook {@link Cook} : The {@link Cook} to check is locked to the {@link Station}.
	 * @return {@code boolean} : {@code true} if the {@link Cook} is locked,
	 * 							 {@code false} if not.
	 */
	public boolean hasCookLocked(Cook cook) {
		return this.lockedCooks.contains(cook, true);
	}

	/**
	 * Locks a {@link Cook} to the {@link Station}.
	 *
	 * @param cook {@link Cook} : The {@link Cook} to lock.
	 */
	public void lockCook(Cook cook) {
		// If the cook isn't already locked...
		if (hasCookLocked(cook)) {
			return;
		}
		// Then add the cook.
		this.lockedCooks.add(cook);
		// Then tell the cook to lock
		cook.lockToStation(this);
	}

	/**
	 * Unlocks a {@link Cook} from the {@link Station}.
	 *
	 * @param cook {@link Cook} : The {@link Cook} to unlock.
	 */
	public void unlockCook(Cook cook) {
		// If the cook is locked...
		if (!hasCookLocked(cook)) {
			return;
		}
		// Remove the Cook
		this.lockedCooks.removeValue(cook, true);
		// And then tell the cook to remove its lock
		cook.unlock();
	}

	/**
	 * Unlocks all of the {@link Cook}s on the {@link Station}.
	 */
	public void unlockCooks() {
		for (int i = lockedCooks.size - 1; i >= 0; i--) {
			unlockCook(lockedCooks.get(i));
		}
	}

	/**
	 * Resets the {@link Station}.
	 */
	public void reset() {
		// Clear the station
		clear();

		// If any cooks are locked, then unlock them
		if (hasCookLocked()) {
			unlockCooks();
		}
	}

	/**
	 * Stops the {@link Station}'s current interaction.
	 */
	public void stop() {
		// Stop the interaction
		interactControl.stop();
	}

	/**
	 * Serializes the {@link Station} as a {@link JsonValue} to
	 * be stored externally.
	 *
	 * @param map {@link Map} : The {@link Map} that the {@link Station} is on.
	 * @return {@link JsonValue} : The {@link Station} in Json format.
	 */
	public JsonValue serial(Map map) {
		// If the station is not unlocked, or if it has no items on it, then just ignore
		// as there's no reason to save anything
		if (!(!disabled && price > 0)) {
			if (items.size() == 0) {
				return null;
			}
		}
		// Get ItemIDs from the station items
		JsonValue theItemIds = new JsonValue(JsonValue.ValueType.array);
		for (Item item : items) {
			theItemIds.addChild("", new JsonValue(item.getID()));
		}

		// Return JsonValue
		JsonValue stationRoot = new JsonValue(JsonValue.ValueType.object);
		stationRoot.addChild("station_id", new JsonValue(id));
		stationRoot.addChild("base_texture", new JsonValue(basePath));
		stationRoot.addChild("has_collision", new JsonValue(hasCollision));
		stationRoot.addChild("x", new JsonValue(MapManager.posToGridFloor(pos.x) - map.getOffsetX()));
		stationRoot.addChild("y", new JsonValue(MapManager.posToGridFloor(pos.y) - map.getOffsetY()));
		stationRoot.addChild("price", new JsonValue(price));
		stationRoot.addChild("disabled", new JsonValue(disabled));
		stationRoot.addChild("items", theItemIds);

		return stationRoot;
	}
}

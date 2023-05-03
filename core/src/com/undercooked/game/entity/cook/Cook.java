package com.undercooked.game.entity.cook;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.MoveableEntity;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.input.InputController;
import com.undercooked.game.input.InputType;
import com.undercooked.game.input.Keys;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.station.Station;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.Observer;

/**
 * A class for the player's way of playing and interacting with the
 * game {@link Map} and other {@link com.undercooked.game.entity.Entity}s.
 */
public class Cook extends MoveableEntity {
  private static final int FRAME_COLS = 5;
  private static final int FRAME_ROWS = 4;

  private Vector2 direction;
  private final int cookno;
  private final TextureManager textureManager;
  private Animation<TextureRegion> walkAnimation;
  private TextureRegion[][] spriteSheet;
  private TextureRegion currentFrame;
  private float stateTime = 0;

  private final Rectangle interactCollision;
  private MapCell interactTarget;
  private Station stationTarget;
  private int holdLimit;

  /**
   * The {@link Station} that the {@link Cook} is locked to.
   */
  public Station lockedTo = null;

  /**
   * The {@link Item}s that the {@link Cook} is currently holding.
   */
  public ItemStack heldItems;

  private final Map map;

  /**
   * Multiplier for the speed of the {@link Cook}.
   */
  protected float speedMultiplier;

  /**
   * The {@link Listener} to tell when the {@link Cook} is trying to serve an
   * {@link Item}.
   */
  protected Listener<Cook> serveListener;

  /**
   * The {@link Listener} to tell when the {@link Cook} interacts with a register
   * {@link MapCell}.
   */
  protected Listener<MapCell> interactRegisterListener;

  /**
   * The {@link Listener} to tell when the {@link Cook} interacts with a phone
   * {@link MapCell}.
   */
  protected Listener<MapCell> interactPhoneListener;

  /**
   * The {@link Listener} to tell when the {@link Cook} buys something.
   */
  protected Listener<Integer> moneyUsedListener;

  /**
   * The {@link Observer} to find out how much money the player has.
   */
  protected Observer<Integer> moneyObserver;

  /**
   * Constructor for the {@link Cook}.
   *
   * @param pos            {@link Vector2} : The position of the {@link Cook}.
   * @param cookNum        {@link int} : The number of the cook {@link Texture} to
   *                       use.
   * @param textureManager {@link TextureManager} : The {@link TextureManager} to
   *                       use.
   * @param map            {@link Map} : The {@link Map} to use for collisions.
   */
  public Cook(Vector2 pos, int cookNum, TextureManager textureManager, Map map) {
    super();
    this.pos = pos;
    this.collision.x = pos.x;
    this.collision.y = pos.y;
    this.cookno = cookNum;
    this.speedMultiplier = 1f;

    this.heldItems = new ItemStack();
    this.holdLimit = 5;

    this.interactTarget = null;
    this.stationTarget = null;
    this.interactCollision = new Rectangle(collision.x, collision.y, 16, 16);

    collision.width = 40;
    offsetX = (64 - collision.getWidth()) / 2;
    collision.height = 10;
    offsetY = -collision.height / 2;
    speed = 2.5f;
    direction = new Vector2(0, -1);

    this.textureManager = textureManager;
    this.map = map;
  }

  @Override
  public void load(TextureManager textureManager, String textureGroup) {
    textureManager.load(textureGroup, "entities/cook_walk_" + cookno + ".png");
    textureManager.load(textureGroup, "entities/cook_walk_hands_" + cookno + ".png");
  }

  @Override
  public void postLoad(TextureManager textureManager) {
    updateTexture();
    // setWalkTexture("entities/cook_walk_" + cookno + ".png");
  }

  /**
   * Check the player's inputs.
   */
  public void checkInput() {
    // Check inputs, and change things based on the result
    dirX = 0;
    dirY = 0;
    // Only check movement if not locked to a station
    if (lockedTo == null) {
      movementCheck();
    }

    // Interact input check
    interactInputCheck();
  }

  private void movementCheck() {
    if (InputController.isInputPressed(Keys.cook_down)) {
      dirY -= 1;
      setWalkFrames(0);
      direction.set(0, -1);
    }
    if (InputController.isInputPressed(Keys.cook_up)) {
      dirY += 1;
      setWalkFrames(2);
      direction.set(0, 1);
    }
    if (InputController.isInputPressed(Keys.cook_left)) {
      dirX -= 1;
      setWalkFrames(1);
      direction.set(-1, 0);
    }
    if (InputController.isInputPressed(Keys.cook_right)) {
      dirX += 1;
      setWalkFrames(3);
      direction.set(1, 0);
    }
  }

  private void interactInputCheck() {
    if (interactTarget == null || stationTarget == null) {
      return;
    }
    // If they're not null, make sure they're not disabled
    if (stationTarget.isDisabled()) {
      // If they're disabled, then try to buy the station if
      // interact is pressed
      if (InputController.isInputJustPressed("interact")) {
        if (moneyUsedListener != null && moneyObserver != null) {
          if (stationTarget.buy(moneyObserver.observe())) {
            moneyUsedListener.tell(stationTarget.getPrice());
          }
        }
      }
      return;
    }

    /// Custom station interactions
    // If it's a phone...
    if (stationTarget.getId().equals(Constants.PHONE_ID)) {
      // Then check if the player is interacting with it
      if (InputController.isInputJustPressed("interact")) {
        // If they are, and the listener for it isn't null, then tell
        // the phone interaction listener that it has been interacted with
        if (interactPhoneListener != null) {
          interactPhoneListener.tell(interactTarget);
        }
        return;
      }

      // Phones can not have any other interactions
      return;
    }

    // If it's a Register...
    if (stationTarget.getId().equals(Constants.REGISTER_ID)) {
      // Then do a few custom checks
      // If they're trying to interact, call the Interact listener
      // if it exists
      if (InputController.isInputJustPressed("interact")) {
        if (interactRegisterListener != null) {
          interactRegisterListener.tell(interactTarget);
        }
        return;
      }

      // If the above doesn't apply, then check if they're trying to
      // put down their item
      if (InputController.isInputJustPressed("drop")) {
        // If they are, check if the serve listener exists
        if (serveListener != null) {
          // If it does, then tell it that the Cook is trying to serve
          serveListener.tell(this);
        }
        return;
      }

      // Registers can not have any other interactions.
      return;
    }

    // Station interactions
    // Check for station interactions
    if (InputController.isInputJustPressed("take")) {
      // If the station has an item, take it
      if (stationTarget.items.size() > 0) {
        // If the Cook can also take it...
        if (canAddItem()) {
          // Then take the top item on the Station's stack
          addItem(stationTarget.takeItem());
          // If the cook is locked, also unlock them
          if (stationTarget.hasCookLocked()) {
            unlock();
          }
          // If it succeeds, stop here
          return;
        }
      }
    }

    if (InputController.isInputJustPressed("drop")) {
      // If the cook has an item, drop it
      if (heldItems.size() > 0) {
        // If the station can hold it...
        if (stationTarget.canHoldItem()) {
          // Then add it
          stationTarget.addItem(takeItem());
          // If it succeeds, stop here
          return;
        }
      }
    }

    interactionsCheck();

  }

  private void interactionsCheck() {
    // Make sure the target is still valid
    if (interactTarget == null || stationTarget == null) {
      return;
    }

    // Check for every input
    for (Object curKey : InputController.getInputs().keys().iterator()) {
      String keyId = (String) curKey;
      // If it's not an interaction key, then ignore and skip
      if (!InputController.isInteraction(keyId)) {
        continue;
      }
      InteractResult interactResult = InteractResult.NONE;
      // Loop through the InputTypes
      for (InputType inputType : InputType.values()) {
        if (InputController.isInput(keyId, inputType)) {
          if (interactTarget.getMapEntity() == null) {
            return;
          }
          interactResult = interactTarget.getMapEntity().interact(this, keyId, inputType);
          // If it's repeat, then repeat the check
          if (interactResult == InteractResult.RESTART) {
            // Start the check over again
            interactionsCheck();
            // And then stop this check
            interactResult = InteractResult.STOP;
          }
          if (interactResult == InteractResult.STOP) {
            break;
          }
        }
      }
      // If it's not null for interaction result, then just ignore other keys.
      if (interactResult == InteractResult.STOP) {
        break;
      }
    }
  }

  /**
   * Update the {@link Cook}.
   *
   * @param delta {@code float} : The time since the last frame.
   */
  public void update(float delta) {
    // Update super
    super.update(delta);

    // If the player is holding more than their limit, then lower their speed
    float finalSpeedMult = speedMultiplier;
    if (heldItems.size() > holdLimit) {
      // Decrease speed by the number of items held, up to a max of 4 times
      finalSpeedMult -= (0.15f * Math.min(4, heldItems.size() - holdLimit));
    }

    // Move
    moveAndCollide(map, dirX * finalSpeedMult, dirY * finalSpeedMult, delta);
    if (collidedX) {
      dirX = 0;
    }
    if (collidedY) {
      dirY = 0;
    }

    interactCollision.x = collision.x + collision.width / 2
            + (direction.x * 32) - interactCollision.width / 2;
    interactCollision.y = 64 + collision.y + collision.height / 2 + (direction.y * 32)
            - interactCollision.width / 2;

    // Update the cell that is currently being looked at
    interactTarget = map.getCollision(interactCollision, true, Map.MapCellType.INTERACTABLE);
    if (interactTarget != null) {
      if (interactTarget.getMapEntity() != null) {
        Station asStation = (Station) interactTarget.getMapEntity();
        // Only set stationTarget if another cook isn't locked to it
        if (!asStation.hasCookLocked() || asStation.hasCookLocked(this)) {
          // If it doesn't have a cook, then it's the target.
          stationTarget = asStation;
        } else {
          // Otherwise, it can't be the target, as it's already
          // the target of another cook.
          stationTarget = null;
        }
      } else {
        stationTarget = null;
      }
    } else {
      stationTarget = null;
    }

    // Update animation
    currentFrame = walkAnimation.getKeyFrame(stateTime, true);
    if (dirX != 0 || dirY != 0) {
      stateTime += delta;
    } else {
      stateTime = 0;
    }

    // Reset move speed
    dirX = 0;
    dirY = 0;
  }

  @Override
  public void draw(SpriteBatch batch) {
    batch.draw(currentFrame, pos.x, pos.y, 64, 128);
    drawHeldItems(batch);
  }

  @Override
  public void drawDebug(ShapeRenderer shape) {
    super.drawDebug(shape);
    shape.setColor(Color.GREEN);
    shape.rect(interactCollision.x, interactCollision.y,
            interactCollision.width, interactCollision.height);
    shape.setColor(Color.WHITE);
  }

  /**
   * Draw the top half of the {@link Cook} at the location provided.
   *
   * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
   * @param x     {@code float} : The {@code x} position to draw at.
   * @param y     {@code float} : The {@code y} position to draw at.
   */
  public void draw_top(SpriteBatch batch, int x, int y) {
    TextureRegion chefTop = new TextureRegion(currentFrame, 0, 0, currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight() / 2);
    batch.draw(chefTop, x, y, 128, 128);
  }

  /**
   * Draw the items that the {@link Cook} is holding.
   *
   * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
   */
  public void drawHeldItems(SpriteBatch batch) {
    int itemIndex = 0;
    int currentOffset = 0;
    for (Item item : heldItems) {
      // if (ingredient.sprite.getTexture() != null) {
      item.draw(batch, pos.x + 32 - item.getWidth() / 2f, pos.y + 112 + currentOffset);
      // batch.draw(item.sprite, pos.x + 16, pos.y + 112 + itemIndex * 8, 32, 32);
      currentOffset += item.getHeight() / 2f;
      itemIndex++;
      // }
    }
  }

  /**
   * Set the speed multiplier of the {@link Cook}.
   *
   * @param multiplier {@code float} : The value to multiply speed by.
   */
  public void setSpeed(float multiplier) {
    this.speedMultiplier = multiplier;
  }

  /**
   * Set the maximum number of items the {@link Cook} can hold.
   *
   * @param maxItems {@code int} : The maximum number of items
   */
  public void setHoldLimit(int maxItems) {
    this.holdLimit = maxItems;
  }

  /**
   * Set the texture to draw the {@link Cook}.
   *
   * @param path {@link String} : The path to the {@link Texture}.
   */
  private void setWalkTexture(String path) {
    Texture walkSheet = textureManager.get(path);
    spriteSheet = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS,
            walkSheet.getHeight() / FRAME_ROWS);
    // walkFrames = new TextureRegion[FRAME_COLS];
    setWalkFrames(0);
  }

  /**
   * Set the walk frames to use.
   *
   * @param row {@code int} : The row on the {@link #spriteSheet}.
   */
  private void setWalkFrames(int row) {
    /*
     * for (int i = 0; i < FRAME_COLS; i++) {
     * walkFrames[i] = spriteSheet[row][i];
     * }
     */
    walkAnimation = new Animation<>(0.09f, spriteSheet[row]);
    currentFrame = walkAnimation.getKeyFrame(stateTime, true);
  }

  /**
   * Returns the x position.
   *
   * @return {@code float} : The x position.
   */
  public float getX() {
    return pos.x;
  }

  /**
   * Returns the y position.
   *
   * @return {@code float} : The y position.
   */
  public float getY() {
    return pos.y;
  }

  /**
   * Returns the width of the collision.
   *
   * @return {@code float} : The collision width.
   */
  public float getWidth() {
    return collision.width;
  }

  /**
   * Returns the height of the collision.
   *
   * @return {@code float} : The collision height.
   */
  public float getHeight() {
    return collision.height;
  }

  /**
   * Returns the current direction the cook is looking, as a {@link Vector2}.
   * The values are:
   * <br>-1: x = left, y = down
   * <br>0: no direction
   * <br>1: x = right, y = up
   *
   * @return {@link Vector2} : The direction the {@link Cook} is facing.
   */
  public Vector2 getDirection() {
    return direction;
  }

  /**
   * Returns the current {@link MapCell} that is being targeted by the
   * {@link Cook}.
   *
   * @return {@link MapCell} : The current {@link MapCell} interaction target
   *                           of the {@link Cook}.
   */
  public MapCell getInteractTarget() {
    return interactTarget;
  }

  /**
   * Returns the current {@link Station} that is being targeted by the
   * {@link Cook}.
   * <br>
   * If the {@link MapCell} target isn't {@code null}, this can still be
   * null if the {@link Station} target is invalid. For example, another
   * {@link Cook} is locked to the station, and therefore it can't be
   * targeted.
   *
   * @return {@link Station} : The current {@link Station} interaction target
   *                             of the {@link Cook}.
   *                           Returns {@code null}, even if {@link #getInteractTarget()}
   *                             does not, if the {@link Station} is an invalid target.
   * @see #getInteractTarget()
   */
  public Station getStationTarget() {
    return stationTarget;
  }

  /**
   * Returns whether the {@link Cook} can hold a number more {@link Item}s.
   *
   * @param number {@code int} : The number of check.
   * @return {@code boolean} : {@code true} if the {@link Cook}
   *                           can hold the {@link Item},
   *                           {@code false} if not.
   */
  public boolean canAddItems(int number) {
    if (number <= 0) {
      return true;
    }
    // Return whether adding it goes above the limit or not
    return !(heldItems.size() + number > holdLimit);
  }

  /**
   * Returns whether the {@link Cook} can hold at least one more {@link Item}s.
   *
   * @return {@code boolean} : {@code true} if the {@link Cook}
   *                           can hold the {@link Item},
   *                           {@code false} if not.
   */
  public boolean canAddItem() {
    return canAddItems(1);
  }

  /**
   * Tries to add an {@link Item} to the {@link Cook}'s held items,
   * but fails if it goes over their hold limit.
   *
   * @param item {@link Item} : The {@link Item} to add.
   * @return {@code boolean} : {@code true} if it was able to be added,
   *                           {@code false} if it was not.
   */
  public boolean addItem(Item item) {
    // Continue only if it can add it
    if (!canAddItem()) {
      return false;
    }
    // Add the item
    heldItems.add(item);
    updateTexture();
    return true;
  }

  /**
   * Take an item from the top of the {@link Cook}'s held items.
   *
   * @return {@link Item} : The {@link Item} at the top of the {@link ItemStack},
   *                        or {@code null} if there is none.
   */
  public Item takeItem() {
    // Continue only if the Cook has an item to remove
    if (heldItems.size() <= 0) {
      return null;
    }
    // Pop the item and return it
    Item poppedItem = heldItems.pop();
    updateTexture();
    return poppedItem;
  }

  /**
   * Clears the {@link Cook}'s held items, and updates the current {@link Texture}.
   */
  public void clear() {
    heldItems.clear();
    updateTexture();
  }

  /**
   * Locks the {@link Cook} to a {@link Station}.
   *
   * @param station {@link Station} : The {@link Station} to lock the {@link Cook}
   *                to.
   */
  public void lockToStation(Station station) {
    // If not already locked to a Station
    if (lockedTo != null) {
      return;
    }
    // Then lock to a station.
    lockedTo = station;
    // And tell the Station to lock this Cook
    station.lockCook(this);
  }

  /**
   * Unlocks the {@link Cook} from the {@link Station} they are locked to,
   * if they are locked to one.
   */
  public void unlock() {
    // If lockedTo is null, then just return
    if (lockedTo == null) {
      return;
    }
    // Save the station temporarily, as it needs to be forgotten to prevent a loop
    Station stationTemp = lockedTo;
    lockedTo = null;
    // If locked to a station, then unlock
    stationTemp.unlockCook(this);
  }

  /**
   * Updates the {@link Cook}'s current {@link Texture}s depending on
   * if they are holding {@link Item}s or not.
   */
  public void updateTexture() {
    if (heldItems.size() > 0) {
      setWalkTexture("entities/cook_walk_hands_" + cookno + ".png");
    } else {
      setWalkTexture("entities/cook_walk_" + cookno + ".png");
    }
  }

  /**
   * Serializes the {@link Cook} into an {@link JsonValue} so that
   * it can be stored externally.
   *
   * @return {@link JsonValue} : The {@link Cook} in Json form.
   */
  public JsonValue serial() {
    // Return JsonValue
    JsonValue cookRoot = new JsonValue(JsonValue.ValueType.object);
    cookRoot.addChild("cookno", new JsonValue(cookno));
    cookRoot.addChild("x", new JsonValue(MapManager.posToGrid(pos.x) - map.getOffsetX()));
    cookRoot.addChild("y", new JsonValue(MapManager.posToGrid(pos.y) - map.getOffsetY()));
    cookRoot.addChild("items", heldItems.serial());
    return cookRoot;
  }
}

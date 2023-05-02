package com.undercooked.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Listener;

/**
 * A class for the {@link PowerUp} entity that can spawn on the
 * {@link com.undercooked.game.map.Map} and give the player different
 * effects depending on which one it is.
 */
public class PowerUp extends Entity {

  /**
   * The {@link PowerUp}'s type.
   */
  private PowerUpType type;

  /**
   * Whether the {@link PowerUp} is currently in use or not.
   */
  boolean inUse = false;

  /**
   * How long the {@link PowerUp} has left before its effect runs out.
   */
  float useTimer = 0;

  /**
   * How long the {@link PowerUp} has before it despawns.
   */
  private float despawnTimer;

  /**
   * The {@link Listener} to tell when the {@link PowerUp} is being removed from
   * the game, either via despawning or running out of use time.
   */
  private Listener<PowerUp> removeListener;

  /**
   * Constructor for the {@link PowerUp}.
   */
  public PowerUp() {
    super();
    collision.setSize(30, 30);
  }

  @Override
  public void postLoad(TextureManager textureManager) {
    super.postLoad(textureManager);
    sprite.setSize(48, 48);
  }

  @Override
  public void update(float delta) {
    collision.x = pos.x - collision.getWidth() / 2f;
    collision.y = pos.y - collision.getHeight() / 2f;

    // If it's in use, then slowly decrease the timer
    if (inUse) {
      useTimer -= delta;
      // If use Timer <= 0, then remove
      if (useTimer <= 0) {
        // It has finished, so remove
        remove();
      }
      return;
    }

    // Only decrease the timer if it's >= 0
    if (despawnTimer >= 0) {
      despawnTimer -= delta;
      if (despawnTimer <= 0) {
        // If it's <= 0, then despawn
        remove();
      }
    }
  }

  /**
   * Remove the {@link PowerUp} from the game by telling the remove {@link Listener}.
   */
  public void remove() {
    if (removeListener == null) {
      return;
    }
    removeListener.tell(this);
  }

  /**
   * Check if the {@link PowerUp} is colliding with another {@link Entity},
   * only if the {@link PowerUp} is not currently in use.
   *
   * @param entity {@link Entity} : The {@link Entity} to check collisions with.
   * @return {@code boolean} : {@code true} if the {@link PowerUp} and {@link Entity} are colliding,
   *                           and the {@link PowerUp} is not in use.
   *                           {@code false} otherwise.
   */
  public boolean checkCollision(Entity entity) {
    // If it's in use, then ignore
    if (inUse) {
      return false;
    }
    // Check collision
    return isColliding(entity);
  }

  /**
   * Set the {@link PowerUp} to consider itself in use and instead
   * lower its use timer instead of its despawn timer.
   */
  public void use() {
    inUse = true;
  }

  @Override
  public void draw(SpriteBatch batch) {
    batch.draw(sprite,
            pos.x - sprite.getWidth() / 2f, pos.y - sprite.getHeight() / 2f,
            sprite.getWidth(), sprite.getHeight());
  }

  /**
   * Set the {@link PowerUp}'s type.
   *
   * @param type {@link PowerUpType} : The {@link PowerUpType} to set the {@link PowerUp} to.
   */
  public void setType(PowerUpType type) {
    this.type = type;
  }

  /**
   * Set the time that the {@link PowerUp} will last once it has
   * been used.
   *
   * @param useTimer {@code float} : How long the {@link PowerUp} will last while in use.
   */
  public void setUseTimer(float useTimer) {
    this.useTimer = useTimer;
  }

  /**
   * Set the time that the {@link PowerUp} will stay spawned before
   * the {@link #remove()} function is called, if not used.
   *
   * @param despawnTime {@code float} : How long the {@link PowerUp} remains on the
   *                    {@link com.undercooked.game.map.Map} before it is removed.
   */
  public void setDespawnTime(float despawnTime) {
    this.despawnTimer = despawnTime;
  }

  /**
   * Sets the {@link Listener} to tell when the {@link PowerUp} needs
   * to be removed from the game, either because it has run out of use time or
   * it has despawned.
   *
   * @param listener {@link Listener}&lt;{@link PowerUp}&gt; : The {@link Listener} to tell when the
   *                 {@link PowerUp} needs to be removed from
   *                 the game.
   */
  public void setRemoveListener(Listener<PowerUp> listener) {
    this.removeListener = listener;
  }

  /**
   * Returns the type of {@link PowerUp} this is.
   *
   * @return {@link PowerUpType} : The {@link PowerUp}'s type.
   */
  public PowerUpType getType() {
    return this.type;
  }

  /**
   * Returns whether the {@link PowerUp} is currently being
   * used and lower its use timer, or not.
   *
   * @return {@code boolean} : {@code true} if the {@link PowerUp} is in use,
   *                           {@code false} if not.
   */
  public boolean isInUse() {
    return inUse;
  }
}

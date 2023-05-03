package com.undercooked.game.food;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import java.util.Iterator;

/**
 * A class which holds {@link Items} in a stack.
 */
public class ItemStack implements Iterable<Item> {
  /**
   * An {@link Array} of the {@link Items} within the stack.
   */
  private final Array<Item> items;

  /**
   * Constructor for the class.
   */
  public ItemStack() {
    this.items = new Array<>();
  }

  /**
   * Returns the number of {@link Item}s in the stack.
   *
   * @return {@code int} : The number of {@link Item}s in the stack.
   */
  public int size() {
    return items.size;
  }

  /**
   * Returns the {@link Item} on the top of the stack.
   *
   * @return {@link Item} : The topmost {@link Item} of the stack.
   * @see #pop()
   */
  public Item peek() {
    if (items.size == 0) {
      return null;
    }
    return items.peek();
  }

  /**
   * Removes the topmost {@link Item} from the stack, and returns it.
   *
   * @return {@link Item} : The topmost {@link Item} of the stack.
   * @see #peek()
   */
  public Item pop() {
    return items.pop();
  }

  /**
   * Removes all of the {@link Item}s from the stack.
   */
  public void clear() {
    items.clear();
  }

  /**
   * Adds an {@link Item} to the top of the stack.
   *
   * @param item {@link Item} : The {@link Item} to add to the stack.
   */
  public void add(Item item) {
    items.add(item);
  }

  /**
   * Returns the {@link Item} at the index provided.
   *
   * @param index {@code int} : The index to get.
   * @return {@link Item} : The {@link Item} at the index.
   */
  public Item get(int index) {
    return items.get(index);
  }

  public Iterator<Item> iterator() {
    return items.iterator();
  }

  /**
   * Returns whether the {@link ItemStack} has an {@link Item}
   * with the provided {@link String} ID or not.
   *
   * @param itemId {@link String} : The ID of the {@link Item}.
   * @return {@code boolean} : {@code true} if it has the ID,
   *                           {@code false} if it does not.
   */
  public boolean hasId(String itemId) {
    for (Item item : items) {
      if (item.getId().equals(itemId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * A static version of the {@link #hasId(String)} function.
   *
   * @param items  {@link Array}&lt;{@link Item}&gt; : An {@link Array} of the {@link Item}s.
   * @param itemId {@link String} : The ID of the {@link Item}.
   * @return {@code boolean} : {@code true} if it has the ID,
   *                           {@code false} if it does not.
   */
  public static boolean hasId(Array<Item> items, String itemId) {
    for (Item item : items) {
      if (item.getId() == itemId) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns an {@link Array} of the {@link #items} {@link Array}
   * that the ItemStack uses, either as a copy or not.
   *
   * @param copy {@code boolean} : Whether it should return as a copy or
   *             not.
   * @return {@link Array}&lt;{@link Item}&gt; : An {@link Array} of the {@link Item}s.
   */
  public Array<Item> asArray(boolean copy) {
    // If it's not a copy, just return the array
    if (!copy) {
      return items;
    }

    // If it is a copy, then create a new array and copy over
    // all the values within
    Array<Item> returnArray = new Array<>();
    for (Item item : items) {
      returnArray.add(item);
    }
    // Return the copy.
    return returnArray;
  }

  /**
   * Returns a copy of the {@link #items} {@link Array}
   * that the ItemStack uses.
   *
   * @return {@link Array}&lt;{@link Item}&gt; : An {@link Array} of the {@link Item}s.
   */
  public Array<Item> asArray() {
    return asArray(true);
  }

  /**
   * Converts the {@link ItemStack} into a {@link JsonValue} array
   * of the {@link Item}s' ids.
   *
   * @return {@link JsonValue} : The {@link ItemStack} as a {@link JsonValue}.
   */
  public JsonValue serial() {
    // Get ItemIDs from {@link heldItems}
    JsonValue theItemIds = new JsonValue(JsonValue.ValueType.array);
    for (Item item : items) {
      theItemIds.addChild("", new JsonValue(item.getId()));
    }
    // Return the ItemIDs
    return theItemIds;
  }
}

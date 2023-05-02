package de.tomgrill.gdxtesting.tests.foodTests;

import de.tomgrill.gdxtesting.GdxTestRunner;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.food.Items;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.Iterator;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemStackTests {
  static ItemStack itemStack;
  static Item item;
  static Item item2;
  static Item item3;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    itemStack = new ItemStack();
    item = new Item("<main>:lettuce.png");
    item2 = new Item("<main>:patty.png");
    item3 = new Item("<main>:burger.png");
  }

  @Test
  public void t00_size_empty() {
    assertEquals(0, itemStack.size());
  }

  @Test
  public void t01_peek_empty() {
    assertNull(itemStack.peek());
  }

  @Test
  public void t02_pop_empty() {
    assertThrows(IllegalStateException.class, () -> {
      itemStack.pop();
    });
  }

  @Test
  public void t20_asArray() {
    Array<Item> itemArray = itemStack.asArray();
    assertEquals(0, itemArray.size);
  }

  @Test
  public void t30_add() {
    itemStack.add(item);
    assertEquals("Item was not added properly.", item, itemStack.asArray().get(0));
  }

  @Test
  public void t31_size_not_empty() {
    assertEquals(1, itemStack.size());
  }

  @Test
  public void t32_peek_not_empty() {
    assertEquals("Item was not peeked properly.", item, itemStack.peek());
  }

  @Test
  public void t33_pop_not_empty() {
    assertEquals("Item was not popped properly.", item, itemStack.pop());
  }

  @Test
  public void t34_size_after_pop() {
    assertEquals(0, itemStack.size());
  }

  @Test
  public void t40_add_multiple() {
    itemStack.add(item);
    itemStack.add(item2);
    itemStack.add(item3);
    assertEquals("Item was not added properly.", item, itemStack.asArray().get(0));
    assertEquals("Item was not added properly.", item2, itemStack.asArray().get(1));
    assertEquals("Item was not added properly.", item3, itemStack.asArray().get(2));
  }

  @Test
  public void t41_size_after_add_multiple() {
    assertEquals(3, itemStack.size());
  }

  @Test
  public void t42_peek_after_add_multiple() {
    assertEquals("Item was not peeked properly.", item3, itemStack.peek());
  }

  @Test
  public void t43_pop_after_add_multiple() {
    assertEquals("Item was not popped properly.", item3, itemStack.pop());
  }

  @Test
  public void t44_size_after_pop_multiple() {
    assertEquals(2, itemStack.size());
  }

  @Test
  public void t50_asArray_reference() {
    Array<Item> itemArray = itemStack.asArray(false);
    itemArray.removeIndex(0);
    assertEquals(item2, itemStack.get(0));
    assertEquals(1, itemStack.size());
  }

  @Test
  public void t51_asArray_copy() {
    Array<Item> itemArray = itemStack.asArray(true);
    itemArray.removeIndex(0);
    assertEquals(item2, itemStack.get(0));
    assertEquals(1, itemStack.size());
  }

  @Test
  public void t60_clear() {
    itemStack.clear();
    assertEquals(0, itemStack.size());
  }

  @Test
  public void t70_hasId() {
    itemStack.add(item2);
    assertTrue(itemStack.hasId("<main>:patty.png"));
    assertFalse(itemStack.hasId("<main>:lettuce.png"));
    assertFalse(itemStack.hasId("not an id"));
  }

  @Test
  public void t80_iterator() {
    assertTrue("Did not return iterator.", itemStack.iterator() instanceof Iterator);
  }

}

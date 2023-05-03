package de.tomgrill.gdxtesting.tests.entityTests;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.undercooked.game.entity.MoveableEntity;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapManager;

import org.junit.runner.RunWith;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MoveableEntityTests {

  static MoveableEntity moveableEntity;
  static Map map;

  @BeforeClass
  public static void setup() {
    moveableEntity = new MoveableEntity();
    moveableEntity.speed = 11;
    moveableEntity.dirX = 1;
    moveableEntity.dirY = -1;

    map = new Map(1000, 1000);
  }

  @Test
  public void t00_moveCalc() {
    float grdPos = MapManager.gridToPos(moveableEntity.speed);
    float moveX = moveableEntity.moveCalc(moveableEntity.dirX, 1f);
    float moveY = moveableEntity.moveCalc(moveableEntity.dirY, 1f);
    assertEquals("Move X is wrong.", grdPos, moveX, 0.01);
    assertEquals("Move Y is wrong.", -1 * grdPos, moveY, 0.01);
  }

  @Test
  public void t10_move() {
    moveableEntity.move(10, 10, 1f);
    assertEquals(moveableEntity.moveCalc(10, 1f), moveableEntity.pos.x, 0.01);
    assertEquals(moveableEntity.moveCalc(10, 1f), moveableEntity.pos.y, 0.01);
  }

  // TODO: Complete this test
  // @Test
  // public void t20_moveAndCollide() {
  // moveableEntity.moveAndCollide(map, 10, 10, 1f);
  // }
}

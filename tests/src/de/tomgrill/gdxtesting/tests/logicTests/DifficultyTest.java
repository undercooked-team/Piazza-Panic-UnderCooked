package de.tomgrill.gdxtesting.tests.logicTests;

import com.undercooked.game.logic.Difficulty;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DifficultyTest {

  @Test
  public void t00_constantValueAsString() {
    assertEquals("Easy not correct string.", Difficulty.EASY_TEXT, Difficulty.toString(Difficulty.EASY));
    assertEquals("Medium not correct string.", Difficulty.MEDIUM_TEXT, Difficulty.toString(Difficulty.MEDIUM));
    assertEquals("Hard not correct string.", Difficulty.HARD_TEXT, Difficulty.toString(Difficulty.HARD));
  }

  @Test
  public void t01_valueAsString() {
    assertEquals("Easy not correct string.", Difficulty.EASY_TEXT, Difficulty.toString(0));
    assertEquals("Medium not correct string.", Difficulty.MEDIUM_TEXT, Difficulty.toString(1));
    assertEquals("Hard not correct string.", Difficulty.HARD_TEXT, Difficulty.toString(2));
  }

  @Test
  public void t02_invalidValueAsString() {
    assertEquals("Lower range doesn't return 'invalid'.", "invalid", Difficulty.toString(-1));
    assertEquals("Above range doesn't return 'invalid'.", "invalid", Difficulty.toString(-1));
  }

  @Test
  public void t10_constantStringAsValue() {
    assertEquals("Easy not correct value.", Difficulty.EASY, Difficulty.asInt(Difficulty.EASY_TEXT));
    assertEquals("Medium not correct value.", Difficulty.MEDIUM, Difficulty.asInt(Difficulty.MEDIUM_TEXT));
    assertEquals("Hard not correct value.", Difficulty.HARD, Difficulty.asInt(Difficulty.HARD_TEXT));
  }

  @Test
  public void t11_stringAsValue() {
    assertEquals("Easy not correct value (lower case).", Difficulty.EASY, Difficulty.asInt("easy"));
    assertEquals("Easy not correct value (UPPER CASE).", Difficulty.EASY, Difficulty.asInt("EASY"));
    assertEquals("Medium not correct value (lower case).", Difficulty.MEDIUM, Difficulty.asInt("medium"));
    assertEquals("Medium not correct value (UPPER CASE).", Difficulty.MEDIUM, Difficulty.asInt("MEDIUM"));
    assertEquals("Hard not correct value (lower case).", Difficulty.HARD, Difficulty.asInt("hard"));
    assertEquals("Hard not correct value (UPPER CASE).", Difficulty.HARD, Difficulty.asInt("HARD"));
  }

  @Test
  public void t12_invalidStringAsValue() {
    assertEquals("Invalid not -1.", -1, Difficulty.asInt(""));
    assertEquals("Invalid not -1.", -1, Difficulty.asInt("difficult"));
    assertEquals("Invalid not -1.", -1, Difficulty.asInt("1"));
    assertEquals("Invalid not -1.", -1, Difficulty.asInt(null));
  }
}

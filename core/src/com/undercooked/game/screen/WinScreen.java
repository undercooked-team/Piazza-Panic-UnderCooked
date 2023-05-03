package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.undercooked.game.GameType;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.input.InputController;
import com.undercooked.game.util.Constants;

/**
 * A class for the Win {@link Screen} which allows the player to
 * input a name, and press Enter to add their score to a
 * {@link com.undercooked.game.util.leaderboard.Leaderboard}.
 */
public class WinScreen extends Screen {

  private String nameInput;
  private float score;
  private GlyphLayout scoreText;
  private GlyphLayout nameGlyph;
  private GlyphLayout text;
  private GameType gameType;
  private String leaderboardId;
  private String leaderboardName;

  /**
   * Constructor for the {@link WinScreen}.
   *
   * @param game {@link MainGameClass} : The {@link MainGameClass} of the game.
   */
  public WinScreen(MainGameClass game) {
    super(game);
  }

  @Override
  public void load() {

  }

  @Override
  public void unload() {
    scoreText = null;
    nameGlyph = null;
  }

  @Override
  public void show() {
    // Stop the game's music
    game.gameMusic.stop();
    // Reset name input
    this.nameInput = "";
    // Make name glyph
    this.nameGlyph = new GlyphLayout();
    // Make basic text glyph
    this.text = new GlyphLayout();
    // And update it
    updateNameGlyph();
  }

  /**
   * Returns a key that is just pressed in the range provided.
   *
   * @param start {@code int} : The start of the range, inclusive.
   * @param end   {@code int} : The end of the range, inclusive.
   * @return {@link String} : The display name of the key pressed.
   */
  public String getKey(int start, int end) {
    for (int i = start; i <= end; i++) {
      if (Gdx.input.isKeyJustPressed(i)) {
        return Input.Keys.toString(i);
      }
    }
    return null;
  }

  /**
   * Update the {@link #nameGlyph}.
   */
  private void updateNameGlyph() {
    this.nameGlyph.setText(game.font, nameInput);
  }

  /**
   * Update the {@link WinScreen}, checking for if the player is inputting
   * a name, or trying to submit the name they have already input.
   *
   * @param delta {@code float} : The time since the last frame.
   */
  public void update(float delta) {
    // Check for input "enter"
    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
      // If no name is provided, just make it "???"
      if (nameInput.length() == 0) {
        nameInput = "???";
      }
      // If it's pressed, go to the LeaderBoard Screen.
      game.screenController.goToScreen(Constants.LEADERBOARD_SCREEN_ID);
      // Get the Screen
      LeaderboardScreen leaderboardScreen = (LeaderboardScreen) (
              game.screenController.getScreen(Constants.LEADERBOARD_SCREEN_ID)
      );
      // And try to add the score
      leaderboardScreen.addLeaderBoardData(gameType, leaderboardId, leaderboardName,
                                           nameInput, score);
      // And then set the leaderboard screen to view the leaderboard
      leaderboardScreen.goToLeaderboardType(gameType);
      leaderboardScreen.showLeaderboard(leaderboardId);
      // And stop here
      return;
    }

    // If backspace is pressed, remove the last added character
    if (nameInput.length() > 0 && Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
      this.nameInput = this.nameInput.substring(0, this.nameInput.length() - 1);
      updateNameGlyph();
    }

    // If the name is too long, don't check for inputs
    if (nameGlyph.width >= Constants.MAX_NAME_LENGTH) {
      return;
    }

    // If any letter keys, a - z, are pressed
    // or if any numbers are pressed, add them.
    String addChar = getKey(Input.Keys.valueOf("A"), Input.Keys.valueOf("Z"));

    if (addChar == null) {
      addChar = getKey(Input.Keys.valueOf("0"), Input.Keys.valueOf("9"));
    }

    if (addChar != null) {
      nameInput += addChar;
      updateNameGlyph();
    }
  }

  @Override
  public void render(float delta) {
    // Update inputs
    InputController.updateKeys();

    // Update
    update(delta);

    // If either of the text is null, stop
    if (scoreText == null || nameGlyph == null) {
      return;
    }

    // Clear the Screen
    ScreenUtils.clear(0, 0, 0, 0);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    game.batch.begin();
    // Draw the score text in the middle of the Screen
    game.font.draw(game.batch, scoreText, Constants.V_WIDTH / 2f - scoreText.width / 2,
            Constants.V_HEIGHT / 2f);

    // Draw "Enter Name"
    text.setText(game.font, "Enter Name:");
    game.font.draw(game.batch, text, Constants.V_WIDTH / 2f - text.width / 2,
            Constants.V_HEIGHT / 2f - 60);
    // And below that, draw the name underneath
    game.font.draw(game.batch, nameGlyph, Constants.V_WIDTH / 2f - nameGlyph.width / 2,
            Constants.V_HEIGHT / 2f - 100);

    // And below that, put "Submit with ENTER"
    text.setText(game.font, "Submit with ENTER");
    game.font.draw(game.batch, text, Constants.V_WIDTH / 2f - text.width / 2,
            Constants.V_HEIGHT / 2f - 200);

    game.batch.end();
  }

  /**
   * Set the {@link GameType} that the
   * {@link com.undercooked.game.util.leaderboard.LeaderboardEntry} will be saved to.
   *
   * @param gameType {@link GameType} : The leaderboard type to save to.
   */
  public void setLeaderboardType(GameType gameType) {
    this.gameType = gameType;
  }

  /**
   * Set the id of the {@link com.undercooked.game.util.leaderboard.Leaderboard}
   * that will be saved to.
   *
   * @param leaderboardId {@link String} : The id of the leaderboard to save to.
   */
  public void setLeaderboardId(String leaderboardId) {
    this.leaderboardId = leaderboardId;
  }

  /**
   * The name to set the {@link com.undercooked.game.util.leaderboard.Leaderboard} to if it
   * does not exist already.
   *
   * @param leaderboardName {@link String} : The name of the leaderboard.
   */
  public void setLeaderboardName(String leaderboardName) {
    this.leaderboardName = leaderboardName;
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }

  /**
   * Must come from the {@link GameScreen}, so get the score from
   * it and update the {@link #scoreText} text.
   */
  @Override
  public void fromScreen(Screen screen) {
    // It shouldn't ever reach this screen if not from the
    // GameScreen
    GameScreen gameScreen = (GameScreen) screen;

    // Get the score from the gameScreen
    this.score = gameScreen.gameLogic.getScore();

    // Make the GlyphLayout for the score
    this.scoreText = new GlyphLayout(MainGameClass.font, gameScreen.gameLogic.getScoreString());
  }
}

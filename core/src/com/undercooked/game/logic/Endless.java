package com.undercooked.game.logic;

import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;

public class Endless extends GameLogic {

    public Endless(GameScreen game, TextureManager textureManager) {
        super(game, textureManager);
    }

    public Endless() {
        this(null, null);
    }

    public void update(float delta) {
        super.update(delta);
    }

    // @Override public void preLoad() {

    // }

    public void load() {
        super.load();

    }

    @Override
    public void unload() {
        gameScreen.getTextureManager().unload(Constants.GAME_TEXTURE_ID);
    }
}

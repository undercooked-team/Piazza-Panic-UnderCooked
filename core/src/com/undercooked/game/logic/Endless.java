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

    @Override
    public void load() {
        // Load all Cook and Customer sprites
        TextureManager textureManager = gameScreen.getTextureManager();
        // Cooks
        for (int i = 1; i <= Constants.NUM_COOK_TEXTURES; i++) {
            textureManager.load(Constants.GAME_TEXTURE_ID, "cook_walk_" + i + ".png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cook_walk_hands_" + i + ".png");
        }

        // Customers
        for (int i = 1; i <= Constants.NUM_CUSTOMER_TEXTURES; i++) {
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "f.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "b.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "r.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "l.png");
        }

        // Load all base ingredients
        ingredients.load(textureManager);
    }

    @Override
    public void unload() {
        gameScreen.getTextureManager().unload(Constants.GAME_TEXTURE_ID);
    }
}

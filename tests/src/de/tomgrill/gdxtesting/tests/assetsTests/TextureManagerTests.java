package de.tomgrill.gdxtesting.tests.assetsTests;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.Texture;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;

import com.badlogic.gdx.assets.AssetManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextureManagerTests {
	static AssetManager assetManager;
	static TextureManager textureManager;

	@BeforeClass
	public static void loadAssetManager() {
		assetManager = new AssetManager();
		textureManager = new TextureManager(assetManager);
		// ! FIXME: Doesn't load default texture
		assetManager.finishLoading();
		// ! ###
	}

	@Test
	public void t00DefaultAssetLoaded() {
		assertTrue("Default texture has not loaded",
				assetManager.isLoaded(Constants.DEFAULT_TEXTURE, Texture.class));
	}
}

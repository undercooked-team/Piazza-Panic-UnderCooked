package de.tomgrill.gdxtesting.tests.assetsTests;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

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

		// ! FIXME: Crashes here as it doesn't manage to load default texture
		assetManager.finishLoading();
		// ! ###
	}

	@Test
	public void t00DefaultAssetLoaded() {
		assertTrue("Default texture has not loaded",
				assetManager.isLoaded(Constants.DEFAULT_TEXTURE, Texture.class));
	}
}

package de.tomgrill.gdxtesting.tests.foodTests;

import de.tomgrill.gdxtesting.GdxTestRunner;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.food.Items;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemStackTests {
	ItemStack itemStack;
	
}

package de.tomgrill.gdxtesting.tests.audioTests;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.files.FileControl;

import org.junit.runner.RunWith;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AudioSlidersTests {

	static AudioSliders audioSliders;
	static Texture texture;
	static Slider slider;
	static Skin skin;

	@BeforeClass
	public static void setup() {
		texture = new Texture(FileControl.getAssetPath("<main>:vControl", "uielements"));
		audioSliders = new AudioSliders(1, 2, 3, 4, texture);
		skin = new Skin();
		slider = new Slider(0, 1, 0.1f, false, skin);
	}

	@Test
	public void t01_getPosX() {
		assertEquals(1, audioSliders.getX(), 0.01);
	}

	@Test
	public void t02_getPosY() {
		assertEquals(2, audioSliders.getY(), 0.01);
	}

	@Test
	public void t03_getWidth() {
		assertEquals(3, audioSliders.getWidth(), 0.01);
	}

	@Test
	public void t04_getHeight() {
		assertEquals(4, audioSliders.getHeight(), 0.01);
	}

	@Test
	public void t10_setPosX() {
		audioSliders.setX(5);
		assertEquals(5, audioSliders.getX(), 0.01);
	}

	@Test
	public void t11_setPosY() {
		audioSliders.setY(6);
		assertEquals(6, audioSliders.getY(), 0.01);
	}

	@Test
	public void t12_setWidth() {
		audioSliders.setWidth(7);
		assertEquals(7, audioSliders.getWidth(), 0.01);
	}

	@Test
	public void t13_setHeight() {
		audioSliders.setHeight(8);
		assertEquals(8, audioSliders.getHeight(), 0.01);
	}

	// TODO: Test addSlider

	@Test
	public void t99_update() {
		audioSliders.update();
	}

}

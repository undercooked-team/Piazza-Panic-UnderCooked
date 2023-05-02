package de.tomgrill.gdxtesting.tests.audioTests;

import static org.junit.Assert.*;

import com.undercooked.game.audio.Slider;
import com.undercooked.game.util.Listener;
import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

	@BeforeClass
	public static void setup() {
		texture = new Texture("uielements/vControl.png");
		audioSliders = new AudioSliders(1, 2, 3, 4, texture);
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

	@Test
	public void t20_addSlider() {
		int slidersBefore = audioSliders.size();
		slider = audioSliders.addSlider(null, texture);
		assertEquals("AudioSliders does not have new slider.", slidersBefore+1, audioSliders.size());
		assertTrue("Slider not added.", audioSliders.hasSlider(slider));
		assertNotNull("Slider not returned.", slider);
	}

	@Test public void t21_removeSlider() {
		int slidersBefore = audioSliders.size();
		audioSliders.removeSlider(slider);
		assertEquals("AudioSliders did not lose a slider.", slidersBefore-1, audioSliders.size());
		assertFalse("Slider not removed", audioSliders.hasSlider(slider));
	}

	@Test
	public void t30_changeSlider() {

		slider = audioSliders.addSlider(null, texture);

		// Test set to 0.
		Listener<Float> testListener = new Listener<Float>() {
			@Override
			public void tell(Float value) {
				assertEquals("Listeners told wrong value.", 0f, value, 0f);
			}
		};

		slider.addChangeListener(testListener);
		slider.updatePercent(0);
		slider.removeChangeListener(testListener);

		// Test set to 1.
		testListener = new Listener<Float>() {
			@Override
			public void tell(Float value) {
				assertEquals("Listeners told wrong value.", 1f, value, 0f);
			}
		};

		slider.addChangeListener(testListener);
		slider.updatePercent(1);
		slider.removeChangeListener(testListener);

		// Test set to more specific value.
		testListener = new Listener<Float>() {
			@Override
			public void tell(Float value) {
				assertEquals("Listeners told wrong value.", 0.32f, value, 0f);
			}
		};

		slider.addChangeListener(testListener);
		slider.updatePercent(0.32f);
		slider.removeChangeListener(testListener);

	}

	@Test
	public void t31_changeSliderBounds() {

		// Test set to -1, should output 0.
		Listener<Float> testListener = new Listener<Float>() {
			@Override
			public void tell(Float value) {
				assertEquals("Percent didn't go to minimum of 0.", 0f, value, 0f);
			}
		};

		slider.addChangeListener(testListener);
		slider.updatePercent(-1);
		slider.removeChangeListener(testListener);

		// Test set to 100, should output 1.
		testListener = new Listener<Float>() {
			@Override
			public void tell(Float value) {
				assertEquals("Percent didn't go to maximum of 1.", 1f, value, 0f);
			}
		};

		slider.addChangeListener(testListener);
		slider.updatePercent(100);
		slider.removeChangeListener(testListener);
	}

	@Test
	public void t99_update() {
		audioSliders.update();
	}

}

package de.tomgrill.gdxtesting.tests.entityTests;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.assets.AssetManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.files.FileControl;

import org.junit.runner.RunWith;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
// ! TESTS ARE RUN IN ALPHABETICAL ORDER !
// * Start every test with t[0-9][0-9][...] to determine the order they run in
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityTests {

  static Entity entity;
  static Entity entity2;
  static AssetManager assetManager;
  static TextureManager textureManager;
  static String texturePath = "<main>:item/lettuce.png";

  @BeforeClass
  public static void setup() {
    assetManager = new AssetManager();
    textureManager = new TextureManager(assetManager);
    entity = new Entity();
    entity.setTexture(texturePath);
    entity.setHeight(10);
    entity.setWidth(15);

    entity2 = new Entity();
    entity2.setTexture(texturePath);
  }

  @Test
  public void t01_load() {
    entity.load(textureManager);
    assetManager.finishLoading();
    assertTrue(assetManager.isLoaded(FileControl.getAssetPath(texturePath, "textures")));
  }

  @Test
  public void t02_postLoad() {
    entity.postLoad(textureManager);
    assetManager.finishLoading();
    assertNotNull(entity.getSprite());
  }

  @Test
  public void t10_update() {
    entity.update(0.01f);
  }

  @Test
  public void t20_isColliding() {
    entity2.load(textureManager);
    assetManager.finishLoading();
    entity2.postLoad(textureManager);
    assetManager.finishLoading();
    entity2.update(0.01f);
    assertFalse("Cannot collide with an entity with no width or height", entity.isColliding(entity2));

    entity2.setWidth(20);
    entity2.setHeight(35);
    entity2.update(0.01f);
    assertTrue("Entities should be colliding", entity.isColliding(entity2));
  }

  @Test
  public void t30_getSprite() {
    assertNotNull("Sprite should be present.", entity.getSprite());
  }

  @Test
  public void t40_unload() {
    entity.unload(textureManager);
    entity2.unload(textureManager);
    assetManager.finishLoading();
    assertFalse("Texture should've unloaded.",
        assetManager.isLoaded(FileControl.getAssetPath(texturePath, "textures")));
  }

}

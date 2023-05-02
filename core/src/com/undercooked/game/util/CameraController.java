package com.undercooked.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * A class that controls the {@link OrthographicCamera}'s of the game
 * so that they can easily be accessed from anywhere within the game's
 * classes.
 * <br>
 * <br>It does this by assigning an ID to a camera, which can be accessed using
 * the static functions.
 */
public class CameraController {

  private static class CameraInfo {

    private OrthographicCamera camera;
    private FitViewport viewport;

    public CameraInfo(OrthographicCamera camera) {
      this.camera = camera;
      this.viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, camera);
    }

    public OrthographicCamera getCamera() {
      return camera;
    }

    public FitViewport getViewport() {
      return viewport;
    }

  }

  /**
   * An {@link ObjectMap} (LibGDX equivalent of {@link java.util.HashMap})
   * that maps a {@link String} ID to an {@link OrthographicCamera}.
   */
  private static final ObjectMap<String, CameraInfo> cameras = new ObjectMap<>();

  /**
   * Gets the camera by the String ID input.
   * <br>
   * <br>If the ID does not exist already, then it will
   * create a new camera and assign it to the ID provided, before
   * returning it.
   *
   * @param cameraId The {@link String} ID of the {@link OrthographicCamera}.
   * @return {@link OrthographicCamera} : The {@link OrthographicCamera} that
   *                                      was requested / created.
   */
  public static OrthographicCamera getCamera(String cameraId) {
    // First check if the camera already exists
    if (cameras.containsKey(cameraId)) {
      // If it does, return it
      return cameras.get(cameraId).getCamera();
    }
    // If the camera does not exist, then make a new one.
    OrthographicCamera newCamera = new OrthographicCamera();
    cameras.put(cameraId, new CameraInfo(newCamera));
    return newCamera;
  }

  /**
   * Gets the viewport by the String ID input.
   * <br>
   * <br>If the ID does not exist already, then it will
   * create a new camera and assign it to the ID provided, before
   * returning the viewport of the camera.
   *
   * @param cameraId The {@link String} ID of the {@link FitViewport}.
   * @return {@link FitViewport} : The {@link FitViewport} that
   *                               was requested / created.
   */
  public static FitViewport getViewport(String cameraId) {
    // First check if the camera already exists
    if (cameras.containsKey(cameraId)) {
      // If it does, return it
      return cameras.get(cameraId).getViewport();
    }
    // If the camera does not exist, then make a new one.
    CameraInfo newCamera = new CameraInfo(new OrthographicCamera());
    cameras.put(cameraId, newCamera);
    return newCamera.getViewport();
  }

  /**
   * Removes the camera using the String ID input.
   *
   * @param cameraId The {@link String} ID of the {@link OrthographicCamera}.
   * @return {@link OrthographicCamera} : The {@link OrthographicCamera} that was removed.
   *                                      {@code null} if the camera does not exist.
   */
  public static OrthographicCamera removeCamera(String cameraId) {
    // First check if the camera exists
    if (cameras.containsKey(cameraId)) {
      // If the camera exists, remove it from the ObjectMap
      return cameras.remove(cameraId).getCamera();
    }
    // If the camera does not exist, return null.
    return null;
  }

  /**
   * Maps the camera ID provided to the camera provided.
   *
   * @param cameraId The {@link String} ID of the {@link OrthographicCamera}.
   * @param camera   The {@link OrthographicCamera} to map to the ID.
   */
  public static void setCamera(String cameraId, OrthographicCamera camera) {
    // Just replace the cameraID with the camera.
    cameras.put(cameraId, new CameraInfo(camera));
  }

}

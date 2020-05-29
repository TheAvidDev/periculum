package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main game class.
 *
 * @author hirundinidae
 * @author TheAvidDev
 */
//2020-05-29 TheAvidDev - render background and foreground layers separately
// 2020-05-29 TheAvidDev - allowed window resizing and scale definitions
// 2020-05-28 hirundinidae - created camera and its movement based on player
// 2020-05-28 TheAvidDev - cleaned up code, removed libGDX defaults
// 2020-05-27 hirundinidae - added level creation and rendering
public class Periculum extends ApplicationAdapter {
  private final int TILE_WIDTH = 16;
  private final int VIEWPORT_WIDTH = 16;

    SpriteBatch batch;
    Player player;
    Level level;
    OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
        level = new Level();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 320 * (4 / 3));
        level.create();
    }

    @Override
    public void render() {
        player.update();
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        level.renderBackground(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(player.getTextureRegion(), player.getX(), player.getY());
        batch.end();

        level.renderForeground(camera);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        level.dispose();
    }

    /**
     * This method resizes the camera's viewport while maintaining scale to
     * allow any window size. It will base the scale off of the shortest side
     * length, so ultra wide screens won't be unnecessarily zoomed in.
     */
    @Override
    public void resize(int width, int height) {
        if ((float) width / (float) height > (float) height / (float) width) {
        float scale = (float) width / (float) height;
        camera.viewportWidth = TILE_WIDTH * VIEWPORT_WIDTH * scale;
            camera.viewportHeight = TILE_WIDTH * VIEWPORT_WIDTH;
      } else {
        float scale = (float) height / (float) width;
        camera.viewportWidth = TILE_WIDTH * VIEWPORT_WIDTH;
          camera.viewportHeight = TILE_WIDTH * VIEWPORT_WIDTH * scale;
      }
      camera.update();
    }
}

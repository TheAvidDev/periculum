package dev.theavid.periculum;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Draws the map of the level.
 * 
 * @author hirundinidae
 * @author TheAvidDev
 */
// 2020-05-29 TheAvidDev - render background and foreground layers separately
// 2020-05-28 hirundinidae - removed unused interface, started using camera  
// 2020-05-27 hirundinidae - Tiled integration to libGDX, level drawing 
public class Level {
	private final int[] BACKGROUND_LAYERS = new int[] {0, 2};
	private final int[] FOREGROUND_LAYERS = new int[] {1};

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public void create() {
        map = new TmxMapLoader().load("tiles/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void renderBackground(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render(BACKGROUND_LAYERS);
    }

    public void renderForeground(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render(FOREGROUND_LAYERS);
    }

    public void dispose() {
        map.dispose();
    }
}

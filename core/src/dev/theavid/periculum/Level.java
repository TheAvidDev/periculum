package dev.theavid.periculum;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Draws the map of the level.
 * 
 * @author hirundinidae
 */
// 2020-05-28 hirundinidae - removed unused interface, started using camera  
// 2020-05-27 hirundinidae - Tiled integration to libGDX, level drawing 
public class Level {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public void create() {
        map = new TmxMapLoader().load("tiles/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose() {
        map.dispose();
    }

}

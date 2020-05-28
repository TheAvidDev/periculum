package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/** 
 * Draws the map of the level. 
 * 
 * @author hirundinidae
 */
// 2020-05-27 hirundinidae - Tiled integration to libGDX, level drawing 
public class Level extends ApplicationAdapter {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    @Override
    public void create() {
        map = new TmxMapLoader().load("tiles/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void render() {
        renderer.render();
    }

    public void dispose() {
        map.dispose();
    }
}

package dev.theavid.periculum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
//    private MapRenderer mapRenderer;
    private OrthogonalTiledMapRenderer renderer;

    @Override
    public void create() {
        map = new TmxMapLoader().load("tiles/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void render() {
        Gdx.gl.glClearColor(.5f, .7f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
    }

    public void dispose() {
        map.dispose();
    }
}

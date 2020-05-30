package dev.theavid.periculum;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Draws the map of the level.
 * 
 * @author hirundinidae
 * @author TheAvidDev
 */
// 2020-05-29 TheAvidDev - add tile collision and rectangle collision
// 2020-05-29 TheAvidDev - render background and foreground layers separately
// 2020-05-28 hirundinidae - removed unused interface, started using camera  
// 2020-05-27 hirundinidae - Tiled integration to libGDX, level drawing 
public class Level {
	private final int[] WALL_TILES = new int[] { 33, 34, 35, 38, 49, 51, 54, 65, 66, 67, 68, 69, 70, 71 };
	private final int[] BACKGROUND_LAYERS = new int[] { 0, 2 };
	private final int[] FOREGROUND_LAYERS = new int[] { 1 };

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private boolean collisionMap[][];

	public void create() {
		map = new TmxMapLoader().load("tiles/map.tmx");
		makeCollisionMap();
		renderer = new OrthogonalTiledMapRenderer(map);
	}

	/**
	 * Checks if a rectangle is colliding with any of the offset tiles listed in
	 * WALL_TILES.
	 * 
	 * @param x      X position of the rectangle
	 * @param y      Y position of the rectangle
	 * @param width  Width of the rectangle
	 * @param height Height of the rectangle
	 * @return Whether the rectangle intersect with any tile
	 */
	public boolean isColliding(int x, int y, int width, int height) {
		Rectangle testRect = new Rectangle(x, y, width, height);
		int xMap = (int) (x / 16);
		int yMap = (int) ((y - 11) / 16);
		for (int xm = xMap - 1; xm <= xMap + 1; xm++) {
			for (int ym = yMap - 1; ym <= yMap + 1; ym++) {
				Rectangle collisionRect = new Rectangle(xm * 16, ym * 16 + 11, 16, 16);
				if (collisionMap[xm][ym] && collisionRect.overlaps(testRect)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Loop through every tile in the foreground layer and check if it's Id matches
	 * any of the collidable tiles.
	 */
	private void makeCollisionMap() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		collisionMap = new boolean[layer.getWidth()][layer.getHeight()];
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				for (int i = 0; i < WALL_TILES.length; i++) {
					if (cell == null)
						continue;
					/**
					 * Tiled starts its tile IDs at 0, while libGDX starts them at 1, so to more
					 * easily convert between the two, adding one here is better than doing it for
					 * every number in the WALL_TILES array.
					 */
					if (cell.getTile().getId() == WALL_TILES[i] + 1) {
						collisionMap[x][y] = true;
						break;
					}
				}
			}
		}
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

	public boolean[][] getCollisionMap() {
		return collisionMap;
	}
}

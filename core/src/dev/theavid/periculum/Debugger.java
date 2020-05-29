package dev.theavid.periculum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * A debugging class to help with fixing problems and finding bugs.
 * 
 * @author TheAvidDev
 */
// 2020-05-29 TheAvidDev - added collision and player debugging
public class Debugger {
	private Player player;
	private Level level;
	private OrthographicCamera camera;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Batch batch = new SpriteBatch();
	private BitmapFont font = new BitmapFont();
	private boolean enabled = false;
	
	public Debugger(Player player, Level level, OrthographicCamera camera) {
		this.player = player;
		this.level = level;
		this.camera = camera;
	}
	
	/**
	 * Alternate whether debugging should be enabled or not on the DEBUG key
	 * map being just pressed.
	 */
	public void update() {
		if (KeyMap.DEBUG.isPressed(true)) {
			enabled = !enabled;
		}
	}
	
	public void render() {
		if (!enabled) return;
		
		/**
		 * Transparency blending section.
		 */
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    	shapeRenderer.setProjectionMatrix(camera.combined);
    	shapeRenderer.begin(ShapeType.Filled);
		debugLevelCollisions();
    	shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        /**
         * Opaque section.
         */
    	batch.begin();
    	debugPlayer();
    	batch.end();
	}
	
	public void dispose() {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
	}
	
	/**
	 * Draw a partially transparent red rectangle over collidable tiles.
	 */
	private void debugLevelCollisions() {
    	shapeRenderer.setColor(1, 0, 0, 0.5f);
    	for (int x = 0; x < level.getCollisionMap().length; x ++) {
        	for (int y = 0; y < level.getCollisionMap()[0].length; y ++) {
        		if (level.getCollisionMap()[x][y]) {
        			shapeRenderer.rect(x*16, y*16 + 11, 16, 16);
        		}
        	}	
        }
	}
	
	/**
	 * Writes important info about the player in the upper left hand corner.
	 */
	private void debugPlayer() {
		String debug = "X: " + player.getX() + "\n"
			+ "Y: " + player.getY() + "\n"
			+ "XVel: " + player.getXVel() + "\n"
			+ "YVel: " + player.getYVel();
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, debug, 10, camera.viewportHeight*2-40);
	}
}






















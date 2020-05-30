package dev.theavid.periculum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

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
	 * Alternate whether debugging should be enabled or not on the DEBUG key map
	 * being just pressed.
	 */
	public void update() {
		if (KeyMap.DEBUG.isPressed(true)) {
			enabled = !enabled;
		}
	}

	public void render() {
		if (!enabled)
			return;

		/**
		 * Transparency blending section.
		 */
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		debugLevelCollisions();
		debugMouse();
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
	 * Draw filled rectangles around where the mouse is in relation to the level.
	 * Two separate rectangles are drawn: one for the normal, aligned tiles, and
	 * another for the offset tiles such as the roof.
	 */
	private void debugMouse() {
		Vector3 mousePos = new Vector3();
		mousePos.x = Gdx.input.getX();
		mousePos.y = Gdx.input.getY();
		mousePos.z = 0;
		camera.unproject(mousePos);

		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect((int) (mousePos.x / 16) * 16, (int) ((mousePos.y - 11) / 16) * 16 + 11, 16, 16);
		shapeRenderer.setColor(1, 1, 1, 0.5f);
		shapeRenderer.rect((int) (mousePos.x / 16) * 16, (int) (mousePos.y / 16) * 16, 16, 16);
	}

	/**
	 * Draw rectangles related to collision detection. The colors are
	 * described below:
	 *  - Blue: a tile which has collision
	 *  - Red: a tile which is being checked for collision
	 *  - Green: a tile which is being collided with (inaccurate*)
	 *  - White: the player's actual collision detection box
	 *  - Black: an extra pixel around each side of the player's collision
	 *           box to simulate where a collision occurred the previous
	 *           frame
	 *
	 * [*] This is inaccurate because the player will attempt to move into a
	 *     wall and get pushed out in the player's update method so knowing
	 *     for sure what a player collided with is impossible unless debugging
	 *     is drawn there.
	 */
	private void debugLevelCollisions() {
		shapeRenderer.setColor(0, 0, 1, 0.5f);
		for (int x = 0; x < level.getCollisionMap().length; x++) {
			for (int y = 0; y < level.getCollisionMap()[0].length; y++) {
				if (level.getCollisionMap()[x][y]) {
					shapeRenderer.rect(x * 16, y * 16 + 11, 16, 16);
				}
			}
		}

		/**
		 * Read the [*] above for why this is offset by a pixel.
		 */
		Rectangle playerRect = new Rectangle(player.getX() + 1, player.getY(), 14, 16);
		int xMap = (int) (player.getX() / 16);
		int yMap = (int) ((player.getY() - 11) / 16);
		for (int xm = xMap - 1; xm <= xMap + 1; xm++) {
			for (int ym = yMap - 1; ym <= yMap + 1; ym++) {
				Rectangle collisionRect = new Rectangle(xm * 16, ym * 16 + 11, 16, 16);
				if (level.getCollisionMap()[xm][ym] && collisionRect.overlaps(playerRect)) {
					shapeRenderer.setColor(0, 1, 0, 0.5f);
				} else {
					shapeRenderer.setColor(1, 0, 0, 0.5f);
				}
				if (level.getCollisionMap()[xm][ym]) {
					shapeRenderer.rect(collisionRect.x, collisionRect.y, collisionRect.width, collisionRect.height);
				}
			}
		}
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(playerRect.x + 1, playerRect.y, playerRect.width - 2, playerRect.height);
		shapeRenderer.setColor(1, 1, 1, 0.5f);
		shapeRenderer.rect(playerRect.x + 2, playerRect.y + 1, playerRect.width - 4, playerRect.height - 2);
	}

	/**
	 * Writes important info about the player in the upper left hand corner.
	 */
	private void debugPlayer() {
		String debug = "X: " + player.getX() + "\n" + "Y: " + player.getY() + "\n" + "XVel: " + player.getXVel() + "\n"
				+ "YVel: " + player.getYVel();
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, debug, 10, camera.viewportHeight * 2 - 40);
	}
}

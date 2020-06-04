package dev.theavid.periculum.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An entity is any object that can be drawn and has some position in game but
 * is not part of the background tiles. This include objects like the player,
 * notifiers, and other characters.
 * 
 * @author TheAvidDev
 * @author hirundinidae
 */
// 2020-06-03 TheAvidDev - Add methods to help with drawing and interface with
//                         the EntityType
// 2020-05-29 hirundinidae - Created Entitiy class
public class Entity {
    private float x, y;
    EntityType entityType;
    
    public Entity(EntityType entityType, float x, float y) {
      this.entityType = entityType;
      this.x = x;
      this.y = y;
    }
    
    public void update() {
      return;
    }
    
    public void dispose() {
      entityType.dispose();
    }
    
    public TextureRegion getTextureRegion() {
      return entityType.getTextureRegion();
    }
    
    public float getX() { return x; }
  public float getY() { return y; }
}

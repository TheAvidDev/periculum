package dev.theavid.periculum.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * An enum for non static entities. 
 * 
 * @author Diana
 */
// 2020-05-29 hirundinidae - Created enum 
public enum EntityType {

    ;
    
    private final String fileName;
    private final boolean collidable;
    private final int width;
    private final int height;
    private Texture texture;

    EntityType(String fileName, boolean collidable, int width, int height, Texture texture) {
        this.fileName = fileName; 
        this.collidable=collidable; 
        this.width = width; 
        this.height = height; 
        this.texture = texture; 
    }

    public Texture getTexture() {
        return texture;
    }

}

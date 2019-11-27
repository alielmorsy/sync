package net.touchmania.game.resource.lazy;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontResource implements Resource<BitmapFont> {
    public final FreeTypeFontParameter parameter = new FreeTypeFontParameter();

    private final FileHandle file;

    public FontResource(FileHandle file) {
        this.file = file;
    }

    /**
     * Copy constructor
     * @param resource
     */
    public FontResource(FontResource resource) {
        this(resource.file);
        parameter.size = resource.parameter.size;
        parameter.mono = resource.parameter.mono;
        parameter.hinting = resource.parameter.hinting;
        parameter.color = resource.parameter.color != null ? new Color(resource.parameter.color) : null;
        parameter.gamma = resource.parameter.gamma;
        parameter.renderCount = resource.parameter.renderCount;
        parameter.borderWidth = resource.parameter.borderWidth;
        parameter.borderColor = resource.parameter.borderColor != null ? new Color(resource.parameter.borderColor) : null;
        parameter.borderStraight = resource.parameter.borderStraight;
        parameter.borderGamma = resource.parameter.borderGamma;
        parameter.shadowOffsetX = resource.parameter.shadowOffsetX;
        parameter.shadowOffsetY = resource.parameter.shadowOffsetY;
        parameter.shadowColor = resource.parameter.shadowColor != null ? new Color(resource.parameter.shadowColor) : null;
        parameter.spaceX = resource.parameter.spaceX;
        parameter.spaceY = resource.parameter.spaceY;
        parameter.characters = resource.parameter.characters;
        parameter.kerning = resource.parameter.kerning;
        parameter.flip = resource.parameter.flip;
        parameter.genMipMaps = resource.parameter.genMipMaps;
        parameter.minFilter = resource.parameter.minFilter;
        parameter.magFilter = resource.parameter.magFilter;
        parameter.incremental = resource.parameter.incremental;
    }

    @Override
    public BitmapFont get() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void load() {
        //TODO
    }
}

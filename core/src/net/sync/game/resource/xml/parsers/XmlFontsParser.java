/*
 * Copyright 2020 Vincenzo Fortunato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sync.game.resource.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ObjectMap;
import net.sync.game.resource.Dimension;
import net.sync.game.resource.lazy.FontResource;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.resource.xml.XmlReferenceNotCompatibleException;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.resource.xml.resolvers.XmlColorResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;
import net.sync.game.util.xml.XmlValueResolver;

public class XmlFontsParser extends XmlMapResourceParser<Resource<BitmapFont>> {
    private final net.sync.game.resource.xml.XmlTheme theme;
    private final XmlValueResolver<Dimension> dimensionResolver;
    private final XmlValueResolver<Boolean> booleanResolver;
    private final XmlValueResolver<Color> colorResolver;
    private final XmlValueResolver<Float> floatResolver;
    private final XmlValueResolver<Integer> integerResolver;
    private final XmlValueResolver<String> stringResolver;
    private final XmlValueResolver<Texture.TextureFilter> filterResolver;
    private final XmlValueResolver<FreeTypeFontGenerator.Hinting> hintingResolver;
    private final net.sync.game.resource.xml.resolvers.XmlFontResolver fontResolver = new net.sync.game.resource.xml.resolvers.XmlFontResolver() {
        @Override
        public Resource<BitmapFont> resolveReference(String resourceId) throws XmlReferenceNotFoundException, net.sync.game.resource.xml.XmlReferenceNotCompatibleException {
            Resource<BitmapFont> resource = getResolvedValueOrThrow(resourceId);

            if(resource instanceof FontResource)
                return new FontResource((FontResource) resource);

            throw XmlReferenceNotCompatibleException.incompatibleType(resource.getClass(), FontResource.class);
        }

        @Override
        public Resource<BitmapFont> resolveValue(String value) {
            return new FontResource(getResourceFile().sibling("fonts").child(value));
        }
    };

    /**
     * Create a resource parser from its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlFontsParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
        this.theme = theme;

        //Init resolvers
        this.dimensionResolver = net.sync.game.resource.xml.resolvers.XmlDimensionResolver.from(theme);
        this.booleanResolver = net.sync.game.resource.xml.resolvers.XmlBooleanResolver.from(theme);
        this.colorResolver = XmlColorResolver.from(theme);
        this.floatResolver = net.sync.game.resource.xml.resolvers.XmlFloatResolver.from(theme);
        this.integerResolver = net.sync.game.resource.xml.resolvers.XmlIntegerResolver.from(theme);
        this.stringResolver = net.sync.game.resource.xml.resolvers.XmlStringResolver.from(theme);
        this.filterResolver = new net.sync.game.resource.xml.resolvers.XmlTextureFilterResolver();
        this.hintingResolver = new XmlHintingResolver();
    }

    @Override
    protected void parseAttributes(String id, Resource<BitmapFont> value, XmlParser.Element element) throws XmlParseException {
        for (ObjectMap.Entry<String, String> attribute : element.getAttributes()) {
            if (!attribute.key.equals("id") && !parseAttribute(value, attribute.key, attribute.value)) {
                throw new XmlParseException(String.format(
                        "Unrecognised attribute with name '%s' and value '%s'!", attribute.key, attribute.value));
            }
        }
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("fonts")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'fonts'!");
        }
    }

    @Override
    protected void checkRootChild(XmlParser.Element element) throws XmlParseException {
        if(!element.getName().equals("font")) {
            throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'font'!", element.getName()));
        }
    }

    @Override
    protected net.sync.game.resource.xml.resolvers.XmlReferenceResolver<Resource<BitmapFont>> getResolver(XmlParser.Element element) {
        return fontResolver;
    }

    /**
     * Parses the font element's attribute with the given name and value.
     * @param resource the result resource.
     * @param name the attribute name.
     * @param value the attribute value.
     * @return true if the attribute has been recognised and parsed, false if it has not been recognised.
     * @throws XmlParseException if the attribute has been recognised but cannot be parsed correctly.
     */
    private boolean parseAttribute(Resource<BitmapFont> resource, String name, String value) throws XmlParseException {
        FreeTypeFontGenerator.FreeTypeFontParameter p = ((FontResource) resource).parameter;
        switch(name) {
            case "size":           p.size = dimensionResolver.resolve(value).getIntValue();                      break;
            case "mono":           p.mono = booleanResolver.resolve(value);                                      break;
            case "color":          p.color = colorResolver.resolve(value);                                       break;
            case "gamma":          p.gamma = floatResolver.resolve(value);                                       break;
            case "renderCount":    p.renderCount = integerResolver.resolve(value);                               break;
            case "borderWidth":    p.borderWidth = dimensionResolver.resolve(value).getValue();                  break;
            case "borderColor":    p.borderColor = colorResolver.resolve(value);                                 break;
            case "borderStraight": p.borderStraight = booleanResolver.resolve(value);                            break;
            case "borderGamma":    p.borderGamma = floatResolver.resolve(value);                                 break;
            case "shadowOffsetX":  p.shadowOffsetX = dimensionResolver.resolve(value).getIntValue();             break;
            case "shadowOffsetY":  p.shadowOffsetY = dimensionResolver.resolve(value).getIntValue();             break;
            case "shadowColor":    p.shadowColor = colorResolver.resolve(value);                                 break;
            case "spaceX":         p.spaceX = dimensionResolver.resolve(value).getIntValue();                    break;
            case "spaceY":         p.spaceY = dimensionResolver.resolve(value).getIntValue();                    break;
            case "kerning":        p.kerning = booleanResolver.resolve(value);                                   break;
            case "flip":           p.flip = booleanResolver.resolve(value);                                      break;
            case "genMipMaps":     p.genMipMaps = booleanResolver.resolve(value);                                break;
            case "incremental":    p.incremental = booleanResolver.resolve(value);                               break;
            case "minFilter":      p.minFilter = filterResolver.resolve(value);                                  break;
            case "magFilter":      p.magFilter = filterResolver.resolve(value);                                  break;
            case "characters":     p.characters = stringResolver.resolve(value);                                 break;
            case "hinting":        p.hinting = hintingResolver.resolve(value);                                   break;
            default: return false; //Unrecognised font attribute
        }
        return true;
    }

    private static class XmlHintingResolver implements XmlValueResolver<FreeTypeFontGenerator.Hinting> {
        @Override
        public FreeTypeFontGenerator.Hinting resolve(String value) throws XmlParseException {
            switch(value.trim().toLowerCase()) {
                case "none": return FreeTypeFontGenerator.Hinting.None;
                case "slight": return FreeTypeFontGenerator.Hinting.Slight;
                case "medium": return FreeTypeFontGenerator.Hinting.Medium;
                case "full": return FreeTypeFontGenerator.Hinting.Full;
                case "autoslight":
                case "auto_slight": return FreeTypeFontGenerator.Hinting.AutoSlight;
                case "automedium":
                case "auto_medium": return FreeTypeFontGenerator.Hinting.AutoMedium;
                case "autofull":
                case "auto_full": return FreeTypeFontGenerator.Hinting.AutoFull;
            }
            throw new XmlParseException(String.format("Invalid align format for value '%s'!", value));
        }
    }
}
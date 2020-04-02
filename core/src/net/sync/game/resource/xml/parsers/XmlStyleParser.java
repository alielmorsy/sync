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
import net.sync.game.resource.xml.XmlStyle;
import net.sync.game.resource.xml.XmlTheme;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.util.HashMap;
import java.util.Map;

public class XmlStyleParser extends XmlResourceParser<XmlStyle> {
    /**
     * Create a resource parser from a its file.
     *
     * @param resourceFile the resource file.
     */
    public XmlStyleParser(FileHandle resourceFile, XmlTheme theme) {
        super(resourceFile);
    }

    @Override
    public net.sync.game.resource.xml.XmlStyle parse(XmlParser.Element root) throws XmlParseException {
        //Initialize map with an appropriate size
        Map<String, String> attributes = new HashMap<>(root.getChildCount());

        for(int i = 0; i < root.getChildCount(); i++) {
            XmlParser.Element element = root.getChild(i);

            if(!element.getName().equals("attribute")) {
                throw new XmlParseException(String.format("Unexpected element name '%s'! Expected to be 'attribute'!", element.getName()));
            }

            //Get the attribute name
            String name = element.getAttribute("name");
            if(name == null || name.isEmpty()) {
                throw new XmlParseException("Required attribute 'name' empty or not found in style attribute definition!");
            }

            //Fix the attribute name
            name = name.trim();

            //Check for illegal attribute name
            if(name.equals("style")) {
                throw new XmlParseException("Attribute with name 'style' is not allowed in a style resource!");
            }

            //Check for duplicates
            if(attributes.containsKey(name)) {
                throw new XmlParseException(String.format("Duplicated attribute name '%s'", name));
            }

            //Get the attribute value and bind it to the attribute name
            attributes.put(name, element.hasText() ? element.getText() : "");
        }

        net.sync.game.resource.xml.XmlStyle style = new XmlStyle();
        style.setAttributes(attributes);
        return style;
    }

    @Override
    protected void checkRoot(XmlParser.Element root) throws XmlParseException {
        if(!root.getName().equals("style")) {
            throw new XmlParseException("Unexpected xml root element name. Expected to be 'style'!");
        }
    }
}
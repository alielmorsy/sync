/*
 * Copyright 2018 Vincenzo Fortunato
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

package net.touchmania.game.util.xml;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

//TODO Based on older version. Check libgdx latest release for changes (bug fixes etc...)
/**
 * Lightweight XML parser. Supports a subset of XML features: elements, attributes, text, predefined entities, CDATA,
 * mixed content. Namespaces are parsed as part of the element or attribute name. Prologs and doctypes are ignored.
 * Only 8-bit character encodings are supported.<br>
 * <br>
 * The default behavior is to parse the XML into a DOM. Extends this class and override methods to perform event
 * driven parsing. When this is done, the parse methods will return null.<br>
 * <br>
 * This a modified version of {@link XmlReader} that throws {@link XmlParseException} when
 * something go wrong parsing the document. It also has an improved documentation.
 *
 * @author Nathan Sweet
 * @author Flood2d
 */
public class XmlParser {
    private final List<Element> elements = new ArrayList<>(8);
    private Element root;
    private Element current;
    private final StringBuilder textBuffer = new StringBuilder(64);

    /**
     * Parses an xml string.
     *
     * @param xml the xml string.
     * @return the xml document root element.
     * @throws XmlParseException if the xml string cannot be parsed correctly.
     */
    public Element parse (String xml) throws XmlParseException {
        char[] data = xml.toCharArray();
        return parse(data, 0, data.length);
    }

    /**
     * Parses an xml document from a {@link Reader}.
     * The {@link Reader} will be closed quietly when done.
     *
     * @param reader the reader.
     * @return the xml document root element.
     * @throws IOException if something goes wrong during parsing.
     */
    public Element parse (Reader reader) throws IOException {
        try {
            char[] data = new char[1024];
            int offset = 0;
            while (true) {
                int length = reader.read(data, offset, data.length - offset);
                if (length == -1) break;
                if (length == 0) {
                    char[] newData = new char[data.length * 2];
                    System.arraycopy(data, 0, newData, 0, data.length);
                    data = newData;
                } else
                    offset += length;
            }
            return parse(data, 0, offset);
        } finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    /**
     * Parses an xml document from an {@link InputStream} using UTF-8 encoding.
     * The {@link InputStream} will be closed quietly when done.
     *
     * @param input the input stream.
     * @return the xml document root element.
     * @throws IOException if something goes wrong during parsing.
     */
    public Element parse (InputStream input) throws IOException {
        try {
            return parse(new InputStreamReader(input, "UTF-8"));
        } finally {
            StreamUtils.closeQuietly(input);
        }
    }

    /**
     * Parses an xml file using UTF-8 encoding.
     *
     * @param file the xml file.
     * @return the xml document root element.
     * @throws IOException if something goes wrong during parsing.
     */
    public Element parse (FileHandle file) throws IOException {
        return parse(file.reader("UTF-8"));
    }

    /**
     * Do the actual parsing. Generated by Ragel.
     */
    private Element parse (char[] data, int offset, int length) throws XmlParseException {
        int cs, p = offset, pe = length;

        int s = 0;
        String attributeName = null;
        boolean hasBody = false;

        {
            cs = xml_start;
        }

        {
            int _klen;
            int _trans = 0;
            int _acts;
            int _nacts;
            int _keys;
            int _goto_targ = 0;

            _goto:
            while (true) {
                switch (_goto_targ) {
                    case 0:
                        if (p == pe) {
                            _goto_targ = 4;
                            continue _goto;
                        }
                        if (cs == 0) {
                            _goto_targ = 5;
                            continue _goto;
                        }
                    case 1:
                        _match:
                        do {
                            _keys = _xml_key_offsets[cs];
                            _trans = _xml_index_offsets[cs];
                            _klen = _xml_single_lengths[cs];
                            if (_klen > 0) {
                                int _lower = _keys;
                                int _mid;
                                int _upper = _keys + _klen - 1;
                                while (true) {
                                    if (_upper < _lower) break;

                                    _mid = _lower + ((_upper - _lower) >> 1);
                                    if (data[p] < _xml_trans_keys[_mid])
                                        _upper = _mid - 1;
                                    else if (data[p] > _xml_trans_keys[_mid])
                                        _lower = _mid + 1;
                                    else {
                                        _trans += (_mid - _keys);
                                        break _match;
                                    }
                                }
                                _keys += _klen;
                                _trans += _klen;
                            }

                            _klen = _xml_range_lengths[cs];
                            if (_klen > 0) {
                                int _lower = _keys;
                                int _mid;
                                int _upper = _keys + (_klen << 1) - 2;
                                while (true) {
                                    if (_upper < _lower) break;

                                    _mid = _lower + (((_upper - _lower) >> 1) & ~1);
                                    if (data[p] < _xml_trans_keys[_mid])
                                        _upper = _mid - 2;
                                    else if (data[p] > _xml_trans_keys[_mid + 1])
                                        _lower = _mid + 2;
                                    else {
                                        _trans += ((_mid - _keys) >> 1);
                                        break _match;
                                    }
                                }
                                _trans += _klen;
                            }
                        } while (false);

                        _trans = _xml_indicies[_trans];
                        cs = _xml_trans_targs[_trans];

                        if (_xml_trans_actions[_trans] != 0) {
                            _acts = _xml_trans_actions[_trans];
                            _nacts = (int)_xml_actions[_acts++];
                            while (_nacts-- > 0) {
                                switch (_xml_actions[_acts++]) {
                                    case 0:
                                    {
                                        s = p;
                                    }
                                    break;
                                    case 1:
                                    {
                                        char c = data[s];
                                        if (c == '?' || c == '!') {
                                            if (data[s + 1] == '[' && //
                                                    data[s + 2] == 'C' && //
                                                    data[s + 3] == 'D' && //
                                                    data[s + 4] == 'A' && //
                                                    data[s + 5] == 'T' && //
                                                    data[s + 6] == 'A' && //
                                                    data[s + 7] == '[') {
                                                s += 8;
                                                p = s + 2;
                                                while (data[p - 2] != ']' || data[p - 1] != ']' || data[p] != '>')
                                                    p++;
                                                text(new String(data, s, p - s - 2));
                                            } else if (c == '!' && data[s + 1] == '-' && data[s + 2] == '-') {
                                                p = s + 3;
                                                while (data[p] != '-' || data[p + 1] != '-' || data[p + 2] != '>')
                                                    p++;
                                                p += 2;
                                            } else
                                                while (data[p] != '>')
                                                    p++;
                                            {
                                                cs = 15;
                                                _goto_targ = 2;
                                                if (true) continue _goto;
                                            }
                                        }
                                        hasBody = true;
                                        open(new String(data, s, p - s));
                                    }
                                    break;
                                    case 2:
                                    {
                                        hasBody = false;
                                        close();
                                        {
                                            cs = 15;
                                            _goto_targ = 2;
                                            if (true) continue _goto;
                                        }
                                    }
                                    break;
                                    case 3:
                                    {
                                        close();
                                        {
                                            cs = 15;
                                            _goto_targ = 2;
                                            if (true) continue _goto;
                                        }
                                    }
                                    break;
                                    case 4:
                                    {
                                        if (hasBody) {
                                            cs = 15;
                                            _goto_targ = 2;
                                            if (true) continue _goto;
                                        }
                                    }
                                    break;
                                    case 5:
                                    {
                                        attributeName = new String(data, s, p - s);
                                    }
                                    break;
                                    case 6:
                                    {
                                        attribute(attributeName, new String(data, s, p - s));
                                    }
                                    break;
                                    case 7:
                                    {
                                        int end = p;
                                        while (end != s) {
                                            switch (data[end - 1]) {
                                                case ' ':
                                                case '\t':
                                                case '\n':
                                                case '\r':
                                                    end--;
                                                    continue;
                                            }
                                            break;
                                        }
                                        int current = s;
                                        boolean entityFound = false;
                                        while (current != end) {
                                            if (data[current++] != '&') continue;
                                            int entityStart = current;
                                            while (current != end) {
                                                if (data[current++] != ';') continue;
                                                textBuffer.append(data, s, entityStart - s - 1);
                                                String name = new String(data, entityStart, current - entityStart - 1);
                                                String value = entity(name);
                                                textBuffer.append(value != null ? value : name);
                                                s = current;
                                                entityFound = true;
                                                break;
                                            }
                                        }
                                        if (entityFound) {
                                            if (s < end) textBuffer.append(data, s, end - s);
                                            text(textBuffer.toString());
                                            textBuffer.setLength(0);
                                        } else
                                            text(new String(data, s, end - s));
                                    }
                                    break;
                                }
                            }
                        }

                    case 2:
                        if (cs == 0) {
                            _goto_targ = 5;
                            continue _goto;
                        }
                        if (++p != pe) {
                            _goto_targ = 1;
                            continue _goto;
                        }
                    case 4:
                    case 5:
                }
                break;
            }
        }

        if (p < pe) {
            int lineNumber = 1;
            for (int i = 0; i < p; i++)
                if (data[i] == '\n') lineNumber++;
            throw new XmlParseException("Error parsing XML on line " + lineNumber + " near: "
                    + new String(data, p, Math.min(32, pe - p)));
        } else if (elements.size() != 0) {
            Element element = elements.get(elements.size() - 1);
            elements.clear();
            throw new XmlParseException("Error parsing XML, unclosed element: " + element.getName());
        }
        Element root = this.root;
        this.root = null;
        return root;
    }

    private static byte[] init__xml_actions_0 () {
        return new byte[] {0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 2, 0, 6, 2, 1, 4, 2, 2, 4};
    }

    private static final byte _xml_actions[] = init__xml_actions_0();

    private static byte[] init__xml_key_offsets_0 () {
        return new byte[] {0, 0, 4, 9, 14, 20, 26, 30, 35, 36, 37, 42, 46, 50, 51, 52, 56, 57, 62, 67, 73, 79, 83, 88, 89, 90, 95,
                99, 103, 104, 108, 109, 110, 111, 112, 115};
    }

    private static final byte _xml_key_offsets[] = init__xml_key_offsets_0();

    private static char[] init__xml_trans_keys_0 () {
        return new char[] {32, 60, 9, 13, 32, 47, 62, 9, 13, 32, 47, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32,
                61, 9, 13, 32, 34, 39, 9, 13, 34, 34, 32, 47, 62, 9, 13, 32, 62, 9, 13, 32, 62, 9, 13, 39, 39, 32, 60, 9, 13, 60, 32,
                47, 62, 9, 13, 32, 47, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32, 61, 9, 13, 32, 34, 39, 9, 13, 34,
                34, 32, 47, 62, 9, 13, 32, 62, 9, 13, 32, 62, 9, 13, 60, 32, 47, 9, 13, 62, 62, 39, 39, 32, 9, 13, 0};
    }

    private static final char _xml_trans_keys[] = init__xml_trans_keys_0();

    private static byte[] init__xml_single_lengths_0 () {
        return new byte[] {0, 2, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 1, 2, 1, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 2, 1, 1, 1, 1, 1,
                0};
    }

    private static final byte _xml_single_lengths[] = init__xml_single_lengths_0();

    private static byte[] init__xml_range_lengths_0 () {
        return new byte[] {0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1,
                0};
    }

    private static final byte _xml_range_lengths[] = init__xml_range_lengths_0();

    private static short[] init__xml_index_offsets_0 () {
        return new short[] {0, 0, 4, 9, 14, 20, 26, 30, 35, 37, 39, 44, 48, 52, 54, 56, 60, 62, 67, 72, 78, 84, 88, 93, 95, 97,
                102, 106, 110, 112, 116, 118, 120, 122, 124, 127};
    }

    private static final short _xml_index_offsets[] = init__xml_index_offsets_0();

    private static byte[] init__xml_indicies_0 () {
        return new byte[] {0, 2, 0, 1, 2, 1, 1, 2, 3, 5, 6, 7, 5, 4, 9, 10, 1, 11, 9, 8, 13, 1, 14, 1, 13, 12, 15, 16, 15, 1, 16,
                17, 18, 16, 1, 20, 19, 22, 21, 9, 10, 11, 9, 1, 23, 24, 23, 1, 25, 11, 25, 1, 20, 26, 22, 27, 29, 30, 29, 28, 32, 31,
                30, 34, 1, 30, 33, 36, 37, 38, 36, 35, 40, 41, 1, 42, 40, 39, 44, 1, 45, 1, 44, 43, 46, 47, 46, 1, 47, 48, 49, 47, 1,
                51, 50, 53, 52, 40, 41, 42, 40, 1, 54, 55, 54, 1, 56, 42, 56, 1, 57, 1, 57, 34, 57, 1, 1, 58, 59, 58, 51, 60, 53, 61,
                62, 62, 1, 1, 0};
    }

    private static final byte _xml_indicies[] = init__xml_indicies_0();

    private static byte[] init__xml_trans_targs_0 () {
        return new byte[] {1, 0, 2, 3, 3, 4, 11, 34, 5, 4, 11, 34, 5, 6, 7, 6, 7, 8, 13, 9, 10, 9, 10, 12, 34, 12, 14, 14, 16, 15,
                17, 16, 17, 18, 30, 18, 19, 26, 28, 20, 19, 26, 28, 20, 21, 22, 21, 22, 23, 32, 24, 25, 24, 25, 27, 28, 27, 29, 31, 35,
                33, 33, 34};
    }

    private static final byte _xml_trans_targs[] = init__xml_trans_targs_0();

    private static byte[] init__xml_trans_actions_0 () {
        return new byte[] {0, 0, 0, 1, 0, 3, 3, 20, 1, 0, 0, 9, 0, 11, 11, 0, 0, 0, 0, 1, 17, 0, 13, 5, 23, 0, 1, 0, 1, 0, 0, 0,
                15, 1, 0, 0, 3, 3, 20, 1, 0, 0, 9, 0, 11, 11, 0, 0, 0, 0, 1, 17, 0, 13, 5, 23, 0, 0, 0, 7, 1, 0, 0};
    }

    private static final byte _xml_trans_actions[] = init__xml_trans_actions_0();

    private static final int xml_start = 1;

    private void open (String name) {
        Element child = new Element(name, current);
        Element parent = current;
        if (parent != null) parent.addChild(child);
        elements.add(child);
        current = child;
    }

    private void attribute (String name, String value) {
        current.setAttribute(name, value);
    }

    private String entity (String name) {
        if (name.equals("lt")) return "<";
        if (name.equals("gt")) return ">";
        if (name.equals("amp")) return "&";
        if (name.equals("apos")) return "'";
        if (name.equals("quot")) return "\"";
        if (name.startsWith("#x")) return Character.toString((char)Integer.parseInt(name.substring(2), 16));
        return null;
    }

    private void text (String text) {
        if(current.hasText()) {
            current.setText(current.getText() + text);
        } else {
            current.setText(text);
        }
    }

    private void close () {
        root = elements.remove(elements.size() - 1);
        current = elements.size() > 0 ? elements.get(elements.size() - 1) : null;
    }

    public static class Element {
        private final String name;
        private ObjectMap<String, String> attributes;
        private Array<Element> children;
        private String text;
        private Element parent;

        public Element(String name, Element parent) {
            this.name = name;
            this.parent = parent;
        }

        /**
         * @return the element name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return a map containing element attributes or null if there's no attribute.
         */
        public ObjectMap<String, String> getAttributes() {
            return attributes;
        }

        /**
         *
         * Gets the value of the attribute with the given name.
         * @param name the attribute name.
         * @return the value of the attribute or null if there's no attribute with the given name.
         */
        public String getAttribute(String name) {
            return attributes != null ? attributes.get(name) : null;
        }

        /**
         * Gets the value of the attribute with the given name. If the element
         * has no attribute with the given name the given default value will be
         * returned.
         *
         * @param name the attribute name.
         * @param defaultValue the value to return if the attribute is not found.
         * @return the value of the attribute or the given default value if the attribute is not found.
         */
        public String getAttribute(String name, String defaultValue) {
            if (attributes == null) return defaultValue;
            String value = attributes.get(name);
            if (value == null) return defaultValue;
            return value;
        }

        /**
         * Checks if the element has an attribute with the given name.
         *
         * @param name the attribute name.
         * @return true if the element has the attribute, false otherwise.
         */
        public boolean hasAttribute(String name) {
            return getAttribute(name) != null;
        }

        /**
         * Sets the value of the attribute with the given name.
         *
         * @param name the attribute name.
         * @param value the attribute value.
         */
        private void setAttribute(String name, String value) {
            if (attributes == null) attributes = new ObjectMap<>(8);
            attributes.put(name, value);
        }

        /**
         * @return the count of children elements.
         */
        public int getChildCount() {
            if (children == null) return 0;
            return children.size;
        }

        /**
         * Gets the child element at the given index.
         *
         * @param index the child index.
         * @return the child element at the given index, or null if there's no such element.
         */
        public Element getChild(int index) {
            return children != null ? children.get(index) : null;
        }

        /**
         * Checks if the element has a child element at the given index.
         *
         * @param index the child index.
         * @return true if the element has the child, false otherwise.
         */
        public boolean hasChildAt(int index) {
            return getChild(index) != null;
        }

        /**
         * Adds child element.
         *
         * @param element the element to add as child.
         */
        private void addChild(Element element) {
            if (children == null) children = new Array<>(8);
            children.add(element);
        }

        /**
         * Gets the element's text content.
         *
         * @return the text content of the element, or null if there's no text content.
         */
        public String getText()  {
            return text;
        }

        /**
         * Checks if the element has text content.
         *
         * @return true if the element has text content.
         */
        public boolean hasText() {
            return text != null;
        }

        /**
         * Sets the element's text content.
         *
         * @param text the text content.
         */
        private void setText(String text) {
            this.text = text;
        }

        /**
         * Removes a child element.
         *
         * @param child the element.
         */
        private void removeChild(Element child) {
            if (children != null) children.removeValue(child, true);
        }

        /**
         * Removes this element from its parent.
         */
        public void remove() {
            parent.removeChild(this);
        }

        /**
         * @return the element's parent.
         */
        public Element getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            StringBuilder buffer = new StringBuilder(128);
            buffer.append(indent);
            buffer.append('<');
            buffer.append(name);
            if (attributes != null) {
                for (ObjectMap.Entry<String, String> entry : attributes.entries()) {
                    buffer.append(' ');
                    buffer.append(entry.key);
                    buffer.append("=\"");
                    buffer.append(entry.value);
                    buffer.append('\"');
                }
            }
            if (children == null && (text == null || text.length() == 0))
                buffer.append("/>");
            else {
                buffer.append(">\n");
                String childIndent = indent + '\t';
                if (text != null && text.length() > 0) {
                    buffer.append(childIndent);
                    buffer.append(text);
                    buffer.append('\n');
                }
                if (children != null) {
                    for (Element child : children) {
                        buffer.append(child.toString(childIndent));
                        buffer.append('\n');
                    }
                }
                buffer.append(indent);
                buffer.append("</");
                buffer.append(name);
                buffer.append('>');
            }
            return buffer.toString();
        }

        /**
         * Gets child by its name.
         *
         * @param name the name of the child element.
         * @return the first child having the given name or null, does not recurse
         */
        public Element getChildByName(String name) {
            if (children == null) return null;
            for (int i = 0; i < children.size; i++) {
                Element element = children.get(i);
                if (element.name.equals(name)) return element;
            }
            return null;
        }

        /**
         * Gets child by its name recursively. Will return the first element with
         * the given name by searching it between element's children and between
         * children of element's children recursively.
         *
         * @param name the name of the child {@link Element}
         * @return the first child having the given name or null, recurses.
         */
        public Element getChildByNameRecursively(String name) {
            if (children == null) return null;
            for (int i = 0; i < children.size; i++) {
                Element element = children.get(i);
                if (element.name.equals(name)) return element;
                Element found = element.getChildByNameRecursively(name);
                if (found != null) return found;
            }
            return null;
        }

        /**
         * Gets an array of element's children with the given name.
         *
         * @param name the name of the children
         * @return the children with the given name or an empty {@link Array}, does not recurse.
         */
        public Array<Element> getChildrenByName(String name) {
            Array<Element> result = new Array<Element>();
            if (children == null) return result;
            for (int i = 0; i < children.size; i++) {
                Element child = children.get(i);
                if (child.name.equals(name)) result.add(child);
            }
            return result;
        }

        /**
         * Gets an array of element's children and children of element's children
         * with the given name recursively.
         *
         * @param name the name of the children
         * @return the children with the given name or an empty {@link Array}.
         */
        public Array<Element> getChildrenByNameRecursively(String name) {
            Array<Element> result = new Array<Element>();
            getChildrenByNameRecursively(name, result);
            return result;
        }

        private void getChildrenByNameRecursively(String name, Array<Element> result) {
            if (children == null) return;
            for (int i = 0; i < children.size; i++) {
                Element child = children.get(i);
                if (child.name.equals(name)) result.add(child);
                child.getChildrenByNameRecursively(name, result);
            }
        }

        /**
         * Returns the attribute value with the specified name, or if no attribute is found,
         * the text of a child with the given name.
         *
         * @param name the attribute/child name.
         * @return the attribute value/child text or null otherwise.
         */
        public String get (String name) {
            return get(name, null);
        }

        /**
         * Returns the attribute value with the specified name, or if no attribute is found,
         * the text of a child with the name. If there's no attribute and child with the given
         * name then the given default value will be returned.
         *
         * @param name the attribute/child name.
         * @param defaultValue the default value.
         * @return the attribute value/child text or the default value otherwise.
         */
        public String get (String name, String defaultValue) {
            if (attributes != null) {
                String value = attributes.get(name);
                if (value != null) return value;
            }
            Element child = getChildByName(name);
            if (child == null) return defaultValue;
            String value = child.getText();
            if (value == null) return defaultValue;
            return value;
        }
    }
}

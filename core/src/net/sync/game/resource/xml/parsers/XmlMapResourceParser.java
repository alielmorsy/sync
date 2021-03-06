/*
 * Copyright (c) 2020 Vincenzo Fortunato.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.sync.game.resource.xml.parsers;

import com.badlogic.gdx.files.FileHandle;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.resource.xml.resolvers.XmlReferenceResolver;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static net.sync.game.resource.xml.resolvers.XmlIdentifierResolver.GLOBAL_IDENTIFIER_RESOLVER;

/**
 * Parses resources that presents data as a map. The xml resource must
 * have only one root element and root element's children must have an id
 * and no children. Additional xml elements attributes are parsed when
 * the value is resolved.
 * @param <T> the type of the resource.
 */
public abstract class XmlMapResourceParser<T> extends XmlResourceParser<Map<String, T>> {
    private Map<String, T> resolved;
    private Map<String, UnresolvedResource> unresolved;

    /**
     * Create a resource parser from its file.
     * @param resourceFile the resource file.
     */
    public XmlMapResourceParser(FileHandle resourceFile) {
        super(resourceFile);
    }

    @Override
    public Map<String, T> parse(XmlParser.Element root) throws XmlParseException {
        resolved = new HashMap<>(root.getChildCount());
        unresolved = new HashMap<>();

        for(int i = 0; i < root.getChildCount(); i++) {
            XmlParser.Element element = root.getChild(i);
            checkRootChild(element);

            //Get and validate id
            String id = GLOBAL_IDENTIFIER_RESOLVER.resolve(element.getAttribute("id"));

            //Check for duplicates
            if(resolved.containsKey(id) || unresolved.containsKey(id)) {
                throw new XmlParseException(String.format("Duplicated id '%s'", id));
            }

            //Resolve the resource value
            XmlReferenceResolver<T> resolver = getResolver(element);

            T value;

            try {
                value = resolver.resolve(element.getText());
            } catch(XmlReferenceNotFoundException e) {
                //Mark as unresolved
                markUnresolved(id, XmlReferenceResolver.getReferenceId(element.getText()), element);
                continue;
            }

            //Mark as resolved
            markResolved(id, value, element);
        }

        if(!unresolved.isEmpty()) {
            //Unresolved resources
            StringBuilder exceptionMessage = new StringBuilder();
            exceptionMessage.append("Referencing undeclared resources. Unresolved ids: ");
            Iterator<String> it = unresolved.keySet().iterator();
            while(it.hasNext()) {
                exceptionMessage.append(it.next());
                if(it.hasNext()) {
                    exceptionMessage.append(", ");
                }
            }
            throw new XmlReferenceNotFoundException(exceptionMessage.toString());
        }

        return resolved;
    }

    private void markResolved(String id, T value, XmlParser.Element element) throws XmlParseException {
        resolved.put(id, value);

        if(element != null && element.getAttributes().size > 1) {
            //Has additional attributes
            parseAttributes(id, value, element);
        }

        if(!unresolved.isEmpty()) {
            //Attempt to resolve unresolved resources
            String unresolvedId;
            while((unresolvedId = findUnresolvedId(id)) != null) {
                //Remove from unresolved
                UnresolvedResource resource = unresolved.remove(unresolvedId);
                //Use resolver to resolve reference and create a copy of the referenced value
                XmlReferenceResolver<T> resolver = getResolver(resource.element);
                markResolved(unresolvedId, resolver.resolveReference(resource.resourceId), resource.element);
            }
        }
    }

    private void markUnresolved(String id, String referenceId, XmlParser.Element element) throws XmlParseException {
        unresolved.put(id, new UnresolvedResource(referenceId, element));
    }

    private String findUnresolvedId(String referenceId) {
        for(Map.Entry<String, UnresolvedResource> entry : unresolved.entrySet()) {
            if(entry.getValue().resourceId.equals(referenceId)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Checks root element child's validity
     * @throws XmlParseException if the child is invalid.
     */
    protected abstract void checkRootChild(XmlParser.Element element) throws XmlParseException;

    /**
     * Gets a resolver for the given xml element.
     * @param element the element that has the value to resolve.
     * @return the resolver.
     */
    protected abstract XmlReferenceResolver<T> getResolver(XmlParser.Element element);

    /**
     * Parse additional element attributes onto the given value of the resource with the given id.
     * @param id the resource id.
     * @param value the resource value.
     * @param element the xml element.
     */
    protected void parseAttributes(String id, T value, XmlParser.Element element) throws XmlParseException {}

    /**
     * Gets the map containing all the resolved resources, where the key
     * is the resource id and the value is the resource value.
     * @return the resolved resources map.
     */
    protected Map<String, T> getResolvedValues() {
        return resolved;
    }

    /**
     * Gets the resolved resource with the given resource id.
     * If a resource with the given id cannot be found throws an {@link XmlReferenceNotFoundException}.
     * @param resourceId the resource id, not null.
     * @return the resolved resource, never null.
     * @throws XmlReferenceNotFoundException if a resource with the given id has not been resolved.
     */
    protected T getResolvedValueOrThrow(String resourceId) throws XmlReferenceNotFoundException {
        T resource = getResolvedValues().get(resourceId);
        if(resource == null) {
            throw new XmlReferenceNotFoundException(
                    String.format("Cannot resolve reference with id %s", resourceId));
        }
        return resource;
    }

    private static class UnresolvedResource {
        private String resourceId;
        private XmlParser.Element element;

        UnresolvedResource(String resourceId, XmlParser.Element element) {
            this.resourceId = resourceId;
            this.element = element;
        }
    }
}

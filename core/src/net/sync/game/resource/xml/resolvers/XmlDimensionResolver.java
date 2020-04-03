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

package net.sync.game.resource.xml.resolvers;

import net.sync.game.resource.Dimension;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.util.xml.XmlParseException;

public abstract class XmlDimensionResolver extends XmlReferenceResolver<Dimension> {
    @Override
    protected String getResourceTypeName() {
        return "dimen";
    }

    @Override
    public net.sync.game.resource.Dimension resolveValue(String value) throws XmlParseException {
        try {
            return new net.sync.game.resource.Dimension(Float.parseFloat(value));
        } catch(NumberFormatException e) {
            throw new XmlParseException("Invalid dimension value!");
        }
    }

    public static XmlDimensionResolver from(final ResourceProvider provider) {
        return new XmlDimensionResolver() {
            @Override
            public net.sync.game.resource.Dimension resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException {
                Dimension dimen = provider.getDimension(resourceId);

                if(dimen == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return dimen;
            }
        };
    }
}

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

package net.sync.game.resource.xml.resolvers;

import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.util.xml.XmlParseException;
import net.sync.game.util.xml.XmlValueResolver;

public abstract class XmlIntegerResolver extends XmlReferenceResolver<Integer> {
    /** Global Integer primitive resolver. */
    public static XmlValueResolver<Integer> GLOBAL_INT_RESOLVER = new XmlValueResolver<Integer>() {

        @Override
        public Integer resolve(String value) throws XmlParseException {
            try {
                return Integer.parseInt(value);
            } catch(NumberFormatException e) {
                throw new XmlParseException(String.format("Invalid int value '%s'!", value));
            }
        }
    };

    @Override
    protected String getResourceTypeName() {
        return "int";
    }

    @Override
    public Integer resolveValue(String value) throws XmlParseException {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new XmlParseException(String.format("Invalid int value '%s'!", value));
        }
    }

    public static XmlIntegerResolver from(final ResourceProvider provider) {
        return new XmlIntegerResolver() {
            @Override
            public Integer resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException {
                Integer integer = provider.getInt(resourceId);

                if(integer == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return integer;
            }
        };
    }
}
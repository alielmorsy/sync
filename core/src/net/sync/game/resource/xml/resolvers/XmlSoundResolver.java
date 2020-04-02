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

import com.badlogic.gdx.audio.Sound;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.resource.xml.XmlReferenceNotFoundException;
import net.sync.game.util.xml.XmlParseException;

public abstract class XmlSoundResolver extends XmlReferenceResolver<Resource<Sound>> {
    @Override
    protected String getResourceTypeName() {
        return "sound";
    }

    @Override
    public Resource<Sound> resolveValue(String value) throws XmlParseException {
        throw new XmlParseException(String.format("Cannot resolve the value '%s'", value));
    }

    public static XmlSoundResolver from(final ResourceProvider provider) {
        return new XmlSoundResolver() {
            @Override
            public Resource<Sound> resolveReference(String resourceId) throws net.sync.game.resource.xml.XmlReferenceNotFoundException {
                Resource<Sound> resource = provider.getSound(resourceId);

                if(resource == null)
                    throw new XmlReferenceNotFoundException(
                            String.format("Cannot resolve reference with id '%s'", resourceId));

                return resource;
            }
        };
    }
}
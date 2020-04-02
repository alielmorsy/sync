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

package net.sync.game.resource;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents a graphical layout. All layout actors have a common
 * {@link #getRootActor() root actor} that is the layout starting point.
 * <p> An identifier can be associated to an
 * actor. Actors associated to an id can be retrieved by using {@link #findActorById(String)}. </p>
 */
public interface Layout {
    /**
     * Gets the layout root actor. A layout must have only one root actor!
     *
     * @return the layout root actor, or null if the layout is empty.
     */
    Actor getRootActor();

    /**
     * Gets the layout actor associated to the given id.
     *
     * @param id the actor id.
     * @return the actor associated to the given id, or null if there's no
     * such actor.
     */
    Actor findActorById(String id);
}
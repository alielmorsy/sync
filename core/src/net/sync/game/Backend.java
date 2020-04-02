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

package net.sync.game;

import com.badlogic.gdx.audio.Music;
import net.sync.game.util.ui.DPI;

import javax.sql.DataSource;

public interface Backend {
    /**
     * Init the backend.
     */
    void initBackend();

    DPI getDeviceDPI();

    /**
     * Gets the duration of the given music.
     * @param music the music
     * @return the duration in seconds.
     */
    @Deprecated
    double getDuration(Music music);

    /**
     * Gets the data source of the database.
     * @return the database data source.
     */
    DataSource getDatabaseDataSource();
}
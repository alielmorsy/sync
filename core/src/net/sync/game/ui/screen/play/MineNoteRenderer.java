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

package net.sync.game.ui.screen.play;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.sync.game.Game;
import net.sync.game.resource.lazy.Resource;
import net.sync.game.song.note.MineNote;
import net.sync.game.song.note.Note;

/**
 * @author Vincenzo Fortunato
 */
public class MineNoteRenderer extends BaseNoteRenderer {

    private Resource<Drawable> mineDrawable;

    public MineNoteRenderer(BeatmapView view) {
        super(view);

        mineDrawable = Game.instance().getResources().getDrawable("play_dance_note_mine");
        mineDrawable.load();
    }

    @Override
    public float getNoteRotation(int panel, Note note, double beat, double time) {
        float degrees = -60f * (float) beat;
        degrees %= 360f;
        return degrees;
    }

    @Override
    public boolean isNoteVisible(int panel, Note note, double beat, double time) {
        MineNote mineNote = (MineNote) note;
        return mineNote.getJudgment() == null || !mineNote.getJudgment().hasExploded();
    }

    @Override
    public Drawable getNoteDrawable(int panel, Note note, double beat, double time) {
        return mineDrawable.get();
    }
}
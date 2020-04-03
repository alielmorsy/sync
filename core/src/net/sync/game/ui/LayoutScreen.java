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

package net.sync.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.base.Preconditions;
import net.sync.game.Game;
import net.sync.game.resource.Layout;
import net.sync.game.resource.ResourceProvider;
import net.sync.game.resource.lazy.Resource;

public class LayoutScreen implements Screen {
    private String layoutId;
    private Resource<Layout> layout;

    public LayoutScreen(String layoutId) {
        Preconditions.checkArgument(layoutId != null && !layoutId.isEmpty(), "Null or empty layout id!");
        this.layoutId = layoutId;
    }

    @Override
    public void prepare(Runnable doneCallback) {
        ResourceProvider resources = Game.instance().getResources();
        layout = resources.getLayout(layoutId);
        Preconditions.checkNotNull(layout, String.format("Layout with id '%s' not found!", layoutId));
        doneCallback.run();
    }

    @Override
    public void show(Stage stage) {
        //TODO
    }

    @Override
    public void hide(Runnable doneCallback) {
        //TODO
    }

    @Override
    public void update() {}

    @Override
    public void dispose() {
        //TODO
    }

    @Override
    public boolean isPrepared() {
        return false;
    }
}

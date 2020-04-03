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

package net.sync.game.round;

import net.sync.game.round.judge.Judgment;
import net.sync.game.round.judge.JudgmentHandler;

public class Score implements JudgmentHandler {

    /**
     * Gets the accuracy value at the given time, where time is relative to the start of the music track.
     * Accuracy is represented as a double value between 0.0 and 1.0.
     * @param time the time in seconds relative to the start of the music track.
     * @return the accuracy value, a double between 1.0 and 0.0 (inclusive).
     */
    public double getAccuracyAt(double time) {
        return 0.0D;
    }

    /**
     * Gets the score value at the given time, where time is relative to the start of the music track.
     * Score is represented as a positive long value.
     * @param time the time in seconds relative to the start of the music track.
     * @return the score value, a long positive value.
     */
    public long getScoreAt(double time) {
        return 0;
    }

    /**
     * Gets the combo value at the given time, where time is relative to the start of the music track.
     * Combo is represented as a positive int value.
     * @param time the time in seconds relative to the start of the music track.
     * @return the combo value, an int positive value.
     */
    public int getComboAt(double time) {
        return 0;
    }

    @Override
    public void handleJudgment(double time, Judgment judgment) {

    }
}

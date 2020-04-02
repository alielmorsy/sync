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

package net.sync.game.song;

import com.badlogic.gdx.files.FileHandle;
import net.sync.game.Game;
import net.sync.game.song.sim.SimChartParser;
import net.sync.game.song.sim.SimFile;
import net.sync.game.song.sim.SimParseException;
import net.sync.game.song.sim.SimParser;
import net.sync.game.util.concurrent.Task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static net.sync.game.song.sim.SimParser.parseOrDefault;

public class SongLoader extends Task<net.sync.game.song.Song> {
    /** The sim file max allowed file length in bytes **/
    private static final long MAX_FILE_LENGTH = 10 * 1024 * 1024; //10 megabytes
    private String pack;
    private net.sync.game.song.sim.SimFile simFile;

    /**
     * Creates a song loader from a given directory.
     * @parack pack the pack the song belongs to
     * @param directory the song directory.
     */
    public SongLoader(String pack, FileHandle directory) {
        this(pack, net.sync.game.song.sim.SimFile.searchSimFile(directory, format -> Game.instance().getSettings().getSimFormatPriority(format)));
    }

    /**
     * Creates a song loader from a given sim file.
     * @param pack the pack the song belongs to.
     * @param simFile the song sim file.
     */
    public SongLoader(String pack, SimFile simFile) {
        this.pack = pack;
        this.simFile = simFile;
    }

    @Override
    protected net.sync.game.song.Song call() throws Exception {
        if (simFile == null) {
            //Sim file not found
            throw new FileNotFoundException("There's no supported sim file in the given directory!");
        }

        if (simFile.getFile().length() > MAX_FILE_LENGTH) {
            //Sim file exceeds max file length
            throw new IOException("Sim file size exceeds the maximum allowed file size.");
        }

        //Get and initialize the parser
        SimParser parser = simFile.getFormat().newParser();
        parser.init(simFile);

        //Parse song
        net.sync.game.song.Song song = new Song();
        song.directory = simFile.getFile().parent();
        song.pack = pack;
        song.simFile = simFile;
        song.title = parseOrDefault(parser::parseTitle, null);
        if(song.title == null) song.title = song.directory.name();  //Use directory name instead
        song.subtitle = parseOrDefault(parser::parseSubtitle, null);
        song.artist = parseOrDefault(parser::parseArtist, null);
        song.genre = parseOrDefault(parser::parseGenre, null);
        song.credit = parseOrDefault(parser::parseCredit, null);
        song.bannerPath = parseOrDefault(parser::parseCredit, null);
        song.backgroundPath = parseOrDefault(parser::parseCredit, null);
        song.lyricsPath = parseOrDefault(parser::parseLyricsPath, null);
        song.album = parseOrDefault(parser::parseAlbum, null);
        song.sampleStart = parseOrDefault(parser::parseSampleStart, -1.0f);
        song.sampleLength = parseOrDefault(parser::parseSampleLength, -1.0f);
        song.selectable = parseOrDefault(parser::parseSelectable, true);
        song.musicPath = parser.parseMusicPath();

        //Validate song
        if(song.musicPath == null)
            throw new net.sync.game.song.sim.SimParseException("Required music path not specified");

        //Parse charts
        song.charts = new ArrayList<>();
        for(SimChartParser chartParser : parser.getChartParsers()) {
            chartParser.init();
            try {
                //Parse chart
                Chart chart = new Chart();
                chart.song = song;
                chart.type = chartParser.parseChartType();
                chart.hash = chartParser.getHash();
                chart.difficultyClass = parseOrDefault(chartParser::parseDifficultyClass, null);
                chart.difficultyMeter = parseOrDefault(chartParser::parseDifficultyMeter, -1);
                chart.name = parseOrDefault(chartParser::parseName, null);
                chart.description = parseOrDefault(chartParser::parseDescription, null);
                chart.credit = parseOrDefault(chartParser::parseCredit, null);
                chart.displayBPM = parseOrDefault(chartParser::parseDisplayBPM, null);
                chart.timingData = chartParser.parseTimingData();

                //Validate chart
                if(chart.type == null)
                    throw new net.sync.game.song.sim.SimParseException("Required chart type not specified");
                if(chart.timingData.bpms.isEmpty())
                    throw new net.sync.game.song.sim.SimParseException("Timing data incomplete. Required BPMS not specified");

                song.charts.add(chart);
            } catch(net.sync.game.song.sim.SimParseException e) {
                //Skip invalid chart
            }
        }

        //Validate song charts
        if(song.charts.isEmpty())
            throw new SimParseException("The song doesn't contain any chart");

        return song;
    }


}
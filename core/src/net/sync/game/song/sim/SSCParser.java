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

package net.sync.game.song.sim;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import net.sync.game.song.Beatmap;
import net.sync.game.song.ChartType;
import net.sync.game.song.DifficultyClass;
import net.sync.game.song.DisplayBPM;
import net.sync.game.song.TimingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Parse SSC SIMs - Stepmania format (.ssc file extension).
 * @author Vincenzo Fortunato
 */
public class SSCParser extends SMParser {
    @Override
    protected DataSupplier createDataSupplier(String rawContent) throws SimParseException {
        return new SSCDataSupplier(rawContent);
    }

    @Override
    protected SimChartParser createChartParser(String chartRawContent) {
        return new SSCChartParser(chartRawContent);
    }

    @Override
    protected TimingData parseGlobalTimingData() throws SimParseException {
        //Parse offset, bpms, stops, delays and warps
        TimingData data = super.parseGlobalTimingData();
        parseDelays(data, dataSupplier.getHeaderTagValue("DELAYS"));
        parseWarps(data, dataSupplier.getHeaderTagValue("WARPS"));
        return data;
    }

    private void parseDelays(TimingData data, String value) throws SimParseException {
        if(value != null) {
            data.delays = null; //Reset
            Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = Double.parseDouble(matcher.group(1));
                double length = Double.parseDouble(matcher.group(3)); //length in seconds
                data.putDelay(beat, length);
            }
        }
    }

    private void parseWarps(TimingData data, String value) {
        if(value != null) {
            data.warps = null; //Reset
            Matcher matcher = TIMING_DATA_PATTERN.matcher(value);
            while(matcher.find()) {
                double beat = Double.parseDouble(matcher.group(1));
                double length = Double.parseDouble(matcher.group(3)); //length in beats
                data.putWarp(beat, length);
            }
        }
    }

    private static class SSCDataSupplier implements DataSupplier {
        /** Contains header tags where key is the tag name and value is the tag value **/
        Map<String, String> headerTagsMap = new HashMap<>();
        /** Contains charts data as strings **/
        List<String> chartTagsList = new ArrayList<>();

        SSCDataSupplier(String rawContent) throws SimParseException {
            boolean parsingHeader = true; //Go false when the chart data begins
            Matcher matcher = TagSimParser.TAG_PATTERN.matcher(rawContent);
            StringBuilder strBuilder = new StringBuilder();
            while(matcher.find()) {
                String tagName = matcher.group(1);
                String tagValue = matcher.group(2);

                if(tagName.equalsIgnoreCase("NOTEDATA")) {
                    if(!parsingHeader) {
                        //Go to the next chart
                        chartTagsList.add(strBuilder.toString());
                        strBuilder = new StringBuilder(); //Reset to hold next chart data
                    } else {
                        parsingHeader = false; //Stop parsing header
                        if(headerTagsMap.size() == 0) {
                            //Header tags map cannot be empty at this point
                            throw new SimParseException("Cannot parse sim file!");
                        }
                    }
                } else if(parsingHeader) {
                    //Header tag
                    headerTagsMap.put(tagName.toUpperCase(), tagValue);
                } else {
                    //Chart data tag
                    strBuilder.append(matcher.group());
                }
            }
            if(!parsingHeader && strBuilder.length() > 0) { //Save last parsed chart
                chartTagsList.add(strBuilder.toString());
            }
        }

        @Override
        public String getHeaderTagValue(String tagName) {
            return headerTagsMap.get(tagName);
        }

        @Override
        public List<String> getChartTagValues() {
            return chartTagsList;
        }
    }

    protected class SSCChartParser implements SimChartParser {
        private Map<String, String> tagsMap = new HashMap<>();
        private String hash;

        public SSCChartParser(String chartRawData) {
            //Compute the chart raw data hash
            Hasher hasher = Hashing.sha256().newHasher();
            hasher.putBytes(chartRawData.getBytes());
            hash = hasher.hash().toString();

            Matcher matcher = TagSimParser.TAG_PATTERN.matcher(chartRawData);
            while(matcher.find()) {
                String chartTagName = matcher.group(1);
                String chartTagValue = matcher.group(2);
                tagsMap.put(chartTagName.toUpperCase(), chartTagValue);
            }
        }

        @Override
        public void init() throws SimParseException {
            if(!tagsMap.containsKey("STEPSTYPE"))
                throw new SimParseException("Required STEPSTYPE tag not found.");
            if(!tagsMap.containsKey("NOTES") && !tagsMap.containsKey("NOTES2"))
                throw new SimParseException("Required NOTES tag not found.");
        }

        @Override
        public ChartType parseChartType() throws SimParseException {
            String value = tagsMap.get("STEPSTYPE");
            if(value != null) {
                switch(value.toLowerCase()) {
                    case "dance-single":
                        return ChartType.DANCE_SINGLE;
                    case "pump-single":
                        return ChartType.PUMP_SINGLE;
                    default:
                        throw new SimParseException("Unrecognised/Unsupported chart type: " + value);
                }
            }
            return null;
        }

        @Override
        public DifficultyClass parseDifficultyClass() throws SimParseException {
            return SSCParser.parseDifficultyClass(tagsMap.get("DIFFICULTY"));
        }

        @Override
        public TimingData parseTimingData() throws SimParseException {
            TimingData data = parseGlobalTimingData();
            //Override global timing data with chart timing data when necessary
            parseOffset(data, tagsMap.get("OFFSET"));
            parseBpms(data, tagsMap.get("BPMS"));
            parseStops(data, tagsMap.get("STOPS"));
            parseDelays(data, tagsMap.get("DELAYS"));
            parseWarps(data, tagsMap.get("WARPS"));
            return data;
        }

        @Override
        public Beatmap parseBeatmap() throws SimParseException {
            String beatmapData = tagsMap.get("NOTES");
            if(beatmapData == null) {
                //Sometimes beatmap data is stored as NOTES2
                beatmapData = tagsMap.get("NOTES2");
            }
            if(beatmapData != null) {
                //Parse beatmap data
                ChartType type = parseChartType();
                if(type != null) {
                    switch(type) {
                        case DANCE_SINGLE:
                            return new DanceSingleBeatmapParser(beatmapData).parse();
                        case PUMP_SINGLE:
                            return new PumpSingleBeatmapParser(beatmapData).parse();
                    }
                }
            }
            return null;
        }

        @Override
        public DisplayBPM parseDisplayBPM() throws SimParseException {
            return parseGlobalDisplayBPM();
        }

        @Override
        public int parseDifficultyMeter() throws SimParseException {
            String value = tagsMap.get("METER");
            if(value != null) {
                try {
                    return Integer.parseInt(value);
                } catch(NumberFormatException e) {
                    throw new SimParseException("Cannot parse difficulty meter value: " + value, e);
                }
            }
            return -1;
        }

        @Override
        public String parseName() throws SimParseException {
            return tagsMap.get("CHARTNAME");
        }

        @Override
        public String parseDescription() throws SimParseException {
            return tagsMap.get("DESCRIPTION");
        }

        @Override
        public String parseCredit() throws SimParseException {
            return tagsMap.get("CREDIT");
        }

        @Override
        public String getHash() {
            return hash;
        }
    }
}

/*
 * Copyright (C) 2016 nevesstudios
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.nevesstudios.rsishipstatsscraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

/**
 *
 * @author nevesstudios
 */
public class StatScraper {
    public static final String RSI_SHIP_STATS_URL = "https://robertsspaceindustries.com/ship-specs";
    public static final String JSON_FILE = "rsi_ship_stats.json";
    public static final int JSON_INDENT = 1;
    
    private static final Logger LOGGER = Logger.getLogger(StatScraper.class.getName());
    
    private URL rsiURL = null;
    
    public StatScraper() {
        try {
            LOGGER.info("Creating URL...");
            rsiURL = new URL(RSI_SHIP_STATS_URL);
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    void scrape() {
        BufferedReader reader = null;
        try {
            LOGGER.info("Creating reader...");
            reader = new BufferedReader(new InputStreamReader(rsiURL.openStream()));
            LOGGER.info("Reading html line by line...");
            String line = null;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("    data: ")) {
                    break;
                }
            }
            line = line.substring(10);
            LOGGER.info("Creating JSON Array...");
            JSONArray data = new JSONArray(line);
            LOGGER.info("Creating JSON file...");
            FileUtils.writeStringToFile(new File(JSON_FILE), data.toString(JSON_INDENT), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            try {
                LOGGER.info("Closing reader...");
                if(null != reader) {
                    reader.close();
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        LOGGER.info("Finished scraping stats!");
    }
    
    public static void main(String[] args) {
        StatScraper scraper = new StatScraper();
        scraper.scrape();
    }
}

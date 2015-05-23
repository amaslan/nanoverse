/*
 * Copyright (c) 2014, 2015 David Bruce Borenstein, Annie
 * Maslan and the Trustees of Princeton University.
 *
 * This file is part of the Nanoverse simulation framework
 * (patent pending).
 *
 * This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General
 * Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.serialize.text;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by annie on 5/23/15.
 */

public class LifetimeWriter extends Serializer {

    private static final String FILENAME = "lifetime.txt";

    // It is necessary to flush all data at the end of each iteration, rather
    // than after each flush event, because a state may appear for the first
    // time in the middle of the simulation, and we want an accurate column
    // for every observed state in the census table.

//    ArrayList<Integer> frames = new ArrayList<>();

    ArrayList<Integer> frames;
    // The keys to this map are FRAMES. The values death rates
    HashMap<Integer, Double> histo;

    ArrayList<Double> lifeAve;
    // keep track of lifetime average so can access it in dispatchHalt
    private double l;

    private BufferedWriter bw;

    public LifetimeWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);
    }

    @Override
    public void init() {
        super.init();
        histo = new HashMap<>();
        frames = new ArrayList<>();
        lifeAve = new ArrayList<>();

        String filename = p.getInstancePath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);
    }

    @Override
    public void flush(StepState stepState) {
        int num = stepState.getNumCells();
        double lifeTotal = stepState.getLifeTotal();
        // calculate the average life
        l = lifeTotal / num;
        doFlush(stepState.getFrame(), l);
    }

    private void doFlush(int t, double l) {
        frames.add(t);
        lifeAve.add(l);
    }

    public void dispatchHalt(HaltCondition ex) {
        int t = (int) Math.round(ex.getGillespie());
        doFlush(t, l);
        conclude();
        closed = true;
    }

    private void conclude() {
        // Write out the header
        StringBuilder line = new StringBuilder();
        line.append("frame");

        line.append("\t");
        line.append("lifeAve");

        line.append("\n");

        hAppend(bw, line);

        ArrayList<Integer> sortedFrames = frames;
        int i = 0;
        for (Integer frame : sortedFrames) {
            line = new StringBuilder();
            line.append(frame);
            line.append("\t");
            double life = lifeAve.get(i);
            histo.put(frame, life);
            if (histo.containsKey(frame)) {
                line.append(histo.get(frame));
            }
            else {
                line.append("0.0");
            }
            line.append("\n");
            hAppend(bw, line);
            i++;
        }
        hClose(bw);
    }

    public void close() {
        // Doesn't do anything.
    }
}

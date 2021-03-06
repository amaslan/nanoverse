/*
 * Copyright (c) 2014, 2015 David Bruce Borenstein and the
 * Trustees of Princeton University.
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ParameterWriter extends Serializer {
    private final String PROJECT_FILENAME = "project.xml";

    public ParameterWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);
        mkDir(p.getPath(), true);

        try {
            String paramsFileStr = p.getPath() + '/' + PROJECT_FILENAME;
            File paramsFile = new File(paramsFileStr);
            FileWriter fw = new FileWriter(paramsFile);
            BufferedWriter bwp = new BufferedWriter(fw);

            bwp.write(p.getProjectXML());
            bwp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void init() {
        // Write out an instance-specific XML file.
        if (p.getNumInstances() == 1) {
            return;
        }
        mkDir(p.getInstancePath(), true);

        try {
            String paramsFileStr = p.getInstancePath() + '/' + PROJECT_FILENAME;
            File paramsFile = new File(paramsFileStr);
            FileWriter fw = new FileWriter(paramsFile);
            BufferedWriter bwp = new BufferedWriter(fw);
            bwp.write(p.getInstanceXML());
            bwp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush(StepState stepState) {

    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // Doesn't do anything.

    }

    @Override
    public void close() {
        // Doesn't do anything.

    }

}

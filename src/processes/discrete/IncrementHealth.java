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

package processes.discrete;

//import cells.BehaviorCell;
import cells.Cell;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.cell.CellLookupManager;
//import layers.cell.CellUpdateManager;
import processes.BaseProcessArguments;
import processes.MaxTargetHelper;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.HashSet;

public class IncrementHealth extends CellProcess {

    private Coordinate[] candidates;
    private StepState ss;

    public IncrementHealth(BaseProcessArguments arguments, CellProcessArguments cpArguments) {
        super(arguments, cpArguments);
    }


    @Override
    public void init() {
        candidates = null;
    }

    public void target(GillespieState gs) throws HaltCondition {
        HashSet<Coordinate> candSet = layer.getViewer().getOccupiedSites();
        candidates = candSet.toArray(new Coordinate[0]);
//        if (gs != null) {
//            gs.add(getID(), candidates.length, candidates.length * 1.0D);
//        }
    }

    public void fire(StepState state) throws HaltCondition {
        execute(candidates);
        candidates = null;
    }

    protected void execute(Coordinate[] candidates) throws HaltCondition {
        ss = getLayerManager().getStepState();
        Object[] chosen = MaxTargetHelper.respectMaxTargets(candidates, maxTargets.next(), getGeneralParameters().getRandom());
        Cell[] chosenCells = toCellArray(chosen);
        for (int i = 0; i < chosenCells.length; i++) {
            Cell cell = chosenCells[i];
            CellLookupManager lm = layer.getLookupManager();
            Coordinate currentLocation = lm.getCellLocation(cell);
            incrementHealth(currentLocation);
        }
    }

    private Cell[] toCellArray(Object[] chosen) {
        int n = chosen.length;
        Cell[] cells = new Cell[n];
        for (int i = 0; i < n; i++) {
            Coordinate coord = (Coordinate) chosen[i];
            Cell cell = layer.getViewer().getCell(coord);
            cells[i] = cell;
        }

        return cells;
    }

    protected void incrementHealth(Coordinate coord) throws HaltCondition {
//        CellUpdateManager um = layer.getUpdateManager();
        Cell cell = layer.getViewer().getCell(coord);
        cell.adjustHealth(1.0);
        double health = cell.getHealth();
        ss.incrementLifeTotal(health);
        ss.incrementNumCells(1);
//        double curHealth = cell.getHealth();
//        double adjHealth = curHealth + 1.0;
//        um.updateHealth(coord);
//        // Get child cell
//        CellUpdateManager um = layer.getUpdateManager();
//        Cell child = um.divide(origin);
//
//        Coordinate target = shoveHelper.chooseVacancy(origin);
//
//        shoveHelper.shove(origin, target);
//
//        // Divide the child cell into the vacancy left by the parent
//        layer.getUpdateManager().place(child, origin);
    }

}

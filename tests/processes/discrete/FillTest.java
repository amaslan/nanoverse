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

import cells.MockCell;
import control.GeneralParameters;
import control.arguments.CellDescriptor;
import control.arguments.ConstantInteger;
import control.arguments.MockCellDescriptor;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.set.CoordinateSet;
import geometry.set.CustomSet;
import geometry.shape.Line;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.BaseProcessArguments;
import test.EslimeTestCase;

public class FillTest extends EslimeTestCase {

    private Geometry geom;
    private MockLayerManager lm;
    private GeneralParameters p;
    private BaseProcessArguments arguments;
    private CellDescriptor cd;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        p = makeMockGeneralParameters();
        lm = new MockLayerManager();

        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        geom = new Geometry(lattice, shape, boundary);

        CellLayer layer = new CellLayer(geom);
        lm.setCellLayer(layer);

        arguments = makeBaseProcessArguments(lm, p);

        cd = new MockCellDescriptor();
    }

    public void testBaseBehavior() throws Exception {
        CellProcessArguments cpArguments = makeCellProcessArguments(geom);
        Fill query = new Fill(arguments, cpArguments, false, cd);
        query.init();
        query.iterate();

        assertEquals(10, lm.getCellLayer().getViewer().getOccupiedSites().size());
    }

    public void testRespectActiveSites() throws Exception {
        CoordinateSet activeSites = new CustomSet();
        for (int y = 2; y < 5; y++) {
            activeSites.add(new Coordinate(0, y, 0));
        }

        CellProcessArguments cpArguments = new CellProcessArguments(activeSites, new ConstantInteger(-1));

        Fill query = new Fill(arguments, cpArguments, false, cd);
        query.init();
        query.iterate();

        for (Coordinate c : geom.getCanonicalSites()) {
            boolean actual = lm.getCellLayer().getViewer().isOccupied(c);
            boolean expected = activeSites.contains(c);
            assertEquals(expected, actual);
        }
    }

    public void testSkipFilledYes() throws Exception {
        Coordinate c = new Coordinate(0, 2, 0);
        lm.getCellLayer().getUpdateManager().place(new MockCell(2), c);
        doSkipFilledTest(true);

        // Original cell should not have been replaced, because it was skipped
        assertEquals(2, lm.getCellLayer().getViewer().getState(c));
    }

    public void testSkipFilledNo() throws Exception {
        Coordinate c = new Coordinate(0, 2, 0);
        lm.getCellLayer().getUpdateManager().place(new MockCell(2), c);
        doSkipFilledTest(false);
    }

    private void doSkipFilledTest(boolean skip) throws Exception {
        CellProcessArguments cpArguments = makeCellProcessArguments(geom);
        Fill query = new Fill(arguments, cpArguments, skip, cd);
        query.init();

        boolean thrown = false;
        try {
            query.iterate();
        } catch (Exception ex) {
            thrown = true;
        }

        assertTrue(thrown == !skip);
    }
}
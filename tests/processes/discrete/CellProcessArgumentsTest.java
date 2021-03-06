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

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import geometry.set.CoordinateSet;
import geometry.set.CustomSet;
import test.EslimeTestCase;

public class CellProcessArgumentsTest extends EslimeTestCase {

    private CoordinateSet activeSites;
    private Argument<Integer> maxTargets;
    private CellProcessArguments query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activeSites = new CustomSet();
        maxTargets = new ConstantInteger(0);

        query = new CellProcessArguments(activeSites, maxTargets);
    }

    public void testGetActiveSites() throws Exception {
        assert (query.getActiveSites() == activeSites);
    }

    public void testGetMaxTargets() throws Exception {
        assert (query.getMaxTargets() == maxTargets);
    }
}
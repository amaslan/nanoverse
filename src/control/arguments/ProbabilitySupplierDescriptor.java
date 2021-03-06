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

package control.arguments;

import agent.action.stochastic.ProbabilitySupplier;
import cells.BehaviorCell;

import java.util.function.Function;

/**
 * Created by dbborens on 1/26/15.
 */
public class ProbabilitySupplierDescriptor<T extends ProbabilitySupplier> {

    private Function<BehaviorCell, T> constructor;

    public ProbabilitySupplierDescriptor(Function<BehaviorCell, T> constructor) {
        this.constructor = constructor;
    }

    public T instantiate(BehaviorCell cell) {
        return constructor.apply(cell);
    }
}

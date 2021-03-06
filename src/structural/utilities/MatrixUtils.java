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

package structural.utilities;

import no.uib.cipr.matrix.BandMatrix;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

public abstract class MatrixUtils {
    /**
     * Returns a matrix in a tab-separated matrix form.
     *
     * @return
     */
    public static String matrixForm(Matrix m) {
        StringBuilder sb = new StringBuilder();

        int r = m.numRows();
        int c = m.numColumns();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                sb.append(m.get(i, j));
                sb.append('\t');
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Returns a vector in a tab-separated matrix form.
     *
     * @return
     */
    public static String asMatrix(Vector v, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.size(); i++) {
            if (i % n == 0) {
                sb.append("\n");
            }

            sb.append(String.format("%.3f", v.get(i)));
            sb.append('\t');
        }

        return sb.toString();
    }

    /**
     * Returns an identity matrix of size n.
     * <p>
     * TODO: There must be a built-in for this.
     */
    public static Matrix I(int n) {
        Matrix m = new BandMatrix(n, 0, 0);
        for (int i = 0; i < n; i++)
            m.set(i, i, 1d);

        return m;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.store.precomputedDists.permutations;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.fs.FSGlobal;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.metricSpace.Dataset;

/**
 *
 * @author Vlada
 */
public class FSPrecomputedPivotPermutationsLoaderImpl extends FSPrecomputedDistancesMatrixLoaderImpl {

    public int[][] loadPivotPermutations(Dataset dataset, int pivotCount) {
        float[][] floats = super.loadPrecomPivotsToObjectsDists(dataset, pivotCount);
        int[][] ret = new int[floats.length][floats[0].length];
        for (int i = 0; i < floats.length; i++) {
            float[] row = floats[i];
            for (int j = 0; j < row.length; j++) {
                ret[i][j] = (int) row[j];
            }
        }
        return ret;
    }

    @Override
    public File deriveFileForDatasetAndPivots(String datasetName, String pivotSetName, int pivotCount, boolean willBeDeleted) {
        File ret = new File(FSGlobal.PRECOMPUTED_PIVOT_PERMUTATIONS_FOLDER, datasetName + "_" + pivotSetName + "_" + pivotCount + "pivots.csv.gz");
        FSGlobal.checkFileExistence(ret, willBeDeleted);
        if (!willBeDeleted && !ret.exists()) {
            Logger.getLogger(FSPrecomputedDistancesMatrixLoaderImpl.class.getName()).log(Level.WARNING, "File with precomputed distances does not exist: {0}", ret.getAbsolutePath());
        }
        return ret;
    }

}

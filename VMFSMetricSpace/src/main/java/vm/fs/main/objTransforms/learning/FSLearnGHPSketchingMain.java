package vm.fs.main.objTransforms.learning;

import java.util.Map;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.dataTransforms.FSGHPSketchesPivotPairsStorageImpl;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.metricSpace.Dataset;
import vm.objTransforms.learning.LearnSketchingGHP;
import vm.objTransforms.storeLearned.PivotPairsStoreInterface;

/**
 *
 * @author xmic
 */
public class FSLearnGHPSketchingMain {

    public static void main(String[] args) {
        PivotPairsStoreInterface sketchingTechStorage = new FSGHPSketchesPivotPairsStorageImpl();
        int[] sketchesLengths = new int[]{384, 1024};
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(true)
        };
        for (Dataset dataset : datasets) {
            run(dataset, sketchingTechStorage, sketchesLengths);
            System.gc();
        }
    }

    private static void run(Dataset dataset, PivotPairsStoreInterface sketchingTechStorage, int[] sketchesLengths) {
        int sampleSize = 1000000; // 100k - 1M, depends od the size of data and dist comp. cost
        int pivotCount = 1024; // min 512, max 1024 - RAM and time grow with the second power of this param!
        LearnSketchingGHP learn = new LearnSketchingGHP(dataset, sketchingTechStorage, pivotCount, 15000);
        // voluntary step and voluntary arguments - is the precomputed distances does not excist, that deals with it automatically
        FSPrecomputedDistancesMatrixLoaderImpl pd = new FSPrecomputedDistancesMatrixLoaderImpl();
        float[][] dists = pd.loadPrecomPivotsToObjectsDists(dataset, pivotCount);
        Map<Comparable, Integer> columnHeaders = pd.getColumnHeaders();
        Map<Comparable, Integer> rowHeaders = pd.getRowHeaders();
        // voluntary step and voluntary arguments
        learn.evaluate(dataset, sampleSize, sketchesLengths, 0.5f, dists, columnHeaders, rowHeaders);
    }
}

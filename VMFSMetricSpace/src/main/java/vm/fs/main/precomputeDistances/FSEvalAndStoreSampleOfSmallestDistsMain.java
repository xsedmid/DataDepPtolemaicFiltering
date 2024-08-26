package vm.fs.main.precomputeDistances;

import java.util.TreeSet;
import java.util.logging.Logger;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.precomputedDists.FSPrecomputedDistPairsStorageImpl;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedPairsOfDistancesStorage;

/**
 *
 * @author Vlada
 */
public class FSEvalAndStoreSampleOfSmallestDistsMain {

    public static final Logger LOG = Logger.getLogger(FSEvalAndStoreSampleOfSmallestDistsMain.class.getName());

    public static void main(String[] args) {
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset()
//            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(true),
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(true),
//            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(true),
//            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(true),
//            new FSDatasetInstanceSingularizator.DeCAFDataset(),
//            new FSDatasetInstanceSingularizator.SIFTdataset(),
//            new FSDatasetInstanceSingularizator.MPEG7dataset(),
//            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_256Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_64Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_192Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_128Dataset(),
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    public static void run(Dataset dataset) {
        TreeSet result = dataset.evaluateSmallestDistances(AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_SET_SIZE, AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_QUERY_SET_SIZE, AbstractPrecomputedPairsOfDistancesStorage.IMPLICIT_K);
        FSPrecomputedDistPairsStorageImpl storage = new FSPrecomputedDistPairsStorageImpl(dataset.getDatasetName(), AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_SET_SIZE, AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_QUERY_SET_SIZE);
        storage.storePrecomputedDistances(result);
    }

    public static boolean existsForDataset(Dataset dataset) {
        FSPrecomputedDistPairsStorageImpl storage = new FSPrecomputedDistPairsStorageImpl(dataset.getDatasetName(), AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_SET_SIZE, AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_QUERY_SET_SIZE);
        return storage.getFileForResults(false).exists();
    }

}

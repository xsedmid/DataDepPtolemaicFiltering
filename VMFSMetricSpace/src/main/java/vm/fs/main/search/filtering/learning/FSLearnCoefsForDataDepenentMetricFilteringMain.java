package vm.fs.main.search.filtering.learning;

import java.io.File;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.auxiliaryForDistBounding.FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.precomputedDists.FSPrecomputedDistPairsStorageImpl;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.bounding.onepivot.learning.LearningTriangleInequalityWithLimitedAngles;
import static vm.metricSpace.distance.bounding.onepivot.learning.LearningTriangleInequalityWithLimitedAngles.RATIO_OF_SMALLEST_DISTS;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedPairsOfDistancesStorage;
import vm.search.algorithm.SearchingAlgorithm;

/**
 *
 * @author Vlada
 */
public class FSLearnCoefsForDataDepenentMetricFilteringMain {

    public static final Integer SAMPLE_O_COUNT = 10000;
    public static final Integer SAMPLE_Q_COUNT = 1000;

    public static void main(String[] args) {
        boolean publicQueries = false;
        Dataset[] datasets = new Dataset[]{
            //            new FSDatasetInstanceSingularizator.SIFTdataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            //            new FSDatasetInstanceSingularizator.MPEG7dataset(),
            //            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
            //            new FSDatasetInstanceSingularizator.LAION_100k_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_300k_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries)
            //            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(true)

            new FSDatasetInstanceSingularizator.RandomDataset20Uniform()
//            new FSDatasetInstanceSingularizator.RandomDataset10Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset15Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset25Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset30Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset35Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset40Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset50Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset60Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset70Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset80Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset90Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset100Uniform()
        };

        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    public static void run(Dataset dataset) {
        int pivotCount = dataset.getRecommendedNumberOfPivotsForFiltering();
        AbstractPrecomputedPairsOfDistancesStorage smallDistSample = new FSPrecomputedDistPairsStorageImpl(dataset.getDatasetName(), SAMPLE_O_COUNT, SAMPLE_Q_COUNT);

        FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl storage = new FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl();
        LearningTriangleInequalityWithLimitedAngles learning = new LearningTriangleInequalityWithLimitedAngles(dataset, smallDistSample, pivotCount, SAMPLE_O_COUNT, SAMPLE_Q_COUNT, storage, dataset.getDatasetName());
        learning.execute();
    }

    public static boolean existsForDataset(Dataset dataset) {
        FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl storage = new FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl();
        String result = storage.getResultDescription(dataset.getDatasetName(), dataset.getRecommendedNumberOfPivotsForFiltering(), SAMPLE_O_COUNT, SAMPLE_Q_COUNT, RATIO_OF_SMALLEST_DISTS);
        File file = storage.getFile(result, false);
        return file.exists();
    }

}

package vm.fs.main.search.filtering.learning;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl;
import static vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getFile;
import vm.fs.store.precomputedDists.FSPrecomputedDistPairsStorageImpl;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.bounding.twopivots.learning.LearningCoefsForPtolemyInequalityWithLimitedAngles;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedPairsOfDistancesStorage;
import vm.search.algorithm.SearchingAlgorithm;

/**
 *
 * @author Vlada
 */
public class FSLearnCoefsForDataDependentPtolemyFilteringMain {

    public static final Integer SAMPLE_SET_SIZE = 10000;
    public static final Integer SAMPLE_QUERY_SET_SIZE = 1000;
    public static final Boolean ALL_PIVOT_PAIRS = true;

    public static void main(String[] args) throws IOException {
        Dataset[] datasets = new Dataset[]{
            //            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
            //            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(true),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(true),
            //            new FSDatasetInstanceSingularizator.SIFTdataset(),
            //            new FSDatasetInstanceSingularizator.MPEG7dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_64Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_128Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_192Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_256Dataset()
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
        List<Object> pivots = dataset.getPivots(dataset.getRecommendedNumberOfPivotsForFiltering());
        AbstractPrecomputedPairsOfDistancesStorage smallDistSample = new FSPrecomputedDistPairsStorageImpl(dataset.getDatasetName(), SAMPLE_SET_SIZE, SAMPLE_QUERY_SET_SIZE);
        FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl storage = new FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl();

        TreeSet<Map.Entry<String, Float>> smallDistsOfSampleObjectsAndQueries = smallDistSample.loadPrecomputedDistances();
        LearningCoefsForPtolemyInequalityWithLimitedAngles learning = new LearningCoefsForPtolemyInequalityWithLimitedAngles(dataset, pivots, SAMPLE_SET_SIZE, SAMPLE_QUERY_SET_SIZE, smallDistsOfSampleObjectsAndQueries, storage, dataset.getDatasetName(), ALL_PIVOT_PAIRS);
        learning.execute();
    }

    public static boolean existsForDataset(Dataset dataset) {
        FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl storage = new FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl();
        String fileName = storage.getNameOfFileWithCoefs(dataset.getDatasetName(), dataset.getRecommendedNumberOfPivotsForFiltering(), true);
        File file = getFile(fileName, false);
        return file.exists();
    }
}

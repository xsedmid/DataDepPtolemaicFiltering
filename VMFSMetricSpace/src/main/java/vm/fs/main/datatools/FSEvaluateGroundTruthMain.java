package vm.fs.main.datatools;

import java.io.File;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.search.algorithm.impl.GroundTruthEvaluator;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.metricSpace.DatasetOfCandidates;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.queryResults.QueryNearestNeighboursStoreInterface;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;

/**
 *
 * @author Vlada
 */
// see learning of all the metadata for the filtering in FSLearnMetadataForAllPivotFilterings? It includes the ground-truth as well.
public class FSEvaluateGroundTruthMain {

    public static final Logger LOG = Logger.getLogger(FSEvaluateGroundTruthMain.class.getName());

    public static void main(String[] args) {
        boolean publicQueries = true;
        Dataset[] datasets = new Dataset[]{
            //            new FSDatasetInstanceSingularizator.RandomDataset10Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset15Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset25Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset30Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset35Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset40Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset50Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset60Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset70Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset80Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset90Uniform(),
            //            new FSDatasetInstanceSingularizator.RandomDataset100Uniform(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_192Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_256Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_1024Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(publicQueries),
            //            
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_192Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_256Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_1024Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_512Dataset(publicQueries),
            //
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_192Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_256Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_1024Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_512Dataset(publicQueries)
            //                    new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset()
            //            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries)
            //            new FSDatasetInstanceSingularizator.MPEG7dataset(),
            //            new FSDatasetInstanceSingularizator.SIFTdataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Euclid(publicQueries)
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Dot(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries)
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Angular(publicQueries)
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries), 
            //            new FSDatasetInstanceSingularizator.DeCAFDataset()
            new FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates(),
            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_PCA256_Candidates()
        };
        for (Dataset dataset : datasets) {
            run(dataset, GroundTruthEvaluator.K_IMPLICIT_FOR_QUERIES);
            run(dataset, GroundTruthEvaluator.K_IMPLICIT_FOR_QUERIES);
//            run(dataset, GroundTruthEvaluator.K_IMPLICIT_FOR_GROUND_TRUTH);
        }
    }

    public static void run(Dataset dataset, int k) {
        String datasetName = dataset.getDatasetName();
        AbstractMetricSpace space = dataset.getMetricSpace();

        QueryNearestNeighboursStoreInterface groundTruthStorage = new FSNearestNeighboursStorageImpl();

        List<Object> queryObjects = dataset.getQueryObjects(1000);
        GroundTruthEvaluator gte = new GroundTruthEvaluator(dataset, k, Float.MAX_VALUE, queryObjects.size());
        TreeSet[] results;
        if (dataset instanceof DatasetOfCandidates) {
            results = gte.evaluateIteratorsSequentiallyForEachQuery(dataset, queryObjects, k);
        } else {
            if (k == GroundTruthEvaluator.K_IMPLICIT_FOR_GROUND_TRUTH) {
                results = gte.evaluateIteratorInParallel(dataset.getMetricObjectsFromDataset(datasetName), datasetName, dataset.getQuerySetName());
            } else {
                results = gte.evaluateIteratorSequentially(dataset.getMetricObjectsFromDataset(), datasetName, dataset.getQuerySetName());
            }
        }
        LOG.log(Level.INFO, "Storing statistics of queries");
        FSQueryExecutionStatsStoreImpl statsStorage = new FSQueryExecutionStatsStoreImpl(dataset.getDatasetName(), dataset.getQuerySetName(), k, dataset.getDatasetName(), dataset.getQuerySetName(), "ground_truth", null);
        statsStorage.storeStatsForQueries(gte.getDistCompsPerQueries(), gte.getTimesPerQueries(), gte.getAddditionalStats());
        statsStorage.save();

        LOG.log(Level.INFO, "Storing results of queries");
        groundTruthStorage.storeQueryResults(space, queryObjects, results, k, dataset.getDatasetName(), dataset.getQuerySetName(), "ground_truth");

        System.gc();
    }

    public static boolean existsForDataset(Dataset dataset, Integer k) {
        if (k == null || k <= 0) {
            k = GroundTruthEvaluator.K_IMPLICIT_FOR_GROUND_TRUTH;
        }
        FSNearestNeighboursStorageImpl groundTruthStorage = new FSNearestNeighboursStorageImpl();
        File fileWithResults = groundTruthStorage.getFileWithResults("ground_truth", dataset.getDatasetName(), dataset.getQuerySetName(), k, false);
        return fileWithResults.exists();
    }

}

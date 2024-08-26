package vm.fs.main.search.perform;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.DataTypeConvertor;
import vm.datatools.Tools;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.dataTransforms.FSSVDStorageImpl;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.ToolsMetricDomain;
import vm.objTransforms.perform.PCAMetricObjectTransformer;
import vm.objTransforms.storeLearned.SVDStoreInterface;
import vm.queryResults.QueryNearestNeighboursStoreInterface;
import vm.queryResults.errorOnDistEvaluation.ErrorOnDistEvaluator;
import vm.queryResults.recallEvaluation.RecallOfCandsSetsEvaluator;
import vm.search.algorithm.SearchingAlgorithm;
import vm.search.algorithm.impl.SimRelSeqScanKNNCandSet;
import vm.simRel.impl.SimRelEuclideanPCAImplForTesting;
import vm.simRel.impl.learn.SimRelEuclideanPCAForLearning;

/**
 *
 * @author Vlada
 */
public class FSKNNQueriesSeqScanWithSimRelMain {

    private static final Logger LOG = Logger.getLogger(FSKNNQueriesSeqScanWithSimRelMain.class.getName());

    public static final Boolean STORE_RESULTS = true;
    public static final Boolean FULL_RERANK = true;
    public static final Boolean INVOLVE_OBJS_UNKNOWN_RELATION = true;

    public static void main(String[] args) {
        boolean publicQueries = false;
        Dataset fullDataset = new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries);
        Dataset pcaDataset = new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset();
//        Dataset fullDataset = new M2DatasetInstanceSingularizator.DeCAF20MDataset();
//        Dataset pcaDataset = new FSDatasetInstanceSingularizator.DeCAF20M_PCA256Dataset();
        run(fullDataset, pcaDataset);
    }

    private static void run(Dataset fullDataset, Dataset pcaDataset) {

        /* kNN queries - the result set size */
        int k = 30;
        /* the length of the shortened vectors */
        int pcaLength = 256;
        /*  prefix of the shortened vectors used by the simRel */
        int prefixLength = 24;
        /* the name of the PCA-shortened dataset */
        int kPCA = 100;
        /* number of query objects to learn t(\Omega) thresholds. We use different objects than the pivots tested. */
        int querySampleCount = 200;
        /* size of the data sample to learn t(\Omega) thresholds, SISAP: 100K */
        int dataSampleCount = 1000000;
        /* percentile - defined in the paper. Defines the precision of the simRel */
        float percentile = 0.95f;

//        /* learn thresholds t(\Omega) */
        float[] learnedErrors = learnTOmegaThresholds(pcaDataset, querySampleCount, dataSampleCount, pcaLength, prefixLength, kPCA, percentile);
        SVDStoreInterface svdStorage = new FSSVDStorageImpl(fullDataset.getDatasetName(), 500000, false);
        PCAMetricObjectTransformer pcaTransformer = initPCA(fullDataset.getMetricSpace(), pcaDataset.getMetricSpace(), svdStorage, prefixLength);
        // TEST QUERIES
        SimRelEuclideanPCAImplForTesting simRel = new SimRelEuclideanPCAImplForTesting(learnedErrors, prefixLength);
//        String resultName = "pure_simRel_PCA" + pcaLength + "_decideUsingFirst" + prefixLength + "_learnErrorsOn__queries" + querySampleCount + "_dataSamples" + dataSampleCount + "_kSearching" + k + "_percentile" + percentile;
//        String resultName = "simRel_IS_20M_SSD_kPCA" + kPCA + "_involveUnknown_" + INVOLVE_OBJS_UNKNOWN_RELATION + "__rerank_" + FULL_RERANK + "__PCA" + pcaLength + "_decideUsingFirst" + prefixLength + "_learnToleranceOn__queries" + querySampleCount + "_dataSamples" + dataSampleCount + "_k" + k + "_percentile" + percentile;
        String resultName = "simRel_LAION10M_kPCA" + kPCA + "_involveUnknown_" + INVOLVE_OBJS_UNKNOWN_RELATION + "__rerank_" + FULL_RERANK + "__PCA" + pcaLength + "_decideUsingFirst" + prefixLength + "_learnToleranceOn__queries" + querySampleCount + "_dataSamples" + dataSampleCount + "_k" + k + "_percentile" + percentile;
//        String resultName = "pure_checkSingleX_deleteMany_simRel_PCA" + pcaLength + "_decideUsingFirst" + prefixLength + "_learnErrorsOn__queries" + querySampleCount + "_dataSamples" + dataSampleCount + "_kSearching" + k + "_percentile" + percentile;
        /* Storage to store the results of the kNN queries */
        QueryNearestNeighboursStoreInterface resultsStorage = new FSNearestNeighboursStorageImpl();
        /* Storage to store the stats about the kNN queries */

        Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> fileNameData = new HashMap<>();
        fileNameData.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_name, fullDataset.getDatasetName());
        fileNameData.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_query_set_name, fullDataset.getQuerySetName());
        fileNameData.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.ground_truth_nn_count, Integer.toString(k));
        fileNameData.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_name, pcaDataset.getDatasetName());
        fileNameData.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.cand_set_query_set_name, pcaDataset.getQuerySetName());
        fileNameData.put(FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME.storing_result_name, resultName);
        FSQueryExecutionStatsStoreImpl statsStorage = new FSQueryExecutionStatsStoreImpl(fileNameData);

        testQueries(fullDataset, pcaDataset, simRel, pcaTransformer, prefixLength, kPCA, k, resultsStorage, resultName, statsStorage, fileNameData);
//        testQueries(fullDataset, pcaDataset, null, null, prefixLength, kPCA, k, resultsStorage, resultName, statsStorage, fileNameData);
    }

    private static float[] learnTOmegaThresholds(Dataset pcaDataset, int querySampleCount, int dataSampleCount, int pcaLength, int prefixLength, int kPCA, float percentileWrong) {
        List<Object> querySamples = pcaDataset.getPivots(querySampleCount);
        List<Object> sampleOfDataset = pcaDataset.getSampleOfDataset(dataSampleCount);
        SimRelEuclideanPCAForLearning simRelLearn = new SimRelEuclideanPCAForLearning(pcaLength);
        SearchingAlgorithm alg = new SimRelSeqScanKNNCandSet(simRelLearn, kPCA);

        simRelLearn.resetLearning(pcaLength);
        for (int i = 0; i < querySamples.size(); i++) {
            Object queryObj = querySamples.get(i);
            simRelLearn.resetCounters(pcaLength);
            alg.candSetKnnSearch(pcaDataset.getMetricSpace(), queryObj, kPCA, sampleOfDataset.iterator());
            LOG.log(Level.INFO, "Learning tresholds with the query obj {0}", new Object[]{i + 1});
        }
        float[][] ret = simRelLearn.getDiffWhenWrong(percentileWrong, prefixLength);
        return ret[0];
    }

    private static void testQueries(Dataset<float[]> fullDataset, Dataset<float[]> pcaDataset, SimRelEuclideanPCAImplForTesting simRel, PCAMetricObjectTransformer pcaTransformer, int prefixLength, int kPCA, int k, QueryNearestNeighboursStoreInterface resultsStorage, String resultName, FSQueryExecutionStatsStoreImpl statsStorage, Map<FSQueryExecutionStatsStoreImpl.DATA_NAMES_IN_FILE_NAME, String> fileNameDataForRecallStorage) {
        List<Object> pcaData = Tools.getObjectsFromIterator(pcaDataset.getMetricObjectsFromDataset());
        pcaData = ToolsMetricDomain.getPrefixesOfVectors(pcaDataset.getMetricSpace(), pcaData, prefixLength);
        System.gc();
        Map<Comparable, float[]> mapOfAllFullObjects = null;
        if (FULL_RERANK) {
            mapOfAllFullObjects = fullDataset.getKeyValueStorage();
//            Iterator<Object> fullDatasetIterator = fullDataset.getMetricObjectsFromDataset();
//            mapOfAllFullObjects = ToolsMetricDomain.getMetricObjectsAsIdDataMap(fullDataset.getMetricSpace(), fullDatasetIterator, true);
//            MVStore storage = VMMVStorageMain.openStorage(fullDataset.getDatasetName());
//            mapOfAllFullObjects = VMMVStorageMain.getStoredMap(storage);
        }
        List<Object> fullQueries = fullDataset.getQueryObjects();
        SimRelSeqScanKNNCandSet alg = new SimRelSeqScanKNNCandSet(simRel, kPCA, INVOLVE_OBJS_UNKNOWN_RELATION);
        AbstractMetricSpace metricSpaceOfFullDataset = fullDataset.getMetricSpace();
        AbstractMetricSpace pcaDatasetMetricSpace = pcaDataset.getMetricSpace();
        for (int i = 0; i < Math.min(fullQueries.size(), 1000); i++) {
            long time = -System.currentTimeMillis();
            Object fullQueryObj = fullQueries.get(i);
            Comparable queryObjId = metricSpaceOfFullDataset.getIDOfMetricObject(fullQueryObj);
            AbstractMap.SimpleEntry<Comparable, float[]> pcaQueryObj = (AbstractMap.SimpleEntry<Comparable, float[]>) pcaTransformer.transformMetricObject(fullQueryObj, prefixLength);

            List<Comparable> candSetObjIDs = alg.candSetKnnSearch(pcaDatasetMetricSpace, pcaQueryObj, kPCA, pcaData.iterator());
            TreeSet<Map.Entry<Comparable, Float>> rerankCandidateSet = alg.rerankCandidateSet(metricSpaceOfFullDataset, fullQueryObj, k, fullDataset.getDistanceFunction(), mapOfAllFullObjects, candSetObjIDs);
            time += System.currentTimeMillis();
            alg.incTime(queryObjId, time);
            if (STORE_RESULTS) {
                resultsStorage.storeQueryResult(queryObjId, rerankCandidateSet, k, fullDataset.getDatasetName(), fullDataset.getQuerySetName(), resultName);
            }
            long[] earlyStopsPerCoords = (long[]) alg.getSimRelStatsOfLastExecutedQuery();
            String earlyStopsPerCoordsString = DataTypeConvertor.longToString(earlyStopsPerCoords, ",");
            if (STORE_RESULTS) {
                statsStorage.storeStatsForQuery(queryObjId, alg.getDistCompsForQuery(queryObjId), alg.getTimeOfQuery(queryObjId), earlyStopsPerCoordsString);
            } else {
                System.out.println(earlyStopsPerCoordsString);
            }
            LOG.log(Level.INFO, "Processed query {0}", new Object[]{i + 1});
        }
        if (STORE_RESULTS) {
            statsStorage.save();
            LOG.log(Level.INFO, "Evaluating accuracy of queries");
            FSRecallOfCandidateSetsStorageImpl recallStorage = new FSRecallOfCandidateSetsStorageImpl(fileNameDataForRecallStorage);
            RecallOfCandsSetsEvaluator recallEvaluator = new RecallOfCandsSetsEvaluator(resultsStorage, recallStorage);
            recallEvaluator.evaluateAndStoreRecallsOfQueries(fullDataset.getDatasetName(), fullDataset.getQuerySetName(), k, fullDataset.getDatasetName(), fullDataset.getQuerySetName(), resultName, k);
            recallStorage.save();
            LOG.log(Level.INFO, "Evaluating error on distance");
            ErrorOnDistEvaluator eodEvaluator = new ErrorOnDistEvaluator(resultsStorage, recallStorage);
            eodEvaluator.evaluateAndStoreErrorsOnDist(fullDataset.getDatasetName(), fullDataset.getQuerySetName(), k, pcaDataset.getDatasetName(), pcaDataset.getQuerySetName(), resultName, k);
            recallStorage.save();
        }
    }

    private static PCAMetricObjectTransformer initPCA(AbstractMetricSpace<float[]> originalMetricSpace, AbstractMetricSpace<float[]> pcaMetricSpace, SVDStoreInterface svdStorage, int pcaPreffixLength) {
        LOG.log(Level.INFO, "Start loading instance of the PCA with length {0}", pcaPreffixLength);
        float[][] vtMatrixFull = svdStorage.getVTMatrix();
        float[][] vtMatrix = Tools.shrinkMatrix(vtMatrixFull, pcaPreffixLength, vtMatrixFull[0].length);
        return new PCAMetricObjectTransformer(vtMatrix, svdStorage.getMeansOverColumns(), originalMetricSpace, pcaMetricSpace);
    }

}

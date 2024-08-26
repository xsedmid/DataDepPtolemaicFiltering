package vm.fs.main.search.perform;

import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.Tools;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.main.search.filtering.learning.FSLearnCoefsForDataDepenentMetricFilteringMain;
import vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.auxiliaryForDistBounding.FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.DatasetOfCandidates;
import vm.metricSpace.ToolsMetricDomain;
import vm.metricSpace.distance.DistanceFunctionInterface;
import vm.metricSpace.distance.bounding.BoundsOnDistanceEstimation;
import vm.metricSpace.distance.bounding.onepivot.AbstractOnePivotFilter;
import vm.metricSpace.distance.bounding.onepivot.impl.TriangleInequality;
import vm.metricSpace.distance.bounding.twopivots.AbstractPtolemaicBasedFiltering;
import vm.metricSpace.distance.bounding.twopivots.AbstractTwoPivotsFilter;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentGeneralisedPtolemaicFiltering;
import vm.metricSpace.distance.bounding.twopivots.impl.FourPointBasedFiltering;
import vm.metricSpace.distance.bounding.twopivots.impl.PtolemaicFiltering;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedDistancesMatrixLoader;
import vm.queryResults.recallEvaluation.RecallOfCandsSetsEvaluator;
import vm.search.algorithm.SearchingAlgorithm;
import vm.search.algorithm.impl.GroundTruthEvaluator;
import vm.search.algorithm.impl.KNNSearchWithOnePivotFiltering;
import vm.search.algorithm.impl.KNNSearchWithGenericTwoPivotFiltering;
import vm.search.algorithm.impl.KNNSearchWithPtolemaicFiltering;

/**
 *
 * @author Vlada
 */
public class FSKNNQueriesSeqScanWithFilteringMain {

    private static final Logger LOG = Logger.getLogger(FSKNNQueriesSeqScanWithFilteringMain.class.getName());

    public static void main(String[] args) {
//        vm.javatools.Tools.sleep(15);
        boolean publicQueries = true;
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates()
//            new FSDatasetInstanceSingularizator.FaissDyn_Clip_100M_PCA256_Candidates(300)
//            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_Candidates()
//            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_PCA256_Candidates()
        //            new FSDatasetInstanceSingularizator.DeCAFDataset(),
        //            new FSDatasetInstanceSingularizator.SIFTdataset(),
        //            new FSDatasetInstanceSingularizator.MPEG7dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Euclid(publicQueries)
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Angular(publicQueries)
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Dot(publicQueries),
        //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(publicQueries)
        //            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset()
        //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries)
        //            new FSDatasetInstanceSingularizator.RandomDataset10Uniform()
        //            new FSDatasetInstanceSingularizator.RandomDataset15Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset25Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset30Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset35Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset40Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset50Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset60Uniform(),
//                    new FSDatasetInstanceSingularizator.RandomDataset70Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset80Uniform(),
        //            new FSDatasetInstanceSingularizator.RandomDataset90Uniform(),
//                    new FSDatasetInstanceSingularizator.RandomDataset100Uniform()
        };

        int k = GroundTruthEvaluator.K_IMPLICIT_FOR_QUERIES;

        for (Dataset dataset : datasets) {
            int pivotCount = dataset.getRecommendedNumberOfPivotsForFiltering();
            if (pivotCount < 0) {
                throw new IllegalArgumentException("Dataset " + dataset.getDatasetName() + " does not specify the number of pivots");
            }
            List pivots = dataset.getPivots(pivotCount);
            BoundsOnDistanceEstimation[] filters = initTestedFilters(null, pivots, dataset, k);
            int repetitions = dataset instanceof DatasetOfCandidates ? 3 : 2;
            for (int i = 0; i < repetitions; i++) {
                run(dataset, filters, pivots, k);
            }
            System.gc();
            pd = null;
        }
    }

    private static AbstractPrecomputedDistancesMatrixLoader pd;
    private static float[][] poDists = null;

    public static final void run(Dataset dataset, BoundsOnDistanceEstimation[] filters, List pivots, int k) {
        for (BoundsOnDistanceEstimation filter : filters) {
            Logger.getLogger(FSKNNQueriesSeqScanWithFilteringMain.class.getName()).log(Level.INFO, "Processing filter {0}", filter.getTechFullName());
            run(dataset, filter, pivots, k);
        }
    }

    private static void run(Dataset dataset, BoundsOnDistanceEstimation filter, List pivots, int k) {
        LOG.log(Level.INFO, "Going to search for {0}NN in dataset {1} with the filter {2}", new Object[]{k, dataset.getDatasetName(), filter.getTechFullName()});
        int maxObjectsCount = -1;
        int pivotCount = pivots.size();
        AbstractMetricSpace metricSpace = dataset.getMetricSpace();
        DistanceFunctionInterface df = dataset.getDistanceFunction();

        initPODists(dataset, pivotCount, maxObjectsCount, pivots);

        List queries = dataset.getQueryObjects(1000);

        float[][] pivotPivotDists = metricSpace.getDistanceMap(df, pivots, pivots);

        SearchingAlgorithm alg = initAlg(filter, dataset, metricSpace, pivots, df, pivotPivotDists);

        TreeSet[] results;
        if (dataset instanceof DatasetOfCandidates) {
            results = alg.evaluateIteratorsSequentiallyForEachQuery(dataset, queries, k);
        } else {
            results = alg.completeKnnFilteringWithQuerySet(metricSpace, queries, k, dataset.getMetricObjectsFromDataset(maxObjectsCount), 1);
        }

        LOG.log(Level.INFO, "Storing statistics of queries");
        FSQueryExecutionStatsStoreImpl statsStorage = new FSQueryExecutionStatsStoreImpl(dataset.getDatasetName(), dataset.getQuerySetName(), k, dataset.getDatasetName(), dataset.getQuerySetName(), alg.getResultName(), null);
        statsStorage.storeStatsForQueries(alg.getDistCompsPerQueries(), alg.getTimesPerQueries(), alg.getAddditionalStats());
        statsStorage.save();

        LOG.log(Level.INFO, "Storing results of queries");
        FSNearestNeighboursStorageImpl resultsStorage = new FSNearestNeighboursStorageImpl();
        resultsStorage.storeQueryResults(metricSpace, queries, results, k, dataset.getDatasetName(), dataset.getQuerySetName(), alg.getResultName());

        LOG.log(Level.INFO, "Evaluating accuracy of queries");
        FSRecallOfCandidateSetsStorageImpl recallStorage = new FSRecallOfCandidateSetsStorageImpl(dataset.getDatasetName(), dataset.getQuerySetName(), k, dataset.getDatasetName(), dataset.getQuerySetName(), alg.getResultName(), null);
        RecallOfCandsSetsEvaluator evaluator = new RecallOfCandsSetsEvaluator(new FSNearestNeighboursStorageImpl(), recallStorage);
        Dataset groundTruthDataset = dataset;
        if (dataset instanceof DatasetOfCandidates) {
            groundTruthDataset = ((DatasetOfCandidates) dataset).getOrigDataset();
        }
        evaluator.evaluateAndStoreRecallsOfQueries(groundTruthDataset.getDatasetName(), groundTruthDataset.getQuerySetName(), k, dataset.getDatasetName(), dataset.getQuerySetName(), alg.getResultName(), k);
        recallStorage.save();
    }

    public static void initPODists(Dataset dataset, int pivotCount, int maxObjectsCount, List pivots) {
        initPODists(dataset, pivotCount, maxObjectsCount, pivots, true);
    }

    public static void initPODists(Dataset dataset, int pivotCount, int maxObjectsCount, List pivots, boolean allowCache) {
        AbstractMetricSpace metricSpace = dataset.getMetricSpace();
        DistanceFunctionInterface df = dataset.getDistanceFunction();
        Dataset origDataset = dataset;
        if (dataset instanceof DatasetOfCandidates) {
            origDataset = ((DatasetOfCandidates) dataset).getOrigDataset();
        }
        if (!allowCache) {
            pd = null;
        }
        if (pd == null) {
            pd = new FSPrecomputedDistancesMatrixLoaderImpl();
            poDists = pd.loadPrecomPivotsToObjectsDists(origDataset, pivotCount);
        }
        if (poDists == null || poDists.length == 0) {
            int precomputedDatasetSize = origDataset.getPrecomputedDatasetSize();
            pd = ToolsMetricDomain.evaluateMatrixOfDistances(origDataset.getMetricObjectsFromDataset(maxObjectsCount), pivots, metricSpace, df, precomputedDatasetSize);
            poDists = pd.loadPrecomPivotsToObjectsDists(null, -1);
        }

    }

    public static AbstractPrecomputedDistancesMatrixLoader getPd() {
        return pd;
    }

    public static float[][] getPoDists() {
        return poDists;
    }

    public static SearchingAlgorithm initAlg(BoundsOnDistanceEstimation filter, Dataset dataset, AbstractMetricSpace metricSpace, List pivots, DistanceFunctionInterface df, float[][] pivotPivotDists) {
        SearchingAlgorithm alg;
        if (filter instanceof AbstractPtolemaicBasedFiltering) {
            alg = new KNNSearchWithPtolemaicFiltering(metricSpace, (AbstractPtolemaicBasedFiltering) filter, pivots, poDists, pd.getRowHeaders(), df);
            if (filter instanceof DataDependentGeneralisedPtolemaicFiltering && dataset.equals(new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset())) {
                KNNSearchWithPtolemaicFiltering tmp = (KNNSearchWithPtolemaicFiltering) alg;
//STRAIN
                tmp.setObjBeforeSeqScan(100000);
                tmp.setThresholdOnLBsPerObjForSeqScan(20);
            }
            if (filter instanceof DataDependentGeneralisedPtolemaicFiltering && dataset.equals(new FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates())) {
                KNNSearchWithPtolemaicFiltering tmp = (KNNSearchWithPtolemaicFiltering) alg;
//STRAIN
                tmp.setObjBeforeSeqScan(20);
                tmp.setThresholdOnLBsPerObjForSeqScan(62.5f);
            }
        } else if (filter instanceof AbstractTwoPivotsFilter) {
            alg = new KNNSearchWithGenericTwoPivotFiltering(metricSpace, (AbstractTwoPivotsFilter) filter, pivots, poDists, pd.getRowHeaders(), pivotPivotDists, df);
        } else if (filter instanceof AbstractOnePivotFilter) {
            alg = new KNNSearchWithOnePivotFiltering(metricSpace, (AbstractOnePivotFilter) filter, pivots, poDists, pd.getRowHeaders(), pd.getColumnHeaders(), df);
        } else {
            throw new IllegalArgumentException("What a weird algorithm ... This class is for the pivot filtering, did you notice?");
        }
        return alg;
    }

    public static final BoundsOnDistanceEstimation[] initTestedFilters(String resultSetPrefix, List pivots, Dataset dataset, int k) {
        int pivotCount = pivots.size();
        List pivotsData = dataset.getMetricSpace().getDataOfMetricObjects(pivots);
        if (resultSetPrefix == null) {
            resultSetPrefix = Tools.getDateYYYYMM() + "_" + pivotCount + "_pivots_" + k + "NN";
        }
        AbstractOnePivotFilter metricFiltering = new TriangleInequality(resultSetPrefix);
        AbstractOnePivotFilter dataDependentMetricFiltering = FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstanceTriangleInequalityWithLimitedAngles(
                resultSetPrefix,
                pivotCount,
                FSLearnCoefsForDataDepenentMetricFilteringMain.SAMPLE_O_COUNT,
                FSLearnCoefsForDataDepenentMetricFilteringMain.SAMPLE_Q_COUNT,
                dataset
        );
        AbstractTwoPivotsFilter fourPointPropertyBased = new FourPointBasedFiltering(resultSetPrefix);

        AbstractPtolemaicBasedFiltering ptolemaicFilteringRandomPivots = new PtolemaicFiltering(resultSetPrefix, pivotsData, dataset.getDistanceFunction(), false);
        AbstractPtolemaicBasedFiltering ptolemaicFiltering = new PtolemaicFiltering(resultSetPrefix, pivotsData, dataset.getDistanceFunction(), true);
        DataDependentGeneralisedPtolemaicFiltering dataDependentPtolemaicFiltering = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstance(
                resultSetPrefix,
                dataset,
                pivotCount
        );
        return new BoundsOnDistanceEstimation[]{
            metricFiltering,
            dataDependentMetricFiltering,
            fourPointPropertyBased,
            ptolemaicFilteringRandomPivots,
            ptolemaicFiltering,
            dataDependentPtolemaicFiltering
        };
    }
}

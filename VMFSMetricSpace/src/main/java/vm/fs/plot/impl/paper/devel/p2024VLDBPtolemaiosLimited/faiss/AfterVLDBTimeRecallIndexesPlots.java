/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.faiss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jfree.chart.JFreeChart;
import vm.datatools.Tools;
import vm.fs.dataset.FSDatasetInstances;
import vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain;
import vm.fs.plot.FSAbstractPlotterFromResults;
import static vm.fs.plot.FSPlotFolders.Y2025_AFTER_VLDB_PTOLEMAIOS_LIMITED_FILTERING_INDEXES;
import vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.auxiliaryForDistBounding.FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.fs.store.queryResults.FSQueryExecutionStatsStoreImpl;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.metricSpace.Dataset;
import vm.metricSpace.DatasetOfCandidates;
import vm.metricSpace.distance.bounding.BoundsOnDistanceEstimation;
import vm.metricSpace.distance.bounding.onepivot.AbstractOnePivotFilter;
import vm.metricSpace.distance.bounding.onepivot.impl.TriangleInequality;
import vm.metricSpace.distance.bounding.twopivots.AbstractPtolemaicBasedFiltering;
import vm.metricSpace.distance.bounding.twopivots.AbstractTwoPivotsFilter;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentPtolemaicFiltering;
import vm.metricSpace.distance.bounding.twopivots.impl.FourPointBasedFiltering;
import vm.metricSpace.distance.bounding.twopivots.impl.PtolemaicFiltering;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedDistancesMatrixLoader;
import vm.plot.impl.LinesOrPointsPlotter;
import static vm.search.algorithm.SearchingAlgorithm.STEP_COUNTS_FOR_CAND_SE_PROCESSING_FROM_INDEX;
import vm.search.algorithm.impl.GroundTruthEvaluator;

/**
 *
 * @author xmic
 */
public class AfterVLDBTimeRecallIndexesPlots {

    public static void main(String[] args) {
        DatasetOfCandidates[] datasets = new DatasetOfCandidates[]{
            new FSDatasetInstances.Faiss_Clip_100M_PCA256_Candidates(), //            new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_Candidates()
        };

        int k = GroundTruthEvaluator.K_IMPLICIT_FOR_QUERIES;

        for (DatasetOfCandidates dataset : datasets) {
            int pivotCount = dataset.getRecommendedNumberOfPivotsForFiltering();
            if (pivotCount < 0) {
                throw new IllegalArgumentException("Dataset " + dataset.getDatasetName() + " does not specify the number of pivots");
            }
            List<Integer> cands = getCandCounts(dataset.getCandidatesProvided(), STEP_COUNTS_FOR_CAND_SE_PROCESSING_FROM_INDEX);
            BoundsOnDistanceEstimation[] filters = initTestedFilters(null, dataset.getPivots(pivotCount), dataset, k);
            run(dataset, filters, cands, k);
            System.gc();
        }

    }

    private static void run(DatasetOfCandidates dataset, BoundsOnDistanceEstimation[] filters, List<Integer> cands, int k) {
        LinesOrPointsPlotter plotter = new LinesOrPointsPlotter();
        plotter.setIncludeZeroForXAxis(true);
        plotter.setIncludeZeroForYAxis(false);
        float[][] xValues = new float[filters.length][cands.size()];
        AbstractPrecomputedDistancesMatrixLoader pd = new FSPrecomputedDistancesMatrixLoaderImpl();
        float[][] yValues = new float[filters.length][cands.size()];
        for (int i = 0; i < filters.length; i++) {
            BoundsOnDistanceEstimation filter = filters[i];
            String resultName = FSKNNQueriesSeqScanWithFilteringMain.initAlg(filter, dataset, dataset.getMetricSpace(), dataset.getPivots(dataset.getRecommendedNumberOfPivotsForFiltering()), dataset.getDistanceFunction(), null).getResultName();
            for (int j = 0; j < cands.size(); j++) {
                Integer cand = cands.get(j);
                float sumTimes = 0;
                float sumRecall = 0;
                dataset.setMaxNumberOfCandidatesToReturn(cand);
                FSRecallOfCandidateSetsStorageImpl recallStorage = new FSRecallOfCandidateSetsStorageImpl(dataset.getDatasetName(), dataset.getQuerySetName(), k, dataset.getDatasetName(), dataset.getQuerySetName(), resultName, null);
                Map<String, TreeMap<FSQueryExecutionStatsStoreImpl.QUERY_STATS, String>> map = recallStorage.getContent();
                for (Map.Entry<String, TreeMap<FSQueryExecutionStatsStoreImpl.QUERY_STATS, String>> entry : map.entrySet()) {
                    TreeMap<FSQueryExecutionStatsStoreImpl.QUERY_STATS, String> queryResult = entry.getValue();
                    String recall = queryResult.get(FSQueryExecutionStatsStoreImpl.QUERY_STATS.recall);
//                    String time = queryResult.get(FSQueryExecutionStatsStoreImpl.QUERY_STATS.query_execution_time);
                    String[] additional = queryResult.get(FSQueryExecutionStatsStoreImpl.QUERY_STATS.additional_stats).split(",");
                    String time = additional[2 * j + 1];
                    sumRecall += Float.parseFloat(recall);
                    sumTimes += Float.parseFloat(time);
                }
                xValues[i][j] = sumTimes / map.size();
                yValues[i][j] = sumRecall / map.size();
            }
        }
        String[] tracesNames = transformFiltersToTracesNames(filters);
        JFreeChart plot = plotter.createPlot("", "Time (ms)", "Recall of 30NN", tracesNames, null, xValues, yValues);
        File f = new File(Y2025_AFTER_VLDB_PTOLEMAIOS_LIMITED_FILTERING_INDEXES, dataset.getDatasetName());
        f.getParentFile().mkdirs();
        plotter.storePlotPDF(f, plot);
    }

    private static List<Integer> getCandCounts(int candidatesProvided, int numberOfPoints) {
        List<Integer> ret = new ArrayList<>();
        float step = candidatesProvided / numberOfPoints;
        for (int i = 1; i <= numberOfPoints; i++) {
            ret.add((int) (i * step));
        }
        return ret;
    }

    private static String[] transformFiltersToTracesNames(BoundsOnDistanceEstimation[] filters) {
        return FSAbstractPlotterFromResults.strings(
                "Triangle Ineq.",
                "Data-dep. Metric Filtering",
                "Four Point Prop.",
                "Ptolemaic Filtering Random pivots",
                "Ptolemaic with Dyn. Pivots",
                "Data-dep. Ptolemaic Filering"
//                "Sequential Brute Force"
        ); 
    }

    public static final BoundsOnDistanceEstimation[] initTestedFilters(String resultSetPrefix, List pivots, Dataset dataset, Integer k) {
        int pivotCount = pivots.size();
        List pivotsData = dataset.getMetricSpace().getDataOfMetricObjects(pivots);
        if (resultSetPrefix == null) {
            resultSetPrefix = Tools.getDateYYYYMM() + "_" + pivotCount + "_pivots";
        }
        if (k != null) {
            resultSetPrefix += "_" + k + "NN";
        }
        AbstractOnePivotFilter metricFiltering = new TriangleInequality(resultSetPrefix);
        AbstractOnePivotFilter dataDependentMetricFiltering = FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstanceTriangleInequalityWithLimitedAngles(resultSetPrefix, pivotCount, dataset);
        AbstractTwoPivotsFilter fourPointPropertyBased = new FourPointBasedFiltering(resultSetPrefix);

        AbstractPtolemaicBasedFiltering ptolemaicFilteringRandomPivots = new PtolemaicFiltering(resultSetPrefix, pivotsData, dataset.getDistanceFunction(), false);
        AbstractPtolemaicBasedFiltering ptolemaicFiltering = new PtolemaicFiltering(resultSetPrefix, pivotsData, dataset.getDistanceFunction(), true);
        DataDependentPtolemaicFiltering dataDependentPtolemaicFiltering = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstance(
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

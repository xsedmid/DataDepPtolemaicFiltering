/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.papers.impl.main.vldb2024;

import java.util.List;
import vm.fs.main.datatools.FSPrepareNewDatasetForPivotFilterings;
import vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain;
import vm.fs.metricSpaceImpl.FSMetricSpaceImpl;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.DatasetOfCandidates;
import vm.metricSpace.data.RandomVectorsGenerator;
import vm.metricSpace.data.toStringConvertors.MetricObjectDataToStringInterface;
import vm.metricSpace.data.toStringConvertors.SingularisedConvertors;
import vm.metricSpace.distance.DistanceFunctionInterface;
import vm.metricSpace.distance.bounding.BoundsOnDistanceEstimation;
import vm.metricSpace.distance.impl.L2OnFloatsArray;
import vm.search.algorithm.SearchingAlgorithm;
import vm.search.algorithm.impl.GroundTruthEvaluator;

/**
 *
 * @author Vlada
 */
public class VLDB24DataDepPtolemaicFiltering {

    /**
     * Just for naming produces files.
     */
    public static final String DATASET_PREFIX_NAME = "VLDB_random";
    /**
     * Not necessary for small dataset, useful if time-consuming.
     */
    public static final Boolean STORE_DISTANCES_TO_PIVOTS = false;
    /**
     * If true, checks whether the filterings are learnt, and if so, asks for
     * re-learning them. If false and filterings are learnt, they are
     * immediatelly re-learnt without asking. Results of the filtering are
     * rewritten with each new run.
     */
    public static final Boolean SKIP_EVERYHING_PREPARED = true;

    public static void main(String[] args) {
        // params. Feel free to modify.
        int[] dimensionalities = {10, 40, 50, 60, 70, 80, 90, 100, 150};
        // number of vectors in each dataset
        int datasetObjectCount = 1000 * 1000;
        // number of pivots used for the filterings. In current settings, it also equals the number of defined lower bounds per each distance.
        int pivotsCount = 128;
        // number of generated and examined query objects
        int queriesCount = 1000;
        // the result set size for kNN search
        int k = 30;

        // Do not modify from here.
        Dataset[] datasets = createOrGetRandomUniformDatasetQueriesPivots(datasetObjectCount, pivotsCount, queriesCount, dimensionalities);
        for (Dataset dataset : datasets) {
            FSPrepareNewDatasetForPivotFilterings.setSkipEverythingEvaluated(SKIP_EVERYHING_PREPARED);
            learnFilterings(dataset);
            List pivots = dataset.getPivots(pivotsCount);
            FSKNNQueriesSeqScanWithFilteringMain.initPODists(dataset, pivotsCount, -1, pivots, false);
            BoundsOnDistanceEstimation[] filters = FSKNNQueriesSeqScanWithFilteringMain.initTestedFilters("VLDB_", pivots, dataset, k);
            FSPrepareNewDatasetForPivotFilterings.evaluateGroundTruth(dataset, GroundTruthEvaluator.K_IMPLICIT_FOR_GROUND_TRUTH);
            FSPrepareNewDatasetForPivotFilterings.evaluateGroundTruth(dataset, k);
            FSPrepareNewDatasetForPivotFilterings.setSkipEverythingEvaluated(false);
            FSKNNQueriesSeqScanWithFilteringMain.run(dataset, filters, pivots, k);
            createPlotsForDataset(dataset, filters, k, pivots);
            System.gc();
        }

    }

    private static Dataset[] createOrGetRandomUniformDatasetQueriesPivots(int datasetObjectCount, int pivotsCount, int queriesCount, int... dimensionalities) {
        DistanceFunctionInterface df = new L2OnFloatsArray();
        FSMetricSpaceImpl metricSpace = new FSMetricSpaceImpl(df);
        MetricObjectDataToStringInterface<float[]> dataSerializator = SingularisedConvertors.FLOAT_VECTOR_SPACE;
        int[] sizes = {datasetObjectCount, queriesCount, pivotsCount};
        RandomVectorsGenerator generator = new RandomVectorsGenerator(metricSpace, new FSMetricSpacesStorage(metricSpace, dataSerializator), sizes, dimensionalities);
        return generator.createOrGet(DATASET_PREFIX_NAME);
    }

    private static void learnFilterings(Dataset dataset) {
        FSPrepareNewDatasetForPivotFilterings.precomputeDatasetSize(dataset);
        Dataset origDataset = dataset;
        if (dataset instanceof DatasetOfCandidates) {
            origDataset = ((DatasetOfCandidates) dataset).getOrigDataset();
            FSPrepareNewDatasetForPivotFilterings.plotDistanceDensity(origDataset);
        }
        FSPrepareNewDatasetForPivotFilterings.plotDistanceDensity(dataset); // not necessary
        FSPrepareNewDatasetForPivotFilterings.evaluateSampleOfSmallestDistances(dataset);
        if (STORE_DISTANCES_TO_PIVOTS) {
            FSPrepareNewDatasetForPivotFilterings.precomputeObjectToPivotDists(origDataset);
        }
        FSPrepareNewDatasetForPivotFilterings.learnDataDependentMetricFiltering(dataset);
        FSPrepareNewDatasetForPivotFilterings.learnDataDependentPtolemaicFiltering(dataset);
    }

    private static void createPlotsForDataset(Dataset dataset, BoundsOnDistanceEstimation[] filters, int k, List pivots) {
        String[] folders = new String[filters.length + 1];
        int i;
        for (i = 0; i < filters.length; i++) {
            BoundsOnDistanceEstimation filter = filters[i];
            SearchingAlgorithm alg = FSKNNQueriesSeqScanWithFilteringMain.initAlg(filter, dataset, dataset.getMetricSpace(), pivots, null, null);
            folders[i] = alg.getResultName();
        }
        folders[i] = "ground_truth";
        VLDBPlotter plotter = new VLDBPlotter(k, dataset, folders);
        plotter.makePlots();
    }
}

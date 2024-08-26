/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.search.perform;

import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.fs.store.queryResults.recallEvaluation.FSRecallOfCandidateSetsStorageImpl;
import vm.fs.store.partitioning.FSGRAPPLEPartitioningStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.datasetPartitioning.StorageDatasetPartitionsInterface;
import vm.search.AlgorithmEvaluator;
import vm.search.algorithm.SearchingAlgorithm;
import vm.search.algorithm.impl.GRAPPLEPartitionsCandSetIdentifier;

/**
 *
 * @author Vlada
 */
public class EvaluateQuerySetByIndexAndSimpleReranking {

    public static void main(String[] args) {
        int k = 100;
        int kCandSetMaxSize = 50000;
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.DeCAFDataset()
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(),
//            new FSDatasetInstanceSingularizator.MPEG7dataset(),
//            new FSDatasetInstanceSingularizator.SIFTdataset(),
        //            new FSDatasetInstanceSingularizator.LAION_100k_Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_300k_Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset()
        };

        for (Dataset dataset : datasets) {
            SearchingAlgorithm alg = getGRAPPLEAlgorithm(dataset);
            evaluate(alg, dataset, k, kCandSetMaxSize);
        }
    }

    private static void evaluate(SearchingAlgorithm alg, Dataset dataset, int k, Integer kCandSetMaxSize) {
        FSRecallOfCandidateSetsStorageImpl statsStorage = new FSRecallOfCandidateSetsStorageImpl(dataset.getDatasetName(), dataset.getQuerySetName(), k, dataset.getDatasetName(), dataset.getQuerySetName(), alg.getResultName(), null);
        FSNearestNeighboursStorageImpl resultsStorage = new FSNearestNeighboursStorageImpl();
        AlgorithmEvaluator evaluator = new AlgorithmEvaluator(alg, statsStorage, resultsStorage, statsStorage, statsStorage);
        evaluator.evaluate(dataset, dataset.getQueryObjects(), k, kCandSetMaxSize, alg.getResultName());
    }

    private static SearchingAlgorithm getGRAPPLEAlgorithm(Dataset dataset) {
        StorageDatasetPartitionsInterface storage = new FSGRAPPLEPartitioningStorage();
        GRAPPLEPartitionsCandSetIdentifier ret = new GRAPPLEPartitionsCandSetIdentifier(dataset, storage, 256);
        return ret;
    }
}

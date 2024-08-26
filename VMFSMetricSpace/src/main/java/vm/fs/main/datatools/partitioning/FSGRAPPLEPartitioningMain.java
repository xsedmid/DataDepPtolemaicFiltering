/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.datatools.partitioning;

import java.util.List;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.partitioning.FSGRAPPLEPartitioningStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.datasetPartitioning.AbstractDatasetPartitioning;
import vm.metricSpace.datasetPartitioning.impl.GRAPPLEPartitioning;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentGeneralisedPtolemaicFiltering;

/**
 *
 * @author Vlada
 */
public class FSGRAPPLEPartitioningMain {

    public static void main(String[] args) {
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
//            new FSDatasetInstanceSingularizator.SIFTdataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(true)
//            new FSDatasetInstanceSingularizator.MPEG7dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_100k_Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_300k_Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset()
        };
        for (Dataset dataset : datasets) {
            run(dataset);
            System.gc();
        }
    }

    private static void run(Dataset dataset) {
        int pivotCount = 256;
        List<Object> pivots = dataset.getPivots(pivotCount);
        DataDependentGeneralisedPtolemaicFiltering filter = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstance(pivotCount + "_pivots", dataset, pivotCount);
        AbstractDatasetPartitioning partitioning = new GRAPPLEPartitioning(filter, dataset.getMetricSpace(), dataset.getDistanceFunction(), pivots);
        FSGRAPPLEPartitioningStorage storage = new FSGRAPPLEPartitioningStorage();
        partitioning.partitionObjects(dataset.getMetricObjectsFromDataset(), dataset.getDatasetName(), storage, pivotCount);
    }
}

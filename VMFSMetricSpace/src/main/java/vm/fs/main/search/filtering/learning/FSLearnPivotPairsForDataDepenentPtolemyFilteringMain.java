/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.search.filtering.learning;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static vm.fs.main.search.filtering.learning.FSLearnCoefsForDataDependentPtolemyFilteringMain.SAMPLE_QUERY_SET_SIZE;
import static vm.fs.main.search.filtering.learning.FSLearnCoefsForDataDependentPtolemyFilteringMain.SAMPLE_SET_SIZE;
import vm.fs.store.auxiliaryForDistBounding.FSDataDependentPtolemyInequalityPivotPairsStorageImpl;
import vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.DistanceFunctionInterface;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentGeneralisedPtolemaicFiltering;
import vm.metricSpace.distance.bounding.twopivots.learning.LearningPivotPairsForPtolemyInequalityWithLimitedAngles;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedPairsOfDistancesStorage;

/**
 *
 * @author au734419
 */
@Deprecated
public class FSLearnPivotPairsForDataDepenentPtolemyFilteringMain {

    public static void main(String[] args) {
        Dataset[] datasets = new Dataset[]{
//            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
//            new FSDatasetInstanceSingularizator.DeCAFDataset(),
//            new FSDatasetInstanceSingularizator.SIFTdataset(),
//            new FSDatasetInstanceSingularizator.MPEG7dataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(true),
//            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(true),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_64Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_128Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_192Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_256Dataset()
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    private static void run(Dataset dataset) {
        AbstractMetricSpace metricSpace = dataset.getMetricSpace();
        DistanceFunctionInterface df = dataset.getDistanceFunction();
        List<Object> pivots = dataset.getPivots(dataset.getRecommendedNumberOfPivotsForFiltering());

        DataDependentGeneralisedPtolemaicFiltering filter = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstance(null, dataset, pivots.size());

        List<Object> sampleObjectsAndQueries = dataset.getSampleOfDataset(SAMPLE_SET_SIZE + SAMPLE_QUERY_SET_SIZE);
        FSDataDependentPtolemyInequalityPivotPairsStorageImpl storage = new FSDataDependentPtolemyInequalityPivotPairsStorageImpl();
        LearningPivotPairsForPtolemyInequalityWithLimitedAngles learning = new LearningPivotPairsForPtolemyInequalityWithLimitedAngles(metricSpace, df, pivots, sampleObjectsAndQueries, SAMPLE_SET_SIZE, SAMPLE_QUERY_SET_SIZE, AbstractPrecomputedPairsOfDistancesStorage.IMPLICIT_K, filter, dataset.getDatasetName(), storage);
        try {
            learning.execute();
        } catch (InterruptedException ex) {
            Logger.getLogger(FSLearnPivotPairsForDataDepenentPtolemyFilteringMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

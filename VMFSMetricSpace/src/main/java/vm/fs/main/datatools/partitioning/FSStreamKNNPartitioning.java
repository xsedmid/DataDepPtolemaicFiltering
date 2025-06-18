package vm.fs.main.datatools.partitioning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.fs.dataset.FSDatasetInstances;
import vm.fs.store.auxiliaryForDistBounding.FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.auxiliaryForDistBounding.FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl;
import vm.fs.store.partitioning.FSStorageDatasetPartitionsInterface;
import vm.fs.store.partitioning.FSVoronoiPartitioningStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.datasetPartitioning.impl.Stream1NNClassifierWithFilter;
import vm.metricSpace.distance.bounding.BoundsOnDistanceEstimation;
import vm.metricSpace.distance.bounding.onepivot.AbstractOnePivotFilter;
import vm.metricSpace.distance.bounding.onepivot.impl.TriangleInequality;
import vm.metricSpace.distance.bounding.twopivots.AbstractTwoPivotsFilter;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentPtolemaicFilteringForStreamKNNClassifier;
import vm.metricSpace.distance.bounding.twopivots.impl.FourPointBasedFiltering;
import vm.metricSpace.distance.bounding.twopivots.impl.PtolemaicFilteringForStreamKNNClassifier;

/**
 *
 * @author au734419
 */
public class FSStreamKNNPartitioning {

    public static void main(String[] args) {
        boolean publicQueries = false;
        Dataset[] datasets = new Dataset[]{
//            new FSDatasetInstanceSingularizator.SIFTdataset(),
//            new FSDatasetInstanceSingularizator.RandomDataset15Uniform(),
//            new FSDatasetInstanceSingularizator.DeCAFDataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
            new FSDatasetInstances.LAION_10M_Dataset_Euclid(publicQueries),
            new FSDatasetInstances.LAION_10M_Dataset(publicQueries)
        //                        new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
        //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset_Euclid(publicQueries),
        //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries)
        };
        int clusterCount = 1000;

        for (Dataset dataset : datasets) {
            int pivotCountForFilter = dataset.getRecommendedNumberOfPivotsForFiltering();
            run(dataset, pivotCountForFilter, clusterCount);
        }
    }

    public static void run(Dataset dataset, int pivotCountForFilter, int clusterCount) {
        if (pivotCountForFilter > clusterCount) {
            throw new IllegalArgumentException("The number of filterts for the filtering cannot be higher than the cluster count" + pivotCountForFilter + ", " + clusterCount);
        }
        List<Object> centroids = dataset.getPivots(clusterCount);
        List<Object> pivots = centroids.subList(0, pivotCountForFilter);
        if (centroids.size() != clusterCount || pivots.size() != pivotCountForFilter) {
            throw new RuntimeException("Not enough number of object: " + centroids.size() + ", " + clusterCount + "; " + pivots.size() + ", " + pivotCountForFilter);
        }
        String resultSetPrefix = pivotCountForFilter + "pivotsFilt_" + clusterCount + "clusters";
        BoundsOnDistanceEstimation[] filters = initTestedFilters(resultSetPrefix, pivots, centroids, dataset);
        for (BoundsOnDistanceEstimation filter : filters) {
            Stream1NNClassifierWithFilter classifier = new Stream1NNClassifierWithFilter<>(
                    dataset.getMetricSpace(),
                    dataset.getDistanceFunction(),
                    pivotCountForFilter,
                    centroids,
                    filter);
            FSVoronoiPartitioningStorage storage = new FSVoronoiPartitioningStorage();
            partition(dataset, classifier, centroids.size(), storage);
            System.gc();
        }
    }

    private static Map<Comparable, List<Comparable>> partition(Dataset dataset, Stream1NNClassifierWithFilter partitioning, int pivotCount, FSStorageDatasetPartitionsInterface storage) {
        Iterator it = dataset.getMetricObjectsFromDataset();
        Map ret = partitioning.partitionObjects(it, dataset.getDatasetName(), storage, pivotCount);
        try {
            String path = storage.getFile(dataset.getDatasetName(), null, pivotCount, true).getAbsolutePath();
            path += ".log.csv";
            PrintStream tmp = System.err;
            System.setErr(new PrintStream(new FileOutputStream(path, true)));
            System.err.print(vm.javatools.Tools.getCurrDateAndTime());
            System.err.print(";");
            System.err.print(partitioning.getName());
            System.err.print(";Time;");
            System.err.print(partitioning.getLastTimeOfPartitioning());
            System.err.print(";Dist comps;");
            System.err.print(partitioning.getDcOfPartitioning());
            System.err.print(";LB checked;");
            System.err.print(partitioning.getLastAdditionalStats());
            System.err.println();
            System.err.flush();
            System.setErr(tmp);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FSPartitioningMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private static BoundsOnDistanceEstimation[] initTestedFilters(String resultSetPrefix, List pivots, List centroids, Dataset dataset) {
        int pivotCount = pivots.size();
        List pivotsData = dataset.getMetricSpace().getDataOfMetricObjects(pivots);
        List centroidsData = dataset.getMetricSpace().getDataOfMetricObjects(centroids);
        AbstractOnePivotFilter metricFiltering = new TriangleInequality(resultSetPrefix);
        AbstractOnePivotFilter dataDependentMetricFiltering = FSTriangleInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstanceTriangleInequalityWithLimitedAngles(resultSetPrefix, pivotCount, dataset);
        AbstractTwoPivotsFilter fourPointPropertyBased = new FourPointBasedFiltering(resultSetPrefix);

        PtolemaicFilteringForStreamKNNClassifier ptolemaicFilteringRandomPivots = new PtolemaicFilteringForStreamKNNClassifier(resultSetPrefix, pivotsData, centroidsData, dataset.getDistanceFunction(), false);
        PtolemaicFilteringForStreamKNNClassifier ptolemaicFiltering = new PtolemaicFilteringForStreamKNNClassifier(resultSetPrefix, pivotsData, centroidsData, dataset.getDistanceFunction(), true);
        DataDependentPtolemaicFilteringForStreamKNNClassifier dataDependentPtolemaicFilteringRandomPivots = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstanceForVoronoiPartitioning(resultSetPrefix, dataset, pivotCount, centroidsData.size(), false);
        DataDependentPtolemaicFilteringForStreamKNNClassifier dataDependentPtolemaicFiltering = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstanceForVoronoiPartitioning(resultSetPrefix, dataset, pivotCount, centroidsData.size(), true);
        return new BoundsOnDistanceEstimation[]{
            null,
            metricFiltering,
            dataDependentMetricFiltering,
            ptolemaicFilteringRandomPivots,
            ptolemaicFiltering,
            fourPointPropertyBased,
            dataDependentPtolemaicFilteringRandomPivots,
            dataDependentPtolemaicFiltering
        };
    }

}

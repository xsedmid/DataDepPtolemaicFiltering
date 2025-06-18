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
import vm.fs.store.partitioning.FSGRAPPLEPartitioningStorage;
import vm.fs.store.partitioning.FSStorageDatasetPartitionsInterface;
import vm.fs.store.partitioning.FSVoronoiPartitioningStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.datasetPartitioning.AbstractDatasetPartitioning;
import vm.metricSpace.datasetPartitioning.impl.GRAPPLEPartitioning;
import vm.metricSpace.datasetPartitioning.impl.VoronoiPartitioningWithoutFilter;
import vm.metricSpace.distance.bounding.BoundsOnDistanceEstimation;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentPtolemaicFiltering;

/**
 *
 * @author Vlada
 */
public class FSPartitioningMain {

    public static void main(String[] args) {
        boolean publicQueries = false;
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstances.SIFTdataset(),
            new FSDatasetInstances.RandomDataset15Uniform(),
//            new FSDatasetInstanceSingularizator.DeCAFDataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset_Euclid(publicQueries),
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries),
//            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_100M_Dataset_Euclid(publicQueries),
//            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries)
        };
        int pivotCount = 256;

        for (Dataset dataset : datasets) {
            runVoronoiPartitioning(dataset, pivotCount);
        }
    }

    public static void runVoronoiPartitioning(Dataset dataset, int pivotCount) {
        List<Object> pivots = dataset.getPivots(pivotCount);
        VoronoiPartitioningWithoutFilter vp = new VoronoiPartitioningWithoutFilter(dataset.getMetricSpace(), dataset.getDistanceFunction(), pivots);
        FSVoronoiPartitioningStorage storage = new FSVoronoiPartitioningStorage();
        partition(dataset, vp, pivotCount, storage);
        System.gc();
//        }
    }

    private static void runGRAPPLEPartitioning(Dataset dataset, BoundsOnDistanceEstimation filter, int pivotCount) {
        List<Object> pivots = dataset.getPivots(pivotCount);
        filter = FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.getLearnedInstance(pivotCount + "_pivots", dataset, pivotCount);
        AbstractDatasetPartitioning partitioning = new GRAPPLEPartitioning((DataDependentPtolemaicFiltering) filter, dataset.getMetricSpace(), dataset.getDistanceFunction(), pivots);
        FSGRAPPLEPartitioningStorage storage = new FSGRAPPLEPartitioningStorage();
        partition(dataset, partitioning, pivotCount, storage);
        System.gc();
    }

    private static Map<Comparable, List<Comparable>> partition(Dataset dataset, AbstractDatasetPartitioning partitioning, int pivotCount, FSStorageDatasetPartitionsInterface storage) {
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
            System.err.print(";");
            System.err.print(partitioning.getLastAdditionalStats());
            System.err.println();
            System.err.flush();
            System.setErr(tmp);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FSPartitioningMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

}

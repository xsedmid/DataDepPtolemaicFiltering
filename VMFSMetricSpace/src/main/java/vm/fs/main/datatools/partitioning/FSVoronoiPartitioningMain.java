package vm.fs.main.datatools.partitioning;

import java.util.List;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.partitioning.FSVoronoiPartitioningStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.datasetPartitioning.impl.VoronoiPartitioning;

/**
 *
 * @author Vlada
 */
public class FSVoronoiPartitioningMain {

    public static void main(String[] args) {
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            new FSDatasetInstanceSingularizator.MPEG7dataset(),
            new FSDatasetInstanceSingularizator.SIFTdataset(),
//            new FSDatasetInstanceSingularizator.LAION_100k_Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_300k_Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_100M_Dataset()
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    private static void run(Dataset dataset) {
        int pivotCount = 256;
        List<Object> pivots = dataset.getPivots(pivotCount);
        VoronoiPartitioning vp = new VoronoiPartitioning(dataset.getMetricSpace(), dataset.getDistanceFunction(), pivots);
        FSVoronoiPartitioningStorage storage = new FSVoronoiPartitioningStorage();
        vp.partitionObjects(dataset.getMetricObjectsFromDataset(), dataset.getDatasetName(), storage, pivotCount);
    }

}

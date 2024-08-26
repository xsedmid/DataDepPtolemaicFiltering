/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.datatools.storage;

import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.metricSpace.AbstractMetricSpacesStorage;
import vm.metricSpace.Dataset;

/**
 *
 * @author Vlada
 */
public class FSCheckDatasetFilesConsistency {

    public static void main(String[] args) {
        boolean publicQueries = true;
        Dataset[] datasets = new Dataset[]{
            //            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF20M_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
            //            new FSDatasetInstanceSingularizator.SIFTdataset(),
            //            new FSDatasetInstanceSingularizator.MPEG7dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA8Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA10Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA12Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA16Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA24Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA32Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA46Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA68Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA128Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA670Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_PCA1540Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_256Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_192Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_128Dataset(),
            //            new FSDatasetInstanceSingularizator.DeCAF_GHP_50_64Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_1M_SampleDataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100k_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_300k_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100k_PCA32Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_300k_PCA32Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_PCA32Dataset(),
            new FSDatasetInstanceSingularizator.LAION_30M_PCA32Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100M_PCA32Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100k_PCA96Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_300k_PCA96Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_PCA96Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_30M_PCA96Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100M_PCA96Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_30M_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_30M_PCA256Prefixes24Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Prefixes24Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Prefixes24Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Prefixes32Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_100k_GHP_50_192Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_300k_GHP_50_192Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_192Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_192Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_192Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100k_GHP_50_256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_300k_GHP_50_256Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_256Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_256Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_256Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100k_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_300k_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_1024Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_384Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_1024Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_384Dataset(publicQueries),
            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_1024Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_100k_GHP_50_512Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_300k_GHP_50_512Dataset(),
            //            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(publicQueries),
            //            new FSDatasetInstanceSingularizator.LAION_30M_GHP_50_512Dataset(publicQueries),
            new FSDatasetInstanceSingularizator.LAION_100M_GHP_50_512Dataset(publicQueries)
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    private static void run(Dataset dataset) {
        try {
            dataset.updateDatasetSize();
        } catch (Exception e) {
            System.out.println("Error in dataset " + dataset.getDatasetName());
        }
        try {
            dataset.getPivots(-1);
        } catch (Exception e) {
            System.out.println("Error in pivot set " + dataset.getPivotSetName());
        }
        try {
            dataset.getQueryObjects();
        } catch (Exception e) {
            System.out.println("Error in query set " + dataset.getPivotSetName());
        }

    }
}

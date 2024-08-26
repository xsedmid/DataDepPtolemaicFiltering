/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.datatools;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.Tools;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.distance.DistanceFunctionInterface;

/**
 *
 * @author Vlada
 */
public class PrintiiDimOfDatasetMain {

    public static void main(String[] args) {
        boolean publicQueries = true;
        Dataset[] datasets = new Dataset[]{
            //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries),
//            new FSDatasetInstanceSingularizator.DeCAFDataset(),
//            new FSDatasetInstanceSingularizator.DeCAF100M_Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_10M_PCA256Dataset(),
//            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(publicQueries),
//            new FSDatasetInstanceSingularizator.RandomDataset10Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset15Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset25Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset30Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset35Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset40Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset50Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset60Uniform(),
////            new FSDatasetInstanceSingularizator.RandomDataset70Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset80Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset90Uniform(),
//            new FSDatasetInstanceSingularizator.RandomDataset100Uniform(),
//            new FSDatasetInstanceSingularizator.DeCAF100M_Dataset()
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }

    }

    private static void run(Dataset dataset) {
        List sampleOfDataset = dataset.getSampleOfDataset(PrintAndPlotDDOfDatasetMain.IMPLICIT_OBJ_COUNT);
        AbstractMetricSpace metricSpace = dataset.getMetricSpace();
        DistanceFunctionInterface df = dataset.getDistanceFunction();
        double[] distances = new double[PrintAndPlotDDOfDatasetMain.IMPLICIT_DIST_COUNT];
        for (int i = 0; i < distances.length; i++) {
            Object o1 = metricSpace.getDataOfMetricObject(Tools.randomObject(sampleOfDataset));
            Object o2 = metricSpace.getDataOfMetricObject(Tools.randomObject(sampleOfDataset));
            distances[i] = df.getDistance(o1, o2);
            if (i % 100000 == 0) {
                Logger.getLogger(PrintiiDimOfDatasetMain.class.getName()).log(Level.INFO, "Evaluated {0} distances out of {1}", new Object[]{i, distances.length});
            }
        }
        double iDim = vm.math.Tools.getIDim(distances, true);
        System.out.println("");
        System.out.println("iDim: of dataset " + dataset.getDatasetName() + " is : " + iDim);
    }
}

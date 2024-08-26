/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.precomputeDistances.pivotPermutations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import vm.datatools.Tools;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.fs.store.precomputedDists.permutations.FSPrecomputedPivotPermutationsLoaderImpl;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;

/**
 *
 * @author au734419
 */
public class FSEvalAndStorePivotPermutationsMain {

    public static void main(String[] args) {
        boolean publicQueries = true;
        int pivotCount = 256;
        Dataset[] datasets = new Dataset[]{
            new FSDatasetInstanceSingularizator.DeCAFDataset(),
            new FSDatasetInstanceSingularizator.SIFTdataset(),
            new FSDatasetInstanceSingularizator.MPEG7dataset(),
            new FSDatasetInstanceSingularizator.RandomDataset20Uniform(),
            new FSDatasetInstanceSingularizator.LAION_10M_Dataset(publicQueries),
            new FSDatasetInstanceSingularizator.LAION_10M_GHP_50_512Dataset(publicQueries)
        };
        for (Dataset dataset : datasets) {
            run(dataset, pivotCount);
            System.gc();
        }

    }

    private static void run(Dataset dataset, int pivotCount) {
        FSPrecomputedDistancesMatrixLoaderImpl loader = new FSPrecomputedDistancesMatrixLoaderImpl();
        float[][] distsToPivots = loader.loadPrecomPivotsToObjectsDists(dataset, pivotCount);
        Map<Comparable, Integer> rowHeaders = loader.getRowHeaders();
        AbstractMetricSpace metricSpace = dataset.getMetricSpace();
        List pivots = dataset.getPivots(pivotCount);
        String[] pivotIDs = checkPivots(metricSpace, loader.getColumnHeaders(), pivots);
        Iterator it = dataset.getMetricObjectsFromDataset();
        SortedSet<AbstractMap.SimpleEntry<Integer, Float>> pivotPermutation = new TreeSet<>(new Tools.MapByFloatValueComparator<>());

        String output = new FSPrecomputedPivotPermutationsLoaderImpl().deriveFileForDatasetAndPivots(dataset.getDatasetName(), dataset.getPivotSetName(), pivots.size(), true).getAbsolutePath();
        GZIPOutputStream outputStream = null;
        try {
            outputStream = new GZIPOutputStream(new FileOutputStream(output), true);
            outputStream.write(';');
            for (int i = 0; i < pivots.size(); i++) {
                String pId = pivotIDs[i];
                outputStream.write(pId.getBytes());
                outputStream.write(';');
            }
            outputStream.write('\n');
            for (int i = 0; it.hasNext(); i++) {
                pivotPermutation.clear();
                Object o = it.next();
                String oId = metricSpace.getIDOfMetricObject(o).toString();
                outputStream.write(oId.getBytes());
                outputStream.write(';');
                Integer idx = rowHeaders.get(oId);
                if (idx == null) {
                    Logger.getLogger(FSEvalAndStorePivotPermutationsMain.class.getName()).log(Level.INFO, "Weird object with unknown ID. Do you have precomputed distances for the dataset? ID is: {0}", oId);
                    continue;
                }
                float[] dists = distsToPivots[idx];
                for (int pIdx = 0; pIdx < dists.length; pIdx++) {
                    float dist = dists[pIdx];
                    pivotPermutation.add(new AbstractMap.SimpleEntry<>(pIdx, dist));
                }
                StringBuilder sb = new StringBuilder();
                for (AbstractMap.SimpleEntry<Integer, Float> entry : pivotPermutation) {
                    sb.append(entry.getKey()).append(";");
                }
                String s = sb.toString();
                s = s.substring(0, s.length() - 1);
                outputStream.write(s.getBytes());
                outputStream.write('\n');
                if (i % 50000 == 0) {
                    Logger.getLogger(FSEvalAndStorePivotPermutationsMain.class.getName()).log(Level.INFO, "Put down {0} permutations", i);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FSEvalAndStorePivotPermutationsMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(FSEvalAndStorePivotPermutationsMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String[] checkPivots(AbstractMetricSpace metricSpace, Map<Comparable, Integer> columnHeaders, List pivots) {
        String[] ret = new String[pivots.size()];
        for (int i = 0; i < pivots.size(); i++) {
            Object pId = metricSpace.getIDOfMetricObject(pivots.get(i));
            Integer idx = columnHeaders.get(pId);
            if (idx == null || i != idx) {
                throw new IllegalArgumentException("Wrong order of pivots: " + i + ", " + idx);
            }
            ret[i] = pId.toString();
        }
        return ret;
    }

}

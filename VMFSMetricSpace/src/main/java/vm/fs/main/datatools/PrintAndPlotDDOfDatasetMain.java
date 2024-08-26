package vm.fs.main.datatools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.JFreeChart;
import vm.fs.FSGlobal;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.math.Tools;
import vm.metricSpace.ToolsMetricDomain;
import vm.metricSpace.Dataset;
import vm.plot.impl.XYLinesPlotter;

/**
 *
 * @author xmic
 */
public class PrintAndPlotDDOfDatasetMain {

    public static final int IMPLICIT_OBJ_COUNT = 1000 * 1000;//1,000,000
    public static final int IMPLICIT_DIST_COUNT = 1000 * 10000;//10,000,000

    public static void main(String[] args) {
        Dataset[] datasets = {
            //            new FSDatasetInstanceSingularizator.LAION_100M_Dataset(true),
            new FSDatasetInstanceSingularizator.DeCAFDataset(),
//            new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
//            new FSDatasetInstanceSingularizator.DeCAF100M_Dataset()
        };
        for (Dataset dataset : datasets) {
            run(dataset);
        }
    }

    public static void run(Dataset dataset) {
        String datasetName = dataset.getDatasetName();
//      getHistogramsForRandomPairs
        File f = getFileForDistDensity(datasetName, IMPLICIT_OBJ_COUNT, IMPLICIT_DIST_COUNT, false);
        SortedMap<Float, Float> ddRandomSample;
        if (f.exists()) {
            ddRandomSample = vm.datatools.Tools.parseCsvMapFloats(f.getAbsolutePath());
        } else {
            ddRandomSample = ToolsMetricDomain.createDistanceDensityPlot(dataset, IMPLICIT_OBJ_COUNT, IMPLICIT_DIST_COUNT, null);
        }
//      print
        Map<Float, Float> mapOfValues = printDD(f, ddRandomSample);
        createPlot(f, mapOfValues);
    }

    private static Map<Float, Float> printDD(File f, SortedMap<Float, Float> histogram) {
        PrintStream ps = null;
        Map<Float, Float> ret = new TreeMap<>();
        try {
            if (f.exists()) {
                ps = System.out;
            } else {
                ps = new PrintStream(f);
            }
            ps.println("Distance;Density of random sample");
            float lastDist = 0;
            float distInterval = ToolsMetricDomain.computeBasicDistInterval(histogram.lastKey());
            for (float dist : histogram.keySet()) {
                while (dist - lastDist > distInterval * 1.1d) {
                    lastDist += distInterval;
                    lastDist = Tools.round(lastDist, distInterval, false);
                    ps.println(lastDist + ";" + 0);
                    ret.put(lastDist, 0f);
                }
                Float value = histogram.get(dist);
                ps.println(dist + ";" + value);
                ret.put(dist, value);
                lastDist = dist;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrintAndPlotDDOfDatasetMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ps.flush();
            ps.close();
        }
        return ret;
    }

    public static boolean existsForDataset(Dataset dataset) {
        File f = getFileForDistDensity(dataset.getDatasetName(), IMPLICIT_OBJ_COUNT, IMPLICIT_DIST_COUNT, false);
        return f.exists();
    }

    private static File getFileForDistDensity(String datasetName, int objCount, int distCount, boolean willBeDeleted) {
        String fileName = datasetName + "_o" + objCount + "_d" + distCount + ".csv";
        File ret = new File(FSGlobal.DIST_DISTRIBUTION_PLOTS_FOLDER, fileName);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

    private static void createPlot(File f, Map<Float, Float> mapOfValues) {
        XYLinesPlotter plotter = new XYLinesPlotter();
        float[] traceXValues = new float[mapOfValues.size()];
        float[] traceYValues = new float[mapOfValues.size()];
        Iterator<Map.Entry<Float, Float>> it = mapOfValues.entrySet().iterator();
        for (int i = 0; it.hasNext(); i++) {
            Map.Entry<Float, Float> entry = it.next();
            traceXValues[i] = entry.getKey();
            traceYValues[i] = entry.getValue();
        }
        plotter.setIncludeZeroForXAxis(true);
        JFreeChart plot = plotter.createPlot("", "Distance", "", "", null, traceXValues, traceYValues);
        String path = f.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("."));
        plotter.storePlotPDF(path, plot);
        plotter.setLogY(true);
        plot = plotter.createPlot("", "Distance", "occurences", "", null, traceXValues, traceYValues);
        path += "_log";
        plotter.storePlotPDF(path, plot);
    }

}

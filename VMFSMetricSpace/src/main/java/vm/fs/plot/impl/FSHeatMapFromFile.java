/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl;

import java.io.File;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import vm.fs.FSGlobal;
import vm.fs.store.precomputedDists.FSPrecomputedDistancesMatrixLoaderImpl;
import vm.plot.impl.HeatMapPlotter;

/**
 *
 * @author Vlada
 */
public class FSHeatMapFromFile {

    public static void main(String[] args) {
        String filePath = "h:\\Similarity_search\\Trials\\Skittle_time_estimations_2024_08_19.csv";
        run(filePath);
    }

    public static void run(String filePath, int legendCount) {
        File file = new File(filePath);

        File resultFile = new File(FSGlobal.FOLDER_PLOTS, "HeatMaps");
        resultFile = new File(resultFile, file.getName());
        resultFile = FSGlobal.checkFileExistence(resultFile, true);

        FSPrecomputedDistancesMatrixLoaderImpl pd = new FSPrecomputedDistancesMatrixLoaderImpl();
        float[][] values = pd.loadPrecomPivotsToObjectsDists(file, null, -1);
        Map<Comparable, Integer> columnHeaders = pd.getColumnHeaders();
        Map<Comparable, Integer> rowHeaders = pd.getRowHeaders();

        HeatMapPlotter plotter = new HeatMapPlotter();
        String xLabel = "Threshold on # LB per avg. obj";
        String yLabel = "Checked obj until attempt for the STRAIN";
        String traceName = "Estimated time per q (ms)";
        JFreeChart createPlot = plotter.createPlot(file.getName(), xLabel, yLabel, traceName, values, columnHeaders, rowHeaders);
        if (legendCount > 0) {
            plotter.setLegendCount(legendCount);
        }
        plotter.storePlotPNG(resultFile.getAbsolutePath(), createPlot);
    }

    public static void run(String filePath) {
        run(filePath, -1);
    }
}

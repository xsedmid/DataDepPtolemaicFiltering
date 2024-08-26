/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import vm.datatools.DataTypeConvertor;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.plot.AbstractPlotter;
import vm.plot.impl.XYLinesPlotter;

/**
 *
 * @author au734419
 */
public class FSPlotXYDataInFolder {

    public static final String X_NAME = "xName";
    public static final String Y_NAME = "yName";
    public static final String X_VALUES = "xValues";
    public static final String PLOT_TITLE = "Plot title";
    public static final String TRACE = "Trace";

    public static void main(String[] args) {
        File folder = new File(FSGlobal.FOLDER_DATA_FOR_PLOTS);
        folder = FSGlobal.checkFileExistence(folder, false);
        folder.mkdirs();
        File[] files = folder.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".csv"));

        AbstractPlotter plotter = new XYLinesPlotter();

        for (File file : files) {
            plotFile(plotter, file);
        }
    }

    @SuppressWarnings("null")
    private static void plotFile(AbstractPlotter plotter, File file) {
        String path = file.getAbsolutePath();
        List<String[]> csv = Tools.parseCsvRowOriented(path, ";");
        String[] xAxisValues = null;
        String xName = null, yName = null;
        String plotTitle = null;

        List<float[]> yDataValues = new ArrayList<>();

        Map<String, Integer> tracesNumbers = new HashMap<>();
        for (String[] strings : csv) {
            if (strings.length == 0) {
                continue;
            }
            String rowName = Tools.removeQuotes(strings[0]);
            if (rowName.equals(X_NAME)) {
                xName = Tools.removeQuotes(strings[1]);
            }
            if (rowName.equals(Y_NAME)) {
                if (strings.length >= 2) {
                    yName = Tools.removeQuotes(strings[1]);
                } else {
                    yName = null;
                }
            }
            if (rowName.equals(X_VALUES)) {
                xAxisValues = new String[strings.length - 1];
                for (int i = 1; i < strings.length; i++) {
                    xAxisValues[i - 1] = strings[i];
                }
            }
            if (rowName.equals(TRACE)) {
                String traceName = Tools.removeQuotes(strings[1]);
                if (!tracesNumbers.containsKey(traceName)) {
                    int idx = tracesNumbers.size();
                    tracesNumbers.put(traceName, idx);
                    yDataValues.add(idx, new float[xAxisValues.length]);
                }
                int idx = tracesNumbers.get(traceName);
                float[] values = yDataValues.get(idx);
                for (int i = 0; i < values.length; i++) {
                    if (!Tools.isEmptyString(strings[i + 2])) {
                        values[i] = Float.parseFloat(strings[i + 2]);
                    }
                }
            }

            if (rowName.equals(PLOT_TITLE)) {
                if (plotTitle != null) {
                    String[] tracesNames = transformTracesNumbers(tracesNumbers);
                    plot(plotter, plotTitle, xName, yName, tracesNames, xAxisValues, yDataValues);
                }
                plotTitle = Tools.removeQuotes(strings[1]);
            }
        }
        if (plotTitle != null) {
            String[] tracesNames = transformTracesNumbers(tracesNumbers);
            plot(plotter, plotTitle, xName, yName, tracesNames, xAxisValues, yDataValues);
        }
    }

    private static void plot(AbstractPlotter plotter, String plotTitle, String xName, String yName, String[] tracesNames, String[] xAxisValues, List<float[]> yDataValues) {
        float[][] xPlotValues = transformXStringValues(xAxisValues, yDataValues.size());
        float[][] yPlotValues = DataTypeConvertor.listOfFloatsToMatrix(yDataValues);
        String fileName = plotTitle;
        if (yName == null) {
            yName = plotTitle;
            plotTitle = null;
        }
        JFreeChart plot = plotter.createPlot(plotTitle, xName, yName, tracesNames, xPlotValues, yPlotValues);
        File fileForPlot = getFileForPlot(FSGlobal.FOLDER_PLOTS, fileName);
        plotter.storePlotPDF(fileForPlot.getAbsolutePath(), plot);
    }

    private static float[][] transformXStringValues(String[] xAxisValues, int numberOfTraces) {
        float[] values = DataTypeConvertor.stringArrayToFloats(xAxisValues);
        float[][] ret = new float[numberOfTraces][values.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = values;
        }
        return ret;
    }

    private static String[] transformTracesNumbers(Map<String, Integer> tracesNumbers) {
        String[] ret = new String[tracesNumbers.size()];
        for (Map.Entry<String, Integer> entry : tracesNumbers.entrySet()) {
            ret[entry.getValue()] = entry.getKey();
        }
        return ret;
    }

    private static File getFileForPlot(String folder, String fileName) {
        File f = new File(folder, Tools.getDateYYYYMM() + "_" + fileName + ".svg");
        f = FSGlobal.checkFileExistence(f, true);
        return f;
    }
}

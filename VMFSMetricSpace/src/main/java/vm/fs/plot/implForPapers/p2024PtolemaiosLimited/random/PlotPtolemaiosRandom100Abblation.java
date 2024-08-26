/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.random;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotPlotter;

/**
 *
 * @author Vlada
 */
public class PlotPtolemaiosRandom100Abblation extends FSAbstractPlotterFromResults {

    public PlotPtolemaiosRandom100Abblation(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        int[] numbers = {8, 12, 16, 20, 24, 28, 32, 48, 64, 256};
        String[] ret = new String[numbers.length + 2];
        ret[0] = "Data-dep. Metric Filtering 256p_256LB";
        ret[ret.length - 1] = "Sequential Brute Force";
        for (int i = 0; i < numbers.length; i++) {
            ret[i + 1] = numbers[i] + "pivots_" + numbers[i] + "LB";
        }
        return ret;
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        int[] numbers = {8, 12, 16, 20, 24, 28, 32, 48, 64};
        String[] ret = new String[numbers.length + 3];
        ret[0] = "2024_03_256_pivots_30NN_seq_data-dependent_metric_filtering";
        ret[ret.length - 2] = "2024_03_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection";
        ret[ret.length - 1] = "ground_truth";
        for (int i = 0; i < numbers.length; i++) {
            ret[i + 1] = "2024_04_" + numbers[i] + "_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_" + numbers[i] + "LB";
        }
        return ret;
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "20D",
                "40D",
                "100D"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "random_20dim_uniform_1M__random_20dim_uniform_1M__30__random_20dim_uniform_1M__random_20dim_uniform_1M__.csv",
                "random_40dim_uniform_1M__random_40dim_uniform_1M__30__random_40dim_uniform_1M__random_40dim_uniform_1M__.csv",
                "random_100dim_uniform_1M__random_100dim_uniform_1M__30__random_100dim_uniform_1M__random_100dim_uniform_1M__.csv"
        );
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / 1000000;
    }

    @Override
    public String getXAxisLabel() {
        return null;
    }

    @Override
    public AbstractPlotter getPlotter() {
        return new BoxPlotPlotter();
    }

    @Override
    public String getResultName() {
        return "Filterings";
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2024_PTOLEMAIOS_LIMITED;
    }

    @Override
    protected String getYAxisNameForAdditionalParams() {
        return "# LBs checked per distance";
    }

    @Override
    protected AbstractPlotter.COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return null;
    }

}

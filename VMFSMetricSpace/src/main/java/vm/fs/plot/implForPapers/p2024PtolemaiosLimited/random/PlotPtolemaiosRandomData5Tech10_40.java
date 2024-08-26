/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.random;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.AbstractPlotter.COLOUR_NAMES;
import vm.plot.impl.BoxPlotPlotter;
import vm.search.algorithm.SearchingAlgorithm;

/**
 *
 * @author au734419
 */
public class PlotPtolemaiosRandomData5Tech10_40 extends FSAbstractPlotterFromResults {

    public static final Integer PIVOTS = 128;

    public PlotPtolemaiosRandomData5Tech10_40(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Triangle Ineq.",
                "Data-dep. Metric Filtering",
                "Four Point Prop.",
                "Ptolemaic Filtering",
                "Ptolemaic with Dyn. Pivots",
                "Data-dep. Ptolemaic Filering",
                "Sequential Brute Force"
        );
    }

    @Override
    protected COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return new COLOUR_NAMES[]{
            COLOUR_NAMES.C1_BLUE,
            COLOUR_NAMES.C2_RED,
            COLOUR_NAMES.C3_GREEN,
            COLOUR_NAMES.C4_ORANGE,
            COLOUR_NAMES.C6_BROWN,
            COLOUR_NAMES.C5_VIOLET,
            COLOUR_NAMES.CX_BLACK
        };
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_triangle_inequality",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_FourPointBasedFiltering",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_ptolemaios_" + PIVOTS + "LB_random_pivots",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_ptolemaios_" + PIVOTS + "LB",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_" + PIVOTS + "LB",
                "ground_truth"
        //                "2024_03_256_pivots_30NN_seq_triangle_inequality",
        //                "2024_03_256_pivots_30NN_seq_data-dependent_metric_filtering",
        //                "2024_03_256_pivots_30NN_seq_FourPointBasedFiltering",
        //                "2024_03_256_pivots_30NN_seq_ptolemaios",
        //                "2024_03_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection",
        //                "ground_truth"
        );
    }

    public int[] getDims() {
        return new int[]{10, 15, 20, 25, 30, 35, 40};
    }

    @Override
    public Object[] getDisplayedNamesOfGroupsThatMeansFiles() {
        int[] dims = getDims();
        Object[] ret = new Object[dims.length];
        for (int i = 0; i < dims.length; i++) {
            ret[i] = "Dimensionality " + dims[i];
        }
        return ret;
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        int[] dims = getDims();
        String[] ret = new String[dims.length];
        for (int i = 0; i < dims.length; i++) {
            ret[i] = dims[i] + "dim_uniform_1M__30";
        }
        return ret;
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
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / (1000000 - SearchingAlgorithm.K_IMPLICIT_FOR_QUERIES);
    }

    @Override
    protected String getYAxisNameForAdditionalParams() {
        return "# LBs checked per distance";
    }

}

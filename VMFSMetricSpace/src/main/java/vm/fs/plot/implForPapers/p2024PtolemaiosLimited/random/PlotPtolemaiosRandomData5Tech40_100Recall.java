/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.random;

import vm.plot.AbstractPlotter.COLOUR_NAMES;

/**
 *
 * @author au734419
 */
public class PlotPtolemaiosRandomData5Tech40_100Recall extends PlotPtolemaiosRandomData5Tech10_40 {

    public static final Integer PIVOTS = 128;

    public PlotPtolemaiosRandomData5Tech40_100Recall(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Data-dep. Metric Filtering",
                "Data-dep. Ptolemaic Filering"
        );
    }

    @Override
    protected COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return new COLOUR_NAMES[]{
            COLOUR_NAMES.C2_RED,
            COLOUR_NAMES.C5_VIOLET
        };
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_08_" + PIVOTS + "_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_" + PIVOTS + "LB"
        );
    }

    @Override
    public int[] getDims() {
        return new int[]{50, 60, 70, 80, 90, 100};
    }

    @Override
    public Object[] getDisplayedNamesOfGroupsThatMeansFiles() {
        int[] dims = getDims();
        Object[] ret = new Object[dims.length];
        for (int i = 0; i < dims.length; i++) {
            ret[i] = dims[i];
        }
        return ret;
    }

    @Override
    public String getXAxisLabel() {
        return "Dimensionality";
    }

}

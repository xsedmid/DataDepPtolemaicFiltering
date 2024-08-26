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

/**
 *
 * @author au734419
 */
public class PlotPtolemaiosRandomDataPivotSelectionForPtolemaios extends FSAbstractPlotterFromResults {

    public PlotPtolemaiosRandomDataPivotSelectionForPtolemaios(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public Object[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return array(
                20
        );
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Uniform Pivot Pairs",
                "Proposed Pairs Selection",
                "Data-dep. Ptolemaic Filering"
        );
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2024_04_256_pivots_30NN_seq_ptolemaios_randomPivots",
                "2024_03_256_pivots_30NN_seq_ptolemaios",
                "2024_03_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        Object[] dims = getDisplayedNamesOfGroupsThatMeansFiles();
        String[] ret = new String[dims.length];
        for (int i = 0; i < dims.length; i++) {
            ret[i] = dims[i].toString() + "dim";
        }
        return ret;
    }

    @Override
    public String getXAxisLabel() {
        return "20D";
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
        return firstValue / 1000000;
    }

    @Override
    protected String getYAxisNameForAdditionalParams() {
        return "# LBs checked per distance";
    }

    @Override
    protected AbstractPlotter.COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return new COLOUR_NAMES[]{COLOUR_NAMES.CX_BLACK,
            COLOUR_NAMES.C4_ORANGE,
            COLOUR_NAMES.C5_VIOLET
        };
    }
}

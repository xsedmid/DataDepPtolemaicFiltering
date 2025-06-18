/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.devel.p2024VLDBPtolemaiosLimited.random;

import vm.colour.StandardColours;
import vm.colour.StandardColours.COLOUR_NAME;
import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotXCategoriesPlotter;

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
        return new BoxPlotXCategoriesPlotter();
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
    protected StandardColours.COLOUR_NAME[] getVoluntaryColoursForTracesOrNull() {
        return new COLOUR_NAME[]{COLOUR_NAME.CX_BLACK,
            COLOUR_NAME.C4_ORANGE,
            COLOUR_NAME.C5_VIOLET
        };
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.used.p2024VLDBPtolemaiosLimited;

import vm.colour.StandardColours;
import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotXCategoriesPlotter;

/**
 *
 * @author Vlada
 */
public class PlotPtolemaiosTransformedClips5TechSTRAIN extends FSAbstractPlotterFromResults {

    public PlotPtolemaiosTransformedClips5TechSTRAIN(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Data-dep. Ptolemaic Filering",
                "Data-dep. Ptolemaic Filering STRAIN 17-150000Mem",
                "Data-dep. Ptolemaic Filering STRAIN 20-100000Mem",
                "Data-dep. Ptolemaic Filering STRAIN 25-50000Mem",
                "Data-dep. Ptolemaic Filering STRAIN 32-50000",
                "Data-dep. Ptolemaic Filering STRAIN 32-50000Mem",
                "Data-dep. Ptolemaic Filering STRAIN 32-100000",
                "Data-dep. Ptolemaic Filering STRAIN 32-100000Mem",
                "Data-dep. Ptolemaic Filering STRAIN 32-400000",
                "Data-dep. Ptolemaic Filering STRAIN 32-500000",
                "Sequential Brute Force"
        );
    }

    @Override
    protected StandardColours.COLOUR_NAME[] getVoluntaryColoursForTracesOrNull() {
        return new StandardColours.COLOUR_NAME[]{
            StandardColours.COLOUR_NAME.C1_BLUE,
            StandardColours.COLOUR_NAME.C2_RED,
            StandardColours.COLOUR_NAME.C3_GREEN,
            StandardColours.COLOUR_NAME.C4_ORANGE,
            StandardColours.COLOUR_NAME.C5_VIOLET,
            StandardColours.COLOUR_NAME.C6_BROWN,
            StandardColours.COLOUR_NAME.C7_PURPLE,
            StandardColours.COLOUR_NAME.C8_GREY,
            StandardColours.COLOUR_NAME.C9_LIME,
            StandardColours.COLOUR_NAME.C10_CYAN,
            StandardColours.COLOUR_NAME.CX_BLACK
        };
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_17perc_150000objMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_20perc_100000objMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_25perc_50000objMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_32perc_50000objIntMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_32perc_50000objMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_32perc_100000objIntMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_32perc_100000objMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_32perc_400000objIntMem",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB_32perc_500000objIntMem",
                "ground_truth"
        );
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "CLIP_10M_PCA256"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "laion2B-en-clip768v2-n=10M.h5_PCA256__laion2B-en-clip768v2-n=10M.h5_PCA256__30__laion2B-en-clip768v2-n=10M.h5_PCA256__laion2B-en-clip768v2-n=10M.h5_PCA256__"
        );
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / 10120191;
    }

    @Override
    public String getXAxisLabel() {
        return null;
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
    protected String getYAxisNameForAdditionalParams() {
        return "# LBs checked per distance";
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.impl.paper.devel.p2025ICDEPtolemaiosLimited;

import vm.colour.StandardColours;
import static vm.fs.plot.FSAbstractPlotterFromResults.strings;
import vm.fs.plot.FSPlotFolders;
import vm.fs.plot.impl.paper.used.p2024VLDBPtolemaiosLimited.PlotPtolemaiosClips10MPCA5Tech;
import vm.search.algorithm.SearchingAlgorithm;

/**
 *
 * @author Vlada
 */
public class PlotPtolemaiosClips30MPCA5Tech extends PlotPtolemaiosClips10MPCA5Tech {

    private static int pivotCount = 128;

    public PlotPtolemaiosClips30MPCA5Tech(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "CLIP_30M_PCA256"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "laion2B-en-clip768v2-n=30M.h5_PCA256__laion2B-en-clip768v2-n=100M.h5_PCA256__30__laion2B-en-clip768v2-n=30M.h5_PCA256__laion2B-en-clip768v2-n=100M.h5_PCA256__"
        );
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return firstValue / (30369256 - SearchingAlgorithm.K_IMPLICIT_FOR_QUERIES);
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
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2025_05_" + pivotCount + "_pivots_30NN_triangle_inequality",
                "2025_05_" + pivotCount + "_pivots_30NN_data-dependent_metric_filtering",
                "2025_05_" + pivotCount + "_pivots_30NN_FourPointBasedFiltering",
                "2025_05_" + pivotCount + "_pivots_30NN_ptolemaios_randomPivots_" + pivotCount + "LB_random_pivots",
                "2025_05_" + pivotCount + "_pivots_30NN_ptolemaios_" + pivotCount + "LB",
                "2025_05_" + pivotCount + "_pivots_30NN_data-dependent_ptolemaic_filtering_" + pivotCount + "LB",
                "ground_truth"
        );
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2025_PTOLEMAIOS_LIMITED;
    }

    @Override
    protected StandardColours.COLOUR_NAME[] getVoluntaryColoursForTracesOrNull() {
        return new StandardColours.COLOUR_NAME[]{
            StandardColours.COLOUR_NAME.C1_BLUE,
            StandardColours.COLOUR_NAME.C2_RED,
            StandardColours.COLOUR_NAME.C3_GREEN,
            StandardColours.COLOUR_NAME.C4_ORANGE,
            StandardColours.COLOUR_NAME.C6_BROWN,
            StandardColours.COLOUR_NAME.C5_VIOLET,
            StandardColours.COLOUR_NAME.CX_BLACK
        };
    }

}

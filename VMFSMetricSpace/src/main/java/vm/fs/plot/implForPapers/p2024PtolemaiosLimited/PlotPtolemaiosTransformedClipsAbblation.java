/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotPlotter;

/**
 *
 * @author Vlada
 */
public class PlotPtolemaiosTransformedClipsAbblation extends FSAbstractPlotterFromResults {

    public PlotPtolemaiosTransformedClipsAbblation(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "Data-dep. Metric Filtering 256p_256LB",
                "256p_256LB",
                "256p_64LB",
                "256p_32LB",
                "208p_208LB",
                "192p_192LB",
                "176p_176LB",
                "160p_160LB",
                "144p_144LB",
                "128p_128LB",
                "112p_112LB",
                "96p_96LB",
                "80p_80LB",
                "80p_64LB",
                "72p_72LB",
                "64p_64LB",
                "64p_32LB", 
                "40p_40LB",
                "Sequential Brute Force"
        );
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "2024_03_256_pivots_30NN_seq_data-dependent_metric_filtering",
                "2024_05_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_256LB",
                "2024_05_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_64LB",
                "2024_05_256_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_32LB",
                "2024_05_208_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_208LB",
                "2024_05_192_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_192LB",
                "2024_05_176_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_176LB",
                "2024_05_160_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_160LB",
                "2024_05_144_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_144LB",
                "2024_05_128_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_128LB",
                "2024_05_112_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_112LB",
                "2024_05_96_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_96LB",
                "2024_05_80_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_80LB",
                "2024_05_80_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_64LB",
                "2024_05_72_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_72LB",
                "2024_05_64_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_64LB",
                "2024_05_64_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_32LB",
                "2024_05_40_pivots_30NN_seq_data-dependent_generalised_ptolemaic_filtering_pivot_array_selection_40LB",
                "ground_truth"
        );
    }

    @Override
    public String[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return strings(
                "CLIP_10M_PCA256"
//                "CLIP_10M_GHP512"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "laion2B-en-clip768v2-n=10M.h5_PCA256__laion2B-en-clip768v2-n=10M.h5_PCA256__30__laion2B-en-clip768v2-n=10M.h5_PCA256__laion2B-en-clip768v2-n=10M.h5_PCA256__"
//                "laion2B-en-clip768v2-n=10M.h5_GHP_50_512__public-queries-10k-clip768v2.h5_GHP_50_512__30__laion2B-en-clip768v2-n=10M.h5_GHP_50_512__public-queries-10k-clip768v2.h5_GHP_50_512__"
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

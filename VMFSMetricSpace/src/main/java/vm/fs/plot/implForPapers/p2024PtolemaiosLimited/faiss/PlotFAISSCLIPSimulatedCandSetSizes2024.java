/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.faiss;

import vm.fs.main.queryResults.recallEvaluation.FSEvaluateRecallsOfApproximateDatasetMain;
import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotPlotter;

/**
 *
 * @author Vlada
 */
public class PlotFAISSCLIPSimulatedCandSetSizes2024 extends FSAbstractPlotterFromResults {

    private Integer[] kCands;

    public PlotFAISSCLIPSimulatedCandSetSizes2024(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000"
        );
    }

    @Override
    protected AbstractPlotter.COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return null;
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000"
        );
    }

    @Override
    public Object[] getDisplayedNamesOfGroupsThatMeansFiles() {
        if (kCands == null) {
            initCands();
        }
        String[] ret = new String[4 * kCands.length];
        for (int i = 0; i < kCands.length; i++) {
            ret[4 * i] = "nprobe16_" + kCands[i] + "Cands";
            ret[4 * i + 1] = "nprobe32_" + kCands[i] + "Cands";
            ret[4 * i + 2] = "nprobe64_" + kCands[i] + "Cands";
            ret[4 * i + 3] = "nprobe128_" + kCands[i] + "Cands";
        }
        return ret;
    }

    private void initCands() {
        kCands = FSEvaluateRecallsOfApproximateDatasetMain.kCands;
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        if (kCands == null) {
            initCands();
        }
        String[] ret = new String[4 * kCands.length];
        for (int i = 0; i < kCands.length; i++) {
            ret[4 * i] = "nprobe16____" + kCands[i] + "__";
            ret[4 * i + 1] = "nprobe32____" + kCands[i] + "__";
            ret[4 * i + 2] = "nprobe64____" + kCands[i] + "__";
            ret[4 * i + 3] = "nprobe128____" + kCands[i] + "__";
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
        return "FAISS";
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2024_PTOLEMAIOS_LIMITED_FAISS;
    }

    @Override
    protected Float transformAdditionalStatsForQueryToFloat(float firstValue) {
        return null;
    }

    @Override
    protected String getYAxisNameForAdditionalParams() {
        return null;
    }

}

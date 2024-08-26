/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.faiss;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.AbstractPlotter.COLOUR_NAMES;
import vm.plot.impl.BoxPlotPlotter;

/**
 *
 * @author Vlada
 */
public class PlotFAISSCLIPIndexes2024 extends FSAbstractPlotterFromResults {

    public PlotFAISSCLIPIndexes2024(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "faiss-100M_CLIP_PCA256-IVF-tr20000000-cc262144-qc1000-k100000",
                "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000",
                "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m64-nbits16-qc1000-k100000"
        );
    }

    @Override
    protected COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return null;
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "faiss-100M_CLIP_PCA256-IVF-tr20000000-cc262144-qc1000-k100000",
                "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000",
                "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m64-nbits16-qc1000-k100000"
        );
    }

    @Override
    public Object[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return array(
                "nprobe16",
                "nprobe32",
                "nprobe64",
                "nprobe128",
                "nprobe256",
                "nprobe512"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "nprobe16",
                "nprobe32",
                "nprobe64",
                "nprobe128",
                "nprobe256",
                "nprobe512"
        );
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

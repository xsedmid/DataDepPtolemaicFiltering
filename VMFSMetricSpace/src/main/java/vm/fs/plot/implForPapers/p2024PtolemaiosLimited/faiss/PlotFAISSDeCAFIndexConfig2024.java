/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.faiss;

import vm.fs.plot.FSAbstractPlotterFromResults;
import vm.fs.plot.FSPlotFolders;
import vm.plot.AbstractPlotter;
import vm.plot.impl.BoxPlotPlotter;

/**
 *
 * @author Vlada
 */
public class PlotFAISSDeCAFIndexConfig2024  extends FSAbstractPlotterFromResults {

    public PlotFAISSDeCAFIndexConfig2024(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getDisplayedNamesOfTracesThatMatchesFolders() {
        return strings(
                "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000",
                "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m64-nbits16-qc1000-k10000"
        );
    }

    @Override
    protected AbstractPlotter.COLOUR_NAMES[] getVoluntaryColoursForTracesOrNull() {
        return null;
    }

    @Override
    public String[] getFolderNamesForDisplayedTraces() {
        return strings(
                "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000",
                "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m64-nbits16-qc1000-k10000"
        );
    }

    @Override
    public Object[] getDisplayedNamesOfGroupsThatMeansFiles() {
        return array(
                "nprobe64",
                "nprobe128",
                "nprobe256",
                "nprobe384",
                "nprobe512",
                "nprobe768",
                "nprobe1024"
        );
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "nprobe64____.csv",
                "nprobe128____.csv",
                "nprobe256____.csv",
                "nprobe384____.csv",
                "nprobe512____.csv",
                "nprobe768____.csv",
                "nprobe1024____.csv"
        );
    }

    @Override
    public String getXAxisLabel() {
        return null;
    }

    @Override
    public AbstractPlotter getPlotter() {
        BoxPlotPlotter ret = new BoxPlotPlotter();
        ret.setEnforceInvolvingZeroToYAxis(true);
        return ret;
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

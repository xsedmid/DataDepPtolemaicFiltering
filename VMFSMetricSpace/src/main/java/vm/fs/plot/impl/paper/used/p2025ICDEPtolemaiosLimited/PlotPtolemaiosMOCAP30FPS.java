package vm.fs.plot.impl.paper.used.p2025ICDEPtolemaiosLimited;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import vm.fs.plot.FSPlotFolders;

/**
 *
 * @author xmic
 */
public class PlotPtolemaiosMOCAP30FPS extends PlotPtolemaiosMOCAP10FPS {

    public PlotPtolemaiosMOCAP30FPS(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public String[] getUniqueArtifactIdentifyingFileNameForDisplaydGroup() {
        return strings(
                "actions-single-subject-all-POS.data_selected.txt__actions-single-subject-all-POS.data_selected.txt__30__actions-single-subject-all-POS.data_selected.txt__actions-single-subject-all-POS.data_selected.txt__.csv"
        );
    }

    @Override
    public String getResultName() {
        return "Filterings " + pivotCount + "pivots_30FPS";
    }

    @Override
    public String getFolderForPlots() {
        return FSPlotFolders.Y2025_PTOLEMAIOS_LIMITED30FPS;
    }

}

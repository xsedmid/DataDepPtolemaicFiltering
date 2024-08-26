/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited;

import vm.fs.plot.FSAbstractPlotterFromResults;

/**
 *
 * @author Vlada
 */
public class FSPtolemaiosWithSTRAINMain {

    public static final Boolean PLOT_ONLY_PDF = true;

    public static final FSAbstractPlotterFromResults Y2024_PlotPtolemaiosTransformedClipsSTRAIN = new PlotPtolemaiosTransformedClips5TechSTRAIN(PLOT_ONLY_PDF);

    public static void main(String[] args) {
        Y2024_PlotPtolemaiosTransformedClipsSTRAIN.makePlots();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.plot.implForPapers.p2024PtolemaiosLimited.random;

/**
 *
 * @author au734419
 */
public class PlotPtolemaiosRandomData5Tech50_100 extends PlotPtolemaiosRandomData5Tech10_40 {

    public PlotPtolemaiosRandomData5Tech50_100(boolean plotOnlyPDF) {
        super(plotOnlyPDF);
    }

    @Override
    public int[] getDims() {
        return new int[]{50, 60, 70, 80, 90, 100};
    }

}

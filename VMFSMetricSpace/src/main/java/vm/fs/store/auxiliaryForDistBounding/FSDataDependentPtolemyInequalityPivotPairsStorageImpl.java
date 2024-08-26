/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.store.auxiliaryForDistBounding;

import java.io.File;
import vm.fs.FSGlobal;
import vm.fs.store.dataTransforms.FSGHPSketchesPivotPairsStorageImpl;

/**
 *
 * @author Vlada
 */
public class FSDataDependentPtolemyInequalityPivotPairsStorageImpl extends FSGHPSketchesPivotPairsStorageImpl {

    @Override
    protected File getFileForResults(String datasetName, boolean willBeDeleted) {
        File ret = new File(FSGlobal.PIVOT_PAIRS_FOR_DATA_DEPENDENT_PTOLEMAIC_FILTERING, datasetName + ".csv");
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

}

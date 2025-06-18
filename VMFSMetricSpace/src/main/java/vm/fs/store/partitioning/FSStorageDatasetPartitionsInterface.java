/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vm.fs.store.partitioning;

import java.io.File;
import vm.metricSpace.datasetPartitioning.StorageDatasetPartitionsInterface;

/**
 *
 * @author Vlada
 */
public abstract class FSStorageDatasetPartitionsInterface extends StorageDatasetPartitionsInterface {

    public abstract File getFile(String datasetName, String suffix, int pivotCount, boolean willBeDeleted);

}

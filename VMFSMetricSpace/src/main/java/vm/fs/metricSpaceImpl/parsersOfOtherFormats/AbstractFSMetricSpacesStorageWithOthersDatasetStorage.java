/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.metricSpaceImpl.parsersOfOtherFormats;

import java.io.BufferedReader;
import java.io.File;
import java.util.AbstractMap;
import java.util.Iterator;
import vm.fs.FSGlobal;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.metricSpace.data.toStringConvertors.MetricObjectDataToStringInterface;
import vm.metricSpace.distance.DistanceFunctionInterface;

/**
 *
 * @author Vlada
 * @param <T>
 */
public abstract class AbstractFSMetricSpacesStorageWithOthersDatasetStorage<T> extends FSMetricSpacesStorage<T> {

    public AbstractFSMetricSpacesStorageWithOthersDatasetStorage(DistanceFunctionInterface<T> df, MetricObjectDataToStringInterface<T> dataSerializator) {
        super(df, dataSerializator);
    }

    @Override
    protected File getFileForObjects(String folder, String datasetName, boolean willBeDeleted) {
        if (!folder.equals(FSGlobal.DATASET_FOLDER)) {
            return super.getFileForObjects(folder, datasetName, willBeDeleted);
        }
        File ret = getFileForDataset(datasetName);
        if (willBeDeleted && ret.exists()) {
            throw new IllegalArgumentException("Attempt to delete implicit dataset!");
        }
        return ret;
    }

    @Override
    protected Iterator<AbstractMap.SimpleEntry<String, T>> getIteratorForReader(BufferedReader br, int count, String filePath) {
        if (filePath.startsWith(FSGlobal.DATASET_FOLDER)) {
            return createIteratorForReader(br.lines().iterator(), count);
        }
        return super.getIteratorForReader(br, count, filePath);
    }

    protected abstract Iterator<AbstractMap.SimpleEntry<String, T>> createIteratorForReader(Iterator<String> lines, int maxObjCountToReturn);

    protected abstract File getFileForDataset(String datasetName);
}

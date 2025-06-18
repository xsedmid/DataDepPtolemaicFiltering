/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.metricSpaceImpl.parsersOfOtherFormats.impl;

import java.io.File;
import java.util.AbstractMap;
import java.util.Iterator;
import vm.datatools.DataTypeConvertor;
import vm.fs.FSGlobal;
import vm.fs.dataset.FSDatasetInstances;
import vm.fs.metricSpaceImpl.parsersOfOtherFormats.AbstractFSMetricSpacesStorageWithOthersDatasetStorage;
import vm.metricSpace.Dataset;
import vm.metricSpace.data.toStringConvertors.MetricObjectDataToStringInterface;
import vm.metricSpace.data.toStringConvertors.SingularisedConvertors;
import vm.metricSpace.distance.DistanceFunctionInterface;
import vm.metricSpace.distance.impl.L2OnFloatsArray;

/**
 *
 * @author au734419
 * @param <T>
 */
public class FSSpectraPhilipStorage<T> extends AbstractFSMetricSpacesStorageWithOthersDatasetStorage<T> {

    public static final String DATASET_NAME = "data95_dyn.txt";

    public FSSpectraPhilipStorage(DistanceFunctionInterface<T> df, MetricObjectDataToStringInterface<T> dataSerializator) {
        super(df, dataSerializator);
    }

    @Override
    protected File getFileForDataset(String datasetName) {
        File ret = new File(FSGlobal.DATASET_FOLDER, "Philip");
        ret = new File(ret, datasetName);
        return ret;
    }
///////////////////////////////// priprietary

    public static final Dataset<float[]> createDataset() {
        FSSpectraPhilipStorage storage = new FSSpectraPhilipStorage(new L2OnFloatsArray(), SingularisedConvertors.FLOAT_VECTOR_SPACE);
        return new FSDatasetInstances.FSDatasetWithOtherSource(DATASET_NAME, storage) {
            @Override
            public boolean shouldStoreDistsToPivots() {
                return false;
            }

            @Override
            public boolean shouldCreateKeyValueStorage() {
                return false;
            }
        };
    }

    @Override
    protected Iterator<AbstractMap.SimpleEntry<String, T>> createIteratorForReader(Iterator<String> lines, int maxObjCountToReturn) {
        return new Iterator<AbstractMap.SimpleEntry<String, T>>() {
            private int counter = 0;

            @Override
            public boolean hasNext() {
                return counter < maxObjCountToReturn && lines.hasNext();
            }

            @Override
            public AbstractMap.SimpleEntry<String, T> next() {
                counter++;
                String next = lines.next();
                float[] values = DataTypeConvertor.stringToFloats(next, ",");
                return new AbstractMap.SimpleEntry(Integer.toString(counter), values);
            }
        };
    }
}

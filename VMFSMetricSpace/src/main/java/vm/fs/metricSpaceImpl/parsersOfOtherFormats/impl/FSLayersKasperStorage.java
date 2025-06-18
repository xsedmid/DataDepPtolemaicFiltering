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
 * @author Vlada
 * @param <T>
 */
public class FSLayersKasperStorage<T> extends AbstractFSMetricSpacesStorageWithOthersDatasetStorage<T> {

    public static enum DIMENSIONALITY {
        DIM_0008,
        DIM_0016,
        DIM_0032,
        DIM_0064,
        DIM_0128,
        DIM_0256,
        DIM_0512,
        DIM_1024
    };

    public static enum TYPE {
        SMALL1_misfit_Bi5d_full_h5,
        LARGE2_gr_flake_full_h5,
        SMALL3_misfit_Se3d,
        SMALL4_misfit_VB,
    };
    public static final int DIM_0008 = 64;
    public static final int DIM_0016 = 256;
    public static final int DIM_0032 = 1024;
    public static final int DIM_0064 = 4096;
    public static final int DIM_0128 = 16384;
    public static final int DIM_0256 = 65536;
    public static final int DIM_0512 = 262144;
    public static final int DIM_1024 = 1048576;

    public static int dimensionalityEnumToSquare(DIMENSIONALITY dimensionalityOfVectors) {
        switch (dimensionalityOfVectors) {
            case DIM_0008:
                return 64;
            case DIM_0016:
                return 256;
            case DIM_0032:
                return 1024;
            case DIM_0064:
                return 4096;
            case DIM_0128:
                return 16384;
            case DIM_0256:
                return 65536;
            case DIM_0512:
                return 266144;
            case DIM_1024:
                return 1048576;
            default:
                throw new AssertionError();
        }
    }

    private static String typeToName(TYPE type) {
        switch (type) {
            case SMALL1_misfit_Bi5d_full_h5:
                return "misfit_Bi5d_full_h5";
            case LARGE2_gr_flake_full_h5:
                return "gr_flake_full_h5";
            case SMALL3_misfit_Se3d:
                return "misfit_Se3d";
            case SMALL4_misfit_VB:
                return "misfit_VB";
            default:
                throw new AssertionError();
        }
    }

    public FSLayersKasperStorage(DistanceFunctionInterface<T> df, MetricObjectDataToStringInterface<T> dataSerializator) {
        super(df, dataSerializator);
    }

    @Override
    protected File getFileForDataset(String datasetName) {
        int dimSqrt = parseDim(datasetName);
        String folderName = vm.mathtools.Tools.formatFirstZeros(dimSqrt * dimSqrt, 7) + "dim";
        File ret = new File(FSGlobal.DATASET_FOLDER, folderName);
        ret = new File(ret, datasetName + ".gz");
        return ret;
    }
///////////////////////////////// priprietary

    public static final Dataset<float[]> createDataset(TYPE type, DIMENSIONALITY dimensionalityOfVectors) {
        int dimSquareRooted = dimensionalityEnumToSquare(dimensionalityOfVectors);
        if (type == TYPE.SMALL1_misfit_Bi5d_full_h5 && dimSquareRooted == 512) {
            return null;
        }
        if (type == TYPE.SMALL1_misfit_Bi5d_full_h5 && dimSquareRooted == 1024) {
            return null;
        }
        String name = typeToName(type) + "_" + dimSquareRooted;
        if (type.equals(TYPE.SMALL3_misfit_Se3d) || type.equals(TYPE.SMALL4_misfit_VB)) {
            name += "_5h";
        }
        name += "_dct.txt";
        FSLayersKasperStorage storage = new FSLayersKasperStorage(new L2OnFloatsArray(), SingularisedConvertors.FLOAT_VECTOR_SPACE);
        return new FSDatasetInstances.FSDatasetWithOtherSource(name, storage) {
            @Override
            public boolean shouldStoreDistsToPivots() {
                return true;
            }

            @Override
            public boolean shouldCreateKeyValueStorage() {
                return true;
            }
        };
    }

    private int parseDim(String datasetName) {
        String[] pref = new String[]{
            "misfit_Bi5d_full_h5_",
            "gr_flake_full_h5_",
            "misfit_Se3d_",
            "misfit_VB_"};
        for (String p : pref) {
            if (datasetName.startsWith(p)) {
                String ret = datasetName.substring(p.length());
                int min = Math.min(ret.indexOf("_"), ret.indexOf("."));
                ret = ret.substring(0, min);
                return Integer.parseInt(ret);
            }
        }
        throw new RuntimeException("Unknown name of file" + datasetName);
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
                float[] values = DataTypeConvertor.stringToFloats(next, " ");
                return new AbstractMap.SimpleEntry(Integer.toString(counter), values);
            }
        };
    }

}

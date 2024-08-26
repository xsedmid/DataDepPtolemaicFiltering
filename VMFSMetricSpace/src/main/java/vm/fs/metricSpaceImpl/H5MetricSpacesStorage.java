package vm.fs.metricSpaceImpl;

import io.jhdf.HdfFile;
import io.jhdf.api.Dataset;
import io.jhdf.api.Node;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.data.toStringConvertors.MetricObjectDataToStringInterface;

/**
 *
 * @author xmic
 */
public class H5MetricSpacesStorage<T> extends FSMetricSpacesStorage<T> {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public final Logger LOG = Logger.getLogger(H5MetricSpacesStorage.class.getName());

    public H5MetricSpacesStorage(AbstractMetricSpace<T> metricSpace, MetricObjectDataToStringInterface dataSerializator) {
        super(metricSpace, dataSerializator);
    }

    public H5MetricSpacesStorage(MetricObjectDataToStringInterface dataSerializator) {
        super(dataSerializator);
    }

    @Override
    public Iterator<Object> getObjectsFromDataset(String datasetName, Object... params) {
        params = Tools.concatArrays(params, new Object[]{""});
        return getIteratorOfObjects(FSGlobal.DATASET_FOLDER, datasetName, params);
    }

    @Override
    public List<Object> getPivots(String pivotSetName, Object... params) {
        params = Tools.concatArrays(params, new Object[]{"P"});
        Iterator<Object> it = getIteratorOfObjects(FSGlobal.PIVOT_FOLDER, pivotSetName, params);
        return Tools.getObjectsFromIterator(it);
    }

    @Override
    public List<Object> getQueryObjects(String querySetName, Object... params) {
        params = Tools.concatArrays(params, new Object[]{"Q"});
        Iterator<Object> it = getIteratorOfObjects(FSGlobal.QUERY_FOLDER, querySetName, params);
        return Tools.getObjectsFromIterator(it);
    }

    @Override
    public Iterator<Object> getIteratorOfObjects(File f, Object... params) {
        List<Object> listOfParams = Tools.arrayToList(params);
        if (params.length > 0 && listOfParams.contains("P")) {
            Iterator<Object> it = super.getIteratorOfObjects(f, params);
            return it;
        }
        if (!f.exists()) {
            LOG.log(Level.SEVERE, "The file does not exists!");
        }
        HdfFile hdfFile = new HdfFile(f.toPath());
        Node node = hdfFile.iterator().next();
        String name = node.getName();
        LOG.log(Level.INFO, "Returning data from the dataset (group) {0} in the file {1}", new Object[]{name, f.getName()});
        Dataset dataset = hdfFile.getDatasetByPath(name);
        int count = params.length > 0 && params[0] instanceof Integer ? (int) params[0] : Integer.MAX_VALUE;
        if (count < 0) {
            count = Integer.MAX_VALUE;
        }
        String prefix = params[params.length - 1].toString();
        return new H5MetricObjectFileIterator(hdfFile, dataset, prefix, count);
    }

    @Override
    protected void storeMetricObject(Object metricObject, OutputStream datasetOutputStream, Object... additionalParamsToStoreWithNewDataset) throws IOException {
        super.storeMetricObject(metricObject, datasetOutputStream, additionalParamsToStoreWithNewDataset);
    }

    public Map<Comparable, Object> getAsMap(String datasetName) {
        File f = getFileForObjects(FSGlobal.DATASET_FOLDER, datasetName, false);
        HdfFile hdfFile = new HdfFile(f.toPath());
        Node node = hdfFile.iterator().next();
        String name = node.getName();
        LOG.log(Level.INFO, "Returning data from the dataset (group) {0} in the file {1}", new Object[]{name, f.getName()});
        Dataset dataset = hdfFile.getDatasetByPath(name);
        VMH5StorageAsMap ret = new VMH5StorageAsMap(dataset);
        return ret;
    }

    private class H5MetricObjectFileIterator implements Iterator<Object> {

        protected AbstractMap.SimpleEntry<String, float[]> nextObject;
        protected AbstractMap.SimpleEntry<String, float[]> currentObject;

        private final HdfFile hdfFile;
        private final Dataset dataset;
        private final int maxCount;
        private final int[] vectorDimensions;
        private final String prefixFoIDs;
        private final long[] counter;

        private H5MetricObjectFileIterator(HdfFile hdfFile, Dataset dataset, String prefix, int maxCount) {
            this.hdfFile = hdfFile;
            this.dataset = dataset;
            int[] storageDimensions = dataset.getDimensions();
            this.maxCount = Math.min(maxCount, storageDimensions[0]);
            this.vectorDimensions = new int[]{1, storageDimensions[1]};
            this.prefixFoIDs = prefix;
            counter = new long[]{0, 0};
            nextObject = nextStreamObject();
        }

        @Override
        public boolean hasNext() {
            boolean ret = nextObject != null;
            if (!ret) {
                hdfFile.close();
            }
            return ret;
        }

        @Override
        public AbstractMap.SimpleEntry<String, float[]> next() {
            if (nextObject == null) {
                throw new NoSuchElementException("No more objects in the stream");
            }
            currentObject = nextObject;
            nextObject = nextStreamObject();
            return currentObject;
        }

        private AbstractMap.SimpleEntry<String, float[]> nextStreamObject() {
            if (counter[0] >= maxCount) {
                return null;
            }
            float[][] dataBuffer = (float[][]) dataset.getData(counter, vectorDimensions);
            String id = prefixFoIDs + (counter[0] + 1);
            AbstractMap.SimpleEntry<String, float[]> entry = new AbstractMap.SimpleEntry<>(id, dataBuffer[0]);
            counter[0]++;
            return entry;
        }
    }

    private class VMH5StorageAsMap implements Map<Comparable, Object> {

        private final Dataset dataset;
        private final int[] dimensions;
        private final int[] vectorDimensions;

        public VMH5StorageAsMap(Dataset dataset) {
            this.dataset = dataset;
            dimensions = dataset.getDimensions();
            vectorDimensions = new int[]{1, dimensions[1]};
        }

        @Override
        public int size() {
            return dimensions[0];
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean containsKey(Object key) {
            int id;
            if (key instanceof String) {
                id = Integer.parseInt(key.toString());
            } else if (!(key instanceof Long) && !(key instanceof Integer)) {
                return false;
            } else {
                id = (int) key;
            }
            return id <= size();
        }

        @Override
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float[] get(Object key) {
            if (containsKey(key)) {
                long x = Long.parseLong(key.toString());
                x -= 1;
                long[] shift = new long[]{x, 0};
                float[][] dataBuffer = (float[][]) dataset.getData(shift, vectorDimensions);
                return dataBuffer[0];
            }
            return null;
        }

        @Override
        public Object put(Comparable key, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object remove(Object key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void putAll(java.util.Map m) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set keySet() {
            System.out.println("Minimum is 0, maximum is " + size() + ". Use row index as the key");
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Collection values() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set entrySet() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

}

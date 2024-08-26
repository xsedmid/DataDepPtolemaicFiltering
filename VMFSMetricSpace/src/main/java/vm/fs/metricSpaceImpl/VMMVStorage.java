package vm.fs.metricSpaceImpl;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import vm.fs.FSGlobal;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.ToolsMetricDomain;

/**
 *
 * @author Vlada
 * @param <T>
 */
public class VMMVStorage<T> {

    public static final Logger LOG = Logger.getLogger(VMMVStorage.class.getName());
    private static final String MAP_NAME = "data";

    private final MVStore storage;
    private final String datasetName;
    private final boolean willBeDeleted;
    private final MVMap<Comparable, T> map;

    public VMMVStorage(String datasetName, boolean createNew) {
        this.datasetName = datasetName;
        this.willBeDeleted = createNew;
        storage = getStorage();
        if (storage != null) {
            map = storage.openMap(MAP_NAME);
        } else {
            map = null;
        }
    }

    private MVStore getStorage() {
        if (datasetName == null) {
            throw new Error("datasetName cannot be null");
        }
        File file = getFile();
        if (!file.exists() && !willBeDeleted) {
            LOG.log(Level.SEVERE, "The file {0} does not exists. Cannot read.", file.getAbsolutePath());
            return null;
        }
        MVStore.Builder ret = new MVStore.Builder().fileName(file.getAbsolutePath()).compressHigh();
        if (!willBeDeleted) {
            ret = ret.readOnly();
            ret = ret.autoCommitDisabled();
        }
        return ret.open();
    }

    public static boolean exists(String datasetName) {
        File ret = new File(FSGlobal.DATASET_MVSTORAGE_FOLDER, datasetName);
        ret = FSGlobal.checkFileExistence(ret, false);
        return ret.exists();
    }

    public static void delete(String datasetName) {
        File ret = new File(FSGlobal.DATASET_MVSTORAGE_FOLDER, datasetName);
        ret = FSGlobal.checkFileExistence(ret, true);
        ret.delete();

    }

    public File getFile() {
        File ret = new File(FSGlobal.DATASET_MVSTORAGE_FOLDER, datasetName);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

    public Map<Comparable, T> getKeyValueStorage() {
        if (map == null) {
            return null;
        }
        return Collections.unmodifiableMap(map);
    }

    public void insertObjects(Map<Comparable, T> source) {
        LOG.log(Level.INFO, "Commit of {0} objects", map.size());
        map.putAll(source);
        storage.commit();
        System.gc();
        LOG.log(Level.INFO, "Stored {0} objects", map.size());
    }

    public void insertObjects(Dataset<T> dataset) {
        Iterator metricObjects = dataset.getMetricObjectsFromDataset();
        AbstractMetricSpace<T> metricSpace = dataset.getMetricSpace();
        SortedMap<Comparable, T> priorityObjects = loadBatch(metricObjects, metricSpace);
        int batchSize = priorityObjects.size();
        if (metricObjects.hasNext()) {
            SortedSet allSortedIDs = new TreeSet<>(ToolsMetricDomain.getIDs(dataset.getMetricObjectsFromDataset(), metricSpace));
            LOG.log(Level.INFO, "Loaded and sorted {0} IDs", allSortedIDs.size());
            storePrefix(allSortedIDs, priorityObjects);
            int goArounds = 0;
            // process wisely, search for first
            while (!allSortedIDs.isEmpty()) {
                goArounds++;
                System.gc();
                performPrefixBatch(metricObjects, metricSpace, priorityObjects, allSortedIDs, batchSize, goArounds);
                metricObjects = dataset.getMetricObjectsFromDataset();
            }
        } else {
            map.putAll(priorityObjects);
        }
        System.gc();
        storage.commit();
        LOG.log(Level.INFO, "Stored {0} objects", map.size());
    }

    public int size() {
        return getKeyValueStorage().size();
    }

    private SortedMap<Comparable, T> loadBatch(Iterator metricObjects, AbstractMetricSpace<T> metricSpace) {
        SortedMap<Comparable, T> ret = new TreeMap<>();
        List<Object> objectsFromIterator = vm.datatools.Tools.getObjectsFromIterator(90f, metricObjects);
        for (int i = 0; i < objectsFromIterator.size(); i++) {
            Object next = objectsFromIterator.get(i);
            Comparable id = metricSpace.getIDOfMetricObject(next);
            T dataOfMetricObject = metricSpace.getDataOfMetricObject(next);
            ret.put(id, dataOfMetricObject);
        }
        LOG.log(Level.INFO, "Loaded batch of {0} objects", ret.size());
        return ret;
    }

    private boolean storePrefix(SortedSet<Comparable> topIDs, SortedMap<Comparable, T> batch) {
        boolean ret = false;
        if (topIDs.isEmpty() || batch.isEmpty()) {
            return ret;
        }
        Comparable firstID = topIDs.first();
        Comparable batchID = batch.firstKey();
        while (firstID.equals(batchID)) {
            map.put(firstID, batch.get(batchID));
            ret = true;
            topIDs.remove(firstID);
            batch.remove(firstID);
            if (topIDs.isEmpty() || batch.isEmpty()) {
                return ret;
            }
            firstID = topIDs.first();
            batchID = batch.firstKey();
        }
        return ret;
    }

    private void performPrefixBatch(Iterator metricObjects, AbstractMetricSpace<T> metricSpace, SortedMap<Comparable, T> priorityObjects, SortedSet allSortedIDs, int batchSize, int goArounds) {
        int itCounter = 0;
        while (metricObjects.hasNext() && !allSortedIDs.isEmpty()) {
            itCounter++;
            Object o = metricObjects.next();
            Comparable id = metricSpace.getIDOfMetricObject(o);
            T data = metricSpace.getDataOfMetricObject(o);
            boolean add = (priorityObjects.size() < batchSize || id.compareTo(priorityObjects.lastKey()) < 0) && (id.compareTo(allSortedIDs.first()) >= 0);
            if (add) {
                priorityObjects.put(id, data);
                boolean check = priorityObjects.size() >= batchSize * 0.7f || allSortedIDs.isEmpty();
//                LOG.log(Level.INFO, "Checking? {5}: added {0} to cached, waiting for {1}, cached {2} objects, first obj in cached: {3}, comparison of first cached with the required: {4}",
//                        new Object[]{id, allSortedIDs.first(), priorityObjects.size(), priorityObjects.firstKey(), priorityObjects.firstKey().compareTo(allSortedIDs.first()), check});
                while (check) {
                    check = storePrefix(allSortedIDs, priorityObjects);
                }
            }
            while (priorityObjects.size() > batchSize) {
                priorityObjects.remove(priorityObjects.lastKey());
            }
            if (itCounter % 100000 == 0) {
                LOG.log(Level.INFO, "Pass {3}, read {0} objects, stored {4}, remain {1}, cached: {2}", new Object[]{itCounter, allSortedIDs.size(), priorityObjects.size(), goArounds, map.size()});
            }
        }
        storePrefix(allSortedIDs, priorityObjects);
    }

    public void close() {
        storage.close();
    }
}

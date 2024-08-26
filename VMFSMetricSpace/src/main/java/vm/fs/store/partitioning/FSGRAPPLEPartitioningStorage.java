/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.store.partitioning;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.logging.Level;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.metricSpace.datasetPartitioning.impl.GRAPPLEPartitioning;

/**
 *
 * @author Vlada
 */
public class FSGRAPPLEPartitioningStorage extends FSVoronoiPartitioningStorage {

    @Override
    public File getFile(String datasetName, int pivotCount, boolean willBeDeleted) {
        String name = datasetName + "_" + pivotCount + "pivots.csv.gz";
        File ret = new File(FSGlobal.GRAPPLE_PARTITIONING_STORAGE, name);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleted);
        return ret;
    }

    @Override
    public Map<Comparable, TreeSet<Comparable>> load(String datasetName, int origPivotCount) {
        File f = getFile(datasetName, origPivotCount, false);
        SortedMap<String, String[]> keyValueMap = Tools.parseCsvMapKeyValues(f.getAbsolutePath());
        Map<Comparable, TreeSet<Comparable>> ret = new HashMap<>();
        Iterator<Map.Entry<String, String[]>> it = keyValueMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            String[] values = entry.getValue();
            List<GRAPPLEPartitioning.ObjectMetadata> list = new ArrayList();
            for (int i = 1; i < values.length; i++) {
                String value = values[i];                
                list.add(GRAPPLEPartitioning.getObjectMetadataInstance(value));
            }
            ret.put(entry.getKey(), new TreeSet(list));
        }
        LOG.log(Level.INFO, "The Voronoi partitioning has {0} non empty cells", ret.size());
        return ret;
    }

}

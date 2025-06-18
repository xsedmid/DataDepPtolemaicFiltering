/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.metricSpaceImpl.parsersOfOtherFormats;

import java.io.File;
import java.util.Iterator;
import vm.fs.FSGlobal;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.metricSpace.MetricSpaceWithIDsAsObjects;
import vm.metricSpace.data.toStringConvertors.SingularisedConvertors;
import vm.metricSpace.distance.impl.QScore;

/**
 *
 * @author xmic
 */
public class FSPDBeStorage extends FSMetricSpacesStorage<String> {
    
    public static final String DOUBLE_DOT_WINDOWS = "%3A";

    public FSPDBeStorage() {
        super(new MetricSpaceWithIDsAsObjects(new QScore(0)), SingularisedConvertors.EMPTY_CONVERTOR);
    }

    @Override
    public void storeObjectToDataset(Object metricObject, String datasetName, Object... additionalParamsToStoreWithNewDataset) {
        throw new UnsupportedOperationException("This dataset does not support adding objects in this way. Download binaries instead.");
    }

    @Override
    public Iterator<Object> getObjectsFromDataset(String datasetName, Object... params) {
        File root = new File(FSGlobal.DATASET_FOLDER, datasetName);
        QScore.setRootFolder(root.getAbsolutePath());
        File[] folders = root.listFiles();
        int max = params.length > 0 ? (int) params[0] : -1;
        return new FilesIterator(folders, max);
    }

    private class FilesIterator implements Iterator<Object> {

        private final int maxCount;
        private final File[] folders;
        private File[] currChildren;
        private int totalCounter;
        private int folderIdx;
        private int fileIdx;

        public FilesIterator(File[] folders, int maxCount) {
            this.folders = folders;
            this.maxCount = maxCount;
            totalCounter = 0;
            folderIdx = 0;
            fileIdx = 0;
            currChildren = folders[0].listFiles();
        }

        @Override
        public boolean hasNext() {
            return totalCounter != maxCount && (fileIdx != currChildren.length || folderIdx != folders.length);
        }

        @Override
        public String next() {
            String ret = currChildren[fileIdx].getName();
            ret = ret.substring(0, ret.indexOf(".bin"));
            if (fileIdx == currChildren.length - 1) {
                folderIdx++;
                fileIdx = 0;
                currChildren = folders[folderIdx].listFiles();
            } else {
                fileIdx++;
            }
            totalCounter++;
            return ret.replace(DOUBLE_DOT_WINDOWS, ":");
        }

    }
}

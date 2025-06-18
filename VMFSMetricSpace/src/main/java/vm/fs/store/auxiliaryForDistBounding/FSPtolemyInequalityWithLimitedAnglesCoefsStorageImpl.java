package vm.fs.store.auxiliaryForDistBounding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vm.datatools.Tools;
import vm.fs.FSGlobal;
import vm.metricSpace.Dataset;
import vm.metricSpace.ToolsMetricDomain;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentPtolemaicFiltering;
import vm.metricSpace.distance.bounding.twopivots.impl.DataDependentPtolemaicFilteringForStreamKNNClassifier;
import vm.metricSpace.distance.bounding.twopivots.storeLearned.PtolemyInequalityWithLimitedAnglesCoefsStoreInterface;
import vm.metricSpace.distance.storedPrecomputedDistances.AbstractPrecomputedPairsOfDistancesStorage;

/**
 *
 * @author xmic
 */
public class FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl implements PtolemyInequalityWithLimitedAnglesCoefsStoreInterface {

    public static final Logger LOG = Logger.getLogger(FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl.class.getName());

    public static File getFile(String resultName, boolean willBeDeleled) {
        File folderFile = new File(FSGlobal.AUXILIARY_FOR_PTOLEMAIOS_COEFS_WITH_LIMITED_ANGLES);
        File ret = new File(folderFile, resultName);
        ret = FSGlobal.checkFileExistence(ret, willBeDeleled);
        LOG.log(Level.INFO, "File path: {0}", ret.getAbsolutePath());
        return ret;
    }

    @Override
    public String getResultDescription(String datasetName, int smallestDists, int sampleOCount, int sampleQcount, int pivots, boolean allPivotPairs) {
        String ret = datasetName + "__smallestDists_" + smallestDists + "_sampleOCount" + sampleOCount + "_sampleQcount" + sampleQcount + "__pivots_" + pivots + "__allPivotPairs_" + allPivotPairs + ".csv";
        LOG.log(Level.INFO, "File name: {0}", ret);
        return ret;
    }

    public static DataDependentPtolemaicFiltering getLearnedInstance(String resultPreffixName, Dataset dataset, int pivotCount) {
        return getLearnedInstance(resultPreffixName, dataset, pivotCount, true);
    }

    public static DataDependentPtolemaicFiltering getLearnedInstance(String resultPreffixName, Dataset dataset, int pivotCount, boolean queryDynamicPivotPairsSelection) {
        FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl storageOfCoefs = new FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl();
        String fileName = storageOfCoefs.getNameOfFileWithCoefs(dataset.getDatasetName(), pivotCount, true);
        File file = getFile(fileName, false);
        Map<String, float[]> coefs = Tools.parseCsvMapKeyFloatValues(file.getAbsolutePath());
        List pivots = dataset.getPivots(pivotCount);
        List pivotIDs = ToolsMetricDomain.getIDsAsList(pivots.iterator(), dataset.getMetricSpace());
        float[][][] coefsToArrays = transformsCoefsToArrays(coefs, pivotIDs);
        return new DataDependentPtolemaicFiltering(resultPreffixName, coefsToArrays, queryDynamicPivotPairsSelection);
    }

    public static final float[][][] transformsCoefsToArrays(Map<String, float[]> coefs, List pivotIDs) {
        Iterator<String> it = coefs.keySet().iterator();
        float[][][] ret = new float[pivotIDs.size()][pivotIDs.size()][4];
        while (it.hasNext()) {
            String key = it.next();
            String[] pivots = key.split("-");
            String[] pairs = new String[2];
            if (pivots.length > 2 && pivots.length % 2 == 0) {
                pairs[0] = pivots[0];
                pairs[1] = pivots[pivots.length / 2];
                for (int i = 1; i < pivots.length / 2; i++) {
                    pairs[0] = pairs[0] + "-" + pivots[i];
                    pairs[1] = pairs[1] + "-" + pivots[i + pivots.length / 2];
                }
            } else {
                pairs = pivots;
            }
            int idx1 = pivotIDs.indexOf(pairs[0]);
            int idx2 = pivotIDs.indexOf(pairs[1]);
            if (idx1 == -1 || idx2 == -1) {
                throw new IllegalArgumentException("Bad pivot IDs. Do they contain unequal number of - in it? " + pivots[0] + "   " + pivots[1]);
            }
            ret[idx1][idx2] = coefs.get(key);
            ret[idx2][idx1] = coefs.get(key);
        }
        return ret;
    }

    @Override
    public void storeCoefficients(Map<Object, float[]> results, String resultName) {
        try {
            File resultFile = getFile(resultName, true);
            PrintStream err = System.err;
            System.setErr(new PrintStream(new FileOutputStream(resultFile, false)));
            Tools.printMapOfKeyFloatValues(results);
            System.setErr(err);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public static DataDependentPtolemaicFilteringForStreamKNNClassifier getLearnedInstanceForVoronoiPartitioning(String resultPreffixName, Dataset dataset, int pivotCount, int centroidsCount) {
        return getLearnedInstanceForVoronoiPartitioning(resultPreffixName, dataset, pivotCount, centroidsCount, true);
    }

    public static DataDependentPtolemaicFilteringForStreamKNNClassifier getLearnedInstanceForVoronoiPartitioning(String resultPreffixName, Dataset dataset, int pivotCount, int centroidsCount, boolean wisePivotSelection) {
        FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl storageOfCoefs = new FSPtolemyInequalityWithLimitedAnglesCoefsStorageImpl();
        String fileName = storageOfCoefs.getNameOfFileWithCoefs(dataset.getDatasetName(), pivotCount, true);
        File file = getFile(fileName, false);
        Map<String, float[]> coefs = Tools.parseCsvMapKeyFloatValues(file.getAbsolutePath());
        List pivots = dataset.getPivots(pivotCount);
        List pivotIDs = ToolsMetricDomain.getIDsAsList(pivots.iterator(), dataset.getMetricSpace());
        float[][][] coefsToArrays = transformsCoefsToArrays(coefs, pivotIDs);
        List centroids = dataset.getPivots(centroidsCount);
        List centroidsData = dataset.getMetricSpace().getDataOfMetricObjects(centroids);
        return new DataDependentPtolemaicFilteringForStreamKNNClassifier(resultPreffixName, coefsToArrays, centroidsData, dataset.getDistanceFunction(), wisePivotSelection);
    }

    public String getNameOfFileWithCoefs(String datasetName, int pivotCount, boolean allPivotPairs) {
        return getResultDescription(datasetName, AbstractPrecomputedPairsOfDistancesStorage.IMPLICIT_K, AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_SET_SIZE, AbstractPrecomputedPairsOfDistancesStorage.SAMPLE_QUERY_SET_SIZE, pivotCount, allPivotPairs);
    }

}

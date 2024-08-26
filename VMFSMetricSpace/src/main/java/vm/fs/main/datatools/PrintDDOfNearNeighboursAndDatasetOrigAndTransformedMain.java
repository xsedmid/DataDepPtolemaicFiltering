package vm.fs.main.datatools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeSet;
import vm.fs.metricSpaceImpl.FSMetricSpaceImpl;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.search.algorithm.impl.GroundTruthEvaluator;
import vm.fs.dataset.FSDatasetInstanceSingularizator;
import vm.metricSpace.ToolsMetricDomain;
import vm.metricSpace.AbstractMetricSpacesStorage;
import vm.metricSpace.AbstractMetricSpace;
import vm.metricSpace.Dataset;
import vm.metricSpace.data.toStringConvertors.impl.FloatVectorConvertor;
import vm.metricSpace.distance.DistanceFunctionInterface;

/**
 *
 * @author Vlada
 */
public class PrintDDOfNearNeighboursAndDatasetOrigAndTransformedMain {

    public static void main(String[] args) {
        Dataset datasetOrig = new FSDatasetInstanceSingularizator.DeCAFDataset();
        Dataset datasetTransformed = new FSDatasetInstanceSingularizator.DeCAF_PCA10Dataset();
        String datasetName = datasetOrig.getDatasetName();
        String transformedDatasetName = datasetTransformed.getDatasetName();
        float transformedDistInterval = 1.0f;

//      getHistogramsForRandomAndNearestNeighbours
        int objCount = PrintAndPlotDDOfDatasetMain.IMPLICIT_OBJ_COUNT;//100,000
        int distCount = PrintAndPlotDDOfDatasetMain.IMPLICIT_DIST_COUNT;//1000,000
        int queriesCount = 1;//1000
        int k = 100;
        List<Object[]> idsOfRandomPairs = new ArrayList<>();
        List<Object[]> idsOfNNPairs = new ArrayList<>();
        SortedMap<Float, Float> ddRandomSample = ToolsMetricDomain.createDistanceDensityPlot(datasetOrig, objCount, distCount, idsOfRandomPairs);
        float distInterval = ToolsMetricDomain.computeBasicDistInterval(ddRandomSample.lastKey());
        SortedMap<Float, Float> ddOfNNSample = createDDOfNNSample(datasetOrig, queriesCount, objCount, k, idsOfNNPairs);
//      print
        printDDOfRandomAndNearNeighbours(datasetName, distInterval, ddRandomSample, ddOfNNSample);

//      find the same pairs in the transformed dataset and print corresponding distances
        List<Object> transformedObjects = datasetTransformed.getMetricSpacesStorage().getSampleOfDataset(transformedDatasetName, -1);
        AbstractMetricSpace metricSpace = datasetTransformed.getMetricSpace();
        Map<Comparable, Object> metricObjectsAsIdObjectMap = ToolsMetricDomain.getMetricObjectsAsIdDataMap(metricSpace, transformedObjects);
        DistanceFunctionInterface distanceFunctionForTransformedDataset = metricSpace.getDistanceFunctionForDataset(transformedDatasetName);
        SortedMap<Float, Float> ddRandomSampleTransformed = evaluateDDForPairs(distanceFunctionForTransformedDataset, idsOfRandomPairs, metricObjectsAsIdObjectMap);
        SortedMap<Float, Float> ddOfNNSampleTransformed = evaluateDDForPairs(distanceFunctionForTransformedDataset, idsOfNNPairs, metricObjectsAsIdObjectMap);

//      print
        printDDOfRandomAndNearNeighbours(transformedDatasetName, transformedDistInterval, ddRandomSampleTransformed, ddOfNNSampleTransformed);
    }

    private static SortedMap<Float, Float> createDDOfNNSample(Dataset dataset, int queryObjCount, int sampleCount, int k, List<Object[]> idsOfNNPairs) {
        List<Object> queryObjects = dataset.getQueryObjects(queryObjCount);
        List<Object> metricObjects = dataset.getSampleOfDataset(sampleCount + queryObjCount);
        AbstractMetricSpace metricSpace = dataset.getMetricSpace();
        for (int i = 0; i < queryObjCount; i++) {
            metricObjects.remove(0);
        }
        GroundTruthEvaluator gte = new GroundTruthEvaluator(dataset, k);
        TreeSet<Map.Entry<Object, Float>>[] groundTruth = gte.evaluateIteratorInParallel(metricObjects.iterator());
        List<Float> distances = new ArrayList<>();
        for (int i = 0; i < groundTruth.length; i++) {
            TreeSet<Map.Entry<Object, Float>> evaluatedQuery = groundTruth[i];
            Object queryObjID = metricSpace.getIDOfMetricObject(queryObjects.get(i));
            for (Map.Entry<Object, Float> entry : evaluatedQuery) {
                Object idOfMetricObject = entry.getKey();
                idsOfNNPairs.add(new Object[]{queryObjID, idOfMetricObject});
                distances.add(entry.getValue());
            }
        }
        return ToolsMetricDomain.createDistanceDensityPlot(distances);
    }

    private static SortedMap<Float, Float> evaluateDDForPairs(DistanceFunctionInterface distanceFunction, List<Object[]> idsPairs, Map<Comparable, Object> metricObjects) {
        List<Float> distances = new ArrayList<>();
        for (Object[] idsPair : idsPairs) {
            Object o1 = metricObjects.get(idsPair[0]);
            Object o2 = metricObjects.get(idsPair[1]);
            float distance = distanceFunction.getDistance(o1, o2);
            distances.add(distance);
        }
        return ToolsMetricDomain.createDistanceDensityPlot(distances);
    }

    private static void printDDOfRandomAndNearNeighbours(String datasetName, float distInterval, SortedMap<Float, Float> ddRandomSample, SortedMap<Float, Float> ddOfNNSample) {
        System.out.println(datasetName);
        System.out.println("Distance;Density of random sample; Density of distances to near neighbours");
        for (float dist = 0; true; dist += distInterval) {
            float rand = ddRandomSample.containsKey(dist) ? ddRandomSample.get(dist) : 0;
            float nn = ddOfNNSample.containsKey(dist) ? ddOfNNSample.get(dist) : 0;
            System.out.println(dist + ";" + rand + ";" + nn);
            if (dist > ddOfNNSample.lastKey() && dist > ddRandomSample.lastKey()) {
                break;
            }
        }
    }
}

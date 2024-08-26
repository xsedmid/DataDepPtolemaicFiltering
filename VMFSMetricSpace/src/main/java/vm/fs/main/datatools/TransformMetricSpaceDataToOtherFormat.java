///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package vm.fs.main.datatools;
//
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import vm.datatools.Tools;
//import vm.fs.dataset.FSDatasetInstanceSingularizator;
//import vm.metricSpace.AbstractMetricSpace;
//import vm.metricSpace.Dataset;
//import vm.metricSpace.ToolsMetricDomain;
//
///**
// *
// * @author Vlada
// */
//public class TransformMetricSpaceDataToOtherFormat {
//
//    public static final Integer BATCH_SIZE = 50000;
//
//    public static void main(String[] args) {
//        Dataset origDataset = new M2DatasetInstanceSingularizator.DeCAF100MDatasetAndromeda();
//        Dataset destDataset = new FSDatasetInstanceSingularizator.DeCAF100M_Dataset();
//
//        transformObjects(origDataset.getMetricObjectsFromDataset(), origDataset.getMetricSpace(), destDataset);
//    }
//
//    private static void transformObjects(Iterator it, AbstractMetricSpace origMetricSpace, Dataset destDataset) {
//        AbstractMetricSpace destMetricSpace = destDataset.getMetricSpace();
//        Iterator alreadyTransformedIt = destDataset.getMetricObjectsFromDataset();
//        Set alreadyDoneIDs;
//        if (alreadyTransformedIt != null) {
//            alreadyDoneIDs = ToolsMetricDomain.getIDs(alreadyTransformedIt, destMetricSpace);
//            Logger.getLogger(TransformMetricSpaceDataToOtherFormat.class.getName()).log(Level.INFO, "Loaded IDs from {0} already transformed objects", alreadyDoneIDs.size());
//        } else {
//            alreadyDoneIDs = new HashSet<>();
//        }
//        int counter = 0;
//        while (it.hasNext()) {
//            List batch = Tools.getObjectsFromIterator(it, BATCH_SIZE);
//            counter += batch.size();
//            List transformed = ToolsMetricDomain.transformMetricObjectsToOtherRepresentation(batch, origMetricSpace, destMetricSpace, alreadyDoneIDs);
//            destDataset.getMetricSpacesStorage().storeObjectsToDataset(transformed.iterator(), -1, destDataset.getDatasetName());
//            Logger.getLogger(TransformMetricSpaceDataToOtherFormat.class.getName()).log(Level.INFO, "Transformed {0} objects", counter);
//        }
//        destDataset.updateDatasetSize();
//    }
//}

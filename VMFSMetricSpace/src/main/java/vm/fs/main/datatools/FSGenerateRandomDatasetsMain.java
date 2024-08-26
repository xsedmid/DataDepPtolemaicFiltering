/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vm.fs.main.datatools;

import vm.fs.metricSpaceImpl.FSMetricSpaceImpl;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.metricSpace.AbstractMetricSpacesStorage;
import vm.metricSpace.data.RandomVectorsGenerator;
import vm.metricSpace.data.toStringConvertors.impl.FloatVectorConvertor;

/**
 *
 * @author au734419
 */
public class FSGenerateRandomDatasetsMain {

    public static void main(String[] args) {
        FloatVectorConvertor floatVectorConvertor = new FloatVectorConvertor();
        FSMetricSpaceImpl<float[]> metricSpace = new FSMetricSpaceImpl<>();
        AbstractMetricSpacesStorage storage = new FSMetricSpacesStorage(metricSpace, floatVectorConvertor);

        RandomVectorsGenerator generator = new RandomVectorsGenerator(metricSpace, storage);
        generator.run();
    }

}

package vm.fs.dataset;

import java.util.HashMap;
import vm.metricSpace.DatasetOfCandidates;
import java.util.Map;
import org.h2.mvstore.MVStoreException;
import vm.fs.metricSpaceImpl.FSMetricSpaceImpl;
import vm.fs.metricSpaceImpl.FSMetricSpacesStorage;
import vm.fs.metricSpaceImpl.H5MetricSpacesStorage;
import vm.metricSpace.data.toStringConvertors.SingularisedConvertors;
import vm.fs.metricSpaceImpl.VMMVStorage;
import vm.fs.store.queryResults.FSNearestNeighboursStorageImpl;
import vm.metricSpace.Dataset;
import vm.metricSpace.ToolsMetricDomain;
import vm.queryResults.QueryNearestNeighboursStoreInterface;

/**
 *
 * @author xmic
 */
public class FSDatasetInstanceSingularizator {

    public static class DeCAFDataset extends FSFloatVectorDataset {

        public DeCAFDataset() {
            super("decaf_1m");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 256;
        }
    }

    public static class DeCAF20M_PCA256Dataset extends FSFloatVectorDataset {

        public DeCAF20M_PCA256Dataset() {
            super("decaf_20m_PCA256");
        }
    }

    public static class DeCAF100M_PCA256Dataset extends FSFloatVectorDataset {

        public DeCAF100M_PCA256Dataset() {
            super("decaf_100m_PCA256");
        }
    }

    public static class DeCAF100M_Dataset extends FSFloatVectorDataset {

        public DeCAF100M_Dataset() {
            super("decaf_100m");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 64;
        }
    }

    private static final Integer PIVOTS_RANDOM_DATASETS = 128;

    public static class RandomDataset10Uniform extends FSFloatVectorDataset {

        public RandomDataset10Uniform() {
            super("random_10dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }

    }

    public static class RandomDataset15Uniform extends FSFloatVectorDataset {

        public RandomDataset15Uniform() {
            super("random_15dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset20Uniform extends FSFloatVectorDataset {

        public RandomDataset20Uniform() {
            super("random_20dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset25Uniform extends FSFloatVectorDataset {

        public RandomDataset25Uniform() {
            super("random_25dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset30Uniform extends FSFloatVectorDataset {

        public RandomDataset30Uniform() {
            super("random_30dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset35Uniform extends FSFloatVectorDataset {

        public RandomDataset35Uniform() {
            super("random_35dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset40Uniform extends FSFloatVectorDataset {

        public RandomDataset40Uniform() {
            super("random_40dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset50Uniform extends FSFloatVectorDataset {

        public RandomDataset50Uniform() {
            super("random_50dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset60Uniform extends FSFloatVectorDataset {

        public RandomDataset60Uniform() {
            super("random_60dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset70Uniform extends FSFloatVectorDataset {

        public RandomDataset70Uniform() {
            super("random_70dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset80Uniform extends FSFloatVectorDataset {

        public RandomDataset80Uniform() {
            super("random_80dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset90Uniform extends FSFloatVectorDataset {

        public RandomDataset90Uniform() {
            super("random_90dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class RandomDataset100Uniform extends FSFloatVectorDataset {

        public RandomDataset100Uniform() {
            super("random_100dim_uniform_1M");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return PIVOTS_RANDOM_DATASETS;
        }
    }

    public static class SIFTdataset extends FSFloatVectorDataset {

        public SIFTdataset() {
            super("sift_1m");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 256;
        }

    }

    public static class MPEG7dataset extends Dataset<Map<String, Object>> {

        public MPEG7dataset() {
            super(
                    "mpeg7_1m",
                    new FSMetricSpaceImpl(),
                    new FSMetricSpacesStorage<>(new FSMetricSpaceImpl(), SingularisedConvertors.MPEG7_SPACE)
            );
        }

        @Override
        public Map<Comparable, Map<String, Object>> getKeyValueStorage() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean hasKeyValueStorage() {
            return VMMVStorage.exists(datasetName);
        }

        @Override
        public void deleteKeyValueStorage() {
            VMMVStorage.delete(datasetName);
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return -1;
        }
    }

    public static class DeCAF_PCA8Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA8Dataset() {
            super("decaf_1m_PCA8");
        }

    }

    public static class DeCAF_PCA10Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA10Dataset() {
            super("decaf_1m_PCA10");
        }
    }

    public static class DeCAF_PCA12Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA12Dataset() {
            super("decaf_1m_PCA12");
        }
    }

    public static class DeCAF_PCA16Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA16Dataset() {
            super("decaf_1m_PCA16");
        }
    }

    public static class DeCAF_PCA24Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA24Dataset() {
            super("decaf_1m_PCA24");
        }
    }

    public static class DeCAF_PCA32Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA32Dataset() {
            super("decaf_1m_PCA32");
        }
    }

    public static class DeCAF_PCA46Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA46Dataset() {
            super("decaf_1m_PCA46");
        }
    }

    public static class DeCAF_PCA68Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA68Dataset() {
            super("decaf_1m_PCA68");
        }
    }

    public static class DeCAF_PCA128Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA128Dataset() {
            super("decaf_1m_PCA128");
        }
    }

    public static class DeCAF_PCA256Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA256Dataset() {
            super("decaf_1m_PCA256");
        }
    }

    public static class DeCAF_PCA670Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA670Dataset() {
            super("decaf_1m_PCA670");
        }
    }

    public static class DeCAF_PCA1540Dataset extends FSFloatVectorDataset {

        public DeCAF_PCA1540Dataset() {
            super("decaf_1m_PCA1540");
        }
    }

    public static class DeCAF_GHP_50_256Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_256Dataset() {
            super("decaf_1m_GHP_50_256");
        }
    }

    public static class DeCAF_GHP_50_192Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_192Dataset() {
            super("decaf_1m_GHP_50_192");
        }
    }

    public static class DeCAF_GHP_50_128Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_128Dataset() {
            super("decaf_1m_GHP_50_128");
        }
    }

    public static class DeCAF_GHP_50_64Dataset extends FSHammingSpaceDataset {

        public DeCAF_GHP_50_64Dataset() {
            super("decaf_1m_GHP_50_64");
        }
    }

    public static class LAION_1M_SampleDataset extends FSFloatVectorDataset {

        public LAION_1M_SampleDataset() {
            super("laion2B-en-clip768v2-n=1M_sample.h5.gz");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-clip768v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots";
        }
    }

    public static class LAION_100k_Dataset extends H5FloatVectorDataset {

        private final boolean publicQueries;

        public LAION_100k_Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100K.h5");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5";
            }
            return "private-queries-10k-clip768v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots";
        }
    }

    public static class LAION_300k_Dataset extends H5FloatVectorDataset {

        private final boolean publicQueries;

        public LAION_300k_Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=300K.h5");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5";
            }
            return "private-queries-10k-clip768v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots";
        }
    }

    public static class LAION_10M_Dataset_Euclid extends LAION_10M_Dataset {

        public LAION_10M_Dataset_Euclid(boolean publicQueries) {
            super(publicQueries);
            datasetName += "euclid";
        }

    }

    public static class LAION_10M_Dataset_Dot extends LAION_10M_Dataset {

        public LAION_10M_Dataset_Dot(boolean publicQueries) {
            super(publicQueries);
            datasetName += "DotPro";
        }

    }

    public static class LAION_10M_Dataset_Angular extends LAION_10M_Dataset {

        public LAION_10M_Dataset_Angular(boolean publicQueries) {
            super(publicQueries);
            datasetName += "Angular";
        }

    }

    public static class LAION_30M_Dataset_Dot extends LAION_30M_Dataset {

        public LAION_30M_Dataset_Dot(boolean publicQueries) {
            super(publicQueries);
            datasetName += "DotPro";
        }

    }

    public static class LAION_100M_Dataset_Dot extends LAION_100M_Dataset {

        public LAION_100M_Dataset_Dot(boolean publicQueries) {
            super(publicQueries);
            datasetName += "DotPro";
        }

    }

    public static class LAION_30M_Dataset_Euclid extends LAION_30M_Dataset {

        public LAION_30M_Dataset_Euclid(boolean publicQueries) {
            super(publicQueries);
            datasetName += "euclid";
        }

    }

    public static class LAION_100M_Dataset_Euclid extends LAION_100M_Dataset {

        public LAION_100M_Dataset_Euclid(boolean publicQueries) {
            super(publicQueries);
            datasetName += "euclid";
        }

    }

    public static class LAION_10M_Dataset extends H5FloatVectorDataset {

        private final boolean publicQueries;

        public LAION_10M_Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=10M.h5");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5";
            }
            return "private-queries-10k-clip768v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots";
        }
    }

    public static class LAION_30M_Dataset extends H5FloatVectorDataset {

        private final boolean publicQueries;

        public LAION_30M_Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=30M.h5");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5";
            }
            return "private-queries-10k-clip768v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots";
        }
    }

    public static class LAION_100M_Dataset extends H5FloatVectorDataset {

        private final boolean publicQueries;

        public LAION_100M_Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100M.h5");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5";
            }
            return "private-queries-10k-clip768v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots";
        }
    }

    public static class LAION_100k_PCA32Dataset extends H5FloatVectorDataset {

        public LAION_100k_PCA32Dataset() {
            super("laion2B-en-pca32v2-n=100K.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca32v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA32_20000";

        }
    }

    public static class LAION_300k_PCA32Dataset extends H5FloatVectorDataset {

        public LAION_300k_PCA32Dataset() {
            super("laion2B-en-pca32v2-n=300K.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca32v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA32_20000";
        }
    }

    public static class LAION_10M_PCA32Dataset extends H5FloatVectorDataset {

        public LAION_10M_PCA32Dataset() {
            super("laion2B-en-pca32v2-n=10M.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca32v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA32_20000";
        }
    }

    public static class LAION_30M_PCA32Dataset extends H5FloatVectorDataset {

        public LAION_30M_PCA32Dataset() {
            super("laion2B-en-pca32v2-n=30M.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca32v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA32_20000";
        }
    }

    public static class LAION_100M_PCA32Dataset extends H5FloatVectorDataset {

        public LAION_100M_PCA32Dataset() {
            super("laion2B-en-pca32v2-n=100M.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca32v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA32_20000";
        }
    }

    public static class LAION_100k_PCA96Dataset extends H5FloatVectorDataset {

        public LAION_100k_PCA96Dataset() {
            super("laion2B-en-pca96v2-n=100K.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca96v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA96_20000";
        }
    }

    public static class LAION_300k_PCA96Dataset extends H5FloatVectorDataset {

        public LAION_300k_PCA96Dataset() {
            super("laion2B-en-pca96v2-n=300K.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca96v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA96_20000";

        }
    }

    public static class LAION_10M_PCA96Dataset extends H5FloatVectorDataset {

        public LAION_10M_PCA96Dataset() {
            super("laion2B-en-pca96v2-n=10M.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca96v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA96_20000";
        }

    }

    public static class LAION_30M_PCA96Dataset extends H5FloatVectorDataset {

        public LAION_30M_PCA96Dataset() {
            super("laion2B-en-pca96v2-n=30M.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca96v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA96_20000";
        }
    }

    public static class LAION_100M_PCA96Dataset extends H5FloatVectorDataset {

        public LAION_100M_PCA96Dataset() {
            super("laion2B-en-pca96v2-n=100M.h5");
        }

        @Override
        public String getQuerySetName() {
            return "public-queries-10k-pca96v2.h5";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA96_20000";
        }
    }

    public static class LAION_10M_PCA256Dataset extends FSFloatVectorDataset {

        public LAION_10M_PCA256Dataset() {
            super("laion2B-en-clip768v2-n=10M.h5_PCA256");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 128;
        }

    }

    public static class LAION_30M_PCA256Dataset extends FSFloatVectorDataset {

        public LAION_30M_PCA256Dataset() {
            super("laion2B-en-clip768v2-n=30M.h5_PCA256");
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA256";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA256";
        }
    }

    public static class LAION_100M_PCA256Dataset extends FSFloatVectorDataset {

        public LAION_100M_PCA256Dataset() {
            super("laion2B-en-clip768v2-n=100M.h5_PCA256");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_20000pivots_PCA256";
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 32;
        }

    }

    public static class LAION_30M_PCA256Prefixes24Dataset extends FSFloatVectorDataset {

        public LAION_30M_PCA256Prefixes24Dataset() {
            super("laion2B-en-clip768v2-n=30M.h5_PCA_pref24of256");
        }

    }

    public static class LAION_10M_PCA256Prefixes24Dataset extends FSFloatVectorDataset {

        public LAION_10M_PCA256Prefixes24Dataset() {
            super("laion2B-en-clip768v2-n=10M.h5_PCA_pref24of256");
        }

    }

    public static class LAION_100M_PCA256Prefixes24Dataset extends FSFloatVectorDataset {

        public LAION_100M_PCA256Prefixes24Dataset() {
            super("laion2B-en-clip768v2-n=100M.h5_PCA_pref24of256");
        }

    }

    public static class LAION_100M_PCA256Prefixes32Dataset extends FSFloatVectorDataset {

        public LAION_100M_PCA256Prefixes32Dataset() {
            super("laion2B-en-clip768v2-n=100M.h5_PCA_pref32of256");
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA_pref32of256";
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_PCA_pref32of256";
        }
    }

    public static class LAION_100k_GHP_50_192Dataset extends FSHammingSpaceDataset {

        public LAION_100k_GHP_50_192Dataset() {
            super("laion2B-en-clip768v2-n=100k.h5_GHP_50_192");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_192";
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_192";
        }
    }

    public static class LAION_300k_GHP_50_192Dataset extends FSHammingSpaceDataset {

        public LAION_300k_GHP_50_192Dataset() {
            super("laion2B-en-clip768v2-n=300k.h5_GHP_50_192");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_192";
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_192";
        }

    }

    public static class LAION_10M_GHP_50_192Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_10M_GHP_50_192Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=10M.h5_GHP_50_192");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_192";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_192";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_192";
        }
    }

    public static class LAION_30M_GHP_50_192Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_30M_GHP_50_192Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=30M.h5_GHP_50_192");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_192";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_192";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_192";
        }

    }

    public static class LAION_100M_GHP_50_192Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_100M_GHP_50_192Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100M.h5_GHP_50_192");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_192";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_192";
        }

    }

    public static class LAION_100k_GHP_50_256Dataset extends FSHammingSpaceDataset {

        public LAION_100k_GHP_50_256Dataset() {
            super("laion2B-en-clip768v2-n=100k.h5_GHP_50_256");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_256";
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_256";
        }

    }

    public static class LAION_300k_GHP_50_256Dataset extends FSHammingSpaceDataset {

        public LAION_300k_GHP_50_256Dataset() {
            super("laion2B-en-clip768v2-n=300k.h5_GHP_50_256");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_256";
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_256";
        }
    }

    public static class LAION_10M_GHP_50_256Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_10M_GHP_50_256Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=10M.h5_GHP_50_256");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_256";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_256";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_256";
        }

    }

    public static class LAION_30M_GHP_50_256Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_30M_GHP_50_256Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=30M.h5_GHP_50_256");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_256";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_256";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_256";
        }
    }

    public static class LAION_100M_GHP_50_256Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_100M_GHP_50_256Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100M.h5_GHP_50_256");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_256";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_256";
        }

    }

    public static class LAION_100k_GHP_50_384Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_100k_GHP_50_384Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100k.h5_GHP_50_384");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_384";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_384";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_384";
        }

    }

    public static class LAION_300k_GHP_50_384Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_300k_GHP_50_384Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=300k.h5_GHP_50_384");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_384";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_384";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_384";
        }

    }

    public static class LAION_10M_GHP_50_384Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_10M_GHP_50_384Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=10M.h5_GHP_50_384");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_384";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_384";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_384";
        }

    }

    public static class LAION_10M_GHP_50_1024Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_10M_GHP_50_1024Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=10M.h5_GHP_50_1024");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_1024";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_1024";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_1024";
        }

    }

    public static class LAION_30M_GHP_50_384Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_30M_GHP_50_384Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=30M.h5_GHP_50_384");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_384";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_384";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_384";
        }

    }

    public static class LAION_30M_GHP_50_1024Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_30M_GHP_50_1024Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=30M.h5_GHP_50_1024");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_1024";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_1024";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_1024";
        }

    }

    public static class LAION_100M_GHP_50_384Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_100M_GHP_50_384Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100M.h5_GHP_50_384");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_384";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_384";
        }
    }

    public static class LAION_100M_GHP_50_1024Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_100M_GHP_50_1024Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100M.h5_GHP_50_1024");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_1024";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_1024";
        }

    }

    public static class LAION_100k_GHP_50_512Dataset extends FSHammingSpaceDataset {

        public LAION_100k_GHP_50_512Dataset() {
            super("laion2B-en-clip768v2-n=100k.h5_GHP_50_512");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_512";
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_512";
        }

    }

    public static class LAION_300k_GHP_50_512Dataset extends FSHammingSpaceDataset {

        public LAION_300k_GHP_50_512Dataset() {
            super("laion2B-en-clip768v2-n=300k.h5_GHP_50_512");
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_512";
        }

        @Override
        public String getQuerySetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_512";
        }

    }

    public static class LAION_10M_GHP_50_512Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_10M_GHP_50_512Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=10M.h5_GHP_50_512");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_512";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_512";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_512";
        }

    }

    public static class LAION_30M_GHP_50_512Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_30M_GHP_50_512Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=30M.h5_GHP_50_512");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getPivotSetName() {
            return "laion2B-en-clip768v2-n=100M.h5_GHP_50_512";
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_512";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_512";
        }

    }

    public static class LAION_100M_GHP_50_512Dataset extends FSHammingSpaceDataset {

        private final boolean publicQueries;

        public LAION_100M_GHP_50_512Dataset(boolean publicQueries) {
            super("laion2B-en-clip768v2-n=100M.h5_GHP_50_512");
            this.publicQueries = publicQueries;
        }

        @Override
        public String getQuerySetName() {
            if (publicQueries) {
                return "public-queries-10k-clip768v2.h5_GHP_50_512";
            }
            return "private-queries-10k-clip768v2.h5_GHP_50_512";
        }

    }

    public static class Faiss_Clip_100M_PCA256_Candidates extends FSDatasetOfCandidates<float[]> {

        public Faiss_Clip_100M_PCA256_Candidates() {
            super(
                    new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
                    "Faiss_Clip_100M_PCA256_Candidates",
                    new FSNearestNeighboursStorageImpl(),
                    "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k750",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k750-nprobe256",
                    "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000_QueriesSample",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1-k10000-nprobe256");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 64;
        }

    }

    public static class FaissDyn_Clip_100M_PCA256_Candidates extends FSDatasetOfCandidates<float[]> {

        public FaissDyn_Clip_100M_PCA256_Candidates(int faissCands) {
            super(
                    new FSDatasetInstanceSingularizator.LAION_100M_PCA256Dataset(),
                    "Faiss" + faissCands + "_Clip_100M_PCA256_Candidates",
                    new FSNearestNeighboursStorageImpl(),
                    "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k750",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k750-nprobe256",
                    "faiss-100M_CLIP_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000_QueriesSample",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1-k10000-nprobe256");

            setMaxNumberOfCandidatesToReturn(faissCands);
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 32;
        }

    }

    public static class Faiss_DeCAF_100M_Candidates extends FSDatasetOfCandidates<float[]> {

        public Faiss_DeCAF_100M_Candidates() {
            super(
                    new FSDatasetInstanceSingularizator.DeCAF100M_Dataset(),
                    "Faiss_DeCAF_100M_Candidates",
                    new FSNearestNeighboursStorageImpl(),
                    "faiss-100M_DeCAF-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1000-k100000",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000-nprobe1024",
                    "faiss-100M_DeCAF-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1000-k100000_QueriesSample",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k100000-nprobe1024");
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return 64;
        }

    }

    public static class Faiss_DeCAF_100M_PCA256_Candidates extends FSDatasetOfCandidates<float[]> {

        public Faiss_DeCAF_100M_PCA256_Candidates() {
            super(
                    new FSDatasetInstanceSingularizator.DeCAF100M_PCA256Dataset(),
                    "Faiss_DeCAF_100M_PCA256_Candidates",
                    new FSNearestNeighboursStorageImpl(),
                    "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000-nprobe1024",
                    "faiss-100M_DeCAF_PCA256-IVFPQ-tr1000000-cc262144-m32-nbits8-qc1000-k10000_QueriesSample",
                    "query_results-IVFPQ-tr1000000-cc262144-m32-nbits8-qc-1-k10000-nprobe1024");
        }

    }

    public static class FSFloatVectorDataset extends Dataset<float[]> {

        public FSFloatVectorDataset(String datasetName) {
            super(datasetName, new FSMetricSpaceImpl(), new FSMetricSpacesStorage<>(new FSMetricSpaceImpl(), SingularisedConvertors.FLOAT_VECTOR_SPACE));
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return -1;
        }

        @Override
        public Map<Comparable, float[]> getKeyValueStorage() {
            try {
                VMMVStorage storage = ((FSMetricSpacesStorage) metricSpacesStorage).getSingularizatorOfDiskStorage();
                if (storage == null) {
                    try {
                        storage = new VMMVStorage(datasetName, false);
                        ((FSMetricSpacesStorage) metricSpacesStorage).setSingularizatorOfDiskStorage(storage);
                    } catch (Exception e) {
                        return ToolsMetricDomain.getMetricObjectsAsIdDataMap(metricSpace, getMetricObjectsFromDataset());
                    }
                }
                return storage.getKeyValueStorage();
            } catch (MVStoreException ex) {
                return null;
            }
        }

        @Override
        public boolean hasKeyValueStorage() {
            return VMMVStorage.exists(datasetName);
        }

        @Override
        public void deleteKeyValueStorage() {
            VMMVStorage.delete(datasetName);
        }
    }

    public static class H5FloatVectorDataset extends Dataset<float[]> {

        public H5FloatVectorDataset(String datasetName) {
            super(
                    datasetName,
                    new FSMetricSpaceImpl<>(),
                    new H5MetricSpacesStorage<>(new FSMetricSpaceImpl<>(), SingularisedConvertors.FLOAT_VECTOR_SPACE)
            );
        }

        @Override
        public Map<Comparable, float[]> getKeyValueStorage() {
            H5MetricSpacesStorage storage = (H5MetricSpacesStorage) metricSpacesStorage;
            return storage.getAsMap(datasetName);
        }

        @Override
        public boolean hasKeyValueStorage() {
            return true;
        }

        @Override
        public void deleteKeyValueStorage() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return -1;
        }

    }

    public static class FSHammingSpaceDataset extends Dataset<long[]> {

        public FSHammingSpaceDataset(String datasetName) {
            super(
                    datasetName,
                    new FSMetricSpaceImpl<>(),
                    new FSMetricSpacesStorage<>(new FSMetricSpaceImpl<>(), SingularisedConvertors.LONG_VECTOR_SPACE)
            );
        }

        @Override
        public Map<Comparable, long[]> getKeyValueStorage() {
            return ToolsMetricDomain.getMetricObjectsAsIdDataMap(metricSpace, getMetricObjectsFromDataset());
        }

        @Override
        public boolean hasKeyValueStorage() {
            return true;
        }

        @Override
        public void deleteKeyValueStorage() {
        }

        @Override
        public int getRecommendedNumberOfPivotsForFiltering() {
            return -1;
        }

    }

    public static class FSDatasetOfCandidates<T> extends DatasetOfCandidates<T> {

        private static Map<String, VMMVStorage<Comparable[]>> singularizator;

        public FSDatasetOfCandidates(Dataset origDataset, String newDatasetName, QueryNearestNeighboursStoreInterface resultsStorage, String resultFolderName, String directResultFileName, String trainingResultFolderName, String trainingDirectResultFileName) {
            super(origDataset, newDatasetName, resultsStorage, resultFolderName, directResultFileName, trainingResultFolderName, trainingDirectResultFileName);
        }

        @Override
        protected Map<Comparable, Comparable[]> getDiskBasedDatasetOfCandsMap(String datasetName) {
            if (!VMMVStorage.exists(datasetName)) {
                return null;
            }
            if (singularizator == null) {
                singularizator = new HashMap<>();
            }
            if (!singularizator.containsKey(datasetName)) {
                singularizator.put(datasetName, new VMMVStorage<>(datasetName, false));
            }
            return singularizator.get(datasetName).getKeyValueStorage();
        }

        @Override
        protected void materialiseMap(Map<Comparable, Comparable[]> map, String storageName) {
            if (singularizator != null && singularizator.containsKey(storageName)) {
                throw new RuntimeException("The dataset " + storageName + " has a disk based representation already");
            }
            VMMVStorage<Comparable[]> vmmvStorage = new VMMVStorage<>(storageName, true);
            vmmvStorage.insertObjects(map);
            if (singularizator == null) {
                singularizator = new HashMap<>();
            }
            singularizator.put(storageName, vmmvStorage);
        }

        @Override
        public boolean hasKeyValueStorage() {
            return VMMVStorage.exists(datasetName);
        }
    }
}

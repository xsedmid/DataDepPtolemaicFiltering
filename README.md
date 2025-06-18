# DataDepPtolemaicFiltering
The objective of this repository is to reproduce the experiments of the "Data-Dependent Ptolemaic Filtering for High-Dimensional Vector Databases" paper.

## Reproducibility Steps

* Clone the three Java-based projects (VMFSMetricSpace, VMGenericTools, VMMetricSpaceTechniquesImpl)
* Change the path in method ```vm.fs.FSGlobal/initRoot()``` (project VMFSMetricSpace) to provide the path to the folder 'Similarity_search'.
<!--* If interested only in synthetical data, create somewhere an empty folder Similarity_search and update ```vm.fs.FSGlobal/initRoot()``` to provide it. If you want to also test datasets of image embeddings and not only the artificial vectors (synthetical data), download the folder '[Similarity_search](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search.zip)' (~24 GB) and store it locally on your system -->

### Reproducibility Steps -- Synthetical Data

* Set up the main Java class in ```vm.fs.papers.impl.main.icde2025.DataDepPtolemaicFiltering.ICDE25DataDepPtolemaicFiltering``` (project VMFSMetricSpace).
* By running the main Java class in ```vm.fs.papers.impl.main.icde2025.DataDepPtolemaicFiltering.ICDE25DataDepPtolemaicFiltering``` (project VMFSMetricSpace), vectors of uniformly distributed numbers are generated, filtering techniques are learned, the learned techniques are evaluated, and plots are generated from the evaluated results.
* The class produces results that are consistent with the article. pdf files with the plots are stored in the folder Similarity_search/Plots/ICDE25_DataDepPtolemaicFilering/
* Results of the search in .gz format and the csv file with statistics are stored in respective folders in Similarity_search/Result/ ... The statistics are stored in the folder Processed_stats

### Reproducibility Steps -- PKU-MMD / DeCAF Datasets

* We recommend to first run the demo with the random vectors that creates the structure of folders in the Similarity_search folder (defined in the FSGlobal class).
* Download the corresponding datasets you would like to evaluate (see the download links below)
* Store the files to proper folders: Similarity_search\Dataset\Dataset\; Similarity_search\Dataset\Pivot\ Similarity_search\Dataset\Query\
* Set up and run the main Java class ```vm.fs.main.datatools.FSPrepareNewDatasetForPivotFilterings``` (project VMFSMetricSpace) by selecting the datasets to be tested. The instances of datasets tested in the article are: 
  * new FSDatasetInstances.DeCAFDataset()
  * new FSDatasetInstances.MOCAP10FPS()
  * new FSDatasetInstances.MOCAP30FPS()
* Set up and run the main Java class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` (project VMFSMetricSpace) by selecting the datasets to be tested.

<!--
### Reproducibility Steps -- CLIP / Yahoo Datasets (Query Candidates Identified by FAISS)

* If not downloaded yet, download the corresponding datasets and key-value storages from the previous section
* Set up the main Java class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` (project VMFSMetricSpace) by selecting the datasets to be tested. The instances of datasets tested in the article are: 
  * new FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates()
  * new FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_Candidates()       (... corresponds to the Yahoo_96M dataset)
* Define these datasets in the main class ```vm.fs.main.search.perform.FSKNNQueriesSeqScanWithFilteringMain``` and run it.
-->  
## Datasets
<!--* 10M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/laion2B-en-clip768v2-n=10M.h5_PCA256.gz) (~13 GB)
* 102M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/laion2B-en-clip768v2-n=100M.h5_PCA256.gz) (~128 GB)--> 
* 1M DeCAF 4,096-D vectors: Dataset: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/decaf_1m.gz) (~7 GB)
* 1M DeCAF 4,096-D vectors: Queries: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Query/decaf_1m.gz) (~7 MB)
* 1M DeCAF 4,096-D vectors: Pivots: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Pivot/decaf_1m.gz) (~17 MB)

* 17K PKU-MMD_10fps MoCAP data: please reach out to us at v.mic11111@gmail.com. The data cannot be provided online due to copyright.
* 17K PKU-MMD_30fps MoCAP data: please reach out to us at v.mic11111@gmail.com. The data cannot be provided online due to copyright.
<!--* 96M Yahoo 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/decaf_100m.gz) (~559 GB)--> 

<!--## Key-value Storages
Due to the limited size of main memory, the following key-value storages manage the content (coordinates) of dataset vectors within secondary storage, while IDs of objects are kept in main memory.
* Key-value storage of 102M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/MV_storage/laion2B-en-clip768v2-n=100M.h5_PCA256) (~103 GB)
* Key-value storage of 96M Yahoo 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/MV_storage/decaf_100m) (~770 GB)
--> 

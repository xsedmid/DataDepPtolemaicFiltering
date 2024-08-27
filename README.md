# DataDepPtolemaicFiltering
The objective of this repository is to reproduce the experiments of the "Data-Dependent Ptolemaic Filtering for High-Dimensional Vector Databases" paper.

## Reproducibility Steps

* Clone the thee Java-based projects (VMFSMetricSpace, VMGenericTools, VMMetricSpaceTechniquesImpl)
* Download the folder '[Similarity_search](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search.zip)' (~24 GB) and store it locally on your system
* Change the path in method ```vm.fs.FSGlobal/initRoot()``` (project VMFSMetricSpace) to contain the full path to the downloaded 'Similarity_search' folder
* Set up the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace) based on a given experiment configuration (see 3 types of reproducibility steps below)
* By running the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace), datasets are loaded or synthetical data can be generated, filtering techniques are learned, the learned techniques are evaluated, and plots are generated from the evaluated results.

### Reproducibility Steps -- Synthetical Data

* Set up the main Java class ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace) by:
  * ...
* Run the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace)

### Reproducibility Steps -- CLIP / DeCAF Datasets (Random Query Candidates)

* Download the corresponding datasets you would like to evaluate (see the download links below)
* Download the corresponding key-value storages of the downloaded datasets to be able to read a vector's content based on its ID (see the download links below)
* Set up the main Java class ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace) by:
  * ...
  * ...
* Run the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace)

### Reproducibility Steps -- CLIP / DeCAF Datasets (Query Candiates Identified by FAISS)

* If not downloaded yet, download the corresponding datasets and key-value storages from the previous section
* Set up the main Java class ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace) by:
  * ...
  * Using the corresponding FAISS index folder 'Similarity_search/Result' to read the candidates identified by FAISS by changing...:
    * Use ```vm.fs.dataset.FSDatasetInstanceSingularizator.Faiss_Clip_100M_PCA256_Candidates``` (project VMFSMetricSpace) to...
    * Use ```vm.fs.dataset.FSDatasetInstanceSingularizator.Faiss_DeCAF_100M_Candidates``` (project VMFSMetricSpace) to...
  * ...
* Run the main Java class in ```vm.fs.papers.impl.main.vldb2024.VLDB24DataDepPtolemaicFiltering``` (project VMFSMetricSpace)

## Datasets
* 10M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/laion2B-en-clip768v2-n=10M.h5_PCA256.gz) (~13 GB)
* 102M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/laion2B-en-clip768v2-n=100M.h5_PCA256.gz) (~128 GB)
* 1M DeCAF 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/decaf_1m.gz) (~7 GB)
* 100M DeCAF 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/Dataset/decaf_100m.gz) (~559 GB)

## Key-value Storages
Due to a limited size of main memory, the following key-value storages manage the content (coordinates) of dataset vectors within secondary storage, while IDs of objects are kept in main memory.
* Key-value storage of 102M CLIP vectors reduced by PCA to 256-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/MV_storage/laion2B-en-clip768v2-n=100M.h5_PCA256) (~103 GB)
* Key-value storage of 100M DeCAF 4,096-D vectors: [download](https://disa.fi.muni.cz/~xmic/DataDepPtolemaicFiltering/Similarity_search/Dataset/MV_storage/decaf_100m) (~770 GB)

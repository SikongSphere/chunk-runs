[**中文**](./README.md) | [**English**](./README.en.md)

# ChunkRuns：A Toolkit for Massive Urban Data Query and Analysis Based on Distributed Computing System

<p align="center">
  <a href="#"><img align="center" width="100%" src="docs/img/logo.png"></img></a>
</p>

[![Code License](https://img.shields.io/badge/Code%20License-Apache_2.0-green.svg)](LICENSE) <a href='https://gitee.com/sikongsphere/chunk-runs/stargazers'><img src='https://gitee.com/sikongsphere/chunk-runs/badge/star.svg?theme=dark' alt='star'></img></a>

## Introduction

**ChunkRuns** is a toolkit for massive urban data query and analysis based on distributed computing system. Based on the open source geospatial data management platform GeoMesa, it provides efficient distributed storage, indexing, querying and analytical computing capabilities for large-scale IFC data.

#### Features

ChunkRuns provides the following functions:

 - **BIM data storage**: Support bulk import of IFC files, construct spatial division markers and store them on distributed column storage Hbase database.
 - **Spatial Index**: Construct spatial index according to the location of the build in 3D space to provide efficient spatial query capability.
 - **BIM Query**: Support query by name or by type, support spatial query, support combined query of spatial query and attribute query.

### Architecture Overview

The architecture of ChunkRuns is shown in the following figure:

<p align="center">
  <img align="center" width="70%" src="docs/img/architecture-overview.png"></img>
</p>

The architecture of ChunkRuns consists of three main parts:

 - **IFC Parsering**：[Sikongsphere-ifctools](https://gitee.com/sikongsphere/sikongsphere-ifctools)is a full implementation of the IFC standard open source model library. Parsing IFC files through *ifctools* to extract the component information from the model.
 - **Data Storage**：HBase is a distributed, column-oriented open source database that runs on top of HDFS to provide efficient storage and access to massive amounts of data. HBase acts as a data store, organizing data with spatial division identification codes as the primary key.
 - **Data Index**：ChunkRuns uses GeoMesa to assist in building spatial indexes, providing efficient indexing and querying capabilities for massive amounts of BIM data.

## Contributing

Please review the [Contribution Guide]() for information on how to get started contributing to the project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](./LICENSE) file for details.

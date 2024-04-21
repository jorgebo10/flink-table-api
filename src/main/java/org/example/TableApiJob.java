/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example;

import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

//2 ways of deployment:
// 1. Framework style: The job is packed in a jar and submitted by a client to a running service
// 2. Library style: The Flink app and the job are bundled in an application-specific container image, such as Docker.
public class TableApiJob {

    public static void main(String[] args) {

        //This is the entrypoint for any Flink application with local web ui
        //StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        //This is the entrypoint for any Flink application. Exec env will be automatically selected either local or remote
        //StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        //This is the entrypoint for any Flink application.Will deploy to a running cluster
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.createRemoteEnvironment(
                "localhost",
                8081
        );

        //This is the entry point for Table API and SQL integration
        TableEnvironment tableEnvironment = StreamTableEnvironment.create(executionEnvironment);

        //Creates an in memory table from an in memory data source
        tableEnvironment.createTemporaryTable("SourceTable", TableDescriptor.forConnector("datagen")
                .schema(Schema.newBuilder()
                        .column("f0", DataTypes.INT())
                        .column("f1", DataTypes.INT())
                        .build())
                .option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
                .build());

        Table result = tableEnvironment.from("SourceTable").select($("f0"));

        tableEnvironment.createTemporaryTable("SinkTable", TableDescriptor.forConnector("print")
                .schema(Schema.newBuilder()
                        .column("f0", DataTypes.INT())
                        .build())
                .build());

        TablePipeline pipeline = result.insertInto("SinkTable");
        pipeline.printExplain();
        pipeline.execute();
    }
}
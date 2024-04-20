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

import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.File;

import static org.apache.flink.table.api.Expressions.$;

public class TableApiJob {

    public static void main(String[] args) {
        //We create first a stream "execution environment" as we can create a web ui with it
        Configuration conf = new Configuration();
        conf.setString("security.basic.auth.enabled", "true");
        conf.setString("security.basic.auth.client.credentials", "testusr:$apr1$w7MhlTpg$r1Lx2b8S21.Y97ohCvNTj/");

        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        //We create a "table environment" with the previously "web execution environment"
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
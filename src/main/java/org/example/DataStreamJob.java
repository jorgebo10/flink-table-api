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

import org.apache.flink.table.api.*;

import java.io.File;

import static org.apache.flink.table.api.Expressions.$;

public class DataStreamJob {

    public static void main(String[] args) {

        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.inStreamingMode());

        final Schema schema = Schema.newBuilder().column("a", DataTypes.INT()).column("b", DataTypes.STRING()).column("c", DataTypes.BIGINT()).build();

        //Describes a source of data, e.g. a filesystem
        TableDescriptor tableDescriptor = TableDescriptor
                .forConnector("filesystem")
                .schema(schema)
                .option("path", getAbsolutePath("src/main/resources/source.csv"))
                .format(FormatDescriptor.forFormat("csv")
                        .option("field-delimiter", ",")
                        .build())
                .build();

        //We create a table from a file source connector which has some options to affect how rows are created
        //We could have created a table from other external source such as kafka or from memory by selecting the
        //correct table descriptor connector
        tableEnvironment.createTemporaryTable("SourceTable", tableDescriptor);

        //virtual table created from query as results. AKA views in sql jergon
        //We might have used the SQL api e.g. tableEnv.executeSql("CREATE [TEMPORARY] TABLE MyTable (...) WITH (...)");
        Table result = tableEnvironment.from("SourceTable").select($("a"));

        // Create a sink table (using SQL DDL instead of Table API as done before)
        tableEnvironment.executeSql("CREATE TEMPORARY TABLE SinkTable(A INT) WITH " + "('connector' = 'filesystem'," + " 'path' = '" + getAbsolutePath("src/main/resources") + "'," + " 'format' = 'json')");

        TablePipeline pipeline = result.insertInto("SinkTable");
        pipeline.printExplain();
        pipeline.execute();
    }

    private static String getAbsolutePath(String pathname) {
        return new File(pathname).getAbsolutePath();
    }

    private static String pathFromResourceFolder(String name) {
        return DataStreamJob.class.getClassLoader().getResource(name).getPath();
    }
}
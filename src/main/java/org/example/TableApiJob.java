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
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;

import static org.apache.flink.streaming.api.windowing.time.Time.minutes;
import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.lit;

public class TableApiJob {

    public static void main(String[] args) {

        //This is the entrypoint for any Flink application. Exec env will be automatically selected either local or remote
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        //This is the entry point for Table API and SQL and Stream integration
        TableEnvironment tableEnvironment = StreamTableEnvironment.create(executionEnvironment);

        //Creates an in memory table from an in memory data source
        tableEnvironment.createTemporaryTable("SourceTable", TableDescriptor.forConnector("datagen")
                .schema(Schema.newBuilder()
                        .columnByExpression("proc_time", "PROCTIME()")
                        .column("item", DataTypes.STRING())
                        .column("quantity", DataTypes.INT())
                        .build())
                .option(DataGenConnectorOptions.ROWS_PER_SECOND, 1L)
                .build());

        Table sourceTable = tableEnvironment.from("SourceTable");

        Table result = sourceTable
                .window(Tumble.over(lit(1).minutes())
                        .on($("proc_time"))
                        .as("fiveMinutesWindow"))
                .groupBy($("fiveMinutesWindow"), $("item"))
                .select($("item"), $("fiveMinutesWindow").end().as("hour"), $("quantity").avg().as("avgBillingAmount"));

        sourceTable.printSchema();
        result.execute().print();
    }
}
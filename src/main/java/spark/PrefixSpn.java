///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package spark;
//
//import org.apache.spark.ml.fpm.PrefixSpan;
//import org.apache.spark.sql.DataFrameReader;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Encoders;
//import org.apache.spark.sql.Row;
//import org.apache.spark.sql.RowFactory;
//import org.apache.spark.sql.SQLContext;
//import org.apache.spark.sql.SparkSession;
//import org.apache.spark.sql.types.*;
//import org.spark_project.dmg.pmml.Array;
//
//import au.com.bytecode.opencsv.CSVReader;
//import scala.collection.Seq;
//
//import static org.apache.spark.sql.functions.array;
//import static org.apache.spark.sql.functions.size;
//import static org.apache.spark.sql.functions.split;
//
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.apache.spark.sql.functions.max;
//import static org.apache.spark.sql.functions.explode;
//import org.apache.spark.api.java.*;
//import org.apache.spark.api.java.function.*;
//
//public class PrefixSpn implements Function<String, Row> {
//  public static void main(String[] args) {
//	  
//    SparkSession spark = SparkSession
//      .builder()
//      .appName("PrefixSpan")
//      .config("spark.master", "local[*]")
//      .getOrCreate();
//
//    String path ="C:\\\\Users\\\\me\\\\Desktop\\\\out\\\\spark\\\\path.csv";
//   
//    StructType schema = new StructType(new StructField[]{ new StructField(
//  		  "sequence", new ArrayType(new ArrayType(DataTypes.StringType, true), true), false, Metadata.empty())
//  		});
//   
// 
//            JavaRDD<String> csvFile = spark.read().textFile(path).toJavaRDD().filter(s -> s.contains(","));
//            List<Row> csvData = csvFile.map(new PrefixSpn()).collect();
//            Dataset<Row> df = spark.createDataFrame(csvData, schema);
//            df.show();
//            //using df.as
////            List<String> listOne = df.as(Encoders.STRING()).collectAsList();
////            System.out.println(listOne);
//            //using df.map
////            List<String> listTwo = df.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
////            System.out.println(listTwo);
////            Dataset<Row> sequenceDF = spark.createDataset(csvData, schema);
//            System.out.println("This prints the total count " + csvData);
//            PrefixSpan prefixSpan = new PrefixSpan().setMinSupport(0.4).setMaxPatternLength(10);
//            
//        	// Finding frequent sequential patterns
//        	prefixSpan.findFrequentSequentialPatterns(df).show();
//            spark.stop();
//        }
//    
//  
//
//  public Row call(String line) throws Exception {
//      CSVReader reader = new CSVReader(new StringReader(line));
//     String[] l = reader.readNext();
//     List<String> strAry = Arrays.asList(l);
//     List<List<String>> m = new ArrayList<List<String>>();
//     strAry.forEach(a -> m.add(Arrays.asList(a)));
//     return RowFactory.create(m);
//     
////      return Arrays.asList(Arrays.asList(reader.readNext()));
////      return reader.readNext();
//  }
//  
////  public List<List<String>> call(String line) throws Exception {
////      CSVReader reader = new CSVReader(new StringReader(line));
////     String[] l = reader.readNext();
////     List<String> strAry = Arrays.asList(l);
////     List<List<String>> m = new ArrayList<List<String>>();
////     strAry.forEach(a -> m.add(Arrays.asList(a)));
////     return m;}
//}
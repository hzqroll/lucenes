package com.roll.lucenes.index;

import com.roll.lucenes.ik.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * 创建索引
 *
 * @author zongqiang.hao
 * created on 2018/10/6 下午12:24.
 */
public class IndexDoc {
    public static String testToString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = br.readLine()) != null) {
                result.append(System.lineSeparator() + str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String args[]) throws IOException {
        File newsFile = new File("/Users/roll/demos/lucenes/news.txt");
        String text1 = testToString(newsFile);
        Analyzer smcAnalyzer = new IKAnalyzer6x(true);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(smcAnalyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //索引存储路径
        Directory directory = null;
        //索引的增删改有indexWrite创建
        IndexWriter indexWriter = null;
        directory = FSDirectory.open(Paths.get("indexdir"));
        indexWriter = new IndexWriter(directory, indexWriterConfig);
        //新建fieldTye 用于指定字段索引时的信息
        FieldType type = new FieldType();
        //索引保存文档,词频信息,位子信息,便宜信息
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);//词条化
        Document doc1 = new Document();
        Field field1 = new Field("content", text1, type);
        doc1.add(field1);
        indexWriter.addDocument(doc1);
        indexWriter.close();
        directory.close();
    }
}

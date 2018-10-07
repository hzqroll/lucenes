package com.roll.lucenes.index;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author zongqiang.hao
 * created on 2018/10/6 下午12:58.
 */
public class GetTopTerms {
    //获取top-n
    public static void getTopN(List<Map.Entry<String, Integer>> sortMap, int N) {
        for (int i = 0; i < N; i++) {
            System.out.println(sortMap.get(i).getKey() + ":" + sortMap.get(i).getValue());
        }
    }

    public static void main(String args[]) throws IOException {
        Directory directory = FSDirectory.open(Paths.get("indexdir"));
        IndexReader reader = DirectoryReader.open(directory);
        Terms terms = reader.getTermVector(0, "content");
        TermsEnum termsEnum = terms.iterator();
        Map<String, Integer> map = new HashMap<>();
        BytesRef thisTerm;
        while ((thisTerm = termsEnum.next()) != null) {
            String termText = thisTerm.utf8ToString();
            //通过totalTermFrep方法获取词项频率
            map.put(termText, (int) termsEnum.totalTermFreq());
        }
        List<Map.Entry<String, Integer>> sortedMap = new ArrayList<>(map.entrySet());
        sortedMap.sort((o1, o2) -> (o2.getValue() - o1.getValue()));
        getTopN(sortedMap, 10);
    }
}

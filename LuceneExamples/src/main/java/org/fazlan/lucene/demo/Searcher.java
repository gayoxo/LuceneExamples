package main.java.org.fazlan.lucene.demo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;

import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Searcher {

    private IndexSearcher searcher;
    private QueryParser titleQueryParser;
    private QueryParser contentQueryParser;

    public Searcher(String indexDir) throws IOException {
        // open the index directory to search
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexDir))));
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // defining the query parser to search items by title field.
        titleQueryParser = new QueryParser(IndexItem.TITLE, analyzer);

        // defining the query parser to search items by content field.
        contentQueryParser = new QueryParser( IndexItem.CONTENT, analyzer);
    }

    /**
      * This method is used to find the indexed items by the title.
      * @param queryString - the query string to search for
      */
    public List<IndexItem> findByTitle(String queryString, int numOfResults) throws ParseException, IOException {
        // create query from the incoming query string.
        Query query = titleQueryParser.parse(queryString);
        // execute the query and get the results
        ScoreDoc[] queryResults = searcher.search(query, numOfResults).scoreDocs;

        List<IndexItem> results = new ArrayList<IndexItem>();
        // process the results
        for (ScoreDoc scoreDoc : queryResults) {
            Document doc = searcher.doc(scoreDoc.doc);
            results.add(new IndexItem(Long.parseLong(doc.get(IndexItem.ID)), doc.get(IndexItem.TITLE), doc.get(IndexItem
                    .CONTENT)));
        }

         return results;
    }

    /**
      * This method is used to find the indexed items by the content.
      * @param queryString - the query string to search for
      */
    public List<IndexItem> findByContent(String queryString, int numOfResults) throws ParseException, IOException {
        // create query from the incoming query string.
        Query query = contentQueryParser.parse(queryString);
         // execute the query and get the results
        ScoreDoc[] queryResults = searcher.search(query, numOfResults).scoreDocs;

        List<IndexItem> results = new ArrayList<IndexItem>();
        // process the results
        for (ScoreDoc scoreDoc : queryResults) {
            Document doc = searcher.doc(scoreDoc.doc);
            results.add(new IndexItem(Long.parseLong(doc.get(IndexItem.ID)), doc.get(IndexItem.TITLE), doc.get(IndexItem
                    .CONTENT)));
        }

         return results;
    }

    public void close() throws IOException {
       // searcher.close();
    }
}

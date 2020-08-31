package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.PrintWriter;

public class GetSpecificEpisodesNewSite {

    public static void main(String[] args) throws IOException {
        String url = "https://animeunity.it/anime/12-one-piece";
        Document document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(500000)
                .get();

        Element animes = document.select("video-player").first();

        String result = animes.attr("anime");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser parser = new JsonParser();
        JsonElement je = parser.parse(result);
        String prettyJson = gson.toJson(je);
        System.out.println(prettyJson);

        PrintWriter printWriter = new PrintWriter("C:\\test\\file_episodes.json");
        printWriter.println(prettyJson);
        printWriter.flush();
        printWriter.close();


    }


}

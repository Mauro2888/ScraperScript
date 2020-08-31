package com.company;

import com.google.gson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataTopFromNewSite {

    static List<String> urls = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String url = "https://www.animeunity.it/top-anime?page=1";
        Document document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(500000)
                .get();

        Element animes = document.select("top-anime").first();

        String result = animes.attr("animes");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String prettyJson = gson.toJson(parsingUrl(result));
        //System.out.println(prettyJson);

        JsonObject jsonObject = gson.fromJson(prettyJson, JsonObject.class);
        int end = Integer.parseInt(String.valueOf(jsonObject.get("last_page")));
        int start = Integer.parseInt(String.valueOf(jsonObject.get("from")));
        for (int i = start; i < end + 1; i++){
            urls.add("https://www.animeunity.it/top-anime?page=" + i);
        }

        for (String i: urls) {
            PrintWriter printWriter = new PrintWriter("C:\\test\\file_episodes.json");
            File file = new File("C:\\test\\tutti.json");
            FileWriter fr = new FileWriter(file, true);
            fr.write(getDataFromUrl(i));
            fr.close();
        }

    }

    public static String getDataFromUrl(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(500000)
                .get();

        String animes = document.select("top-anime").first().attr("animes");

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String prettyJson = gson.toJson(parsingUrl(animes));

        return prettyJson;
    }


    public static JsonElement parsingUrl(String url){
        return new JsonParser().parse(url);
    }


}

package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        final ArrayList<String> arrayList_urls = new ArrayList<>();
        final ArrayList<String> arrayList_titles = new ArrayList<>();
        final ArrayList<String> arrayList_plots = new ArrayList<>();
        final ArrayList<String> arrayList_generes = new ArrayList<>();
        final ArrayList<String> arrayList_mangakas = new ArrayList<>();

        String url = "https://animeunity.it/anime.php?c=archive&page=*";

        Document document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();

        Elements urls = document.select("a");
        Elements titles = document.select("h6.card-title");
        Elements mangakas = document.select("p.card-text.text-secondary");
        Elements plots = document.select("p.card-text.archive-plot");
        Elements generes = document.select("span.badge.btn-archive-genres");

        for (Element title : titles) {
            arrayList_titles.add(title.text());
        }

        for (Element mangaka : mangakas) {
            arrayList_mangakas.add(mangaka.text());
        }

        for (Element plot : plots) {
            arrayList_plots.add(plot.text());
        }

        for (Element genere : generes) {
            arrayList_generes.add(genere.text());
        }

        for (Element url_titles : urls) {
            String allUrls_final = url_titles.attr("href");
            if (allUrls_final.contains("anime.php?id")) {
                arrayList_urls.add(allUrls_final);
            }

        }


        JsonObject object = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < arrayList_titles.size(); i++) {

            JsonObject object1 = new JsonObject();
            object1.addProperty("Titolo",arrayList_titles.get(i));
            object1.addProperty("Url",arrayList_urls.get(i));
            object1.addProperty("Trama",arrayList_plots.get(i));
            object1.addProperty("Mangaka",arrayList_mangakas.get(i));
            object1.addProperty("Genere",arrayList_generes.get(i));
            jsonArray.add(object1);

        }
        System.out.println(arrayList_titles.size() + " " + arrayList_urls.size());
        object.add("Anime",jsonArray);



        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String prettyJson = gson.toJson(object).replace("\\\\", "\\");

        PrintWriter printWriter = new PrintWriter("C:\\test\\" + "file.json");
        printWriter.println(prettyJson);
        printWriter.flush();
        printWriter.close();

    }
}

package com.company;

import com.google.gson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

      Logger logger = Logger.getLogger(Main.class.getSimpleName());

        final ArrayList<String> arrayList_urls = new ArrayList<>();
        final ArrayList<String> arrayList_titles = new ArrayList<>();
        final ArrayList<String> arrayList_plots = new ArrayList<>();
        final ArrayList<String> arrayList_generes = new ArrayList<>();
        final ArrayList<String> arrayList_mangakas = new ArrayList<>();
        final ArrayList<String> arrayList_imgs = new ArrayList<>();

        final ArrayList<String> arrayList_inCorso_titles = new ArrayList<>();
        final ArrayList<String> arrayList_inCorso_urls = new ArrayList<>();
        final ArrayList<String> arrayList_inCorso_imgs = new ArrayList<>();
        final ArrayList<String> arrayList_inCorso_trama = new ArrayList<>();

        String url = "https://animeunity.it/anime.php?c=archive&page=*";

        Document document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(500000)
                .get();

        String url2 = "https://animeunity.it/anime.php?c=onair";

        Document document2 = Jsoup.connect(url2)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();


        Elements titles_corso = document2.select(".col-md-7.col-sm-7.archive-col>.card-block>.card-title>b");
        Elements imgs_corso = document2.select(".card-img-top.archive-card-img>a>img");
        Elements urls_corso = document2.select(".card-img-top.archive-card-img>a");
        Elements plots_corso = document2.select("p.card-text.archive-plot");

        for (Element title : titles_corso) {
            arrayList_inCorso_titles.add(title.text());
        }


        for (Element img : imgs_corso) {
            arrayList_inCorso_imgs.add(img.attr("abs:src"));
        }

        for (Element url_c : urls_corso) {
            arrayList_inCorso_urls.add(url_c.attr("href"));
        }

        for (Element url_trama : plots_corso) {
            arrayList_inCorso_trama.add(url_trama.text());
        }

        System.out.println(arrayList_inCorso_imgs.size() + arrayList_inCorso_titles.size() + arrayList_inCorso_urls.size());


        Elements urls = document.select("a");
        Elements titles = document.select("h6.card-title");
        Elements mangakas = document.select("p.card-text.text-secondary");
        Elements plots = document.select("p.card-text.archive-plot");
        Elements generes = document.select("span.badge.btn-archive-genres");
        Elements imgs = document.select("img.card-img.archive-card-img");

        for (Element title : titles) {
            arrayList_titles.add(title.text());
        }

        for (Element img : imgs) {
            arrayList_imgs.add(img.attr("abs:src"));
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

        JsonObject all_anime_data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < arrayList_titles.size(); i++) {
            JsonObject object1 = new JsonObject();
            object1.addProperty("Titolo", arrayList_titles.get(i));
            object1.addProperty("Url", arrayList_urls.get(i));
            object1.addProperty("Img_url", arrayList_imgs.get(i));
            object1.addProperty("Trama", arrayList_plots.get(i));
            object1.addProperty("Mangaka", arrayList_mangakas.get(i));
            object1.addProperty("Genere", arrayList_generes.get(i));
            //GET
            jsonArray.add(object1);
        }


        JsonArray inCorsoArray = new JsonArray();
        for (int y = 0; y < arrayList_inCorso_titles.size(); y++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Titolo", arrayList_inCorso_titles.get(y));
            jsonObject.addProperty("Img_url", arrayList_inCorso_imgs.get(y));
            jsonObject.addProperty("Url", arrayList_inCorso_urls.get(y));
            jsonObject.addProperty("Trama",arrayList_inCorso_trama.get(y));
            inCorsoArray.add(jsonObject);
        }


        all_anime_data.add("Anime", jsonArray);
        all_anime_data.add("Anime_in_Corso", inCorsoArray);

        //----------------------------------------------------------------------------------
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String prettyJson = gson.toJson(all_anime_data).replace("\\\\", "\\");

        PrintWriter printWriter = new PrintWriter("C:\\test\\" + "file.json");
        printWriter.println(prettyJson);
        printWriter.flush();
        printWriter.close();
        //----------------------------------------------------------------------------------*/

    }


    private static ArrayList<String> getEpisodes(ArrayList<String> urls) {

        System.out.println("START");
        ArrayList<String> episode_list = new ArrayList<>();
        ArrayList<String> urls_temp = new ArrayList<>();

       urls.forEach(urls_anime -> {
            try {
                Document doc = Jsoup.connect("https://animeunity.it/" + urls_anime)
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(600000)
                        .get();

                Elements titles = doc.select("a");
                for (Element title : titles) {
                    String all = title.attr("href");
                    if (all.contains("&ep") && all.contains("anime.php")) {
                        urls_temp.add(all);
                        System.out.println("EXTRACT " +all);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        urls_temp.forEach(link -> {
            try {
                Document doc = Jsoup.connect("https://animeunity.it/" + link)
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(600000)
                        .get();
                Elements basic = doc.select("source");
                for (Element i : basic) {
                    episode_list.add(i.attr("src"));
                    System.out.println("EXTRACT EPISODE " + i.attr("src"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




        return episode_list;
    }
}

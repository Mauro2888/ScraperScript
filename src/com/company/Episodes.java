package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Episodes {
    public static void main(String[] args) throws IOException {

        final ArrayList<String> arrayList_episodes = new ArrayList<>();
        final ArrayList<String> arrayList_episodes_final = new ArrayList<>();

        String url2 = "https://animeunity.it/anime.php?id=12";

        Document document2 = Jsoup.connect(url2)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();


        Elements titles = document2.select("a");
        for (Element title : titles) {
            String all = title.attr("href");
            if (all.contains("&ep") && all.contains("anime.php")) {
                arrayList_episodes.add(all);
            }
        }


        arrayList_episodes.forEach(link -> {
            try {
                Document doc = Jsoup.connect("https://animeunity.it/"+link).get();
                Elements basic = doc.select("source");
                for (Element i: basic) {
                    arrayList_episodes_final.add(i.attr("src"));
                    System.out.println(i.attr("src"));
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}

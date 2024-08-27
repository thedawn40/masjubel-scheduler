package masjubel.platform.config;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SilverPriceFetcher {

    public static void main(String[] args) {
        String url = "https://www.silvergram.co.id/portal/product.do?cat=silver";
        try {
            // Fetch the HTML content from the URL
            Document document = Jsoup.connect(url).get();

            // Parse and extract the relevant information
            // Elements priceElements = document.select("div.desc-detail"); // Adjust the selector based on the actual HTML structure
            Elements priceElements = document.select("div#product-content");

            String title = document.title(); 
            System.out.println(title+" "+priceElements.size());
            for (Element priceElement : priceElements) {
                System.out.println(priceElement.getElementsByTag("div"));
                String productName = priceElement.select("div.product-name").text();    
                String price = priceElement.select("div.product-price").text();
                System.out.println("Product: " + productName + " | Price: " + price);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

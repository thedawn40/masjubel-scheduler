package masjubel.platform.config;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import masjubel.platform.entity.Harga;

public class GoldPriceFetcher {

    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static Harga antamPriceFetch(){
        Harga result = new Harga();
        Date date = new Date(); // Replace with your date object

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String formattedDate = formatter.format(date);
        result.setTanggal(formattedDate);
        try {
            // Connect to the website and get the HTML document
            Document doc = Jsoup.connect("https://www.logammulia.com/id/harga-emas-hari-ini").get();

            String title = doc.title(); 
            System.out.println(title);

            Elements pricElements = doc.getElementsByTag("tr");
            List<Map<String,Object>> priceList = new ArrayList<>();
            boolean isActive = false;
            for(Element p : pricElements){
                int total = p.getElementsByTag("td").size();
                if(total>0){
                    isActive = true;
                    List<String> items  = new ArrayList<String>();
                    for(Element o : p.getElementsByTag("td")){       	
                        items.add(o.text().replace(",", ""));
                    }
                    String weight = items.get(0);
                    String price = items.get(1);
                    String pricePph = items.get(2); //+pph 0.25%
                    String priceMasjubel = priceMasjubel(weight, pricePph);
                    Map<String,Object> item  = new HashMap<>();
                    item.put("weight", weight);
                    item.put("price", price);
                    item.put("pricePph", pricePph);
                    item.put("priceNew", priceMasjubel);

                    System.out.println("Weight: " + weight + " - Price: " + pricePph+" "+priceMasjubel);
                    priceList.add(item);                
                    
                    if(weight.equalsIgnoreCase("0.5 gr")){
                        result.setHargaSatu(priceMasjubel);                    
                    }
                    if(weight.equalsIgnoreCase("1 gr")){
                        result.setHargaDua(priceMasjubel);
                    }
                    if(weight.equalsIgnoreCase("2 gr")){
                        result.setHargaTiga(priceMasjubel);
                    }
                    if(weight.equalsIgnoreCase("3 gr")){
                        result.setHargaEmpat(priceMasjubel);
                    }
                    if(weight.equalsIgnoreCase("5 gr")){
                        result.setHargaLima(priceMasjubel);
                    }
                    if(weight.equalsIgnoreCase("10 gr")){
                        result.setHargaEnam(priceMasjubel);
                    }
                }
                if(total==0 && isActive){
                    break;
                }
            }
            // Convert list to JSONArray
            JSONArray jsonArray = new JSONArray(priceList);

            // Print JSON output
            System.out.println(jsonArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String priceMasjubel(String weight, String price){
        String result = "";
        try {
            Long newPrice = null;
            weight = weight.replace(" gr", "");
            if(weight.equalsIgnoreCase("0.5")||
            weight.equalsIgnoreCase("1")||
            weight.equalsIgnoreCase("2")||
            weight.equalsIgnoreCase("3")){
                newPrice = Long.valueOf(price)+25000;
            }else if(weight.equalsIgnoreCase("5")||
            weight.equalsIgnoreCase("10")){
                newPrice = Long.valueOf(price)+50000;
            }
            result = roundToNearestThousand(newPrice);
        } catch (Exception e) {
        }
        return result;
    }

    public static String formatNumber(String numberStr) {
        int length = numberStr.length();
        
        if (length > 3) {
            return numberStr.substring(0, length - 3) + "." + numberStr.substring(length - 3);
        } else {
            return numberStr;
        }
    }

    public static String roundToNearestThousand(Long price) {
        Long number = price;
        Long roundedNumber = (long) (Math.ceil(number / 1000.0) * 1000);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedAmount = formatter.format(roundedNumber);

        return formattedAmount;
    }

    public static void main(String[] args) {
       GoldPriceFetcher.antamPriceFetch();
    }
}

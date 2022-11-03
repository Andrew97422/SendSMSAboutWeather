package Project.service;

import Project.model.ModelSMS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ServiceSMS {
    /*Main logic*/

    @Value("${URL1}")
    private String URL1;

    @Value("${URL2}")
    private String URL2;
    Document html1;
    Document html2;
    String[] weathersToday;
    String[] weathersTomorrow;
    String[] rainToday;
    String[] rainTomorrow;
    String[] windToday;
    String[] windTomorrow;
    @Bean
    public ModelSMS create() throws IOException {
        html1 = Jsoup.connect(URL1).get();
        html2 = Jsoup.connect(URL2).get();

        weathersToday = new String[8];
        weathersTomorrow = new String[8];
        rainToday = new String[8];
        rainTomorrow = new String[8];
        windToday = new String[8];
        windTomorrow = new String[8];

        for (int i = 0; i < 8; i++) {
            Elements e = html1.select("body > section.content.wrap > div.content-column.column1 > section:nth-child(3) > div > div > div > div.widget-row-chart.widget-row-chart-temperature > div > div > div:nth-child(" + (i + 1) + ") > span.unit.unit_temperature_c");
            String weather = e.text().replace("?", "-");
            weathersToday[i] = weather;

            Elements e1 = html2.select("body > section.content.wrap > div.content-column.column1 > section:nth-child(3) > div > div > div > div.widget-row-chart.widget-row-chart-temperature > div > div > div:nth-child(" + (i + 1) + ") > span.unit.unit_temperature_c");
            String weather1 = e1.text().replace("?", "-");
            weathersTomorrow[i] = weather1;

            Elements e2 = html1.select("body > section.content.wrap > div.content-column.column1 > section:nth-child(3) > div > div > div > div.widget-row.widget-row-precipitation-bars.row-with-caption > div:nth-child(" + (i + 2) + ") > div");
            String rain = e2.text().replace("?", "-");
            rainToday[i] = rain;

            Elements e3 = html2.select("body > section.content.wrap > div.content-column.column1 > section:nth-child(3) > div > div > div > div.widget-row.widget-row-precipitation-bars.row-with-caption > div:nth-child(" + (i + 2) + ") > div");
            String rain1 = e3.text().replace("?", "-");
            rainTomorrow[i] = rain1;

            Elements e4 = html1.select("body > section.content.wrap > div.content-column.column1 > section:nth-child(3) > div > div > div > div.widget-row.widget-row-wind-speed-gust.row-with-caption > div:nth-child(" + (i + 2) + ") > span.wind-unit.unit.unit_wind_m_s");
            String wind1 = e4.text();
            windToday[i] = wind1;

            Elements e5 = html2.select("body > section.content.wrap > div.content-column.column1 > section:nth-child(3) > div > div > div > div.widget-row.widget-row-wind-speed-gust.row-with-caption > div:nth-child(" + (i + 2) + ") > span.wind-unit.unit.unit_wind_m_s");
            String wind2 = e5.text();
            windTomorrow[i] = wind2;
        }

        /*Temperature at the day*/
        String tempAtDay;
        int td = 0;
        for (int i = 2; i < 7; i++) {
            td += Integer.parseInt(weathersToday[i]);
        }
        td /= 5;
        tempAtDay = String.valueOf(td);

        /*Temperature at the night*/
        String tempAtNight;
        int tn = 0;
        tn += Integer.parseInt(weathersToday[weathersToday.length - 1]);
        tn += Integer.parseInt(weathersTomorrow[0]);
        tn += Integer.parseInt(weathersTomorrow[1]);
        tn /= 3;
        tempAtNight = String.valueOf(tn);

        /*Rain at day*/
        String rainAtDay;
        int rt = 0;
        for (int i = 2; i < 7; i++) {
            float r;
            r = Float.parseFloat(rainToday[i].replace(",", "."));
            rt += Math.round(r);
        }
        rt /= 5;
        rainAtDay = String.valueOf(rt);
        if (rainAtDay.equals("0")) {
            rainAtDay = "No";
        }
        System.out.println(rainAtDay);

        /*Rain at night*/
        String rainAtNight;
        int rn = 0;
        rn += Integer.parseInt(rainToday[rainToday.length - 1]);
        rn += Integer.parseInt(rainTomorrow[0]);
        rn += Integer.parseInt(rainTomorrow[1]);
        rn /= 3;
        rainAtNight = String.valueOf(rn);
        if (rainAtNight.equals("0")) {
            rainAtNight = "No";
        }
        System.out.println(rainAtNight);

        /*Wind at the day*/
        String windAtDay;
        StringBuilder builder = new StringBuilder();
        List<Integer> porivs = new ArrayList<>();
        int wd = 0;
        for (int i = 2; i < 7; i++) {
            if (windToday[i].length() > 2) {
                int y = Integer.parseInt(String.valueOf(windToday[i].charAt(2)));
                wd += y;
                porivs.add(y);
            } else {
                wd += Integer.parseInt(windToday[i]);
            }
        }

        wd /= 5;
        builder.append(wd);
        if (!porivs.isEmpty()) {
            porivs = porivs.stream().sorted(Comparator.naturalOrder()).toList();
            int max = porivs.get(porivs.size() - 1);
            builder.append("-").append(max);
        }

        windAtDay = builder.toString();
        System.out.println(windAtDay);

        /*Wind at the night*/
        String windAtNight;
        StringBuilder builder1 = new StringBuilder();
        List<Integer> porivs1 = new ArrayList<>();
        int wn = 0;
        if (windToday[7].length() > 2) {
            int y = Integer.parseInt(String.valueOf(windToday[7].charAt(2)));
            wn += y;
            porivs1.add(y);
        } else {
            wn += Integer.parseInt(windToday[7]);
        }

        if (windTomorrow[0].length() > 2) {
            int y = Integer.parseInt(String.valueOf(windTomorrow[0].charAt(2)));
            wn += y;
            porivs1.add(y);
        } else {
            wn += Integer.parseInt(windTomorrow[0]);
        }

        if (windTomorrow[1].length() > 2) {
            int y = Integer.parseInt(String.valueOf(windTomorrow[1].charAt(2)));
            wn += y;
            porivs1.add(y);
        } else {
            wn += Integer.parseInt(windTomorrow[1]);
        }

        wn /= 3;
        builder1.append(wn);
        if (!porivs1.isEmpty()) {
            porivs1 = porivs1.stream().sorted(Comparator.naturalOrder()).toList();
            int max = porivs1.get(porivs1.size() - 1);
            builder1.append("-").append(max);
        }

        windAtNight = builder1.toString();
        System.out.println(windAtNight);

        return ModelSMS.builder()
                .tempAtDay(tempAtDay)
                .tempAtNight(tempAtNight)
                .rainAtDay(rainAtDay)
                .rainAtNight(rainAtNight)
                .windAtDay(windAtDay)
                .windAtNight(windAtNight)
                .build();
    }
}

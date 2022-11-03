package Project.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelSMS {
    private String tempAtDay; //Температура днём
    private String tempAtNight; //Температура ночью
    private String rainAtDay; //Осадки днём
    private String rainAtNight; //Осадки ночью
    private String windAtDay; //Ветер днём
    private String windAtNight; //Ветер ночью

    public String toString() {
        return "Weather today:" +
                "\nDay: " + getTempAtDay() +
                "\nNight: " + getTempAtNight() +
                "\nRain: day - " + getRainAtDay() + " mm night - " + getRainAtNight() + " mm" +
                "\nWind: day - " + getWindAtDay() + " m/c night - " + getWindAtNight() + " m/c";
    }
}

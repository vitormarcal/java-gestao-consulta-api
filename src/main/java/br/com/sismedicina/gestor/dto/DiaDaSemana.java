package br.com.sismedicina.gestor.dto;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public enum DiaDaSemana {

    SEGUNDA(DayOfWeek.MONDAY), TERCA(DayOfWeek.TUESDAY), QUARTA(DayOfWeek.WEDNESDAY), QUINTA(DayOfWeek.THURSDAY), SEXTA(DayOfWeek.FRIDAY), SABADO(DayOfWeek.SATURDAY), DOMINGO(DayOfWeek.SUNDAY);

    private final DayOfWeek dayOfWeek;

    DiaDaSemana(DayOfWeek dayOfWeek) {

        this.dayOfWeek = dayOfWeek;
    }

    public static DayOfWeek getDayOfWeek(String day) {
        DiaDaSemana diaDaSemana = valueOf(day.trim().toUpperCase());
        return diaDaSemana.dayOfWeek;
    }

    public static List<DayOfWeek> getDayOfWeek(String... days) {
        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        for (String day : days) {
            DayOfWeek dayOfWeek = getDayOfWeek(day);
            dayOfWeeks.add(dayOfWeek);
        }
        return dayOfWeeks;
    }
}

package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Titanic {
    

    public static List<String> supervivientes() throws IOException{
        Path ruta = Path.of("data/titanic.csv");
        return Files.readAllLines(ruta).stream().skip(1)
            .filter(x-> x.split(",")[15].trim().equals("1")).toList();
    }

    public static String masComun() throws IOException{
        Path ruta = Path.of("data/titanic.csv");

        List<String> valoresEmbarked = Files.readAllLines(ruta).stream()
            .skip(1) // Saltar la cabecera
            .map(linea -> linea.split(","))
            .map(columnas -> columnas[10].trim())
            .filter(valor -> !valor.isEmpty()) // Ignorar puertos vacíos
            .toList();

        Map<String, Long> frecuencias = valoresEmbarked.stream()
        .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
        ));

        return frecuencias.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElseThrow();
    }

    public static String tasaSupervivenciaPorEdad() throws IOException {
        Path ruta = Path.of("data/titanic.csv");
        List<String> lineas = Files.readAllLines(ruta);
        int ninos = 0, ninosSupervivientes = 0;
        int adultos = 0, adultosSupervivientes = 0;

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] columnas = linea.split(",");
            if (columnas[4].isBlank()) continue;

            boolean esNino = Double.parseDouble(columnas[4]) < 12;
            boolean sobrevivio = columnas[14].trim().equals("1");

            if (esNino) {
                ninos++;
                if (sobrevivio) ninosSupervivientes++;
            } else {
                adultos++;
                if (sobrevivio) adultosSupervivientes++;
            }
        }

        return String.format(Locale.ROOT, "Niños: %.1f%% | Adultos: %.1f%%",
            100.0 * ninosSupervivientes / ninos,
            100.0 * adultosSupervivientes / adultos);
    }

}

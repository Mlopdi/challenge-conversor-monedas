import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Scanner;

public class ConversorMonedaApp {
    private static String apiKey = "e30867bd718962cf8ab1f2bc";

    public static void main(String[] args) throws Exception {
        boolean salir = false;
        Scanner scanner = new Scanner(System.in);

        while (!salir) {

            System.out.println("""
            ***********************************************************
            Bienvenido al conversor de moneda!
            
            1) Dólar (USD) -> Peso Colombiano (COP)
            2) Peso Colombiano (COP) -> Dólar (USD)
            3) Dólar (USD) -> Peso Argentino (ARS)
            4) Peso Argentino (ARS) -> Dólar (USD)
            5) Dólar (USD) -> Real Brasileño (BRL)
            6) Real Brasileño (BRL) -> Dólar (USD)
            7) Salir
            
            Seleccione una opción válida:
            **********************************************************
            """);

            Integer opcion = scanner.nextInt();

            if (!opcion.equals(7)) {
                if (opcion > 0  && opcion < 7) {
                    System.out.println("Ingresa el valor que deseas convertir: ");
                    BigDecimal montoAConvertir = scanner.nextBigDecimal();
                    switch (opcion) {
                        case 1:
                            System.out.println(montoAConvertir.setScale(2, RoundingMode.HALF_UP) + " USD corresponde a " + obtenerDatosConcurrencia("USD", "COP").multiply(montoAConvertir).setScale(2, RoundingMode.HALF_UP) + " COP");
                            break;
                        case 2:
                            System.out.println(montoAConvertir.setScale(2, RoundingMode.HALF_UP) + " COP corresponde a " + obtenerDatosConcurrencia("COP", "USD").multiply(montoAConvertir).setScale(2, RoundingMode.HALF_UP) + " USD");
                            break;
                        case 3:
                            System.out.println(montoAConvertir.setScale(2, RoundingMode.HALF_UP) + " USD corresponde a " + obtenerDatosConcurrencia("USD", "ARS").multiply(montoAConvertir).setScale(2, RoundingMode.HALF_UP) + " ARS");
                            break;
                        case 4:
                            System.out.println(montoAConvertir.setScale(2, RoundingMode.HALF_UP) + " ARS corresponde a " + obtenerDatosConcurrencia("ARS", "USD").multiply(montoAConvertir).setScale(2, RoundingMode.HALF_UP) + " USD");
                            break;
                        case 5:
                            System.out.println(montoAConvertir.setScale(2, RoundingMode.HALF_UP) + " USD corresponde a " + obtenerDatosConcurrencia("USD", "BRL").multiply(montoAConvertir).setScale(2, RoundingMode.HALF_UP) + " BRL");
                            break;
                        case 6:
                            System.out.println(montoAConvertir.setScale(2, RoundingMode.HALF_UP) + " BRL corresponde a " + obtenerDatosConcurrencia("BRL", "USD").multiply(montoAConvertir).setScale(2, RoundingMode.HALF_UP) + " USD");
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.println("Opción inválida, por favor seleccione correctamente una de la lista.");
                }
            } else {
                scanner.close();
                salir = true;
                System.out.println("Saliendo del programa, gracias por utilizar nuestros servicios!.");
            }
        }
    }

    private static BigDecimal obtenerDatosConcurrencia(String monedaActual, String monedaNueva) throws Exception {
        try {
            HttpClient cliente = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + monedaActual))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> respuesta = cliente.send(solicitud, BodyHandlers.ofString());
            JsonObject objetoJson = JsonParser.parseString(respuesta.body()).getAsJsonObject();
            return objetoJson.get("conversion_rates").getAsJsonObject().get(monedaNueva).getAsBigDecimal();
        } catch (Exception excepcion) {
            throw new Exception("Ha ocurrido un error al tratar de obtener los datos de la concurrencia: " + excepcion.getMessage());
        }
    }
}

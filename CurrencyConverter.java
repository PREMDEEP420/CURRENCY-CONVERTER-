import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Step 1: Input base and target currencies
            System.out.print("Enter base currency (e.g., USD): ");
            String baseCurrency = scanner.nextLine().toUpperCase();

            System.out.print("Enter target currency (e.g., EUR): ");
            String targetCurrency = scanner.nextLine().toUpperCase();

            // Step 2: Input amount
            System.out.print("Enter the amount to convert: ");
            double amount = scanner.nextDouble();

            // Step 3: Fetch exchange rate
            double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);

            if (exchangeRate == -1) {
                System.out.println("Error: Unable to fetch exchange rate.");
                return;
            }

            // Step 4: Calculate and display result
            double convertedAmount = amount * exchangeRate;
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, targetCurrency);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static double fetchExchangeRate(String base, String target) {
        String apiKey = "YOUR_API_KEY"; // Replace with your API key
        String apiUrl = String.format("https://api.exchangerate-api.com/v4/latest/%s", base);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getJSONObject("rates").getDouble(target);
            } else {
                System.out.println("HTTP Error: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
        }
        return -1;
    }
}

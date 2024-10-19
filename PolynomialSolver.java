import java.util.*;
import java.io.*;
import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PolynomialSolver {

    // Function to perform Lagrange Interpolation
    public static double lagrangeInterpolation(int[] x, double[] y, int k) {
        double result = 0.0;

        // Apply Lagrange interpolation formula
        for (int i = 0; i < k; i++) {
            double term = y[i];
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term = term * (0 - x[j]) / (x[i] - x[j]);  // L_i(0) part
                }
            }
            result += term;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        // Read the JSON input from a file
        String jsonFilePath = "testcase.json";  // Path to your input file
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject jsonObject = new JSONObject(jsonContent);

        // Extract the keys for n and k
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Arrays to store x and y values
        int[] xValues = new int[k];  // We only need k values for Lagrange interpolation
        double[] yValues = new double[k];

        // Read and decode x and y values from the JSON input
        int count = 0;
        for (int i = 1; i <= n && count < k; i++) {
            String key = String.valueOf(i);
            if (jsonObject.has(key)) {
                // x is the key
                int x = Integer.parseInt(key);

                // Get the base and value
                JSONObject point = jsonObject.getJSONObject(key);
                int base = point.getInt("base");
                String value = point.getString("value");

                // Decode the y value from the given base
                double y = (double) Long.parseLong(value, base);

                // Store x and y in arrays
                xValues[count] = x;
                yValues[count] = y;
                count++;
            }
        }

        // Apply Lagrange interpolation to find the constant term (c)
        double constantTerm = lagrangeInterpolation(xValues, yValues, k);

        // Print the result
        System.out.printf("Constant term (c): %.0f\n", constantTerm);
    }
}

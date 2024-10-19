import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class kk {

    public static void main(String[] args) throws Exception {
        
        String jsonContent = new String(Files.readAllBytes(Paths.get("tt.json")));
        JSONObject jsonObject = new JSONObject(jsonContent);

        
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        
        Map<Integer, BigInteger> points = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                JSONObject valueObj = jsonObject.getJSONObject(key);
                int base = valueObj.getInt("base");
                String value = valueObj.getString("value");
                BigInteger decodedY = new BigInteger(value, base);  
                points.put(x, decodedY);
            }
        }

        
        BigInteger secret = lagrangeInterpolation(points, k);


        System.out.println("The secret is: " + secret);
    }

    
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger secret = BigInteger.ZERO;

        for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
            int xi = entry.getKey();
            BigInteger yi = entry.getValue();

            
            BigInteger li = BigInteger.ONE;
            for (Map.Entry<Integer, BigInteger> otherEntry : points.entrySet()) {
                if (xi != otherEntry.getKey()) {
                    int xj = otherEntry.getKey();
                    li = li.multiply(BigInteger.valueOf(-xj)).divide(BigInteger.valueOf(xi - xj));
                }
            }

            
            secret = secret.add(li.multiply(yi));
        }

        return secret;
    }
}

package com.manankakkar.smartpaint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * OpenAiPaint — Similar to OllamaPaint, but using OpenAI API.
 * It defines a system prompt once and appends user input.
 */
public class OpenAiPaint {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final HttpClient client;
    private String apiKey;
    private final String systemPrompt;  // 🧠 fixed system instructions

    public OpenAiPaint() {
        String apiKey1;
        this.client = HttpClient.newHttpClient();
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
        apiKey1 = dotenv.get("OPENAI_API_KEY");
        if (apiKey1 == null || apiKey1.isEmpty()) {
            apiKey1 = System.getenv("OPENAI_API_KEY");
        }
        this.apiKey = apiKey1;
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("Missing OPENAI_API_KEY environment variable.");
        }
        String format = FileIO.readResourceFile("paintSaveFileFormat.txt");
        String format2 = FileIO.readResourceFile("InvalidPaintSaveFileExample1.txt");
        String format3 = FileIO.readResourceFile("InvalidPaintSaveFileExample2.txt");
        String format4 = FileIO.readResourceFile("InvalidPaintSaveFileExample3.txt");
        String format5 = FileIO.readResourceFile("InvalidPaintSaveFileExample4.txt");
        String format6 = FileIO.readResourceFile("InvalidPaintSaveFileExample5.txt");
        String format7 = FileIO.readResourceFile("InvalidPaintSaveFileExample6.txt");
        String format8 = FileIO.readResourceFile("InvalidPaintSaveFileExample7.txt");
        String format9 = FileIO.readResourceFile("InvalidPaintSaveFileExample8.txt");
        String format10 = FileIO.readResourceFile("InvalidPaintSaveFileExample9.txt");
        String format11 = FileIO.readResourceFile("InvalidPaintSaveFileExample10.txt");
        String format12 = FileIO.readResourceFile("ValidPaintSaveExample1.txt");
        String format13 = FileIO.readResourceFile("ValidPaintSaveExample2.txt");
        String format14 = FileIO.readResourceFile("ValidPaintSaveExample3.txt");
        String format15 = FileIO.readResourceFile("ValidPaintSaveExample4.txt");
        String format16 = FileIO.readResourceFile("ValidPaintSaveExample5.txt");
        String format17 = FileIO.readResourceFile("ValidPaintSaveExample6.txt");
        String format18 = FileIO.readResourceFile("ValidPaintSaveExample7.txt");
        String format19 = FileIO.readResourceFile("ValidPaintSaveExample8.txt");
        String format20 = FileIO.readResourceFile("ValidPaintSaveExample9.txt");
        String format21 = FileIO.readResourceFile("ValidPaintSaveExample10.txt");
        String format22 = FileIO.readResourceFile("ValidPaintSaveExample11.txt");

        this.systemPrompt = "The answer to this question should be a paintSaveFile. " +
                "Respond only with a VALID paintSaveFile Document and nothing else. " +
                "A valid 'paintSaveFile' is defined by the following: " +
                "1. The file must always start with 'Paint Save File Version 1.0' as the first line. " +
                "2. The file must end with 'End Paint Save File' as the last line. " +
                "3. Includes only Circles, Polylines, Squiggles, and Rectangles. " +
                "4. Follows all format rules without additional lines, explanations, or comments. " +
                "5. A center of a circle is defined as a point: 'center:(x,y)' where x,y are positive integers." + "The points of a squiggle/polyline is defined as a point: 'point:(x,y)' are integers." +
                "6. The color of a drawing is defined by: 'color:r,g,b' where r,g,b are RANDOM integers between 0 and 255." +
                "7. The filled attribute of a drawing is defined by: 'filled:bool'" +
                "8. The radius of a circle is defined by: 'radius:r' where r is a positive integer and not a decimal." +
                "9. The first point of a rectangle is defined by: 'p1:(x,y)' where x,y are positive integers." +
                "10. The second point of a rectangle is defined by: 'p2:(x,y)' where x,y are positive integers." +
                "11. The file should always look like the valid examples provided below." +
                "12. Follows all format rules without additional lines, explanations, or comments. " +
                "13. To write points for squiggles and polylines, you must start with 'points' and end writing the points with 'end points' as defined in the examples attached below. Make sure both 'points' and 'end points' have the same tabbing. " +
                "14. Make sure to include ALL attributes for each drawing." +
                "15. Make sure to follow the order seen in the examples attached below for each drawing." +
                "16. Make sure that each point, center, p1, p2 only ALWAYS have positive values for each (x,y). " +
                "17. The points for both squiggles and polylines MUST follow the syntax: 'point:(x,y)' where x,y are positive integers. DO NOT INCLUDE A '.' or '(' in any of the point LINES. " +
                "18. If there are too many points, you must take the smallest point value and the largest points value. Do NOT provide '...'. " +
                "The document must match exactly to the defined formats and include no extra or invalid content. " + "Make sure the tabbing matches the examples." +
                "The document must always follow the guidelines set by: " + format +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + "." + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + "." +
                "Make sure to NOT create duplicates when writing the file." + "Make sure to ALWAYS follow rule 17 and 18 of a 'valid paintSaveFile'.";
    }

    /**
     * Sends the system prompt and user prompt to OpenAI and returns the response content.
     */
    public String call(String userPrompt, String outFileName) throws Exception {
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }

        JsonObject root = new JsonObject();
        root.addProperty("model", "gpt-4o-mini");

        JsonArray messages = new JsonArray();

        JsonObject sysMsg = new JsonObject();
        sysMsg.addProperty("role", "system");
        sysMsg.addProperty("content", systemPrompt);
        messages.add(sysMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userPrompt);
        messages.add(userMsg);

        root.add("messages", messages);

        String body = root.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("OpenAI API Error: " + response.body());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        String content = json.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString()
                .trim();

        StringBuilder cleaned = new StringBuilder();
        for (String line : content.split("\n")) {
            if (!line.trim().isEmpty()) {
                cleaned.append(line).append("\n");
            }
        }

        FileIO.writeHomeFile(cleaned.toString(), outFileName);

        return content;
    }

    public String callWithValidation(String userPrompt, String outFileName, PaintModel paintModel) throws Exception {
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) file.delete();

        Pattern[] patterns = {
                Pattern.compile("^color:\\d+,\\d+,\\d+$"),
                Pattern.compile("^filled:(true|false)$"),
                Pattern.compile("^center:\\((-?\\d+),(-?\\d+)\\)$"),
                Pattern.compile("^radius:(\\d+)$"),
                Pattern.compile("^points$"),
                Pattern.compile("^point:\\((-?\\d+),(-?\\d+)\\)$"),
                Pattern.compile("^end points$"),
                Pattern.compile("^p1:\\((-?\\d+),(-?\\d+)\\)$"),
                Pattern.compile("^p2:\\((-?\\d+),(-?\\d+)\\)$"),
                Pattern.compile("^Paint Save File Version 1.0$"),
                Pattern.compile("^End Paint Save File$"),
                Pattern.compile("^Circle$"),
                Pattern.compile("^End Circle$"),
                Pattern.compile("^Rectangle$"),
                Pattern.compile("^End Rectangle$"),
                Pattern.compile("^Polyline$"),
                Pattern.compile("^End Polyline$"),
                Pattern.compile("^Squiggle$"),
                Pattern.compile("^End Squiggle$")
        };

        int attempts = 0;
        int maxAttempts = 10;
        boolean valid = false;
        String response = "";
        String system = this.systemPrompt;

        while (attempts < maxAttempts && !valid) {
            attempts++;

            JsonObject root = new JsonObject();
            root.addProperty("model", "gpt-3.5");

            JsonArray messages = new JsonArray();
            JsonObject sysMsg = new JsonObject();
            sysMsg.addProperty("role", "system");
            sysMsg.addProperty("content", system);
            messages.add(sysMsg);

            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            userMsg.addProperty("content", userPrompt);
            messages.add(userMsg);

            root.add("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(root.toString()))
                    .build();

            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new RuntimeException("OpenAI API Error: " + httpResponse.body());
            }

            JsonObject json = JsonParser.parseString(httpResponse.body()).getAsJsonObject();
            response = json.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString()
                    .trim();

            String[] lines = response.split("\n");
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                for (Pattern pattern : patterns) {
                    if (pattern.matcher(line.trim()).matches()) {
                        builder.append(line).append("\n");
                        break;
                    }
                }
            }

            FileIO.writeHomeFile(builder.toString(), outFileName);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                PaintFileParser parser = new PaintFileParser();
                valid = parser.parse(reader, paintModel);
                if (!valid) {
                    // Feed back errors to the system prompt for next attempt
                    system += "\nThe following file contains errors. Fix them and follow the rules strictly:\n"
                            + FileIO.readHomeFile(outFileName) + "\n"
                            + parser.getErrorMessage();
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found on attempt " + attempts);
            }

            System.out.println("Attempt " + attempts + " — valid: " + valid);
        }

        if (!valid) {
            System.out.println("AI could not produce a valid file after " + maxAttempts + " attempts.");
        } else {
            System.out.println("Valid file produced after " + attempts + " attempt(s).");
        }

        return response;
    }


    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}

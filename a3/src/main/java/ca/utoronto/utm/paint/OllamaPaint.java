package ca.utoronto.utm.paint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;


public class OllamaPaint extends Ollama {

    PaintModel paintModel = new PaintModel();

    public OllamaPaint(String host) {
        super(host);
    }

    /**
     * Ask llama3 to generate a new Paint File based on the given prompt
     *
     * @param prompt
     * @param outFileName name of new file to be created in users home directory
     */
    public void newFile(String prompt, String outFileName) {
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
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String system = "The answer to this question should be a paintSaveFile. " +
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
                "16. Make sure that each point, center, p1, p2 only have positive INTEGER for each (x,y). " +
                "17. The points for both squiggles and polylines MUST follow the syntax: 'point:(x,y)' where x,y are positive integers." +
                "18. The color attribute must NOT be set to 255,255,255" +
                "The document must match exactly to the defined formats and include no extra or invalid content. " + "Make sure the tabbing matches the examples." +
                "The document must always follow the guidelines set by: " + format +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + "." + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + "." +
                "Make sure to NOT create duplicates when writing the file." + "Make sure to follow rule 16.";

        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION" +
                " being performed on the following paintSaveFile Document. " + "MAKE SURE TO FOLLOW THE DEFINITION OF THE PaintSaveFile" + ". OPERATION START" + prompt + " OPERATION END " + ".";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Ask llama3 to generate a new Paint File based on a modification of inFileName and the prompt
     *
     * @param prompt      the user supplied prompt
     * @param inFileName  the Paint File Format file to be read and modified to outFileName
     * @param outFileName name of new file to be created in users home directory
     */
    public void modifyFile(String prompt, String inFileName, String outFileName) {
        // YOUR CODE GOES HERE
        // Your job is to create the right system and prompt.
        // then call Ollama and write the new file in the home directory
        // HINT: You should have a collection of resources, examples, prompt wrapper etc. available
        // in the resources directory. See OllamaNumberedFile as an example.
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
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String system = "The answer to this question should be a paintSaveFile. " +
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

        String f = FileIO.readHomeFile(inFileName);
        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION" +
                " being performed on the following paintSaveFile Document. " + "MAKE SURE TO FOLLOW THE DEFINITION OF THE PaintSaveFile" + ". OPERATION START" + prompt + " OPERATION END " + f + ".";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * newFile1: Generates a new file with n amount of figures which look like
     * houses and always generates a sun. All colors for each shape are randomized.
     *
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void newFile1(String outFileName) {
        String houseExample2 = FileIO.readResourceFile("housePaintSaveFileExample2.txt");
        String houseExample3 = FileIO.readResourceFile("housePaintSaveFileExample3.txt");
        String houseExample4 = FileIO.readResourceFile("housePaintSaveFileExample4.txt");
        String houseExample5 = FileIO.readResourceFile("housePaintSaveFileExample5.txt");
        String houseExample6 = FileIO.readResourceFile("housePaintSaveFileExample6.txt");
        String houseExample7 = FileIO.readResourceFile("housePaintSaveFileExample7.txt");
        String houseExample8 = FileIO.readResourceFile("housePaintSaveFileExample8.txt");
        String sunExample = FileIO.readResourceFile("sunPaintSaveFileExample.txt");
        String format = FileIO.readResourceFile("paintSaveFileFormat.txt");
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String prompt = "Draw three houses and a sun.";
        String system = "The answer must be a paintSaveFile Document. Respond only with a valid paintSaveFile Document, nothing else. " +
                "A valid paintSaveFile is described by: " + format +
                "The file should always start with 'Paint Save File Version 1.0' and end with 'End Paint Save File'." +
                "The points for squiggles and polylines must only contain INTEGER (x,y)." +
                "- The canvas ranges from (0,0) in the top-left corner to (500,500) in the bottom-right corner. " +
                "  - 'Up' corresponds to decreasing y-coordinates (closer to 0). " +
                "  - 'Down' corresponds to increasing y-coordinates (closer to 500). " +
                "  - 'Left' corresponds to decreasing x-coordinates (closer to 0). " +
                "  - 'Right' corresponds to increasing x-coordinates (closer to 500). " +
                "- Do not replicate the following examples verbatim. Use it as inspiration to create something unique. " +
                "- House Example 2: " + houseExample2 +
                "- House Example 3: " + houseExample3 +
                "- House Example 4: " + houseExample4 +
                "- House Example 5: " + houseExample5 +
                "- House Example 6: " + houseExample6 +
                "- House Example 7: " + houseExample7 +
                "- House Example 8: " + houseExample8 +
                "- Sun Example: " + sunExample +
                "Remember to NEVER write anything other than what is defined by the paintSaveFileFormat.";
        prompt += "A house is defined by the following: \n" +
                "1. It has a 200 by 250 rectangle which is the body of the house." +
                "2. It has a roof which is made up of a polyline which must ALWAYS consist of 3 points: \n" +
                "- 1. The first point is EQUAL to p1 of the rectangle. \n" +
                "- 2. The next point would be the vertex of the polyline which would exist halfway between the top-left corner of the rectangle and the top-right corner of the rectangle. " +
                "The vertex must ALWAYS be above the rectangle such that its y-value is always LOWER than the y-value of the top-left point of the rectangle. \n" +
                "- 3. The last point is identical to the top-right corner of the rectangle. " +
                "This corner is defined as the point where: " +
                "The x-coordinate equals the x-coordinate of p2 (the bottom-right corner of the rectangle), " +
                "and the y-coordinate equals the y-coordinate of p1 (the top-left corner of the rectangle). \n" +
                "The rectangle must always be BELOW the polyline and must ALWAYS be filled." +
                "Do not replicate the examples exactly, you can however, take inspiration from them. " +
                "DO NOT ADD ANY SHAPE OTHER THAN 1 RECTANGLE AND 1 POLYLINE." +
                "Each house and sun must have distinct coordinates for each of 'p1:(x,y)', 'p2:(x,y)' for rectangles and " +
                "'point:(x,y)' for polyline and 'center:(x,y) for circles, where x,y are positive integers such that (x,y) < (500,500).";
        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION being performed on the following paintSaveFile Document. OPERATION START" + prompt + " OPERATION END ";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * newFile2: Generates a new paintSaveFile with n smiley faces where n can be specified in the prompt.
     * All colors for each shape are randomized.
     *
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void newFile2(String outFileName) {
        String faceExample2 = FileIO.readResourceFile("facePaintSaveFileExample2.txt");
        String faceExample3 = FileIO.readResourceFile("facePaintSaveFileExample3.txt");
        String faceExample4 = FileIO.readResourceFile("facePaintSaveFileExample4.txt");
        String faceExample5 = FileIO.readResourceFile("facePaintSaveFileExample5.txt");
        String faceExample6 = FileIO.readResourceFile("facePaintSaveFileExample6.txt");
        String faceExample7 = FileIO.readResourceFile("facePaintSaveFileExample7.txt");
        String faceExample8 = FileIO.readResourceFile("facePaintSaveFileExample8.txt");
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
        String format = FileIO.readResourceFile("paintSaveFileFormat.txt");
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String prompt = "Draw a smiley face. ";
        String system = "The answer must be a paintSaveFile Document. Respond only with a valid paintSaveFile Document, nothing else.\n" +
                "A valid paintSaveFile is described as follows:\n" + format +
                "- The file must always start with 'Paint Save File Version 1.0' and end with 'End Paint Save File'.\n" +
                "- The points for squiggles and polylines must only contain INTEGER (x, y) values.\n" +
                "- The canvas ranges from (0, 0) in the top-left corner to (500, 500) in the bottom-right corner:\n" +
                "   - 'Up'/'Above' corresponds to decreasing y-coordinates (closer to 0).\n" +
                "   - 'Down'/'Below' corresponds to increasing y-coordinates (closer to 500).\n" +
                "   - 'Left' corresponds to decreasing x-coordinates (closer to 0).\n" +
                "   - 'Right' corresponds to increasing x-coordinates (closer to 500).\n" +
                "- Do not replicate the examples verbatim. Use them as inspiration to create a unique design.\n" +
                "Smiley Face Examples:\n" +
                " - Face Example 1: " + faceExample2 + "\n" +
                " - Face Example 2: " + faceExample3 + "\n" +
                " - Face Example 3: " + faceExample4 + "\n" +
                " - Face Example 4: " + faceExample5 + "\n" +
                " - Face Example 5: " + faceExample6 + "\n" +
                " - Face Example 6: " + faceExample7 + "\n" +
                " - Face Example 7: " + faceExample8 + "\n" +
                "Remember to NEVER include anything other than what is defined by the paintSaveFile format.\n" +
                "Each circle must be clearly distinct and non-overlapping." +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + ".";

        prompt += "NEVER respond with any coordinates for 'center:(x, y)' or 'point:(x, y)' where x, y represent either decimals or arithmetic operations such as (a+b, c+d).\n\n" +
                "A smiley face is defined as follows:\n" +
                "1. The face is represented as a circle:\n" +
                "   - The circle MUST have a center defined as 'center:(x, y)', where x and y are random positive integers such that (x, y) < (500, 500).\n" +
                "   - The radius of the face MUST be greater than equal to 50.\n" +
                "   - The face MUST be filled with a randomly generated color that is NOT (255, 255, 255).\n" +
                "2. The smiley face MUST have two eyes, represented as smaller filled circles:\n" +
                "   - Each eye must have a randomly generated, distinct color that is NOT (255, 255, 255).\n" +
                "   - Each EYE MUST have a radius of 15.\n" +
                "   - The left eye MUST have its center point at (x-15, y-20) relative to the center point of the face, where (x,y) represent the center point of the face.\n" +
                "   - The right eye MUST have its center point at (x+15, y-20) relative to the center point of the face, where (x,y) represent the center point of the face.\n" +
                "3. The smile must be represented by a polyline with exactly 3 points:\n" +
                "   - The first point MUST be at (x-20, y+20) relative to the center point of the face, where (x,y) represent the center point of the face.\n" +
                "   - The second point MUST be at (x, y+30) relative to the center point of the face, where (x,y) represent the center point of the face.\n" +
                "   - The last point MUST be at (x+20, y+20) relative to the center point of the face, where (x,y) represent the center point of the face.\n" +
                "   - The y-coordinate of the second point MUST ALWAYS be less than the y-coordinate of the eyes.\n" +
                "4. Ensure the following:\n" +
                "   - Each shape does not overlap.\n" +
                "   - All colors must be distinct and randomly generated, with no use of (255, 255, 255) or (0,0,0)." +
                "   - The eyes and the smile MUST fit inside the face." +
                "   - There must ALWAYS be two eyes and one face and once smile." +
                "   - The eyes MUST be above the smile and the smile MUST be underneath the eyes." +
                "Every 3 circles MUST have 1 Polyline. You must ONLY use circles and polylines, NOTHING else.";

        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION being performed on the following paintSaveFile Document. OPERATION START" + prompt + " OPERATION END.";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * newFile3: Generates a new file that represents outer space. This file also
     * produces an arbitrary number of planets in the outer space which can be
     * specified in the prompt. All colors for each shape are randomized.
     *
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void newFile3(String outFileName) {
        String format = FileIO.readResourceFile("paintSaveFileFormat.txt");
        String planetExample1 = FileIO.readResourceFile("planetPaintSaveFileExample1.txt");
        String planetExample2 = FileIO.readResourceFile("planetPaintSaveFileExample2.txt");
        String planetExample3 = FileIO.readResourceFile("planetPaintSaveFileExample3.txt");
        String planetExample4 = FileIO.readResourceFile("planetPaintSaveFileExample4.txt");
        String planetExample5 = FileIO.readResourceFile("planetPaintSaveFileExample5.txt");
        String planetExample6 = FileIO.readResourceFile("planetPaintSaveFileExample6.txt");
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
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String prompt = "Draw five planets in space. ";
        String system = "The answer must be a paintSaveFile Document. Respond only with a valid paintSaveFile Document, nothing else.\n" +
                "A valid paintSaveFile must follow the rules given by: " + format +
                "- The file must always start with 'Paint Save File Version 1.0' and end with 'End Paint Save File'.\n" +
                "- The points for squiggles and polylines must only contain INTEGER (x, y) values.\n" +
                "- The canvas ranges from (0, 0) in the top-left corner to (500, 500) in the bottom-right corner:\n" +
                "   - 'Up'/'Above' corresponds to decreasing y-coordinates (closer to 0).\n" +
                "   - 'Down'/'Below' corresponds to increasing y-coordinates (closer to 500).\n" +
                "   - 'Left' corresponds to decreasing x-coordinates (closer to 0).\n" +
                "   - 'Right' corresponds to increasing x-coordinates (closer to 500).\n" +
                "Do not replicate the following examples verbatim. Use them as inspiration to create a unique design, do not limit the number of planets that can be created by these examples.\n" +
                "Planet Example 1: " + planetExample1 + "\n" +
                "Planet Example 2: " + planetExample2 + "\n" +
                "Planet Example 3: " + planetExample3 + "\n" +
                "Planet Example 4: " + planetExample4 + "\n" +
                "Planet Example 5: " + planetExample5 + "\n" +
                "Planet Example 6: " + planetExample6 + "\n" +
                "When asked to draw n planets, you **must draw exactly 2*n concentric circles for n planets.\n" +
                "Each planet must be distinct and visually non-overlapping.\n" +
                "Remember to NEVER include anything other than what is defined by the paintSaveFile format.\n" +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + ".";

        prompt += "The following guidelines must be adhered to for creating Space and Planets:\n\n" +
                "1. The definition of space is as follows: \n" +
                "   - Space is represented by a single rectangle.\n" +
                "   - The rectangle must have dimensions of exactly 500 by 500.\n" +
                "   - The top-left corner of the rectangle must start at the origin point (0,0).\n" +
                "   - The rectangle MUST be filled with the color (0,0,0).\n" +
                "   - Space MUST ALWAYS be drawn first.\n\n" +
                "2. The definition of a planet is as follows: \n" +
                "   - A planet consists of two concentric circles (outer and inner) with the same center point.\n" +
                "   - When asked to draw n planets, you must draw 2*n concentric circles and strictly follow the definition of a planet given here.\n" +
                "   - The center point of each planet must be within the bounds of the canvas, specifically (0,0) to (500,500).\n" +
                "   - Each planet MUST have a distinct color. No two planets may have the same outer circle color.\n" +
                "   - Each of the inner and outer circles, MUST have the same center point.\n" +
                "   - Outer Circle:\n" +
                "       - The radius of the outer circle must be a maximum of 25.\n" +
                "       - The outer circle MUST NOT be filled.\n" +
                "   - Inner Circle:\n" +
                "       - The radius of the inner circle must be a maximum of 15.\n" +
                "       - The inner circle MUST be filled.\n" +
                "   - The colors of both the inner and outer circles must NEVER be (0,0,0).\n\n" +
                "Ensure that space is drawn first, followed by the planets. Each planet must adhere to the rules described above." +
                "You must treat each inner and outer circle as a circle, DO NOT make specific objects such as " +
                "'Outer Circle' or 'Inner Circle', just use 'Circle' as shown in the examples." +
                "There must ALWAYS be an even number of circles. " +
                "ALL circles will be considered 'planets' and must strictly adhere to the rules above.";


        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION being performed on the following paintSaveFile Document. OPERATION START" + prompt + " OPERATION END.";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * modifyFile1: MODIFY inFileName TO PRODUCE outFileName BY changing the color
     * of specified shapes to a specified color.
     *
     * @param inFileName  the name of the source file in the users home directory
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void modifyFile1(String inFileName, String outFileName) {
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
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String prompt = "Change the color of all rectangles to 255,10,10. ";
        String system = "The answer must be a paintSaveFile Document. Respond only with a valid paintSaveFile Document, nothing else.\n" +
                "A valid paintSaveFile must follow the rules given by: " + format +
                "- The file must always start with 'Paint Save File Version 1.0' and end with 'End Paint Save File'.\n" +
                "- The points for squiggles and polylines must only contain INTEGER (x, y) values.\n" +
                "- The canvas ranges from (0, 0) in the top-left corner to (500, 500) in the bottom-right corner:\n" +
                "   - 'Up'/'Above' corresponds to decreasing y-coordinates (closer to 0).\n" +
                "   - 'Down'/'Below' corresponds to increasing y-coordinates (closer to 500).\n" +
                "   - 'Left' corresponds to decreasing x-coordinates (closer to 0).\n" +
                "   - 'Right' corresponds to increasing x-coordinates (closer to 500).\n" +
                "Remember to NEVER include anything other than what is defined by the paintSaveFile format.\n" +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + "." +
                "If the " + inFileName + "file does not have any of the requested shapes to be changed, do NOT change the file.";

        String f = FileIO.readHomeFile(inFileName);
        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION being performed on the following paintSaveFile Document. OPERATION START" + prompt + " OPERATION END " + f + ".";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * modifyFile2: MODIFY inFileName TO PRODUCE outFileName BY changing the specified
     * shape to another specified shape. All colors for each shape are randomized.
     *
     * @param inFileName  the name of the source file in the users home directory
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void modifyFile2(String inFileName, String outFileName) {
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
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String prompt = "Change all circles to rectangles. ";
        String system = "The answer must be a paintSaveFile Document. Respond only with a valid paintSaveFile Document, nothing else.\n" +
                "A valid paintSaveFile must follow the rules given by: " + format +
                "- The file must always start with 'Paint Save File Version 1.0' and end with 'End Paint Save File'.\n" +
                "- The points for squiggles and polylines must only contain INTEGER (x, y) values.\n" +
                "- The canvas ranges from (0, 0) in the top-left corner to (500, 500) in the bottom-right corner:\n" +
                "   - 'Up'/'Above' corresponds to decreasing y-coordinates (closer to 0).\n" +
                "   - 'Down'/'Below' corresponds to increasing y-coordinates (closer to 500).\n" +
                "   - 'Left' corresponds to decreasing x-coordinates (closer to 0).\n" +
                "   - 'Right' corresponds to increasing x-coordinates (closer to 500).\n" +
                "Remember to NEVER include anything other than what is defined by the paintSaveFile format.\n" +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + "." +
                "If the " + inFileName + "file does not have any of the requested shapes to be changed, do NOT change the file.";

        String f = FileIO.readHomeFile(inFileName);
        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION being performed on the following paintSaveFile Document. OPERATION START" + prompt + " OPERATION END " + f + ".";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * modifyFile3: MODIFY inFileName TO PRODUCE outFileName BY decreasing/increasing the size
     * of all shapes specified that exist in inFileName.
     *
     * @param inFileName  the name of the source file in the users home directory
     * @param outFileName the name of the new file in the users home directory
     */
    @Override
    public void modifyFile3(String inFileName, String outFileName) {
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
        String homeDir = System.getProperty("user.home");
        File file = new File(homeDir, outFileName);
        if (file.exists()) {
            file.delete();
        }
        String prompt = "Modify the file to decrease the size of all shapes. ";
        String system = "The answer must be a paintSaveFile Document. Respond only with a valid paintSaveFile Document, nothing else.\n" +
                "A valid paintSaveFile must follow the rules given by: " + format +
                "- The file must always start with 'Paint Save File Version 1.0' and end with 'End Paint Save File'.\n" +
                "- The points for squiggles and polylines must only contain INTEGER (x, y) values.\n" +
                "- The canvas ranges from (0, 0) in the top-left corner to (500, 500) in the bottom-right corner:\n" +
                "   - 'Up'/'Above' corresponds to decreasing y-coordinates (closer to 0).\n" +
                "   - 'Down'/'Below' corresponds to increasing y-coordinates (closer to 500).\n" +
                "   - 'Left' corresponds to decreasing x-coordinates (closer to 0).\n" +
                "   - 'Right' corresponds to increasing x-coordinates (closer to 500).\n" +
                "Remember to NEVER include anything other than what is defined by the paintSaveFile format.\n" +
                "The following files show INVALID examples that should never be produced: " + format2 + format3 + format4 + format5 + format6 + format7 + format8 + format9 + format10 + format11 + "." +
                "The following files show VALID examples that should always be produced: " + format12 + format13 + format14 + format15 + format16 + format17 + format18 + format19 + format20 + format21 + format22 + "." +
                "If the " + inFileName + "file does not have any of the requested shapes to be changed, do NOT change the file.";

        String f = FileIO.readHomeFile(inFileName);
        String fullPrompt = "Produce a new paintSaveFile Document, resulting from the following OPERATION being performed on the following paintSaveFile Document. OPERATION START" + prompt + " OPERATION END " + f + ".";
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
        String response = this.call(system, fullPrompt);
        String[] lines = response.split("\n");
        StringBuilder builder = new StringBuilder();
        boolean flag2;
        for (int i = 0; i < lines.length; i++) {
            flag2 = false;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(lines[i].trim()).matches()) {
                    flag2 = true;
                }
            }
            if (flag2) {
                builder.append(lines[i]).append("\n");
            }
        }
        FileIO.writeHomeFile(builder.toString(), outFileName);
        file = new File(homeDir, outFileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getPath()));
            PaintFileParser parser = new PaintFileParser();
            Boolean flag = parser.parse(reader, paintModel);
            int attempts = 0;
            int maxAttempts = 10;
            while (!flag && (attempts < maxAttempts)) {
                attempts++;
                flag = parser.parse(reader, paintModel);
                if (flag) {
                    break;
                }
                System.out.println("ATTEMPTS: " + attempts);
                System.out.println(parser.getErrorMessage());
                system += parser.getErrorMessage() +
                        "\nThe following file contains errors. Fix them and follow the rules strictly:\n" +
                        FileIO.readHomeFile(outFileName);
                response = this.call(system, fullPrompt);
                lines = response.split("\n");
                builder = new StringBuilder();
                if (file.exists()) {
                    file.delete();
                }
                for (int i = 0; i < lines.length; i++) {
                    flag2 = false;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(lines[i].trim()).matches()) {
                            flag2 = true;
                        }
                    }
                    if (flag2) {
                        builder.append(lines[i]).append("\n");
                    }
                }
                FileIO.writeHomeFile(builder.toString(), outFileName);
                file = new File(homeDir, outFileName);
                reader = new BufferedReader(new FileReader(file.getPath()));
            }
            if (attempts == 0) {attempts = 1;}
            System.out.println("Took " + attempts + " attempts.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String prompt = null;
        prompt="Draw a 100 by 120 rectangle with 4 radius 5 circles at each rectangle corner.";
        OllamaPaint op = new OllamaPaint("dh2010pc08.utm.utoronto.ca"); // Replace this with your assigned Ollama server.

        prompt="Draw a 100 by 120 rectangle with 4 circles with radius 5 at each rectangle corner.";
        op.newFile(prompt, "OllamaPaintFile1.txt");
        op.modifyFile("Remove all shapes except for the circles.","OllamaPaintFile1.txt", "OllamaPaintFile2.txt" );

        prompt="Draw 5 concentric circles with different colors.";
        op.newFile(prompt, "OllamaPaintFile3.txt");
        op.modifyFile("Change all circles into rectangles.", "OllamaPaintFile3.txt", "OllamaPaintFile4.txt" );

        prompt="Draw a polyline then two circles then a rectangle then 3 polylines all with different colors.";
        op.newFile(prompt, "OllamaPaintFile4.txt");

        prompt="Modify the following Paint Save File so that each circle is surrounded by a non-filled rectangle. ";
        op.modifyFile("Change all circles into rectangles.", "OllamaPaintFile4.txt", "OllamaPaintFile5.txt" );


        for (int i = 1; i <= 3; i++) {
            op.newFile1("PaintFile1_" + i + ".txt");
            op.newFile2("PaintFile2_" + i + ".txt");
            op.newFile3("PaintFile3_" + i + ".txt");
        }

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                op.modifyFile1("PaintFile" + i + "_" + j + ".txt", "PaintFile" + i + "_" + j + "_1.txt");
                op.modifyFile2("PaintFile"+ i +"_"+j+ ".txt", "PaintFile"+ i +"_"+j+"_2.txt");
                op.modifyFile3("PaintFile"+ i +"_"+j+ ".txt", "PaintFile"+ i +"_"+j+"_3.txt");
            }
        }
    }
}


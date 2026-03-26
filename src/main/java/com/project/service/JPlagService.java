// // // // // // // package com.project.service;

// // // // // // // import java.io.File;
// // // // // // // import java.util.HashMap;
// // // // // // // import java.util.Map;

// // // // // // // import org.springframework.stereotype.Service;

// // // // // // // import de.jplag.JPlag;
// // // // // // // import de.jplag.Options;
// // // // // // // import de.jplag.Result;
// // // // // // // import de.jplag.Result.ResultPair;
// // // // // // // import de.jplag.languages.Language;

// // // // // // // @Service
// // // // // // // public class JPlagService {

// // // // // // //     /**
// // // // // // //      * Runs JPlag on a folder containing multiple student code files.
// // // // // // //      * Returns a map of file1 <-> file2 => similarity score
// // // // // // //      */
// // // // // // //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

// // // // // // //         // Output directory
// // // // // // //         File outputDir = new File("jplag-output");
// // // // // // //         outputDir.mkdir();

// // // // // // //         // Setup JPlag options
// // // // // // //         Options options = new Options(Language.JAVA, submissionsDir.getAbsolutePath(), outputDir.getAbsolutePath());
// // // // // // //         options.setShowMatches(false); // only similarity scores
// // // // // // //         options.setEncoding("UTF-8");

// // // // // // //         // Run JPlag
// // // // // // //         Result result = JPlag.run(options);

// // // // // // //         // Collect similarity scores
// // // // // // //         Map<String, Double> similarityMap = new HashMap<>();
// // // // // // //         for (ResultPair pair : result.getResults()) {
// // // // // // //             String key = pair.getFile1() + " <-> " + pair.getFile2();
// // // // // // //             similarityMap.put(key, pair.getSimilarity());
// // // // // // //         }

// // // // // // //         return similarityMap;
// // // // // // //     }
// // // // // // // }

// // // // // // package com.project.service;

// // // // // // import java.io.File;
// // // // // // import java.util.HashMap;
// // // // // // import java.util.Map;

// // // // // // import org.springframework.stereotype.Service;

// // // // // // import de.jplag.JPlag;
// // // // // // import de.jplag.JPlagResult;
// // // // // // import de.jplag.Match;
// // // // // // import de.jplag.Submission;
// // // // // // import de.jplag.languages.LanguageJava; // JPlag 6.x uses classes like LanguageJava

// // // // // // @Service
// // // // // // public class JPlagService {

// // // // // //     /**
// // // // // //      * Run JPlag 6.x on a folder of submissions.
// // // // // //      */
// // // // // //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {
// // // // // //         Map<String, Double> similarityMap = new HashMap<>();

// // // // // //         // Use JPlag's LanguageJava instance
// // // // // //         LanguageJava javaLang = new LanguageJava();

// // // // // //         // Create JPlag instance
// // // // // //         JPlag jplag = new JPlag(javaLang);

// // // // // //         // Add submissions
// // // // // //         File[] files = submissionsDir.listFiles();
// // // // // //         if (files != null) {
// // // // // //             for (File file : files) {
// // // // // //                 if (file.isFile()) {
// // // // // //                     jplag.addSubmission(new Submission(file.getName(), file, true, null, javaLang));
// // // // // //                 }
// // // // // //             }
// // // // // //         }

// // // // // //         // Run the analysis
// // // // // //         JPlagResult result = jplag.run();

// // // // // //         // Extract matches and similarity scores
// // // // // //         for (Match match : result.getMatches()) {
// // // // // //             String key = match.getFirstSubmission().getName() + " <-> " + match.getSecondSubmission().getName();
// // // // // //             similarityMap.put(key, match.getSimilarity());
// // // // // //         }

// // // // // //         return similarityMap;
// // // // // //     }
// // // // // // }

// // // // // package com.project.service;

// // // // // import java.io.File;
// // // // // import java.util.HashMap;
// // // // // import java.util.Map;

// // // // // import org.springframework.stereotype.Service;

// // // // // import de.jplag.JPlag;
// // // // // import de.jplag.JPlagResult;
// // // // // import de.jplag.Match;
// // // // // import de.jplag.Submission;
// // // // // import de.jplag.languages.LanguageJava;

// // // // // @Service
// // // // // public class JPlagService {

// // // // //     /**
// // // // //      * Run JPlag 6.x on a folder of code submissions.
// // // // //      * Returns a map of "file1 <-> file2" -> similarity score.
// // // // //      */
// // // // //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {
// // // // //         Map<String, Double> similarityMap = new HashMap<>();

// // // // //         LanguageJava javaLang = new LanguageJava();
// // // // //         JPlag jplag = new JPlag(javaLang);

// // // // //         // Add submissions
// // // // //         File[] files = submissionsDir.listFiles();
// // // // //         if (files != null) {
// // // // //             for (File file : files) {
// // // // //                 if (file.isFile()) {
// // // // //                     jplag.addSubmission(new Submission(file.getName(), file, true, null, javaLang));
// // // // //                 }
// // // // //             }
// // // // //         }

// // // // //         // Run JPlag
// // // // //         JPlagResult result = jplag.run();

// // // // //         // Extract similarity matches
// // // // //         for (Match match : result.getMatches()) {
// // // // //             String key = match.getFirstSubmission().getName() + " <-> " + match.getSecondSubmission().getName();
// // // // //             similarityMap.put(key, match.getSimilarity());
// // // // //         }

// // // // //         return similarityMap;
// // // // //     }
// // // // // }

// // // // package com.project.service;

// // // // import java.io.BufferedReader;
// // // // import java.io.File;
// // // // import java.io.InputStreamReader;
// // // // import java.util.HashMap;
// // // // import java.util.Map;

// // // // import org.springframework.stereotype.Service;

// // // // @Service
// // // // public class JPlagService {

// // // //     /**
// // // //      * Run JPlag 6.x via CLI and parse the output.
// // // //      */
// // // //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {
// // // //         Map<String, Double> similarityMap = new HashMap<>();

// // // //         // Output folder
// // // //         File outputDir = new File("jplag-output");
// // // //         outputDir.mkdir();

// // // //         // Build command: java -jar jplag-6.3.0-jar-with-dependencies.jar -l java17 -s submissionsDir -r outputDir
// // // //         String command = String.format("java -jar jplag-6.3.0-jar-with-dependencies.jar -l java17 -s %s -r %s -v",
// // // //                 submissionsDir.getAbsolutePath(), outputDir.getAbsolutePath());

// // // //         ProcessBuilder pb = new ProcessBuilder(command.split(" "));
// // // //         pb.redirectErrorStream(true);
// // // //         Process process = pb.start();

// // // //         // Print JPlag output for debugging
// // // //         try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
// // // //             String line;
// // // //             while((line = reader.readLine()) != null) {
// // // //                 System.out.println(line);
// // // //             }
// // // //         }

// // // //         process.waitFor();

// // // //         // TODO: Parse the output JSON in outputDir to extract similarity scores
// // // //         // Typically JPlag writes a file like 'results.json' or HTML report.
// // // //         // You can read the JSON and populate similarityMap.

// // // //         return similarityMap;
// // // //     }
// // // // }


// // // package com.project.service;

// // // import java.io.File;
// // // import java.util.HashMap;
// // // import java.util.Map;

// // // import org.springframework.stereotype.Service;

// // // import de.jplag.JPlag;
// // // import de.jplag.JPlagOptions;
// // // import de.jplag.JPlagResult;
// // // import de.jplag.Match;
// // // import de.jplag.language.java.JavaLanguage;

// // // @Service
// // // public class JPlagService {

// // //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

// // //         Map<String, Double> similarityMap = new HashMap<>();

// // //         // Configure JPlag
// // //         JPlagOptions options = new JPlagOptions();

// // //         options.setLanguage(new JavaLanguage());
// // //         options.setSubmissionDirectory(submissionsDir.toPath());

// // //         // Run JPlag
// // //         JPlag jplag = new JPlag(options);
// // //         JPlagResult result = jplag.run();

// // //         // Collect similarity results
// // //         for (Match match : result.getMatches()) {

// // //             String submission1 = match.getSubmissionA().getName();
// // //             String submission2 = match.getSubmissionB().getName();

// // //             double similarity = match.getSimilarity();

// // //             similarityMap.put(submission1 + " <-> " + submission2, similarity);
// // //         }

// // //         return similarityMap;
// // //     }
// // // }


// // // package com.project.service;

// // // import java.io.BufferedReader;
// // // import java.io.File;
// // // import java.io.InputStreamReader;
// // // import java.util.HashMap;
// // // import java.util.Map;

// // // import org.springframework.stereotype.Service;

// // // @Service
// // // public class JPlagService {

// // //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

// // //         Map<String, Double> similarityMap = new HashMap<>();

// // //         File outputDir = new File("jplag-output");
// // //         if(!outputDir.exists()) {
// // //             outputDir.mkdirs();
// // //         }

// // //         ProcessBuilder pb = new ProcessBuilder(
// // //                 "java",
// // //                 "-jar",
// // //                 "jplag-6.2.0-jar-with-dependencies.jar",
// // //                 "-l",
// // //                 "java",
// // //                 "-s",
// // //                 submissionsDir.getAbsolutePath(),
// // //                 "-r",
// // //                 outputDir.getAbsolutePath()
// // //         );

// // //         pb.redirectErrorStream(true);

// // //         Process process = pb.start();

// // //         BufferedReader reader = new BufferedReader(
// // //                 new InputStreamReader(process.getInputStream())
// // //         );

// // //         String line;
// // //         while((line = reader.readLine()) != null) {
// // //             System.out.println(line);
// // //         }

// // //         process.waitFor();

// // //         return similarityMap;
// // //     }
// // // }

// // package com.project.service;

// // import java.io.BufferedReader;
// // import java.io.File;
// // import java.io.InputStreamReader;
// // import java.util.HashMap;
// // import java.util.Map;

// // import org.springframework.stereotype.Service;

// // @Service
// // public class JPlagService {

// //     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

// //         Map<String, Double> similarityMap = new HashMap<>();

// //         File outputDir = new File("jplag-output");

// //         if (!outputDir.exists()) {
// //             outputDir.mkdirs();
// //         }

// //         ProcessBuilder pb = new ProcessBuilder(
// //                 "java",
// //                 "-jar",
// //                 "jplag-6.2.0-jar-with-dependencies.jar",
// //                 "-l",
// //                 "java",
// //                 "-t",
// //                 "5",
// //                 "-r",
// //                 outputDir.getAbsolutePath(),
// //                 submissionsDir.getAbsolutePath()
// //         );

// //         pb.redirectErrorStream(true);

// //         Process process = pb.start();

// //         BufferedReader reader = new BufferedReader(
// //                 new InputStreamReader(process.getInputStream())
// //         );

// //         String line;

// //         while ((line = reader.readLine()) != null) {
// //             System.out.println(line);
// //         }

// //         process.waitFor();

// //         return similarityMap;
// //     }
// // }


// package com.project.service;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.InputStreamReader;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.stereotype.Service;

// @Service
// public class JPlagService {

//     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

//         Map<String, Double> similarityMap = new HashMap<>();

//         File outputDir = new File("jplag-output");

//         if (!outputDir.exists()) {
//             outputDir.mkdirs();
//         }

//         ProcessBuilder pb = new ProcessBuilder(
//                 "java",
//                 "-jar",
//                 "jplag-6.2.0-jar-with-dependencies.jar",
//                 "-l",
//                 "java",
//                 "-t",
//                 "5",
//                 "-r",
//                 outputDir.getAbsolutePath(),
//                 submissionsDir.getAbsolutePath()
//         );

//         pb.redirectErrorStream(true);

//         Process process = pb.start();

//         BufferedReader reader = new BufferedReader(
//                 new InputStreamReader(process.getInputStream())
//         );

//         String line;

//         while ((line = reader.readLine()) != null) {
//             System.out.println(line);
//         }

//         process.waitFor();

//         return similarityMap;
//     }
// }


// package com.project.service;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.InputStreamReader;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.stereotype.Service;

// @Service
// public class JPlagService {

//     /**
//      * Run JPlag 6.x on a folder of student submissions.
//      * Fixes:
//      * 1️⃣ Sets -m (minimum token match) to 3 for small files.
//      * 2️⃣ Ensures submission directory is passed after -s.
//      * 3️⃣ Creates output directory if missing.
//      *
//      * @param submissionsDir Folder containing student submissions (unzipped)
//      * @return Map of "file1 <-> file2" -> similarity score (currently placeholder, needs parsing of results.json)
//      */
//     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {
//         Map<String, Double> similarityMap = new HashMap<>();

//         // Create output folder
//         File outputDir = new File("jplag-output");
//         if (!outputDir.exists()) outputDir.mkdirs();

//         // Fixed ProcessBuilder command
//         ProcessBuilder pb = new ProcessBuilder(
//                 "java",
//                 "-jar",
//                 "jplag-6.2.0-jar-with-dependencies.jar", // make sure this jar is in project root
//                 "-l", "java",
//                 "-s", submissionsDir.getAbsolutePath(),   // submission dir MUST be after -s
//                 "-m", "3",                                 // minimum token match
//                 "--overwrite",                             // overwrite previous results
//                 "-r", outputDir.getAbsolutePath()          // report/output directory
//         );

//         pb.redirectErrorStream(true);
//         Process process = pb.start();

//         // Print console output for debugging
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 System.out.println(line);
//             }
//         }

//         process.waitFor();

//         // TODO: Parse results.json or results.jplag from outputDir to populate similarityMap
//         // Currently returning empty map
//         return similarityMap;
//     }
// }


// package com.project.service;

// import java.awt.Desktop;
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.InputStreamReader;
// import java.net.URI;
// import java.util.HashMap;
// import java.util.Map;

// import org.json.JSONArray;
// import org.json.JSONObject;
// import org.springframework.stereotype.Service;

// @Service
// public class JPlagService {

//     /**
//      * Run JPlag 6.x on a folder of student submissions,
//      * parse results.json, and open report in default browser.
//      *
//      * @param submissionsDir Folder containing student submissions
//      * @return Map of "file1 <-> file2" -> similarity percentage
//      * @throws Exception
//      */
//     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {
//         Map<String, Double> similarityMap = new HashMap<>();

//         // Create output folder if missing
//         File outputDir = new File("jplag-output");
//         if (!outputDir.exists()) outputDir.mkdirs();

//         // JPlag command
//         ProcessBuilder pb = new ProcessBuilder(
//                 "java",
//                 "-jar",
//                 "jplag-6.2.0-jar-with-dependencies.jar",
//                 "-l", "java",
//                 "-t", "5",                        // minimum tokens
//                 "-r", outputDir.getAbsolutePath(),
//                 submissionsDir.getAbsolutePath()   // submissions folder last
//         );

//         pb.redirectErrorStream(true);
//         Process process = pb.start();

//         // Print JPlag console output
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 System.out.println(line);
//             }
//         }

//         process.waitFor();

//         // Parse results.json
//         File jsonFile = new File(outputDir, "results.json");
//         if (jsonFile.exists()) {
//             try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
//                 StringBuilder sb = new StringBuilder();
//                 String line;
//                 while ((line = reader.readLine()) != null) {
//                     sb.append(line);
//                 }

//                 JSONObject json = new JSONObject(sb.toString());
//                 JSONArray comparisons = json.getJSONArray("comparisons");

//                 for (int i = 0; i < comparisons.length(); i++) {
//                     JSONObject comp = comparisons.getJSONObject(i);
//                     String file1 = comp.getString("file1");
//                     String file2 = comp.getString("file2");
//                     double similarity = comp.getDouble("similarity"); // percentage
//                     similarityMap.put(file1 + " <-> " + file2, similarity);
//                 }
//             }
//         } else {
//             System.out.println("No results.json found in " + outputDir.getAbsolutePath());
//         }

//         // Automatically open the report in browser
//         File indexFile = new File(outputDir, "index.html");
//         if (indexFile.exists() && Desktop.isDesktopSupported()) {
//             Desktop.getDesktop().browse(indexFile.toURI());
//         }

//         return similarityMap;
//     }
// }




// package com.project.service;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.InputStreamReader;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.stereotype.Service;

// @Service
// public class JPlagService {

//     public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

//         Map<String, Double> similarityMap = new HashMap<>();

//         File outputDir = new File("jplag-output");

//         if (!outputDir.exists()) {
//             outputDir.mkdirs();
//         }

//         ProcessBuilder pb = new ProcessBuilder(
//                 "java",
//                 "-jar",
//                 "jplag-6.2.0-jar-with-dependencies.jar",
//                 "-l",
//                 "java",
//                 "-t",
//                 "5",
//                 "-r",
//                 outputDir.getAbsolutePath(),
//                 submissionsDir.getAbsolutePath()
//         );

//         pb.redirectErrorStream(true);

//         Process process = pb.start();

//         BufferedReader reader = new BufferedReader(
//                 new InputStreamReader(process.getInputStream())
//         );

//         String line;

//         while ((line = reader.readLine()) != null) {
//             System.out.println(line);
//         }

//         process.waitFor();

//         return similarityMap;
//     }
// }



package com.project.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class JPlagService {

    public Map<String, Double> runJPlag(File submissionsDir) throws Exception {

        Map<String, Double> similarityMap = new HashMap<>();

        File outputDir = new File("jplag-output");
        if (!outputDir.exists()) outputDir.mkdirs();

        // Add --overwrite to avoid "output file already exists" error
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-jar",
                "jplag-6.2.0-jar-with-dependencies.jar",
                "-l", "java",
                "-t", "5",
                "--overwrite",
                "-r", outputDir.getAbsolutePath(),
                submissionsDir.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        process.waitFor();

        return similarityMap;
    }
}
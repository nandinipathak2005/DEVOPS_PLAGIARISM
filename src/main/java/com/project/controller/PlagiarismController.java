// // // // // package com.project.controller;

// // // // // import java.io.IOException;

// // // // // import org.springframework.web.bind.annotation.PostMapping;
// // // // // import org.springframework.web.bind.annotation.RequestMapping;
// // // // // import org.springframework.web.bind.annotation.RequestParam;
// // // // // import org.springframework.web.bind.annotation.RestController;
// // // // // import org.springframework.web.multipart.MultipartFile;

// // // // // import com.project.service.PlagiarismService;

// // // // // @RestController
// // // // // @RequestMapping("/plagiarism")
// // // // // public class PlagiarismController {

// // // // //     private final PlagiarismService plagiarismService;

// // // // //     public PlagiarismController(PlagiarismService plagiarismService) {
// // // // //         this.plagiarismService = plagiarismService;
// // // // //     }

// // // // //     @PostMapping("/check")
// // // // //     public String checkPlagiarism(
// // // // //             @RequestParam("file1") MultipartFile file1,
// // // // //             @RequestParam("file2") MultipartFile file2) throws IOException {

// // // // //         String text1 = new String(file1.getBytes());
// // // // //         String text2 = new String(file2.getBytes());

// // // // //         double similarity = plagiarismService.calculateSimilarity(text1, text2);

// // // // //         if(similarity > 0.7)
// // // // //             return "Plagiarism detected. Similarity = " + similarity;

// // // // //         return "No plagiarism. Similarity = " + similarity;
// // // // //     }
// // // // // }


// // // // package com.project.controller;

// // // // import java.io.IOException;

// // // // import org.springframework.web.bind.annotation.GetMapping;
// // // // import org.springframework.web.bind.annotation.PostMapping;
// // // // import org.springframework.web.bind.annotation.RequestMapping;
// // // // import org.springframework.web.bind.annotation.RequestParam;
// // // // import org.springframework.web.bind.annotation.RestController;
// // // // import org.springframework.web.multipart.MultipartFile;

// // // // import com.project.service.PlagiarismService;

// // // // @RestController
// // // // @RequestMapping("/plagiarism")
// // // // public class PlagiarismController {

// // // //     private final PlagiarismService plagiarismService;

// // // //     public PlagiarismController(PlagiarismService plagiarismService) {
// // // //         this.plagiarismService = plagiarismService;
// // // //     }

// // // //     // Existing POST endpoint
// // // //     @PostMapping("/check")
// // // //     public String checkPlagiarism(
// // // //             @RequestParam("file1") MultipartFile file1,
// // // //             @RequestParam("file2") MultipartFile file2) throws IOException {

// // // //         String text1 = new String(file1.getBytes());
// // // //         String text2 = new String(file2.getBytes());

// // // //         double similarity = plagiarismService.calculateSimilarity(text1, text2);

// // // //         if(similarity > 0.7)
// // // //             return "Plagiarism detected. Similarity = " + similarity;

// // // //         return "No plagiarism. Similarity = " + similarity;
// // // //     }

// // // //     // NEW: GET test endpoint for quick browser check
// // // //     @GetMapping("/check/test")
// // // //     public String testPlagiarism() {
// // // //         return "Plagiarism endpoint is alive. Use POST with files to test.";
// // // //     }
// // // // }

// // // package com.project.controller;

// // // import java.io.IOException;
// // // import java.util.HashMap;
// // // import java.util.Map;

// // // import org.springframework.http.ResponseEntity;
// // // import org.springframework.web.bind.annotation.GetMapping;
// // // import org.springframework.web.bind.annotation.PostMapping;
// // // import org.springframework.web.bind.annotation.RequestMapping;
// // // import org.springframework.web.bind.annotation.RequestParam;
// // // import org.springframework.web.bind.annotation.RestController;
// // // import org.springframework.web.multipart.MultipartFile;

// // // import com.project.service.PlagiarismService;

// // // @RestController
// // // @RequestMapping("/plagiarism")
// // // public class PlagiarismController {

// // //     private final PlagiarismService plagiarismService;

// // //     public PlagiarismController(PlagiarismService plagiarismService) {
// // //         this.plagiarismService = plagiarismService;
// // //     }

// // //     // POST endpoint for actual plagiarism check with file upload
// // //     @PostMapping("/check")
// // //     public ResponseEntity<Map<String, Object>> checkPlagiarism(
// // //             @RequestParam("file1") MultipartFile file1,
// // //             @RequestParam("file2") MultipartFile file2) throws IOException {

// // //         String text1 = new String(file1.getBytes());
// // //         String text2 = new String(file2.getBytes());

// // //         double similarity = plagiarismService.calculateSimilarity(text1, text2);

// // //         Map<String, Object> response = new HashMap<>();
// // //         response.put("similarity", similarity);
// // //         response.put("plagiarism", similarity > 0.7);

// // //         return ResponseEntity.ok(response);
// // //     }

// // //     // GET endpoint for browser-friendly check
// // //     @GetMapping("/check")
// // //     public String checkPlagiarismInfo() {
// // //         return "Use POST /plagiarism/check with form-data: file1 and file2 to test plagiarism.";
// // //     }

// // //     // GET endpoint for quick browser test
// // //     @GetMapping("/check/test")
// // //     public String testPlagiarism() {
// // //         return "Plagiarism endpoint is alive. Use POST with files to test.";
// // //     }
// // // }

// // package com.project.controller;

// // import java.io.File;
// // import java.io.IOException;
// // import java.util.Map;

// // import org.springframework.http.ResponseEntity;
// // import org.springframework.web.bind.annotation.PostMapping;
// // import org.springframework.web.bind.annotation.RequestMapping;
// // import org.springframework.web.bind.annotation.RequestParam;
// // import org.springframework.web.bind.annotation.RestController;
// // import org.springframework.web.multipart.MultipartFile;

// // import com.project.service.JPlagService;

// // @RestController
// // @RequestMapping("/plagiarism")
// // public class PlagiarismController {

// //     private final JPlagService jPlagService;

// //     public PlagiarismController(JPlagService jPlagService) {
// //         this.jPlagService = jPlagService;
// //     }

// //     // POST endpoint to upload a zip of student submissions
// //     @PostMapping("/check-zip")
// //     public ResponseEntity<?> checkZipPlagiarism(@RequestParam("zipFile") MultipartFile zipFile) throws IOException {

// //         if (zipFile.isEmpty()) {
// //             return ResponseEntity.badRequest().body("Zip file is required!");
// //         }

// //         // Save zip to temp folder
// //         File tempZip = new File(System.getProperty("java.io.tmpdir"), zipFile.getOriginalFilename());
// //         zipFile.transferTo(tempZip);

// //         // Extract zip
// //         File tempDir = new File(System.getProperty("java.io.tmpdir"), "jplag-" + System.currentTimeMillis());
// //         tempDir.mkdirs();
// //         ZipUtils.extractZip(tempZip, tempDir); // helper method to unzip files

// //         try {
// //             Map<String, Double> results = jPlagService.runJPlag(tempDir);
// //             return ResponseEntity.ok(results);
// //         } catch (Exception e) {
// //             e.printStackTrace();
// //             return ResponseEntity.status(500).body("Error running JPlag: " + e.getMessage());
// //         }
// //     }
// // }


// package com.project.controller;

// import java.io.File;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import com.project.service.JPlagService;
// import com.project.utils.ZipUtils;

// @RestController
// @RequestMapping("/plagiarism")
// public class PlagiarismController {

//     @Autowired
//     private JPlagService jPlagService;

//     /**
//      * Uploads a ZIP file containing student submissions.
//      * Runs JPlag and returns similarity scores.
//      */
//     @PostMapping("/check")
//     public Map<String, Double> checkPlagiarism(@RequestParam("file") MultipartFile file) throws Exception {
//         // Save uploaded file to temp directory
//         File tempDir = new File("temp-submissions");
//         tempDir.mkdirs();
//         File uploadedFile = new File(tempDir, file.getOriginalFilename());
//         file.transferTo(uploadedFile);

//         // Extract ZIP
//         File extractedDir = new File(tempDir, "extracted");
//         extractedDir.mkdirs();
//         ZipUtils.unzip(uploadedFile, extractedDir);

//         // Run JPlag on extracted folder
//         return jPlagService.runJPlag(extractedDir);
//     }
// }

package com.project.controller;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.service.JPlagService;
import com.project.utils.ZipUtils;

@RestController
@RequestMapping("/plagiarism")
public class PlagiarismController {

    @Autowired
    private JPlagService jPlagService;

    @PostMapping("/check")
    public ResponseEntity<?> checkPlagiarism(@RequestParam("file") MultipartFile file) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a ZIP file.");
            }

            File tempDir = new File(System.getProperty("java.io.tmpdir"), "temp-submissions");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            File uploadedZip = new File(tempDir, file.getOriginalFilename());
            file.transferTo(uploadedZip);

            File extractedDir = new File(tempDir, "extracted-" + System.currentTimeMillis());
            extractedDir.mkdirs();

            ZipUtils.unzip(uploadedZip, extractedDir);

            Map<String, Double> results = jPlagService.runJPlag(extractedDir);

            return ResponseEntity.ok(results);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error running plagiarism check: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Plagiarism API running!";
    }
}
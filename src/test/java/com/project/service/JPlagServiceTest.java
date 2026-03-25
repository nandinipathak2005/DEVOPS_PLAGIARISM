// package com.project;

// import com.project.service.JPlagService;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// // These are the imports that were missing causing your 27 errors
// import java.io.File;
// import java.time.Duration;
// import java.util.List;
// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
// class JPlagServiceTest {

//     @Autowired
//     private JPlagService jPlagService;

//     @Test
//     @DisplayName("1. Happy Path: Run JPlag on real submissions")
//     void testRunJPlagUsingFolderPath() throws Exception {
//         File submissions = new File("src/test/resources/STUDENT");
//         if (submissions.exists()) {
//             // Updated to runJPlag to match your service method name
//             Map<String, Object> result = jPlagService.runJPlag(submissions, "test-job");
//             assertNotNull(result);
//         }
//     }

//     @Test
//     @DisplayName("2. Error Handling: Fail if directory does not exist")
//     void testMissingDirectory() {
//         File submissions = new File("src/test/resources/nonexistent");
//         assertThrows(Exception.class, () -> {
//             jPlagService.runJPlag(submissions, "fail-job");
//         });
//     }

//     @Test
//     @DisplayName("3. Security: Protect server from long execution")
//     void testTimeoutProtection() {
//         assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
//             try {
//                 File submissions = new File("src/test/resources/STUDENT");
//                 if (submissions.exists()) {
//                     jPlagService.runJPlag(submissions, "timeout-job");
//                 }
//             } catch (Exception ignored) {}
//         });
//     }

//     @Test
//     @DisplayName("4. Concurrency: Handle parallel executions safely")
//     void testParallelExecution() throws Exception {
//         File submissions = new File("src/test/resources/STUDENT");
//         if (!submissions.exists()) return;

//         Thread t1 = new Thread(() -> {
//             try { jPlagService.runJPlag(submissions, "job1"); } catch (Exception ignored) {}
//         });

//         Thread t2 = new Thread(() -> {
//             try { jPlagService.runJPlag(submissions, "job2"); } catch (Exception ignored) {}
//         });

//         t1.start();
//         t2.start();
//         t1.join();
//         t2.join();
        
//         assertTrue(true);
//     }
// }

// package com.project.service;

// import java.io.File;
// import java.time.Duration;
// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// class JPlagServiceTest {

//     private JPlagService jPlagService;

//     @BeforeEach
//     void setUp() {
//         jPlagService = new JPlagService();
//     }

//     @Test
//     @DisplayName("1. Happy Path: Run JPlag on real submissions")
//     void testRunJPlagUsingFolderPath() throws Exception {
//         File submissions = new File("src/test/resources/STUDENT");
//         if (submissions.exists()) {
//             Map result = jPlagService.runJPlag(submissions, "folder-test", false);
//             assertNotNull(result);
//         }
//     }

//     @Test
//     @DisplayName("2. Error Handling: Fail if directory does not exist")
//     void testMissingDirectory() {
//         File submissions = new File("src/test/resources/nonexistent");
//         assertThrows(Exception.class, () -> {
//             jPlagService.runJPlag(submissions, "fail-test", false);
//         });
//     }

//     @Test
//     @DisplayName("3. Security: Protect server from long execution")
//     void testTimeoutProtection() {
//         assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
//             try {
//                 File submissions = new File("src/test/resources/STUDENT");
//                 if (submissions.exists()) {
//                     jPlagService.runJPlag(submissions, "timeout-test", false);
//                 }
//             } catch (Exception ignored) {}
//         });
//     }

//     @Test
//     @DisplayName("4. Dirty Data: Fail when no valid Java files")
//     void testDirtyDataFiles() {
//         File submissions = new File("src/test/resources/dirty");
//         // Ensure folder exists but is 'dirty' for this test to trigger JPlag's internal error
//         assertThrows(Exception.class, () -> {
//             jPlagService.runJPlag(submissions, "dirty-test", false);
//         });
//     }

//     @Test
//     @DisplayName("5. Edge Case: Fail when only one submission exists")
//     void testSingleSubmission() {
//         File submissions = new File("src/test/resources/single");
//         assertThrows(Exception.class, () -> {
//             jPlagService.runJPlag(submissions, "single-test", false);
//         });
//     }

//     @Test
//     @DisplayName("6. Concurrency: Handle parallel executions safely")
//     void testParallelExecution() throws Exception {
//         File submissions = new File("src/test/resources/STUDENT");
//         if (!submissions.exists()) return;

//         Thread t1 = new Thread(() -> {
//             try { jPlagService.runJPlag(submissions, "job1", false); } catch (Exception ignored) {}
//         });
//         Thread t2 = new Thread(() -> {
//             try { jPlagService.runJPlag(submissions, "job2", false); } catch (Exception ignored) {}
//         });

//         t1.start();
//         t2.start();
//         t1.join();
//         t2.join();
//         assertTrue(true);
//     }
// }

package com.project.service;

import java.io.File;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JPlagServiceTest {

    private JPlagService jPlagService;

    @BeforeEach
    void setUp() {
        jPlagService = new JPlagService();
    }

    @Test
    @DisplayName("1. Happy Path: Run JPlag on valid submissions")
    void testRunJPlagUsingFolderPath() throws Exception {
        File submissions = new File("src/test/resources/STUDENT");

        assertTrue(submissions.exists(), "Test folder does not exist");

        Map<String, Object> result = jPlagService.runJPlag(submissions, "folder-test", false);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.containsKey("peerMatches"), "peerMatches key missing");

        Map<String, Double> matches = (Map<String, Double>) result.get("peerMatches");

        assertNotNull(matches, "Matches map should not be null");

        // ✅ IMPORTANT: check if similarity actually extracted
        assertFalse(matches.isEmpty(), "Similarity results should not be empty");
    }

    @Test
    @DisplayName("2. Error Handling: Fail if directory does not exist")
    void testMissingDirectory() {
        File submissions = new File("src/test/resources/nonexistent");

        Exception ex = assertThrows(Exception.class, () -> {
            jPlagService.runJPlag(submissions, "fail-test", false);
        });

        assertNotNull(ex.getMessage());
    }

    @Test
    @DisplayName("3. Timeout Protection: Should complete within 30 seconds")
    void testTimeoutProtection() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            File submissions = new File("src/test/resources/STUDENT");

            if (submissions.exists()) {
                jPlagService.runJPlag(submissions, "timeout-test", false);
            }
        });
    }

    @Test
    @DisplayName("4. Dirty Data: Fail when no valid Java files")
    void testDirtyDataFiles() {
        File submissions = new File("src/test/resources/dirty");

        assertTrue(submissions.exists(), "Dirty folder must exist for test");

        assertThrows(Exception.class, () -> {
            jPlagService.runJPlag(submissions, "dirty-test", false);
        });
    }

    @Test
    @DisplayName("5. Edge Case: Fail when only one submission exists")
    void testSingleSubmission() {
        File submissions = new File("src/test/resources/single");

        assertTrue(submissions.exists(), "Single submission folder must exist");

        assertThrows(Exception.class, () -> {
            jPlagService.runJPlag(submissions, "single-test", false);
        });
    }

    @Test
    @DisplayName("6. Concurrency: Handle parallel executions safely")
    void testParallelExecution() throws Exception {
        File submissions = new File("src/test/resources/STUDENT");

        assertTrue(submissions.exists(), "Test folder missing");

        final boolean[] completed = {false, false};

        Thread t1 = new Thread(() -> {
            try {
                jPlagService.runJPlag(submissions, "job1", false);
                completed[0] = true;
            } catch (Exception ignored) {}
        });

        Thread t2 = new Thread(() -> {
            try {
                jPlagService.runJPlag(submissions, "job2", false);
                completed[1] = true;
            } catch (Exception ignored) {}
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // ✅ Now actually validating
        assertTrue(completed[0] || completed[1], "At least one execution should succeed");
    }
}
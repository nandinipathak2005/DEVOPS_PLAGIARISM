// package com.project.service;

// import java.util.Arrays;
// import java.util.HashSet;
// import java.util.Set;

// import org.springframework.stereotype.Service;

// @Service
// public class PlagiarismService {

//     public double calculateSimilarity(String text1, String text2) {

//         if(text1 == null || text2 == null)
//             return 0.0;

//         String[] words1 = text1.toLowerCase().split("\\W+");
//         String[] words2 = text2.toLowerCase().split("\\W+");

//         Set<String> set1 = new HashSet<>(Arrays.asList(words1));
//         Set<String> set2 = new HashSet<>(Arrays.asList(words2));

//         Set<String> intersection = new HashSet<>(set1);
//         intersection.retainAll(set2);

//         Set<String> union = new HashSet<>(set1);
//         union.addAll(set2);

//         if(union.size() == 0)
//             return 0.0;

//         return (double) intersection.size() / union.size();
//     }
// }


package com.project.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class PlagiarismService {

    /**
     * Calculates similarity between two text strings.
     * Returns a value between 0.0 and 1.0.
     */
    public double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) return 0.0;

        String[] words1 = text1.toLowerCase().split("\\W+");
        String[] words2 = text2.toLowerCase().split("\\W+");

        Set<String> set1 = new HashSet<>(Arrays.asList(words1));
        Set<String> set2 = new HashSet<>(Arrays.asList(words2));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LectureExamples;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author don
 */
public class ChildEmpConstraintTest {

    static final String correct = "test/LectureExamples/Correct/";

    public ChildEmpConstraintTest() {
    }

    @Test
    public void cect() {
        RegTest.Utility.redirectStdOut("out.txt");
        try {
            ChildEmpConstraint.main(correct + "ex.ce.pl");
        } catch (RuntimeException e) {
        }
        RegTest.Utility.validate("out.txt", correct + "ex.ce.txt", false);
    }

}

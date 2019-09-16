package com.ge.exercise4;

import com.ge.exercise4.exception.EngineException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.ge.exercise4.constant.CommonConstants.CANNOT_REBUILD;
import static com.ge.exercise4.constant.CommonConstants.GEPASSPORT_ENGINE;
import static com.ge.exercise4.constant.CommonConstants.SERVICE_HOURS_EXCEEDED;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_EXCEED_MAX;
import static com.ge.exercise4.exception.ErrorCode.REBUILD_COUNT_EXCEEDS_MAX;
import static org.junit.Assert.assertEquals;

public class GEPassportTest {

    GEPassport testEngine;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    @Before
    public void setUp() throws EngineException {
        testEngine = new GEPassport("0001");
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    @Test
    public void toStringTest() {
        assertEquals("GEPassport SN: 0001", testEngine.toString());
    }

    @Test
    public void thrustToWeightRatioTest() {
        assertEquals(testEngine.takeoffThrust / testEngine.dryWeight, testEngine.thrustToWeightRatio(), 0.01);
    }

    @Test
    public void hoursBeforeRebuildTest() throws EngineException {
        assertEquals(50_000, testEngine.getHoursBeforeRebuild(), 0.01f);
        assertEquals(String.format(CANNOT_REBUILD, GEPASSPORT_ENGINE).concat("\n"), baos.toString());

        baos.reset();
        testEngine.setFlightHours(40_000);
        assertEquals(10_000, testEngine.getHoursBeforeRebuild(), 0.01f);
        assertEquals(String.format(CANNOT_REBUILD, GEPASSPORT_ENGINE).concat("\n"), baos.toString());
    }

    @Test
    public void getServiceHoursLeftTest() throws EngineException {
        double maxServiceLife = testEngine.flightHoursBeforeRebuild * (testEngine.maxNumRebuilds + 1);
        assertEquals(maxServiceLife, testEngine.getLeftServiceLife(), 0.01f);

        testEngine.setFlightHours(10_000);
        assertEquals(maxServiceLife - 10_000f, testEngine.getLeftServiceLife(), 0.01f);
    }

    @Test
    public void flightHoursExceedMaxPossibleTest() {
        try {
            testEngine = new GEPassport("0004", 95_000, 0);
        } catch (EngineException e) {
            assertEquals(FLIGHT_HOURS_EXCEED_MAX.toString(), e.getMessage());
        }
    }

    @Test
    public void maxRebuildsExceededTest() {
        try {
            testEngine = new GEPassport("0003", 45_000, 0);
        } catch (EngineException e) {
            assertEquals(REBUILD_COUNT_EXCEEDS_MAX.toString(), e.getMessage());
        }
    }
}
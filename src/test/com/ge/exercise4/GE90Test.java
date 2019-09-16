package com.ge.exercise4;

import com.ge.exercise4.exception.EngineException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.ge.exercise4.constant.CommonConstants.CANNOT_REBUILD;
import static com.ge.exercise4.constant.CommonConstants.GE90_ENGINE;
import static com.ge.exercise4.constant.CommonConstants.SERVICE_HOURS_EXCEEDED;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_EXCEED_MAX;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_INCONSISTENT;
import static com.ge.exercise4.exception.ErrorCode.REBUILD_COUNT_EXCEEDS_MAX;
import static org.junit.Assert.assertEquals;

public class GE90Test {

    GE90 testEngine;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();


    @Before
    public void setUp() throws EngineException {
        testEngine = new GE90("0001");
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    @Test
    public void toStringTest() {
        assertEquals("GE90 SN: 0001", testEngine.toString());
    }

    @Test
    public void thrustToWeightRatioTest() {
        assertEquals(testEngine.takeoffThrust / testEngine.dryWeight, testEngine.thrustToWeightRatio(), 0.01);
    }

    @Test
    public void hoursBeforeRebuildTest() throws EngineException {
        assertEquals(testEngine.flightHoursBeforeRebuild, testEngine.getHoursBeforeRebuild(), 0.01f);

        testEngine.setFlightHours(17_000);
        assertEquals(8_000, testEngine.getHoursBeforeRebuild(), 0.01f);

        testEngine = new GE90("0002", 35_000, 1);
        assertEquals(15_000, testEngine.getHoursBeforeRebuild(), 0.01f);

        testEngine = new GE90("0003", 80_000, 3);
        assertEquals(20_000, testEngine.getHoursBeforeRebuild(),0.01f);
        assertEquals(String.format(CANNOT_REBUILD, GE90_ENGINE).concat("\n"), baos.toString());

    }

    @Test
    public void getServiceHoursLeftTest() throws EngineException {
        double maxServiceLife = testEngine.flightHoursBeforeRebuild * (testEngine.maxNumRebuilds + 1);
        assertEquals(maxServiceLife, testEngine.getLeftServiceLife(), 0.01f);

        testEngine.setFlightHours(17_000);
        assertEquals(maxServiceLife - 17_000f, testEngine.getLeftServiceLife(), 0.01f);

        testEngine = new GE90("0004", 45_000, 1);
        assertEquals(maxServiceLife - 45_000, testEngine.getLeftServiceLife(), 0.01f);

        testEngine = new GE90("0004", 80_000, 3);
        assertEquals(20000, testEngine.getLeftServiceLife(), -0.01f);
    }

    @Test
    public void flightHoursExceededTest() {
        try {
            testEngine = new GE90("0005", 101_000, 3);
        } catch (EngineException e) {
            assertEquals(FLIGHT_HOURS_EXCEED_MAX.toString(), e.getMessage());
        }

    }

    @Test
    public void inconsistentFlightHours() {
        try {
            testEngine = new GE90("0004", 45_000, 1);
        } catch (EngineException e) {
            assertEquals(FLIGHT_HOURS_INCONSISTENT.toString(), e.getMessage());
        }

    }

    @Test
    public void maxRebuildsExceededTest() {
        try {
            testEngine = new GE90("0003", 80_000, 4);
        } catch (EngineException e) {
            assertEquals(REBUILD_COUNT_EXCEEDS_MAX.toString(), e.getMessage());
        }
    }
}
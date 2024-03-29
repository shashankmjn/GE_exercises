package com.ge.exercise4;

import com.ge.exercise4.exception.EngineException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.ge.exercise4.constant.CommonConstants.CANNOT_REBUILD;
import static com.ge.exercise4.constant.CommonConstants.GE90_ENGINE;
import static com.ge.exercise4.constant.CommonConstants.GENX_ENGINE;
import static com.ge.exercise4.constant.CommonConstants.SERVICE_HOURS_EXCEEDED;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_EXCEED_MAX;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_INCONSISTENT;
import static com.ge.exercise4.exception.ErrorCode.REBUILD_COUNT_EXCEEDS_MAX;
import static org.junit.Assert.assertEquals;

public class GENxTest {

    GENx testEngine;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();


    @Before
    public void setUp() throws EngineException {
        testEngine = new GENx("0001");
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    @Test
    public void toStringTest() {
        assertEquals("GENx SN: 0001", testEngine.toString());
    }

    @Test
    public void thrustToWeightRatioTest() {
        assertEquals(testEngine.takeoffThrust / testEngine.dryWeight, testEngine.thrustToWeightRatio(), 0.01);
    }

    @Test
    public void hoursBeforeRebuildTest() throws EngineException {
        assertEquals(testEngine.flightHoursBeforeRebuild, testEngine.getHoursBeforeRebuild(), 0.01f);

        testEngine.setFlightHours(17_000);
        assertEquals(3_000, testEngine.getHoursBeforeRebuild(), 0.01f);

        testEngine = new GENx("0002", 35_000, 1);
        assertEquals(5_000, testEngine.getHoursBeforeRebuild(), 0.01f);

        testEngine = new GENx("0003", 80_000, 3);
        assertEquals(0, testEngine.getHoursBeforeRebuild(),0.01f);

        testEngine = new GENx("0003", 90_000, 4);
        assertEquals(10_000, testEngine.getHoursBeforeRebuild(),0.01f);
        assertEquals(String.format(CANNOT_REBUILD, GENX_ENGINE).concat("\n"), baos.toString());

    }

    @Test
    public void getServiceHoursLeftTest() throws EngineException {
        double maxServiceLife = testEngine.flightHoursBeforeRebuild * (testEngine.maxNumRebuilds + 1);
        assertEquals(maxServiceLife, testEngine.getLeftServiceLife(), 0.01f);

        testEngine.setFlightHours(10_000);
        assertEquals(maxServiceLife - 10_000f, testEngine.getLeftServiceLife(), 0.01f);

        testEngine = new GENx("0004", 95_000, 4);
        assertEquals(maxServiceLife - 95_000, testEngine.getLeftServiceLife(), -0.01f);

        testEngine = new GENx("0005", 100_000, 4);
        assertEquals(maxServiceLife - 100_000, testEngine.getLeftServiceLife(), -0.01f);
    }

    @Test
    public void maxFlightHoursExceededTest() {
        try {
            testEngine = new GENx("0003", 81_200, 4);
        } catch (EngineException e) {
            assertEquals(FLIGHT_HOURS_EXCEED_MAX.toString(), e.getMessage());
        }
    }

    @Test
    public void maxRebuildsExceededTest() {
        try {
            testEngine = new GENx("0003", 70_000, 5);
        } catch (EngineException e) {
            assertEquals(REBUILD_COUNT_EXCEEDS_MAX.toString(), e.getMessage());
        }
    }

    @Test
    public void flightHoursInconsistentTest() {
        try {
            testEngine = new GENx("0003", 90_000, 4);
        } catch (EngineException e) {
            assertEquals(FLIGHT_HOURS_INCONSISTENT.toString(), e.getMessage());
        }
    }
}
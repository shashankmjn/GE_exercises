package com.ge.exercise4;

import com.ge.exercise4.exception.EngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ge.exercise4.constant.CommonConstants.CANNOT_REBUILD;
import static com.ge.exercise4.constant.CommonConstants.GENX_ENGINE;
import static com.ge.exercise4.constant.CommonConstants.SERVICE_HOURS_EXCEEDED;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_EXCEED_MAX;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_INCONSISTENT;
import static com.ge.exercise4.exception.ErrorCode.REBUILD_COUNT_EXCEEDS_MAX;

public class GENx {
    private static final Logger logger = LogManager.getLogger(GENx.class);

    private static final String ENGINE_MODEL = GENx.class.getSimpleName();
    private final String serialNumber;

    public final int maxNumRebuilds = 4;
    public final double flightHoursBeforeRebuild = 20_000;
    public final double dryWeight = 13_552;
    public final double wetWeight = 14_103;
    public final double takeoffThrust = 74_170;

    private double flightHours;
    private int numRebuilds;

    public GENx(String serialNumber, double flightHours, int numRebuilds) throws EngineException {
        this.serialNumber = serialNumber;
        validateEnteredFlightHours(flightHours, numRebuilds);
        this.flightHours = flightHours;
        this.numRebuilds = numRebuilds;
    }

    public GENx(String serialNumber, double flightHours) throws EngineException {
        this(serialNumber, flightHours, 0);
    }

    public GENx(String serialNumber) throws EngineException {
        this(serialNumber, 0.0);
    }

    public double getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(double flightHours) {
        this.flightHours = flightHours;
    }

    public double thrustToWeightRatio() {
        return takeoffThrust / dryWeight;
    }

    public String toString() {
        return ENGINE_MODEL + " SN: " + serialNumber;
    }

    public double getHoursBeforeRebuild() {
        if(numRebuilds >= maxNumRebuilds)
            System.out.println(String.format(CANNOT_REBUILD, GENX_ENGINE));
        return ((numRebuilds+1) * flightHoursBeforeRebuild) - flightHours;
    }

    public double getLeftServiceLife() {
        double maxFlightHours = (maxNumRebuilds+1) * flightHoursBeforeRebuild;
        return maxFlightHours - flightHours;
    }

    private void validateEnteredFlightHours(double flightHours, int rebuiltCount) throws EngineException {
        double maxFlightHours = (maxNumRebuilds+1) * flightHoursBeforeRebuild;
        if(maxFlightHours < flightHours)
            throw new EngineException(FLIGHT_HOURS_EXCEED_MAX);
        if(rebuiltCount > maxNumRebuilds)
            throw new EngineException(REBUILD_COUNT_EXCEEDS_MAX);
        if(Math.floor(flightHours / flightHoursBeforeRebuild) < rebuiltCount)
            throw new EngineException(FLIGHT_HOURS_INCONSISTENT);

    }
}

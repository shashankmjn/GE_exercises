package com.ge.exercise4;

import com.ge.exercise4.exception.EngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ge.exercise4.constant.CommonConstants.CANNOT_REBUILD;
import static com.ge.exercise4.constant.CommonConstants.GEPASSPORT_ENGINE;
import static com.ge.exercise4.constant.CommonConstants.SERVICE_HOURS_EXCEEDED;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_EXCEED_MAX;
import static com.ge.exercise4.exception.ErrorCode.FLIGHT_HOURS_INCONSISTENT;
import static com.ge.exercise4.exception.ErrorCode.REBUILD_COUNT_EXCEEDS_MAX;

public class GEPassport {
    private static final Logger logger = LogManager.getLogger(GEPassport.class);

    private static final String ENGINE_MODEL = GEPassport.class.getSimpleName();
    private final String serialNumber;

    public final int maxNumRebuilds = 0;
    public final double flightHoursBeforeRebuild = 50_000;
    public final double dryWeight = 4_505;
    public final double wetWeight = 5_230;
    public final double takeoffThrust = 20_650;

    private double flightHours;
    private int numRebuilds;

    public GEPassport(String serialNumber, double flightHours, int numRebuilds) throws EngineException {
        this.serialNumber = serialNumber;
        validateEnteredFlightHours(flightHours, numRebuilds);
        this.flightHours = flightHours;
        this.numRebuilds = numRebuilds;
    }

    public GEPassport(String serialNumber, double flightHours) throws EngineException {
        this(serialNumber, flightHours, 0);
    }

    public GEPassport(String serialNumber) throws EngineException {
        this(serialNumber, 0.0);
    }

    public double getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(double flightHours) throws EngineException {
        validateEnteredFlightHours(flightHours, numRebuilds);
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
            System.out.println(String.format(CANNOT_REBUILD, GEPASSPORT_ENGINE));
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

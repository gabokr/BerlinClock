package com.ubs.opsit.interviews;

import java.util.ArrayList;
import java.util.List;

public class TimeConverterImpl implements TimeConverter {

	// input time format
	private static final int HOURS_POSITION_IDX = 0;
	private static final int MINUTES_POSITION_IDX = 1;
	private static final int SECONDS_POSITION_IDX = 2;
	
	// light block duration
	private static final int FIRST_ROW_HOURS_1BLOCK_DURATION = 5;
	private static final int FIRST_ROW_MINUTES_1BLOCK_DURATION = 5;
	
	// light colors
	private static final String LIGHT_COLOR_RED = "R";
	private static final String LIGHT_COLOR_YELLOW = "Y";
	private static final String QUARTER_YYY_LIGHT_SEQUENCE = "YYY";
	private static final String QUARTER_YYR_LIGHT_SEQUENCE = "YYR";
	private static final String LIGHT_OFF = "O";
	
	// clock rows' dimensions
	private static final int FIRST_ROW_HOURS_COUNT = 4;
	private static final int SECOND_ROW_HOURS_COUNT = 4;
	private static final int FIRST_ROW_MINUTES_COUNT = 11;
	private static final int SECOND_ROW_MINUTES_COUNT = 4;
	
	private static final int SECONDS_LIGHT_INTERVAL = 2;
	
	@Override
	public String convertTime(String aTime) {
		List<Integer> listOfTimeUnits = getListOfTimeUnits(aTime);

		StringBuffer sb = new StringBuffer();
		appendBerlinClockTimeUnit(sb, getBerlinClockSeconds(listOfTimeUnits.get(SECONDS_POSITION_IDX)));
		appendBerlinClockTimeUnit(sb, getBerlinClockFirstRowHours(listOfTimeUnits.get(HOURS_POSITION_IDX)));
		appendBerlinClockTimeUnit(sb, getBerlinClockSecondRowHours(listOfTimeUnits.get(HOURS_POSITION_IDX)));
		appendBerlinClockTimeUnit(sb, getBerlinClockFirstRowMinutes(listOfTimeUnits.get(MINUTES_POSITION_IDX)));
		appendBerlinClockTimeUnit(sb, getBerlinClockSecondRowMinutes(listOfTimeUnits.get(MINUTES_POSITION_IDX)));

		return sb.toString();
	}

	/**
	 * Get first row representation 
	 * @param value hour part of the time
	 * @return full row representation of hours in first row
	 */
	private String getBerlinClockFirstRowHours(int value) {
		return getLampRowRepresentation(FIRST_ROW_HOURS_COUNT, getCountOfLightsOn(value, FIRST_ROW_HOURS_1BLOCK_DURATION), LIGHT_COLOR_RED);
	}

	/**
	 * Get second row representation 
	 * @param value hour part of the time
	 * @return full row representation of hours in second row
	 */
	private String getBerlinClockSecondRowHours(int value) {
		return getLampRowRepresentation(SECOND_ROW_HOURS_COUNT, value % FIRST_ROW_HOURS_1BLOCK_DURATION, LIGHT_COLOR_RED);
	}

	/**
	 * Get third row representation 
	 * @param value minute part of the time
	 * @return full row representation of minutes in third row
	 */
	private String getBerlinClockFirstRowMinutes(int value) {
		String rowWithYellowLightsOn = getLampRowRepresentation(
				FIRST_ROW_MINUTES_COUNT,
				getCountOfLightsOn(value, FIRST_ROW_MINUTES_1BLOCK_DURATION),
				LIGHT_COLOR_YELLOW);
		// check if at least one quarter passed, change quarter light color to red
		if (rowWithYellowLightsOn.length() >= 3) {
			String minutesWithQuartersInRed = rowWithYellowLightsOn.replace(QUARTER_YYY_LIGHT_SEQUENCE, QUARTER_YYR_LIGHT_SEQUENCE);
			return minutesWithQuartersInRed;
		}
		return rowWithYellowLightsOn;
	}

	/**
	 * Get fourth row representation 
	 * @param value minute part of the time
	 * @return full row representation of minutes in fourth row
	 */
	private String getBerlinClockSecondRowMinutes(int value) {
		return getLampRowRepresentation(SECOND_ROW_MINUTES_COUNT, value % FIRST_ROW_MINUTES_1BLOCK_DURATION, LIGHT_COLOR_YELLOW);
	}

	/**
	 * Get top light (seconds) representation
	 * @param value second part of the time
	 * @return light is on (yellow) or off
	 */
	private String getBerlinClockSeconds(int value) {
		if (value % SECONDS_LIGHT_INTERVAL == 0)
			return LIGHT_COLOR_YELLOW;
		else
			return LIGHT_OFF;
	}
	
	/**
	 * Render each light inside the row. Light is either on or off and can have a specific color.
	 * @param totalLampsInRow
	 * @param countOfLampsToTurnOn
	 * @param lightColor
	 * @return
	 */
	private String getLampRowRepresentation(int totalLampsInRow, int countOfLampsToTurnOn, String lightColor) {
        StringBuffer sb = new StringBuffer();
        addLightOn(countOfLampsToTurnOn, lightColor, sb);
        addLightOff(totalLampsInRow, countOfLampsToTurnOn, sb);
        return sb.toString();
    }

	private void addLightOn(int countOfLampsToTurnOn, String lightColor, StringBuffer sb) {
		for (int i = 0; i < countOfLampsToTurnOn; i++) {
			sb.append(lightColor);
		}
	}
	
	private void addLightOff(int totalLampsInRow, int countOfLampsToTurnOn, StringBuffer sb) {
		for (int i = 0; i < (totalLampsInRow - countOfLampsToTurnOn); i++) {
			sb.append(LIGHT_OFF);
		}
	}

	/**
	 * Get number of lamps inside one row to turn on.
	 * @param duration time duration measured in one of the possible time units, i.e. hour or minute
	 * @param blockDuration one lamp duration representation
	 * @return number of lamps to turn on
	 */
	private int getCountOfLightsOn(int duration, int blockDuration) {
		int maxCompleteLightBloxCount = duration - (duration % blockDuration);
		return maxCompleteLightBloxCount / blockDuration;
	}
	
	/**
	 * Append a new row to Berlin clock representation.
	 * @param sb source string buffer with the clock content
	 * @param valueToAdd
	 */
	private void appendBerlinClockTimeUnit(StringBuffer sb, String valueToAdd) {
		sb.append(valueToAdd);
		sb.append(System.lineSeparator());
	}

	/**
	 * Separate hour, minute and seconds from the input time.
	 * @param timeString
	 * @return List containing the time units in this order: hour, minute, second
	 */
	private List<Integer> getListOfTimeUnits(String timeString) {
		List<Integer> listOfTimeUnits = new ArrayList<>();
		for (String timeUnit : timeString.split(":")) {
			listOfTimeUnits.add(Integer.parseInt(timeUnit));
		}
		return listOfTimeUnits;
	}
	
}

package org.eqasim.wayne_county.mode_choice.utilities.predictors;

import org.matsim.api.core.v01.population.Person;

public class WayneCountyPredictorUtils {

	static public int hhlIncomeClass(Person person) {
		int hhlIncomeClass = (int) ((String)person.getAttributes().getAttribute("income")).charAt(3);
		return hhlIncomeClass;
	}
}

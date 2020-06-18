package org.eqasim.jakarta.mode_choice.utilities.estimators;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.utilities.estimators.CarUtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.CarPredictor;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.PersonPredictor;
import org.eqasim.core.simulation.mode_choice.utilities.variables.CarVariables;
import org.eqasim.jakarta.mode_choice.parameters.SaoPauloModeParameters;
import org.eqasim.jakarta.mode_choice.utilities.predictors.JakartaPersonPredictor;
import org.eqasim.jakarta.mode_choice.utilities.variables.JakartaPersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;

import com.google.inject.Inject;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;

public class SaoPauloCarUtilityEstimator extends CarUtilityEstimator {
	private final SaoPauloModeParameters parameters;
	private final JakartaPersonPredictor predictor;
	private final CarPredictor carPredictor;

	@Inject
	public SaoPauloCarUtilityEstimator(SaoPauloModeParameters parameters, PersonPredictor personPredictor,
			CarPredictor carPredictor, JakartaPersonPredictor predictor) {
		super(parameters, carPredictor);
		this.carPredictor = carPredictor;
		this.parameters = parameters;
		this.predictor = predictor;
	}
	
	protected double estimateRegionalUtility(JakartaPersonVariables variables) {
		return (variables.cityTrip) ? parameters.spCar.alpha_car_city : 0.0;
	}

	@Override
	public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		JakartaPersonVariables variables = predictor.predictVariables(person, trip, elements);
		CarVariables variables_car = carPredictor.predict(person, trip, elements);

		double utility = 0.0;

		utility += estimateConstantUtility();
		utility += estimateTravelTimeUtility(variables_car);
		utility += estimateRegionalUtility(variables);
		utility += estimateAccessEgressTimeUtility(variables_car);
		if (variables.hhlIncome == 0.0)
			utility += estimateMonetaryCostUtility(variables_car)
			* (parameters.spAvgHHLIncome.avg_hhl_income / 1.0);
		else
			utility += estimateMonetaryCostUtility(variables_car)
				* (parameters.spAvgHHLIncome.avg_hhl_income / variables.hhlIncome);

		return utility;
	}

}
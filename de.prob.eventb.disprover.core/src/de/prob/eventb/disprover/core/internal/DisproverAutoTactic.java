package de.prob.eventb.disprover.core.internal;

import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.eventbExtensions.AutoTactics.AbsractLazilyConstrTactic;
import org.eventb.core.seqprover.tactics.BasicTactics;

import de.prob.eventb.disprover.core.DisproverReasonerInput;


public class DisproverAutoTactic extends AbsractLazilyConstrTactic {
	@Override
	protected ITactic getSingInstance() {
		return BasicTactics.reasonerTac(new DisproverReasoner(),
				new DisproverReasonerInput());
	}
}
package util;

public enum ReWritingRules {
	FARM_INTRO("FI"), FARM_ELIM("FE"), PIPE_INTRO("PI"), PIPE_ELIM("PE")
	,PIPE_ASSOC("PA"),PIPE_DIST("PD"), MAP_DIST("MD"), MAP_ELIM("ME"), PIPE_OF_MAP("PM"), MAP_OF_PIPE("MP"),MAP_OF_COMP("MC"); 
	
	private final String rule;
	private ReWritingRules(String rule) {
		this.rule =rule;
	}
	public String getRule() {
		return rule;
	}
	
}

package jopt;

import edu.harvard.econcs.jopt.solver.IMIP;
import edu.harvard.econcs.jopt.solver.IMIPResult;
import edu.harvard.econcs.jopt.solver.client.SolverClient;
import edu.harvard.econcs.jopt.solver.mip.CompareType;
import edu.harvard.econcs.jopt.solver.mip.Constraint;
import edu.harvard.econcs.jopt.solver.mip.MIP;
import edu.harvard.econcs.jopt.solver.mip.VarType;
import edu.harvard.econcs.jopt.solver.mip.Variable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple JOpt usage example:<br>
 * <pre>
 * MIN x^2+y^2
 * subject to
 * X - 2Y >= 5
 * Y^2 <= 4
 * </pre>
 * 
 * @author Ben Lubin; Last modified by $Author: blubin $
 * @version $Revision: 1.1 $ on $Date: 2013/12/04 03:16:20 $
 * @since Dec 3, 2013
 **/
public class SimpleQCPExample {

    private static final Logger logger = LogManager.getLogger(SimpleQCPExample.class);

    private IMIP mip;
	
    public SimpleQCPExample() {
    }
    
    public IMIP getMIP() {
    	return mip;
    }
    
    public void buildMIP() {
    	mip = new MIP();
    	
    	Variable x = new Variable("x", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);
        Variable y = new Variable("y", VarType.DOUBLE, -MIP.MAX_VALUE, MIP.MAX_VALUE);
        
        mip.add(x);
        mip.add(y);
        
        mip.setObjectiveMax(false);
        mip.addObjectiveTerm(1, x, x);
        mip.addObjectiveTerm(1, y, y);

        Constraint c1 = new Constraint(CompareType.GEQ, 5);
        c1.addTerm(1, x);
        c1.addTerm(2, y);
        mip.add(c1);

        Constraint c2 = new Constraint(CompareType.LEQ, 4);
        c2.addTerm(1, y, y);
        mip.add(c2);   
        /*x*y = [(x + y)^2 - (x - y)^2]/4
		So, you introduce constraints
		
		u = (x + y)/2
		
		v = (x - y)/2
		         * 
		         * product = 0
acopy = a
while acopy < 0:
    product = product - b
    acopy = acopy + 1
while acopy > 0:
    product = product + b
    acopy = acopy - 1
		         * */
        
        Constraint c3 = new Constraint(CompareType.GEQ, 3);
        c3.addTerm(1, y, x);
        mip.add(c3); 
       System.out.println("quad terms");
       c3.getQuadraticTerms();
    }
    
    public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
    	return solverClient.solve(mip);
    }

    public static void main(String[] argv) {
    	SimpleQCPExample example = new SimpleQCPExample();
    	example.buildMIP();
        IMIP mip = example.getMIP();
        logger.info(mip);
        System.out.println(mip);
        IMIPResult result = example.solve();
        logger.info(result);
        System.out.println(result);
    }
}

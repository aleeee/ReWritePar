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
 * Y = 2
 * Y=XY
 * </pre>
 * 
 * @author Ben Lubin; Last modified by $Author: blubin $
 * @version $Revision: 1.3 $ on $Date: 2013/12/04 03:16:20 $
 * @since Dec 3, 2013
 **/
public class SimpleExample {

    private static final Logger logger = LogManager.getLogger(SimpleExample.class);

    private IMIP mip;
    
	
    public SimpleExample() {
    }
    
    public IMIP getMIP() {
    	return mip;
    }
    
    public void buildMIP() {
    	mip = new MIP();
    	
    	Variable x = new Variable("x", VarType.DOUBLE, 1, 20);
        Variable y = new Variable("y", VarType.DOUBLE, -1, 20);
        
        mip.add(x);
        mip.add(y);
        
        mip.setObjectiveMax(false);
        mip.addObjectiveTerm(1, x, x);
        mip.addObjectiveTerm(1, y, y);
//
        Constraint c1 = new Constraint(CompareType.GEQ, 5);
        c1.addTerm(1, x);
        c1.addTerm(2, y);
        mip.add(c1);

        Constraint c2 = new Constraint(CompareType.EQ, 3);
        c2.addTerm(1, y);
        mip.add(c2);
        

        Constraint c3 = new Constraint(CompareType.LEQ, 20);
        c3.addTerm(1, x,y);
        mip.add(c3);
              
    }
    
    public IMIPResult solve() {
		SolverClient solverClient = new SolverClient();
    	return solverClient.solve(mip);
    }

    public static void main(String[] argv) {
    	SimpleExample example = new SimpleExample();
    	example.buildMIP();
        IMIP mip = example.getMIP();
        logger.info(mip);
        System.out.println(mip);
        IMIPResult result = example.solve();
        logger.info(result);
        System.out.println(result);
    }
}
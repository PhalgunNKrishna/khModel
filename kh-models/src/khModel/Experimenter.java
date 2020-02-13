package khModel;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Experimenter implements Steppable {

	
	int n = 0;
	double sX = 0;
	double sY = 0;
	double sX2 = 0;
	double sY2 = 0;
	double sXY = 0;
	public Stoppable event;

		/**
		 * Takes two agents and gets their attractiveness values and then does the 
		 * calculation for preparing the data to calculate a correlation value	
		 * @param x
		 * @param y
		 */
	public void getData(Agent x, Agent y) {
		getData(x.attractiveness, y.attractiveness);
	}
	
	public void getData(double x, double y){
		sXY += x*y;
		sX += x;
		sY += y;
		sX2 += x*x;
		sY2 += y*y;
		n++;

	}
	
	
	public double correlation(){
		return (sXY - (sX*sY)/n)/Math.sqrt((sX2-(sX*sX)/n)*(sY2-(sY*sY)/n));
	}
	
	public void printData(Environment state) {
		int percent = (int)((n/(double)state.females)*100.0);
		if(n>1) {
			System.out.println(state.schedule.getSteps() +"      "+percent+"      "+correlation() + "     "+(sX+sY)/(2*n));
		}
	}
	
	public void stop(Environment state) {
		Bag agents = state.space.getAllObjects();
		if(agents == null || agents.numObjs == 0) {
			event.stop();
		}
	}
	
	public void populations(Environment state) {
		Bag tempMale, tempFemale;//temp bags to hold agents
		tempMale = state.male;
		tempFemale = state.female;
		state.male = state.nextMale;
		state.female = state.nextFemale;
		state.nextFemale = tempFemale;
		state.nextMale = tempMale;
		state.nextFemale.clear();
		state.nextMale.clear();
		for(int i=0; i<state.male.numObjs;i++) {
			Agent a = (Agent)state.male.objs[i];
			a.dated = false;
		}
		for(int i=0; i<state.female.numObjs;i++) {
			Agent a = (Agent)state.female.objs[i];
			a.dated = false;
		}
	}
	
	public void step(SimState state) {
		Environment environment = (Environment)state;
		stop(environment);
		if(state.schedule.getSteps() ==1) {
			System.out.println();//create a return
			System.out.println("step    %          r       attractiveness");
		}
		printData(environment);
		populations(environment);
	}

}

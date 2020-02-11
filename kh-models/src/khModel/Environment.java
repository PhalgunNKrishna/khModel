package khModel;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;

public class Environment extends SimState {
	public SparseGrid2D space;//space for the agents
	public int gridWidth = 50;//dimensions for the space
	public int gridHeight = 50;
	public int n = 50;  //n is the number of agents
	public double active = 1.0;//probability of activity
	public double p = 0.0; //random movement
	public boolean oneCellPerAgent = false;//controls whether agents can occupy the same place or not
	public double aggregate = 0.0;//probability of aggregating
	public int searchRadius = 1;// the radius or view of an agent when aggregating

	/*
	 * KH Model parameters
	 */
	public boolean nonSpatialModel = true;
	public int males = 1000;
    public int females = 1000;
    public double maxAttractiveness = 10;
    public double choosiness =3;
    public double maxDates = 50;
    public Rule rule= Rule.ATTRACTIVE;
    public int ruleNumber = 0;
    Experimenter experimenter = null;
    AgentGUI gui = null;
    Bag male = new Bag();
    Bag female = new Bag();
    Bag nextMale = new Bag();
    Bag nextFemale = new Bag();

	public Environment(long seed) {
		super(seed);
	}


	public boolean isNonSpatialModel() {
		return nonSpatialModel;
	}


	public void setNonSpatialModel(boolean nonSpatialModel) {
		this.nonSpatialModel = nonSpatialModel;
	}


	public int getMales() {
		return males;
	}

	public void setMales(int males) {
		this.males = males;
	}

	public int getFemales() {
		return females;
	}

	public void setFemales(int females) {
		this.females = females;
	}

	public double getMaxAttractiveness() {
		return maxAttractiveness;
	}

	public void setMaxAttractiveness(double maxAttractiveness) {
		this.maxAttractiveness = maxAttractiveness;
	}

	public double getChoosiness() {
		return choosiness;
	}

	public void setChoosiness(double choosiness) {
		this.choosiness = choosiness;
	}

	public double getMaxDates() {
		return maxDates;
	}

	public void setMaxDates(double maxDates) {
		this.maxDates = maxDates;
	}

	public int getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(int ruleNumber) {
		this.ruleNumber = ruleNumber;
		this.rule = (Rule.values())[ruleNumber];
	}

	public Object domRuleNumber() {
		return Rule.values();
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getActive() {
		return active;
	}

	public void setActive(double active) {
		this.active = active;
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public boolean isOneCellPerAgent() {
		return oneCellPerAgent;
	}

	public void setOneCellPerAgent(boolean oneCellPerAgent) {
		this.oneCellPerAgent = oneCellPerAgent;
	}

	public double getAggregate() {
		return aggregate;
	}

	public void setAggregate(double aggregate) {
		this.aggregate = aggregate;
	}

	public int getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(int searchRadius) {
		this.searchRadius = searchRadius;
	}

	/**
	 * Creates agents and places them in space and on the schedule.
	 */
	public void makeAgentsSpatial() {
		for(int i=0;i<n;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			Object o = space.getObjectsAtLocation(x, y);
			while(this.oneCellPerAgent && o!=null) {
				x = random.nextInt(gridWidth);
				y = random.nextInt(gridHeight);
				o = space.getObjectsAtLocation(x, y);
			}
			int dirx  = random.nextInt(3)-1;
			int diry = random.nextInt(3)-1;
			Agent a = new Agent(x,y,dirx,diry);
			space.setObjectLocation(a, x, y);
			schedule.scheduleRepeating(a);
		}
	}
	
	public void makeAgentNonSpatial() {
		for(int i=0;i<females;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent f = new Agent(x, y, true,attractiveness);
			f.event = schedule.scheduleRepeating(f);
			space.setObjectLocation(f,random.nextInt(gridWidth), random.nextInt(gridHeight));
			gui.setOvalPortrayal2DColor(f, (float)1, (float)0, (float)0, (float)(attractiveness/maxAttractiveness));
			female.add(f);
		}
		for(int i=0;i<males;i++) {
			int x = random.nextInt(gridWidth);
			int y = random.nextInt(gridHeight);
			double attractiveness = random.nextInt((int)maxAttractiveness)+1;
			Agent m = new Agent(x, y, false,attractiveness);
			m.event = schedule.scheduleRepeating(m);
			space.setObjectLocation(m,random.nextInt(gridWidth), random.nextInt(gridHeight));
			gui.setOvalPortrayal2DColor(m, (float)0, (float)0, (float)1, (float)(attractiveness/maxAttractiveness));
			male.add(m);
		}
	}
	
	public void makeAgents() {
		if(nonSpatialModel) {
			makeAgentNonSpatial();
			
		}
		else {
			makeAgentsSpatial();
		}
	}

	public void start() {
		super.start();
		space = new SparseGrid2D(gridWidth,gridHeight); //create a new space
		experimenter  = new Experimenter();
		experimenter.event = schedule.scheduleRepeating(1, 2, experimenter);
		makeAgents();
	}
}

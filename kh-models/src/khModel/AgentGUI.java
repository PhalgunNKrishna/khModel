package khModel;

import java.awt.Color;

import sim.engine.SimState;
import sweep.GUISimState;

public class AgentGUI extends GUISimState {

	public AgentGUI(SimState state, String spaceName, int gridWidth, int gridHeight, Color backdrop,
			Color agentDefaultColor, boolean agentPortrayal) {
		super(state, spaceName, gridWidth, gridHeight, backdrop, agentDefaultColor, agentPortrayal);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Environment s = (Environment)AgentGUI.initialize(Environment.class, AgentGUI.class, 600, 600, Color.WHITE, Color.BLUE, false, "space");
		s.gui = (AgentGUI)gui;
	}

}

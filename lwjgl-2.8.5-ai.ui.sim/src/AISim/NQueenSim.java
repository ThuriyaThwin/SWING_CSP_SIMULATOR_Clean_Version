package AISim;

import java.util.Iterator;
import java.util.List;

import lab4.nqueen.NQueenApp;
import lab4.nqueen.NQueenResultFactory;
import lab4.nqueen.NQueensState;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import aima.core.agent.Action;
import aima.core.util.datastructure.XYLocation;

public class NQueenSim {

	public NQueensState			nQueenState;
	private Iterator<Action>	currentAction;

	long						lastFrame;
	int							fps;
	long						lastFPS;

	long						lastMouseClick	= 0;
	Texture						texture;

	public void start(NQueensState state, Iterator<Action> currentAction) {
		try {
			Display.setDisplayMode(new DisplayMode(1000, 800));
			Display.create();
			//texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/image.png"));
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		nQueenState = state;
		this.currentAction = currentAction;
		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer

		while (!Display.isCloseRequested()) {
			int delta = getDelta();

			if (Mouse.isButtonDown(0)) {
				if ((System.currentTimeMillis() - lastMouseClick) > 400) {
					updateNQueenState();
					lastMouseClick = System.currentTimeMillis();
				}
			}
			update(delta);
			draw();

			Display.update();
			Display.sync(60); // cap fps to 60fps
		}

		Display.destroy();
	}

	public void updateNQueenState() {
		if (currentAction.hasNext())
			nQueenState = (NQueensState) (new NQueenResultFactory()).result(nQueenState, currentAction.next());
	}

	long	timeUpdater	= 0;

	public void update(int delta) {

		timeUpdater++;
		//System.out.println("timeUpdater = " + timeUpdater);
		if ((timeUpdater % 100) == 0) {
			//updateNQueenState();
		}

		updateFPS(); // update FPS Counter
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */

	void draw() {
		GL11.glClearColor(1.0f, 0.0f, 1.0f, 0.7f);

		drawBoard();
		drawQueen();
	}

	public final int	warp	= 20;

	private void drawBoard() {

		for (int i = 0; i < nQueenState.getSize(); i++) {
			for (int j = 0; j < nQueenState.getSize(); j++) {
				GL11.glColor3f(0.5f, 0.5f, 1.0f);

				int X1 = (i * 100) + warp;
				int Y1 = (j * 100) + warp;

				int X2 = (X1 + 100) - warp;
				int Y2 = Y1;

				int X3 = (X1 + 100) - warp;
				int Y3 = (Y1 + 100) - warp;

				int X4 = X1;
				int Y4 = (Y1 + 100) - warp;

				GL11.glBegin(GL11.GL_QUADS);
				{
					GL11.glVertex2f(X1, Y1);
					GL11.glVertex2f(X2, Y2);
					GL11.glVertex2f(X3, Y3);
					GL11.glVertex2f(X4, Y4);
				}
				GL11.glEnd();
			}
		}
	}

	private void drawQueen() {

		List<XYLocation> l = nQueenState.getQueenPositions();

		for (XYLocation xyLocation : l) {

			int i = xyLocation.getXCoOrdinate();
			int j = xyLocation.getYCoOrdinate();

			GL11.glColor3f(0.0f, 1.0f, 0.0f);

			int X1 = (i * 100) + warp + 5;
			int Y1 = (j * 100) + warp + 5;

			int X2 = (X1 + 100) - warp - 10;
			int Y2 = Y1;

			int X3 = (X1 + 100) - warp - 10;
			int Y3 = (Y1 + 100) - warp - 10;

			int X4 = X1;
			int Y4 = (Y1 + 100) - warp - 10;

			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2f(X1, Y1);
				GL11.glVertex2f(X2, Y2);
				GL11.glVertex2f(X3, Y3);
				GL11.glVertex2f(X4, Y4);
			}
			GL11.glEnd();
		}

	}

	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if ((getTime() - lastFPS) > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void initGL() {

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	}

	public static void main(String[] argv) throws Exception {

		NQueensState start = new NQueensState(4);
		start.setRandomBoard();
		try {

			NQueenApp app = new NQueenApp();
			app.start = start;
			//List<Action> a  = app.getActionsSimulatedAnnealingSearch();
			List<Action> a = app.getActionsHillClimbingSearch(null);

			NQueenSim queenExample = new NQueenSim();
			queenExample.start(start, a.iterator());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
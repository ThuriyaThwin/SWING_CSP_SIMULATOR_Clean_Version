package AISim;

import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import lab5.Csp.Sudoku.ISodokuListener;
import lab5.Csp.Sudoku.SodokuBoard;
import lab5.Csp.Sudoku.SodokuVariable;
import lab5.Csp.Sudoku.SodokuVariableCollection;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.ImprovedBacktrackingStrategy.Inference;
import aima.core.search.csp.ImprovedBacktrackingStrategy.Selection;

public class SodokuApp {
	private static final int		height	= 360;
	private static final int		width	= 360;

	public static final int			WIDTH	= 30;
	public static final int			HEIGHT	= 30;
	public static final int			WARP	= 3;

	public SodokuBoard				csp;
	public SodokuVariableCollection	var;
	private int						counter	= 0;

	public SodokuApp(String string) {
		var = new SodokuVariableCollection(string);
		csp = new SodokuBoard(var);
	}

	public void solver() {

		ImprovedBacktrackingStrategy solver2 = new ImprovedBacktrackingStrategy();

		solver2.setVariableSelection(Selection.MRV);
		solver2.setInference(Inference.FORWARD_CHECKING);

		System.out.println("Apply Backtracking Strategy: ImprovedBacktrackingStrategy");

		ISodokuListener listener = new ISodokuListener() {

			@Override
			public void setVariable(SodokuVariable v, int assign) {
				counter++;
				v.value = assign;
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		csp.setSodokuListener(listener);

		// Solve the Problem, and Get the result
		long start = System.currentTimeMillis();
		Assignment results = solver2.solve(csp);
		long finish = System.currentTimeMillis();
		System.out.println("counter = " + counter);
		long diff = finish - start;

		System.out.println("execute time " + (diff / 1000) + " second");

		// Print the output
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				SodokuVariable v = var.get(i, j);
				v.value = (Integer) results.getAssignment(v);
			}
		}

		System.out.println("\n------->>  result  <<-----------");
		System.out.println(var.getString());

	}

	public void start() {

		initGL();
		initFont();

		new Thread(new Runnable() {

			@Override
			public void run() {
				solver();
			}
		}).start();

		while (!Display.isCloseRequested()) {
			// Clear the screen and depth buffer

			int delta = getDelta();
			update(delta);

			drawGL();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();
	}

	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		Display.setTitle("Sodoku Simulator");
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

	}

	private TrueTypeFont	font;
	private int				speed;

	private void initFont() {
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
		font = new TrueTypeFont(awtFont, true);
	}

	private void drawGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.29f, 0.76f, 1);
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(0, 360, 0);
			GL11.glRotatef(-90, 0, 0, 1);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {

					drawColor(i + 1, j + 1);
					GL11.glBegin(GL11.GL_QUADS);
					{
						int x = (i + 1) * (WIDTH + WARP);
						int y = (j + 1) * (HEIGHT + WARP);

						int X1 = x, Y1 = y;
						int X2 = x + WIDTH, Y2 = y;
						int X3 = x + WIDTH, Y3 = y + HEIGHT;
						int X4 = x, Y4 = y + HEIGHT;

						GL11.glVertex2f(X1, Y1);
						GL11.glVertex2f(X2, Y2);
						GL11.glVertex2f(X3, Y3);
						GL11.glVertex2f(X4, Y4);

					}
					GL11.glEnd();
				}
			}
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					SodokuVariable v = var.get(i, j);
					if (v.value != 0) {
						if (!v.fixed) {
							drawString(String.valueOf(v.value), i, j, Color.red);
						} else {
							drawString(String.valueOf(v.value), i, j, Color.green);
						}
					}

				}

			}
			//GL11.glTranslatef(150, 150, 0);
		}
		GL11.glPopMatrix();
	}

	public void drawString(String text, int i, int j, Color color) {

		int x = ((i + 1) * (WIDTH + WARP)) + (WARP * 3);
		int y = ((j + 1) * (HEIGHT + WARP)) + (WARP * 3);

		GL11.glPushMatrix();
		{
			GL11.glTranslatef(x, y, 0);
			GL11.glScalef(0.7f, -0.7f, 0.7f);
			GL11.glRotated(-90, 0, 0, 1);
			GL11.glTranslatef(-x, -y, 0);
			color.bind();
			GL11.glEnable(GL11.GL_BLEND);
			font.drawString(x, y, text);
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glPopMatrix();

	}

	private void drawColor(int i, int j) {
		if (i <= 3) {
			if (j <= 3) {
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
			} else if (j <= 6) {
				GL11.glColor3f(0.4f, 0.4f, 0.4f);
			} else if (j <= 9) {
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
			}
		} else if (i <= 6) {
			if (j <= 3) {
				GL11.glColor3f(0.4f, 0.4f, 0.4f);
			} else if (j <= 6) {
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
			} else if (j <= 9) {
				GL11.glColor3f(0.4f, 0.4f, 0.4f);
			}
		} else if (i <= 9) {
			if (j <= 3) {
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
			} else if (j <= 6) {
				GL11.glColor3f(0.4f, 0.4f, 0.4f);
			} else if (j <= 9) {
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
			}
		}

	}

	protected void setSpeed(int s) {
		speed = s;
	}

	public void pollInput() {

		if (Mouse.isButtonDown(0)) {

		}

		if (Mouse.isButtonDown(1)) {
			int x = Mouse.getX();
			int y = Mouse.getY();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {

		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {

				}
				if (Keyboard.getEventKey() == Keyboard.KEY_H) {

				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {

				}

			}
		}
	}

	/** time at last frame */
	long	lastFrame;

	/** frames per second */
	int		fps;
	/** last fps time */
	long	lastFPS;

	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
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
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void update(int delta) {

		updateFPS(); // update FPS Counter
	}

	public static void init() {

	}

	public static void main(String[] argv) throws Exception {
		control_panel();

	}

	public static void control_panel() {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();
				final SodokuApp[] gui = new SodokuApp[1];
				final String[] sfile = new String[1];

				final javax.swing.JLabel jLabel1;
				final javax.swing.JLabel jLabelDataPresent;
				final javax.swing.JLabel jLabel3;
				final javax.swing.JMenu jMenu1;
				final javax.swing.JMenuBar jMenuBar1;
				final javax.swing.JMenu jMenuFile;
				final javax.swing.JMenuItem jMenuItemStepByStep;
				final javax.swing.JMenuItem jMenuItemGotoResult;
				final javax.swing.JMenuItem jMenuItemHereQuickSolve;
				final javax.swing.JMenuItem jMenuItemStepbyStepHere;

				final javax.swing.JMenuItem jMenuItemExit;
				final javax.swing.JMenuItem jMenuItemOpenDefault;
				final javax.swing.JMenuItem jMenuItemOpenFile;
				final javax.swing.JMenu jMenuOpen;

				jLabel1 = new javax.swing.JLabel();
				jLabelDataPresent = new javax.swing.JLabel();
				jLabel3 = new javax.swing.JLabel();
				jMenuBar1 = new javax.swing.JMenuBar();
				jMenuFile = new javax.swing.JMenu();
				jMenuOpen = new javax.swing.JMenu();
				jMenuItemOpenDefault = new javax.swing.JMenuItem();
				jMenuItemOpenFile = new javax.swing.JMenuItem();
				jMenuItemExit = new javax.swing.JMenuItem();
				jMenu1 = new javax.swing.JMenu();
				jMenuItemStepByStep = new javax.swing.JMenuItem();
				jMenuItemGotoResult = new javax.swing.JMenuItem();
				jMenuItemHereQuickSolve = new javax.swing.JMenuItem();
				jMenuItemStepbyStepHere = new javax.swing.JMenuItem();

				frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
				frame.setTitle("lab5.Csp.Sudoku");

				jLabel1.setText("<html><H3>data presetation</H3></html>");

				jLabelDataPresent.setText("please load data file. or CTRL+D to open default data");

				jLabel3.setText("Author: ");

				jMenuFile.setText("File");

				jMenuOpen.setText("Open..");

				jMenuItemOpenDefault.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemOpenDefault.setText("Default");
				jMenuOpen.add(jMenuItemOpenDefault);

				jMenuItemOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemOpenFile.setText("File");
				jMenuOpen.add(jMenuItemOpenFile);

				jMenuFile.add(jMenuOpen);

				jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
				jMenuItemExit.setText("Exit");
				jMenuFile.add(jMenuItemExit);

				jMenuBar1.add(jMenuFile);

				jMenu1.setText("Solve");

				jMenuItemStepByStep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemStepByStep.setText("'Step by step' Solve via OpenGL");
				jMenu1.add(jMenuItemStepByStep);

				jMenuItemGotoResult.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemGotoResult.setText("'Quick Solve' via OpenGL");
				jMenu1.add(jMenuItemGotoResult);

				jMenuItemStepbyStepHere.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemStepbyStepHere.setText("'Step by step' Solve via Swing App");
				jMenu1.add(jMenuItemStepbyStepHere);

				jMenuItemHereQuickSolve.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemHereQuickSolve.setText("'Quick Solve' via Swing App");
				jMenu1.add(jMenuItemHereQuickSolve);

				jMenuBar1.add(jMenu1);

				frame.setJMenuBar(jMenuBar1);

				javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
				frame.getContentPane().setLayout(layout);
				layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelDataPresent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel3)).addGap(0, 287, Short.MAX_VALUE))).addContainerGap()));
				layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(6, 6, 6).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabelDataPresent, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)));

				frame.pack();

				jMenuItemOpenDefault.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						sfile[0] = new File("").getAbsolutePath() + "\\res\\lab5\\sodoku\\sodoku.txt";
						System.out.println(sfile[0]);

						gui[0] = new SodokuApp(sfile[0]);
						jLabelDataPresent.setText(gui[0].var.getHtml());
					}
				});
				jMenuItemOpenFile.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						JFileChooser fc = new JFileChooser(new File("..").getAbsolutePath());
						fc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "txt file";
							}

							@Override
							public boolean accept(File f) {
								if (f.isDirectory())
									return true;

								if (f.getName().endsWith(".txt"))
									return true;
								return false;
							}
						});
						fc.showOpenDialog(null);

						File selFile = fc.getSelectedFile();
						sfile[0] = selFile.toString();

						gui[0] = new SodokuApp(sfile[0]);
						jLabelDataPresent.setText(gui[0].var.getHtml());

						System.out.println(sfile);
					}
				});

				jMenuItemOpenDefault.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						sfile[0] = new File("").getAbsolutePath() + "\\res\\lab5\\sodoku\\sodoku.txt";
						System.out.println(sfile[0]);

						gui[0] = new SodokuApp(sfile[0]);
						jLabelDataPresent.setText(gui[0].var.getHtml());
					}
				});

				jMenuItemStepByStep.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								gui[0] = new SodokuApp(sfile[0]);
								jLabelDataPresent.setText(gui[0].var.getHtml());
								gui[0].setSpeed(500);
								gui[0].start();
							}
						}).start();
					}
				});

				jMenuItemGotoResult.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								gui[0] = new SodokuApp(sfile[0]);
								jLabelDataPresent.setText(gui[0].var.getHtml());
								gui[0].setSpeed(0);
								gui[0].start();
							}
						}).start();

					}
				});

				jMenuItemHereQuickSolve.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								gui[0] = new SodokuApp(sfile[0]);

								final SodokuVariableCollection var = new SodokuVariableCollection(sfile[0]);
								final SodokuBoard csp = new SodokuBoard(var);
								final ImprovedBacktrackingStrategy solver2 = new ImprovedBacktrackingStrategy();

								solver2.setVariableSelection(Selection.MRV);
								solver2.setInference(Inference.FORWARD_CHECKING);

								System.out.println("Apply Backtracking Strategy: ImprovedBacktrackingStrategy");

								ISodokuListener listener = new ISodokuListener() {

									@Override
									public void setVariable(SodokuVariable v, int assign) {
										v.value = assign;
										jLabelDataPresent.setText(var.getHtml());
										try {
											Thread.sleep(0);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								};

								csp.setSodokuListener(listener);

								// Solve the Problem, and Get the result
								long start = System.currentTimeMillis();
								Assignment results = solver2.solve(csp);
								long finish = System.currentTimeMillis();
								long diff = finish - start;

								System.out.println("execute time " + (diff / 1000) + " second");

								// Print the output
								for (int i = 0; i < 9; i++) {
									for (int j = 0; j < 9; j++) {
										SodokuVariable v = var.get(i, j);
										v.value = (Integer) results.getAssignment(v);
									}
								}

								System.out.println("\n------->>  result  <<-----------");
								System.out.println(var.getString());

								jLabelDataPresent.setText(gui[0].var.getHtml());
								gui[0].setSpeed(0);
								gui[0].start();
							}
						}).start();

					}
				});

				jMenuItemStepbyStepHere.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								gui[0] = new SodokuApp(sfile[0]);

								final SodokuVariableCollection var = new SodokuVariableCollection(sfile[0]);
								final SodokuBoard csp = new SodokuBoard(var);
								final ImprovedBacktrackingStrategy solver2 = new ImprovedBacktrackingStrategy();

								solver2.setVariableSelection(Selection.MRV);
								solver2.setInference(Inference.FORWARD_CHECKING);

								System.out.println("Apply Backtracking Strategy: ImprovedBacktrackingStrategy");

								ISodokuListener listener = new ISodokuListener() {

									@Override
									public void setVariable(SodokuVariable v, int assign) {
										v.value = assign;
										jLabelDataPresent.setText(var.getHtml());
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								};

								csp.setSodokuListener(listener);

								// Solve the Problem, and Get the result
								long start = System.currentTimeMillis();
								Assignment results = solver2.solve(csp);
								long finish = System.currentTimeMillis();
								long diff = finish - start;

								System.out.println("execute time " + (diff / 1000) + " second");

								// Print the output
								for (int i = 0; i < 9; i++) {
									for (int j = 0; j < 9; j++) {
										SodokuVariable v = var.get(i, j);
										v.value = (Integer) results.getAssignment(v);
									}
								}

								System.out.println("\n------->>  result  <<-----------");
								System.out.println(var.getString());

								jLabelDataPresent.setText(gui[0].var.getHtml());
								gui[0].setSpeed(0);
								gui[0].start();
							}
						}).start();

					}
				});
				frame.setVisible(true);
			}
		};

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {}

		EventQueue.invokeLater(runner);
	}

}

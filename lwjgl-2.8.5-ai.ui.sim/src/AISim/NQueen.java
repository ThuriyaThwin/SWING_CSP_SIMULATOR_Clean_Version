package AISim;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import lab4.nqueen.NQueenApp;
import lab4.nqueen.NQueenResultFactory;
import lab4.nqueen.NQueensState;
import lab5.Csp.NQueen.INQueenListener;
import lab5.Csp.NQueen.NQueenCSP;
import lab5.Csp.NQueen.NQueenVariableCollection;
import lab5.Csp.NQueen.QueenVariable;
import aima.core.agent.Action;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.local.HillClimbingSearch.SearchOutcome;

public class NQueen {

	public static void main(String arg[]) {
		appController();
	}

	public static void appController() {

		Runnable runner = new Runnable() {
			@Override
			public void run() {
				final NQueensState[] initState = new NQueensState[1];

				final JFrame frame = new JFrame();
				// Variables declaration - do not modify
				final javax.swing.JLabel jLabel1;
				final javax.swing.JLabel jLabel2;
				final javax.swing.JLabel jLabel3;
				final javax.swing.JLabel jLabel4;
				final javax.swing.JLabel jLabel5;
				final javax.swing.JMenuBar jMenuBar1;
				final javax.swing.JMenu jMenuFile;
				final javax.swing.JMenuItem jMenuItemBFS;
				final javax.swing.JMenuItem jMenuItemCSP;
				final javax.swing.JMenuItem jMenuItemDFS;
				final javax.swing.JMenuItem jMenuItemExit;
				final javax.swing.JMenuItem jMenuItemHC;
				final javax.swing.JMenuItem jMenuItemSA;
				final javax.swing.JMenu jMenuRandomQueen;
				final javax.swing.JMenu jMenuSolve;
				final javax.swing.JSlider jSlider1;
				final javax.swing.JTextField jTextField1;
				// End of variables declaration

				jLabel1 = new javax.swing.JLabel();
				jLabel2 = new javax.swing.JLabel();
				jLabel3 = new javax.swing.JLabel();
				jLabel4 = new javax.swing.JLabel();
				jTextField1 = new javax.swing.JTextField();
				jSlider1 = new javax.swing.JSlider();
				jLabel5 = new javax.swing.JLabel();
				jMenuBar1 = new javax.swing.JMenuBar();
				jMenuFile = new javax.swing.JMenu();
				jMenuItemExit = new javax.swing.JMenuItem();
				jMenuRandomQueen = new javax.swing.JMenu();
				jMenuSolve = new javax.swing.JMenu();
				jMenuItemCSP = new javax.swing.JMenuItem();
				jMenuItemHC = new javax.swing.JMenuItem();
				jMenuItemSA = new javax.swing.JMenuItem();

				jMenuItemHC.setEnabled(false);
				jMenuItemSA.setEnabled(false);

				jMenuItemDFS = new javax.swing.JMenuItem();
				jMenuItemBFS = new javax.swing.JMenuItem();

				frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
				frame.setTitle("lab5.Csp.NQueen");
				frame.setAlwaysOnTop(true);
				frame.setBackground(new java.awt.Color(255, 255, 255));

				jLabel1.setText("Data presetation");

				jLabel2.setText("choose action");

				jLabel3.setText("author:");

				jLabel4.setText("Number of queen");

				jTextField1.setText("6");

				jSlider1.setToolTipText("");
				jSlider1.setValue(100);

				jLabel5.setText("Speed");

				jMenuFile.setText("File");

				jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
				jMenuItemExit.setText("Exit");
				jMenuFile.add(jMenuItemExit);

				jMenuBar1.add(jMenuFile);

				jMenuRandomQueen.setText("Random Queen");

				jMenuBar1.add(jMenuRandomQueen);

				jMenuSolve.setText("Solve");

				jMenuItemCSP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemCSP.setText("CSP");
				jMenuSolve.add(jMenuItemCSP);

				jMenuItemHC.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemHC.setText("Hill Climbing Search");
				jMenuSolve.add(jMenuItemHC);

				jMenuItemSA.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemSA.setText("Simulated Annealing Search");
				jMenuSolve.add(jMenuItemSA);

				jMenuItemDFS.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemDFS.setText("DFS");
				jMenuSolve.add(jMenuItemDFS);

				jMenuItemBFS.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
				jMenuItemBFS.setText("BFS");
				jMenuSolve.add(jMenuItemBFS);

				jMenuBar1.add(jMenuSolve);

				frame.setJMenuBar(jMenuBar1);

				javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
				frame.getContentPane().setLayout(layout);
				layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
				layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(23, 23, 23).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)).addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3)));

				frame.pack();

				jMenuRandomQueen.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						//System.out.println("xxxxxxxxxxxx");

						int nqueen = Integer.parseInt(jTextField1.getText());
						initState[0] = new NQueensState(nqueen);
						initState[0].setRandomBoard();

						jLabel2.setText(initState[0].getHtml());
						jMenuItemHC.setEnabled(true);
						jMenuItemSA.setEnabled(true);

					}
				});

				jMenuItemCSP.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {

						int nqueen = Integer.parseInt(jTextField1.getText());
						// Create a CSP Problem
						final NQueenVariableCollection var = new NQueenVariableCollection(nqueen);
						final NQueenCSP csp = new NQueenCSP(var, new INQueenListener() {
							@Override
							public void onSetAssignment(QueenVariable v) {
								//System.out.println("try set assignment: Q" + v.index + " >> " + v.location);

								try {
									int speed = (100 - jSlider1.getValue()) * (100 - jSlider1.getValue());
									Thread.sleep(speed);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								jLabel2.setText(var.getHtml());
							}
						});

						jMenuSolve.setEnabled(false);
						new Thread(new Runnable() {
							@Override
							public void run() {
								BacktrackingStrategy solver = new BacktrackingStrategy();
								Assignment results = solver.solve(csp);
								System.out.println(results.toString());
								jMenuSolve.setEnabled(true);
								jLabel2.setText(var.getHtml());
							}
						}).start();

						jLabel2.setText(var.getHtml());
						var.print();

					}
				});

				jMenuItemHC.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {

						try {

							final NQueenApp app = new NQueenApp();
							app.start = initState[0];

							jMenuSolve.setEnabled(false);
							//List<Action> a  = app.getActionsSimulatedAnnealingSearch();
							SearchOutcome outCome = null;
							List<Action> a = app.getActionsHillClimbingSearch(outCome);
							System.out.println("outCome = " + outCome);
							final Iterator<Action> currentAction = a.iterator();
							new Thread(new Runnable() {
								@Override
								public void run() {

									NQueensState nQueenState = initState[0];
									while (currentAction.hasNext()) {
										try {
											int speed = (100 - jSlider1.getValue()) * (100 - jSlider1.getValue());
											Thread.sleep(speed);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										nQueenState = (NQueensState) (new NQueenResultFactory()).result(nQueenState, currentAction.next());
										jLabel3.setText("getNumberOfAttackingPairs = " + nQueenState.getNumberOfAttackingPairs());
										jLabel2.setText(nQueenState.getHtml());
									}

									jMenuSolve.setEnabled(true);
								}
							}).start();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				jMenuItemSA.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {

						try {

							final NQueenApp app = new NQueenApp();
							app.start = initState[0];

							jMenuSolve.setEnabled(false);
							List<Action> a = app.getActionsSimulatedAnnealingSearch();
							final Iterator<Action> currentAction = a.iterator();
							new Thread(new Runnable() {
								@Override
								public void run() {

									NQueensState nQueenState = initState[0];
									while (currentAction.hasNext()) {
										try {
											int speed = (100 - jSlider1.getValue()) * (100 - jSlider1.getValue());
											Thread.sleep(speed);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										nQueenState = (NQueensState) (new NQueenResultFactory()).result(nQueenState, currentAction.next());
										jLabel3.setText("getNumberOfAttackingPairs = " + nQueenState.getNumberOfAttackingPairs());
										jLabel2.setText(nQueenState.getHtml());
									}

									jMenuSolve.setEnabled(true);
								}
							}).start();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
					@Override
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						int speed = (100 - jSlider1.getValue()) * (100 - jSlider1.getValue());
						jLabel3.setText("Thread have been slept  " + speed + " miliseconds");
						System.out.println("Thread.sleep  =" + speed);
					}
				});
				frame.setVisible(true);
			}
		};

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbusx".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {}

		EventQueue.invokeLater(runner);

	}
}

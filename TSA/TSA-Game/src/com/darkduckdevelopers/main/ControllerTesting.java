package com.darkduckdevelopers.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;

public class ControllerTesting implements ActionListener {

	// Java frame vars
	private static JFrame frame;
	private static JPanel panel;
	private static JTextField textField;
	private static JTextArea textArea;
	private static GridBagLayout gbl;
	private static GridBagConstraints gbc;
	private static String textAreaBuilder = "";

	// JInput controller vars
	private static Controller finalController;
	private static Controller[] controllers;
	private static Component[] components;

	public static void main(String[] args) {
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();

		textField = new JTextField(40);
		// This is so unclean please dont do this
		textField.addActionListener(new ControllerTesting());
		textArea = new JTextArea(25, 40);
		panel = new JPanel(gbl);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(textField, gbc);

		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		panel.add(textArea, gbc);

		frame = new JFrame();
		frame.setTitle("Controller Tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();
		int pointer = 0;
		for (Controller c : controllers) {
			if (c.getType().equals(Type.GAMEPAD)) {
				controllers[pointer] = c;
				textAreaBuilder = textAreaBuilder + pointer + ": "
						+ c.getName() + "\n";
				pointer++;
			}
		}
		if (pointer == 0) {
			textAreaBuilder = "No controllers detected.";
		} else {
			textAreaBuilder = textAreaBuilder + "\nEnter the ID of the controller to test.";
		}
		textArea.setText(textAreaBuilder);

		while (true) {
			if (finalController != null) {
				textAreaBuilder = "";
				finalController.poll();
				for (Component c : components) {
					float pollData = finalController.getComponent(c.getIdentifier()).getPollData();
					String inputLine = c.getName() + ": " + pollData;
					textAreaBuilder = textAreaBuilder + inputLine + "\n";
				}
				textArea.setText(textAreaBuilder);
			} else {
				System.out.println("compiler tricks");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String input = textField.getText();
		try {
			int controllerPicked = Integer.parseInt(input);
			finalController = controllers[controllerPicked];
			components = finalController.getComponents();
			textField.setText("You can close the application at any time with the close button.");
		} catch (Exception exc) {
			textField.setText("Not a valid integer!");
			textField.selectAll();
		}
	}
}

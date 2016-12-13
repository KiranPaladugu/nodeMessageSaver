/* ********************************************************************************
 * All rights reserved.
 ******************************************************************************* */
package com.tools.node.message.saver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import com.tcs.application.*;
import com.tcs.application.pluign.PluginManager;

public class NodeMessageSaverPlugin extends AbstractPlugin implements Subscriber, ActionListener {

	public void startPlugin() {
		Application.getSubscriptionManager().subscribe(this, this.getClass().getName() + ".OK",
				this.getClass().getName() + ".RESPONSE");
		JMenuItem item = new JMenuItem("SaveToFile");
		item.addActionListener(this);
		Application.getSubscriptionManager().notifySubscriber("messageDisplayPopupAddMenu", this, item);
		Application.getSubscriptionManager().notifySubscriber(PluginManager.PLUGIN_STARTED, this);
	}

	public void stopPlugin() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Application.getSubscriptionManager().notifySubscriber(this.getClass().getName() + ".REQUEST", this, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tcs.application.Subscriber#onSubscriptionEvent(com.tcs.application.
	 * SubscriptionEvent)
	 */
	@Override
	public void onSubscriptionEvent(SubscriptionEvent arg0) throws Exception {
		String event = arg0.getEvent();
		Object data = arg0.getData();
		if (event.equals(this.getClass().getName() + ".OK")) {
			System.out.println("Request SUCESS...");
		} else if (event.equals(this.getClass().getName() + ".RESPONSE")) {
			System.out.println("Got the response..");
			saveToFileDialog((String) data);
		}
	}

	/**
	 * @param data
	 */
	private void saveToFileDialog(String data) {
		JFileChooser chooser = new JFileChooser();
		int selection = chooser.showSaveDialog(null);
		File file = null;
		String cause = "";
		boolean saved = false;
		if (selection == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(data);
				writer.flush();
				writer.close();
				saved = true;
				cause = "success";
			} catch (IOException e) {
				e.printStackTrace();
				cause = e.getMessage();
			}
		}
		if (saved) {
			JOptionPane.showMessageDialog(chooser, "File saved@" + file.getAbsolutePath(), "Save Success",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(chooser, "Save Failed for " + file.getAbsolutePath() + "\n Cause:" + cause,
					"Save Failed!!", JOptionPane.ERROR_MESSAGE);
		}
	}
}

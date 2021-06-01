/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author eccentric_nz
 */
public class UserInterface extends javax.swing.JFrame {

	@Serial
	private static final long serialVersionUID = 3259909191489626727L;
	private final PrintWriter consoleStream;
	private File lastDir = new File(".");
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton convertButton;
	private javax.swing.JButton inputButton;
	private javax.swing.JTextField inputFile;
	private javax.swing.JLabel inputLabel;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton outputButton;
	private javax.swing.JTextArea outputConsole;
	private javax.swing.JTextField outputFile;
	private javax.swing.JLabel outputLabel;
	private javax.swing.JTextField prefix;
	private javax.swing.JLabel titleLabel;

	/**
	 * Creates new form UserInterface
	 */
	public UserInterface() {
		consoleStream = new PrintWriter(new StringWriter() {

			@Override
			public void flush() {
				outputConsole.append(getBuffer().toString());
				getBuffer().setLength(0);
			}
		}, true);
		initComponents();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		/*
		 * Set the Nimbus look and feel
		 */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>

		/*
		 * Create and display the form
		 */
		java.awt.EventQueue.invokeLater(() -> new UserInterface().setVisible(true));
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		inputButton = new javax.swing.JButton();
		outputButton = new javax.swing.JButton();
		convertButton = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		outputConsole = new javax.swing.JTextArea();
		outputLabel = new javax.swing.JLabel();
		inputLabel = new javax.swing.JLabel();
		titleLabel = new javax.swing.JLabel();
		inputFile = new javax.swing.JTextField();
		outputFile = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		prefix = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		inputButton.setText("Browse");
		inputButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				inputButtonMouseReleased(evt);
			}
		});

		outputButton.setText("Browse");
		outputButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				outputButtonMouseReleased(evt);
			}
		});

		convertButton.setText("Convert");
		convertButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				convertButtonMouseReleased(evt);
			}
		});

		outputConsole.setColumns(20);
		outputConsole.setRows(5);
		jScrollPane1.setViewportView(outputConsole);

		outputLabel.setText("Output file");

		inputLabel.setText("TVM.db");

		titleLabel.setText("TARDISVortexManipulator SQLite -> MySQL Database Converter");

		inputFile.addActionListener(this::inputFileActionPerformed);

		jLabel1.setText("Prefix");

		prefix.addActionListener(this::prefixActionPerformed);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(titleLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE).addComponent(convertButton)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(outputLabel).addComponent(inputLabel).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(outputFile).addComponent(inputFile).addComponent(prefix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(outputButton, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(inputButton, javax.swing.GroupLayout.Alignment.TRAILING)))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(inputButton).addComponent(inputLabel).addComponent(inputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(outputButton).addComponent(outputLabel).addComponent(outputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(prefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(convertButton).addComponent(titleLabel)).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void inputFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputFileActionPerformed
		// add your handling code here:
	}//GEN-LAST:event_inputFileActionPerformed

	private void inputButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputButtonMouseReleased
		choose(inputFile, "SQLite Database", "db");
	}//GEN-LAST:event_inputButtonMouseReleased

	private void outputButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputButtonMouseReleased
		choose(outputFile, "SQL Text File", "sql");
	}//GEN-LAST:event_outputButtonMouseReleased

	private void convertButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_convertButtonMouseReleased
		try {
			convert();
		} catch (IOException ex) {
			Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
		}
	}//GEN-LAST:event_convertButtonMouseReleased

	private void prefixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefixActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_prefixActionPerformed

	/**
	 * Opens a file chooser.
	 *
	 * @param box         the text field to target
	 * @param description a String describing the file type
	 * @param extension   the file extension
	 */
	private void choose(JTextField box, String description, String extension) {
		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.setFileFilter(new FileNameExtensionFilter(description, extension));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showOpenDialog(this);

		if (chooser.getSelectedFile() != null) {
			box.setText(chooser.getSelectedFile().getPath());
			lastDir = chooser.getCurrentDirectory();
		}
	}

	/**
	 *
	 */
	private void convert() throws IOException {
		if (inputFile.getText().isEmpty() || outputFile.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please browse for TARDIS.db and TARDIS.sql files.", "Please select required files.", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String pre = prefix.getText();
		Main.process(consoleStream, new File(inputFile.getText()), new File(outputFile.getText()), pre);
	}
	// End of variables declaration//GEN-END:variables
}

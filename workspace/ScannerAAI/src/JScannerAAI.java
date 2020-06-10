/*
 ========================================================================================================================================================
 Name        : ScannerAII
 Author      : Eduardo Augusto Lima Pereira
 Version     : 1.0
 Copyright   : Your copyright notice
 Description : Trabalho Autoinstrucional para a disciplina de Compiladores do Curso de Ciência da Computação (Noturno) da Universidade FUMEC
 ========================================================================================================================================================
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class JScannerAAI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lblTitulo;
	private JLabel lblArquivoEntrada;
	private JLabel lblArquivoSaida;
	private JScrollPane scrollPaneArquivoEntrada;
	private JTextPane textPaneArquivoEntrada;
	private JButton btnArquivoEntrada;
	private JScrollPane scrollPaneArquivoSaida;
	private JTextPane textPaneArquivoSaida;
	private JButton btnArquivoSaida;

	private String stringInputFile = "";
	private String stringExitFile = "";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

					JScannerAAI jAutoinstrucionalScanner = new JScannerAAI();
					jAutoinstrucionalScanner.setVisible(true);
					jAutoinstrucionalScanner.setLocationRelativeTo(null);
				} catch (ClassNotFoundException classNotFoundException) {
					JOptionPane.showMessageDialog(null, "Erro ao localizar arquivos do sistema.\nErro: " + classNotFoundException.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (InstantiationException instantiationException) {
					JOptionPane.showMessageDialog(null, "Erro ao inicializar o sistema.\nErro: " + instantiationException.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (IllegalAccessException illegalAccessException) {
					JOptionPane.showMessageDialog(null, "Erro ao acessar dependências do sistema.\nErro: " + illegalAccessException.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar o layout do sistema.\nErro: " + unsupportedLookAndFeelException.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, "Erro ao inicializar o sistema.\nErro: " + exception.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	public JScannerAAI() {
		setTitle("Autoinstrucional Compiladores - Analisador L\u00E9xico");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitulo = new JLabel("Analisador L\u00E9xico para Express\u00F5es Aritm\u00E9ticas");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
		lblTitulo.setBounds(143, 32, 700, 40);
		contentPane.add(lblTitulo);

		lblArquivoEntrada = new JLabel("Arquivo de Entrada:");
		lblArquivoEntrada.setFont(new Font("Arial", Font.PLAIN, 20));
		lblArquivoEntrada.setBounds(400, 104, 180, 30);
		contentPane.add(lblArquivoEntrada);

		lblArquivoSaida = new JLabel("Arquivo de Sa\u00EDda:");
		lblArquivoSaida.setFont(new Font("Arial", Font.PLAIN, 20));
		lblArquivoSaida.setBounds(400, 444, 180, 30);
		contentPane.add(lblArquivoSaida);

		scrollPaneArquivoEntrada = new JScrollPane();
		scrollPaneArquivoEntrada.setBounds(10, 145, 974, 229);
		contentPane.add(scrollPaneArquivoEntrada);

		textPaneArquivoEntrada = new JTextPane();
		scrollPaneArquivoEntrada.setViewportView(textPaneArquivoEntrada);
		textPaneArquivoEntrada.setEditable(false);
		textPaneArquivoEntrada.setFont(new Font("Arial", Font.PLAIN, 16));

		btnArquivoEntrada = new JButton("Upload do Arquivo de Entrada");
		btnArquivoEntrada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stringInputFile = readFile();
				textPaneArquivoEntrada.setText(stringInputFile);

				stringExitFile = generateExitFile(stringInputFile, "\n");
				textPaneArquivoSaida.setText(stringExitFile);
			}
		});
		btnArquivoEntrada.setFont(new Font("Arial", Font.PLAIN, 16));
		btnArquivoEntrada.setBounds(275, 385, 400, 35);
		contentPane.add(btnArquivoEntrada);

		scrollPaneArquivoSaida = new JScrollPane();
		scrollPaneArquivoSaida.setBounds(10, 485, 974, 229);
		contentPane.add(scrollPaneArquivoSaida);

		textPaneArquivoSaida = new JTextPane();
		scrollPaneArquivoSaida.setViewportView(textPaneArquivoSaida);
		textPaneArquivoSaida.setEditable(false);
		textPaneArquivoSaida.setFont(new Font("Arial", Font.PLAIN, 16));

		btnArquivoSaida = new JButton("Download do Arquivo de Sa\u00EDda");
		btnArquivoSaida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!stringExitFile.equals("")) {
					String tempStringExitFile = stringExitFile.replaceAll("\n", System.lineSeparator());
					saveFile(tempStringExitFile);
				} else {
					JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo.\nAinda não há nenhum arquivo carregado.", "ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnArquivoSaida.setFont(new Font("Arial", Font.PLAIN, 16));
		btnArquivoSaida.setBounds(275, 725, 400, 35);
		contentPane.add(btnArquivoSaida);
	}

	private static String readFile() {
		String fileContent = "";
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedFile = jfc.getSelectedFile();
				fileContent = new String(Files.readAllBytes(Paths.get(selectedFile.getPath())));
				//System.out.println(fileContent);
			} catch (IOException ioException) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar o arquivo.\nErro: " + ioException.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			}
		}

		return fileContent;
	}

	private static void saveFile(String fileContent) {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedFile = jfc.getSelectedFile();
				Files.write(Paths.get(selectedFile.getPath() + ".txt"), fileContent.getBytes());
			} catch (IOException ioException) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo.\nErro: " + ioException.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private static String generateExitFile(String string, String separator){
		String stringExitFile = "";
		String arrayString[] = string.split("\\s*" + separator + "\\s*");
		List<String> arrayListString = new ArrayList<String>();
		
		arrayListString = Arrays.asList(arrayString);
		for (String s: arrayListString)
			stringExitFile += s + ScannerAAI.checkWord(s) + "\n\n";

		return stringExitFile;
	}
}
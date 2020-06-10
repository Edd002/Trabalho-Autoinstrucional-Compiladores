/*
 ========================================================================================================================================================
 Name        : ScannerAII
 Author      : Eduardo Augusto Lima Pereira
 Version     : 1.0
 Copyright   : Your copyright notice
 Description : Trabalho Autoinstrucional para a disciplina de Compiladores do Curso de Ciência da Computação (Noturno) da Universidade FUMEC
 ========================================================================================================================================================
 */

public class ScannerAAI {

	private static final String OPERADORES = "+/-*><";
	private static final String NUMEROS = "0123456789";
	private static final String LETRAS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String[] PALAVRAS_RESERVADAS = {"boolean", "char", "byte", "short", "int", "long", "float", "double", "String", "for", "while", "if", "else", "continue", "break", "switch"};
	private static final String[] TIPOS = {"byte", "short", "int", "long"};
	private static final int ESTADO_FINAL = 35;

	public static String checkWord(String word) {
		char[] wordChar;
		int i = 0, state = 0;
		String tempTipo = "";
		String tempVariavel = "";

		word += "\0"; // Adicionar o símbolo terminal no final da palavra
		wordChar = word.toCharArray();

		final int LIM_PALAVRA = wordChar.length - 1;
		while (state != ESTADO_FINAL) {
			//System.out.println("Estado atual: " + state + " - Símbolo a consumir: " + wordChar[i]);

			switch (state) {
			case 0:
				if (!searchString(tempVariavel, PALAVRAS_RESERVADAS)) {
					tempVariavel = "";
					while (wordChar[i] == ' ') {
						i++;
					}
					if (wordChar[i] == '\0') {
						state = ESTADO_FINAL;
					} else if (wordChar[i] == ';') {
						i++;
						state = 0;
					} else if (wordChar[i] == '/') {
						i++;
						state = 1;
					} else if (searchChar(wordChar[i], LETRAS)) {
						tempTipo += wordChar[i]; // Montar a word do tipo
						i++;
						state = 4;
					} else {
						return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
					}
				} else {
					return "\nPalavra inválida. A variável " + tempVariavel + " é uma palavra reservada.\nÚltimo estado visitado: " + state;
				}
				break;

			case 1:
				if (wordChar[i] == '/') {
					i++;
					state = 30;
				} else if (wordChar[i] == '*') {
					i++;
					state = 2;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 2:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if (wordChar[i] == '*') {
					i++;
					state = 3;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 3:
				while (wordChar[i] == '*') {
					i++;
				}
				if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 2;
				} else if (wordChar[i] == '/') {
					i++;
					state = 0;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 4:
				while (searchChar(wordChar[i], LETRAS)) {
					tempTipo += wordChar[i]; // Montar a word do tipo
					i++;
				}
				while (searchChar(wordChar[i], NUMEROS)) {
					tempTipo += wordChar[i]; // Montar a word do tipo
					i++;
				}
				if (wordChar[i] == ' ') {
					i++;
					state = 31;
				} else if (wordChar[i] == '/') {
					i++;
					state = 32;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 31:
				while (wordChar[i] == ' ') {
					i++;
				}
				if (wordChar[i] == '/') {
					i++;
					state = 32;
				} else if (searchChar(wordChar[i], LETRAS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
					state = 5;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 32:
				if (wordChar[i] == '*') {
					i++;
					state = 33;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 33:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if (wordChar[i] == '*') {
					i++;
					state = 34;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 34:
				while (wordChar[i] == '*') {
					i++;
				}
				if (wordChar[i] == '/') {
					i++;
					state = 31;
				} else if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 33;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 5:
				// Neste estado o TIPO já esta definido e pronto para verificação
				if (searchString(tempTipo, TIPOS)) {
					tempTipo = "";
					while (searchChar(wordChar[i], LETRAS) || searchChar(wordChar[i], NUMEROS)) {
						tempVariavel += wordChar[i]; // Montar a word da variável
						i++;
					}
					if (wordChar[i] == ' ') {
						i++;
						state = 6;
					} else if (wordChar[i] == '/') {
						i++;
						state = 7;
					} else if (wordChar[i] == '=') {
						i++;
						state = 10;
					} else {
						return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
					}
				} else {
					return "\nPalavra inválida. O tipo " + tempTipo + " não é válido.\nÚltimo estado visitado: " + state;
				}
				break;

			case 6:
				// Neste já há uma variável definida e pronta para a verificação
				if (!searchString(tempVariavel, PALAVRAS_RESERVADAS)) {
					tempVariavel = "";
					while (wordChar[i] == ' ') {
						i++;
					}
					if (wordChar[i] == '/') {
						i++;
						state = 7;
					} else if (wordChar[i] == '=') {
						i++;
						state = 10;
					} else {
						return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
					}
				} else {
					return "\nPalavra inválida. A variável " + tempVariavel + " é uma palavra reservada.\nÚltimo estado visitado: " + state;
				}
				break;

			case 7:
				if (wordChar[i] == '*') {
					i++;
					state = 8;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 8:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if (wordChar[i] == '*') {
					i++;
					state = 9;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 9:
				while (wordChar[i] == '*') {
					i++;
				}
				if (wordChar[i] == '/') {
					i++;
					state = 6;
				} else if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 8;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 10:
				while (wordChar[i] == ' ') {
					i++;
				}
				if (searchChar(wordChar[i], LETRAS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
					state = 14;
				} else if (searchChar(wordChar[i], NUMEROS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
					state = 15;
				} else if (wordChar[i] == '/') {
					i++;
					state = 11;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 11:
				if (wordChar[i] == '*') {
					i++;
					state = 12;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 12:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if (wordChar[i] == '*') {
					i++;
					state = 13;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 13:
				while (wordChar[i] == '*') {
					i++;
				}
				if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 12;
				} else if (wordChar[i] == '/') {
					i++;
					state = 10;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 14:
				while (searchChar(wordChar[i], LETRAS) || searchChar(wordChar[i], NUMEROS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
				}
				if (wordChar[i] == ';') {
					i++;
					state = 0;
				} else if (searchChar(wordChar[i], OPERADORES)) {
					i++;
					state = 20;
				} else if (wordChar[i] == ' ') {
					i++;
					state = 16;
				} else if (wordChar[i] == '/') {
					i++;
					state = 17;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 15:
				while (searchChar(wordChar[i], NUMEROS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
				}
				if (wordChar[i] == ';') {
					i++;
					state = 0;
				} else if (wordChar[i] == ' ') {
					i++;
					state = 16;
				} else if (wordChar[i] == '/') {
					i++;
					state = 17;
				} else if (searchChar(wordChar[i], OPERADORES)) {
					i++;
					state = 20;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 16:
				while (wordChar[i] == ' ') {
					i++;
				}
				if (wordChar[i] == ';') {
					i++;
					state = 0;
				} else if (wordChar[i] == '/') {
					i++;
					state = 17;
				} else if (searchChar(wordChar[i], OPERADORES)) {
					i++;
					state = 20;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 17:
				if (wordChar[i] == '*') {
					i++;
					state = 18;
				} else if (wordChar[i] == '/') {
					i++;
					state = 21;
				} else if (wordChar[i] == ' ') {
					i++;
					state = 20;
				} else if (searchChar(wordChar[i], LETRAS)) {
					i++;
					state = 24;
				} else if (searchChar(wordChar[i], NUMEROS)) {
					i++;
					state = 25;
				} else  {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 18:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if ((wordChar[i] == '*')) {
					i++;
					state = 19;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 19:
				while (wordChar[i] == '*') {
					i++;
				}
				if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 18;
				} else if (wordChar[i] == '/') {
					i++;
					state = 16;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 20:
				if (!searchString(tempVariavel, PALAVRAS_RESERVADAS)) {
					tempVariavel = "";
					while (wordChar[i] == ' ') {
						i++;
					}
					if (searchChar(wordChar[i], LETRAS)) {
						tempVariavel += wordChar[i]; // Montar a word da variável
						i++;
						state = 24;
					} else if (searchChar(wordChar[i], NUMEROS)) {
						tempVariavel += wordChar[i]; // Montar a word da variável
						i++;
						state = 25;
					} else if (wordChar[i] == '/') {
						i++;
						state = 21;
					} else {
						return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
					}
				} else {
					return "\nPalavra inválida. A variável " + tempVariavel + " é uma palavra reservada.\nÚltimo estado visitado: " + state;
				}
				break;

			case 21:
				if (wordChar[i] == '*') {
					i++;
					state = 22;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 22:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if (wordChar[i] == '*') {
					i++;
					state = 23;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 23:
				while (wordChar[i] == '*') {
					i++;
				}
				if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 22;
				} else if (wordChar[i] == '/') {
					i++;
					state = 20;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 24:
				while (searchChar(wordChar[i], LETRAS) || searchChar(wordChar[i], NUMEROS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
				}
				if (wordChar[i] == ';') {
					i++;
					state = 0;
				} else if (searchChar(wordChar[i], OPERADORES)) {
					i++;
					state = 20;
				} else if (wordChar[i] == ' ') {
					i++;
					state = 26;
				} else if (wordChar[i] == '/') {
					i++;
					state = 27;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 25:
				while (searchChar(wordChar[i], NUMEROS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
				}
				if (wordChar[i] == ';') {
					i++;
					state = 0;
				} else if (wordChar[i] == ' ') {
					i++;
					state = 26;
				} else if (wordChar[i] == '/') {
					i++;
					state = 27;
				} else if (searchChar(wordChar[i], OPERADORES)) {
					i++;
					state = 20;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 26:
				while (wordChar[i] == ' ') {
					i++;
				}
				if (wordChar[i] == ';') {
					i++;
					state = 0;
				} else if (wordChar[i] == '/') {
					i++;
					state = 27;
				} else if (searchChar(wordChar[i], OPERADORES)) {
					i++;
					state = 20;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 27:
				if (wordChar[i] == '*') {
					i++;
					state = 28;
				} else if (wordChar[i] == ' ') {
					i++;
					state = 20;
				} else if (wordChar[i] == '/') {
					i++;
					state = 21;
				} else if (searchChar(wordChar[i], LETRAS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
					state = 24;
				} else if (searchChar(wordChar[i], NUMEROS)) {
					tempVariavel += wordChar[i]; // Montar a word da variável
					i++;
					state = 25;
				} else  {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 28:
				while (wordChar[i] != '*' && (i < LIM_PALAVRA)) {
					i++;
				}
				if (wordChar[i] == '*') {
					i++;
					state = 29;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 29:
				while (wordChar[i] == '*') {
					i++;
				}
				if ((wordChar[i] != '*') && (wordChar[i] != '/') && (i < LIM_PALAVRA)) {
					i++;
					state = 28;
				} else if (wordChar[i] == '/') {
					i++;
					state = 26;
				} else {
					return "\nPalavra inválida.\nÚltimo estado visitado: " + state;
				}
				break;

			case 30:
				while (i < LIM_PALAVRA) {
					i++;
				}
				if (wordChar[i] == '\0') {
					state = ESTADO_FINAL;
				}
				break;
			}
		}

		return "\nPalavra válida.";
	}

	private static boolean searchChar(char searchedCharacter, String string) {
		return string.indexOf(searchedCharacter) != -1;
	}

	private static boolean searchString(String searchedString, String[] Strings) {
		for (String string : Strings)
			if (searchedString.equals(string))
				return true;

		return false;
	}
}
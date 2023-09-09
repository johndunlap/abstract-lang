package abs.compiler.archive.parser;

import abs.compiler.lexer.TokenStream;

/**
 * It is intended that this one class parse all supported paradigms. This is partly because the paradigm of the source
 * code being parsed isn't known until after parsing has begun and also because it encourages code reuse between
 * paradigms.
 */
public class Parser {
	private TokenStream tokenStream;
	private ErrorHandler errorHandler;

	public Parser(TokenStream tokenStream, ErrorHandler errorHandler) {
		this.tokenStream = tokenStream;
		this.errorHandler = errorHandler;
	}

	public TokenStream getTokenStream() {
		return tokenStream;
	}

	public void setTokenStream(TokenStream tokenStream) {
		this.tokenStream = tokenStream;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
/*
	public FileNode parse() throws ParserException {
		Paradigm paradigm = parseParadigm();

		if (errorHandler.errorCount() == 0) {
			if (paradigm.equals(Paradigm.OOP)) {
				return parseOop();
			} else if (paradigm.equals(Paradigm.PROCEDURAL)) {
				throw new RuntimeException("NOT IMPLEMENTED"); // TODO
			} else if (paradigm.equals(Paradigm.FUNCTIONAL)) {
				throw new RuntimeException("NOT IMPLEMENTED"); // TODO
			} else {
				// An exception here *should* be fine because this condition should already result in an error being
				// added to the error handler in the parseParadigm method which *should* preclude ever getting to this
				// point - JDD
				throw new RuntimeException("Unsupported paradigm: " + paradigm.name());
			}
		}

		return null;
	}

	public Namespace parseNamespace() throws ParserException {
		Namespace currentNamespace = null;

		Token token = peek();

		if (token.isType(Type.IDENTIFIER)) {
			currentNamespace = new Namespace(token.getValue());

			// Start looking at the next token
			token = eatAndPeek();

			boolean lastWasIdentifier = true;

			while (token.isType(Type.IDENTIFIER) || token.isType(Type.PERIOD)) {
				if (lastWasIdentifier) {
					if (token.isType(Type.PERIOD)) {
						lastWasIdentifier = false;
					} else {
						errorHandler.addExpectationError(".", token);
					}
				} else {
					if (token.isType(Type.IDENTIFIER)) {
						Namespace child = new Namespace(token.getValue());
						currentNamespace.addChild(child);
						currentNamespace = child;
						lastWasIdentifier = true;
					} else {
						errorHandler.addMissingIdentifierError(token);
					}
				}

				// Start looking at the next token
				token = eatAndPeek();
			}
		} else {
			errorHandler.addMissingIdentifierError(token);
		}

		return currentNamespace;
	}

	public PackageNode parsePackageStatement() throws ParserException {
		PackageNode packageNode = new PackageNode();

		Token token = peek();

		if (token.isType(Type.IDENTIFIER)) {
			eat();

			// Create an error if the name of the token is not what we expect
			if (!token.getValue().equals("package")) {
				errorHandler.addExpectationError("package", token);
			}

			// Attempt to parse the namespace
			Namespace namespace = parseNamespace();
			packageNode.setNamespace(namespace);

			// Update our peek token now that the namespace has been parsed
			token = peek();

			if (token.isType(Type.SEMICOLON)) {
				eat();
			} else {
				errorHandler.addMissingSemicolonError(tokenStream.getPosition());
			}
		} else if (token.isType(Type.EOF)) {
			errorHandler.addEofError(tokenStream.getPosition());
		} else {
			errorHandler.addMissingIdentifierError(token);
		}

		return packageNode;
	}

	public OopFileNode parseOop() throws ParserException {
		OopFileNode oopFileNode = new OopFileNode();

		// Parse package declaration
		PackageNode packageNode = parsePackageStatement();

		if (errorHandler.fatalErrorCount() > 0) {
			return oopFileNode;
		} else {
			oopFileNode.setPackageNode(packageNode);
		}

		// Parse imports
		List<ImportNode> importNodes = parseImports();
		oopFileNode.setImportNodes(importNodes);

		// TODO: Parse class declaration
		// TODO: Parse class body(both variable and function declarations)

		return oopFileNode;
	}

	public List<ImportNode> parseImports() throws ParserException {
		List<ImportNode> importNodes = new ArrayList<>();

		Token token = peek();

		if (token.isType(Type.IDENTIFIER)) {
			eat();

			// Create an error if the identifier is not what we expect
			if (!token.getValue().equals("import")) {
				errorHandler.addExpectationError("import", token);
			}

			Namespace namespace = parseNamespace();
			ImportNode importNode = new ImportNode(namespace);
			importNodes.add(importNode);

			// Update our peek token now that the namespace has been parsed
			token = peek();
		} else if (token.isType(Type.EOF)) {
			errorHandler.addEofError(tokenStream.getPosition());
		} else {
			errorHandler.addMissingIdentifierError(token);
		}

		if (token.isType(Type.SEMICOLON)) {
			eat();
		} else {
			errorHandler.addMissingSemicolonError(tokenStream.getPosition());
		}

		// Don't return a broken object
		if (errorHandler.errorCount() > 0) {
			return null;
		}

		return importNodes;
	}

	public Paradigm parseParadigm() throws ParserException {
		Paradigm paradigm = null;

		List<Token> tokens = peekList(3);

		Token first = tokens.get(0);
		Token second = tokens.get(1);
		Token third = tokens.get(2);

		if (first.isType(Type.IDENTIFIER) && first.hasValue("paradigm")) {
			if (second.isType(Type.IDENTIFIER)) {
				String v = second.getValue();

				if (Paradigm.isSupported(v)) {

				} else {
					errorHandler.addFatalError("Unsupported paradigm: " + second.getValue() + ". Supported paradigms are: " + Paradigm.getLegalParadigmsAsString(), second.getRange());
				}
			}
		} else if (first.isType(Type.EOF)) {
			errorHandler.addEofError(tokenStream.getPosition());
		} else {
			errorHandler.addFatalExpectationError("paradigm", first);
		}





























		if (!first.isType(Type.EOF)) {
			if (first.isType(Type.IDENTIFIER)) {
				eat();

				if (!first.hasValue("paradigm")) {
					errorHandler.addFatalExpectationError("paradigm", first);
				}

				second = tokens.get(1);

				System.err.println("SECOND: " + second);

				if (second.isType(Type.IDENTIFIER)) {
					if (second.hasValue("oop")) {
						paradigm = Paradigm.OOP;
					} else if (second.hasValue("procedural")) {
						paradigm = Paradigm.PROCEDURAL;
					} else if (second.hasValue("functional")) {
						paradigm = Paradigm.FUNCTIONAL;
					} else {
						errorHandler.addFatalError("Unsupported paradigm: " + second.getValue() + ". Supported paradigms are: " + Paradigm.getLegalParadigmsAsString(), second.getRange());
					}

					if (paradigm != null) {
						eat();
						third = tokens.get(2);

						if (third.isType(Type.EOF)) {
							errorHandler.addEofError(tokenStream.getPosition());
						} else if (third.isType(Type.SEMICOLON)) {
							eat();
						} else {
							errorHandler.addMissingSemicolonError(tokenStream.getPosition());
						}
					} else if (second.isType(Type.EOF)) {
						errorHandler.addEofError(tokenStream.getPosition());
					} else if (second.isType(Type.SEMICOLON)) {
						errorHandler.addFatalError("Unspecified paradigm", tokenStream.getPosition());
					}
				} else {
					errorHandler.addFatalMissingIdentifierError(second);
				}
			} else {
				errorHandler.addFatalMissingIdentifierError(first);
			}
		} else {
			errorHandler.addEofError(tokenStream.getPosition());
		}

		return paradigm;
	}

	private boolean peekExpected(Type expectedType) throws ParserException {
		return peekExpected(expectedType, null);
	}

	private boolean peekExpected(Type expectedType, String extraMessage) throws ParserException {
		Token t = peek();

		if (t.getType().equals(expectedType)) {
			return true;
		} else if (t.getType().equals(Type.EOF)) {
			String m = "Unexpected end of file";

			if (extraMessage != null) {
				m += ": " + extraMessage;
			}

			errorHandler.addError(m, tokenStream.getPosition());
			return false;
		}

		String expected = (String) coalesce(expectedType.getRepresentation(), expectedType.toString());
		String actual = (String) coalesce(t.getValue(), t.getType().getRepresentation(), t.getType().toString());

		String m = "Expected \"" + expected + "\" but found \"" + actual + "\" instead";

		if (extraMessage != null) {
			m = extraMessage + ": " + m;
		}

		errorHandler.addError(m, tokenStream.getPosition());

		return false;
	}

	public List<Token> peekList(int count) throws ParserException {
		try {
			List<Token> tokens = new ArrayList<>();
			Token token;

			int i = 0;
			while ((token = tokenStream.peek(i)).getType() != Type.EOF && tokens.size() < count) {
				i++;

				if (token.getType().isParserIgnore()) {
					continue;
				}

				tokens.add(token);
			}

			if (tokens.size() < count && token.getType().equals(Type.EOF)) {
				tokens.add(token);
			}

			return tokens;
		} catch (LexerException e) {
			throw new ParserException(e, tokenStream.getPosition());
		}
	}

*
	 * This method is used for fast pattern matches so that parsing is, hopefully, faster when there are no errors.


	public List<Type> peekTypeList(int count) throws ParserException {
		try {
			List<Type> tokens = new ArrayList<>();
			Token token;

			int i = 0;
			while ((token = tokenStream.peek(i)).getType() != Type.EOF && tokens.size() < count) {
				i++;

				if (token.getType().isParserIgnore()) {
					continue;
				}

				tokens.add(token.getType());
			}

			if (tokens.size() < count && token.getType().equals(Type.EOF)) {
				tokens.add(token.getType());
			}

			return tokens;
		} catch (LexerException e) {
			throw new ParserException(e, tokenStream.getPosition());
		}
	}

	public Token peek() throws ParserException {
		try {
			Token token;

			int i = 0;
			while ((token = tokenStream.peek(i)).getType() != Type.EOF) {
				// This avoids clutter throughout the parser by avoiding the need for explicitly handling tokens which
				// have no grammatical significance(like comments and whitespace)
				if (!token.getType().isParserIgnore()) {
					break;
				}
				i++;
			}

			if (token == null) {
				throw new ParserException("BUG: Tokens returned by the token stream should never be null", tokenStream.getPosition());
			}

			return token;
		} catch (LexerException e) {
			throw new ParserException(e, tokenStream.getPosition());
		}
	}

	public boolean nextTypeIs(Type type) throws ParserException {
		return peek().getType().equals(type);
	}

	public Token next() throws ParserException {
		try {
			Token token;

			while ((token = tokenStream.next()).getType() != Type.EOF) {
				// This avoids clutter throughout the parser by avoiding the need for explicitly handling tokens which have
				// no grammatical significance(like comments)
				if (!token.getType().isParserIgnore()) {
					break;
				}
			}

			if (token == null) {
				throw new ParserException("BUG: Tokens returned by the token stream should never be null", tokenStream.getPosition());
			}

			return token;
		} catch (LexerException e) {
			throw new ParserException(e, tokenStream.getPosition());
		}
	}

	public static void main(String[] args) throws IOException, ParserException, InterruptedException {
		String filename = "src/test/resources/abs/samples/lexer/car.abs";
		File file = new File(filename);

		if (args.length > 0) {
			filename = args[0];
			file = new File(filename);
		}

		if (!file.exists()) {
			System.err.println("File does not exist: " + filename);
			System.exit(2);
		}

		String outputMethod;

		if (args.length > 1) {
			if (args[1].equals("JSON") || args[1].equals("DOT") || args[1].equals("IMAGE")) {
				outputMethod = args[1];
			} else {
				outputMethod = null;
				System.err.println("Unsupported output method: " + args[1]);
				System.exit(3);
			}
		} else {
			outputMethod = "IMAGE";
		}

		Options o = new Options();;

		CharacterStream characterStream = new CharacterStream(new FileInputStream(file), o);
		TokenStream tokenStream = new TokenStream(characterStream, o);

		ErrorHandler errorHandler = new ErrorHandler();
		Parser parser = new Parser(tokenStream, errorHandler);

		// TODO: Fix this when parsing produces model objects instead of nodes
		Node rootNode = parse();

		// Report any errors
		if (errorHandler.count() > 0) {
			System.err.println("Errors encountered:");
			for (ParseError error : errorHandler.getErrors()) {
				System.err.println(error);
			}
			System.exit(4);
		}

		if (outputMethod.equals("IMAGE")) {
			File tmpDotFile = new File("tmp.dot");
			File tmpSvgFile = new File("tmp.svg");

			FileWriter fileWriter = new FileWriter(tmpDotFile);
			fileWriter.write(rootNode.toDot());
			fileWriter.flush();
			fileWriter.close();

			String command1 = "dot -Tsvg " + tmpDotFile.getName() + " -o " + tmpSvgFile.getName();
			System.out.println(command1);
			Runtime.getRuntime().exec(command1).waitFor();

			// This will only work in Gnome based Linux distributions
			String command2 = "eog " + tmpSvgFile.getName();
			System.out.println(command2);
			Runtime.getRuntime().exec(command2).waitFor();

			// Remove the temporary file now that we don't need it anymore
			if (tmpDotFile.delete()) {
				System.out.println("Deleted: " + tmpDotFile.getName());
			} else {
				System.out.println("Failed to delete file: " + tmpDotFile.getName());
			}

			if (tmpSvgFile.delete()) {
				System.out.println("Deleted: " + tmpSvgFile.getName());
			} else {
				System.out.println("Failed to delete file: " + tmpSvgFile.getName());
			}

			String command3 = "rm -f " + tmpDotFile.getName();
			System.out.println(command3);
			Runtime.getRuntime().exec(command3).waitFor();

			String command4 = "rm -f " + tmpSvgFile.getName();
			System.out.println(command4);
			Runtime.getRuntime().exec(command4).waitFor();
		} else if (outputMethod.equals("JSON")) {
			System.out.println(rootNode.toJson());
		} else if (outputMethod.equals("DOT")) {
			System.out.println(rootNode.toDot());
		} else {
			System.err.println("Unsupported output method: " + args[1]);
			System.exit(5);
		}


	}

	public TokenStream getTokenStream() {
		return tokenStream;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public int errorCount() {
		return errorHandler.errorCount();
	}

	public int fatalErrorCount() {
		return errorHandler.fatalErrorCount();
	}

	public void printErrors() {
		printErrors(System.err);
	}

	public void printErrors(PrintStream printStream) {
		for (ParseError parseError : errorHandler.getErrors()) {
			printStream.println(parseError);
		}
	}

	public boolean isPattern(Type...types) throws ParserException {
		List<Token> tokens = peekList(types.length);

		for (int x = 0; x < types.length; x++) {
			Token token = tokens.get(x);
			Type type = types[x];

			if (token == null || type == null) {
				return false;
			} else if (!token.isType(type)) {
				return false;
			}
		}

		return true;
	}
*/
}

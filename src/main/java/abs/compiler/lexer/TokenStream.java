package abs.compiler.lexer;

import static abs.compiler.Util.tabs;
import static abs.compiler.lexer.Type.ADD;
import static abs.compiler.lexer.Type.ADDADD;
import static abs.compiler.lexer.Type.ADDEQ;
import static abs.compiler.lexer.Type.ANDAND;
import static abs.compiler.lexer.Type.ANDEQ;
import static abs.compiler.lexer.Type.COLON;
import static abs.compiler.lexer.Type.COLONCOLON;
import static abs.compiler.lexer.Type.COMMENT;
import static abs.compiler.lexer.Type.COMMENTC;
import static abs.compiler.lexer.Type.COMMENTL;
import static abs.compiler.lexer.Type.COMMENTO;
import static abs.compiler.lexer.Type.DIV;
import static abs.compiler.lexer.Type.DIVEQ;
import static abs.compiler.lexer.Type.DOLLAR;
import static abs.compiler.lexer.Type.EOF;
import static abs.compiler.lexer.Type.GT;
import static abs.compiler.lexer.Type.GTEQ;
import static abs.compiler.lexer.Type.LBRACE;
import static abs.compiler.lexer.Type.LF;
import static abs.compiler.lexer.Type.LT;
import static abs.compiler.lexer.Type.LTEQ;
import static abs.compiler.lexer.Type.MOD;
import static abs.compiler.lexer.Type.MODEQ;
import static abs.compiler.lexer.Type.MUL;
import static abs.compiler.lexer.Type.MULEQ;
import static abs.compiler.lexer.Type.NOT;
import static abs.compiler.lexer.Type.NOTEQ;
import static abs.compiler.lexer.Type.OREQ;
import static abs.compiler.lexer.Type.OROR;
import static abs.compiler.lexer.Type.PERIOD;
import static abs.compiler.lexer.Type.PIPE;
import static abs.compiler.lexer.Type.QUESTION;
import static abs.compiler.lexer.Type.RBRACE;
import static abs.compiler.lexer.Type.SL;
import static abs.compiler.lexer.Type.SLEQ;
import static abs.compiler.lexer.Type.SR;
import static abs.compiler.lexer.Type.SREQ;
import static abs.compiler.lexer.Type.SUBEQ;
import static abs.compiler.lexer.Type.SUBSUB;
import static abs.compiler.lexer.Type.TILDE;
import static abs.compiler.lexer.Type.TILDEEQ;
import static abs.compiler.lexer.Type.XOREQ;
import abs.ImplementMeException;
import abs.compiler.exception.IllegalCharacterException;
import abs.compiler.exception.UnexpectedEndOfInput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a lossless lexer for the abs programming language. This means that it is possible
 * to reconstruct the original source code from the tokens produced by this lexer. This is
 * necessary for building development tools.
 */
public class TokenStream {
    private final LinkedList<Token> buffer = new LinkedList<>();
    private final CharacterStream characterStream;
    private int depth = -1;
    private int index = 1;

    public TokenStream(CharacterStream characterStream, LexerOptions options) {
        this.characterStream = characterStream;
        setOptions(options);
    }

    public Token next() {
        depth = 0;
        trace(tabs(depth) + "invoke<next>");

        // Return tokens from the buffer, if there are any available
        if (buffer.size() > 0) {
            return buffer.removeFirst();
        } else {
            int c = characterStream.peek();

            loop: while (c != CharacterStream.END_OF_FILE) {
                switch ((char)c) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        readNumber();
                        break loop;
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'n':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 'r':
                    case 's':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    case 'y':
                    case 'z':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '_':
                        readIdentifier();
                        break loop;
                    case '/':
                        readComment();
                        break loop;
                    case ' ':
                        readWhitespace();
                        break loop;
                    case '"':
                        readStringConstant();
                        break loop;
                    case '^':
                    case '!':
                    case '>':
                    case '%':
                    case '&':
                    case '~':
                    case '*':
                    case '+':
                    case '-':
                    case '.':
                    case '?':
                    case '<':
                    case ':':
                    case '|':
                    case '=':
                        readOperator();
                        break loop;
                    case '[':
                    case ']':
                    case '(':
                    case ')':
                    case ',':
                    case '{':
                    case ';':
                    case '}':
                        readSymbol();
                        break loop;
                    case '\t':
                        Position start1 = characterStream.getCurrentPosition();
                        characterStream.next();
                        buffer.addLast(new Token(Type.TAB, new Range(start1, characterStream.getCurrentPosition()), '\t'));
                        break loop;
                    case '\n':
                        Position start2 = characterStream.getCurrentPosition();
                        characterStream.next();
                        buffer.addLast(new Token(LF, new Range(start2, characterStream.getCurrentPosition()), '\n'));
                        break loop;
                    default:
                        throw new IllegalCharacterException((char)c, characterStream.getCurrentPosition());
                }
            }
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<next>");
        }

        if (buffer.size() == 0) {
            return new Token(EOF, null);
        }

        index++;

        return buffer.removeFirst();
    }

    /**
     * Skip the next token without doing anything with it.
     */
    public void eat() {
        next();
    }

    /**
     * Skip the next n tokens without doing anything with them.
     */
    public void eat(int count) {
        for (int i = 0; i < count; i++) {
            next();
        }
    }

    /**
     * Eat the current token and return the value of peek
     */
    public Token eatAndPeek() {
        eat();
        return peek();
    }

    public Token peek() {
        Token t = next();
        buffer.addFirst(t);
        return t;
    }

    public Token peek(int num) {
        int originalIndex = index;

        List<Token> extraTokens = new ArrayList<>();

        // Load more tokens, if necessary
        Token t;
        while (buffer.size() + extraTokens.size() < num + 1 && !(t=next()).getType().equals(EOF)) {
            extraTokens.add(t);
        }

        // Add the newly loaded tokens to the buffer
        if (extraTokens.size() > 0) {
            buffer.addAll(extraTokens);
        }

        // Return eof if we still don't have enough tokens in the buffer
        if (buffer.size() < num + 1) {
            return new Token(EOF, null);
        }

        // Restore the index to what it was prior to this call
        index = originalIndex;

        // Otherwise, return the requested token
        return buffer.get(num);
    }

    public int getIndex() {
        return index;
    }

    protected Token readSymbol() {
        if (traceEnabled) {
            depth = 1;
            trace(tabs(depth) + "invoke<readSymbol()>");
        }
        Position firstPosition = characterStream.getCurrentPosition();
        int c = characterStream.next();
        Type type = Type.lookup((char)c);

        if (type == null) {
            throw new IllegalCharacterException((char)c, firstPosition);
        }

        buffer.addLast(new Token(type, new Range(firstPosition, characterStream.getCurrentPosition())));

        if (traceEnabled) {
            trace(tabs(depth) + "return<readSymbol>");
        }

        return buffer.getFirst();
    }

    protected void readStringInterpolation() {
        if (traceEnabled) {
            depth = 2;
            trace(tabs(depth) + "invoke<readStringInterpolation()>");
        }

        // Return immediately if the next character isn't a dollar sign
        if ((char)characterStream.peek() != '$') {
            return;
        }

        // Create a token for the dollar sign
        Position position = characterStream.getCurrentPosition();
        int c = characterStream.next();
        buffer.addLast(new Token(DOLLAR, new Range(position, getPosition()), (char)c));

        loop: while ((c = characterStream.peek()) != CharacterStream.END_OF_FILE) {
            switch ((char)c) {
                case '{':
                    position = characterStream.getCurrentPosition();
                    c = characterStream.next();
                    buffer.addLast(new Token(LBRACE, new Range(position, getPosition()), (char)c));
                    break;
                case '}':
                    position = characterStream.getCurrentPosition();
                    c = characterStream.next();
                    buffer.addLast(new Token(RBRACE, new Range(position, getPosition()), (char)c));
                    break;
                case '.':
                    position = characterStream.getCurrentPosition();
                    c = characterStream.next();
                    buffer.addLast(new Token(PERIOD, new Range(position, getPosition()), (char)c));
                    break;
                case 'a': case 'b': case 'c': case 'd':
                case 'e': case 'f': case 'g': case 'h':
                case 'i': case 'j': case 'k': case 'l':
                case 'm': case 'n': case 'o': case 'p':
                case 'q': case 'r': case 's': case 't':
                case 'u': case 'v': case 'w': case 'x':
                case 'y': case 'z': case 'A': case 'B':
                case 'C': case 'D': case 'E': case 'F':
                case 'G': case 'H': case 'I': case 'J':
                case 'K': case 'L': case 'M': case 'N':
                case 'O': case 'P': case 'Q': case 'R':
                case 'S': case 'T': case 'U': case 'V':
                case 'W': case 'X': case 'Y': case 'Z':
                case '_': case '0': case '1': case '2':
                case '3': case '4': case '5': case '6':
                case '7': case '8': case '9':
                    readIdentifier();
                    break;
                default:
                    break loop;
            }
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readStringInterpolation>");
        }
    }

    protected Token readStringConstant() {
        if (traceEnabled) {
            depth = 1;
            trace(tabs(depth) + "invoke<readStringConstant()>");
        }

        Position firstPosition = characterStream.getCurrentPosition();
        int c = characterStream.next();

        if (c == CharacterStream.END_OF_FILE) {
            throw new UnexpectedEndOfInput(characterStream.getCurrentPosition());
        } else if ((char)c != '"') {
            throw new IllegalCharacterException((char)c, firstPosition);
        }

        Position constantFirst = characterStream.getCurrentPosition();

        // This is the token which will be returned
        buffer.addLast(new Token(Type.DQUOTE, new Range(firstPosition, constantFirst)));

        c = characterStream.peek();

        if (c == CharacterStream.END_OF_FILE) {
            throw new UnexpectedEndOfInput(characterStream.getCurrentPosition());
        }

        if ((char)c == '"') {
            // Don't bother looking for a string constant if this is an empty string
            characterStream.next();
            buffer.addLast(new Token(
                Type.DQUOTE,
                new Range(constantFirst, characterStream.getCurrentPosition()),
                "" + (char)c
            ));
            return buffer.getFirst();
        } else {
            StringBuilder sb = new StringBuilder();
            boolean isEscaped = false;

            while((c=characterStream.peek()) != CharacterStream.END_OF_FILE) {
                // Accept any escaped character as valid. Leave error detection of characters which should not be
                // escaped to the parser
                if ((char) c == '\\') {
                    sb.append((char)characterStream.next());

                    if (characterStream.peek() == CharacterStream.END_OF_FILE) {
                        throw new UnexpectedEndOfInput(characterStream.getCurrentPosition());
                    }

                    sb.append((char)characterStream.next());
                    isEscaped = true;

                    if (characterStream.peek() == CharacterStream.END_OF_FILE) {
                        throw new UnexpectedEndOfInput(characterStream.getCurrentPosition());
                    }
                } else if ((char)c == '"') {
                    isEscaped = false;
                    Position quoteStart = characterStream.getCurrentPosition();

                    if (sb.length() > 0) {
                        buffer.addLast(new Token(Type.STRCONST, new Range(constantFirst, quoteStart), sb.toString()));
                    }

                    characterStream.next();
                    buffer.addLast(new Token(Type.DQUOTE, new Range(quoteStart, characterStream.getCurrentPosition())));
                    break;
                } else if (!isEscaped && (char)c == '$') {
                    // Create a token for the string constant which has already been read
                    if (sb.length() > 0) {
                        buffer.addLast(new Token(Type.STRCONST, new Range(constantFirst, getPosition()), sb.toString()));

                        // Clear the buffer
                        sb.setLength(0);
                    }

                    // Attempt to read the interpolated variable
                    readStringInterpolation();
                    constantFirst = getPosition();
                } else {
                    isEscaped = false;
                    sb.append((char)characterStream.next());
                }
            }
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readStringConstant>");
        }

        return buffer.getFirst();
    }

    protected Token readWhitespace () {
        if (traceEnabled) {
            depth = 1;
            trace(tabs(depth) + "invoke<readWhitespace()>");
        }

        StringBuilder sb = new StringBuilder();
        Position firstPosition = characterStream.getCurrentPosition();

        while(characterStream.peek() != CharacterStream.END_OF_FILE) {
            if ((char)characterStream.peek() == ' ') {
                sb.append((char)characterStream.next());
            } else {
                buffer.addLast(new Token(Type.WHITESPACE, new Range(firstPosition, characterStream.getCurrentPosition()), sb.toString()));
                break;
            }
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readWhitespace>");
        }

        return buffer.getFirst();
    }

    protected Token readIdentifier() {
        if (traceEnabled) {
            depth = 1;
            trace(tabs(depth) + "invoke<readName()>");
        }

        StringBuilder sb = new StringBuilder();
        Position firstPosition = characterStream.getCurrentPosition();

        loop: while(characterStream.peek() != CharacterStream.END_OF_FILE) {
            switch((char)characterStream.peek()) {
                case 'a': case 'b': case 'c': case 'd':
                case 'e': case 'f': case 'g': case 'h':
                case 'i': case 'j': case 'k': case 'l':
                case 'm': case 'n': case 'o': case 'p':
                case 'q': case 'r': case 's': case 't':
                case 'u': case 'v': case 'w': case 'x':
                case 'y': case 'z': case 'A': case 'B':
                case 'C': case 'D': case 'E': case 'F':
                case 'G': case 'H': case 'I': case 'J':
                case 'K': case 'L': case 'M': case 'N':
                case 'O': case 'P': case 'Q': case 'R':
                case 'S': case 'T': case 'U': case 'V':
                case 'W': case 'X': case 'Y': case 'Z':
                case '_': case '0': case '1': case '2':
                case '3': case '4': case '5': case '6':
                case '7': case '8': case '9':
                    sb.append((char)characterStream.next());
                    break;
                default:
                    break loop;
            }
        }

        String value = sb.toString();

        // Attempt to resolve this token to something more specific
        Type type = Type.coalesce(Type.lookup(value), Type.IDENTIFIER);
        buffer.addLast(new Token(type, new Range(firstPosition, characterStream.getCurrentPosition()), value));

        if (traceEnabled) {
            trace(tabs(depth) + "return<readName>");
        }

        return buffer.getFirst();
    }

    protected Token readNumber() {
        if (traceEnabled) {
            trace(tabs(depth) + "invoke<readNumber()>");
        }

        StringBuilder sb = new StringBuilder();
        Position firstPosition = characterStream.getCurrentPosition();
        int first = characterStream.next();
        int c;

        sb.append((char)first);

        if (first == '0' && characterStream.peek() == 'x') { // Hex number
            sb.append((char)characterStream.next());
            c = characterStream.peek();

            loop: while (c != CharacterStream.END_OF_FILE) {
                switch ((char)c) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                        sb.append((char)characterStream.next());
                        break;
                    default:
                        break loop;
                }

                c = characterStream.peek();
            }

            buffer.addLast(new Token(Type.HEX_LITERAL, new Range(firstPosition, characterStream.getCurrentPosition()), sb.toString()));
        } else {
            Type type = Type.WHOLE_NUMBER_LITERAL;
            c = characterStream.peek();

            // This is necessary because the first character is not processed by the following switch statement
            if ((char)first == '.') {
                type = Type.FLOAT_LITERAL;
            }

            loop: while (c != CharacterStream.END_OF_FILE) {
                switch ((char)c) {
                    case '.':
                        type = Type.FLOAT_LITERAL;
                        sb.append((char)characterStream.next());
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        sb.append((char)characterStream.next());
                        break;
                    default:
                        break loop;
                }

                c = characterStream.peek();
            }

            buffer.addLast(new Token(type, new Range(firstPosition, characterStream.getCurrentPosition()), sb.toString()));
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readNumber> ; ");
        }

        return buffer.getFirst();
    }

    protected Token readOperator() {
        if (traceEnabled) {
            depth = 1;
            trace(tabs(depth) + "invoke<readOperator()>");
        }

        Position firstPosition = characterStream.getCurrentPosition();
        int first = characterStream.peek(0);
        int second = characterStream.peek(1);
        int third = characterStream.peek(2);

        switch ((char)first) {
            case '.':
                switch ((char) second) {
                    case '=':
                        characterStream.next();
                        characterStream.next();
                        buffer.addLast(new Token(Type.PERIODEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        readNumber();
                        break;
                    default:
                        characterStream.next();
                        buffer.addLast(new Token(Type.PERIOD, new Range(firstPosition, characterStream.getCurrentPosition())));
                        break;
                }
                break;
            case '=':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(Type.EQEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(Type.EQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '-':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(SUBEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char)second == '-') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(SUBSUB, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(Type.SUB, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '&':
                if ((char) second == '&') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(ANDAND, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(ANDEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(Type.AND, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '^':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(XOREQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(Type.XOR, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '|':
                if ((char) second == '|') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(OROR, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(OREQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '>') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(PIPE, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(Type.OR, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '+':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(ADDEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '+') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(ADDADD, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(ADD, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '*':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(MULEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(MUL, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '/':
                if (((char)second == '/' || (char)second == '*')) {
                    readComment();
                } else if ((char)second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(DIVEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(DIV, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '>':
                if ((char) second == '>' && (char)third == '=') {
                    characterStream.next();
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(SREQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '>') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(SR, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(GTEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(GT, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '<':
                if ((char) second == '<' && (char)third == '=') {
                    characterStream.next();
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(SLEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '<') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(SL, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(LTEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(LT, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '?':
                characterStream.next();
                buffer.addLast(new Token(QUESTION, new Range(firstPosition, characterStream.getCurrentPosition())));
                break;
            case ':':
                if ((char) second == ':') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(COLONCOLON, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(COLON, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '~':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(TILDEEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(TILDE, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '%':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(MODEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(MOD, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;
            case '!':
                if ((char) second == '=') {
                    characterStream.next();
                    characterStream.next();
                    buffer.addLast(new Token(NOTEQ, new Range(firstPosition, characterStream.getCurrentPosition())));
                } else {
                    characterStream.next();
                    buffer.addLast(new Token(NOT, new Range(firstPosition, characterStream.getCurrentPosition())));
                }
                break;

            // TODO: Add support for the remaining operators

            default:
                throw new IllegalCharacterException(
                    (char)first,
                    characterStream.getCurrentPosition()
                );
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readOperator>");
        }

        return buffer.getFirst();
    }

    // Do not call this method outside of the readComment method
    protected Token readSingleLineComment() {
        if (traceEnabled) {
            trace(tabs(depth) + "invoke<readSingleLineComment()>");
        }

        Position firstPosition = characterStream.getCurrentPosition();
        StringBuilder sb = new StringBuilder();

        // Read the two characters which were previously peeked
        characterStream.next();
        characterStream.next();

        // Create a token for the single line comment
        buffer.addLast(new Token(COMMENTL, new Range(firstPosition, characterStream.getCurrentPosition())));

        // Record the position of the first character in the comment
        Position commentStart = characterStream.getCurrentPosition();

        boolean commentCreated = false;

        // Read through to the end of the line(or end of input)
        loop:
        do {
            // Get the current position within the stream
            Position commentEnd = characterStream.getCurrentPosition();

            switch ((char)characterStream.peek()) {
                case '\n':
                    Position lfPosition = characterStream.getCurrentPosition();
                    characterStream.next();
                    buffer.addLast(new Token(COMMENT, new Range(commentStart, commentEnd), sb.toString()));
                    buffer.addLast(new Token(LF, new Range(lfPosition, characterStream.getCurrentPosition()), '\n'));
                    commentCreated = true;
                    break loop;
                default:
                    sb.append((char)characterStream.next());
                    break;
            }
        } while (characterStream.peek() != CharacterStream.END_OF_FILE);

        // This *should* only be necessary if a single line comment appeared at the end of a source file without
        // a trailing line feed character
        if (!commentCreated) {
            buffer.addLast(new Token(COMMENT, new Range(commentStart, characterStream.getCurrentPosition()), sb.toString()));
            commentCreated = true;
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readSingleLineComment>");
        }

        return buffer.getFirst();
    }

    protected Token readMultiLineComment() {
        if (traceEnabled) {
            trace(tabs(depth) + "invoke<readMultiLineComment()>");
        }

        Position firstPosition = characterStream.getCurrentPosition();
        StringBuilder sb = new StringBuilder();

        characterStream.next();
        characterStream.next();
        buffer.addLast(new Token(COMMENTO, new Range(firstPosition, characterStream.getCurrentPosition())));
        Position commentStart = characterStream.getCurrentPosition();

        // Read through to the end of the line
        while (characterStream.peek() != CharacterStream.END_OF_FILE) {
            if ((char)characterStream.peek() == '*') {
                Position commentEnd = characterStream.getCurrentPosition();
                characterStream.next();

                if ((char)characterStream.peek() == '/') {
                    characterStream.next();
                    buffer.addLast(new Token(COMMENT, new Range(commentStart, commentEnd), sb.toString()));
                    buffer.addLast(new Token(COMMENTC, new Range(commentEnd, characterStream.getCurrentPosition())));
                    break;
                } else {
                    sb.append('*');
                }
            } else {
                sb.append((char)characterStream.next());
            }
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readMultiLineComment>");
        }

        return buffer.getFirst();
    }

    protected Token readComment() {
        if (traceEnabled) {
            depth = 1;
            trace(tabs(depth) + "invoke<readComment()>");
        }
        int first = characterStream.peek(0);
        int second = characterStream.peek(1);

        if ((char)first == '/') {
            if ((char)second == '/') {
                readSingleLineComment();
            } else if ((char)second == '*') {
                readMultiLineComment();
            } else {
                // This is a division operator not a command
                readOperator();
            }
        } else {
            throw new IllegalCharacterException((char)first, characterStream.getCurrentPosition(), "Comments must begin with a forward slash");
        }

        if (traceEnabled) {
            trace(tabs(depth) + "return<readComment>");
        }

        return buffer.getFirst();
    }

    public Position getPosition() {
        return characterStream.getCurrentPosition();
    }

    public String toJson() {
        // TODO: Extract all tokens from the character stream and return a JSON representation of them
        throw new ImplementMeException();
    }

    // Extremely simple interface for invoking the token stream
    public static void main(String[] args) throws FileNotFoundException {
        String filename = args[0];
        File file = new File(filename);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filename);
            System.exit(1);
        }

        LexerOptions o = new LexerOptions();;

        CharacterStream characterStream = new CharacterStream(new FileInputStream(file), o);
        TokenStream tokenStream = new TokenStream(characterStream, o);

        System.out.println(tokenStream.toJson());
    }

    protected LexerOptions options;
    protected boolean traceEnabled = false;

    public LexerOptions getOptions() {
        return options;
    }

    public void setOptions(LexerOptions options) {
        this.options = options;

        if (options.isTraceEnabled()) {
            traceEnabled = true;
        }
    }

    protected PrintStream trace(String s) {
        if (options.isTraceEnabled()) {
            options.getTrace().println(s);
        }
        return options.getTrace();
    }

    protected PrintStream debug(String s) {
        if (options.isDebugEnabled()) {
            options.getDebug().println(s);
        }
        return options.getDebug();
    }
}

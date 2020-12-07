package com.asd2.beerdistributiongame.gameagent.parser;// Generated from AgentGrammar.g4 by ANTLR 4.7.1

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AgentGrammarLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            PLUS = 1, MIN = 2, MUL = 3, DIV = 4, EXP = 5, ROOT = 6, LESS = 7, GREATER = 8, ORDER = 9,
            IF = 10, THEN = 11, EQUALS = 12, SEMICOLON = 13, STANDARD_VAR = 14, ORDER_VAR = 15,
            PERSONAL_VAR = 16, NUMBER = 17, DECIMAL = 18, OPEN_ROUND_BRACKET = 19, CLOSE_ROUND_BRACKET = 20,
            OPEN_SQUARE_BRACKET = 21, CLOSE_SQUARE_BRACKET = 22, WS = 23;
    public static String[] channelNames = {
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    };

    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

    public static final String[] ruleNames = {
            "PLUS", "MIN", "MUL", "DIV", "EXP", "ROOT", "LESS", "GREATER", "ORDER",
            "IF", "THEN", "EQUALS", "SEMICOLON", "STANDARD_VAR", "ORDER_VAR", "PERSONAL_VAR",
            "NUMBER", "DECIMAL", "OPEN_ROUND_BRACKET", "CLOSE_ROUND_BRACKET", "OPEN_SQUARE_BRACKET",
            "CLOSE_SQUARE_BRACKET", "WS"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'+'", "'-'", "'*'", "'/'", "'^'", "'\u221A'", "'<'", "'>'", "'NewOrder'",
            "'if'", "'then'", "'='", "';'", null, "'Order'", null, null, null, "'('",
            "')'", "'['", "']'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, "PLUS", "MIN", "MUL", "DIV", "EXP", "ROOT", "LESS", "GREATER", "ORDER",
            "IF", "THEN", "EQUALS", "SEMICOLON", "STANDARD_VAR", "ORDER_VAR", "PERSONAL_VAR",
            "NUMBER", "DECIMAL", "OPEN_ROUND_BRACKET", "CLOSE_ROUND_BRACKET", "OPEN_SQUARE_BRACKET",
            "CLOSE_SQUARE_BRACKET", "WS"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }


    public AgentGrammarLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @Override
    public String getGrammarFileName() {
        return "AgentGrammar.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\31\u00ef\b\1\4\2" +
                    "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4" +
                    "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22" +
                    "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2" +
                    "\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3" +
                    "\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r" +
                    "\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17" +
                    "\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17" +
                    "\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17" +
                    "\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17" +
                    "\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17" +
                    "\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17" +
                    "\u00a8\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\6\21\u00b2\n\21\r" +
                    "\21\16\21\u00b3\3\21\5\21\u00b7\n\21\3\21\7\21\u00ba\n\21\f\21\16\21\u00bd" +
                    "\13\21\3\22\3\22\3\22\7\22\u00c2\n\22\f\22\16\22\u00c5\13\22\5\22\u00c7" +
                    "\n\22\3\23\3\23\3\23\7\23\u00cc\n\23\f\23\16\23\u00cf\13\23\3\23\3\23" +
                    "\7\23\u00d3\n\23\f\23\16\23\u00d6\13\23\3\23\3\23\7\23\u00da\n\23\f\23" +
                    "\16\23\u00dd\13\23\5\23\u00df\n\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27" +
                    "\3\27\3\30\6\30\u00ea\n\30\r\30\16\30\u00eb\3\30\3\30\2\2\31\3\3\5\4\7" +
                    "\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22" +
                    "#\23%\24\'\25)\26+\27-\30/\31\3\2\b\3\2c|\3\2C\\\3\2\62\62\3\2\63;\3\2" +
                    "\62;\5\2\13\f\17\17\"\"\2\u00ff\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2" +
                    "\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2" +
                    "\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2" +
                    "\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2" +
                    "\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\3\61\3\2\2\2\5\63\3\2\2\2\7\65\3\2\2" +
                    "\2\t\67\3\2\2\2\139\3\2\2\2\r;\3\2\2\2\17=\3\2\2\2\21?\3\2\2\2\23A\3\2" +
                    "\2\2\25J\3\2\2\2\27M\3\2\2\2\31R\3\2\2\2\33T\3\2\2\2\35\u00a7\3\2\2\2" +
                    "\37\u00a9\3\2\2\2!\u00af\3\2\2\2#\u00c6\3\2\2\2%\u00de\3\2\2\2\'\u00e0" +
                    "\3\2\2\2)\u00e2\3\2\2\2+\u00e4\3\2\2\2-\u00e6\3\2\2\2/\u00e9\3\2\2\2\61" +
                    "\62\7-\2\2\62\4\3\2\2\2\63\64\7/\2\2\64\6\3\2\2\2\65\66\7,\2\2\66\b\3" +
                    "\2\2\2\678\7\61\2\28\n\3\2\2\29:\7`\2\2:\f\3\2\2\2;<\7\u221c\2\2<\16\3" +
                    "\2\2\2=>\7>\2\2>\20\3\2\2\2?@\7@\2\2@\22\3\2\2\2AB\7P\2\2BC\7g\2\2CD\7" +
                    "y\2\2DE\7Q\2\2EF\7t\2\2FG\7f\2\2GH\7g\2\2HI\7t\2\2I\24\3\2\2\2JK\7k\2" +
                    "\2KL\7h\2\2L\26\3\2\2\2MN\7v\2\2NO\7j\2\2OP\7g\2\2PQ\7p\2\2Q\30\3\2\2" +
                    "\2RS\7?\2\2S\32\3\2\2\2TU\7=\2\2U\34\3\2\2\2VW\7U\2\2WX\7v\2\2XY\7q\2" +
                    "\2YZ\7e\2\2Z\u00a8\7m\2\2[\\\7D\2\2\\]\7w\2\2]^\7f\2\2^_\7i\2\2_`\7g\2" +
                    "\2`\u00a8\7v\2\2ab\7D\2\2bc\7c\2\2cd\7e\2\2de\7m\2\2ef\7n\2\2fg\7q\2\2" +
                    "g\u00a8\7i\2\2hi\7R\2\2ij\7t\2\2jk\7q\2\2kl\7f\2\2lm\7w\2\2mn\7e\2\2n" +
                    "o\7v\2\2op\7k\2\2pq\7q\2\2qr\7p\2\2rs\7E\2\2st\7q\2\2tu\7u\2\2u\u00a8" +
                    "\7v\2\2vw\7U\2\2wx\7v\2\2xy\7q\2\2yz\7e\2\2z{\7m\2\2{|\7E\2\2|}\7q\2\2" +
                    "}~\7u\2\2~\u00a8\7v\2\2\177\u0080\7D\2\2\u0080\u0081\7c\2\2\u0081\u0082" +
                    "\7e\2\2\u0082\u0083\7m\2\2\u0083\u0084\7n\2\2\u0084\u0085\7q\2\2\u0085" +
                    "\u0086\7i\2\2\u0086\u0087\7E\2\2\u0087\u0088\7q\2\2\u0088\u0089\7u\2\2" +
                    "\u0089\u00a8\7v\2\2\u008a\u008b\7K\2\2\u008b\u008c\7p\2\2\u008c\u008d" +
                    "\7e\2\2\u008d\u008e\7q\2\2\u008e\u008f\7o\2\2\u008f\u0090\7k\2\2\u0090" +
                    "\u0091\7p\2\2\u0091\u0092\7i\2\2\u0092\u0093\7Q\2\2\u0093\u0094\7t\2\2" +
                    "\u0094\u0095\7f\2\2\u0095\u0096\7g\2\2\u0096\u00a8\7t\2\2\u0097\u0098" +
                    "\7K\2\2\u0098\u0099\7p\2\2\u0099\u009a\7e\2\2\u009a\u009b\7q\2\2\u009b" +
                    "\u009c\7o\2\2\u009c\u009d\7k\2\2\u009d\u009e\7p\2\2\u009e\u009f\7i\2\2" +
                    "\u009f\u00a0\7F\2\2\u00a0\u00a1\7g\2\2\u00a1\u00a2\7n\2\2\u00a2\u00a3" +
                    "\7k\2\2\u00a3\u00a4\7x\2\2\u00a4\u00a5\7g\2\2\u00a5\u00a6\7t\2\2\u00a6" +
                    "\u00a8\7{\2\2\u00a7V\3\2\2\2\u00a7[\3\2\2\2\u00a7a\3\2\2\2\u00a7h\3\2" +
                    "\2\2\u00a7v\3\2\2\2\u00a7\177\3\2\2\2\u00a7\u008a\3\2\2\2\u00a7\u0097" +
                    "\3\2\2\2\u00a8\36\3\2\2\2\u00a9\u00aa\7Q\2\2\u00aa\u00ab\7t\2\2\u00ab" +
                    "\u00ac\7f\2\2\u00ac\u00ad\7g\2\2\u00ad\u00ae\7t\2\2\u00ae \3\2\2\2\u00af" +
                    "\u00b1\7B\2\2\u00b0\u00b2\t\2\2\2\u00b1\u00b0\3\2\2\2\u00b2\u00b3\3\2" +
                    "\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00bb\3\2\2\2\u00b5" +
                    "\u00b7\t\3\2\2\u00b6\u00b5\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\3\2" +
                    "\2\2\u00b8\u00ba\t\2\2\2\u00b9\u00b6\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb" +
                    "\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\"\3\2\2\2\u00bd\u00bb\3\2\2\2" +
                    "\u00be\u00c7\t\4\2\2\u00bf\u00c3\t\5\2\2\u00c0\u00c2\t\6\2\2\u00c1\u00c0" +
                    "\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4" +
                    "\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c6\u00be\3\2\2\2\u00c6\u00bf\3\2" +
                    "\2\2\u00c7$\3\2\2\2\u00c8\u00c9\t\6\2\2\u00c9\u00cd\7\60\2\2\u00ca\u00cc" +
                    "\t\6\2\2\u00cb\u00ca\3\2\2\2\u00cc\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd" +
                    "\u00ce\3\2\2\2\u00ce\u00df\3\2\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d4\t\5" +
                    "\2\2\u00d1\u00d3\t\6\2\2\u00d2\u00d1\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4" +
                    "\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d7\3\2\2\2\u00d6\u00d4\3\2" +
                    "\2\2\u00d7\u00db\7\60\2\2\u00d8\u00da\t\6\2\2\u00d9\u00d8\3\2\2\2\u00da" +
                    "\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00df\3\2" +
                    "\2\2\u00dd\u00db\3\2\2\2\u00de\u00c8\3\2\2\2\u00de\u00d0\3\2\2\2\u00df" +
                    "&\3\2\2\2\u00e0\u00e1\7*\2\2\u00e1(\3\2\2\2\u00e2\u00e3\7+\2\2\u00e3*" +
                    "\3\2\2\2\u00e4\u00e5\7]\2\2\u00e5,\3\2\2\2\u00e6\u00e7\7_\2\2\u00e7.\3" +
                    "\2\2\2\u00e8\u00ea\t\7\2\2\u00e9\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb" +
                    "\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00ee\b\30" +
                    "\2\2\u00ee\60\3\2\2\2\16\2\u00a7\u00b3\u00b6\u00bb\u00c3\u00c6\u00cd\u00d4" +
                    "\u00db\u00de\u00eb\3\b\2\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
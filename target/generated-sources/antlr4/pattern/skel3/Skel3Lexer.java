// Generated from pattern\skel3\Skel3.g4 by ANTLR 4.7
package pattern.skel3;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class Skel3Lexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, Seq=7, Comp=8, Farm=9, 
		Pipe=10, Map=11, IDENTIFIER=12, NUMBER=13, COMMENT=14, SPACE=15, OTHER=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "Seq", "Comp", "Farm", 
		"Pipe", "Map", "IDENTIFIER", "NUMBER", "COMMENT", "SPACE", "OTHER"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'='", "'main'", "'('", "')'", "','", "'Seq'", "'Comp'", 
		"'Farm'", "'Pipe'", "'Map'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, "Seq", "Comp", "Farm", "Pipe", 
		"Map", "IDENTIFIER", "NUMBER", "COMMENT", "SPACE", "OTHER"
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


	public Skel3Lexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Skel3.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22d\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\f\3\r\3\r\7\rL\n\r\f\r\16\rO\13\r\3\16\6\16R\n\16\r\16\16\16S\3"+
		"\17\3\17\7\17X\n\17\f\17\16\17[\13\17\3\17\3\17\3\20\3\20\3\20\3\20\3"+
		"\21\3\21\2\2\22\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22\3\2\7\4\2C\\c|\5\2\62;C\\c|\3\2\62;\4\2\f\f"+
		"\17\17\5\2\13\f\17\17\"\"\2f\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\3#\3\2\2\2\5%\3\2\2\2\7\'\3\2\2\2\t,\3\2\2\2\13.\3"+
		"\2\2\2\r\60\3\2\2\2\17\62\3\2\2\2\21\66\3\2\2\2\23;\3\2\2\2\25@\3\2\2"+
		"\2\27E\3\2\2\2\31I\3\2\2\2\33Q\3\2\2\2\35U\3\2\2\2\37^\3\2\2\2!b\3\2\2"+
		"\2#$\7=\2\2$\4\3\2\2\2%&\7?\2\2&\6\3\2\2\2\'(\7o\2\2()\7c\2\2)*\7k\2\2"+
		"*+\7p\2\2+\b\3\2\2\2,-\7*\2\2-\n\3\2\2\2./\7+\2\2/\f\3\2\2\2\60\61\7."+
		"\2\2\61\16\3\2\2\2\62\63\7U\2\2\63\64\7g\2\2\64\65\7s\2\2\65\20\3\2\2"+
		"\2\66\67\7E\2\2\678\7q\2\289\7o\2\29:\7r\2\2:\22\3\2\2\2;<\7H\2\2<=\7"+
		"c\2\2=>\7t\2\2>?\7o\2\2?\24\3\2\2\2@A\7R\2\2AB\7k\2\2BC\7r\2\2CD\7g\2"+
		"\2D\26\3\2\2\2EF\7O\2\2FG\7c\2\2GH\7r\2\2H\30\3\2\2\2IM\t\2\2\2JL\t\3"+
		"\2\2KJ\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2N\32\3\2\2\2OM\3\2\2\2PR\t"+
		"\4\2\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\34\3\2\2\2UY\7%\2\2VX"+
		"\n\5\2\2WV\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z\\\3\2\2\2[Y\3\2\2\2"+
		"\\]\b\17\2\2]\36\3\2\2\2^_\t\6\2\2_`\3\2\2\2`a\b\20\2\2a \3\2\2\2bc\13"+
		"\2\2\2c\"\3\2\2\2\6\2MSY\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
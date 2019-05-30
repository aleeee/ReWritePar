// Generated from pattern\Pattern.g4 by ANTLR 4.7
package pattern;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PatternParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SCOL=1, ASSIGN=2, OPAR=3, CPAR=4, ID=5, INT=6, COMMENT=7, SPACE=8, OTHER=9, 
		COMP=10, FARM=11, PIPE=12, MAP=13, OBRACE=14, CBRACE=15;
	public static final int
		RULE_parse = 0, RULE_pattern = 1, RULE_main = 2, RULE_stream = 3, RULE_sequence = 4, 
		RULE_dataparallel = 5, RULE_seq = 6, RULE_comp = 7, RULE_farm = 8, RULE_pipe = 9, 
		RULE_map = 10, RULE_block = 11;
	public static final String[] ruleNames = {
		"parse", "pattern", "main", "stream", "sequence", "dataparallel", "seq", 
		"comp", "farm", "pipe", "map", "block"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'='", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "SCOL", "ASSIGN", "OPAR", "CPAR", "ID", "INT", "COMMENT", "SPACE", 
		"OTHER", "COMP", "FARM", "PIPE", "MAP", "OBRACE", "CBRACE"
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

	@Override
	public String getGrammarFileName() { return "Pattern.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PatternParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ParseContext extends ParserRuleContext {
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public TerminalNode EOF() { return getToken(PatternParser.EOF, 0); }
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitParse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			pattern();
			setState(25);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PatternContext extends ParserRuleContext {
		public Token OTHER;
		public StreamContext stream() {
			return getRuleContext(StreamContext.class,0);
		}
		public SequenceContext sequence() {
			return getRuleContext(SequenceContext.class,0);
		}
		public DataparallelContext dataparallel() {
			return getRuleContext(DataparallelContext.class,0);
		}
		public MainContext main() {
			return getRuleContext(MainContext.class,0);
		}
		public TerminalNode OTHER() { return getToken(PatternParser.OTHER, 0); }
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_pattern);
		try {
			setState(33);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(27);
				stream();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(28);
				sequence();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(29);
				dataparallel();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(30);
				main();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(31);
				((PatternContext)_localctx).OTHER = match(OTHER);
				System.err.println("unknown char: " + (((PatternContext)_localctx).OTHER!=null?((PatternContext)_localctx).OTHER.getText():null));
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MainContext extends ParserRuleContext {
		public StreamContext stream() {
			return getRuleContext(StreamContext.class,0);
		}
		public SequenceContext sequence() {
			return getRuleContext(SequenceContext.class,0);
		}
		public DataparallelContext dataparallel() {
			return getRuleContext(DataparallelContext.class,0);
		}
		public MainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_main; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitMain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainContext main() throws RecognitionException {
		MainContext _localctx = new MainContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_main);
		try {
			setState(38);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FARM:
			case PIPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				stream();
				}
				break;
			case ID:
			case COMP:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				sequence();
				}
				break;
			case MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(37);
				dataparallel();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StreamContext extends ParserRuleContext {
		public FarmContext farm() {
			return getRuleContext(FarmContext.class,0);
		}
		public PipeContext pipe() {
			return getRuleContext(PipeContext.class,0);
		}
		public StreamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stream; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitStream(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StreamContext stream() throws RecognitionException {
		StreamContext _localctx = new StreamContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_stream);
		try {
			setState(42);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FARM:
				enterOuterAlt(_localctx, 1);
				{
				setState(40);
				farm();
				}
				break;
			case PIPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(41);
				pipe();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SequenceContext extends ParserRuleContext {
		public SeqContext seq() {
			return getRuleContext(SeqContext.class,0);
		}
		public CompContext comp() {
			return getRuleContext(CompContext.class,0);
		}
		public SequenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequence; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitSequence(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceContext sequence() throws RecognitionException {
		SequenceContext _localctx = new SequenceContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_sequence);
		try {
			setState(46);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				seq();
				}
				break;
			case COMP:
				enterOuterAlt(_localctx, 2);
				{
				setState(45);
				comp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataparallelContext extends ParserRuleContext {
		public MapContext map() {
			return getRuleContext(MapContext.class,0);
		}
		public DataparallelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataparallel; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitDataparallel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataparallelContext dataparallel() throws RecognitionException {
		DataparallelContext _localctx = new DataparallelContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_dataparallel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			map();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SeqContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PatternParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(PatternParser.ASSIGN, 0); }
		public TerminalNode INT() { return getToken(PatternParser.INT, 0); }
		public TerminalNode SCOL() { return getToken(PatternParser.SCOL, 0); }
		public SeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seq; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeqContext seq() throws RecognitionException {
		SeqContext _localctx = new SeqContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_seq);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(ID);
			setState(51);
			match(ASSIGN);
			setState(52);
			match(INT);
			setState(53);
			match(SCOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompContext extends ParserRuleContext {
		public TerminalNode COMP() { return getToken(PatternParser.COMP, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public CompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitComp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompContext comp() throws RecognitionException {
		CompContext _localctx = new CompContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_comp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(COMP);
			setState(56);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FarmContext extends ParserRuleContext {
		public TerminalNode FARM() { return getToken(PatternParser.FARM, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FarmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_farm; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitFarm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FarmContext farm() throws RecognitionException {
		FarmContext _localctx = new FarmContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_farm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(FARM);
			setState(59);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PipeContext extends ParserRuleContext {
		public TerminalNode PIPE() { return getToken(PatternParser.PIPE, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public PipeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipe; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitPipe(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PipeContext pipe() throws RecognitionException {
		PipeContext _localctx = new PipeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_pipe);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			match(PIPE);
			setState(62);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapContext extends ParserRuleContext {
		public TerminalNode MAP() { return getToken(PatternParser.MAP, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitMap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapContext map() throws RecognitionException {
		MapContext _localctx = new MapContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_map);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(MAP);
			setState(65);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode OBRACE() { return getToken(PatternParser.OBRACE, 0); }
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public TerminalNode CBRACE() { return getToken(PatternParser.CBRACE, 0); }
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatternVisitor ) return ((PatternVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			match(OBRACE);
			setState(68);
			pattern();
			setState(69);
			match(CBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\21J\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3$\n\3\3\4\3\4\3"+
		"\4\5\4)\n\4\3\5\3\5\5\5-\n\5\3\6\3\6\5\6\61\n\6\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\2\2E\2\32\3\2\2\2\4#\3"+
		"\2\2\2\6(\3\2\2\2\b,\3\2\2\2\n\60\3\2\2\2\f\62\3\2\2\2\16\64\3\2\2\2\20"+
		"9\3\2\2\2\22<\3\2\2\2\24?\3\2\2\2\26B\3\2\2\2\30E\3\2\2\2\32\33\5\4\3"+
		"\2\33\34\7\2\2\3\34\3\3\2\2\2\35$\5\b\5\2\36$\5\n\6\2\37$\5\f\7\2 $\5"+
		"\6\4\2!\"\7\13\2\2\"$\b\3\1\2#\35\3\2\2\2#\36\3\2\2\2#\37\3\2\2\2# \3"+
		"\2\2\2#!\3\2\2\2$\5\3\2\2\2%)\5\b\5\2&)\5\n\6\2\')\5\f\7\2(%\3\2\2\2("+
		"&\3\2\2\2(\'\3\2\2\2)\7\3\2\2\2*-\5\22\n\2+-\5\24\13\2,*\3\2\2\2,+\3\2"+
		"\2\2-\t\3\2\2\2.\61\5\16\b\2/\61\5\20\t\2\60.\3\2\2\2\60/\3\2\2\2\61\13"+
		"\3\2\2\2\62\63\5\26\f\2\63\r\3\2\2\2\64\65\7\7\2\2\65\66\7\4\2\2\66\67"+
		"\7\b\2\2\678\7\3\2\28\17\3\2\2\29:\7\f\2\2:;\5\30\r\2;\21\3\2\2\2<=\7"+
		"\r\2\2=>\5\30\r\2>\23\3\2\2\2?@\7\16\2\2@A\5\30\r\2A\25\3\2\2\2BC\7\17"+
		"\2\2CD\5\30\r\2D\27\3\2\2\2EF\7\20\2\2FG\5\4\3\2GH\7\21\2\2H\31\3\2\2"+
		"\2\6#(,\60";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
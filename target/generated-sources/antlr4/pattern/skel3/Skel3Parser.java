// Generated from pattern\skel3\Skel3.g4 by ANTLR 4.7
package pattern.skel3;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class Skel3Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, Seq=7, Comp=8, Farm=9, 
		Pipe=10, Map=11, IDENTIFIER=12, NUMBER=13, COMMENT=14, SPACE=15, OTHER=16;
	public static final int
		RULE_skeletonProgram = 0, RULE_programPart = 1, RULE_statement = 2, RULE_mainExpr = 3, 
		RULE_assignment = 4, RULE_patternExpr = 5, RULE_varType = 6, RULE_streamPattern = 7, 
		RULE_sequential = 8, RULE_dataParallelPattern = 9, RULE_main = 10, RULE_block = 11, 
		RULE_sequence = 12, RULE_composition = 13, RULE_pipeSkel = 14, RULE_farmSkel = 15, 
		RULE_mapSkel = 16, RULE_stages = 17;
	public static final String[] ruleNames = {
		"skeletonProgram", "programPart", "statement", "mainExpr", "assignment", 
		"patternExpr", "varType", "streamPattern", "sequential", "dataParallelPattern", 
		"main", "block", "sequence", "composition", "pipeSkel", "farmSkel", "mapSkel", 
		"stages"
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

	@Override
	public String getGrammarFileName() { return "Skel3.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public Skel3Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class SkeletonProgramContext extends ParserRuleContext {
		public Token OTHER;
		public TerminalNode EOF() { return getToken(Skel3Parser.EOF, 0); }
		public List<ProgramPartContext> programPart() {
			return getRuleContexts(ProgramPartContext.class);
		}
		public ProgramPartContext programPart(int i) {
			return getRuleContext(ProgramPartContext.class,i);
		}
		public TerminalNode OTHER() { return getToken(Skel3Parser.OTHER, 0); }
		public SkeletonProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_skeletonProgram; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitSkeletonProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SkeletonProgramContext skeletonProgram() throws RecognitionException {
		SkeletonProgramContext _localctx = new SkeletonProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_skeletonProgram);
		int _la;
		try {
			setState(45);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(37); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(36);
					programPart();
					}
					}
					setState(39); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__2 || _la==IDENTIFIER );
				setState(41);
				match(EOF);
				}
				break;
			case OTHER:
				enterOuterAlt(_localctx, 2);
				{
				setState(43);
				((SkeletonProgramContext)_localctx).OTHER = match(OTHER);
				System.err.println("unknown char: " + (((SkeletonProgramContext)_localctx).OTHER!=null?((SkeletonProgramContext)_localctx).OTHER.getText():null));
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

	public static class ProgramPartContext extends ParserRuleContext {
		public MainExprContext mainExpr() {
			return getRuleContext(MainExprContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ProgramPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitProgramPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramPartContext programPart() throws RecognitionException {
		ProgramPartContext _localctx = new ProgramPartContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_programPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(47);
				statement();
				}
				}
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(53);
			mainExpr();
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

	public static class StatementContext extends ParserRuleContext {
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			assignment();
			setState(56);
			match(T__0);
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

	public static class MainExprContext extends ParserRuleContext {
		public MainContext mainMethod;
		public VarTypeContext type;
		public PatternExprContext expr;
		public MainContext main() {
			return getRuleContext(MainContext.class,0);
		}
		public VarTypeContext varType() {
			return getRuleContext(VarTypeContext.class,0);
		}
		public PatternExprContext patternExpr() {
			return getRuleContext(PatternExprContext.class,0);
		}
		public MainExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitMainExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainExprContext mainExpr() throws RecognitionException {
		MainExprContext _localctx = new MainExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_mainExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			((MainExprContext)_localctx).mainMethod = main();
			setState(59);
			match(T__1);
			setState(60);
			((MainExprContext)_localctx).type = varType();
			setState(61);
			((MainExprContext)_localctx).expr = patternExpr();
			setState(62);
			match(T__0);
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

	public static class AssignmentContext extends ParserRuleContext {
		public Token varName;
		public VarTypeContext type;
		public PatternExprContext expr;
		public TerminalNode IDENTIFIER() { return getToken(Skel3Parser.IDENTIFIER, 0); }
		public VarTypeContext varType() {
			return getRuleContext(VarTypeContext.class,0);
		}
		public PatternExprContext patternExpr() {
			return getRuleContext(PatternExprContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitAssignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			((AssignmentContext)_localctx).varName = match(IDENTIFIER);
			setState(65);
			match(T__1);
			setState(66);
			((AssignmentContext)_localctx).type = varType();
			setState(67);
			((AssignmentContext)_localctx).expr = patternExpr();
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

	public static class PatternExprContext extends ParserRuleContext {
		public StreamPatternContext stream;
		public SequentialContext seq;
		public DataParallelPatternContext dataParallel;
		public VarTypeContext type;
		public PatternExprContext expr;
		public Token varName;
		public StreamPatternContext streamPattern() {
			return getRuleContext(StreamPatternContext.class,0);
		}
		public SequentialContext sequential() {
			return getRuleContext(SequentialContext.class,0);
		}
		public DataParallelPatternContext dataParallelPattern() {
			return getRuleContext(DataParallelPatternContext.class,0);
		}
		public VarTypeContext varType() {
			return getRuleContext(VarTypeContext.class,0);
		}
		public PatternExprContext patternExpr() {
			return getRuleContext(PatternExprContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(Skel3Parser.IDENTIFIER, 0); }
		public PatternExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_patternExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitPatternExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PatternExprContext patternExpr() throws RecognitionException {
		PatternExprContext _localctx = new PatternExprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_patternExpr);
		try {
			setState(76);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(69);
				((PatternExprContext)_localctx).stream = streamPattern();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(70);
				((PatternExprContext)_localctx).seq = sequential();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(71);
				((PatternExprContext)_localctx).dataParallel = dataParallelPattern();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(72);
				((PatternExprContext)_localctx).type = varType();
				setState(73);
				((PatternExprContext)_localctx).expr = patternExpr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(75);
				((PatternExprContext)_localctx).varName = match(IDENTIFIER);
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

	public static class VarTypeContext extends ParserRuleContext {
		public TerminalNode Seq() { return getToken(Skel3Parser.Seq, 0); }
		public TerminalNode Comp() { return getToken(Skel3Parser.Comp, 0); }
		public TerminalNode Farm() { return getToken(Skel3Parser.Farm, 0); }
		public TerminalNode Pipe() { return getToken(Skel3Parser.Pipe, 0); }
		public TerminalNode Map() { return getToken(Skel3Parser.Map, 0); }
		public VarTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitVarType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarTypeContext varType() throws RecognitionException {
		VarTypeContext _localctx = new VarTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_varType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Seq) | (1L << Comp) | (1L << Farm) | (1L << Pipe) | (1L << Map))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static class StreamPatternContext extends ParserRuleContext {
		public FarmSkelContext farm;
		public PipeSkelContext pipe;
		public FarmSkelContext farmSkel() {
			return getRuleContext(FarmSkelContext.class,0);
		}
		public PipeSkelContext pipeSkel() {
			return getRuleContext(PipeSkelContext.class,0);
		}
		public StreamPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_streamPattern; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitStreamPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StreamPatternContext streamPattern() throws RecognitionException {
		StreamPatternContext _localctx = new StreamPatternContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_streamPattern);
		try {
			setState(82);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(80);
				((StreamPatternContext)_localctx).farm = farmSkel();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(81);
				((StreamPatternContext)_localctx).pipe = pipeSkel();
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

	public static class SequentialContext extends ParserRuleContext {
		public SequenceContext sec;
		public CompositionContext comp;
		public SequenceContext sequence() {
			return getRuleContext(SequenceContext.class,0);
		}
		public CompositionContext composition() {
			return getRuleContext(CompositionContext.class,0);
		}
		public SequentialContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequential; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitSequential(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequentialContext sequential() throws RecognitionException {
		SequentialContext _localctx = new SequentialContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_sequential);
		try {
			setState(86);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				((SequentialContext)_localctx).sec = sequence();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				((SequentialContext)_localctx).comp = composition();
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

	public static class DataParallelPatternContext extends ParserRuleContext {
		public MapSkelContext map;
		public MapSkelContext mapSkel() {
			return getRuleContext(MapSkelContext.class,0);
		}
		public DataParallelPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataParallelPattern; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitDataParallelPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataParallelPatternContext dataParallelPattern() throws RecognitionException {
		DataParallelPatternContext _localctx = new DataParallelPatternContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_dataParallelPattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			((DataParallelPatternContext)_localctx).map = mapSkel();
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
		public MainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_main; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitMain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainContext main() throws RecognitionException {
		MainContext _localctx = new MainContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_main);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(T__2);
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
		public PatternExprContext expr;
		public PatternExprContext patternExpr() {
			return getRuleContext(PatternExprContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(T__3);
			setState(93);
			((BlockContext)_localctx).expr = patternExpr();
			setState(94);
			match(T__4);
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
		public Token ts;
		public TerminalNode NUMBER() { return getToken(Skel3Parser.NUMBER, 0); }
		public SequenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequence; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitSequence(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceContext sequence() throws RecognitionException {
		SequenceContext _localctx = new SequenceContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_sequence);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(T__3);
			setState(97);
			((SequenceContext)_localctx).ts = match(NUMBER);
			setState(98);
			match(T__4);
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

	public static class CompositionContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public CompositionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_composition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitComposition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompositionContext composition() throws RecognitionException {
		CompositionContext _localctx = new CompositionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_composition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
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

	public static class PipeSkelContext extends ParserRuleContext {
		public StagesContext stages() {
			return getRuleContext(StagesContext.class,0);
		}
		public PipeSkelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipeSkel; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitPipeSkel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PipeSkelContext pipeSkel() throws RecognitionException {
		PipeSkelContext _localctx = new PipeSkelContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_pipeSkel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			stages();
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

	public static class FarmSkelContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FarmSkelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_farmSkel; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitFarmSkel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FarmSkelContext farmSkel() throws RecognitionException {
		FarmSkelContext _localctx = new FarmSkelContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_farmSkel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
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

	public static class MapSkelContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MapSkelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapSkel; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitMapSkel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapSkelContext mapSkel() throws RecognitionException {
		MapSkelContext _localctx = new MapSkelContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_mapSkel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
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

	public static class StagesContext extends ParserRuleContext {
		public PatternExprContext patternExpr;
		public List<PatternExprContext> expr = new ArrayList<PatternExprContext>();
		public List<PatternExprContext> patternExpr() {
			return getRuleContexts(PatternExprContext.class);
		}
		public PatternExprContext patternExpr(int i) {
			return getRuleContext(PatternExprContext.class,i);
		}
		public StagesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stages; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof Skel3Visitor ) return ((Skel3Visitor<? extends T>)visitor).visitStages(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StagesContext stages() throws RecognitionException {
		StagesContext _localctx = new StagesContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_stages);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(T__3);
			setState(109);
			((StagesContext)_localctx).patternExpr = patternExpr();
			((StagesContext)_localctx).expr.add(((StagesContext)_localctx).patternExpr);
			setState(110);
			match(T__5);
			setState(111);
			((StagesContext)_localctx).patternExpr = patternExpr();
			((StagesContext)_localctx).expr.add(((StagesContext)_localctx).patternExpr);
			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(112);
				match(T__5);
				setState(113);
				((StagesContext)_localctx).patternExpr = patternExpr();
				((StagesContext)_localctx).expr.add(((StagesContext)_localctx).patternExpr);
				}
				}
				setState(118);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(119);
			match(T__4);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\22|\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\4\23"+
		"\t\23\3\2\6\2(\n\2\r\2\16\2)\3\2\3\2\3\2\3\2\5\2\60\n\2\3\3\7\3\63\n\3"+
		"\f\3\16\3\66\13\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7O\n\7\3\b\3\b\3\t\3\t\5\t"+
		"U\n\t\3\n\3\n\5\nY\n\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\7\23u\n\23\f\23\16\23x\13\23\3\23\3\23\3\23\2\2\24\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$\2\3\3\2\t\r\2s\2/\3\2\2\2\4\64\3\2\2\2\6"+
		"9\3\2\2\2\b<\3\2\2\2\nB\3\2\2\2\fN\3\2\2\2\16P\3\2\2\2\20T\3\2\2\2\22"+
		"X\3\2\2\2\24Z\3\2\2\2\26\\\3\2\2\2\30^\3\2\2\2\32b\3\2\2\2\34f\3\2\2\2"+
		"\36h\3\2\2\2 j\3\2\2\2\"l\3\2\2\2$n\3\2\2\2&(\5\4\3\2\'&\3\2\2\2()\3\2"+
		"\2\2)\'\3\2\2\2)*\3\2\2\2*+\3\2\2\2+,\7\2\2\3,\60\3\2\2\2-.\7\22\2\2."+
		"\60\b\2\1\2/\'\3\2\2\2/-\3\2\2\2\60\3\3\2\2\2\61\63\5\6\4\2\62\61\3\2"+
		"\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\67\3\2\2\2\66\64\3\2"+
		"\2\2\678\5\b\5\28\5\3\2\2\29:\5\n\6\2:;\7\3\2\2;\7\3\2\2\2<=\5\26\f\2"+
		"=>\7\4\2\2>?\5\16\b\2?@\5\f\7\2@A\7\3\2\2A\t\3\2\2\2BC\7\16\2\2CD\7\4"+
		"\2\2DE\5\16\b\2EF\5\f\7\2F\13\3\2\2\2GO\5\20\t\2HO\5\22\n\2IO\5\24\13"+
		"\2JK\5\16\b\2KL\5\f\7\2LO\3\2\2\2MO\7\16\2\2NG\3\2\2\2NH\3\2\2\2NI\3\2"+
		"\2\2NJ\3\2\2\2NM\3\2\2\2O\r\3\2\2\2PQ\t\2\2\2Q\17\3\2\2\2RU\5 \21\2SU"+
		"\5\36\20\2TR\3\2\2\2TS\3\2\2\2U\21\3\2\2\2VY\5\32\16\2WY\5\34\17\2XV\3"+
		"\2\2\2XW\3\2\2\2Y\23\3\2\2\2Z[\5\"\22\2[\25\3\2\2\2\\]\7\5\2\2]\27\3\2"+
		"\2\2^_\7\6\2\2_`\5\f\7\2`a\7\7\2\2a\31\3\2\2\2bc\7\6\2\2cd\7\17\2\2de"+
		"\7\7\2\2e\33\3\2\2\2fg\5\30\r\2g\35\3\2\2\2hi\5$\23\2i\37\3\2\2\2jk\5"+
		"\30\r\2k!\3\2\2\2lm\5\30\r\2m#\3\2\2\2no\7\6\2\2op\5\f\7\2pq\7\b\2\2q"+
		"v\5\f\7\2rs\7\b\2\2su\5\f\7\2tr\3\2\2\2ux\3\2\2\2vt\3\2\2\2vw\3\2\2\2"+
		"wy\3\2\2\2xv\3\2\2\2yz\7\7\2\2z%\3\2\2\2\t)/\64NTXv";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
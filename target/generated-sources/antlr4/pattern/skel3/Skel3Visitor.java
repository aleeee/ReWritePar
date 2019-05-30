// Generated from pattern\skel3\Skel3.g4 by ANTLR 4.7
package pattern.skel3;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Skel3Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Skel3Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#skeletonProgram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkeletonProgram(Skel3Parser.SkeletonProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#programPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramPart(Skel3Parser.ProgramPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(Skel3Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#mainExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainExpr(Skel3Parser.MainExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(Skel3Parser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#patternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternExpr(Skel3Parser.PatternExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarType(Skel3Parser.VarTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#streamPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStreamPattern(Skel3Parser.StreamPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#sequential}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequential(Skel3Parser.SequentialContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#dataParallelPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataParallelPattern(Skel3Parser.DataParallelPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(Skel3Parser.MainContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(Skel3Parser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence(Skel3Parser.SequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#composition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComposition(Skel3Parser.CompositionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#pipeSkel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipeSkel(Skel3Parser.PipeSkelContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#farmSkel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFarmSkel(Skel3Parser.FarmSkelContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#mapSkel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapSkel(Skel3Parser.MapSkelContext ctx);
	/**
	 * Visit a parse tree produced by {@link Skel3Parser#stages}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStages(Skel3Parser.StagesContext ctx);
}
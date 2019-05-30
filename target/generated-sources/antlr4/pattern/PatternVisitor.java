// Generated from pattern\Pattern.g4 by ANTLR 4.7
package pattern;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PatternParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PatternVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PatternParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(PatternParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPattern(PatternParser.PatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(PatternParser.MainContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#stream}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream(PatternParser.StreamContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence(PatternParser.SequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#dataparallel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataparallel(PatternParser.DataparallelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq(PatternParser.SeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp(PatternParser.CompContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#farm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFarm(PatternParser.FarmContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#pipe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipe(PatternParser.PipeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap(PatternParser.MapContext ctx);
	/**
	 * Visit a parse tree produced by {@link PatternParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(PatternParser.BlockContext ctx);
}
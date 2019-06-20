package skel3;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import pattern.skel3.Skel3Lexer;
import pattern.skel3.Skel3Parser;
import rewriter.Refactorer;
import tree.model.SkeletonPatt;
import visitor.NodesVisitor;
import visitor.Visitor6;
public class Main {
	public static  <T> void main(String[] args) throws Exception {

        if (args.length == 0) {
            args = new String[]{"src/main/pattern/pattern2.skel"};
        }

        System.out.println("parsing: " + args[0]);
        Path path = Paths.get(args[0]);
        Skel3Lexer lexer = new Skel3Lexer(CharStreams.fromPath(path));
        Skel3Parser parser = new Skel3Parser(new CommonTokenStream(lexer));
        ParseTree tree = parser.skeletonProgram();
        Visitor6 visitor3 = new Visitor6();
        SkeletonPatt n =visitor3.visit(tree);
//        System.out.println("n "+ n);
//        printTree(n);
//		display(n);
        NodesVisitor v = new NodesVisitor();
//        v.visit( n);
        n.accept(v);
//        System.out.println("after " + n);
        Refactorer reWriter = new Refactorer();
        n.refactor(reWriter);
//        System.out.println("refactoring options" + n.getPatterns());
//        n.getPatterns().forEach(p -> System.out.println(p));
        System.out.println("nodes");
        n.getChildren().forEach(p-> System.out.println(p.getPatterns()));
//       n.getChildren().stream().forEach(sk -> System.out.println(sk.getPatterns()));
//        System.out.println(getMainNode(n));
//        System.out.println(Util.computeServiceTime(getMainNode(n),0));
    }
	
	
/**
 * compute serviceTime
  if has child
    ts =  child.compute   
    if(farm)
      ts/n
    if(map)

  else if has children

    ts += children.foreach.compute
    if(pipe)
      max(children.compute(max of ts))
    if(comp)
    ts = ts
  else 
    compute
 */
	
	
}

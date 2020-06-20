package rewriter;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;

public interface ReWriter {
	public void reWrite(SeqPatt s);
	public void reWrite(CompPatt s);
	public void reWrite(FarmPatt s);
	public void reWrite(PipePatt s);
	public void reWrite(MapPatt s);
}

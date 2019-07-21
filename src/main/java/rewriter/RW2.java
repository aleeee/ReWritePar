package rewriter;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import util.Ref2;

public class RW2 implements ReWriter {
	
	@Override
	public void reWrite(SeqPatt seq) {
		seq = Ref2.refactor(seq);
	}

	@Override
	public void reWrite(CompPatt comp) {
		comp = Ref2.refactor(comp);
	}

	@Override
	public void reWrite(FarmPatt farm) {
        farm = Ref2.refactor(farm);
	}

	@Override
	public void reWrite(PipePatt pipe) {
		pipe = Ref2.refactor(pipe);

	}

	@Override
	public void reWrite(MapPatt map) {
		map = Ref2.refactor(map);
	}

}

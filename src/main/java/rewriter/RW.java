package rewriter;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import util.Ref;

public class RW implements ReWriter {
	
	@Override
	public void reWrite(SeqPatt seq) {
		seq = Ref.refactor(seq);
	}

	@Override
	public void reWrite(CompPatt comp) {
		comp = Ref.refactor(comp);
	}

	@Override
	public void reWrite(FarmPatt farm) {
        farm = Ref.refactor(farm);
	}

	@Override
	public void reWrite(PipePatt pipe) {
		pipe = Ref.refactor(pipe);

	}

	@Override
	public void reWrite(MapPatt map) {
		map = Ref.refactor(map);
	}

}

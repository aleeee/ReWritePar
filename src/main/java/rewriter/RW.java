package rewriter;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import util.ReWrite;

public class RW implements ReWriter {
	
	@Override
	public void reWrite(SeqPatt seq) {
		seq = ReWrite.refactor(seq);
	}

	@Override
	public void reWrite(CompPatt comp) {
		comp = ReWrite.refactor(comp);
	}

	@Override
	public void reWrite(FarmPatt farm) {
        farm = ReWrite.refactor(farm);
	}

	@Override
	public void reWrite(PipePatt pipe) {
		pipe = ReWrite.refactor(pipe);

	}

	@Override
	public void reWrite(MapPatt map) {
		map = ReWrite.refactor(map);
	}

}

package rewriter;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import util.ReWrite;

public class RW implements ReWriter {
	private ReWrite reWritor;
	 public RW() {
		this.reWritor = new ReWrite();
	}
	@Override
	public void reWrite(SeqPatt seq) {
		seq = reWritor.refactor(seq);
	}

	@Override
	public void reWrite(CompPatt comp) {
		comp = reWritor.refactor(comp,this);
	}

	@Override
	public void reWrite(FarmPatt farm) {
        farm = reWritor.refactor(farm,this);
	}

	@Override
	public void reWrite(PipePatt pipe) {
		pipe = reWritor.refactor(pipe,this);

	}

	@Override
	public void reWrite(MapPatt map) {
		map = reWritor.refactor(map,this);
	}

}

package tree.model;

public class SkelType {
	
	final Object skelType;
	
	public SkelType(Object skelType) {
		this.skelType = skelType;
	}
	
	public SeqPatt asSeq() {
		return (SeqPatt)skelType;
	}
	public CompPatt asComp() {
		return (CompPatt)skelType;
	}
	public FarmPatt asFarm() {
		return (FarmPatt)skelType;
	}
	public PipePatt asPipe() {
		return (PipePatt)skelType;
	}
	public MapPatt asMapSkel() {
		return (MapPatt)skelType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skelType == null) ? 0 : skelType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkelType other = (SkelType) obj;
		if (skelType == null) {
			if (other.skelType != null)
				return false;
		} else if (!skelType.equals(other.skelType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SkelType [skelType=" + skelType + "]";
	}
	
	
}

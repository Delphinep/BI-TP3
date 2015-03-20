package clustering;

public class DistanceManhattan implements Distance {

	@Override
	public double valeur(Donnee d1, Donnee d2) {
		double val = 0;
		for(int i=0;i<d1.nbDimensions();i++){
			val += Math.abs(d1.valeurDim(i)-d2.valeurDim(i));
		}
		double res = Math.sqrt(val);
		return res;
	}

}

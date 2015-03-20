package clustering;

public class DistanceChebyshev implements Distance {

	@Override
	public double valeur(Donnee d1, Donnee d2) {
		double valMax = 0;
		double valTmp = 0;
		for(int i=0;i<d1.nbDimensions();i++){
			valTmp = Math.abs(d1.valeurDim(i)-d2.valeurDim(i));
			if (valTmp > valMax)
				valMax = valTmp;
		}
		return valMax;
	}

}

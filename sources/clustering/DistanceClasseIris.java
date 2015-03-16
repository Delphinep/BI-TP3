package clustering;

public class DistanceClasseIris implements Distance {

	@Override
	public double valeur(Donnee d1, Donnee d2) {
		if(d1.valeurDim(3)==d2.valeurDim(3))
			return 0;
		return 1;
	}

}

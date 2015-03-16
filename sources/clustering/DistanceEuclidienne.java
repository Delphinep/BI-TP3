package clustering;

public class DistanceEuclidienne implements Distance {

	/**
     * renvoie la distance entre les deux données ayant le même nombre de dimensions.
     */
	@Override
	public double valeur(Donnee d1, Donnee d2) {
		double val = 0;
		for(int i=0;i<d1.nbDimensions();i++){
			val += Math.pow((d1.valeurDim(i)-d2.valeurDim(i)),2);
		}
		double res = Math.sqrt(val);
		return res;
	}
	

}

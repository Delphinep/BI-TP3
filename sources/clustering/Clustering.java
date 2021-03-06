package clustering ;
import java.util.Random ;

/**
 * L'algorithme de clustering, méthode des k-means.
 * On applique l'algorithme sur des données placées initialement dans 1 cluster, et que l'on va répartir dans k clusters.
 * Il est possible de partir de k centres donnés, ou bien on va les choisir aléatoirement.
 * Il est aussi possible de choisir la distance, par défaut c'est la distance euclidienne sans normalisation.
 *
 * @author   Anne-Cécile Caron
 */
public class Clustering{
    private int k ; // nb de clusters
    private Cluster lesDonnees ; // les données sur lesquelles on applique le clustering
    private Cluster[] lesClusters ; // tableau de k Clusters
    private Donnee[] lesCentres ; // tableau des k centres des clusters
    private Random hasard = new Random() ; // va servir à initialiser les centres
    private Distance distance ; // permet de choisir la façon de calculer la distance entre 2 données

    // initialisation de l'ensemble des données, et des k clusters (ils sont vides au départ).
    private void init(int k, Cluster data){
        this.k = k;
        this.lesDonnees = data ;
        // on crée un tableau de taille k pour les clusters
        this.lesClusters = new Cluster[k];
        for (int i=0; i<k; i++){ // on crée k Clusters
            this.lesClusters[i] = new Cluster(data.nbDimensions());
        }
    }
  
    /** Constructeur
     * @param k le nombre de clusters
     * @param data toutes les données que l'on va répartir dans k clusters
     */
    public Clustering(int k, Cluster data){
        this.init(k,data) ;
        // par defaut, la distance est euclidienne
        this.distance = new DistanceEuclidienne() ;
        // on crée un tableau de taille k pour les centres des clusters
        this.lesCentres = new Donnee[k];
        this.choisirCentres() ;
    }

    /** Constructeur
     * @param k le nombre de clusters
     * @param data toutes les données que l'on va répartir dans k clusters
     * @param d la distance utilisée, si on veut autre chose que la distance euclidienne non normalisée
     */
    public Clustering(int k, Cluster data, Distance d){
        this.init(k,data) ;
        this.distance = d ;
        // on crée un tableau de taille k pour les centres des clusters
        this.lesCentres = new Donnee[k];
        this.choisirCentres() ;
    }
  
    /** Constructeur
     * @param k le nombre de clusters
     * @param data toutes les données que l'on va répartir dans k clusters
     * @param centres un tableau avec les k centres initiaux des k clusters.
     */
    public Clustering(int k, Cluster data, Donnee[] centres) throws ClusterException{
        this.init(k,data) ;
        // par defaut, la distance est euclidienne
        this.distance = new DistanceEuclidienne() ;
        // on initialise les centres des clusters
        this.lesCentres = centres ;
    }

    /** Constructeur
     * @param k le nombre de clusters
     * @param data toutes les données que l'on va répartir dans k clusters
     * @param centres un tableau avec les k centres initiaux des k clusters.
     * @param d la distance utilisée, si on veut autre chose que la distance euclidienne non normalisée
     */
    public Clustering(int k, Cluster data, Donnee[] centres, Distance d) throws ClusterException{
        this.init(k,data) ;
        this.distance = d ;
        // on initialise les centres des clusters
        this.lesCentres = centres ;
    }
  
    // on choisit (pseudo-)aléatoirement k centres pour commencer l'algo.
    private void choisirCentres(){
        for(int i=0;i<k;i++){
        	this.lesCentres[i] = this.lesDonnees.get(this.hasard.nextInt(this.lesDonnees.size()));
        }
    }
  
    // on change les centres en prenant les barycentres des clusters.
    // à faire après chaque étape
    private void nouveauxCentres(){
        for(int i=0;i<this.k;i++){
           	for(int j=0;j<this.lesClusters[i].size();j++){
        		this.lesCentres[i] = this.lesClusters[i].moyenne();
        	}
        }
    }
  
    // une étape : on calcule la distance de chaque donnée par rapport aux centres des clusters
    // et on place chaque donnée dans le cluster dont le centre est le plus proche
    private boolean etape() throws ClusterException{
        boolean change = false ;
        
        for(int i=0;i<this.lesDonnees.size();i++){
        	Donnee data = this.lesDonnees.get(i);
        	double minDist = this.distance.valeur(data, this.lesCentres[0]);
        	int iMin = 0;
        	for(int j=0;j<this.k;j++){
        		data.evalueChangementCluster(this.lesCentres[j], j, lesClusters, this.distance);
        		}
        	if(change == false && data.aChangeDeCluster()){
        		change = true;
        	}
        	}
        	return change; // renvoie true ssi au moins une donnee a change de cluster
    }

    /**
     * renvoie la compacité des clusters, c'est à dire la somme des compacités de tous les clusters (WC vient de "within clusters").
     * La compacité d'un cluster est la somme des distances des données du cluster par rapport à son centre.
     * @return la compacité des k clusters
     * @throws ClusterException 
     */
    public double wc() throws ClusterException {
        double som = 0.0 ;
        for(int i=0;i<this.lesClusters.length;i++){
        	som+=this.lesClusters[i].wc();
        }
        return som ;
    }

    /**
     * renvoie la séparation, c'est à dire la somme des distances entre les centres des clusters (BC vient de "between clusters").
     * @return la séparation des clusters
     */
    public double bc(){
        double som = 0.0 ;
        for(int i=0;i<this.lesCentres.length;i++){
        	for(int j=i+1;j<this.lesCentres.length;j++){
        	som += this.distance.valeur(this.lesCentres[i], this.lesCentres[j]);
        	}
        }
        return som ;
    }
  
    /**
     * l'algorithme de clustering sur les données que l'on a passées au constructeur.
     * On applique l'algo des k-means
     * @param trace boolean qui permet de demander (ou pas) d'avoir une trace des étapes de l'algorithme. A eviter s'il y a beaucoup de données !
     * @return le tableau des k Clusters résultat de l'application de l'algorithme.
     * @throws ClusterException 
     */
    public Cluster[] algo(boolean trace) throws ClusterException{
        boolean change = true ;
        if (trace) {
            System.out.println("données avant le clustering : "); 
            this.affichage() ;
            System.out.println("Application du clustering : "); 
        }
        this.choisirCentres();
        change = this.etape();
        while (change) {
        	this.nouveauxCentres();
        	change = this.etape();

        }
        return this.lesClusters ;
    }

    // affiche toutes les données avec leur numéro de cluster et la distance par rapport au centre
    // donne aussi un résumé des mesures de qualité : WC et BC
    private void affichage() throws ClusterException{
        System.out.println("--------------------");
        for (Donnee d : this.lesDonnees) {
            System.out.println(d.toComplexString());
        }
        double wc = this.wc();
        double bc = this.bc();
        double rapport ;
        try{ rapport = bc/wc ;}
        catch(java.lang.ArithmeticException e){ rapport = 0.0 ; }
        System.out.println("WC = "+wc+" BC = "+bc+" Rapport BC/WC = "+rapport);
        System.out.println("--------------------");
    }

}

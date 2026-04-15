import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Creation de la bibliotheque
        Bibliotheque bibliotheque = new Bibliotheque("Bibliotheque municipale");

        // Ajout des livres
        bibliotheque.ajouterLivre(new Livre("L1", "Le Petit Prince", "Saint-Exupery", 3, "Centre"));
        bibliotheque.ajouterLivre(new Livre("L2", "L'Etranger", "Albert Camus", 2, "Nord"));

        // Ajout des membres
        bibliotheque.ajouterMembre(new Membre("M1", "Fatim"));
        bibliotheque.ajouterMembre(new Membre("M2", "Demba"));

        System.out.println("---- ETAT INITIAL ----");
        bibliotheque.afficherEtat();

        // Emprunt d'un livre
        bibliotheque.emprunterLivre("L1", "M1");
        System.out.println("\n---- APRES EMPRUNT ----");
        bibliotheque.afficherEtat();

        // Retour du livre avec 2 jours de retard
        bibliotheque.retournerLivre("L1", "M1", 2);
        System.out.println("\n--- APRES RETOUR ---");
        bibliotheque.afficherEtat();

        // Paiement de l'amende
        bibliotheque.payerAmende("M1", 4);
        System.out.println("\n--- APRES PAIEMENT ---");
        bibliotheque.afficherEtat();

        // Transfert d'un livre vers une autre annexe
        bibliotheque.transfererLivre("L2", "Sud");
        System.out.println("\n--- APRES TRANSFERT ---");
        bibliotheque.afficherEtat();

        // Verification simple de la conservation
        bibliotheque.verifierConservation("L2", 32);
        System.out.println("\n--- APRES VERIFICATION CONSERVATION ---");
        bibliotheque.afficherEtat();
    }
}

// Classe Bibliotheque qui gere les livres, les membres, les prets et la caisse
class Bibliotheque {
    private String nom;
    private ArrayList<Livre> livres;
    private ArrayList<Membre> membres;
    private ArrayList<Pret> prets;
    private double caisse;

    public Bibliotheque(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de la bibliotheque est obligatoire!");
        }
        this.nom = nom;
        this.livres = new ArrayList<>();
        this.membres = new ArrayList<>();
        this.prets = new ArrayList<>();
        this.caisse = 0;
    }

    public void ajouterLivre(Livre livre) {
        if (livre == null) {
            throw new IllegalArgumentException("Le livre est obligatoire!");
        }
        livres.add(livre);
    }

    public void ajouterMembre(Membre membre) {
        if (membre == null) {
            throw new IllegalArgumentException("Le membre est obligatoire!");
        }
        membres.add(membre);
    }

    public Livre chercherLivre(String code) {
        for (Livre livre : livres) {
            if (livre.getCode().equals(code)) {
                return livre;
            }
        }
        return null;
    }

    public Membre chercherMembre(String id) {
        for (Membre membre : membres) {
            if (membre.getId().equals(id)) {
                return membre;
            }
        }
        return null;
    }

    public void emprunterLivre(String codeLivre, String idMembre) {
        Livre livre = chercherLivre(codeLivre);
        Membre membre = chercherMembre(idMembre);

        if (livre != null && membre != null && livre.getStock() > 0) {
            livre.setStock(livre.getStock() - 1);
            membre.setNombreLivres(membre.getNombreLivres() + 1);
            prets.add(new Pret(livre, membre));
        } else {
            System.out.println("Emprunt impossible.");
        }
    }

    public void retournerLivre(String codeLivre, String idMembre, int retard) {
        if (retard < 0) {
            throw new IllegalArgumentException("Le retard ne peut pas etre negatif!");
        }
        for (Pret pret : prets) {
            if (!pret.isTermine()
                    && pret.getLivre().getCode().equals(codeLivre)
                    && pret.getMembre().getId().equals(idMembre)) {

                pret.setTermine(true);
                pret.setRetard(retard);

                Livre livre = pret.getLivre();
                Membre membre = pret.getMembre();

                livre.setStock(livre.getStock() + 1);
                membre.setNombreLivres(membre.getNombreLivres() - 1);

                double amende = retard * 2;
                membre.setAmende(membre.getAmende() + amende);
                return;
            }
        }
        System.out.println("Retour impossible.");
    }

    public void payerAmende(String idMembre, double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit etre positif!");
        }
        Membre membre = chercherMembre(idMembre);
        if (membre != null && membre.getAmende() >= montant) {
            membre.setAmende(membre.getAmende() - montant);
            caisse = caisse + montant;
        } else {
            System.out.println("Paiement impossible.");
        }
    }

    public void transfererLivre(String codeLivre, String nouvelleAnnexe) {
        if (nouvelleAnnexe == null || nouvelleAnnexe.isBlank()) {
            throw new IllegalArgumentException("La nouvelle annexe est obligatoire!");
        }
        Livre livre = chercherLivre(codeLivre);
        if (livre != null) {
            livre.setAnnexe(nouvelleAnnexe);
        } else {
            System.out.println("Transfert impossible.");
        }
    }

    public void verifierConservation(String codeLivre, double temperature) {
        Livre livre = chercherLivre(codeLivre);
        if (livre != null && temperature > 30) {
            livre.setEtatConservation("a surveiller.");
        } else if (livre == null) {
            System.out.println("Verification impossible");
        }
    }

    public void afficherEtat() {
        System.out.println("Nom de la bibliotheque : " + nom);
        System.out.println("Caisse : " + caisse + " euros");

        System.out.println("\nLivres :");
        for (Livre livre : livres) {
            System.out.println(livre);
        }

        System.out.println("\nMembres :");
        for (Membre membre : membres) {
            System.out.println(membre);
        }

        System.out.println("\nPrets :");
        for (Pret pret : prets) {
            System.out.println(pret);
        }
    }
}

// Classe Livre
class Livre {
    private String code;
    private String titre;
    private String auteur;
    private int stock;
    private String annexe;
    private String etatConservation;

    public Livre(String code, String titre, String auteur, int stock, String annexe) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Le code du livre est obligatoire!");
        }
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre du livre est obligatoire!");
        }
        if (auteur == null || auteur.isBlank()) {
            throw new IllegalArgumentException("L'auteur du livre est obligatoire!");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas etre negatif!");
        }
        if (annexe == null || annexe.isBlank()) {
            throw new IllegalArgumentException("L'annexe est obligatoire!");
        }
        this.code = code;
        this.titre = titre;
        this.auteur = auteur;
        this.stock = stock;
        this.annexe = annexe;
        this.etatConservation = "correct";
    }

    public String getCode() {
        return code;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas etre negatif!");
        }
        this.stock = stock;
    }

    public void setAnnexe(String annexe) {
        if (annexe == null || annexe.isBlank()) {
            throw new IllegalArgumentException("L'annexe est obligatoire!");
        }
        this.annexe = annexe;
    }

    public void setEtatConservation(String etatConservation) {
        if (etatConservation == null || etatConservation.isBlank()) {
            throw new IllegalArgumentException("L'etat de conservation est obligatoire!");
        }
        this.etatConservation = etatConservation;
    }

    public String toString() {
        return "Livre{code='" + code + "', titre='" + titre + "', auteur='" + auteur
                + "', stock=" + stock + ", annexe='" + annexe
                + "', conservation='" + etatConservation + "'}";
    }
}

// Classe Membre
class Membre {
    private String id;
    private String nom;
    private int nombreLivres;
    private double amende;

    public Membre(String id, String nom) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'id du membre est obligatoire!");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du membre est obligatoire!");
        }
        this.id = id;
        this.nom = nom;
        this.nombreLivres = 0;
        this.amende = 0;
    }

    public String getId() {
        return id;
    }

    public int getNombreLivres() {
        return nombreLivres;
    }

    public void setNombreLivres(int nombreLivres) {
        if (nombreLivres < 0) {
            throw new IllegalArgumentException("Le nombre de livres ne peut pas etre negatif!");
        }
        this.nombreLivres = nombreLivres;
    }

    public double getAmende() {
        return amende;
    }

    public void setAmende(double amende) {
        if (amende < 0) {
            throw new IllegalArgumentException("L'amende ne peut pas etre negative!");
        }
        this.amende = amende;
    }

    public String toString() {
        return "Membre{id='" + id + "', nom='" + nom + "', livresEmpruntes="
                + nombreLivres + ", amende=" + amende + "}";
    }
}

// Classe Pret qui represente un emprunt d'un livre par un membre
class Pret {
    private Livre livre;
    private Membre membre;
    private boolean termine;
    private int retard;

    public Pret(Livre livre, Membre membre) {
        if (livre == null || membre == null) {
            throw new IllegalArgumentException("Le livre et le membre sont obligatoires!");
        }
        this.livre = livre;
        this.membre = membre;
        this.termine = false;
        this.retard = 0;
    }

    public Livre getLivre() {
        return livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public boolean isTermine() {
        return termine;
    }

    public void setTermine(boolean termine) {
        this.termine = termine;
    }

    public void setRetard(int retard) {
        if (retard < 0) {
            throw new IllegalArgumentException("Le retard ne peut pas etre negatif!");
        }
        this.retard = retard;
    }

    public String toString() {
        return "Pret{livre='" + livre.getCode() + "', membre='" + membre.getId()
                + "', termine=" + termine + ", retard=" + retard + "}";
    }
}

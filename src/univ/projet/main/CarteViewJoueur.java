package univ.projet.main;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.util.Duration;
import univ.projet.ressource_cartes.ImageCarte;


/**
 * Classe contenant l'imaView pour afficher un carte du joueur et va contenir les évènement associer
 * @author Wangon Romain "NekoRomain"
 */

public class CarteViewJoueur
{

    private ImageView carteView;
    private Carte carte;
    private DemiTerrain demiTerrainOwner;
    static final DataFormat CARTE_FORMAT = new DataFormat("univ.projet.main.Carte");

    public CarteViewJoueur(Group root, DemiTerrain demiTerrain, Carte carte, int x, int y, boolean adversaire)
    {
        this.carte = carte;
        if(!adversaire)
            carteView = new ImageView(carte.getImage());
        else
            carteView = new ImageView(ImageCarte.CARTE_DOS.getImage());
        carteView.setX(0);
        carteView.setY(0);
        this.demiTerrainOwner = demiTerrain;
        initialisation(root, adversaire);

        //animation de tirage de la carte
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(carteView.xProperty(), demiTerrain.getDeckView().getCarteDeckView().getX())),
                new KeyFrame(Duration.ZERO, new KeyValue(carteView.yProperty(), demiTerrain.getDeckView().getCarteDeckView().getY())),
                new KeyFrame(new Duration(500), new KeyValue(carteView.xProperty(), x)),
                new KeyFrame(new Duration(500), new KeyValue(carteView.yProperty(), y))
        );
        timeline.play();


    }

    public ImageView getCarteView()
    {
        return carteView;
    }
    
    private void initialisation(Group root, boolean adversaire)
    {
        root.getChildren().add(carteView);

        if(!adversaire)
        {
            if(carte.type == "avant") //si une carte de type avant
            {
                //Un drag'nDrop est détecté
                carteView.setOnDragDetected(mouseEvent->
                {
                    carteView.setVisible(false);
                    System.err.println("DnD detected");
                    final Dragboard dragBoard = carteView.startDragAndDrop(TransferMode.MOVE);
                    final ClipboardContent content = new ClipboardContent();
                    content.putImage(carte.getImage());
                    content.putString(carte.getType());
                    content.put(CARTE_FORMAT, carte);
                    dragBoard.setContent(content);
                    mouseEvent.consume();
                });

                //Un drag'nDrop est fini avec succès
                carteView.setOnDragDone(event ->
                {
                    if (event.getTransferMode() == TransferMode.MOVE)
                    {
                        this.carteView.setVisible(false);
                        this.carteView = null;
                        int ind = demiTerrainOwner.getMainView().indexOf(this);
                        demiTerrainOwner.getOwner().getMain_joueur().remove(carte);
                        demiTerrainOwner.getMainView().remove(this);
                        demiTerrainOwner.miseAJourMainSelonIndice(ind, false);
                        root.getChildren().remove(carteView);
                    }
                    else
                        carteView.setVisible(true);
                    event.consume();
                });

                carteView.setOnMouseReleased(event ->
                {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        //TODO gagner mana


                        Timeline timeline = new Timeline();
                        timeline.getKeyFrames().addAll(
                                new KeyFrame(Duration.ZERO, new KeyValue(carteView.xProperty(), carteView.getX())),
                                new KeyFrame(Duration.ZERO, new KeyValue(carteView.yProperty(), carteView.getY())),
                                new KeyFrame(new Duration(200), new KeyValue(carteView.xProperty(), 793)),
                                new KeyFrame(new Duration(200), new KeyValue(carteView.yProperty(), 407))
                        );
                        timeline.play();
                        timeline.setOnFinished(eventTime ->
                        {
                            this.carteView.setVisible(false);
                            this.carteView = null;
                            int ind = demiTerrainOwner.getMainView().indexOf(this);
                            demiTerrainOwner.getOwner().getMain_joueur().remove(carte);
                            demiTerrainOwner.getOwner().getBreak_zone().add(carte);
                            carte = null;
                            demiTerrainOwner.getMainView().remove(this);
                            demiTerrainOwner.miseAJourMainSelonIndice(ind, false);
                            root.getChildren().remove(carteView);
                        });
                    }

                });
            }
            else //si une carte de type sort, on lui donne d'autre évènement possible
            {
                carteView.setOnMouseReleased(event ->
                {

                    if(event.getButton() == MouseButton.PRIMARY)
                    {
                        //TODO faire en sort de lancer le sort si possible et de gérer les liste de carte et les pv
                        System.err.println("SORT LANCER !");
                    }
                    else if (event.getButton() == MouseButton.SECONDARY)
                    {
                        //TODO gagner mana
                        //faut gagner le mana
                    }

                    Timeline timeline = new Timeline();
                    timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO, new KeyValue(carteView.xProperty(), carteView.getX())),
                            new KeyFrame(Duration.ZERO, new KeyValue(carteView.yProperty(), carteView.getY())),
                            new KeyFrame(new Duration(200), new KeyValue(carteView.xProperty(), 793)),
                            new KeyFrame(new Duration(200), new KeyValue(carteView.yProperty(), 407))
                    );
                    timeline.play();
                    timeline.setOnFinished(eventTime ->
                    {
                        this.carteView.setVisible(false);
                        this.carteView = null;
                        int ind = demiTerrainOwner.getMainView().indexOf(this);
                        demiTerrainOwner.getOwner().getMain_joueur().remove(carte);
                        demiTerrainOwner.getOwner().getBreak_zone().add(carte);
                        carte = null;
                        demiTerrainOwner.getMainView().remove(this);
                        demiTerrainOwner.miseAJourMainSelonIndice(ind, false);
                        root.getChildren().remove(carteView);
                    });

                });
            }
            carteView.setOnMouseEntered(event ->
            {
                Jeu.carteDescriptionBox.setTextBoxCarte(carte);
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, new KeyValue(Jeu.carteDescriptionBox.getRec().yProperty(), Jeu.carteDescriptionBox.getRec().getY())),
                        new KeyFrame(Duration.ZERO, new KeyValue(Jeu.carteDescriptionBox.getText().yProperty(), Jeu.carteDescriptionBox.getText().getY())),
                        new KeyFrame(new Duration(200), new KeyValue(Jeu.carteDescriptionBox.getText().yProperty(), 670)),
                        new KeyFrame(new Duration(200), new KeyValue(Jeu.carteDescriptionBox.getRec().yProperty(), 650))
                );
                timeline.play();
            });

            carteView.setOnMouseExited(event ->
            {
                Jeu.carteDescriptionBox.setTextBoxCarte(carte);
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, new KeyValue(Jeu.carteDescriptionBox.getRec().yProperty(), Jeu.carteDescriptionBox.getRec().getY())),
                        new KeyFrame(Duration.ZERO, new KeyValue(Jeu.carteDescriptionBox.getText().yProperty(), Jeu.carteDescriptionBox.getText().getY())),
                        new KeyFrame(new Duration(200), new KeyValue(Jeu.carteDescriptionBox.getText().yProperty(), 1200)),
                        new KeyFrame(new Duration(200), new KeyValue(Jeu.carteDescriptionBox.getRec().yProperty(), 1200))
                );
                timeline.play();
            });
        }


    }

    /**
     * Méthode qui va déplacer la vue aux coordonnées x et y
     * @param x coordonnée x
     * @param y coordonnée y
     */
    public void deplacerView(double x, double y)
    {
        //animation de tirage de la carte
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(carteView.xProperty(), carteView.getX())),
                new KeyFrame(Duration.ZERO, new KeyValue(carteView.yProperty(), carteView.getY())),
                new KeyFrame(new Duration(50), new KeyValue(carteView.xProperty(), x)),
                new KeyFrame(new Duration(50), new KeyValue(carteView.yProperty(), y))
        );
        timeline.play();
    }


    public Carte getCarte()
    {
        return carte;
    }
}

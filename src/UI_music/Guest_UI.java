/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_music;

import Component_Music.Account;
import Component_Music.AddSong;
import Component_Music.Artist;
import Component_Music.DetailSongPopUp;
import Component_Music.SearchPage;
import Component_Music.SearchSystem;
import Component_Music.Song;
import Component_Music.TopChartPane;
import static UI_music.User_UI.searchTextField;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author poomi
 */
public class Guest_UI extends UI {

    private SearchSystem searchSystemMyLibrary = new SearchSystem();

    // Create File for downloader
    private File fileForDownload;
    private String nameSet;
    private Song songSelected;
    private Account userAccount;
    private String page;
    
    private File musicfile = new File("src/data/music.dat");
    private File artistfile = new File("src/data/artist.dat");

    private ArrayList<Account> updateAccount = new ArrayList<>();
    private File user = new File("src/data/user.dat");

    public Guest_UI(Stage stage, Account userAccount) {
        super(stage, userAccount);
        this.userAccount = userAccount;
        try {
            songArrayList = ReadWriteFile.readFileSong(musicfile);
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("SearchPage: ERROR READ MUSIC.DAT");
        }

        try {
            artistArrayList = new ReadWriteFile().readFileArtist(artistfile);
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("SearchPage: ERROR READ MUSIC.DAT");
        }

        Scene scene = new Scene(allPane(), 1280, 960);
        String stylrSheet = getClass().getResource("/style_css/style.css").toExternalForm();
        scene.getStylesheets().add(stylrSheet);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public AnchorPane firstPagePane(String page) {
        AnchorPane pane = new AnchorPane();
        this.page = page;
        pane.getChildren().addAll(allSong());

        return pane;
    }

    private AnchorPane pane = new AnchorPane();
    private VBox detailDownload = new VBox(10);

    @Override
    public AnchorPane thirdPagePane() {
        return null;
    }

    public void updateDetailDownload() {
        pane.getChildren().remove(0);
        ((Label) detailDownload.getChildren().get(0)).setText("Song : " + songSelected.getNameSong());
        ((Label) detailDownload.getChildren().get(1)).setText("Artist : " + songSelected.getArtistSong());
        ((Label) detailDownload.getChildren().get(2)).setText("Downloadable(Time) : " + "3"); // wait

        AnchorPane img = new AnchorPane();
        img.setMaxSize(300, 400);
        img.setLayoutX(1030 - 300 - 20);
        img.setLayoutY(20);

        Image imageMy = songSelected.getPhoto();
        ImageView imgMy = new ImageView(imageMy);
        imgMy.setFitHeight(400);
        img.getChildren().add(imgMy);

        pane.getChildren().add(0, img);
    }

    private Button creaButton(String text) {
        Button downLoadButton = new Button(text);
        downLoadButton.getStyleClass().add("detailbtn");
        downLoadButton.setMinSize(200, 50);
        return downLoadButton;

    }

    @Override
    public HBox searchBoxAll() {
        HBox hBox = new HBox();
        hBox.setPrefSize(1030 - 300 - 60 - 70, 30);
        //hBox.setAlignment(Pos.CENTER);
        hBox.setLayoutX(20);
        hBox.setLayoutY(15);

        searchTextField = new TextField();
        searchTextField.setPromptText("Search");
        searchTextField.setStyle("-fx-font-size: 12px;");
        searchTextField.setPrefSize(250, 30);

        searchTextField.setOnMouseClicked(event -> {
            UI.vbox.getChildren().remove(1);
            UI.vbox.getChildren().addAll(new SearchPage("").getSearchPane());
        });

        /// 1030-300-60-70
        hBox.getChildren().addAll(searchTextField);

        return hBox;
    }

    @Override
    public HBox searchBoxMy() {
        HBox hBox = new HBox();
        hBox.setMinSize(1030 - 300 - 60, 30);
        hBox.setLayoutX(20);
        hBox.setLayoutY(60);

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Search Music");
        searchTextField.setMinSize(1030 - 300 - 60 - 70, 30);

        Button searchButton = creaButton("Refresh");
        searchButton.setStyle("-fx-font-size : 15px;");
        searchButton.setMinSize(50, 30);
        HBox.setMargin(searchButton, new Insets(0, 0, 0, 10));

        searchTextField.textProperty().addListener(searchSystemMyLibrary);

        hBox.getChildren().addAll(searchTextField, searchButton);

        return hBox;
    }

    public static AnchorPane totalPane;

    private BorderPane allSong() {

        BorderPane scrollPane = new BorderPane();
        scrollPane.setPrefSize(1030, 900);
        scrollPane.setPadding(new Insets(10));
        scrollPane.getStyleClass().add("scroll-bar");
        totalPane = new AnchorPane();
        totalPane.getStyleClass().add("allSong");

        totalPane.getChildren().addAll(new TopChartPane().getTopchartpane());

        scrollPane.setCenter(totalPane);

        return scrollPane;
    }

    public TilePane updateScrollPane(String text) {
        VBox paneContent;
        Button contentButton;
        ImageView imageView;

        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(10, 10, 10, 10));
        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.setAlignment(Pos.CENTER_LEFT); // By POP
        ObservableList<Song> list = null;
        try {
            list = Song.getMyMusicList();
        } catch (IOException ex) {
            System.out.println("song 1");
        } catch (ClassNotFoundException ex) {
            System.out.println("song 2");
        }

        String lowerCase = text.toLowerCase();

        for (Song song : list) {

            if ((song.getNameSong().toLowerCase().contains(lowerCase) || song.getArtistSong().toLowerCase().contains(lowerCase))) {

                boolean inMyList = false;
                for (AddSong song1 : userAccount.getMyListSong()) {
                    System.out.println("1");
                    if (!userAccount.isFirstSong()) {
                        if ((song.getNameSong().toLowerCase().contains(song1.getNameSong().toLowerCase()) && song.getArtistSong().toLowerCase().contains(song1.getArtistSong().toLowerCase()))) {
                            System.out.println("2");
                            inMyList = true;
                            break;
                        }
                    }

                }

                if (!inMyList) {
                    contentButton = new Button();
                    contentButton.getStyleClass().add("contentDetailbtn");
                    paneContent = new VBox();
                    paneContent.setAlignment(Pos.CENTER);
                    paneContent.setPadding(new Insets(20));
                    paneContent.getStyleClass().add("content-allSong");

                    imageView = new ImageView(new Image("/image/1.jpg"));
                    imageView.setFitHeight(200); // By pop
                    imageView.setFitWidth(150); // By pop

                    paneContent.getChildren().addAll(imageView, new Label(song.getNameSong()), new Label("ARTIST : " + song.getArtistSong()));
                    contentButton.setGraphic(paneContent);
                    contentButton.setMinHeight(300); // By Pop
                    contentButton.setMinWidth(300); // By Pop
                    contentButton.setOnMouseClicked(e -> {
                        try {
                            new DetailSongPopUp(song);
                        } catch (InterruptedException ex) {
                            System.out.println("Detail Song Popup : " + ex);
                        }
                    });

                    tilePane.getChildren().add(contentButton);
                }
            }
        }
        return tilePane;
    }

    @Override
    public BorderPane myAccount() {
        return null;
    }

    public void downloader() {

        System.out.println("Download");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        fileChooser.setInitialFileName(nameSet);
        File downloadFile = fileChooser.showSaveDialog(null);

        if (downloadFile != null) {
            try {
                Files.copy(fileForDownload.toPath(), downloadFile.toPath());
            } catch (IOException ex) {
                System.out.println("DownloadFile" + ex);
            }
        }

    }

    @Override
    public void userLogout() {

        ReadWriteFile file = new ReadWriteFile();
        ArrayList<Account> nowAccount = null;

        try {
            nowAccount = file.readFile(user);
        } catch (IOException ex) {
            Logger.getLogger(Guest_UI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Guest_UI.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Account account : nowAccount) {
            if (account.getUsername().equals(userAccount.getUsername())) {
                updateAccount.add(userAccount);
            } else {
                updateAccount.add(account);
            }
        }

        try {
            file.writeFile(user, updateAccount);
        } catch (IOException ex) {
            Logger.getLogger(Guest_UI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

package com.example.inventorymanagementsystem;


import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 *
 *
 * Class Inventory.java
 */



/**
 *
 * @author Joshua Call
 */



public class Inventory extends Application {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static boolean saved;

    //Javadoc Folder is located in WarehouseInventory Package
    /**
     * FUTURE ENHANCEMENT (Already Implemented)
     * The specified requirements allow product associated parts to be added
     * and removed solely based on the add and remove associated parts buttons
     * and this action is not undone when the user presses cancel, this could be detrimental
     * and cause product errors if many changes are made but "cancelled" and the user
     * cannot remember the original associated parts of the product.  This enhancement stores
     * all potential changes to the products parts and only official updates the product if the
     * user presses the save button.  The cancel button discards the changes made to the products parts
     * and reverts the product back to its true original state.
     *
     * @param args start of program
     */
    public static void main(String[] args){
        saved = true;
        try (Scanner scanner = new Scanner(Paths.get("InventoryData.txt"))){
            boolean partsProcessing = true;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(partsProcessing){
                    if(line.isEmpty()){
                        partsProcessing = false;
                        continue;
                    }else if(line.equals("NULL")){
                        allParts.add(null);
                    }else {
                        Part part;
                        boolean inHouse = true;
                        String parts[] = line.split(":");
                        try {
                            Integer.parseInt(parts[6]);
                        } catch (NumberFormatException nfe) {
                            inHouse = false;
                        }
                        if (inHouse) {
                            part = new InHouse(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]),
                                    Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]));
                        } else {
                            part = new Outsourced(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]),
                                    Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), parts[6]);
                        }
                        allParts.add(part);
                    }
                }else{
                    if(line.equals("NULL")){
                        allProducts.add(null);
                    }else{
                        Product product;
                        String products[] = line.split(":");
                        product = new Product(Integer.parseInt(products[0]), products[1], Double.parseDouble(products[2]),
                                Integer.parseInt(products[3]), Integer.parseInt(products[4]), Integer.parseInt(products[5]));
                        for(int x = 0; x < Integer.parseInt(products[6]); x++){
                            product.addAssociatedPart(allParts.get(Integer.parseInt(scanner.nextLine())-1));
                        }
                        allProducts.add(product);
                    }
                }
            }
        }catch(IOException ioe){
            System.out.println("Unable To Open Load File");
        }

        launch(Inventory.class);

    }


    /**
     * This method starts the GUI
     *
     * @param stage the Stage that will be used
     * @throws IOException in case fails
     */
    public void start(Stage stage) throws IOException {



//=============  Opening Scene   =========================================================================================================



        AnchorPane main = new AnchorPane();                             //Main Application Pane
        main.setPrefSize(1150, 400);



        Label MFLabel = new Label("Inventory Management System");    //Main Form
        MFLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
        AnchorPane.setTopAnchor(MFLabel, 20.0);
        AnchorPane.setLeftAnchor(MFLabel, 20.0);

        Button saveButton = new Button ("Save");
        AnchorPane.setBottomAnchor(saveButton, 10.0);
        AnchorPane.setRightAnchor(saveButton, 60.0);

        Button exitButton = new Button("Exit");
        AnchorPane.setBottomAnchor(exitButton, 10.0);
        AnchorPane.setRightAnchor(exitButton, 10.0);

        Popup popup = new Popup();
        AnchorPane popupPane = new AnchorPane();
        Label popupMessage = new Label("Some Changes Have Not Been Saved!\n\n   Are You Sure You Want To Exit?");
        AnchorPane.setTopAnchor(popupMessage, 20.0);
        AnchorPane.setLeftAnchor(popupMessage, 20.0);
        AnchorPane.setRightAnchor(popupMessage, 20.0);
        Button popupYesButton = new Button("Yes");
        AnchorPane.setTopAnchor(popupYesButton, 80.0);
        AnchorPane.setLeftAnchor(popupYesButton, 40.0);
        Button popupNoButton = new Button("No");
        AnchorPane.setTopAnchor(popupNoButton, 80.0);
        AnchorPane.setLeftAnchor(popupNoButton, 160.0);
        popupPane.setPadding(new Insets(20, 20, 20, 20));
        popupPane.getChildren().addAll(popupMessage, popupYesButton, popupNoButton);
        popup.getContent().add(popupPane);


        Label MFnotifier = new Label("");
        AnchorPane.setLeftAnchor(MFnotifier, 475.0);
        AnchorPane.setTopAnchor(MFnotifier, 350.0);

        AnchorPane MFpartsPane = new AnchorPane();                      //Main Form Parts
        MFpartsPane.setPrefSize(500, 250);
        MFpartsPane.setStyle("-fx-border-color: black; -fx-border-radius: 10px");
        AnchorPane.setTopAnchor(MFpartsPane, 70.0);
        AnchorPane.setLeftAnchor(MFpartsPane, 50.0);


        Label partsLabel = new Label("Parts");
        partsLabel.setStyle("-fx-font-weight: bold");
        AnchorPane.setTopAnchor(partsLabel, 10.0);
        AnchorPane.setLeftAnchor(partsLabel, 10.0);

        TextField partsTF = new TextField();
        partsTF.setPromptText("Search by Part ID or Name");
        AnchorPane.setTopAnchor(partsTF, 10.0);
        AnchorPane.setRightAnchor(partsTF, 10.0);

        TableView<Part> partsTV = new TableView<>();
        AnchorPane.setTopAnchor(partsTV, 40.0);
        AnchorPane.setBottomAnchor(partsTV, 40.0);
        AnchorPane.setLeftAnchor(partsTV, 10.0);
        AnchorPane.setRightAnchor(partsTV, 10.0);
        TableColumn partId = new TableColumn("Part ID");
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partId.setMinWidth(25);
        TableColumn partName = new TableColumn("Part Name");
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partName.setMinWidth(100);
        TableColumn partInventoryLevel = new TableColumn("Inventory Level");
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partInventoryLevel.setMinWidth(150);
        TableColumn partPrice = new TableColumn("Price/ Cost Per Unit");
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partPrice.setMinWidth(150);
        partsTV.getColumns().addAll(partId, partName, partInventoryLevel, partPrice);
        partsTV.setItems(getAllParts());                                         //Assign parts list to view

        Button partsAdd = new Button("Add");
        AnchorPane.setBottomAnchor(partsAdd, 10.0);
        AnchorPane.setRightAnchor(partsAdd, 150.0);

        Button partsModify = new Button("Modify");
        AnchorPane.setBottomAnchor(partsModify, 10.0);
        AnchorPane.setRightAnchor(partsModify, 75.0);

        Button partsDelete = new Button("Delete");
        AnchorPane.setBottomAnchor(partsDelete, 10.0);
        AnchorPane.setRightAnchor(partsDelete, 10.0);

        MFpartsPane.getChildren().addAll(partsLabel, partsTF, partsTV, partsAdd, partsModify, partsDelete);


        AnchorPane MFproductsPane = new AnchorPane();                      //Main Form Products
        MFproductsPane.setPrefSize(500, 250);
        MFproductsPane.setStyle("-fx-border-color: black; -fx-border-radius: 10px");
        AnchorPane.setTopAnchor(MFproductsPane, 70.0);
        AnchorPane.setLeftAnchor(MFproductsPane, 600.0);

        Label productsLabel = new Label("Products");
        productsLabel.setStyle("-fx-font-weight: bold");
        AnchorPane.setTopAnchor(productsLabel, 10.0);
        AnchorPane.setLeftAnchor(productsLabel, 10.0);

        TextField productsTF = new TextField();
        productsTF.setPromptText("Search by Product ID or Name");
        AnchorPane.setTopAnchor(productsTF, 10.0);
        AnchorPane.setRightAnchor(productsTF, 10.0);

        TableView<Product> productsTV = new TableView<>();
        AnchorPane.setTopAnchor(productsTV, 40.0);
        AnchorPane.setBottomAnchor(productsTV, 40.0);
        AnchorPane.setLeftAnchor(productsTV, 10.0);
        AnchorPane.setRightAnchor(productsTV, 10.0);
        TableColumn productId = new TableColumn("Product ID");
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productId.setMinWidth(25);
        TableColumn productName = new TableColumn("Product Name");
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productName.setMinWidth(100);
        TableColumn productInventoryLevel = new TableColumn("Inventory Level");
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productInventoryLevel.setMinWidth(150);
        TableColumn productPrice = new TableColumn("Price/ Cost Per Unit");
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPrice.setMinWidth(150);
        productsTV.getColumns().addAll(productId, productName, productInventoryLevel, productPrice);
        productsTV.setItems(getAllProducts());

        Button productsAdd = new Button("Add");
        AnchorPane.setBottomAnchor(productsAdd, 10.0);
        AnchorPane.setRightAnchor(productsAdd, 150.0);

        Button productsModify = new Button("Modify");
        AnchorPane.setBottomAnchor(productsModify, 10.0);
        AnchorPane.setRightAnchor(productsModify, 75.0);

        Button productsDelete = new Button("Delete");
        AnchorPane.setBottomAnchor(productsDelete, 10.0);
        AnchorPane.setRightAnchor(productsDelete, 10.0);

        MFproductsPane.getChildren().addAll(productsLabel, productsTF, productsTV, productsAdd, productsModify, productsDelete);

        main.getChildren().addAll(MFLabel, MFpartsPane, MFproductsPane, MFnotifier, saveButton, exitButton);



//==============   Part Scene    ==================================================================================================



        AnchorPane parts = new AnchorPane();
        parts.setPadding(new Insets(20, 20, 20, 20));

        Label partsSceneLabel = new Label("");
        partsSceneLabel.setStyle("-fx-font-weight: bold");
        AnchorPane.setLeftAnchor(partsSceneLabel, 30.0);
        AnchorPane.setTopAnchor(partsSceneLabel, 30.0);

        RadioButton inHouseButton = new RadioButton("In-House");
        inHouseButton.selectedProperty().set(true);
        AnchorPane.setLeftAnchor(inHouseButton, 130.0);
        AnchorPane.setTopAnchor(inHouseButton, 30.0);

        RadioButton outsourcedButton = new RadioButton("Outsourced");
        AnchorPane.setLeftAnchor(outsourcedButton, 350.0);
        AnchorPane.setTopAnchor(outsourcedButton, 30.0);

        Label partsIdLabel = new Label("ID");
        AnchorPane.setLeftAnchor(partsIdLabel, 50.0);
        AnchorPane.setTopAnchor(partsIdLabel, 100.0);

        Label partsNameLabel = new Label("Name");
        AnchorPane.setLeftAnchor(partsNameLabel, 50.0);
        AnchorPane.setTopAnchor(partsNameLabel, 150.0);

        Label partsInvLabel = new Label("Inventory");
        AnchorPane.setLeftAnchor(partsInvLabel, 50.0);
        AnchorPane.setTopAnchor(partsInvLabel, 200.0);

        Label partsPriceLabel = new Label("Price/Cost");
        AnchorPane.setLeftAnchor(partsPriceLabel, 50.0);
        AnchorPane.setTopAnchor(partsPriceLabel, 250.0);

        Label partsMaxLabel = new Label("Max");
        AnchorPane.setLeftAnchor(partsMaxLabel, 50.0);
        AnchorPane.setTopAnchor(partsMaxLabel, 300.0);

        Label partsMinLabel = new Label("Min");
        AnchorPane.setLeftAnchor(partsMinLabel, 350.0);
        AnchorPane.setTopAnchor(partsMinLabel, 300.0);

        Label partsOriginLabel = new Label("Machine ID");
        AnchorPane.setLeftAnchor(partsOriginLabel, 50.0);
        AnchorPane.setTopAnchor(partsOriginLabel, 350.0);

        Label partsErrorMessage = new Label();
        AnchorPane.setLeftAnchor(partsErrorMessage, 50.0);
        AnchorPane.setTopAnchor(partsErrorMessage, 400.0);

        TextField partsIdTF = new TextField();
        partsIdTF.setPromptText("Auto Gen- Disabled");
        partsIdTF.setDisable(true);
        AnchorPane.setLeftAnchor(partsIdTF, 150.0);
        AnchorPane.setTopAnchor(partsIdTF, 100.0);

        TextField partsNameTF = new TextField();
        AnchorPane.setLeftAnchor(partsNameTF, 150.0);
        AnchorPane.setTopAnchor(partsNameTF, 150.0);

        TextField partsInvTF = new TextField();
        AnchorPane.setLeftAnchor(partsInvTF, 150.0);
        AnchorPane.setTopAnchor(partsInvTF, 200.0);

        TextField partsPriceTF = new TextField();
        AnchorPane.setLeftAnchor(partsPriceTF, 150.0);
        AnchorPane.setTopAnchor(partsPriceTF, 250.0);

        TextField partsMaxTF = new TextField();
        AnchorPane.setLeftAnchor(partsMaxTF, 150.0);
        AnchorPane.setTopAnchor(partsMaxTF, 300.0);

        TextField partsMinTF = new TextField();
        AnchorPane.setLeftAnchor(partsMinTF, 400.0);
        AnchorPane.setTopAnchor(partsMinTF, 300.0);

        TextField partsOriginTF = new TextField();
        AnchorPane.setLeftAnchor(partsOriginTF, 150.0);
        AnchorPane.setTopAnchor(partsOriginTF, 350.0);

        Button partsSaveButton = new Button("Save");
        AnchorPane.setLeftAnchor(partsSaveButton, 450.0);
        AnchorPane.setTopAnchor(partsSaveButton, 600.0);

        Button partsCancelButton = new Button("Cancel");
        AnchorPane.setLeftAnchor(partsCancelButton, 500.0);
        AnchorPane.setTopAnchor(partsCancelButton, 600.0);

        parts.getChildren().addAll(partsSceneLabel, inHouseButton, outsourcedButton, partsIdLabel,
                partsNameLabel, partsInvLabel, partsPriceLabel, partsMaxLabel, partsMinLabel, partsOriginLabel,
                partsIdTF, partsNameTF, partsInvTF, partsPriceTF, partsMaxTF, partsMinTF, partsOriginTF,
                partsErrorMessage, partsSaveButton, partsCancelButton);




        //=============   Product Scene  ==========================================================================================



        AnchorPane products = new AnchorPane();
        products.setPadding(new Insets(0, 50, 50, 0));


        AnchorPane productsPane = new AnchorPane();
        productsPane.setPadding(new Insets(20, 30, 20, 30));
        productsPane.setStyle("-fx-border-color: black; -fx-border-radius: 10px");
        AnchorPane.setTopAnchor(productsPane, 50.0);
        AnchorPane.setLeftAnchor(productsPane, 50.0);

        Label productsPaneLabel = new Label("");
        productsPaneLabel.setStyle("-fx-font-weight: bold");
        AnchorPane.setLeftAnchor(productsPaneLabel, 20.0);
        AnchorPane.setTopAnchor(productsPaneLabel, 30.0);

        Label productsIdLabel = new Label("ID");
        AnchorPane.setLeftAnchor(productsIdLabel, 50.0);
        AnchorPane.setTopAnchor(productsIdLabel, 150.0);

        Label productsNameLabel = new Label("Name");
        AnchorPane.setLeftAnchor(productsNameLabel, 50.0);
        AnchorPane.setTopAnchor(productsNameLabel, 200.0);

        Label productsInvLabel = new Label("Inventory");
        AnchorPane.setLeftAnchor(productsInvLabel, 50.0);
        AnchorPane.setTopAnchor(productsInvLabel, 250.0);

        Label productsPriceLabel = new Label("Price");
        AnchorPane.setLeftAnchor(productsPriceLabel, 50.0);
        AnchorPane.setTopAnchor(productsPriceLabel, 300.0);

        Label productsMaxLabel = new Label("Max");
        AnchorPane.setLeftAnchor(productsMaxLabel, 50.0);
        AnchorPane.setTopAnchor(productsMaxLabel, 350.0);

        Label productsMinLabel = new Label("Min");
        AnchorPane.setLeftAnchor(productsMinLabel, 250.0);
        AnchorPane.setTopAnchor(productsMinLabel, 350.0);

        TextField productsIdTF = new TextField();
        productsIdTF.setPromptText("Auto Gen- Disabled");
        productsIdTF.setDisable(true);
        AnchorPane.setLeftAnchor(productsIdTF, 130.0);
        AnchorPane.setTopAnchor(productsIdTF, 150.0);

        TextField productsNameTF = new TextField();
        AnchorPane.setLeftAnchor(productsNameTF, 130.0);
        AnchorPane.setTopAnchor(productsNameTF, 200.0);

        TextField productsInvTF = new TextField();
        productsInvTF.setPrefWidth(100);
        AnchorPane.setLeftAnchor(productsInvTF, 130.0);
        AnchorPane.setTopAnchor(productsInvTF, 250.0);

        TextField productsPriceTF = new TextField();
        productsPriceTF.setPrefWidth(100);
        AnchorPane.setLeftAnchor(productsPriceTF, 130.0);
        AnchorPane.setTopAnchor(productsPriceTF, 300.0);

        TextField productsMaxTF = new TextField();
        productsMaxTF.setPrefWidth(100);
        AnchorPane.setLeftAnchor(productsMaxTF, 130.0);
        AnchorPane.setTopAnchor(productsMaxTF, 350.0);

        TextField productsMinTF = new TextField();
        productsMinTF.setPrefWidth(100);
        AnchorPane.setLeftAnchor(productsMinTF, 300.0);
        AnchorPane.setTopAnchor(productsMinTF, 350.0);

        TextField productPartSearchTF = new TextField();
        productPartSearchTF.setPromptText("Search by Part ID or Name");
        AnchorPane.setLeftAnchor(productPartSearchTF, 735.0);
        AnchorPane.setTopAnchor(productPartSearchTF, 10.0);

        TableView<Part> partLookupTV = new TableView();
        partLookupTV.setPrefSize(400, 200);
        AnchorPane.setLeftAnchor(partLookupTV, 500.0);
        AnchorPane.setTopAnchor(partLookupTV, 50.0);
        TableColumn topTVId = new TableColumn("Part ID");
        topTVId.setCellValueFactory(new PropertyValueFactory<>("id"));
        topTVId.setMinWidth(25);
        TableColumn topTVName = new TableColumn("Part Name");
        topTVName.setCellValueFactory(new PropertyValueFactory<>("name"));
        topTVName.setMinWidth(80);
        TableColumn topTVInventoryLevel = new TableColumn("Inventory Level");
        topTVInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        topTVInventoryLevel.setMinWidth(110);
        TableColumn topTVPrice = new TableColumn("Price/ Cost Per Unit");
        topTVPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        topTVPrice.setMinWidth(130);
        partLookupTV.getColumns().addAll(topTVId, topTVName, topTVInventoryLevel, topTVPrice);
        partLookupTV.setItems(getAllParts());

        Button addPartButton = new Button("Add");
        AnchorPane.setLeftAnchor(addPartButton, 800.0);
        AnchorPane.setTopAnchor(addPartButton, 260.0);

        TableView<Part> associatedPartsTV = new TableView();
        associatedPartsTV.setPrefSize(400, 200);
        AnchorPane.setLeftAnchor(associatedPartsTV, 500.0);
        AnchorPane.setTopAnchor(associatedPartsTV, 300.0);
        TableColumn bottomTVId = new TableColumn("Part ID");
        bottomTVId.setCellValueFactory(new PropertyValueFactory<>("id"));
        bottomTVId.setMinWidth(25);
        TableColumn bottomTVName = new TableColumn("Part Name");
        bottomTVName.setCellValueFactory(new PropertyValueFactory<>("name"));
        bottomTVName.setMinWidth(80);
        TableColumn bottomTVInventoryLevel = new TableColumn("Inventory Level");
        bottomTVInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        bottomTVInventoryLevel.setMinWidth(110);
        TableColumn bottomTVPrice = new TableColumn("Price/ Cost Per Unit");
        bottomTVPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        bottomTVPrice.setMinWidth(130);
        associatedPartsTV.getColumns().addAll(bottomTVId, bottomTVName, bottomTVInventoryLevel, bottomTVPrice);


        Button removePartButton = new Button("Remove Associated Part");
        AnchorPane.setLeftAnchor(removePartButton, 735.0);
        AnchorPane.setTopAnchor(removePartButton, 520.0);


        Button productsSaveButton = new Button("Save");
        AnchorPane.setLeftAnchor(productsSaveButton, 735.0);
        AnchorPane.setTopAnchor(productsSaveButton, 570.0);

        Button productsCancelButton = new Button("Cancel");
        AnchorPane.setLeftAnchor(productsCancelButton, 810.0);
        AnchorPane.setTopAnchor(productsCancelButton, 570.0);

        Label productsErrorMessage = new Label("");
        AnchorPane.setTopAnchor(productsErrorMessage, 400.0);
        AnchorPane.setLeftAnchor(productsErrorMessage, 50.0);

        productsPane.getChildren().addAll(productsPaneLabel, productsIdLabel, productsNameLabel, productsInvLabel, productsPriceLabel, productsMaxLabel, productsMinLabel,
                productsIdTF, productsNameTF,  productsInvTF, productsPriceTF,  productsMaxTF, productsMinTF, productPartSearchTF, addPartButton,
                partLookupTV, associatedPartsTV, removePartButton, productsSaveButton, productsCancelButton, productsErrorMessage);
        products.getChildren().add(productsPane);



        //================= Set the Stage =================================================================================================


        popupPane.setStyle("-fx-font-family: 'serif'");
        Scene openingScene = new Scene(main);
        Scene partScene = new Scene(parts);
        Scene productScene = new Scene(products);
        openingScene.getRoot().setStyle("-fx-font-family: 'serif'");     //---Fixes something wrong with font----
        partScene.getRoot().setStyle("-fx-font-family: 'serif'");
        productScene.getRoot().setStyle("-fx-font-family: 'serif'");

        stage.setScene(openingScene);



        //================= Button and Search Field Actions ===============================================================================


        //Read in data from file csv each on new line for parts?
        exitButton.setOnAction((event) -> {                               //Close the Application
            if(saved) {
                stage.close();
            }else{
                popup.show(stage);
                popup.getScene().getRoot().setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10px");


            }
        });

        popupYesButton.setOnAction((event) -> {
            stage.close();
        });
        popupNoButton.setOnAction((event) -> {
            popup.hide();
        });
        saveButton.setOnAction((event) -> {
            //Must save parts to file first
            //Write all parts to file each on own line, remember nulls
            //Leave empty Line
            //Write all products to file each on own line and associated parts names on next line
            //leave empty line
            //Colon Separated Values
            try (PrintWriter writer = new PrintWriter("InventoryData.txt")){
                for(Part part : allParts){
                    if(part == null){
                        writer.println("NULL");
                    }else {
                        writer.println(part);
                    }
                }
                //Products with : prefaced parts
                writer.println();
                for(Product product : allProducts){
                    if(product == null){
                        writer.println("NULL");
                    }else{
                        writer.println(product);
                        for(Part part : product.getAllAssociatedParts()){
                            if(!(part == null)) {
                                writer.println(part.getId());
                            }
                        }
                    }
                }

                writer.close();

                saved = true;
            }catch (IOException ioe){
                System.out.println("Error Saving File, Try Again");
            }

        });




        //==========Part Specific===========================



        partsTF.setOnAction((event) ->{                                 //Search for a Part
            MFnotifier.setText("");
            partsTV.getSelectionModel().clearSelection();
            boolean searchById = true;
            ObservableList<Part> lookedUpItems;
            try{
                Integer.parseInt(partsTF.getText());
            }catch (NumberFormatException nfe){
                searchById = false;
            }
            if(searchById){                                             //Search by ID
                if(Integer.parseInt(partsTF.getText()) >= 0 && Integer.parseInt(partsTF.getText()) <= allParts.size()) {
                    lookedUpItems = FXCollections.observableArrayList();
                    Part part = lookupPart(Integer.parseInt(partsTF.getText()) - 1);
                    if (part == null) {
                        MFnotifier.setText("No Part Found With That ID!");
                    }
                    lookedUpItems.add(part);
                    partsTV.setItems(lookedUpItems);
                    partsTV.getSelectionModel().select(lookedUpItems.get(0));
                }else{
                    MFnotifier.setText("No Part Found With That ID!");
                }

            }else {                                                     //Search by Name
                lookedUpItems = lookupPart(partsTF.getText());
                if(lookedUpItems.size() == 0){
                    MFnotifier.setText("No Part Found With That Name!");
                }
                partsTV.setItems(lookedUpItems);
                if (lookedUpItems.size() == 1) {
                    partsTV.getSelectionModel().select(lookedUpItems.get(0));

                }
                if (lookedUpItems.size() == 1 && partsTF.getText().equals("")){
                    partsTV.getSelectionModel().clearSelection();
                }
            }


        });

        partsAdd.setOnAction((event) -> {                               //Go to Add Part
            partsIdTF.setText(Integer.toString(allParts.size()+1));
            partsNameTF.setText("");
            partsPriceTF.setText("");
            partsInvTF.setText("");
            partsMaxTF.setText("");
            partsMinTF.setText("");
            partsOriginTF.setText("");
            partsSceneLabel.setText("Add Part");
            partsErrorMessage.setText("");
            stage.setScene(partScene);
        });

        inHouseButton.setOnAction((event) -> {                          //Radio Button In House
            partsOriginLabel.setText("Machine ID");
            outsourcedButton.setSelected(false);
        });

        outsourcedButton.setOnAction((event) -> {                       //Radio Button Outsourced
            partsOriginLabel.setText("Company Name");
            inHouseButton.setSelected(false);
        });

        partsModify.setOnAction((event) -> {                            //Go to Modify Part
            MFnotifier.setText("");
            if(partsTV.getSelectionModel().getSelectedItem() != null) {
                Part part = partsTV.getSelectionModel().getSelectedItem();
                partsIdTF.setText(Integer.toString(part.getId()));
                partsNameTF.setText(part.getName());
                partsPriceTF.setText(Double.toString(part.getPrice()));
                partsInvTF.setText(Integer.toString(part.getStock()));
                partsMaxTF.setText(Integer.toString(part.getMax()));
                partsMinTF.setText(Integer.toString(part.getMin()));
                if (part instanceof InHouse) {
                    outsourcedButton.setSelected(false);
                    inHouseButton.setSelected(true);
                    partsOriginLabel.setText("Machine ID");
                    partsOriginTF.setText(Integer.toString(((InHouse) part).getMachineId()));
                } else {
                    inHouseButton.setSelected(false);
                    outsourcedButton.setSelected(true);
                    partsOriginLabel.setText("Company Name");
                    partsOriginTF.setText(((Outsourced) part).getCompanyName());
                }
                partsSceneLabel.setText("Modify Part");
                partsErrorMessage.setText("");
                stage.setScene(partScene);
            }else{
                MFnotifier.setText("Select a Part to Modify!");
            }
        });

        partsSaveButton.setOnAction((event) -> {                        //Save a Part


            boolean dataGood = true;
            boolean maxCheck = true;
            boolean minCheck = true;
            boolean inventoryCheck = true;

            partsErrorMessage.setText("");
            //Add all these checks to add part method below
            if(partsNameTF.getText().isEmpty()){
                partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Part Name Required");
                dataGood = false;
            }
            try{
                Integer.parseInt(partsInvTF.getText());
            }catch (NumberFormatException nfe){
                partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Invalid Input: Inventory");
                dataGood = false;
                inventoryCheck = false;
            }
            try{
                Double.parseDouble(partsPriceTF.getText());
            }catch (NumberFormatException nfe){
                partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Invalid Input: Price");
                dataGood = false;
            }
            try{
                Integer.parseInt(partsMaxTF.getText());
            }catch (NumberFormatException nfe){
                partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Invalid Input: Max");
                dataGood = false;
                maxCheck = false;
            }
            try {
                Integer.parseInt(partsMinTF.getText());
            }catch (NumberFormatException nfe){
                partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Invalid Input: Min");
                dataGood = false;
                minCheck = false;
            }
            if(maxCheck && minCheck && inventoryCheck) {
                if (!(Integer.parseInt(partsInvTF.getText()) <= Integer.parseInt(partsMaxTF.getText()) &&
                        Integer.parseInt(partsInvTF.getText()) >= Integer.parseInt(partsMinTF.getText()))) {
                    partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Inventory Level Must Be Between Min and Max");
                    dataGood = false;
                }
            }
            if(maxCheck && minCheck) {
                if (!(Integer.parseInt(partsMinTF.getText()) <= Integer.parseInt(partsMaxTF.getText()))) {
                    partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Min Cannot Be Greater Than Max");
                    dataGood = false;
                }
            }
            if(inHouseButton.isSelected()) {
                try {
                    Integer.parseInt(partsOriginTF.getText());
                }catch (NumberFormatException nfe){
                    partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Machine ID Invalid");
                    dataGood = false;
                }
            }
            if(outsourcedButton.isSelected()) {
                if(partsOriginTF.getText().isEmpty()){
                    partsErrorMessage.setText(partsErrorMessage.getText() + "\n\n Company Name Required");
                    dataGood = false;
                }
            }


            if(dataGood) {
                Part part;
                int id = allParts.size() + 1;


                if(Integer.parseInt(partsIdTF.getText()) == allParts.size() + 1){   //If adding a part

                    if (inHouseButton.isSelected()) {
                        part = new InHouse(id, partsNameTF.getText(), Double.parseDouble(partsPriceTF.getText()),
                                Integer.parseInt(partsInvTF.getText()), Integer.parseInt(partsMinTF.getText()),
                                Integer.parseInt(partsMaxTF.getText()), Integer.parseInt(partsOriginTF.getText()));
                    } else {
                        part = new Outsourced(id, partsNameTF.getText(), Double.parseDouble(partsPriceTF.getText()),
                                Integer.parseInt(partsInvTF.getText()), Integer.parseInt(partsMinTF.getText()),
                                Integer.parseInt(partsMaxTF.getText()), partsOriginTF.getText());
                    }
                    addPart(part);


                    MFnotifier.setText("Part Added Successfully!");


                } else {                                                      //If modifying a part
                    if (inHouseButton.isSelected()) {
                        part = new InHouse(Integer.parseInt(partsIdTF.getText()), partsNameTF.getText(),Double.parseDouble(partsPriceTF.getText()),
                                Integer.parseInt(partsInvTF.getText()), Integer.parseInt(partsMinTF.getText()), Integer.parseInt(partsMaxTF.getText()),
                                Integer.parseInt(partsOriginTF.getText()));
                    } else {
                        part = new Outsourced(Integer.parseInt(partsIdTF.getText()), partsNameTF.getText(),Double.parseDouble(partsPriceTF.getText()),
                                Integer.parseInt(partsInvTF.getText()), Integer.parseInt(partsMinTF.getText()), Integer.parseInt(partsMaxTF.getText()),
                                partsOriginTF.getText());
                    }
                    updatePart(part.getId() -1, part);
                    MFnotifier.setText("Part Modified Successfully!");



                }



                partsTV.getSelectionModel().clearSelection();
                partsTF.clear();
                partsTV.setItems(getAllParts());
                partsTV.refresh();
                partLookupTV.setItems(getAllParts());
                saved = false;
                stage.setScene(openingScene);
            }


        });

        partsDelete.setOnAction((event) -> {                            //Delete a Part
            Part part = partsTV.getSelectionModel().getSelectedItem();
            if(part == null){
                MFnotifier.setText("Select a Part to Delete!");
            }else if(deletePart(part)) {
                partsTV.refresh();
                partsTV.getSelectionModel().clearSelection();
                partsTF.clear();
                MFnotifier.setText("Part Deleted Successfully!");
                partsTV.setItems(getAllParts());
                partLookupTV.setItems(getAllParts());
                associatedPartsTV.refresh();
                saved = false;
            } else{
                MFnotifier.setText("Unable to Delete Part!");
            }

        });

        partsCancelButton.setOnAction((event) -> {                         //Exit Add/Modify Part
            //maybe create methods to reset opening scene values that takes scene as parameter

            MFnotifier.setText("");
            partsTV.getSelectionModel().clearSelection();
            partsTF.clear();
            productsTF.clear();
            partsTV.setItems(getAllParts());
            stage.setScene(openingScene);
        });



        //==========Product Specific========================


        productsTF.setOnAction((event) ->{                                 //Search for a Product
            MFnotifier.setText("");
            productsTV.getSelectionModel().clearSelection();
            boolean searchById = true;
            ObservableList<Product> lookedUpItems;
            try{
                Integer.parseInt(productsTF.getText());
            }catch (NumberFormatException nfe){
                searchById = false;
            }
            if(searchById){                                             //Search by ID
                if(Integer.parseInt(productsTF.getText()) >= 0 && Integer.parseInt(productsTF.getText()) <= allProducts.size()) {
                    lookedUpItems = FXCollections.observableArrayList();
                    Product product = lookupProduct(Integer.parseInt(productsTF.getText()) - 1);

                    if (product == null) {
                        MFnotifier.setText("No Product Found With That ID!");
                    }
                    lookedUpItems.add(product);
                    productsTV.setItems(lookedUpItems);
                    productsTV.getSelectionModel().select(lookedUpItems.get(0));
                }else{
                    MFnotifier.setText("No Product Found With That ID!");
                }


            }else {                                                     //Search by Name
                lookedUpItems = lookupProduct(productsTF.getText());
                if(lookedUpItems.size() == 0){
                    MFnotifier.setText("No Product Found With That Name!");
                }
                productsTV.setItems(lookedUpItems);

                if (lookedUpItems.size() == 1) {
                    productsTV.getSelectionModel().select(lookedUpItems.get(0));

                }
                if (lookedUpItems.size() == 1 && productsTF.getText().equals("")){
                    productsTV.getSelectionModel().clearSelection();
                }
            }
        });

        productsAdd.setOnAction((event) -> {                            //Go to Add Product
            associatedPartsTV.getSelectionModel().clearSelection();
            partLookupTV.getSelectionModel().clearSelection();
            productPartSearchTF.clear();
            partLookupTV.setItems(getAllParts());
            ObservableList<Part> blankList = FXCollections.observableArrayList();
            associatedPartsTV.setItems(blankList);
            //Auto gen id
            productsIdTF.setText(Integer.toString(allProducts.size()+1));
            productsNameTF.setText("");
            productsPriceTF.setText("");
            productsInvTF.setText("");
            productsMaxTF.setText("");
            productsMinTF.setText("");
            productsErrorMessage.setText("");
            productsPaneLabel.setText("Add Product");
            stage.setScene(productScene);


        });

        productsModify.setOnAction((event) -> {                         //Go to Modify Product
            associatedPartsTV.getSelectionModel().clearSelection();
            partLookupTV.getSelectionModel().clearSelection();
            productPartSearchTF.clear();
            partLookupTV.setItems(getAllParts());

            if(productsTV.getSelectionModel().getSelectedItem() == null){
                MFnotifier.setText("Select a Product to Modify!");
            }else {
                Product product = productsTV.getSelectionModel().getSelectedItem();
                ObservableList<Part> tempAssociateParts = FXCollections.observableArrayList();
                for(Part part : product.getAllAssociatedParts()){
                    tempAssociateParts.add(part);
                }

                associatedPartsTV.setItems(tempAssociateParts);
                productsIdTF.setText(Integer.toString(product.getId()));
                productsNameTF.setText(product.getName());
                productsPriceTF.setText(Double.toString(product.getPrice()));
                productsInvTF.setText(Integer.toString(product.getStock()));
                productsMaxTF.setText(Integer.toString(product.getMax()));
                productsMinTF.setText(Integer.toString(product.getMin()));
                productsErrorMessage.setText("");
                productsPaneLabel.setText("Modify Product");
                stage.setScene(productScene);
            }
        });

        productsDelete.setOnAction((event) -> {                         //Delete a Product
            Product product = productsTV.getSelectionModel().getSelectedItem();
            if(product == null){
                MFnotifier.setText("Select a Product to Delete");
            }else if(deleteProduct(product)){
                MFnotifier.setText("Product Deleted Successfully!");
                productsTV.setItems(getAllProducts());
                productsTF.clear();
                productsTV.getSelectionModel().clearSelection();
                saved = false;
            } else {
                MFnotifier.setText("Unable to Delete Product With Associated Parts!");
            }
        });

        productPartSearchTF.setOnAction((event) ->{                                 //Search for a Part in Products
            productsErrorMessage.setText("");
            partLookupTV.getSelectionModel().clearSelection();
            boolean searchById = true;
            ObservableList<Part> lookedUpItems;
            try{
                Integer.parseInt(productPartSearchTF.getText());
            }catch (NumberFormatException nfe){
                searchById = false;
            }
            if(searchById){                                             //Search by ID
                if(Integer.parseInt(productPartSearchTF.getText()) >= 0 && Integer.parseInt(productPartSearchTF.getText()) <= allParts.size()) {
                    lookedUpItems = FXCollections.observableArrayList();
                    Part part = lookupPart(Integer.parseInt(productPartSearchTF.getText()) - 1);
                    if (part == null) {
                        productsErrorMessage.setText("No part Found With That ID!");
                    }
                    lookedUpItems.add(part);
                    partLookupTV.setItems(lookedUpItems);
                    partLookupTV.getSelectionModel().select(lookedUpItems.get(0));
                }else{
                    productsErrorMessage.setText("No Part Found With That ID!");
                }
            }else {                                                     //Search by Name
                lookedUpItems = lookupPart(productPartSearchTF.getText());
                if(lookedUpItems.size() == 0){
                    productsErrorMessage.setText("No Part Found With That Name");
                }
                partLookupTV.setItems(lookedUpItems);
                if (lookedUpItems.size() == 1) {
                    partLookupTV.getSelectionModel().select(lookedUpItems.get(0));

                }
                if (lookedUpItems.size() == 1 && productPartSearchTF.getText().equals("")){
                    partLookupTV.getSelectionModel().clearSelection();
                }
            }
        });

        addPartButton.setOnAction((event) -> {                          //Add a Part to a Product

            productsErrorMessage.setText("");
            Part part = partLookupTV.getSelectionModel().getSelectedItem();
            if(part == null){
                productsErrorMessage.setText("Select a Part to Add!");
            }else{

                associatedPartsTV.getItems().add(part);
                productsErrorMessage.setText("Part Added Successfully!");
            }
        });

        removePartButton.setOnAction((event) -> {                       //Remove a Part from a Product

            productsErrorMessage.setText("");
            Part part = associatedPartsTV.getSelectionModel().getSelectedItem();
            if(part == null){
                productsErrorMessage.setText("Select a Part to Remove!");
            }else{
                associatedPartsTV.getItems().remove(part);
                productsErrorMessage.setText("Part Removed Successfully");
            }
        });

        productsSaveButton.setOnAction((event) -> {                     //Save Added/Modified Product

            boolean dataGood = true;
            boolean maxCheck = true;
            boolean minCheck = true;
            boolean inventoryCheck = true;

            productsErrorMessage.setText("");
            //Add all these checks to add product method below
            if(productsNameTF.getText().isEmpty()){
                productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Product Name Required");
                dataGood = false;
            }
            try{
                Integer.parseInt(productsInvTF.getText());
            }catch (NumberFormatException nfe){
                productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Invalid Input: Inventory");
                dataGood = false;
                inventoryCheck = false;
            }
            try{
                Double.parseDouble(productsPriceTF.getText());
            }catch (NumberFormatException nfe){
                productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Invalid Input: Price");
                dataGood = false;
            }
            try{
                Integer.parseInt(productsMaxTF.getText());
            }catch (NumberFormatException nfe){
                productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Invalid Input: Max");
                dataGood = false;
                maxCheck = false;
            }
            try {
                Integer.parseInt(productsMinTF.getText());
            }catch (NumberFormatException nfe){
                productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Invalid Input: Min");
                dataGood = false;
                minCheck = false;
            }
            if(maxCheck && minCheck && inventoryCheck) {
                if (!(Integer.parseInt(productsInvTF.getText()) <= Integer.parseInt(productsMaxTF.getText()) &&
                        Integer.parseInt(productsInvTF.getText()) >= Integer.parseInt(productsMinTF.getText()))) {
                    productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Inventory Level Must Be Between Min and Max");
                    dataGood = false;
                }
            }
            if(maxCheck && minCheck) {
                if (!(Integer.parseInt(productsMinTF.getText()) <= Integer.parseInt(productsMaxTF.getText()))) {
                    productsErrorMessage.setText(productsErrorMessage.getText() + "\n\n Min Cannot Be Greater Than Max");
                    dataGood = false;
                }
            }


            if(dataGood) {
                Product product;

                //If adding a product
                if(Integer.parseInt(productsIdTF.getText()) == allProducts.size() + 1){
                    product = new Product(Integer.parseInt(productsIdTF.getText()), productsNameTF.getText(), Double.parseDouble(productsPriceTF.getText()),
                            Integer.parseInt(productsInvTF.getText()), Integer.parseInt(productsMinTF.getText()),
                            Integer.parseInt(productsMaxTF.getText()));

                    addProduct(product);

                    //Add all associated Parts
                    for(Part part : associatedPartsTV.getItems()){
                        product.addAssociatedPart(part);
                    }
                    MFnotifier.setText("Product Added Successfully!");


                } else {                                                      //If modifying a product

                    product = new Product(Integer.parseInt(productsIdTF.getText()), productsNameTF.getText(), Double.parseDouble(productsPriceTF.getText()),
                            Integer.parseInt(productsInvTF.getText()), Integer.parseInt(productsMinTF.getText()),
                            Integer.parseInt(productsMaxTF.getText()));
                    for(Part part : associatedPartsTV.getItems()){
                        product.addAssociatedPart(part);
                    }
                    updateProduct(product.getId()-1, product);

                    MFnotifier.setText("Product Modified Successfully!");
                }
                productsTV.getSelectionModel().clearSelection();
                productsTF.clear();
                productsTV.setItems(getAllProducts());
                productsTV.refresh();
                saved = false;
                stage.setScene(openingScene);
            }

        });

        productsCancelButton.setOnAction((event) -> {                   //Exit Add/Modify Product

            MFnotifier.setText("");
            productsTV.getSelectionModel().clearSelection();
            productsTF.clear();
            partsTF.clear();
            productsTV.setItems(getAllProducts());
            stage.setScene(openingScene);
        });

        stage.setMinHeight(300);
        stage.show();

    }



//===================== Methods ========================================================================================



    /**
     * This method adds a Part to the Parts list
     *
     * @param newPart the part to add
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * This method adds a Product to the Products list
     *
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * This method searches for a Part by index in a list
     *
     * @param partId the part id to look up
     * @return the part
     */
    public static Part lookupPart(int partId){
        return allParts.get(partId);

    }

    /**
     * This method searches for a Product by index in a list
     *
     * @param productId the product id to look up
     * @return the product
     */
    public static Product lookupProduct(int productId){
        return allProducts.get(productId);                          //Retrieve product by index

    }

    /**
     * RUNTIME ERROR
     * This method was resulting in an error after any parts had been deleted (deletion sets the index to null)
     * A null value cannot call the getName() method and therefore the error occurred.  The fix for this was to
     * modify and utilize the getAllParts() method to only return non-null values.  In this way parts may be deleted
     * and this method will still function by only searching within a list of existent parts.
     *
     * This method searches all Parts for ones containing a match to a given substring and
     * returns all matches in a list
     *
     * @param partName the part name to look up
     * @return the list with the part
     */

    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        for(Part part : getAllParts()){
            if(part.getName().contains(partName)){
                matchingParts.add(part);
            }
        }
        return matchingParts;
    }

    /**
     * This method searches all Products for ones containing a match to a given substring and
     * returns all matches in a list
     *
     * @param productName the product name to look up
     * @return the list with the product
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        for(Product product : getAllProducts()){
            if(product.getName().contains(productName)){
                matchingProducts.add(product);
            }
        }
        return matchingProducts;
    }

    /**
     * This method replaces a Part at the specified index with the provided Part
     *
     * @param index the index of the part to update
     * @param selectedPart the part to update
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * This method replaces a Product at the specified index with the provided Product
     *
     * @param index the index of the product to update
     * @param newProduct the product to update
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * This Method sets the index of the chosen part to null, removing it from the list
     *
     * @param selectedPart the part to be deleted
     * @return the part was deleted successfully
     */


    public static boolean deletePart(Part selectedPart){
        //Future Enhancement--Don't allow deletion of part that is part of product, delete from product manually first?
        //Future Enhancement--Auto-Delete associated parts from product
        //associated parts can exist independent of actual parts inventory
        for(Product product : getAllProducts()){
            for(Part part : product.getAllAssociatedParts()){
                if(part.equals(selectedPart)){
                    product.deleteAssociatedPart(part);
                    break;
                }
            }
        }
        allParts.set(selectedPart.getId()-1, null);
        return true;

    }

    /**
     * This Method sets the index of the chosen product to null only if
     * it has no associated parts, removing it from the list.
     *
     * @param selectedProduct the product to be deleted
     * @return the product was deleted successfully
     */

    public static boolean deleteProduct(Product selectedProduct){
        if(selectedProduct.getAllAssociatedParts().size() != 0){
            return false;
        }else{
            allProducts.set(selectedProduct.getId() - 1, null);
            return true;
        }
    }

    /**
     * This method returns a list of all non-null Parts
     *
     * @return the list of all the parts
     */

    public static ObservableList<Part> getAllParts(){
        ObservableList<Part> nonNullParts = FXCollections.observableArrayList();
        for(int x = 0; x < allParts.size(); x++){
            if(allParts.get(x) != null){
                nonNullParts.add(allParts.get(x));
            }
        }
        return nonNullParts;
    }

    /**
     * This method returns a list of all non-null Products
     *
     * @return the list of all the products
     */
    public static ObservableList<Product> getAllProducts(){
        ObservableList<Product> nonNullProducts = FXCollections.observableArrayList();
        for(int x = 0; x < allProducts.size(); x++){
            if(allProducts.get(x) != null){
                nonNullProducts.add(allProducts.get(x));
            }
        }
        return nonNullProducts;
    }
}

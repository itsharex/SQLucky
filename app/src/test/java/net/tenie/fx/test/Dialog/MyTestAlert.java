package net.tenie.fx.test.Dialog;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class MyTestAlert extends Dialog<ButtonType> {


   public static enum MyAlertType {
       NONE,
       INFORMATION,
       WARNING,
       CONFIRMATION,
       ERROR
   }



   private WeakReference<DialogPane> dialogPaneRef;

   private boolean installingDefaults = false;
   private boolean hasCustomButtons = false;
   private boolean hasCustomTitle = false;
   private boolean hasCustomHeaderText = false;

   private final InvalidationListener headerTextListener = o -> {
       if (!installingDefaults) hasCustomHeaderText = true;
   };

   private final InvalidationListener titleListener = o -> {
       if (!installingDefaults) hasCustomTitle = true;
   };

   private final ListChangeListener<ButtonType> buttonsListener = change -> {
       if (!installingDefaults) hasCustomButtons = true;
   };


   public MyTestAlert(@NamedArg("alertType") MyAlertType alertType) {
       this(alertType, "");
   }
   public MyTestAlert(@NamedArg("alertType") MyAlertType alertType,
                @NamedArg("contentText") String contentText,
                @NamedArg("buttonTypes") ButtonType... buttons) {
       super();

       final DialogPane dialogPane = getDialogPane();
       dialogPane.setContentText(contentText);
       getDialogPane().getStyleClass().add("alert");

       dialogPaneRef = new WeakReference<>(dialogPane);

       hasCustomButtons = buttons != null && buttons.length > 0;
       if (hasCustomButtons) {
           for (ButtonType btnType : buttons) {
               dialogPane.getButtonTypes().addAll(btnType);
           }
       }

       setAlertType(alertType);

       // listening to property changes on Dialog and DialogPane
       dialogPaneProperty().addListener(o -> updateListeners());
       titleProperty().addListener(titleListener);
       updateListeners();
   }


   private final ObjectProperty<MyAlertType> alertType = new SimpleObjectProperty<MyAlertType>(null) {
       final String[] styleClasses = new String[] { "information", "warning", "error", "confirmation" };

       @Override
       protected void invalidated() {
           String newTitle = "";
           String newHeader = "";
//           Node newGraphic = null;
           String styleClass = "";
           ButtonType[] newButtons = new ButtonType[] { ButtonType.OK };
           switch (getAlertType()) {
               case NONE: {
                   newButtons = new ButtonType[] { };
                   break;
               }
               case INFORMATION: {
                   styleClass = "information";
                   break;
               }
               case WARNING: {
                   styleClass = "warning";
                   break;
               }
               case ERROR: {
                   styleClass = "error";
                   break;
               }
               case CONFIRMATION: {
                   styleClass = "confirmation";
                   newButtons = new ButtonType[] { ButtonType.OK, ButtonType.CANCEL };
                   break;
               }
           }

           installingDefaults = true;
           if (!hasCustomTitle) setTitle(newTitle);
           if (!hasCustomHeaderText) setHeaderText(newHeader);
           if (!hasCustomButtons) getButtonTypes().setAll(newButtons);

           DialogPane dialogPane = getDialogPane();
           if (dialogPane != null) {
               List<String> toRemove = new ArrayList<>(Arrays.asList(styleClasses));
               toRemove.remove(styleClass);
               dialogPane.getStyleClass().removeAll(toRemove);
               if (! dialogPane.getStyleClass().contains(styleClass)) {
                   dialogPane.getStyleClass().add(styleClass);
               }
           }

           installingDefaults = false;
       }
   };

   public final MyAlertType getAlertType() {
       return alertType.get();
   }

   public final void setAlertType(MyAlertType alertType) {
       this.alertType.setValue(alertType);
   }

   public final ObjectProperty<MyAlertType> alertTypeProperty() {
       return alertType;
   }


   public final ObservableList<ButtonType> getButtonTypes() {
       return getDialogPane().getButtonTypes();
   }

   private void updateListeners() {
       DialogPane oldPane = dialogPaneRef.get();

       if (oldPane != null) {
           oldPane.headerTextProperty().removeListener(headerTextListener);
           oldPane.getButtonTypes().removeListener(buttonsListener);
       }


       DialogPane newPane = getDialogPane();
       if (newPane != null) {
           newPane.headerTextProperty().addListener(headerTextListener);
           newPane.getButtonTypes().addListener(buttonsListener);
       }

       dialogPaneRef = new WeakReference<DialogPane>(newPane);
   }
}

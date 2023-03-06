package net.tenie.fx.test.Dialog;


import java.net.URL;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

abstract class FXDialog {


    protected Object owner;


    protected FXDialog() {
    }



    /* ************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    public boolean requestPermissionToClose(final MyDialog<?> dialog) {
        // We only allow the dialog to be closed abnormally (i.e. via the X button)
        // when there is a cancel button in the dialog, or when there is only
        // one button in the dialog. In all other cases, we disable the ability
        // (as best we can) to close a dialog abnormally.
        boolean denyClose = true;

        // if we are here, the close was abnormal, so we must call close to
        // clean up, if we don't consume the event to cancel closing...
        Pane dialogPane = dialog.getDialogPane();
        if (dialogPane != null) {
        }

        return !denyClose;
    }



    public abstract void show();

    public abstract void showAndWait();
    public abstract void close();

    public abstract void initOwner(Window owner);

    public abstract Window getOwner();

    public abstract void initModality(Modality modality);

    public abstract Modality getModality();

    public abstract ReadOnlyBooleanProperty showingProperty();

    public abstract Window getWindow();

    public abstract void sizeToScene();

    // --- x
    public abstract double getX();
    public abstract void setX(double x);
    public abstract ReadOnlyDoubleProperty xProperty();

    // --- y
    public abstract double getY();
    public abstract void setY(double y);
    public abstract ReadOnlyDoubleProperty yProperty();

    // --- resizable
    abstract BooleanProperty resizableProperty();


    // --- focused
    abstract ReadOnlyBooleanProperty focusedProperty();


    // --- title
    abstract StringProperty titleProperty();

    // --- content
    public abstract void setDialogPane(Pane node);

    // --- root
    public abstract Node getRoot();


    // --- width
    /**
     * Property representing the width of the dialog.
     */
    abstract ReadOnlyDoubleProperty widthProperty();

    abstract void setWidth(double width);


    // --- height
    /**
     * Property representing the height of the dialog.
     */
    abstract ReadOnlyDoubleProperty heightProperty();

    abstract void setHeight(double height);


    // stage style
    abstract void initStyle(StageStyle style);
    abstract StageStyle getStyle();


    abstract double getSceneHeight();

    /* *************************************************************************
     *
     * Implementation
     *
     **************************************************************************/




    /* *************************************************************************
     *
     * Support Classes
     *
     **************************************************************************/



    /* *************************************************************************
     *
     * Stylesheet Handling
     *
     **************************************************************************/
}

